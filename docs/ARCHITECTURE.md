# ARCHITECTURE.md — 架构说明

> Wild Wind 多模块 NeoForge 模组的技术架构。设计动机见根目录 [DESIGN.md](../DESIGN.md)，模块边界细则见 [BOUNDARY.md](./BOUNDARY.md)。

## 1. 总览

```
WildWindModule/
├── build.gradle.kts        # 根构建脚本：allprojects 统一配置 moddev 插件
├── settings.gradle.kts     # include core / wilderness / barren，projectDir → modules/<名>
├── src/
│   ├── templates/          # neoforge.mods.toml 模板（generatedMetadata 任务 expand）
│   ├── core/               # ww_core：java/ + resources/
│   ├── wilderness/         # ww_wilderness
│   └── barren/             # ww_barren
├── modules/<模块>/         # Gradle projectDir（占位，不承载源码）
├── textures_generator/     # 贴图生成 PowerShell 脚本
└── build/generated/<模块>/{client,server}   # datagen 输出，打包时作为 resources srcDir
```

- 工具链：Minecraft 26.2、NeoForge 26.2.0.75（moddev 插件 2.x）、Java 25、GeckoLib 5.5.4、Lombok（compileOnly）。
- 每个子项目独立 jar，`neoforge.mods.toml` 由 `generatedMetadata` 任务从 `src/templates` 展开生成，非 core 模块自动注入对 `ww_core` 的 required/AFTER 依赖。

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

## 4. 跨模块依赖

- `wilderness`、`barren`：`implementation project(":core")`（编译期）+ mods.toml required 依赖（运行期）。
- `barren` 开发期额外 `localRuntime project(":wilderness")` 联调，不进发布 jar。
- 共享代码（WoodFamily、WindupAttackMob、制箭台）只允许放 core，细则见 [BOUNDARY.md](./BOUNDARY.md)。

## 5. 运行与调试

- `./gradlew :<模块>:runClient|runServer|runClientData|runServerData`；运行目录在 `build/runs/<模块>/…`（仓库根的 `run/` 是历史运行区，不提交）。
- datagen 输出验证：generated 目录不入库（见 [BOUNDARY.md](./BOUNDARY.md) §5），连跑两套 datagen 对比两次输出是否一致。
- CI：GitHub Actions `build.yml`，JDK 25 temurin + `./gradlew build`。

## 6. 演进记录

重大结构变化以 ADR 记录于 [adr/](./adr/)（如多模块拆分、WoodFamily 聚合、datagen-first 资源策略）。
