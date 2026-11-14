# ARCHITECTURE.md — 架构说明

> Wild Wind 多模块 NeoForge 模组的技术架构。设计动机见根目录 [DESIGN.md](../DESIGN.md)，模块边界细则见 [BOUNDARY.md](./BOUNDARY.md)。

## 1. 总览

```
WildWindModule/
├── build.gradle.kts        # 根构建脚本：allprojects 统一配置 moddev 插件
├── settings.gradle.kts     # include core / wilderness / barren（projectDir → modules/<名>）
│                           #   与 imp / imp-annotations / imp-processor（根目录标准布局）
├── gradle.properties       # 版本标量 targetMcVersion（CI 用 -P 覆盖，不回改文件）
├── src/
│   ├── templates/          # neoforge.mods.toml 模板（generatedMetadata 任务 expand）
│   ├── core/               # ww_core：java/ + resources/
│   ├── wilderness/         # ww_wilderness
│   └── barren/             # ww_barren
├── imp/                    # 版本中间层（ADR-0003）：唯二允许 import MC/NeoForge 的地方（另一个是各模块自身）
├── imp-annotations/        # @Since / @Until / @ParamSince（SOURCE 保留策略）
├── imp-processor/          # APT 处理器：生成 ImpManifest 常量类
├── modules/<模块>/         # Gradle projectDir（占位，不承载源码）
├── textures_generator/     # 贴图生成 PowerShell 脚本
└── build/generated/<模块>/{client,server}   # datagen 输出，打包时作为 resources srcDir
```

- 工具链：Minecraft 26.2、NeoForge 26.2.0.75（moddev 插件 2.x）、Java 25、GeckoLib 5.5.4、Lombok（compileOnly）。
- 每个子项目独立 jar，产物名带目标版本（`ww_core-mc26.2-1.0.0.jar`）；`neoforge.mods.toml` 由 `generatedMetadata` 任务从 `src/templates` 展开生成，非 core 模块自动注入对 `ww_core` 的 required/AFTER 依赖。
- `imp` 不是 mod（无 `@Mod` 主类、无 mods.toml、无 run 配置），因此其字节码在 jar 阶段被合并进三个 mod 产物（根脚本 `tasks.named<Jar>("jar")`），否则发布后运行期 `NoClassDefFoundError`。

## 2. 各模块结构（以 core 为例，另两个模块同构）

| 包 | 职责 |
|---|---|
| `WildWindCore` | `@Mod` 主类，注册总线入口 |
| `registry/` | ModItems / ModBlocks / ModEntities / ModDataComponents / ModSoundEvents / ModBlockEntities |
| `block/` | 方块实现（FletchingTableBlock）、`WoodFamily` / `ModWoodFamilies` 聚合 |
| `block/entity/`、`menu/` | 制箭台方块实体与菜单 |
| `arrow/` | 矢术：ArrowFletching / ArrowShaft / ArrowHead / ArrowEffects / ArrowComponent |
| `entity/` | Spiderling、`WindupAttackMob` 接口与 AI goal（ChargingMeleeAttackGoal 等） |
| `client/` | Screen/Tooltip/渲染器/RenderState/GeckoLib 模型 |
| `datagen/` | 各 Provider（模型/战利品/标签/配方/lang/DataMap），入口 `CoreDatagen` |
| `worldgen/` | ConfiguredFeatures / PlacedFeatures / TreeGrowers / BiomeModifiers |
| `mixin/` | AbstractArrowMixin、BlockItemMixin 等，登记于 `ww_core.mixins.json` |
| `config/` | ModCommonConfig（如 `worldgen.replaceVanillaFletchingTable`） |
| `event/` | 游戏事件处理器（区块加载替换制箭台等） |
| `groups/` | 创造模式物品栏 |

## 3. 关键数据流

### 3.1 资源生成流

```
Provider（src/<模块>/java/datagen）
  → runClientData / runServerData（--output build/generated/<模块>/client|server）
  → build.gradle.kts 将 generated 目录作为 resources srcDir
  → processResources 打包进 jar
```

手写资源（贴图、GeckoLib json）只放 `src/<模块>/resources`；两侧同名时以 build/generated 先注册者优先（EXCLUDE 策略）。

