# ADR-0005 — datagen 用中间层兼容：规格 / 发射器 / 薄适配三层

- 状态：已接受（机制已实现并在三族编译验证；各 Provider 的规格化迁移为增量工作）
- 关联：[ADR-0004](./ADR-0004-source-preprocessing-and-name.md) · [ARCHITECTURE.md](../ARCHITECTURE.md) §3.4 · [evidence/2026-11-13-datagen-fork](../../evidence/2026-11-13-datagen-fork/README.md)

## 背景

datagen 是版本差异最重的一块。实测（探针编译）：

| API | 26.2 | 1.21.1 | 1.20.6 |
|---|---|---|---|
| `net.minecraft.client.data.models.ModelProvider` | ✅ | ❌ 包不存在 | ❌ 包不存在 |
| `net.minecraft.data.models.ModelProvider` | ❌ | ✅ | ✅ |
| `net.minecraft.data.tags.TagAppender`（按 ResourceKey 追加） | ✅ | ❌ | ❌ |
| `IntrinsicHolderTagsProvider.IntrinsicTagAppender`（按值追加） | ❌ | ✅ | ✅ |

**不可消除的部分**：`class X extends <版本专属 Provider>`。一个类只能继承一个父类，`@Since`/`@Until` 删的是成员不是 `extends` 子句，所以 Provider 类本身永远无法做到单类跨版本。

**可消除的部分**：Provider 里 90% 的代码是"往哪个标签/模型里放什么东西"——纯数据。

## 决策

把 datagen 拆成三层：

```
① 规格（Spec）        纯数据，零 MC 类型        → 放在 imp，三族共用
② 发射器（Writer）    版本分支（@Since/@Until） → 放在 imp，按族编译，每段都被真编译验证
③ 薄适配（Appenders） 交出该版本的 Provider/Appender → 放在内容模块，通常 3~15 行
```

已有实现：

- `imp/data/ImpTagSpec`：`(Kind, tag ImpId, values List<ImpId>)` + `Collector`
- `imp/data/ImpTagWriter`：`apply(List<ImpTagSpec>, Appenders)`
  - `@Since("26.1")` 分支：`TagAppender.addAll(Stream<ResourceKey<T>>)`
  - `@Until("26.1")` 分支：`IntrinsicTagAppender.add(T)`，并用 `@Name` 统一方法名
  - 两个分支在各自族里真实编译通过（26.2 / 1.21.1 / 1.20.6 三族 `:imp:build` 均成功）
- `ImpTagWriter.Appenders`：内容模块实现的接口，每版本只需三行 `return tag(key);`

### 内容模块侧的模板

```java
// 26.x（现有 Provider，仅需把 addTags 的循环换成规格驱动）
public class ModBlockTagsProvider extends BlockTagsProvider {
    @Override protected void addTags(HolderLookup.Provider provider) {
        ImpTagWriter.apply(ModTagSpecs.blockTags(), new ImpTagWriter.Appenders() {
            public Object blockTag(TagKey<Block> k) { return tag(k); }
            public Object itemTag(TagKey<Item> k) { throw new UnsupportedOperationException(); }
            public Object entityTypeTag(TagKey<EntityType<?>> k) { throw new UnsupportedOperationException(); }
        });
    }
}
```

规格里的条目用 `ImpId.of("ww_core", "cinder_log")` 这类字符串构造，**不需要**从 `DeferredBlock` 取 id——因此规格本身不含任何版本分叉。

## 实施进度

| 类别 | 26.x 分支 | 旧版分支 | 验证方式 |
|---|---|---|---|
| tags（block/item/entity） | ✅ 实现 | ✅ 实现 | 26.x 逐字节 diff 零差异；旧版编译验证 |
| models | ✅ 实现 | ✅ 实现 | 26.x 逐字节 diff 零差异；旧版编译验证 |
| loot | ✅ 实现 | ✅ 实现 | 26.x 逐字节 diff 零差异；旧版编译验证 |
| recipes | ✅ 实现 | ✅ 实现 | 26.x 逐字节 diff 零差异；旧版编译验证 |
| datapacks | ✅ 实现 | ✅ 实现（单实现三族共用） | 26.x 逐字节 diff 零差异 |
| datamaps | ✅ 实现 | ⚠️ 燃料已实现；**去皮无对应 DataMap** | 26.x 逐字节 diff 零差异 |

六类的三模块产物 diff 结果均为「**丢失或改动 = 0**」（core 154、wilderness 72、barren 514，
后两者含修复 client datagen 后新增的产物）。

