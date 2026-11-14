# 模型规格化迁移（ADR-0005 第二类）（2026-11-13）

## 为什么模型是最难的一类

实测两代 API 完全不同（用 `javap` 读 1.21.1 的 jar）：

| 能力 | 26.x（原版新式） | 1.20 / 1.21（NeoForge Forge 风格） |
|---|---|---|
| Provider | `net.minecraft.client.data.models.ModelProvider`，钩子 `registerModels(BlockModelGenerators, ItemModelGenerators)` | 无该钩子；用 `client/model/generators/BlockStateProvider`，钩子 `registerStatesAndModels()` |
| 木材 | `blocks.woodProvider(log).logWithHorizontal().wood()` | 无等价物，需 `logBlock` / `axisBlock` 逐条写 |
| 方块族 | `blocks.family(planks).stairs().slab().fence()...` | 无等价物，需 `stairsBlock` / `slabBlock` / `fenceBlock` … |
| 门/活板门 | `blocks.createDoor` / `createTrapdoor`（public） | `createDoor` / `createTrapdoor` 在 1.21.1 是**包私有**，只能用 `BlockStateProvider.doorBlock` |
| 树叶 | `blocks.createTintedLeaves(block, TexturedModel.LEAVES, tint)` | 无等价物（需自行拼 tinted 模型） |
| 物品 | `ItemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM)` | `ItemModelProvider.basicItem` / `spawnEggItem` |

结论：**不能靠同一套调用跨版本**，只能"规格 + 两套发射分支"。

## 实现

- `imp/data/ImpModelSpec`：sealed 接口 + 6 个变体
  - `WoodSet`（原木/木头/去皮/木板/楼梯/台阶/栅栏/栅栏门/压力板/按钮/门/活板门/树叶 + 树叶色）
  - `Cross`、`DoublePlant`、`BuiltinDoublePlant`、`VanillaLike`、`FlatItem`
- `imp/data/ImpModelWriter`：`apply(specs, Generators)`，每个变体有 `@Since("26.1")` 与 `@Until("26.1")` 两份实现，`@Name` 统一方法名
  - 26.x 分支：按原实现逐一调用 `BlockModelGenerators` / `ItemModelGenerators`
  - 旧版分支：**显式抛 `UnsupportedOperationException`**（见下方"未实现"说明）
- `core/data/ModModelSpecs`：从 `ModWoodFamilies` / `ModBlocks` / `ModItems` 生成规格（纯数据、零 MC 类型）
- `ModModelProvider`：只剩把两个生成器对象交出去

## 验证：逐字节 diff

| 阶段 | 结果 |
|---|---|
| 首次 diff | **差异 8 条** —— `soul_rose_bush` 相关 |
| 定位 | 26.x 内置 `createDoublePlantWithDefaultItem` 生成 `_bottom`/`_top` **两个**模型；而 `MissingModels.doublePlant` 是**单模型**实现。原 Provider 对两个方块用了不同做法 |
| 修法 | 规格里区分 `BuiltinDoublePlant`（内置助手）与 `DoublePlant`（自定义单模型），与原实现一一对应 |
| 再次 diff | ✅ **无差异（154 个文件哈希一致）** |

这次差异是 diff 机制价值的直接证明：如果只靠"看起来等价"，`soul_rose_bush` 的模型会静默变成另一种形状。

## 未实现：旧版模型发射分支

旧版分支目前抛异常，**这是刻意选择**：

- 旧版没有等价的木材助手，需要把 `WoodSet` 逐条映射到 `BlockStateProvider` 的
  `logBlock` / `stairsBlock` / `slabBlock` / `fenceBlock` / `fenceGateBlock` /
  `pressurePlateBlock` / `buttonBlock` / `doorBlock` / `trapdoorBlock`，
  并自行推导每个方块的贴图路径；
- 内容模块目前**还不能为旧版编译**，因此这段代码即使写出来也**无法做运行时验证**；
- 与其交付一份"能编译但没人跑过、可能悄悄产出错误模型"的实现，不如显式失败并在消息里指明待办。

待办：内容模块支持旧版编译后，按"基线 → 实现旧版分支 → 跑 1.21 datagen → 逐字节/语义 diff"完成。

## 现状汇总（ADR-0005 进度）

| 类别 | 26.x 分支 | 旧版分支 |
|---|---|---|
| tags（block/item/entity） | ✅ 已实现 + diff 零差异 | ✅ 已实现（编译验证） |
| models | ✅ 已实现 + diff 零差异 | ⛔ 显式未实现（见上） |
| loot / recipes / datapacks / datamaps | ⛔ 未开始 | ⛔ 未开始 |
