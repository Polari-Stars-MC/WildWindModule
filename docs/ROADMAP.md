# ROADMAP.md — 路线图

状态图例：✅ 已完成 · 🚧 进行中 · 📋 计划 · 💡 想法（未承诺）

## 近期（下一个发布周期）

- 🚧 imp 中间层（ADR-0003）：
  - ✅ 阶段一：`imp-annotations` / `imp-processor` 子项目 + 版本标量 `targetMcVersion`（2026.11.12）
  - ✅ 阶段二：APT 生成 `ImpManifest` 常量 + `@ParamSince` 回退重载校验 + 区间/排除语义（2026.11.12）
  - ✅ 阶段三骨架：`imp` 本体（可 import MC、非 mod、字节码并入 mod 产物）+ 产物名带版本（2026.11.12）
  - ✅ 阶段 A（防护）：处理器回归测试 11 项 + `checkImpBoundary` 边界守卫（2026.11.12）
  - ✅ 阶段 B（多版本族）：三族实测编译通过，Java 17/21/25 与各自 NeoForge 版本对齐（2026.11.12）
  - ✅ 机制转向（ADR-0004）：改为源码预处理，`@Name` 改名，单类容纳所有版本，按族分目录的实现已退役（2026.11.13）
  - ✅ 阶段 C-1：core 的 ID 出口收口到 imp（`WildWindCore.id()` → `ImpId`，6 处调用迁移），1.20 族锚点改为 1.20.6（2026.11.14）
  - ✅ 阶段 C-2：wilderness / barren 的 `id()` 同样收口（16 文件 / 30 处调用），边界违规 19 → 12，剩余 12 处均为结构性不可迁移项（2026.11.15）
  - 📋 阶段 D：~~CI 矩阵~~ ✅ 已完成（2026.11.16）：`primary` 全量 26.2、`imp-matrix` 三族必过、`content-matrix` 手动触发观测进度；`checkVersionPairing` 防假产物，版本族单一事实源 `gradle/version-families.properties`
  - ✅ 阶段 E（datagen 分叉）**完成**：independent source set + 按族选目录；**ADR-0005 三层迁移覆盖全部六类**（tags / models / loot / recipes / datapacks / datamaps），三模块产物 diff「丢失或改动 = 0」
    - 26.x 分支：六类全部实现并经 diff 验证
    - 旧版分支：tags + models 已实现（仅编译验证）；loot / recipes / datapacks / datamaps 为显式抛异常
    - 📋 待内容模块支持旧版编译后，补旧版分支的运行时 datagen diff（重点：`fenceBlock` / `buttonBlock` 的 inventory 模型命名）
  - ✅ 阶段 F（内容模块支持旧版编译）：
    - ✅ **GeckoLib 已彻底移除（ADR-0006）**——三个实体（幼蛛 / 泥沼蟹 / 恐鱼）改用原版 `ModelPart` + 自研 Bedrock 动画运行时，Blockbench 导出的 `.geo.json` / `.animation.json` 原文件直接复用。这不是权宜之计：GeckoLib 按 MC 版本独立发布、两代 groupId 不同、4.x/5.x API 互不兼容，留着它等于每个 MC 版本都要额外适配一个第三方库的两个大版本
    - ✅ **`core` 三族编译全绿**（26.2 / 1.21.1 / 1.20.6 均 0 错误）——这是多版本工程第一次做到
    - ✅ **`checkImpBoundary` 三族违规 0**（迁移前 5 处：4 处 GeckoLib `getTextureResource` 覆写 + 1 处 `MudcrabVariant.createKey` 形参），结构性边界随本次迁移全部消失
    - 📋 剩余：`wilderness` / `barren` 为旧版族的编译（它们的错误全是 26.x 独有 API：`ClientAsset`、`SpawnPrioritySelectors`、`TemperatureVariants`、`EntitySpawnReason`、`ValueInput/ValueOutput` 等）
    - 📋 一次 `runClient` 人工确认动画观感（已知限制：同类实体循环动画相位共享、动画时钟走 `nanoTime` 而非 tick）
  - 📋 阶段 F：Fabric 支持（独立一层，注解层帮不上忙，需单独立项）
  - 📋 参数级裁剪：当前 `@ParamSince` 靠重载分派，若重载方案在真实迁移中不敷使用，再让预处理器直接删参数并改写调用点
- 📋 CI 版本矩阵落地：`.github/workflows/build.yml` 改 matrix（26.1 / 26.2），`-PtargetMcVersion` 传参
- 📋 补齐 barren 剩余缺贴图方块（`MissingModels` 引用自身路径，客户端现为 missingno）：焦灰草系列、灰烬植被等
- 📋 下界植物扩充（CHANGE_LOG 待办）
- 📋 成就/进度系统（CHANGE_LOG 待办）
- 🚧 wilderness 沼泽方块（reeds / cattails / bog_mud / remains）贴图与战利品补全

## 中期

- 📋 矢术系统扩展：更多箭羽/箭杆/箭头材料与组合平衡
- 📋 利用 core 已保留的船/告示牌素材（`cinder_*` / `ember_*` 28 张）完善 barren 船与告示牌的剩余细节
- 📋 datagen 确定性校验入 CI：连跑两遍 datagen 对比输出一致（generated 不入库，见 [OPTIMIZATION.md](../OPTIMIZATION.md) 待做表）
- 💡 延展药水与状态效果系统化（wilderness）

## 长期 / 想法

- 💡 各模块独立世界生成主题（荒野湿地群系、瘠土焦土群系深化）
- 💡 泥沼蟹 / 恐鱼行为深化（WindupAttackMob 体系的更多蓄力型生物）
- 💡 第三方可合成性：与常见数据包标签体系（`c:` tags）更全面对齐
- 💡 若「参数真移除」成为刚需，重启 ADR-0003 里被暂缓的方案 A（源码裁剪 Gradle 插件），注解集可直接复用

## 已达成（节选）

- ✅ 三模块拆分与独立创造栏（2026.11.02）
- ✅ 制箭台 GUI 与矢术效果层（2026.11.03）
- ✅ WoodFamily 聚合层与资源去重大扫除（2026.10.23）
- ✅ 船 / 告示牌 / 展示架复用原版类型（2026.11.06）
- ✅ imp 中间层阶段一/二/三骨架（2026.11.12，见 [ADR-0003](./adr/ADR-0003-imp-middle-layer-apt.md)）

> 维护规则：完成一项就把对应 🚧/📋 移到"已达成"并在 CHANGE_LOG 有对应条目；新增计划项注明来源（CHANGE_LOG 待办 / ADR / 讨论）。