### 旧版分支的两个真实能力边界（javap 证据）

1. **`datamaps` 的「去皮」在旧版无数据地图可写**：1.20.6 / 1.21.1 的 jar 里**不存在**
   `neoforged/neoforge/registries/datamaps/builtin/Strippable.class`，`NeoForgeDataMaps` 也**没有
   `STRIPPABLES` 字段**（只有 26.2 的 universal jar 才有）。旧版去皮由原版
   `AxeItem.STRIPPABLES`（`protected static final Map<Block, Block>`）在代码里维护，**不读数据包**。
   因此旧版分支保留 `throw` 并在消息里指出改用 `AxeItem.STRIPPABLES`——自注册一张无人消费的
   `DataMapType` 属于语义不正确，不做。
2. **`recipes` 的烧炼分类无法在旧版控制**：旧版 `SimpleCookingRecipeBuilder.smelting(...)` 没有
   `CookingBookCategory` 形参，分类由 MC 按产物推导。发射器没有假装能控制它，而是复刻旧版推导逻辑
   并在与规格声明不一致时显式失败（`verifyLegacyCookingCategory`）。

### 各类的特殊取舍（详见证件）

- **loot 方块形状留在内容模块**：26.x `BlockLootSubProvider` 的助手全是 `protected`，imp 跨包编译期调不到；
  在 imp 里重抄（含 `createLeavesDrops` 的硬编码概率表）会让产物随 MC 升级静默漂移。
- **datapack 规格刻意做薄**：`ConfiguredFeature` 等的 bootstrap 既是 MC 类型又含领域知识，
  规格只记「注册表 + 条目 id」，bootstrap 由内容模块经 `Sinks` 提供；拒绝把领域知识搬进 imp。
- **recipes 只实现真实用到的形状**（shaped + smelting），无用例的变体不预置。
- **models 的 `BuiltinDoublePlant` 与 `DoublePlant` 必须区分**：前者生成 `_bottom`/`_top` 两个模型，
  后者是单模型——这个差异是靠 diff 发现的，不是靠"看起来等价"猜的。

### 关于旧版模型分支为何是"显式未实现"

旧版没有等价的木材助手（`woodProvider` / `family` / `createTintedLeaves`），必须逐条映射到
NeoForge Forge 风格的 `BlockStateProvider`（`logBlock` / `stairsBlock` / `slabBlock` /
`fenceBlock` / `fenceGateBlock` / `pressurePlateBlock` / `buttonBlock` / `doorBlock` /
`trapdoorBlock`）并自行推导贴图路径。

而内容模块目前**还不能为旧版编译**，所以这段代码即使写出来也**无法做运行时验证**。
与其交付"能编译但从未运行、可能静默产出错误模型"的实现，不如显式失败并在异常消息里指明待办——
这与 tags 的处理不同（tags 的旧版分支简单到可以确信正确）。

## 后果

- ✅ "生成什么"只有一份，三族共用；改内容只需改规格。
- ✅ 版本差异被压缩到发射器的分段方法里，且因为 imp 按族编译，**每一段都会被真编译验证**（不是"写了但没编译过"）。
- ✅ 内容模块只需薄适配，不再需要整份 Provider 重写。
- ⚠️ 每个 Provider 类型仍需一套「规格 + 发射器 + 适配」（tags / models / loot / recipes / datapacks / datamaps 各一套）。
- ⚠️ 发射器依赖 `Object` 强转 + `@SuppressWarnings("unchecked")`：因为 `TagAppender` 等类型在不同族里根本不是同一个类，无法在签名里表达。这是本方案唯一的类型安全妥协，代价可控（强转后立即使用，且编译期已验证）。
- ⚠️ 迁移每个 Provider 后必须做 **datagen 产物 diff**，确认 JSON 未变；目前被 `RegistrySetBuilder` 的 LinkageError 阻塞（见 [evidence](../../evidence/2026-11-13-datagen-fork/README.md)）。

## 替代方案

| 方案 | 否决理由 |
|---|---|
| 每个族整套 Provider 重写（当前 `datagen/<族>/` 目录） | 内容逻辑三份重复，改一处要同步三处，必然漂移 |
| 让 Provider 不继承、改用反射驱动 | 反射在所有族里都要写，且失去编译检查 |
| 依赖第三方多版本 datagen 抽象库 | 生态里没有覆盖 NeoForge datagen 的成熟方案 |