### 3.2 矢术效果流

```
玩家在制箭台组合 箭羽+箭杆+箭头
  → FletchingTableBlockEntity.updateResult 产出带 ArrowComponent 的箭物品（×4）
  → 射出后 AbstractArrowMixin 每读 pickupItemStack 中的组件：
      出手期：箭羽改 power/uncertainty，箭杆改重力/火尾
      命中期：ArrowEffects 应用箭头效果（爆炸/点燃/传送/放置方块…）
      回收期：isNotRecoverable 则 discard
```

### 3.3 木质内容流（新增一个木种）

```
定义 BlockSetType + WoodType（名称必须带命名空间）
  → 构造 WoodFamily 并登记进 ModWoodFamilies.ALL
  → 自动获得：方块模型/物品模型、方块战利品、logs/planks 等 block+item 标签、
             furnace_fuels 燃料、stripping 数据地图、堆肥、创造栏 appendWoodSet()
  → 手写：贴图 png；如需船/告示牌：实体注册 + 贴图（复用原版类型）
```

### 3.4 版本差异流（imp 中间层，ADR-0003）

**主导机制是源码预处理**：构建时先把 imp 源码按目标版本改写一遍，再交给 javac。

```
gradle.properties: targetMcVersion=26.2      （或 -PtargetMcVersion=1.21.1 覆盖）
  │
  ├─ 依赖/工具链选择：各族对应自己的 NeoForge（1.20.4→20.4.253 / 1.21.1→21.1.209 / 26.2→26.2.0.75）
  │                   Java 级别：<1.20.5→17 / 1.20.5~26→21 / >=26→25（JDK 25 + --release 降级）
  │
  ├─ preprocessImpSources（JavaExec → SourcePreprocessor，基于 JavaParser）
  │    1. 删除该版本不存在的成员（@Since / @Until，含 until 区间与 exclude 空洞）
  │    2. 应用 @Name：声明与调用点一起改名
  │    3. 应用外部改名表 gradle/imp-renames.txt（MC 自身跨版本改名）
  │    → 输出 build/preprocessed/java
  │
  ├─ javac 编译预处理后的源码（此时只剩该版本真实存在的代码）
  │
  ├─ imp-processor（APT）仍生成 ImpManifest 布尔常量
  │    → 仅用于"同一成员在某版本多/少一个参数"这类参数差异的运行时分派
  │
  └─ jar 阶段：imp 字节码合并进 core / wilderness / barren 产物
```

**为什么必须预处理而不是纯 APT**：注解处理器只能生成新类，**删不掉既有成员、也改不了名字**。
而"所有版本写在同一个类里"要求把不适用该版本的代码真的抹掉，否则两套分叉 API 会同时
参与编译（如 1.21 的 `ResourceLocation` 与 26.x 的 `Identifier` 不可能共存）。

**单一类里的书写形态**（`ImpIds` 的真实结构）：

```java
@Since("26.1")                  @Name("id") Identifier     idModern(...) { Identifier.parse(...) }
@Since("1.21") @Until("26.1")   @Name("id") ResourceLocation idTen21(...) { ResourceLocation.fromNamespaceAndPath(...) }
@Until("1.21")                  @Name("id") ResourceLocation idTen20(...) { new ResourceLocation(...) }
```

三个产物里的方法都叫 `id`，源码里却是三段可读的独立实现。

**返回类型本身也可以分叉**：上例中 26.x 的声明返回 `Identifier`、旧族返回 `ResourceLocation`，
预处理器每次只保留一个，因此不存在"同名方法返回不同类型"的冲突。调用方用 `var` 接住，
或在需要字符串时改用 `ImpIds.ofString(...)`（返回 `String`，跨版本稳定）。

**`import` 的处理约定**：Java 不允许注解修饰 import，因此**版本分叉的类一律不 import**，
在成员内用全限定名。被裁掉的成员连带其类型引用一起消失，无需条件编译导入。

注解与指令一览：

