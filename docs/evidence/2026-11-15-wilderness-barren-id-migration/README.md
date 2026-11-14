# wilderness / barren 的 ID 出口迁移（2026-11-15）

## 变更摘要

承接 [2026-11-14](../2026-11-14-core-id-migration/README.md)，把同一模式推广到另外两个模块：

- `WildWindWilderness.id(...)` / `WildWindBarren.id(...)` 返回类型改为 `ImpId`
- 16 个文件、30 处调用点补 `.unwrap()`
- `OmniClawItem` 的 3 个属性 ID 字段、`ClientOmniClawTooltip` 的 4 个精灵图字段（含数组）改为 `ImpId`
- 清理 5 处不再需要的 `Identifier` 导入

## 验证结果

### 1. 构建

```
./gradlew clean build → BUILD SUCCESSFUL（core / wilderness / barren / imp 全部）
```

### 2. 边界检查（`-PtargetMcVersion=1.21.1`）

| 阶段 | 违规数 |
|---|---|
| 最初（仅 import 规则） | 17 |
| 补静态调用规则后 | 19 |
| core 迁移后 | 19 |
| **wilderness / barren 迁移后** | **12** |

剩余 12 处的构成（全部为结构性不可迁移）：

| 类别 | 行数 | 文件 |
|---|---|---|
| datagen | 7 | `BarrenItemTagsProvider`×2、`BarrenModelProvider`、`MissingModels`、`ModItemTagsProvider`×2、`ModModelProvider` |
| GeckoLib 覆写签名 | 4 | `MudcrabModel`、`PiranhaModel`、`SpiderlingModel`、`SpiderlingEyesLayer` 的 `getTextureResource` |
| 外部 API 形参类型 | 1 | `MudcrabVariant.createKey(Identifier)`（类型源自 `TemperatureVariants` 常量） |

### 3. 迁移方式说明

新增的 `.unwrap()` 由脚本按 `WildWind(?:Wilderness|Barren)\.id\([^)]*\)` 精确匹配追加（调用点参数均为单一字面量或标识符），避免手工遗漏；随后逐文件处理需要改类型的字段与方法参数。

## 结论

ID 相关的版本分叉在三个模块中**已全部收敛到 imp**。边界检查剩下的 12 处不是遗漏，而是三类结构限制：
1. 旧版 datagen 的 Provider 体系与 1.21.4+ 完全不同——只能按版本各写一份；
2. GeckoLib 决定 `getTextureResource` 的返回类型——覆写签名不可包装；
3. 外部库常量类型决定了形参类型——需要上游提供稳定类型才能改。

后续若要继续降低违规数，唯一有意义的动作是阶段 E（datagen 按版本分叉），而不是继续在 imp 上做文章。
