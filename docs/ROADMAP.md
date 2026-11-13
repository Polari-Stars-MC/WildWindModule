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
  - 🚧 阶段 C：内容模块迁移首个真实跨族差异（当前 core 有 17 处 `Identifier` 需经 imp 访问）
  - 📋 阶段 D：CI 矩阵（1.20.4 / 1.21.1 / 26.2 三轴）+ 断言 `targetMcVersion` 与 `neoVersion` 成对一致
  - 📋 阶段 E：datagen 版本分叉（42 个 datagen 文件全依赖 MC API，最难版本无关化的一块）
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
