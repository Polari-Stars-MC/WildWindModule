# GeckoLib → 原版渲染管线迁移完成（ADR-0006）（2026-11-13）

> 本机日期 2026-11-13；CHANGE_LOG 中 11-21~22 为本会话内编号。

## 成果

**三个实体全部脱离 GeckoLib，改用原版 `ModelPart` + 自研 Bedrock 动画运行时。**
`geckolib` 依赖已从构建脚本、仓库声明与 `neoforge.mods.toml` 中彻底移除。

| 实体 | 几何 | 动画 | 状态 |
|---|---|---|---|
| `core` 幼蛛 spiderling | 14 骨骼 / 13 立方体 | `move.walk`（循环）、`attack.swing`（一次性） | ✅ |
| `wilderness` 泥沼蟹 mudcrab | 14 骨骼 / 13 立方体 | `move.walk`、`move.swim`、`misc.idle`、`misc.hurt`、`attack.swing`、`misc.greeting` | ✅ |
| `wilderness` 恐鱼 piranha | 5 骨骼 / 11 立方体 | `move.swim`、`misc.idle`、`attack.swing` | ✅ |

Blockbench 导出的 `.geo.json` / `.animation.json` **原文件直接复用**，不需要转换成原版模型格式。

## 新增的分层（`imp/.../imp/client/`）

| 层 | 文件 | 职责 | 是否需版本分叉 |
|---|---|---|---|
| 1 | `bedrock/BedrockAnimation` | `.animation.json` 解析 + 关键帧求值（线性 / Catmull-Rom） | 否（纯数据） |
| 2 | `bedrock/BedrockGeometry` | `.geo.json` 解析（骨骼树 + 立方体） | 否（纯数据） |
| 3 | `bedrock/GeoModelBuilder` | 翻成原版 `ModelPart` 层级 | 否（三族 API 一致） |
| 4 | `bedrock/GeoAnimationState` | 动画状态机（循环 + 一次性覆盖） | 否 |
| 5 | `bedrock/GeoResources` | 资源加载 + 每帧写回 `ModelPart` | 否（`ImpId` 收敛 ID） |
| 6 | `ImpGeoRig` | 装配门面 `Rig` | 否 |
| 7 | `ImpAnimatedEntity` | 实体侧动画契约（替代 `GeoEntity`） | 否 |
| 8 | `ImpRig` | **渲染器 / 模型 / 渲染态基类** | **是**（三族分段） |

## 坐标系换算（最容易错的地方）

| 概念 | Bedrock | 原版 `ModelPart` |
|---|---|---|
| 立方体位置 | `origin` = 最小角、绝对像素、y 向上 | `addBox` 的 x/y/z = 相对 pivot 的角点偏移、y **向下** |
| 旋转 | 度 | 弧度 |
| 位移 | 像素 | 像素 |

换算公式：`x = originX - pivotX`、`y = pivotY - (originY + sizeY)`、`z = originZ - pivotZ`。

## 渲染器的版本分叉（javap 实测）

| | 26.x | 1.20.6 / 1.21.1 |
|---|---|---|
| `EntityRenderer` | `<T, S extends EntityRenderState>` | `<T>` |
| 渲染入口 | `submit(S, PoseStack, SubmitNodeCollector, CameraRenderState)` | `render(T, float, float, PoseStack, MultiBufferSource, int)` |
| 纹理 | `getTextureLocation(S)` 返回 `Identifier` | `getTextureLocation(T)` 返回 `ResourceLocation` |
| `LivingEntityRenderer` | `<T, S, M extends EntityModel<? super S>>` | `<T, M extends EntityModel<T>>` |
| `renderToBuffer` | `final`，不可覆写 | 1.21：5 参 int；**1.20.6：8 参 float** |
| `setupRotations` | `<S>(S, PoseStack, float, float)` | `<T>(T, PoseStack, float, float, float, float)` |

26.x 的 `submit` 是**两阶段提交式架构**（渲染态分离），不是简单改签名。它已自己完成姿态、
`state.scale`、`-1,-1,1` 翻转、`-1.501` 基准位移、受伤泛红、光照、阴影与名牌，
所以 26.x 侧反而代码最少。

