# ROADMAP.md — 路线图

状态图例：✅ 已完成 · 🚧 进行中 · 📋 计划 · 💡 想法（未承诺）

## 近期（下一个发布周期）

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

## 已达成（节选）

- ✅ 三模块拆分与独立创造栏（2026.11.02）
- ✅ 制箭台 GUI 与矢术效果层（2026.11.03）
- ✅ WoodFamily 聚合层与资源去重大扫除（2026.10.23）
- ✅ 船 / 告示牌 / 展示架复用原版类型（2026.11.06）

> 维护规则：完成一项就把对应 🚧/📋 移到"已达成"并在 CHANGE_LOG 有对应条目；新增计划项注明来源（CHANGE_LOG 待办 / ADR / 讨论）。
