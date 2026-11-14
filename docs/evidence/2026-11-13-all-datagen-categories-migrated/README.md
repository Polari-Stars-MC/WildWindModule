# datagen 六类全量迁移完成（ADR-0005）（2026-11-13）

> 本机日期 2026-11-13；CHANGE_LOG 中 11-14~20 为会话内编号。

## 成果总览

datagen 的**全部六类**均已按「规格 / 发射器 / 薄适配」三层完成迁移：

| 类别 | 26.x 分支 | 旧版分支 | diff 验证 |
|---|---|---|---|
| tags（block/item/entity） | ✅ 实现 | ✅ 实现 | ✅ core 零差异 |
| models | ✅ 实现 | ✅ 实现 | ✅ core 零差异 |
| loot | ✅ 实现 | 显式抛异常 | ✅ core 零差异 |
| recipes | ✅ 实现 | 显式抛异常 | ✅ core 零差异 |
| datapacks | ✅ 实现 | 显式抛异常 | ✅ core 零差异 |
| datamaps | ✅ 实现 | 显式抛异常 | ✅ core 零差异 |

`imp/data/` 下现有 12 个文件（6 类 × Spec + Writer）：

```
ImpTagSpec/ImpTagWriter        ImpModelSpec/ImpModelWriter
ImpLootSpec/ImpLootWriter      ImpRecipeSpec/ImpRecipeWriter
ImpDatapackSpec/ImpDatapackWriter   ImpDataMapSpec/ImpDataMapWriter
```

各模块 main 里对应的规格类：`ModTagSpecs` / `ModModelSpecs` / `ModLootSpecs` / `ModRecipeSpecs` / `ModDatapackSpecs` / `ModDataMapSpecs`，以及 wilderness / barren 的同名版本。

## 最终 diff 结果（三个模块）

```
core         基线 154 / 当前 154 → 丢失或改动 0 ; 新增 0
wilderness   基线  36 / 当前  72 → 丢失或改动 0 ; 新增 36
barren       基线 152 / 当前 514 → 丢失或改动 0 ; 新增 362
```

**"丢失或改动 = 0" 是硬指标**：说明六类迁移没有改变任何一个已有产物字节。

`wilderness` / `barren` 的新增 36 / 362 个文件**不是回归，而是修复**：它们的 `runClientData` 此前一直失败——

```
NoClassDefFoundError: org/polaris2023/wildwind/core/datagen/provider/MissingModels
```

原因是它们的模型 Provider 直接 import 了 core 的 **datagen source set** 里的 `MissingModels`，而运行期 core 是以 jar 加载的（jar 不含 datagen 类）。把 `MissingModels` 的四种形状（`cube` / `stoneSet` / `cross` / `doublePlant`）表达成 `ImpModelSpec.Cube` / `StoneSet` / `Cross` / `DoublePlant` 之后，两个模块不再依赖 core 的 datagen，client datagen 首次跑通。

**字节码级证据**（`javap -p -c` 扫常量池）：

```
WildernessModelProvider: 0 refs to core/datagen 或 MissingModels
BarrenModelProvider:     0 refs to core/datagen 或 MissingModels
两者均改为 invokestatic ImpModelWriter.apply(...)
```

## 过程中 diff 抓到的真实问题

只有一处，但很典型：`soul_rose_bush` 的模型形状。

26.x 内置 `createDoublePlantWithDefaultItem` 生成 `_bottom`/`_top` **两个**模型，而 `MissingModels.doublePlant` 是**单模型**实现——原 Provider 对 `soul_rose_bush` 与 `wither_rose_bush` 用了不同做法。规格里因此区分 `BuiltinDoublePlant` 与 `DoublePlant`，一一对应后 diff 归零。

**如果只靠"看起来等价"判断，这个方块的模型会静默变形。**

## 各 agent 的关键设计判断（值得留档）

1. **loot 的方块形状必须留在内容模块**：26.x `BlockLootSubProvider` 把 `dropSelf` / `createDoorTable` / `createLeavesDrops` 全部做成 `protected`，imp 跨包**编译期就调不到**。在 imp 里重抄（尤其 `createLeavesDrops` 里硬编码的概率表 `{0.02, 0.022222223, 0.025, 0.033333335, 0.1}`）会让产物在 MC 升级时静默漂移。所以方块侧是「发射器选形状 + 解析 id，适配器一行转出原版助手」；实体侧则在 imp 里真正组装（`LootPool` / `LootItem` 都是公开 API）。
2. **datapack 的规格要薄**：`ConfiguredFeature` / `PlacedFeature` / `BiomeModifier` 的构造本质是 `void run(BootstrapContext<T>)`，既是 MC 类型又含领域知识。规格只写「哪个动态注册表 + 会产出哪些条目 id」，实际 bootstrap 由内容模块通过 `Sinks` 提供。**明确拒绝**把树形/群系退化成纯数据再翻译回 MC 类型——那会让 imp 反向依赖内容模块，破坏分层。
3. **`ResourceKey.createRegistryKey` 是 interned 的**：重建出的键与 `Registries.*` 常量是同一对象（`ResourceKey` 没有 `equals`，完全依赖 interning），所以 datapack 注册表键逐实例相同。
4. **recipes 只实现真实用到的形状**（shaped + smelting），刻意不写 `Shapeless`/`Stonecutting`/`Smithing`——没有用例的变体等于交付未经验证的代码。
5. **`Shaped.key` 必须用 `LinkedHashMap`**（不能用 `Map.copyOf`，它不保证迭代顺序，而 define 顺序参与构建器内部状态）。

## 同时修掉的构建问题

**configuration cache 之前是坏的**（`imp:build` 开着 CC 会直接失败），三个 agent 都独立撞到并报告：

```
Task ':imp:preprocessImpSources' of type JavaExec:
cannot serialize Gradle script object references
```

根因有两处，都已修：

1. `JavaExec` 的 `doFirst { preprocessedDir.get().asFile.deleteRecursively() }` 捕获了 Provider / 脚本对象 → 把清理移进预处理器自己的 `main`（新增 `deleteTree`），任务里不再有 `doFirst`，参数改用 `args(...)` 直接传不可变 `File` 绝对路径。
2. `checkImpBoundary` / `checkVersionPairing` 用 `doLast` 闭包实现，同样捕获脚本对象 → 显式 `notCompatibleWithConfigurationCache(...)`，让 Gradle 为这些任务禁用 CC 而不是让整次构建失败。

修完后 `./gradlew build` 与三族 `:imp:build` 在**开启 configuration cache** 下全部通过。

## 注意事项（留给后续）

1. **旧版分支的运行时验证还欠着**：loot / recipes / datapacks / datamaps 的旧版分支是显式抛异常；models / tags 的旧版分支有实现但**只做过编译验证**。等内容模块支持旧版编译后，必须补 datagen diff，重点看 `fenceBlock` / `buttonBlock` 的 inventory 模型命名。
2. **`ImpLootSpec` 有一个隐含前提**：三个 Provider 都以 `Set.of()` 作 `explosionResistant`，所以 `applyExplosionCondition` 恒生效。规格里没有"抗爆物品"这个概念——若将来传入非空集合，产物会变而规格无法表达（已在 javadoc 写明）。
3. **`EntityPool.smelted()` 绑定 `shouldSmeltLoot()`**：这是现网唯一用法。若出现"烧炼但用别的条件"，需要加变体。
4. **`ImpModelSpec.StoneSet` 目前无调用方**（原 `MissingModels.stoneSet` 也无），仅为补齐形状；barren 的砂岩套件若要迁可直接用。