| 写法 | 作用 | 由谁执行 |
|---|---|---|
| `@Since("26.1")` / `@Until("26.1")` | 成员存废 | 预处理器（删除） |
| `@Since(value="1.21", until="26.1")` | 区间存在 | 预处理器（删除） |
| `@Since(value="1.21", exclude={"1.21.4"})` | 区间内特定版本缺失 | 预处理器（删除） |
| `@Name("id")` | 符号改名（声明 + 调用点） | 预处理器（改写） |
| `gradle/imp-renames.txt` | MC 自身符号改名（`族 => 旧名 => 新名`） | 预处理器（改写，按简单名匹配） |
| `@ParamSince("26.2")` | 参数差异 | APT 生成常量 + 重载分派 |

边界守卫（`checkImpBoundary`，规则见 `gradle/imp-boundary.txt`）：

- 构建某族时禁止内容模块使用该族不存在的分叉 API（如 1.20/1.21 族下出现 `Identifier`）；
- 禁止内容模块使用版本注解——注解只能是 imp 内的收敛点。

已知限制（诚实记录）：

- `@Name` 的源码内改名按简单名匹配，不做符号解析；同一旧名映射到不同新名会直接报错而非猜测。
- 外部改名表同样按简单名匹配，同名不同类的符号会被一起改掉，加规则前需确认名字在仓库内无歧义。
- 单类形态下 IDE 只能对一个版本显示为绿（其它版本的引用会标红），这是"所有版本共处一个文件"的必然代价。

## 4. 跨模块依赖

- `wilderness`、`barren`：`implementation project(":core")`（编译期）+ mods.toml required 依赖（运行期）。
- `barren` 开发期额外 `localRuntime project(":wilderness")` 联调，不进发布 jar。
- 共享代码（WoodFamily、WindupAttackMob、制箭台）只允许放 core，细则见 [BOUNDARY.md](./BOUNDARY.md)。
- `imp`：三个 mod 模块 `api(project(":imp"))`；**imp 不依赖任何 mod 模块**（单向）。只有 imp 内部允许 import `net.minecraft.*`。

## 5. 运行与调试

- `./gradlew :<模块>:runClient|runServer|runClientData|runServerData`；运行目录在 `build/runs/<模块>/…`（仓库根的 `run/` 是历史运行区，不提交）。
- datagen 输出验证：generated 目录不入库（见 [BOUNDARY.md](./BOUNDARY.md) §5），连跑两套 datagen 对比两次输出是否一致。
- 版本矩阵：`./gradlew build -PtargetMcVersion=26.1`，与默认构建分别产出 `*-mc26.1-1.0.0.jar` / `*-mc26.2-1.0.0.jar`。
  - 当前状态：`imp` 已能按族构建（1.20.4 / 1.21.1 / 26.2 三族实测通过）；**内容模块仍只在 26.x 族可编**，因为它们的源码直接使用 26.x 的 API（如 `Identifier`，`checkImpBoundary` 会报出待迁移清单）。
  - 各族依赖与 Java 级别由 `imp/build.gradle.kts` 的 `familyDefaults` / `javaReleaseFor` 决定，可 `-PimpNeoVersion` 覆盖。
- 边界检查：`./gradlew checkImpBoundary -PtargetMcVersion=<版本>`（已挂在各模块 `check` 之前）。
- 版本配对：`./gradlew checkVersionPairing`（同样挂在 `check` 之前）——校验 `targetMcVersion` / `minecraftVersion` / `neoVersion` 属于同一版本族，防止产出"标着 1.21 却按 26.2 编译"的假产物。确需放行用 `-PallowVersionMismatch=true`。
- 版本族单一事实源：`gradle/version-families.properties`（`族=mc|neoForge|java`），根脚本、imp 脚本与 CI 共用。
- CI（`.github/workflows/build.yml`）：
  - `primary`：26.2 全量 `build`（必过），上传三 mod jar + imp jar
  - `imp-matrix`：`1.20.6 / 1.21.1 / 26.2` 三族 `:imp:build`（必过）
  - `content-matrix`：手动触发（`workflow_dispatch` + `full_matrix=true`）构建旧版内容模块，`continue-on-error`，用于观测迁移进度

## 6. 演进记录

重大结构变化以 ADR 记录于 [adr/](./adr/)（如多模块拆分、WoodFamily 聚合、datagen-first 资源策略）。
