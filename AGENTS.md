# AGENTS.md — AI 协作与开发规范

面向在本仓库工作的 AI 助手与新加入的贡献者。先读 [README.md](./README.md)，再按需查 [docs/README.md](./docs/README.md) 的文档索引。

## 项目是什么

**Wild Wind（原野之风）**：一个 NeoForge 多模块 Minecraft 模组（Minecraft 26.2 / NeoForge 26.2.0.75 / Java 25 / Gradle 9），拆分为三个独立 jar：

| Gradle 子项目 | modid | 内容 |
|---|---|---|
| `core` | `ww_core` | 杜鹃木套件、玫瑰/灵魂玫瑰、制箭台与矢术、幼蛛 |
| `wilderness` | `ww_wilderness` | 泥沼蟹、恐鱼、蟹钳系列、延展药水、灵魂南瓜灯、沼泽方块 |
| `barren` | `ww_barren` | 焚烬木/灵焰木全套、砂岩/沙/流沙、焦土、仙人球、船与告示牌 |

源码位于 `src/<模块>/java`（Gradle 的 projectDir 指向 `modules/<模块>`，该目录只是 Gradle 占位）。

## 硬性规则（违反即回退）

1. **datagen 产物不手写**。blockstates、models、items、lang、tags、loot_table、data/** 一律由 `runClientData` / `runServerData` 生成到 `build/generated/<模块>/{client,server}`。不要往 `src/<模块>/resources` 提交与 datagen 输出同名的文件（历史教训见 CHANGE_LOG 2026.10.23 的资源去重条目）。
2. **不要把 `.cache/`（datagen HashCache）提交进 `src`**。它会让 datagen 误判"已生成"而跳过重写。
3. **资源 ID 全小写**，仅 `[a-z0-9/._-]`。大写字母会触发 `IdentifierException` 使 datagen 直接失败。
4. **datagen 无法重建的文件才允许手写**，目前白名单包括：贴图 png（含 `.mcmeta`）、`geckolib/models|animations`、`gui/signs/*.png`、`rotated_top_sandstone.json` 模板类文件。新增白名单条目需在 PR 描述中说明理由。
5. **`WoodType` 名称必须带命名空间**（如 `ww_barren:cinder`），否则告示牌编辑界面会去 `minecraft:` 找 GUI 贴图。
6. **改动跨模块共享的结构（`WoodFamily`、`WindupAttackMob`、制箭台菜单/方块实体）前**，先读 [docs/BOUNDARY.md](./docs/BOUNDARY.md)，这些只允许放在 core。
7. **每个功能改动都更新 [CHANGE_LOG.MD](./CHANGE_LOG.MD)**，按仓库既有的 Keep-a-Changelog 中文格式，修 bug 需带 BUG-XX-NNN 编号与根因描述。
8. **不复制 Mojang 资源进 jar**（如制箭台界面直接引用原版 `smithing.png`）。

## 常用命令

```bash
./gradlew build                     # 构建三个 jar
./gradlew :core:runClient           # 只跑 core
./gradlew :barren:runClient         # 跑 barren（自动额外加载 wilderness 联调）
./gradlew :<模块>:runClientData     # 生成客户端资源（模型/blockstate/lang）
./gradlew :<模块>:runServerData     # 生成服务端数据（loot/tags/recipe）
```

datagen 跑完后检查 `build/generated/<模块>/` 内容符合预期（generated 目录本身**不入库**，见 docs/BOUNDARY.md §5）；`run/` 目录是运行时工作区，不要提交。

## 代码约定

- 包名 `org.polaris2023.wildwind.<模块>.*`，主类 `WildWindCore` / `WildWindWilderness` / `WildWindBarren`。
- 注册器按领域分包：`registry/`（ModItems、ModBlocks…）、`datagen/`、`client/`、`worldgen/`、`mixin/`。
- 木质方块一律走 `WoodFamily` 聚合：模型、战利品、标签、数据地图、创造栏只遍历 `ModWoodFamilies.ALL` 一次，**不要**新写 13~15 参数的手工列表。
- Mixin 命名 `<目标类>Mixin`，登记进对应模块的 `<modid>.mixins.json`；优先用事件/DataMap 而非 Mixin（如燃料用 `furnace_fuels` 数据地图而非事件）。
- 语言文件由 datagen 生成（`ZhEnCombineLangProvider` 等），中文字幕遵循原版格式「实体：动作」。
- 贴图生成脚本放 `textures_generator/*.ps1`，并在 CHANGE_LOG 注明脚本路径，保证可复现。

## imp 中间层（ADR-0003）

**这是本仓库最容易踩错的部分，动版本敏感代码前必读。**

- 目录职责：`imp-annotations`（注解）/ `imp-processor`（源码预处理器 + APT）/ `imp`（适配层本体）。三者都是根目录标准布局，**不**重定向到 `modules/`。
- **只有 `imp` 允许 import `net.minecraft.*` / `net.neoforged.*`**。mod 模块（core/wilderness/barren）要访问版本敏感能力时，走 imp 的版本无关 API。
- **主导机制是源码预处理**，不是注解处理器：`preprocessImpSources` 在编译前按 `targetMcVersion` 删掉不适用的成员、应用 `@Name` 改名，输出 `build/preprocessed/java` 再交给 javac。APT（`ImpManifest`）只保留给"参数差异"这一种场景。
- **所有版本写同一个类**：不适用的实现整块被删掉，所以源码里可以放心使用只属于某版本的 API。

### 写法

```java
@Since("26.1")                          @Name("id")
public static net.minecraft.resources.Identifier idModern(...) { Identifier.parse(...) }

@Since("1.21") @Until("26.1")           @Name("id")
public static net.minecraft.resources.ResourceLocation idTen21(...)  { ResourceLocation.fromNamespaceAndPath(...) }

@Until("1.21")                          @Name("id")
public static net.minecraft.resources.ResourceLocation idTen20(...)  { new ResourceLocation(...) }
```

三个产物里都叫 `id`，源码里是三段独立实现。

| 指令 | 作用 |
|---|---|
| `@Since("26.1")` / `@Until("26.1")` | 成员存废（预处理器删除） |
| `@Since(value="1.21", until="26.1")` | 区间存在 |
| `@Since(value="1.21", exclude={"1.21.4"})` | 区间内特定版本缺失（中途移除又恢复） |
| `@Name("id")` | 符号改名：声明 + 调用点 |
| `@ParamSince("26.2")` | 参数差异：宿主方法**必须**有同名同返回值的回退重载，否则编译失败 |

**Minecraft 自身符号的改名**走外部表 `gradle/imp-renames.txt`（`族 => 旧名 => 新名 => 说明`，按简单名匹配，不做类型解析）。

### 两条硬约定

1. **版本分叉的类一律不写 import**，在成员内用全限定名——Java 不允许注解修饰 `import`，被删掉的成员会连带其类型引用一起消失。
2. **不要用 `if (ImpManifest.HAS_X)` 做成员级分流**——那是旧模式；成员级差异交给删除，只有参数差异才用常量分派。

### 返回类型也可以版本分叉

imp 的 API **可以**直接返回 MC 原生类型（如 `Identifier` / `ResourceLocation`）：每个版本的声明各有自己的返回类型，预处理器只留一个，因此不冲突。

```java
@Since("26.1")                @Name("id") public static net.minecraft.resources.Identifier idModern(...)
@Until("26.1")                @Name("id") public static net.minecraft.resources.ResourceLocation idTen20(...)
```

调用方约定：

- 要喂给 MC API → 用原生返回版本，**必须用 `var` 接**（`var id = ImpIds.of("sample");`）。
- **禁止在内容模块里显式写 `Identifier` / `ResourceLocation`** —— 那等于把版本分叉泄漏到上层，1.21 构建会直接失败。
- 要存字段/写配置/比较字符串 → 用 `ImpIds.ofString(...)` 这类 `String` 版本（跨版本稳定）。
- `var` 只能用于局部变量，不能用于字段与返回类型；需要分叉返回类型的方法必须按版本各写一份。

### 版本族（依赖与工具链）

| 族 | 默认 MC | 默认 NeoForge | Java |
|---|---|---|---|
| `1.20` | 1.20.4 | 20.4.253 | 17 |
| `1.21` | 1.21.1 | 21.1.209 | 21 |
| `26.x` | 26.2 | 26.2.0.75 | 25 |

```bash
./gradlew :imp:build -PtargetMcVersion=1.21.1      # 编 1.21 族
./gradlew :imp:printImpVariant -PtargetMcVersion=1.20.4   # 查看族/依赖/Java 级别
./gradlew checkImpBoundary -PtargetMcVersion=1.21.1      # 校验内容模块未越过 imp 边界
```

要点：

- 族由 `targetMcVersion` 推导；未知族直接构建失败（`familyDefaults` 表在 `imp/build.gradle.kts`）。
- Java 级别按 MC 版本推导：`<1.20.5`→17、`1.20.5~26`→21、`>=26`→25；工具链固定 JDK 25，靠 `options.release` 降级。
- **1.20 族锚定 1.20.4 而非 1.20.2**：NeoForge 20.2.x 没有发布 moddev 需要的 `neoforge-moddev-bundle` capability，无法解析。
- 边界规则在 `gradle/imp-boundary.txt`，格式 `族列表 => 正则 => 说明`（用 `=>` 因为正则常用 `|`）。新增分叉 API 时**先加规则再迁移代码**。
- 已知限制：单类形态下 IDE 只能对一个版本显示为绿；`@Name` 与外部改名表都按简单名匹配，同名不同类会被一起改。
- 现状：内容模块尚未按族迁移，`1.20`/`1.21` 族目前只有 `imp` 能编；`checkImpBoundary` 在 1.21 族下会报出 17 处 `Identifier` 违规，这是待迁移清单，不是误报。

## 调试与排错参考

- 历史踩坑（槽位错乱、STREAM_CODEC 枚举大小写、TagLoader 缺引用、标签误加 `needs_stone_tool` 等）全部记录在 [CHANGE_LOG.MD](./CHANGE_LOG.MD) 的 Fixed 小节，动手修 bug 前先搜一下。
- 新增方块后客户端显示黑紫 missingno 是预期行为（`MissingModels` 引用自身贴图路径），补 png 即可，不要改模型引用。
- 构建问题优先看 Gradle 9 资源重复（`duplicatesStrategy` 已配置 EXCLUDE）与 configuration-cache 报告。

## 提交与证据

- 功能性改动建议在 `docs/evidence/` 留验证记录（见 [docs/evidence/README.md](./docs/evidence/README.md)）。
- 影响架构或模块边界的决策走 ADR（见 [docs/adr/](./docs/adr/)）。
- 路线图与待办在 [docs/ROADMAP.md](./docs/ROADMAP.md)。
