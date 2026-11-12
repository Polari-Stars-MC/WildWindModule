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

## 调试与排错参考

- 历史踩坑（槽位错乱、STREAM_CODEC 枚举大小写、TagLoader 缺引用、标签误加 `needs_stone_tool` 等）全部记录在 [CHANGE_LOG.MD](./CHANGE_LOG.MD) 的 Fixed 小节，动手修 bug 前先搜一下。
- 新增方块后客户端显示黑紫 missingno 是预期行为（`MissingModels` 引用自身贴图路径），补 png 即可，不要改模型引用。
- 构建问题优先看 Gradle 9 资源重复（`duplicatesStrategy` 已配置 EXCLUDE）与 configuration-cache 报告。

## 提交与证据

- 功能性改动建议在 `docs/evidence/` 留验证记录（见 [docs/evidence/README.md](./docs/evidence/README.md)）。
- 影响架构或模块边界的决策走 ADR（见 [docs/adr/](./docs/adr/)）。
- 路线图与待办在 [docs/ROADMAP.md](./docs/ROADMAP.md)。
