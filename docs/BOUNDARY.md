# BOUNDARY.md — 模块边界与依赖规则

定义 core / wilderness / barren 三模块之间"什么放哪、谁能依赖谁"。违反边界的 PR 会被要求拆分。

## 1. 依赖方向（单向，禁止反向）

```
barren ─┐
        ├──> wilderness ──> core      （运行期 mods.toml required/AFTER）
        └───────> core              （编译期 implementation project(":core")）
```

- **禁止**：core 引用 wilderness / barren 的任何类；wilderness 引用 barren 的任何类。
- **禁止**：用反射、事件回调或字符串注册表绕过上述依赖。
- barren 的开发期 `localRuntime project(":wilderness")` 仅作用于本地 run，不是发布依赖。

## 2. 放置规则

| 内容 | 归属 | 说明 |
|---|---|---|
| `WoodFamily` / `ModWoodFamilies` | core | 所有模块的木质聚合，barren 的木种在其自身模块注册 Family 实例，但类型定义在 core |
| `WindupAttackMob` + 蓄力 AI goal | core | 跨模块实体 AI 契约 |
| 制箭台：Block/BlockEntity/Menu/Screen/ArrowComponent/ArrowEffects | core | 矢术是核心玩法，箭物品可被任何模块产出 |
| 创造栏核心页 `ww_core:wild_wind` | core | 其他模块只向**自己**的 tab 或原版栏追加条目 |
| datagen 基础 Provider 风格/工具类 | core | 各模块的 Provider 是自己的实例，不共享有状态对象 |
| 泥沼蟹、恐鱼、蟹钳、延展药水、沼泽方块 | wilderness | 仅 wilderness |
| 焚烬木/灵焰木船、告示牌、砂岩/流沙、焦土 | barren | 仅 barren |
| Mixin | 各模块自有 `<modid>.mixins.json` | 一个 Mixin 只允许出现在一个模块；目标是原版/NeoForge 类，不是兄弟模块的类 |

## 3. 判定口诀

> 「另一个模块不装，这段代码还成立吗？」——成立→留在自己模块；不成立→下沉 core。
> 「这是内容还是机制？」——机制（聚合、契约、玩法系统）→ core；内容（方块、实体、生物群系装饰）→ 对应模块。

## 4. 注册表与命名空间

- 每个模块只往自己的 modid 命名空间注册：`ww_core:*` / `ww_wilderness:*` / `ww_barren:*`。
- **禁止**跨命名空间注册（历史教训：`ww_hfas` 时代混用）。
- 标签可引用他模块 id，但必须确认该 id 已注册（教训：`minecraft:flower_pots` 引用未注册花盆 → TagLoader 报错，BUG-TG-001）。

## 5. 资源边界

- `src/<模块>/resources` 只放该模块命名空间下的手写资源 + `META-INF/accesstransformer.cfg` + mixins.json。
- generated 产物（`build/generated/<模块>`）不属于任何仓库目录，禁止提交。
- 跨模块复用的贴图素材以"归属模块持有、他模块引用路径"处理，不复制文件。

## 6. 变更流程

改动本文件中"归属"表内任一条目属于架构变更，需：

1. 提交 ADR 到 [adr/](./adr/) 说明动机与迁移路径；
2. 更新 [ARCHITECTURE.md](./ARCHITECTURE.md) 对应章节；
3. 在 CHANGE_LOG 的 Changed 中记录。
