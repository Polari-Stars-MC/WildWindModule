# OPTIMIZATION.md — 性能优化与构建优化记录

记录本项目已实施的优化、待做优化与量化方法。每次优化落地后在本文追加条目，并在 `docs/evidence/` 留验证记录。

## 已实施

### 1. 资源层：datagen 统一生成（2026.10.23）

- **问题**：`src` 中混入 438 个与 datagen 产物同名的手写 JSON、130 个未注册方块的孤儿资源、26 张重复贴图，jar 体积与维护成本双高，且手写文件会覆盖生成结果。
- **措施**：回滚手写资源，仅保留 80 个 datagen 无法重建的文件；删除被提交的 `.cache`（HashCache）。
- **收益**：单一资源事实来源（datagen），增量构建时 HashCache 跳过未变更文件。

### 2. 构建层：Gradle 并行与缓存

- `gradle.properties`：`org.gradle.parallel=true`、`org.gradle.caching=true`、`org.gradle.configuration-cache=true`。
- Gradle 9 下资源重复默认报错，`ProcessResources` 统一 `DuplicatesStrategy.EXCLUDE`（build/generated 先注册者优先），避免三 srcDir 叠加导致的重复 entry 失败。
- 已知开销：configuration cache 报告在 `build/reports/configuration-cache/`，CI 构建命令 `./gradlew build` 无需额外参数。

### 3. 代码层：WoodFamily 聚合遍历

- 模型、战利品、标签、数据地图、创造栏提供器全部改为遍历 `ModWoodFamilies.ALL`，新增木种零注册样板；创造栏 62 行 `insertAfter` 链收敛为 `appendWoodSet()`。
- 这是可维护性优化而非运行时优化：注册均发生在启动期一次。

### 4. 事件→数据地图（2026.10.23）

- 燃料由每 tick 触发的 `FurnaceFuelBurnTimeEvent` 改为 `furnace_fuels` 数据地图（静态数据一次加载），同时移除 `FuelHandler` 类。

### 5. 运行时箭矢零开销路径

- `ArrowComponent` 挂在原版 `AbstractArrow#pickupItemStack` 上，无组件的普通箭不进入任何自定义分支；未新增 attachment 或 capability，避免每实体额外数据。

## 待做（候选，按性价比排序）

| 候选 | 说明 | 风险 |
|---|---|---|
| datagen 确定性校验入 CI | CI 中连跑两遍 `runClientData`/`runServerData`，对比两遍输出是否一致（generated 不入库，见 docs/BOUNDARY.md §5，故只能对比两次运行而非 git 历史） | 双倍 datagen 的 CI 时长 |
| Mixin 审计 | `AbstractArrowMixin` 每 tick 分支较多（重力/粒子/落点效果），可用 profiler 确认热点后再收紧 early-return | 低 |
| BlockEntityTypeMixin 收窄 | `isValid` HEAD 注入对所有 BlockEntityType 生效，可先按 `blockSet` 快速过滤再放行可疑沙 | 低 |
| 贴图统一脚本化 | `textures_generator/*.ps1` 目前各自独立，可加参数化入口统一重生成，保证确定性（已有固定噪声种子做法） | 低 |

## 量化方法

- 构建时间：`./gradlew build --profile`，对比 profile 报告。
- 启动注册量：`runClient` 日志 `forge.logging.markers=REGISTRIES`（已默认开启）统计各注册表条目数。
- 运行时：游戏内 F3 + Debug profiler / Spark（如引入），聚焦 entity tick 与 block tick。
- 每条优化以「改动前数值 → 改动后数值」的形式记录进 `docs/evidence/`，没有数值支撑的优化不合并。
