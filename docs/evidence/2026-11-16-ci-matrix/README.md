# CI 版本矩阵（2026-11-16）

## 变更摘要

对应 ADR-0003 阶段 D：

- `.github/workflows/build.yml` 重构为三个 job
- 新增版本族单一事实源 `gradle/version-families.properties`
- 新增 `checkVersionPairing` 守卫（挂到各 mod 模块 `check` 之前）
- mod 模块的 `options.release` 跟随目标版本（此前固定 25）

## 为什么需要配对守卫

只传 `-PtargetMcVersion=1.21.1` 而不改 `minecraftVersion`/`neoVersion` 时：

- 产物名是 `ww_core-mc1.21.1-1.0.0.jar`
- 实际按 MC 26.2 的 API 编译

这是"假产物"，比没有多版本构建更危险。守卫在 `check` 阶段直接失败。

## 验证结果

### 1. 配对守卫四场景（实测）

| 场景 | 命令 | 结果 |
|---|---|---|
| 默认 | `checkVersionPairing` | ✅ 通过 |
| 只改 target | `-PtargetMcVersion=1.21.1` | ❌ `minecraftVersion=26.2 ≠ 族 '1.21' 期望 1.21.1` |
| 三者成对 | `-PtargetMcVersion=1.21.1 -PminecraftVersion=1.21.1 -PneoVersion=21.1.209` | ✅ 通过 |
| 版本对错配 | `-PtargetMcVersion=1.20.6 -PminecraftVersion=1.20.6 -PneoVersion=21.1.209` | ❌ `neoVersion=21.1.209 ≠ 族 '1.20' 期望 20.6.144` |

### 2. 字节码级别

| 构建 | class 文件 major | 对应 Java |
|---|---|---|
| 26.2 的 core jar | 69 | 25 |
| 1.20.6 的 imp jar | 65 | 21 |

（此前 mod 模块固定 Java 25，1.20 族会产出无法加载的字节码。）

### 3. 构建与测试

```
./gradlew clean build → BUILD SUCCESSFUL
三族 :imp:build       → 1.20.6 / 1.21.1 / 26.2 全部 SUCCESSFUL
:imp:processor:test   → 20 项全绿
```

### 4. workflow 语法

用 Python `yaml.safe_load` 解析 `.github/workflows/build.yml`：通过。

## workflow 结构

| job | 触发 | 必过 | 内容 |
|---|---|---|---|
| `primary` | push / PR / 手动 | ✅ | `./gradlew build -PtargetMcVersion=26.2`，上传三 mod jar + imp jar |
| `imp-matrix` | 同上 | ✅ | `target ∈ {1.20.6, 1.21.1, 26.2}` 分别 `:imp:build` |
| `content-matrix` | 仅手动 `full_matrix=true` | ❌（continue-on-error） | 1.20.6 / 1.21.1 的内容模块全量构建，用于观测迁移进度 |

`content-matrix` 里的 `neo` 值写在 YAML 中，但由 `checkVersionPairing` 按事实源校验——YAML 与事实源不一致时构建失败，不会静默产出假产物。

## 未做的一步

`imp-matrix` 目前只编 `imp`，因为内容模块尚未按族迁移（阶段 E 的 datagen 是最后一道墙）。迁移完成后把该 job 改为 `./gradlew build` 即可，`content-matrix` 则可并入必过路径。
