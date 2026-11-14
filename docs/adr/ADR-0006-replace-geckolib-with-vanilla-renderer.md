# ADR-0006：用原版渲染管线替代 GeckoLib

- **状态**：已接受，实施中
- **日期**：2026-11-13
- **相关**：[ADR-0003](./ADR-0003-imp-middle-layer.md)（中间层）、[ADR-0005](./ADR-0005-datagen-via-middle-layer.md)（datagen 三层）

## 背景

`core`（幼蛛）与 `wilderness`（泥沼蟹、恐鱼）用 GeckoLib 做实体模型与骨骼动画。引入中间层
（ADR-0003）后，GeckoLib 成了跨版本支持路上**最硬的一块外部依赖**：

1. **它按 MC 版本分别发布**：artifact 形如 `geckolib-neoforge-<mcver>`，每个 MC 版本一条发布线。
2. **两代 groupId 不同**：4.x（1.20 / 1.21 线）在 `software.bernie.geckolib`，5.x（26.x 线）在
   `com.geckolib`。仓库的 `exclusiveContent` filter 只放行一个 group 就会让另一族解析不到。
3. **4.x 与 5.x 的 API 被整体重写**：包名（`renderer` → `renderer.base`）、泛型元数
   （`GeoEntityRenderer<T>` → `<T,S>`）、控制器构造器（4.x 强制 animatable 实参）、
   `AnimationTest` / `PlayState` / `AnimatableInstanceCache` 的包路径全都不同。
4. 由此产生了 `checkImpBoundary` 里 4 处「结构性不可迁移」的 `getTextureResource` 覆写签名
   —— 返回类型与参数类型都由库决定，内容模块无法写成版本无关。

结论：把 GeckoLib 留在依赖里，就等于**每个 MC 版本都要额外适配一个第三方库的两个大版本**。
而本项目实际只用到它极少的功能：一给几何模型、二给骨骼循环动画、三给一次性触发动画。

## 决策

**用 Minecraft 原版渲染管线 + 自研轻量 Bedrock 动画运行时，替代 GeckoLib。**

保留 Blockbench 导出的 `.geo.json` / `.animation.json` 原文件（无需转换资产），自己解析。

## 实施结构

`imp/.../imp/client/bedrock/` 下六层，前三层是纯数据（零 MC 类型）：

| 层 | 文件 | 职责 | 跨版本 |
|---|---|---|---|
| 1 | `BedrockAnimation` | `.animation.json` 解析；关键帧求值（线性 + Catmull-Rom） | 纯数据，无分叉 |
| 2 | `BedrockGeometry` | `.geo.json` 解析（骨骼树 + 立方体） | 纯数据，无分叉 |
| 3 | `GeoModelBuilder` | 翻成原版 `ModelPart` 层级 | API 三族一致，无分叉 |
| 4 | `GeoAnimationState` | 动画状态机（循环 + 一次性覆盖） | 无分叉 |
| 5 | `GeoResources` | 资源加载 + 每帧写回 `ModelPart` | 用 `ImpId` 收敛 ID 类型 |
| 6 | `ImpGeoRig` | 装配门面（`Rig`） | 无分叉 |

实体侧契约：`ImpAnimatedEntity`（替代 `GeoEntity`）+ `GeoRigController`（替代
`AnimatableManager` / `AnimationController` / `triggerAnim`）。

### 坐标系换算（最容易错的地方）

| 概念 | Bedrock | 原版 `ModelPart` |
|---|---|---|
| 立方体位置 | `origin` = 最小角，**绝对**像素，y 向上 | `addBox` 的 x/y/z = 相对 pivot 的角点偏移，y **向下** |
| 旋转 | 度 | 弧度 |
| 位移 | 像素 | 像素 |

换算：`x = originX - pivotX`、`y = pivotY - (originY + sizeY)`、`z = originZ - pivotZ`。

## 渲染器基类的版本分叉

这一点无法避免，是 Minecraft 自身的差异（已用 javap 核实）：

| | 26.x | 1.20 / 1.21 |
|---|---|---|
| `EntityRenderer` 泛型 | `<T, S extends EntityRenderState>` | `<T>` |
| 渲染入口 | `submit(S, PoseStack, SubmitNodeCollector, CameraRenderState)` | `render(T, float, float, PoseStack, MultiBufferSource, int)` |
| 纹理 | 在渲染态里 | `getTextureLocation(T)` 返回 `ResourceLocation` |
| `LivingEntityRenderer` | `<T, S, M extends EntityModel<? super S>>` | `<T, M extends EntityModel<T>>` |

