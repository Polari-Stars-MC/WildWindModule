# DESIGN.md — 设计文档

> Wild Wind（原野之风）多模块 NeoForge 模组的设计说明。架构细节见 [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)，本文回答"为什么这样设计"。

## 1. 目标

以三个独立发布的模块 jar（core / wilderness / barren）承载"原野之风"系列内容更新，共享一套技术底座，同时允许玩家按模块单独安装。

## 2. 核心设计决策

### 2.1 多模块而非单 jar

- **决策**：一个 Gradle root + 三个子项目，各产出独立 jar，`wilderness` / `barren` 以 `type="required"`、`ordering="AFTER"` 声明依赖 `ww_core`，Gradle 侧 `implementation project(":core")`。
- **理由**：三个内容更新可以独立迭代与发布；只装一个模块时创造栏只出现它自己的页。
- **代价**：跨模块共享代码必须下沉到 core（见 [docs/BOUNDARY.md](docs/BOUNDARY.md)）。

### 2.2 WoodFamily 聚合层

- **决策**：一切木质方块由 `WoodFamily` 记录聚合（原木/去皮/木板/楼梯/台阶/门/活板门/栅栏/告示牌/船…），`ModWoodFamilies.ALL` 是唯一事实来源。
- **理由**：每个木种涉及模型、战利品、标签、燃料数据地图、创造栏、去皮映射等十余处注册，手写参数列表（13~15 个参数）极易遗漏出错；聚合后每类提供器只遍历一次。
- **演进**：新增木种 = 定义 `WoodType` + `BlockSetType` + `WoodFamily` + 贴图，其余全自动。

### 2.3 datagen-first 的资源策略

- **决策**：可由 datagen 重建的资源一律生成，手写资源仅限贴图与 GeckoLib 动画等。
- **理由**：项目曾因 438 个手写 JSON 与 datagen 产物冲突、130 个未注册方块的孤儿资源而大扫除（CHANGE_LOG 2026.10.23）。datagen 是唯一写入路径后，这类污染不会复发。

### 2.4 数据地图优先于事件/Mixin

- **决策**：燃料用 `furnace_fuels` 数据地图、去皮用 DataMaps、堆肥用数据组件，能用注册表数据解决的就不写 Mixin。
- **理由**：数据驱动可被 datagen 生成、可被数据包覆盖、升级 Minecraft 版本时更稳。

### 2.5 制箭台：原版方块实体复用 + 组件化箭矢

- **决策**：制箭台是自有方块 + 4 槽 `Container` 方块实体；箭矢能力由 `ArrowComponent`（数据组件）描述箭羽/箭杆/箭头三层效果，存放在箭矢的 `pickupItemStack` 中随原版存读档，`AbstractArrowMixin` 在飞行与命中时应用效果。
- **理由**：不新增 attachment、不新建实体类型，普通箭零开销走原版逻辑。
- **替换原版制箭台**：26.2 的 `BlockItem#place` 不再触发放置事件，故用 `BlockItemMixin#getPlacementState` 一次性替换（放置校验/音效/统计走原版流程）；旧存档方块由 `FletchingTableReplaceHandler` 在区块加载时替换。

### 2.6 实体复用原版类型

- **决策**：船/告示牌/展示架复用原版 `Boat`、`SIGN`、`SHELF` 等方块实体与实体类型（`BlockEntityTypeAddBlocksEvent` 登记自定义方块），只覆写必要行为（如 `WildWindBoat#canBoatInFluid` 让岩浆可航行）。
- **理由**：不新建 BlockEntityType 可避免大量 client/server 同步样板与兼容问题。

### 2.7 客户端渲染

- 幼蛛用 GeckoLib（`GeoEntity` + `ChargingMeleeAttackGoal` 蓄力扑击）；泥沼蟹/恐鱼用原版实体模型体系（Model/RenderState/Renderer 分层）。
- 枚举编解码统一走 `fromName`（含旧值兼容映射，如 `wind_charge → BREEZE_ROD`），杜绝 STREAM_CODEC 大小写崩溃。

## 3. 命名与视觉规范

- 木材 id：`cinder`（焚烬木，青灰）/ `ember`（灵焰木，橙红）；曾短暂改名 `blaze`/`soul` 已全量回退，**新代码禁止再引入**。
- 中文本地化遵循原版格式：字幕「实体：动作」、可疑沙「可疑的…沙」、运输船 en_us 为 `... Boat with Chest`。
- WIP 内容保留上游 `§4` 红色名标记，发布前统一清理。

## 4. 兼容性承诺

- `ArrowShaft.fromName` 等编解码入口必须对历史值做兼容映射，禁止让旧存档/旧物品崩溃。
- 删除注册项前先检查所有引用它的标签（教训：`minecraft:flower_pots` 引用未注册方块导致 TagLoader 报错，BUG-TG-001）。

## 5. 非目标（Non-goals)

- 不做跨模组 API（第三方扩展）。
- 不追求支持除 NeoForge 之外的加载器。
- 不做 26.2 以下版本回移植。
