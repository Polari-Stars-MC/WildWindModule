# imp 多版本族分区与边界守卫（2026-11-12）

## 变更摘要

对应 ADR-0003 阶段 A（防护）与阶段 B（多版本族分区）：

- `imp` 内部按版本族分区：`src/main`（版本无关 API）+ `src/family/{1.20,1.21,26.x}/java`（各族实现），由 `targetMcVersion` 选入。
- Java 级别按 MC 版本推导，工具链统一 JDK 25 + `--release` 降级。
- 新增 `checkImpBoundary` 守卫任务与规则文件 `gradle/imp-boundary.txt`。
- `imp-processor` 新增 11 个回归测试（真实 `javac` 驱动）。

## 验证步骤与结果

### 1. 三族编译

```
./gradlew :imp:build -PtargetMcVersion=1.20.4   → BUILD SUCCESSFUL
    imp: targetMcVersion=1.20.4 family=1.20  neo=20.4.253  javaRelease=17
./gradlew :imp:build -PtargetMcVersion=1.21.1   → BUILD SUCCESSFUL
    imp: targetMcVersion=1.21.1 family=1.21  neo=21.1.209  javaRelease=21
./gradlew :imp:build -PtargetMcVersion=26.2     → BUILD SUCCESSFUL
    imp: targetMcVersion=26.2   family=26.x  neo=26.2.0.75 javaRelease=25
```

字节码级别实测（1.20 族）：

```
javap -v .../FamilyBridge.class | major version: 61      # 61 = Java 17
```

### 2. 真实跨族差异

同一份 main 源码（`ImpIds.of(...)` → `FamilyBridge.id(...)`），三族各自实现：

| 族 | 实现 | 实测结果 |
|---|---|---|
| 1.20 | `new ResourceLocation(ns, path)` | 编译通过 |
| 1.21 | `ResourceLocation.fromNamespaceAndPath(ns, path)` | 编译通过（**构造器在该族已私有**，直接 `new` 会失败——说明分区确有必要） |
| 26.x | `Identifier.parse(ns + ":" + path)` | 编译通过（该类已改名） |

### 3. 边界守卫

```
./gradlew checkImpBoundary -PtargetMcVersion=26.2   → BUILD SUCCESSFUL
./gradlew checkImpBoundary -PtargetMcVersion=1.21.1 → BUILD FAILED，17 处违规
```

17 处即内容模块中 `net.minecraft.resources.Identifier` 的使用点，与独立统计一致（迁移待办清单）。

### 4. 处理器回归测试

```
./gradlew :imp-processor:test → 11 tests PASSED
```

覆盖：`@Since`/`@Until` 翻转、`[26.1,26.3)` 区间与右开边界（26.3 判定为不存在）、`exclude` 命中/未命中、`@ParamSince` 宿主常量、区间倒置报错、缺回退重载报错、缺目标版本报错。

### 5. 产物打包

```
core jar 内含：org/polaris2023/wildwind/imp/{ImpIds,FamilyBridge}.class
              org/polaris2023/wildwind/imp/runtime/ImpManifest.class
imp jar MANIFEST：Imp-Target-MC-Version: 26.2 / Imp-Version-Family: 26.x / Imp-Neo-Version: 26.2.0.75
```

## 过程中发现并修正的问题

1. **1.20.2 无法用 moddev**：NeoForge 20.2.x 未发布 `neoforge-moddev-bundle` capability，`createMinecraftArtifacts` 直接失败 → 1.20 族改锚定 1.20.4 / 20.4.253。
2. **MC 1.20.5+ 要求 Java 21**：1.20.6 与 Java 17 属性不匹配，导致 neoform 变体解析失败 → Java 级别改为按 MC 版本推导而非按族硬编码。
3. **`imp-annotations` 的 21 字节码无法被 1.20 族引用**：`Dependency resolution is looking for a library compatible with JVM runtime version 17` → 两个基础设施模块降到 `--release 17`。
4. **规则文件分隔符**：初版用 `|` 分隔，与正则中的 `|` 冲突导致解析错乱 → 改用 `=>`。

## 结论

阶段 A、B 达成。**未完成项（已进 ROADMAP）**：内容模块尚未按族迁移，`1.20`/`1.21` 族当前只有 `imp` 可编译；CI 矩阵与 datagen 版本分叉仍待处理。

> **后续修订（2026-11-13）**：本文记录的"按族分目录 + `FamilyBridge`"实现已被
> [ADR-0004](../../adr/ADR-0004-source-preprocessing-and-name.md) 取代并删除，改为单类 + 源码预处理
> （见 [2026-11-13-imp-preprocessor](../2026-11-13-imp-preprocessor/README.md)）。
> 本文中关于依赖版本与 Java 级别的事实仍然有效。
