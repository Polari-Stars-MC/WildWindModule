# core 的 ID 出口迁移到 imp（2026-11-14）

## 变更摘要

- 新增 `ImpId`（跨版本稳定句柄，`unwrap()` 返回类型按版本分叉）
- `WildWindCore.id(String)` 返回类型 `Identifier` → `ImpId`，内部转调 `ImpIds.idOf(...)`
- core 的 6 处调用点与 3 个 `static final Identifier` 字段完成迁移
- 1.20 族锚点 1.20.4 → 1.20.6（NF 20.6.144，Java 21）
- 边界规则补强：拦 `Identifier.(parse|fromNamespaceAndPath|tryParse)` 静态调用

## 验证结果

### 1. 三族 imp 构建 + 返回类型分叉

| target | `ImpId.unwrap()` 实际签名 |
|---|---|
| 26.2 | `public net.minecraft.resources.Identifier unwrap()` |
| 1.21.1 | `public net.minecraft.resources.ResourceLocation unwrap()` |
| 1.20.6 | `public net.minecraft.resources.ResourceLocation unwrap()` |

三族 `:imp:build` 均 BUILD SUCCESSFUL。

### 2. 数据组件可用性（决定 1.20 锚点）

```
探测 net.minecraft.core.component.DataComponents：
  1.20.6  -> BUILD SUCCESSFUL ✅
  1.20.4  -> BUILD FAILED      ❌ 类不存在
  1.21.1  -> BUILD SUCCESSFUL ✅
```

数据组件自 1.20.5 引入，而矢术系统整体依赖它 → 1.20 族锚定 1.20.6，避免为旧版重写 NBT 实现。

### 3. 全量构建与测试

```
./gradlew build            → BUILD SUCCESSFUL（core / wilderness / barren / imp 全部）
./gradlew :imp-processor:test → 20 tests PASSED
```

### 4. 边界检查（`-PtargetMcVersion=1.21.1`）

规则补强后共 19 处，按模块：

| 模块 | 处数 | 性质 |
|---|---|---|
| core | 7 | 2 处覆写签名（GeckoLib `getTextureResource` 返回 `Identifier`）+ 5 处 datagen |
| wilderness | 7 | `id()` 未迁移 |
| barren | 5 | `id()` 未迁移 |

core 迁移前为 9 处（含 2 处 ID 构造），迁移后剩 7 处，**且剩余的全部属于 imp 吸收不了的类别**：
- 覆写签名：`getTextureResource` 的返回类型由 GeckoLib 决定，不能包装；
- datagen：`net.minecraft.client.data.models.*` 是 1.21.4+ 的结构，1.20/1.21 需要完全不同的 Provider。

## 结论

core 中"可以被中间层吸收"的部分已迁完。剩余 7 处是能力边界内的已知不可迁移项，不是遗漏。

wilderness / barren 的 `id()` 是同一模式的重复（约 25 个调用点、3 个 `static final Identifier` 字段），属下一步。

## 过程中修正的问题

1. `javaReleaseFor` 按族锚点而非实际目标版本推导 Java 级别 → 1.20.6 被错判为 Java 17，已改为按 `targetMcVersion`。
2. JavaParser 默认语言级别不认识 `instanceof` 模式匹配 → 改为 `BLEEDING_EDGE`。
3. 守卫规则只匹配 FQN（import 行），漏掉 `Identifier.parse(...)` 这类静态调用 → 补规则。
