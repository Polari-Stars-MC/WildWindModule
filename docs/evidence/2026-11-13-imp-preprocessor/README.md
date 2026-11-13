# 源码预处理与 @Name（2026-11-13）

## 变更摘要

对应 [ADR-0004](../../adr/ADR-0004-source-preprocessing-and-name.md)：机制从纯 APT 改为**源码预处理**，
并新增 `@Name` 改名；上一轮按族分目录的 `FamilyBridge` 实现已退役。

## 验证步骤与结果

### 1. 单类多版本（`ImpIds`）

三族构建后检查 `build/preprocessed/java/.../ImpIds.java` 的留存分支：

| target | 留存实现 | 方法名 |
|---|---|---|
| 26.2 | `Identifier.parse(ns + ":" + path)` | `id` |
| 1.21.1 | `ResourceLocation.fromNamespaceAndPath(ns, path)` | `id` |
| 1.20.4 | `new ResourceLocation(ns, path)` | `id` |

三族 `./gradlew :imp:build -PtargetMcVersion=<版本>` 均 BUILD SUCCESSFUL；
预处理统计示例（26.2）：`2 个文件，裁剪成员 3 处，改名 1 处`。

### 2. 预处理语义测试

```
./gradlew :imp-processor:test → 19 tests PASSED
  SourcePreprocessorTest  8 项（三分支隔离/区间/空洞/改名/歧义报错/外部表/族过滤/非法版本）
  ImpProcessorCompileTest 6 项
  ImpVersionSpecTest      5 项
```

### 3. 全量构建

```
./gradlew build → BUILD SUCCESSFUL（含 checkImpBoundary 与全部测试）
```

## 关键结论

- "一个类容纳所有版本"成立：不适用版本的代码在编译前就被删除，产物里只有当前版本实现。
- `@Name` 解决了"源码名字 vs 产物名字"的绑定，跨版本改名不再需要 if 分支或分目录。
- 代价已记录：IDE 只能对一个版本显示为绿；`@Name`/外部表按简单名匹配（无符号解析）。

## 前一轮结论的修订

2026-11-12 记录的"按族分目录（`imp/src/family/*` + `FamilyBridge`）"方案已被本方案取代，
相关目录已删除；那次验证中确认的依赖与工具链事实仍然有效并继续使用：

- 1.20 族必须锚定 1.20.4 / NeoForge 20.4.253（20.2.x 无 `neoforge-moddev-bundle`）；
- Java 级别：`<1.20.5`→17、`1.20.5~26`→21、`>=26`→25。
