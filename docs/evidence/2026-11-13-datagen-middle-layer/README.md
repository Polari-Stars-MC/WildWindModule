# datagen 的中间层兼容机制（ADR-0005）（2026-11-13）

> 本机日期 2026-11-13；CHANGE_LOG 中 11-14~18 为会话内编号。

## 问题拆解

datagen 的版本差异有两类，处置方式不同：

| 差异类型 | 例子 | 能否用 imp 消除 |
|---|---|---|
| **基类不同**（类声明分叉） | `extends net.minecraft.client.data.models.ModelProvider`（26.x） vs `extends net.minecraft.data.models.ModelProvider`（1.20/1.21） | ❌ 不能：一个类只能继承一个父类，注解删的是成员不是 `extends` |
| **内容逻辑**（"往哪个标签放什么"） | 木质套件的 20 个标签、花朵、制箭台 | ✅ 能：这是纯数据 |

所以策略是**把可消除的 90% 搬进 imp，把不可消除的壳压到最小**。

## 实现的三层

```
① 规格 ImpTagSpec        纯数据（Kind + tag ImpId + values List<ImpId>）    → imp，三族共用
② 发射器 ImpTagWriter    按版本分段（@Since/@Until + @Name 统一方法名）      → imp，按族编译
③ 薄适配 Appenders       交出该版本的 TagAppender（三行）                  → 内容模块
```

内容模块侧最终形态：

```java
@Override protected void addTags(HolderLookup.Provider provider) {
    ImpTagWriter.apply(ModTagSpecs.blockTags(), new ImpTagWriter.Appenders() {
        public Object blockTag(TagKey<Block> k) { return tag(k); }
        public Object itemTag(TagKey<Item> k) { throw new UnsupportedOperationException(); }
        public Object entityTypeTag(TagKey<EntityType<?>> k) { throw new UnsupportedOperationException(); }
    });
}
```

## 两套 API 的实测差异

| 能力 | 26.x | 1.20 / 1.21 |
|---|---|---|
| 追加器类型 | `net.minecraft.data.tags.TagAppender<T>` | `IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>` |
| 追加方式 | `addAll(Stream<ResourceKey<T>>)` | `add(T...)`（直接给值） |
| 按 id 取值 | `BuiltInRegistries.X.getValue(id)` | `BuiltInRegistries.X.get(id)` |

`ImpTagWriter` 里三个 `@Since("26.1")` + 三个 `@Until("26.1")` 分别覆盖两套 API，`@Name` 把留存段统一改名成 `writeBlocks` / `writeItems` / `writeEntityTypes`。

## 验证结果

| 项 | 结果 |
|---|---|
| 26.2 编译（现代分支） | ✅ `:imp:build` 通过；预处理后可见 `(net.minecraft.data.tags.TagAppender<Block>)` |
| 1.21.1 编译（旧版分支） | ✅ `:imp:build` 通过；预处理后只剩 `IntrinsicTagAppender` 段 |
| 1.20.6 编译（旧版分支） | ✅ `:imp:build` 通过 |
| 全量构建 | ✅ `clean build` 通过 |
| 回归测试 | ✅ 20 项全绿 |

**关键点**：因为 imp 按版本族编译，两套 API 分支**都被真实编译验证过**——不是"写了但只在主力版本编过"。

## 唯一的类型安全妥协

发射器里对追加器做 `Object` 强转 + `@SuppressWarnings("unchecked")`——因为 `TagAppender` 与 `IntrinsicTagAppender` 在不同族里根本不是同一个类，无法在方法签名里表达。强转后立即使用，且编译期已验证。

## 剩余工作（同一模式的机械推广）

已有：**tags**（规格 + 发射器 + 薄适配齐全）。

待做（每类一套"规格 + 发射器"）：

| 类别 | 26.x API | 旧版 API | 难点 |
|---|---|---|---|
| models | `client.data.models.ModelProvider` / `BlockModelGenerators` / `ModelTemplates` | `data.models.ModelProvider` | 纹理模板 API 差异最大，需逐条映射 |
| loot | `LootTableProvider` + `BlockLootSubProvider` | 同名但签名不同 | 掉落池 DSL 差异 |
| recipes | `RecipeProvider` | 同名不同签名 | 配方构建器 API 差异 |
| datapacks | `DatapackBuiltinEntriesProvider` | 同名不同签名 | 目前还被 LinkageError 阻塞 |
| datamaps | `DataMapProvider` | 20.6+ 才有 | 版本区间更窄 |

**迁移每个 Provider 后必须做产物 diff 确认 JSON 未变**。此前的阻塞项（`RegistrySetBuilder` 的 LinkageError）**已修复**（根因：`imp` 被当运行库放在 app 类路径上，见 [2026-11-13-datagen-fork](../2026-11-13-datagen-fork/README.md)），`runServerData` / `runClientData` 现已能完整跑完，diff 能力恢复。
