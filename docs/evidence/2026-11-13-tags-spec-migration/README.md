# datagen 标签规格化迁移（ADR-0005 首个 Provider 类型）（2026-11-13）

## 做了什么

把 core 的三个标签 Provider 从"直接写 MC 调用"改为**规格驱动**：

| 之前 | 之后 |
|---|---|
| `ModBlockTagsProvider` 里 60 行 `addToTag(...)` | 规格 `ModTagSpecs.blockTags()` + 三行 `Appenders` 适配 |
| `ModItemTagsProvider` 里 60 行 | 规格 `ModTagSpecs.itemTags()` + 三行适配 |
| `ModEntityTypeTagsProvider` 里 3 行 | 规格 `ModTagSpecs.entityTypeTags()` + 三行适配 |

- 规格（`ModTagSpecs`）在 **main** 源码，只产出 `ImpTagSpec`（纯数据、零 MC 类型），三族共用
- 发射（`ImpTagWriter`）在 **imp**，用 `@Since`/`@Until` 分段覆盖两代 API
- 适配（`Appenders`）在 **datagen** 源码，只有 `return tag(key);`

新增 imp 辅助：`ImpIds.fromString` / `ofHolder` / `ofTag`（用 `toString()` 中转，**单实现即可跨版本**，无需分支）。

## 验证方式：逐字节 diff

先建立基线快照（`build/generated/<模块>` 复制到临时目录），迁移后重跑 datagen 并与基线**按文件哈希**对比：

| 模块 | 对比范围 | 结果 |
|---|---|---|
| core | 154 个文件（server + client，排除 `.cache`） | ✅ 无差异 |
| wilderness | 36 个文件（server） | ✅ 无差异 |
| barren | 152 个文件（server） | ✅ 无差异 |

**三次独立的零差异**（迁移 block → item → entity 后各验证一次），证明规格化没有改变任何产物。

## 关键细节

- **条目顺序必须严格镜像原实现**：标签 JSON 的条目顺序取决于插入顺序，规格里按原调用顺序逐条 append。
- **模块自有标签**（`ww_core:<family>_logs`）通过 `ImpId.of(MOD_ID, ...)` 表达，规格支持带命名空间。
- **`Block::asItem` 的隐式映射**：原物品标签用方块转物品；因为简单方块物品 id 与方块 id 相同，规格里直接写 id 即可，产物一致（已由 diff 证明）。
- **`ImpIds.ofHolder` 不需要版本分支**：`DeferredHolder.getId()` 的返回类型虽然跨版本不同（`Identifier` / `ResourceLocation`），但只用 `toString()` 就绕开了类型差异。

## 尚未迁移（同一模式的机械推广）

| 类别 | 难点 |
|---|---|
| models | 26.x 用 `BlockModelGenerators`/`ModelTemplates`，旧版用 `net.minecraft.data.models.*`，纹理模板 API 差异最大 |
| loot | 掉落池 DSL 差异 |
| recipes | 配方构建器差异 |
| datapacks | 同名不同签名 |
| datamaps | 仅 20.5+ 存在，版本区间更窄 |

每迁移一类都要重复本文件的流程：**建立基线 → 迁移 → 重跑 → 逐字节 diff**。