## 三个版本无关钩子（恢复被迁移丢掉的能力）

原版对应方法的签名在三族各不相同，内容模块无法用一份源码覆写，因此收敛成钩子：

| 钩子 | 替代的 GeckoLib 覆写 | 26.x 落点 | 旧族落点 |
|---|---|---|---|
| `textureFor(T)` | `getTextureResource` | `submit` 前经 `getTextureLocation(state)` → 从 state 取实体 | `getTextureLocation(entity)` |
| `scaleFor(T)` | `scaleModelForRender` | `submit` 内 `poseStack.scale` | `scale(...)` |
| `rotationFor(T, float[])` | `adjustRenderPose` | `submit` 内 `mulPose` | `setupRotations(...)` |

**关键设计**：三个钩子的参数统一是**实体**，不是渲染状态。这样同一份覆写在两族都成立，
内容模块不需要写任何版本注解；26.x 侧由 `extractRenderState` 把实体存进渲染态再取出来。

## 过程中修掉的三个真 bug

1. **攻击动画在客户端从未播放**（既有 bug）。`triggerAnim` 跑在服务端写普通字段，客户端收不到。
   改用 `SynchedEntityData` 的**同步计数器**（每次触发 +1，而非置 boolean——相邻两次攻击要被
   边沿检测区分开），渲染侧每帧比对，只在值变化时返回一次 `true`。泥沼蟹有两套令牌
   （`attack` + `greeting`）。
2. **静态初始化 NPE**。`EntitiesHandler` 的 `static { ... }` 里读 `ModEntities.SPIDERLING.get()`，
   而 `@EventBusSubscriber` 的自动注册正是一次类初始化——那一刻 `DeferredRegister` 还没绑定，
   抛 `Trying to access unbound value`。把声明移进 `onAttributeCreate` 事件回调解决。
3. **刷怪蛋 `Item id not set` NPE**。`ImpLegacy.spawnEgg(...)` 接收立即求值的 `EntityType`，
   但物品注册阶段实体类型尚未绑定。改为接收 `Supplier`（调用点传 `ModEntities.SPIDERLING::value`），
   把求值推迟到真正需要时。

## 验证（全部实跑）

```
26.2 全量构建（clean build）          BUILD SUCCESSFUL
core @26.2 / @1.21.1 / @1.20.6       0 错误 / 0 错误 / 0 错误
:imp:build @三族                     BUILD SUCCESSFUL ×3
:imp-processor:test                  20 项全过
checkImpBoundary @三族               违规 0 / 0 / 0   ← 迁移前是 5
```

**datagen 产物逐字节 diff（六个任务全部成功）**：

| 模块 | 基线 | 当前 | 丢失或改动 | 新增 |
|---|---|---|---|---|
| core | 154 | 154 | **0** | 0 |
| wilderness | 72 | 72 | **0** | 0 |
| barren | 514 | 514 | **0** | 0 |

`checkImpBoundary` 从 5 处降到 **0 处**：4 处 GeckoLib `getTextureResource` 覆写签名与
1 处 `MudcrabVariant.createKey` 形参类型，全部随本次迁移消失。ADR-0006 原本的目标是"降到 1"，
实际清零。

## 已知限制

1. **动画时钟按实体类型共享**：`ModelPart` 树与 `Rig` 属渲染器（每类实体一份），26.x 的
   `EntityModel` 要求构造期固定 root，无法按实体换骨架。所以**同屏多个同类实体的循环动画
   相位一致**。一次性动画时机仍每实体独立（走同步令牌）。
2. **动画时间来自 `System.nanoTime()` 差分**，不随 `tickCount` 推进；实体被剔除时时钟照走，
   重新可见会跳到当时相位而非从头播。
3. **未做 `runClient` 实机验证**——上述两条的实际观感需要一次人工确认。
4. `ImpGeoRig` / `ImpAnimatedEntity` / `GeoRigController` 与 `ImpEntityRenderers` / `ImpGeoModel`
   存在功能重叠（多轮并行开发产生）。当前实际使用的是 `ImpRig` 那一套，其余可后续清理。