26.x 的 `submit` 是**全新的渲染架构**（渲染态分离 + 提交节点），不是简单改签名。
因此渲染器基类由 `@Since`/`@Until` 分段承载，这是本 ADR 接受的成本。

## 代价与收益

**收益**

- 少一个按 MC 版本独立发布、且两代 API 互不兼容的外部依赖；
- `checkImpBoundary` 里 4 处 `getTextureResource` 结构性边界随之消失（覆写移入 imp）；
- 资源路径只认自己一套（`geckolib/models/` 与 `geckolib/animations/`），不必再兼容 4.x 的
  `geo/` 与 `animations/`；
- 动画运行时行为可预测、可测试（纯数据 + 纯函数求值）。

**代价**

- 放弃 GeckoLib 的进阶能力（molang、复杂控制器状态机、渲染类型切换、表情等）——
  本项目**当前未使用**这些能力；
- 需要自己维护几何/动画解析（约 500 行，已按纯数据写好）；
- 26.x 的 `submit` 架构需要单独适配（分段处理）。

## 被否决的替代方案

1. **继续用 GeckoLib，在 imp 里做 4.x / 5.x 双分支适配**：这正是被本 ADR 取代的方案。
   它能让代码编译，但把「一个第三方库的两个大版本」变成了必须长期维护的负担，
   而且 4.x 的 maven 坐标还不在同一个 group 下，CI 与本地环境都要额外配置。
2. **把 GeckoLib 4.x 的 jar 塞进仓库当本地依赖**：引入二进制资产，升级与审计成本高，
   且仍然要维护两套 API 适配代码，只是把网络问题换成了仓库体积问题。
3. **放弃骨骼动画，改用原版静态模型**：视觉退化明显（这些实体的动画是卖点之一），
   不必要——解析与求值部分并不复杂。

## 验证要求

- 三族 `:imp:build` 全绿；
- 26.2 全量构建 + 三模块 datagen 产物**零差异**；
- 客户端能启动、三个实体能正常渲染且动画可见（人工确认）；
- `checkImpBoundary` 在 1.21 族的违规数从 5 降到 1（只剩 `MudcrabVariant.createKey` 的形参类型）。

## 实施进度

| 实体 | 状态 |
|---|---|
| `core` 的 spiderling（14 骨骼 / 2 动画） | ✅ 已迁完 |
| `wilderness` 的 mudcrab（14 骨骼 / 6 动画） | ✅ 已迁完 |
| `wilderness` 的 piranha（5 骨骼 / 11 立方体 / 3 动画） | ✅ 已迁完 |
| 移除 GeckoLib 依赖 | ✅ 完成（构建脚本、仓库声明、`neoforge.mods.toml` 全部清除） |

**最终验证**：26.2 全量构建通过；`core` 三族编译 0 错误；三族 `:imp:build` 通过；
20 项测试全过；`checkImpBoundary` 三族违规 **0**（迁移前 5）；datagen 产物逐字节零差异。

## 三个版本无关钩子

原版对应方法的签名在三族各不相同，内容模块无法用一份源码覆写，因此收敛成钩子
（参数统一是**实体**而非渲染状态，这样同一份覆写两族都成立）：

| 钩子 | 替代 | 26.x 落点 | 旧族落点 |
|---|---|---|---|
| `textureFor(T)` | `getTextureResource` | `getTextureLocation(state)` 内从 state 取实体 | `getTextureLocation(entity)` |
| `scaleFor(T)` | `scaleModelForRender` | `submit` 内 `poseStack.scale` | `scale(...)` |
| `rotationFor(T, float[])` | `adjustRenderPose` | `submit` 内 `mulPose` | `setupRotations(...)` |

## 已知限制

1. **动画时钟按实体类型共享**：`ModelPart` 树与 `Rig` 属渲染器（每类实体一份），26.x 的
   `EntityModel` 要求构造期固定 root，无法按实体换骨架。所以**同屏多个同类实体的循环动画相位一致**。
   一次性动画时机仍每实体独立（走同步令牌）。
2. **动画时间来自 `System.nanoTime()` 差分**，不随 `tickCount` 推进；实体被剔除时时钟照走，
   重新可见会跳到当时相位而非从头播。
3. **`ImpGeo`（旧 GeckoLib 封装）保留至 wilderness 迁移完成**，之后删除。

## 顺带修掉的真 bug

**攻击动画在客户端从未播放**：旧代码 `triggerAnim` 跑在服务端（`processPreparingAttack`），
写的是普通字段，客户端永远收不到。现在改用 `SynchedEntityData` 的**同步计数器**（每次触发 +1，
而非置 boolean——相邻两次攻击要被边沿检测区分开），渲染侧每帧比对，只在值变化时返回一次 true。
