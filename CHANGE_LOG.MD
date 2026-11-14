# Changelog

All notable changes to the Wild Wind Module project will be documented in this file.

---

## [1.0.0] - 2026.04.15

### Added
- **区块加载替换机制** - 使用 `ChunkEvent.Load` 事件在区块加载时替换制箭台
- **制箭台GUI系统** - `FletchingTableBlock`、`FletchingTableBlockEntity`、`FletchingTableMenu`
- **数据生成系统** - 模型、战利品表、语言文件、数据映射生成
- **焚烬木套件** - 16种方块（原木、木板、楼梯、门等）
- **灵焰木套件** - 16种方块
- **杜鹃木套件** - 13种方块（不含树叶和树苗，使用原版）
- **树木自然生成** - 焚烬木（下界）、灵焰木（灵魂沙峡谷）、杜鹃木（繁茂洞穴）
- **杜鹃树增强** - Mixin 修改催熟行为，使用杜鹃木原木
- **配置文件系统** - `worldgen.replaceVanillaFletchingTable` 配置项
- **方块放置事件处理器** - `FletchingTableReplaceHandler`
- **焦灰草方块、焦土、焦灰草丛、焦灰枝条、仙人球**
- **制箭台（带GUI功能）**
- **创造模式物品栏自动排序**
- **木头去皮功能** - 使用 NeoForge DataMaps 注册去皮映射
- **树叶堆肥功能** - 树叶和树苗30%概率增加堆肥等级
- **木质方块燃料功能** - 木质方块可作为熔炉燃料（300 tick）
- **模型数据生成** - 使用 `BlockModelGenerators.family()` 和 `woodProvider()` 自动生成方块和物品模型
- **标签数据生成** - 使用 `ModBlockTagsProvider` 和 `ModItemTagsProvider` 为木质方块添加原版标签（logs、planks、wooden_stairs 等）

### Changed
- 移除 `StructureTemplateMixin`，改用事件监听方式
- 使用 `BiomeModifiers.RemoveFeaturesBiomeModifier` 移除原版杜鹃树
- 使用自定义 `BlockSetType` 和 `WoodType`
- 优化 `ModModelProvider` 使用 `family()` 方法批量生成木质方块模型

---

## 项目总结

### 模块

| 目录 | modid | 显示名 | 内容 |
|:----:|:-----:|:------|:-----|
| `src/core` | `ww_core` | Wild Wind: Core | 杜鹃木套件、玫瑰 / 灵魂玫瑰（丛）、制箭台与矢术、幼蛛 |
| `src/wilderness` | `ww_wilderness` | Wild Wind: Wilderness Update | 泥沼蟹、恐鱼、蟹钳 / 尖牙 / 万用蟹钳、延展药水、灵魂南瓜灯 |
| `src/barren` | `ww_barren` | Wild Wind: Barren Update | 焚烬木 / 灵焰木套件（含船、运输船、展示架、告示牌）、砂岩 / 沙 / 流沙 / 焦土 / 仙人球 / 可疑沙 |

- Java 包名 `org.polaris2023.wildwind.core` / `.wilderness` / `.barren`，主类 `WildWindCore` / `WildWindWilderness` / `WildWindBarren`
- `wilderness`、`barren` 在 `neoforge.mods.toml` 中以 `type="required"`、`ordering="AFTER"` 依赖 `ww_core`，Gradle 侧用 `implementation project(":core")`
- 共享组件留在 core：`WoodFamily`、`WindupAttackMob` 与 AI goal、制箭台菜单与方块实体
- 每个模块各自拥有 `mixins.json`、访问转换器、datagen 入口与 `assets/data` 命名空间
- 创造栏：`ww_core:wild_wind`（杜鹃木原木）、`ww_wilderness:wilderness`（灵魂南瓜灯）、`ww_barren:barren`（焚烬木原木）

### 技术要点

- 木质方块统一由 `WoodFamily` 聚合，模型 / 战利品 / 标签 / 数据地图 / 创造栏只遍历一次
- datagen 生成 blockstates / models / items / lang / tags / loot_table，输出到 `build/generated/<module>/{client,server}`；贴图与配方手写在 `src/<module>/resources`
- 制箭台：`FletchingTableBlock` + 4 槽方块实体（箭羽 / 箭杆 / 箭头 / 产物）；`ArrowComponent` 随箭矢 `pickupItemStack` 存读档，`AbstractArrowMixin` 应用实际效果
- 自定义木种只需注册 `BlockSetType` + `WoodType`；`WoodType` 名必须带命名空间，否则告示牌编辑界面会去 `minecraft:` 找 GUI 贴图
- 船 / 展示架 / 告示牌复用原版实体与方块实体类型（`BlockEntityTypeAddBlocksEvent`，不新建 BlockEntityType）

### 待办

- [ ] 更多下界植物
- [ ] 成就系统

---

## [Unreleased] - 2026.10.23

### Added
- **木材家族聚合** - 新增 `WoodFamily` 与 `ModWoodFamilies`，模型、战利品、标签、数据地图、创造物品栏统一遍历，不再各写一份 13~15 参数列表
- **方块战利品表** - 注册 `BlockLoot`，`ModLootProvider` 此前只挂了实体战利品，方块战利品表完全缺失
- **燃料数据地图** - 使用 NeoForge `furnace_fuels` 数据地图注册燃料（原木/木头/去皮/木板/楼梯/栅栏/栅栏门/压力板 300 tick，台阶 150，按钮与树苗 100）
- **补全方块标签** - `logs_that_burn`、`c:stripped_logs`、`c:stripped_woods`、`fences`、`fence_gates`、`wooden_fences`，原先只写在手写 JSON 里

### Changed
- 模型、战利品、标签、创造物品栏提供器改为遍历 `ModWoodFamilies.ALL`
- `ModItemTagsProvider` 改继承 `net.neoforged.neoforge.common.data.ItemTagsProvider`
- 创造物品栏的 62 行 `insertAfter` 链收敛为 `appendWoodSet()`
- 燃料由 `FurnaceFuelBurnTimeEvent` 事件改为数据地图驱动，移除 `FuelHandler`

### Fixed
- **活板门不掉落** - 原先误用 `createDoorTable`（带 `half=lower` 条件），天花板上的活板门破坏后无掉落，改为 `dropSelf`，与 `VanillaBlockLoot` 一致
- **木质方块被误加入 `needs_stone_tool`** - 导致木板、门、按钮等必须石级工具才能挖掘掉落，已移除该标签注册
- **Gradle 9 下资源重复项报错** - 原 `.cache` 排除模式相对 srcDir 根无效，已修正并显式设置 `duplicatesStrategy`

### Removed
- **回滚 PR #29 的资源污染** - 删除 438 个与 datagen 产物同名的手写 `blockstates` / `models` / `items` / `data` 文件，以及 130 个未注册方块（sign / boat / potted sapling）的孤儿资源，仅保留 80 个 datagen 无法重建的文件
- **被提交进 `src` 的 datagen HashCache** - 删除 `src/hfas/resources/.cache/`（9 个文件），它会令 datagen 误判"已生成"而跳过重写
- **重复的标签与语言文件** - 删除 15 个与 datagen 输出同名的 JSON（datagen 每次重新生成，无信息丢失）
- **改名前的重复贴图** - 删除 26 张与 `blaze_*` / `soul_*` 逐字节相同的 `cinder_*` / `ember_*`（含 `.mcmeta`），无信息丢失；另有 28 张没有对应新命名版本的贴图（船 / 箱船 / 告示牌 / 悬挂告示牌的实体贴图与物品图标、`gui/hanging_signs`、`screens`）全部保留，作为后续船与告示牌开发的素材
- **`ModBlockTagsProvider.INSTANCE`** - 无人使用的静态单例

---

## [Unreleased] - 2026.10.25

### Added
- **树苗花盆方块** - 注册 `potted_blaze_sapling` / `potted_soul_sapling`，使用原版 `FlowerPotBlock(树苗, props)` 构造器，构造时自动把树苗登记进原版空花盆的可放入映射表，无需额外的手工 `addPlant`
- **花盆模型与战利品表** - `ModModelProvider` 用 `createPlant()` 一次生成树苗交叉模型 + 花盆模型（含 blockstate），`BlockLoot` 用 `dropPottedContents()` 生成「花盆 + 树苗」两池掉落，与原版 `potted_oak_sapling` 一致
- **花盆方块本地化** - `block.ww_hfas.potted_blaze_sapling` / `potted_soul_sapling`，en_us 为 `Potted Blaze Sapling` / `Potted Soul Sapling`，zh_cn 为 `焚烬木树苗盆栽` / `灵焰木树苗盆栽`
- **食人鱼拍打音效** - 新增 `entity.piranha.flop` SoundEvent（`createVariableRangeEvent`）与 `subtitles.entity.piranha.flop` 字幕（en_us `Piranha flops` / zh_cn `食人鱼拍打`）

### Changed
- **食人鱼 `getFlopSound()`** - 由原版 `SoundEvents.SALMON_FLOP` 改为自有 `ModSoundEvents.PIRANHA_FLOP`，移除遗留的 `// WIP SOUND` 标记
- **`sounds.json` 暂借原版音源** - `entity.piranha.flop` 以 `sounds: [{ "type": "event", "name": "minecraft:entity.salmon.flop" }]` 引用原版鲑鱼拍打声；待有独立 `.ogg` 后把该条目替换为文件列表即可，事件 ID 与字幕键均无需变动

### Fixed
- **`minecraft:flower_pots` 标签加载失败** - 标签内引用的 `ww_hfas:potted_blaze_sapling` / `ww_hfas:potted_soul_sapling` 此前没有对应注册，`TagLoader` 报 `Couldn't load tag minecraft:flower_pots as it is missing following references`；现两个方块均已注册（BUG-TG-001）
- **杜鹃木 / 焚烬木 / 灵焰木木板无法合成** - `azalea_planks` / `blaze_planks` / `soul_planks` 的 `ingredients` 误写为单个方块 id（`ww_hfas:blaze_log` 等），改为物品标签 `#ww_hfas:azalea_logs` / `#ww_hfas:blaze_logs` / `#ww_hfas:soul_logs`，原木、木头与两种去皮变体都能参与合成（BUG-TG-002）
- **食人鱼 flop 无声音且字幕不显示** - `sounds.json` 曾把 `type` / `name` 写在事件层级并缺少 `sounds` 数组，`SoundEventRegistrationSerializer.getSounds()` 解析出空音源，事件播放被丢弃；已改为把 `type: "event"` 内嵌在 sound 条目中

---

## [Unreleased] - 2026.10.29

### Added
- **幼蛛实体** - 回溯移植 `Spiderling`：继承原版 `Spider`（保留攀爬与目标选择），实现 `GeoEntity` + `WindupAttackMob`，`registerGoals()` 中移除原版 `MeleeAttackGoal` 并改挂 `ChargingMeleeAttackGoal`，蓄力 13 tick 后扑击并把自身弹向目标
- **实体注册与生成** - `ModEntities.SPIDERLING`（`MobCategory.MONSTER`，0.55×0.35、`eyeHeight 0.15`、`notInPeaceful`）、`ModItems.SPIDERLING_SPAWN_EGG`、`EntitiesHandler` 中的属性（8 血 / 1 攻 / 0.25 速）与 `SpawnPlacementTypes.ON_GROUND` 生成规则、创造栏条目
- **GeckoLib 渲染** - `SpiderlingModel` / `SpiderlingRenderer` / `SpiderlingRenderState`，外加 `SpiderlingEyesLayer`（`AutoGlowingGeoLayer`，贴图 `textures/entity/spiderling_eyes.png`）
- **模型与贴图资源** - `geckolib/models/entity/spiderling.geo.json`、`geckolib/animations/entity/spiderling.animation.json`、`textures/entity/spiderling.png`、`textures/item/spiderling_spawn_egg.png`
- **战利品表** - `ModLootProvider` 生成两池：线 0~2 与蜘蛛眼 -1~1，均叠 `enchanted_count_increase`（抢夺），蜘蛛眼镜条件 `killed_by_player`，结构对齐原版 `entities/spider.json`
- **实体标签** - `ModEntityTypeTagsProvider` 追加 `arthropod` / `dismounts_underwater` / `no_anger_from_wind_charge`，与原版蜘蛛一致
- **音效字幕** - `ModSoundEvents` 新增 `entity.spiderling.ambient` / `.hurt` / `.death`，`sounds.json` 以 `type: "event"` 引用原版蜘蛛音源（与食人鱼 `flop` 同一做法），`Spiderling` 覆写 `getAmbientSound` / `getHurtSound` / `getDeathSound`
- **本地化** - `ZhEnCombineLangProvider` 追加实体名（`Spiderling` / `幼蛛`）与刷怪蛋名（`Spiderling Spawn Egg` / `幼蛛刷怪蛋`）；字幕键 `subtitles.entity.spiderling.*` 按原版文案格式补齐

### Changed
- **数据生成资源不手写** - 上游 diff 提交到 `src` 的 `assets/ww_hfas/items`、`models/item`、`lang`、`data/**` 均未带入，统一由 `runClientData` / `runServerData` 生成（与 PR #29 的资源去重约定一致）
- **字幕中文统一为原版格式** - 副标题由 `实体+动作`（`食人鱼袭击`）改为原版中文的 `实体：动作`（`食人鱼：袭击`、`幼蛛：嘶嘶`）；英文侧保持原版无冒号写法（`Piranha attacks`、`Spiderling hisses`），原版中英文本就不同构

### Fixed
- **`SpiderlingEyesLayer` 泛型编译失败** - `GeoRenderLayer` / `AutoGlowingGeoLayer` 要求 `R extends GeoRenderState`，而该接口由 GeckoLib mixin 在运行时注入 `EntityRenderState`，编译期不可见；现由 `SpiderlingRenderState` 显式实现 `GeoRenderState`（自持 `DataTicket` 数据表，覆写 `addGeckolibData` / `hasGeckolibData` / `getDataMap`），图层与渲染器统一使用真实类型，注册处的原始类型强转与 `@SuppressWarnings` 一并删除
- **属性翻译键前缀错误** - `RangedAttribute` 的 `descriptionId` 是构造参数原样保存，不按注册 id 派生；原值为 `attributes.ww_hfas.extra_item_pickup_range`，与 `Attributes` 中的 `attribute.name.armor` 规范不符，属性名会直接显示原始键。现已改为 `attribute.name.ww_hfas.extra_item_pickup_range`，`ModAttributes` 与 en_us / zh_cn 三处同步
- **字幕格式与原版不一致** - 原版 `entity.spider.step` 没有 subtitle（脚步声不产生字幕），此前为改字幕额外覆写 `playStepSound` 属多余，已连同 `entity.spiderling.step` 字幕一并移除

### Removed
- **脚步声字幕及其载体** - `ModSoundEvents.SPIDERLING_STEP`、`sounds.json` 的 `entity.spiderling.step` 条目、`Spiderling.playStepSound` 覆写；脚步声回落原版 `SoundEvents.SPIDER_STEP`，与原版蜘蛛行为一致
- **旧属性键** - `attributes.ww_hfas.extra_item_pickup_range`（en_us / zh_cn 各一条），随属性键修正移除

---

## [Unreleased] - 2026.10.30

### Added
- **砂岩 / 沙 / 花全家桶** - 从 `master/NeoForge-26.2`（`a40d1a4` 添加流沙 → `f5e4c7d` 注册WIP物品 → `ebd3762` 灵魂与石英全家桶）移植 21 种方块：`quicksand`、`suspicious_red_sand`、`soul_jack_o_lantern`、`chiseled` / `cut` / `nitrified` 砂岩八种、`quartz_sandstone`、`soul_sandstone`、`quartz_sand`、`quartz_quicksand`、`soul_quicksand`、`rose`、`soul_rose`、`soul_rose_bush`、`suspicious_quartz_sand`、`suspicious_soul_sand`
- **平滑砂岩** - `smooth_quartz_sandstone` / `smooth_soul_sandstone`，六面均为对应砂岩的顶面贴图（`quartz_sandstone_top` / `soul_sandstone_top`）、底面旋转 180 度；复用已有 `rotated_top_sandstone` 模板，不新增贴图。硝化系列不做平滑
- **`BlockEntityTypeMixin`** - 在 `BlockEntityType.isValid` 的 HEAD 放行自定义可疑沙，使它们能绑定原版 `BrushableBlockEntity`（可用刷子刷出物品）；`ww_hfas.mixins.json` 同步登记
- **砂岩模型生成辅助** - `ModModelProvider` 新增 `ROTATED_TOP_SANDSTONE` 模板与 `createSandstone` / `createRotatedTopSandstone` / `createVanillaTopBottomSandstone` / `createSmoothSandstone`，覆盖「侧面上顶底」「侧面自身上顶底」「顶底借用原版」「六面同图」四种贴图映射
- **战利品 / 标签 / 本地化** - `BlockLoot` 为 23 种新方块补掉落（三种可疑沙用 `createSilkTouchOnlyTable`）；方块标签补 `mineable_with_pickaxe` / `mineable_with_shovel` / `mineable_with_axe` / `piglin_repellents` / `small_flowers` / `flowers`；`en_us` / `zh_cn` 全量条目（WIP 物品保留上游的 `§4` 红色标记）
- **创造物品栏分类** - 模组 tab 内新方块改为按材质分组：石英岩类 / 灵魂岩类 / 硝化岩类 / 沙类 / 可疑沙类 / 花类 / 灯具类，类内按「基础 → 雕纹/切制 → 硝化 → 平滑」排列
- **模型与贴图资源** - 35 张方块贴图、`assets/ww_hfas/models/block/rotated_top_sandstone.json` 与 `BlockEntityTypeMixin.java` 自 `ebd3762` 原样检出，datagen 无法重建这三类文件

### Changed
- **方块标签注释格式** - `ModBlockTagsProvider` 的说明由「用锹挖掘」等形式改为类型式：制箭台 → 工作站类、可疑红沙 → 沙子类、灵魂南瓜灯 → 灯具类、石英沙/流沙/可疑沙 → 沙子类
- **数据生成资源不手写** - 模型、blockstate、战利品表、标签、语言文件均由 `runClientData` / `runServerData` 生成，未把上游提交到 `src` 的 `blockstates` / `models` / `data/**` 带入（与 PR #29 的资源去重约定一致）
- **移植取舍** - 上游 `f5e4c7d` 补挂 `BlockLoot` 的改动未搬，本分支 `ModLootProvider` 早已挂上；上游 `ModModelProvider` 仍为传 13~15 个参数的木材写法，本分支保持 `WoodFamily` / `ModWoodFamilies` 版

### Fixed
- **中文名用词** - `nitrified_red_sandstone` 由「硝化红色砂岩」改为「硝化红砂岩」；`suspicious_quartz_sand` / `suspicious_soul_sand` 由「可疑的…沙子」改为「可疑的…沙」

---

## [Unreleased] - 2026.11.02

### Added
- **三个独立创造标签页** - 拆分后每个模块各注册一页，不再挤在 core 的标签页里：`ww_core:wild_wind`（`tab.ww_core.name`，图标杜鹃木原木）、`ww_wilderness:wilderness`（`tab.ww_wilderness.name`，图标灵魂南瓜灯）、`ww_barren:barren`（`tab.ww_barren.name`，图标焚烬木原木）；三 jar 共存时三页并列，只装某一个时也只出现它自己那页
- **各模块向原版栏追加条目** - core（`groups/ModCreativeTabs`）：建筑方块（杜鹃木套件、玫瑰系列）与自然方块；wilderness（`groups/WildernessCreativeTabs`）：自然方块（灵魂南瓜灯）与工具与实用（蟹类桶、万用蟹钳）、食物与饮品、材料；barren（`groups/BarrenCreativeTabs`）：建筑方块与自然方块（焚烬木 / 灵焰木套件、流沙、石英沙、小型仙人球）
- **制箭台物品形态** - `ModBlocks` 用 `ITEMS.registerSimpleBlockItem(FLETCHING_TABLE)` 注册，方块进入创造栏并可放置
- **制箭台交互** - `FletchingTableBlock` 实现 `EntityBlock`，`useWithoutItem` 打开菜单；`getMenuProvider` 传入方块实体自身作为 `Container` 并带 `ContainerLevelAccess.create(level, pos)`
- **方块实体与结算** - `FletchingTableBlockEntity` 实现 `Container`（4 槽：箭羽 / 箭杆 / 箭头 / 产物，枚举 `Slots`），三格齐备且材料合法时 `updateResult` 生成 `ARROWS_PER_CRAFT = 4` 支箭，`consumeInputs` 在取走产物后各扣 1 并重算；数据用 `ValueOutput` / `ValueInput` 存档
- **制箭台菜单** - `FletchingTableMenu`：三个输入槽用 `FilteredSlot` 按 `ArrowFletching` / `ArrowShaft` / `ArrowHead.fromItem` 过滤，产物槽 `ResultSlot` 只取不放；槽位坐标沿用锻造台布局（(8,48)/(26,48)/(44,48)、产物 (98,48)、背包 (8,84) 起），`quickMoveStack` 以 `INV_START = CONTAINER_SIZE` 为界
- **客户端界面** - `CoreClientScreens` 监听 `RegisterMenuScreensEvent` 注册 `FletchingTableScreen`；界面直接引用原版 `minecraft:textures/gui/container/smithing.png`（不复制进 jar，避免再分发 Mojang 资源）
- **枚举名反序列化** - `ArrowFletching` / `ArrowShaft` / `ArrowHead` 新增 `fromName(String)`，`ArrowComponent` 的 `CODEC` / `STREAM_CODEC` 改用它

### Changed
- **原版制箭台放置替换** - 26.2 的 `BlockItem#place` 不再触发 `BlockEvent.EntityPlaceEvent`，改为 `BlockItemMixin` 在 `getPlacementState` 中用 `@ModifyExpressionValue` 把 `Blocks.FLETCHING_TABLE` 换成模组方块：放置校验、音效、消耗物品、方块实体创建仍走原版流程；`FletchingTableReplaceHandler` 只保留区块加载时替换旧世界方块
- **菜单连接真实容器** - 此前 `getMenuProvider` 用 `SimpleContainer`，界面与方块实体数据不同步（放进去的材料会没、产物不结算），改为传方块实体本身
- **制箭台界面标题** - `container.fletching_table` 由「制箭台 / Fletching Table」改为「制作箭 / Arrow Crafting」，与方块名区分；方块名仍为「制箭台 / Fletching Table」，不受影响
- **界面图标尺寸** - 左上角光灵箭图标由 16×16 放大到 24×24，居中于被遮盖的原版锤子区域（`ICON_SIZE` 常量控制），锤子仍被完全遮挡

### Fixed
- **制箭台无法获取与放置** - 方块缺少物品形态，创造栏里没有条目、也无法放下，已补 `registerSimpleBlockItem`
- **放下后变回原版制箭台 / 放下即消失** - 原先靠 `RightClickBlock` 事件延迟替换，存在回滚与进度统计问题，改为 mixin 直换 `getBlock()` 后一次性生成正确方块
- **槽位索引错乱** - `FletchingTableMenu` 构造时 `addSlot(this.addSlot(...))` 把玩家背包槽位重复登记，且容器尺寸与槽位布局不匹配；已按 4 槽容器 + 36 格背包重排
- **网络包解码失败导致打开界面崩溃** - `Failed to decode packet 'clientbound/minecraft:container_set_slot'`：`ArrowComponent` 的 `STREAM_CODEC` 用 `valueOf("feather")` 反序列化，而枚举常量为大写，抛 `IllegalArgumentException`；改走 `fromName` 后正常
- **打开界面无内容（白屏/原版界面）** - 未注册 `MenuScreen`，`MenuScreens` 拿不到构造器；补 `CoreClientScreens` 注册
---

## [Unreleased] - 2026.11.03

### Added
- **红色流沙** - barren 新增 `ww_barren:red_quicksand`：`BarrenBlocks.RED_QUICKSAND`（`MapColor.COLOR_ORANGE`、`strength(0.5F)`、`SoundType.SAND`，与 `quicksand` 同构），复用已有贴图 `ww_barren:block/red_quicksand`；`blockItems` 注册方块物品，`BarrenBlockLoot` 补 `dropSelf`，`BarrenModelProvider` 走 `createTrivialCube`，`BarrenBlockTagsProvider` 进 `mineable_with_shovel`，创造栏（模组页 + 原版自然方块页）紧随 `quicksand` 排列，`en_us` / `zh_cn` 为 `§4Red Quicksand` / `§4红流沙`
- **矢术效果层** - 新增 `arrow/ArrowEffects` 与 `mixin/AbstractArrowMixin` / `mixin/AbstractArrowAccessor`（`ww_core.mixins.json` 已登记）。组件不用额外 attachment：箭矢实体自带的 `pickupItemStack` 原版就会存读档，飞行中直接读回 `ArrowComponent`，没有组件的普通箭全部走原版逻辑
  - **箭羽**：出手速度倍率（`shoot` 的 power，幻翼膜 1.5×）、额外散布（`shoot` 的 uncertainty）
  - **箭杆**：`getDefaultGravity` 重写重力（旋风棒 0、骨头 1.35×、竹子 0.7×）、烈焰棒逐 tick 火尾粒子 + 落点生成火焰、旋风棒落点风弹爆炸（不破坏方块）、竹子自动补 1 级穿透、骨头 +1 伤害
  - **箭头**：火药爆炸（`ExplosionInteraction.NONE`，不破坏地形）、风弹爆炸、粘液球弹开命中目标、烈焰粉点燃 100 tick、岩浆膏点燃 + 弹开、紫颂果目标 ±8 随机传送、末影之眼与射手交换位置、煤炭在命中面外侧放火把、紫水晶破坏周围植物（`VegetationBlock`）与玻璃类（`SoundType.GLASS`）、海晶碎片 `getWaterInertia` 提到 1.0（无视流体重力）
  - **不可回收**：`tryPickup` 对 `ArrowComponent.isNotRecoverable()` 成立的箭矢直接 `discard()`
- **矢术提示框** - `client/CoreClientTooltips` 监听 `ItemTooltipEvent`，带组件的箭用灰色文字列出 `ArrowComponent.getFullDescription()` 的箭羽 / 箭杆 / 箭头说明

### Changed
- **箭杆槽材料：风弹 → 旋风棒** - `ArrowShaft.WIND_CHARGE` 改为 `ArrowShaft.BREEZE_ROD`（`breeze_rod`，`Items.BREEZE_ROD`，仍为无重力 + 落点风弹爆炸 + 不可回收），制箭台中间格（`SLOT_SHAFT`，slot 1）现在吃旋风棒；箭头槽（slot 2）仍为风弹，`ArrowHead.WIND_CHARGE` 保持原样
- **骨头箭杆重力** - `ArrowShaft.BONE` 的 `gravityMultiplier` 由 `1.0F` 提到 `1.35F`，让「下坠快」在数据上真正成立（原值与木棍相同）
- **清理过时 API** - `builtInRegistryHolder()`（core / barren 的 `DataMapsProvider`、`BlockTagsProvider`、`ItemTagsProvider` 共 6 处）改为 `BuiltInRegistries.BLOCK` / `ITEM` 的 `getResourceKey(...).orElseThrow()`；花盆的 `FlowerPotBlock(Block, Properties)`（已标 `@Deprecated`）改为 NeoForge 推荐的 `FlowerPotBlock(Supplier<FlowerPotBlock>, Supplier<? extends Block>, Properties)`，两处 `@SuppressWarnings("deprecation")` 一并删除。`-Xlint:deprecation` 全量重编译后 core / barren 均无警告，数据生成输出与改动前一致

### Fixed
- **旧存档箭杆读取崩溃** - `ArrowShaft.fromName` 对历史值 `wind_charge` 做兼容映射到 `BREEZE_ROD`，避免旧箭矢在加载时报 `Unknown arrow shaft: wind_charge`
- **右键只拿到一半产物** - `FletchingTableMenu.ResultSlot` 覆写 `tryRemove`：原版「右键取半组」路径会按 `(count + 1) / 2` 从产物槽取走 2 支，而 `onTake` 仍按整份消耗材料。现在产物不可拆分，右键 / Q / 与光标同物品合并一律整份取走，光标放不下就一个都不取
- **shift 只能合成一次** - `quickMoveStack` 在产物槽搬完后会走公共尾巴 `slot.set(ItemStack.EMPTY)`，把 `onTake → consumeInputs → updateResult` 刚重算好的产物清掉，而产物槽的 `setItem` 不再触发重算，于是产物永久为空。产物槽分支改为 `onTake` 后直接返回，不再动槽位
- **关界面材料留在方块里** - `FletchingTableMenu` 补 `removed(Player)`，关闭界面时把三个输入槽的材料塞回玩家背包（放不下就丢地上）并清空方块实体，与工作台一致

---

## [Unreleased] - 2026.11.06

### Added
- **焚烬木 / 灵焰木的船与运输船** - `ww_barren` 新增 `BarrenEntities`（`cinder_boat` / `cinder_chest_boat` / `ember_boat` / `ember_chest_boat`）与 `BarrenItems`，实体用原版 `Boat` / `ChestBoat`、物品用 `BoatItem`（`stacksTo(1)`），尺寸 1.375×0.5625、眼睛高度 0.5625、`clientTrackingRange(10)` 全部照搬原版；四种船都用 `EntityType.Builder#fireImmune()` 注册，免疫火焰与岩浆伤害；实体类改为 `entity/WildWindBoat` / `WildWindChestBoat`，只覆写 `canBoatInFluid(FluidState)` 把岩浆也算作可航行流体，于是岩浆里同样有浮力、能正常划桨；`BarrenClientEvents` 烘焙 `BoatModel` 的船 / 运输船模型层并注册 `BoatRenderer`，贴图为 `textures/entity/boat/<wood>.png`、`textures/entity/chest_boat/<wood>.png`
- **焚烬木 / 灵焰木的展示架** - `ShelfBlock` 直接复用原版 `BlockEntityTypes.SHELF`，通过 `BlockEntityTypeAddBlocksEvent` 登记；掉落、`wooden_shelves` 标签、`createShelf` 模型与配方（6 去皮原木）均按原版
- **焚烬木 / 灵焰木的告示牌与悬挂告示牌** - `StandingSignBlock` / `WallSignBlock` / `CeilingHangingSignBlock` / `WallHangingSignBlock`，方块实体分别挂到原版 `SIGN` / `HANGING_SIGN`；`SignItem` / `HangingSignItem` 用 `useBlockDescriptionPrefix()` 注册，配方与配方解锁进度按原版
- **船 / 告示牌 / 展示架素材** - 木材 id 统一为 `cinder`（**青灰**，焚烬木）/ `ember`（**橙红**，灵焰木）；船的实体与物品图标直接复用同名素材；告示牌编辑界面底板 `gui/signs/<wood>.png`（24×26）为脚本自绘：板面 0~11 行是 3 条横板（颜色取自本模组 `*_planks.png` 每 4 行一带的平均色 + 确定性噪点，带拼缝与 1px 描边），立柱 12~25 行 2px 取木板色压暗，其余透明，轮廓与原版 `oak.png` 一致；生成脚本存于 `textures_generator/gen_sign_gui.ps1`；方块贴图 `block/<wood>_sign|hanging_sign|shelf` 仍为按原版橡木对应贴图明暗套用本模组木材底色

### Changed
- **`WoodType` 注册名带命名空间** - `ww_barren:cinder` / `ww_barren:ember`：原版 `SignEditScreen` / `HangingSignEditScreen` 用 `Identifier.parse(woodType.name() + ".png")` 拼 GUI 贴图路径，不带冒号会默认落到 `minecraft:` 命名空间；`WoodType` 不是注册表对象、记录里只有 `String name`，只能这样传命名空间
- **运输船英文名对齐原版** - `item.ww_barren.*_chest_boat` / `entity.ww_barren.*_chest_boat` 为 `Cinder Boat with Chest` / `Ember Boat with Chest`（中文仍为「运输船」）

### Fixed
- **告示牌编辑界面底板是纯色块** - `gui/signs/<wood>.png` 原先只是把 16×16 木板贴图缩放到 24×26，整张不透明、没有告示牌轮廓，编辑时看到的是一块方形色斑；已按 `SignEditScreen` 的 `TEXTURE_WIDTH/HEIGHT = 24×26`、`POST_HEIGHT = 14` 重画：0~11 行为板面（含边框、拼缝、铆钉），12~25 行仅中间 2px 立柱，其余透明，墙挂告示牌只画前 12 行也正常
- **旋风棒箭杆的箭矢永久悬停** - `ArrowShaft.BREEZE_ROD` 让 `getDefaultGravity()` 永远返回 0，箭矢永不落地，也就永远不进入原版 `tickDespawn()` 的 1200 tick 倒计时；同时空气阻力把速度衰减到接近 0，表现为箭矢一直飘着、最后像静止。现在无重力只在**出手后前 100 tick 且不在流体中**生效，之后恢复正常重力下坠并触发落点风弹爆炸，入水 / 入岩浆立即恢复重力
- **告示牌 / 悬挂告示牌显示原始翻译键** - 手写 `ITEMS.registerItem(...)` 时缺少 `useBlockDescriptionPrefix()`，物品描述键默认是 `item.ww_barren.<wood>_sign`，而语言文件只有 `block.*`；补上后正常显示译名
- **告示牌相关译名缺失** - 补 `block.ww_barren.*_wall_sign` / `*_wall_hanging_sign` 与 `entity.ww_barren.*_boat` / `*_chest_boat`，`/summon` 等提示不再出现原始键
- **焚烬木 / 灵焰木地图色与树叶染色反了** - `MapColor` 与 `WoodFamily.leavesTint` 按旧设计写成「焚烬木橙、灵焰木青」，与实际贴图相反（实际贴图 `cinder_*` 青灰、`ember_*` 橙红）；已按贴图互换，地图颜色与树叶物品图标两者一致
- **资源 id 大写残留** - `stripped_CINDER_log` / `potted_EMBER_sapling` / `add_CINDER_tree` 等混入大写字母导致 `IdentifierException: Non [a-z0-9/._-] character in path of location`，datagen 直接起不来；改为全小写后重跑 `runServerData` / `runClientData`，`build/generated/barren` 内的 blaze / soul 旧产物已清空重建

### Changed（命名）
- **木材 id 统一为 `cinder` / `ember`** - 会话中曾短暂改为 `blaze` / `soul`，已全部回退：方块、物品、实体、`WoodType`、贴图、模型、配方、进度、标签、语言文件一并改名；`core` 中早期遗留的 28 张同系列素材（船 / 箱船 / 告示牌 / 悬挂告示牌的实体贴图与物品图标、`gui/hanging_signs`、`screens`）也保留并同步改名为 `cinder_*` / `ember_*`，未删除



---

### Fixed

- **datagen / 运行期 LinkageError 彻底修复** - 根因是 `imp` 被当作**运行期库**解析（日志可见 `ww-imp-mc26.2-1.0.0.jar (project :imp)`）。FML 只接管 MC/NeoForge 包，其它包委托父加载器（app），于是 imp 类由 app 加载；而 imp 的方法签名里含 `net.minecraft.resources.Identifier`，app 于是去自己的类路径找 MC 类，撞上 TRANSFORMER 已加载的同名类 → `loader constraint violation`。
  - 修法：`api(project(":imp"))` 改为 `compileOnly(project(":imp-annotations"))`；imp 的**预处理输出以源码目录形式并入 core** 的 main source set（`srcDir(imp/build/preprocessed/java)`，排除 `sample`），使其类编译进 core 的输出、由 mod 加载器加载；删除 jar 阶段的字节码合并（已无必要）
  - 追加修正：imp 源码**只能并入 core**。最初并入所有模块时，多个模块各自包含同一个包，触发 JPMS 拒绝（`Module ww_wilderness contains package org.polaris2023.wildwind.imp, module ww_core exports package ...`）；wilderness / barren 通过 `implementation(project(":core"))` 获得
  - 验证：三个模块的 `runServerData` / `runClientData` 全部成功——core（Tags / Loot / Registries / Data Maps / Recipes / Model Definitions / en_us / zh_cn）、wilderness（含 worldgen/biome 标签、Recipes）、barren（含 Data Maps），均 `BUILD SUCCESSFUL`

---

## [Unreleased] - 2026.11.22

### Changed

- **实体渲染全面脱离 GeckoLib，改用原版渲染管线（ADR-0006）** - 三个实体（幼蛛 / 泥沼蟹 / 恐鱼）改用原版 `ModelPart` + 自研 Bedrock 动画运行时，`geckolib` 依赖从构建脚本、仓库声明与 `neoforge.mods.toml` 中彻底移除：
  - 新增 `imp/client/bedrock/`：`BedrockAnimation`（关键帧求值，线性 + Catmull-Rom）、`BedrockGeometry`（`.geo.json` 解析）、`GeoModelBuilder`（转 `ModelPart` 层级）、`GeoAnimationState`（状态机）、`GeoResources`（加载 + 每帧驱动）
  - 新增 `ImpRig`（渲染器 / 模型 / 渲染态基类，三族分段）、`ImpGeoRig`（装配门面）、`ImpAnimatedEntity`（实体侧动画契约）
  - Blockbench 导出的 `.geo.json` / `.animation.json` **原文件直接复用**，不需要格式转换
  - 提供三个版本无关钩子（`textureFor` / `scaleFor` / `rotationFor`）替代被移除的 GeckoLib 覆写，恢复变种纹理、幼年体型、攀爬倾斜

### Fixed

- **攻击/问候动画在客户端从未播放（既有 bug）** - `triggerAnim` 在服务端写普通字段，客户端收不到。改用 `SynchedEntityData` 同步计数器（每次触发 +1 而非置 boolean，以便边沿检测区分相邻两次），渲染侧每帧比对，只在值变化时触发一次
- **`EntitiesHandler` 静态初始化 NPE** - `static` 块里读 `ModEntities.SPIDERLING.get()`，而 `@EventBusSubscriber` 自动注册本身触发类初始化，此时 `DeferredRegister` 尚未绑定，抛 `Trying to access unbound value`。声明移入 `onAttributeCreate` 事件回调
- **刷怪蛋 `Item id not set` NPE** - `ImpLegacy.spawnEgg(...)` 接收立即求值的 `EntityType`，但物品注册阶段实体类型未绑定。改为接收 `Supplier`（调用点传 `ModEntities.SPIDERLING::value`），推迟到真正需要时求值
- **`preprocessImpSources` 切换版本族时残留旧产物** - 任务被判定 up-to-date 时不会执行 `main()` 里的目录清理，导致读到上一族的预处理产物、报出大量"包不存在"假错误。加 `outputs.upToDateWhen { false }`
- **configuration cache 之前是坏的** - `preprocessImpSources` 的 `doFirst` 闭包捕获 Provider/脚本对象；`checkImpBoundary` / `checkVersionPairing` 的 `doLast` 闭包同理。前者把清理移入预处理器 `main`，后者显式声明 `notCompatibleWithConfigurationCache`

### 验证

```
26.2 clean build                BUILD SUCCESSFUL
core @26.2 / 1.21.1 / 1.20.6    0 / 0 / 0 错误      ← 首次三族全绿
:imp:build @三族                BUILD SUCCESSFUL ×3
:imp-processor:test             20 项全过
checkImpBoundary @三族          违规 0 / 0 / 0       ← 迁移前 5 处
```

datagen 六个任务全部成功，产物与基线**逐字节一致**（core 154、wilderness 72、barren 514，丢失或改动 0）。

### 已知限制

同屏同类实体的循环动画相位一致（`ModelPart` 树按实体类型共享，26.x `EntityModel` 要求构造期固定 root）；动画时间走 `System.nanoTime()` 差分而非 tick。两条均已写入 ADR-0006，实际观感需一次 `runClient` 人工确认。

---

## [Unreleased] - 2026.11.21

### Changed

- **datagen 六类全部完成 ADR-0005 三层迁移** - tags / models / loot / recipes / datapacks / datamaps 均拆为「规格（纯数据，三族共用）+ 发射器（`@Since`/`@Until` 分段）+ 薄适配」：
  - `imp/data/` 新增 10 个文件：`ImpLootSpec`/`ImpLootWriter`、`ImpRecipeSpec`/`ImpRecipeWriter`、`ImpDatapackSpec`/`ImpDatapackWriter`、`ImpDataMapSpec`/`ImpDataMapWriter`（tags 与 models 此前已完成）
  - 各模块 main 新增规格类：`ModTagSpecs`/`ModModelSpecs`/`ModLootSpecs`/`ModRecipeSpecs`/`ModDatapackSpecs`/`ModDataMapSpecs` 及 wilderness / barren 的同名版本
  - 所有 Provider 收缩为薄适配；`ImpModelSpec` 新增 `Cube` / `StoneSet` 变体

### Fixed

- **wilderness / barren 的 `runClientData` 长期失败** - `NoClassDefFoundError: core/datagen/provider/MissingModels`：它们的模型 Provider 直接依赖 core 的 datagen source set，而运行期 core 以 jar 加载（不含 datagen 类）。模型能力迁入 imp 后两个模块不再依赖 core 的 datagen，client datagen **首次跑通**（wilderness +36、barren +362 个产物）
- **configuration cache 之前是坏的**（三处独立报告）：
  - `:imp:preprocessImpSources` 的 `doFirst` 闭包捕获 Provider/脚本对象 → 清理逻辑移入预处理器 `main`（新增 `deleteTree`），任务改用 `args(...)` 传不可变 `File` 路径
  - `checkImpBoundary` / `checkVersionPairing` 的 `doLast` 闭包同样捕获脚本对象 → 显式 `notCompatibleWithConfigurationCache(...)`，让 Gradle 只对这两个任务禁用 CC，而不是让整次构建失败
  - 修完后 `./gradlew build` 与三族 `:imp:build` 在**开启 configuration cache** 下全部通过

### 验证

三模块 datagen 与基线逐字节对比：

```
core        154 → 154   丢失或改动 0 ; 新增 0
wilderness   36 →  72   丢失或改动 0 ; 新增 36   ← 修复 client datagen
barren      152 → 514   丢失或改动 0 ; 新增 362  ← 修复 client datagen
```

**"丢失或改动 = 0"** 证明六类迁移未改变任何已有产物字节。另：`clean build` ✅、20 项测试 ✅、三族 `:imp:build --rerun-tasks` ✅、`checkImpBoundary` 无新增违规。

新增工具 [tools/datagen-diff.ps1](tools/datagen-diff.ps1)：`-Snapshot` 建基线 / `-Verify` 重跑对比（PowerShell 5.1 需 UTF-8 BOM，已处理）。

---

## [Unreleased] - 2026.11.20

### Changed

- **模型 datagen 改为规格驱动（ADR-0005 第二类）** - 模型是两代 API 差异最大的一类（`javap` 实测：26.x 用 `BlockModelGenerators` 的 `woodProvider`/`family`/`createTintedLeaves`，1.21.1 用 NeoForge 的 Forge 风格 `BlockStateProvider`，且 `createDoor`/`createTrapdoor` 在旧版是包私有）：
  - 新增 `imp/data/ImpModelSpec`（sealed 接口，6 个变体：`WoodSet` / `Cross` / `DoublePlant` / `BuiltinDoublePlant` / `VanillaLike` / `FlatItem`），纯数据、零 MC 类型
  - 新增 `imp/data/ImpModelWriter`：26.x 分支完整实现；旧版分支**显式抛 `UnsupportedOperationException`** 并附待办说明（原因见 ADR-0005）
  - 新增 `core/data/ModModelSpecs`；`ModModelProvider` 只剩交出生成器对象

### Fixed

- **`soul_rose_bush` 模型形状差异（由 diff 发现）** - 26.x 内置 `createDoublePlantWithDefaultItem` 生成 `_bottom`/`_top` 两个模型，而 `MissingModels.doublePlant` 是单模型实现；原 Provider 对 `soul_rose_bush` 与 `wither_rose_bush` 用了不同做法。规格现区分 `BuiltinDoublePlant` 与 `DoublePlant`，与原实现一一对应，diff 归零

### 验证

`core` 的 datagen 产物与基线**逐字节一致（154 个文件）**；三族 `:imp:build` 通过；`clean build` + 20 项测试通过。

---

## [Unreleased] - 2026.11.19

### Changed

- **core 的三个标签 Provider 改为规格驱动（ADR-0005 落地）** - 把"生成什么"迁进纯数据规格，Provider 只剩薄适配：
  - 新增 `core/data/ModTagSpecs`（main 源码，零 MC 类型）：`blockTags()` / `itemTags()` / `entityTypeTags()`
  - `ModBlockTagsProvider` / `ModItemTagsProvider` / `ModEntityTypeTagsProvider` 改为 `ImpTagWriter.apply(specs, appenders)`，各自只剩三行 `return tag(key);`
  - 新增 imp 辅助 `ImpIds.fromString` / `ofHolder` / `ofTag`：用 `toString()` 中转，**单实现跨版本**（`DeferredHolder.getId()` 的返回类型虽不同，但只需字符串）

### 验证

按"基线快照 → 迁移 → 重跑 → 逐字节 diff"流程验证，三次独立零差异：

| 模块 | 对比文件数 | 结果 |
|---|---|---|
| core | 154（server+client，排除 `.cache`） | ✅ 哈希全一致 |
| wilderness | 36（server） | ✅ 哈希全一致 |
| barren | 152（server） | ✅ 哈希全一致 |

---

## [Unreleased] - 2026.11.18

### Added

- **datagen 的中间层兼容机制（ADR-0005）** - 把 datagen 拆成「规格 / 发射器 / 薄适配」三层，使"生成什么"三族共用：
  - `imp/data/ImpTagSpec`：`(Kind, tag ImpId, values List<ImpId>)` + `Collector`，纯数据、零 MC 类型
  - `imp/data/ImpTagWriter`：`apply(List<ImpTagSpec>, Appenders)`，内部按版本分段：
    - `@Since("26.1")`：`net.minecraft.data.tags.TagAppender.addAll(Stream<ResourceKey<T>>)`
    - `@Until("26.1")`：`IntrinsicHolderTagsProvider.IntrinsicTagAppender.add(T)`，用 `@Name` 统一方法名
  - `ImpTagWriter.Appenders`：内容模块实现的薄适配（每族三行 `return tag(key);`）
- **三族实测** - `:imp:build` 在 26.2 / 1.21.1 / 1.20.6 全部通过，意味着**现代与旧版两套 API 分支都经过真实编译验证**，不是"写了没编过"

### 说明

不可消除的分叉：`class X extends <版本专属 Provider>`——类只能继承一个父类，注解删的是成员而非 `extends`。因此每个 Provider 仍需壳，但壳从"整套逻辑"缩到"三行适配"。

---

## [Unreleased] - 2026.11.17

### Added

- **datagen 独立 source set 与按版本分叉（阶段 E）** - datagen 从 `main` 拆出，成为 Gradle `datagen` source set，按版本族选目录：
  - 目录约定 `src/<模块>/datagen/<族>/java`；现有 34 个 datagen 文件迁至 `src/<模块>/datagen/26.x/java/...`
  - 旧族目录不存在时该编译任务为 `NO-SOURCE`，**datagen 不再阻塞旧版模块构建**（此前 34 个文件混在 main 里，1.20/1.21 连编译都无法开始）
  - `mods { register(...) { sourceSet(datagen) } }`：让 `GatherDataEvent` 能发现 datagen 类
  - `check` 依赖 `compileDatagenJava`，CI 可验证 datagen 可编译
  - wilderness/barren 的 datagen 通过 `gradle.projectsEvaluated` 延迟挂载 core 的 datagen 输出（复用 `MissingModels`、`BlockLoot` 等工具）

### 实测事实（决定 datagen 必须按版本分叉）

| API | 26.2 | 1.21.1 | 1.20.6 |
|---|---|---|---|
| `net.minecraft.client.data.models.ModelProvider` | ✅ | ❌ 包不存在 | ❌ 包不存在 |
| `net.minecraft.data.models.ModelProvider` | ❌ | ✅ | ✅ |

类声明无法用注解表达分叉（一个类只能继承一个父类），因此 datagen 只能分目录。

### Changed

- **边界守卫扫描范围随 datagen 拆分调整** - 只扫描当前族的 datagen 目录（`src/<模块>/datagen/<族>/java`）。效果：`checkImpBoundary -PtargetMcVersion=1.21.1` 从 12 处降到 **5 处**，剩余全为结构性不可迁移项（4 处 GeckoLib `getTextureResource` 覆写签名 + 1 处 `MudcrabVariant.createKey` 形参类型）

### Fixed

- **datagen 运行期 LinkageError** - 此前把 `main.compileClasspath` / `runtimeClasspath` 直接并入 datagen source set，导致 `minecraft-patched-*.jar` 进入运行期，MC 类同时被 `app` 与 `TRANSFORMER` 加载（`loader constraint violation: net.minecraft.resources.Identifier`）。现改为以 **compileOnly 依赖**继承编译类路径、运行期只加 `main.output`

### 未解决（已记录）

`runServerData` 在 tags / loot 跑完（本次生成 64 个文件）后，于 `ModDatapackProvider` 的 `RegistrySetBuilder$RegistryStub.apply` 处仍报同一 LinkageError。类加载器日志显示 datagen 类已正确进入 `ww_core` content loader，触发点在 MC 内部反射路径，判断与 source set 拆分无关，需单独立项排查。

---

## [Unreleased] - 2026.11.16

### Added

- **CI 版本矩阵（ADR-0003 阶段 D）** - `.github/workflows/build.yml` 重构为三个 job：
  - `primary`：26.2 全量 `./gradlew build`（含边界守卫与配对守卫），上传三个 mod jar + imp jar
  - `imp-matrix`：`target ∈ {1.20.6, 1.21.1, 26.2}` 三族分别 `:imp:build`（必过），上传各版本 imp jar
  - `content-matrix`：手动触发（`workflow_dispatch` + `full_matrix`）构建 1.20.6 / 1.21.1 的内容模块，`continue-on-error`——当前预期失败，用于观测迁移进度
  - 并发控制：同分支新提交自动取消上一次运行
- **版本族单一事实源** - 新增 `gradle/version-families.properties`（`族=mc|neoForge|java`），根脚本、`imp/build.gradle.kts` 与 CI 都从它取值，消除"三处各写一份表"的漂移
- **`checkVersionPairing` 守卫** - 校验 `targetMcVersion` / `minecraftVersion` / `neoVersion` 属于同一版本族，防止产出"标着 1.21 却按 26.2 编译"的假产物；已挂到各 mod 模块的 `check` 之前；确需放行时用 `-PallowVersionMismatch=true`

### Changed

- **mod 模块的字节码级别跟随目标版本** - 根脚本 `options.release` 由 `javaReleaseForVersion(targetMcVersion)` 推导（1.20.x→17/21、1.21→21、26.x→25），此前固定 25 会给 1.20 族产出无法加载的字节码（实测：26.2 → major 69、1.20.6 → major 65）

### 验证

`checkVersionPairing` 四场景实测：默认通过；仅传 `-PtargetMcVersion=1.21.1` 失败（`minecraftVersion=26.2 ≠ 族 '1.21' 期望 1.21.1`）；三者成对通过；`-PneoVersion=21.1.209` 配 1.20.6 失败（`neoVersion=21.1.209 ≠ 族 '1.20' 期望 20.6.144`）。

---

## [Unreleased] - 2026.11.15

### Changed

- **wilderness / barren 的 ID 出口同样收口到 imp** - `WildWindWilderness.id(...)` / `WildWindBarren.id(...)` 返回类型改为 `ImpId`，内部转调 `ImpIds.idOf(...)`：
  - 16 个文件、30 处调用点补 `.unwrap()`（`MudcrabModel`、`PiranhaModel`、`MudcrabVariant`、`ClientOmniClawTooltip`、`OmniClawItem`、`ServerBoundSelectOmniClawItemPayload`、`DatadrivenRegistryKey`、`ModMobEffects`、`ModBiomeTags`、`ModBlockTags`、`ModItemTags`、`WildernessBiomeModifiers`、`BarrenClientEvents`、`BarrenModelProvider`、`BarrenBiomeModifiers`、`BarrenConfiguredFeatures`、`BarrenPlacedFeatures`）
  - `OmniClawItem` 的三个属性修饰符 ID 字段由 `Identifier` 改为 `ImpId`；`ClientOmniClawTooltip` 的 4 个精灵图字段与数组改为 `ImpId`
  - 清理 5 处不再需要的 `Identifier` 导入（`WildWindWilderness`、`WildWindBarren`、`OmniClawItem`、`ClientOmniClawTooltip`、wilderness 的 `ModDataComponents`）

### 结果

- `checkImpBoundary -PtargetMcVersion=1.21.1`：19 处 → **12 处**，且剩余全部为结构性不可迁移项：
  - **7 行 datagen**（`BarrenItemTagsProvider`×2、`BarrenModelProvider`、`MissingModels`、`ModItemTagsProvider`×2、`ModModelProvider`）：`net.minecraft.client.data.models.*` 是 1.21.4+ 结构，旧版需完全不同的 Provider
  - **4 行 GeckoLib 覆写签名**（`MudcrabModel`、`PiranhaModel`、`SpiderlingModel`、`SpiderlingEyesLayer` 的 `getTextureResource`）：返回类型由 GeckoLib 决定
  - **1 行外部 API 类型**（`MudcrabVariant.createKey(Identifier)`）：形参类型源自 `TemperatureVariants` 常量

---

## [Unreleased] - 2026.11.14

### Added

- **`ImpId` 跨版本稳定句柄** - 新增 `org.polaris2023.wildwind.imp.ImpId`：字段里存资源 ID 时的稳定类型，`unwrap()` 的返回类型按版本分叉（26.x → `Identifier`，1.20/1.21 → `ResourceLocation`），并提供 `asString()` / `equals` / `hashCode`。配合 `ImpIds.idOf(...)` 使用
- **静态构造的边界规则** - `gradle/imp-boundary.txt` 增加 `Identifier.(parse|fromNamespaceAndPath|tryParse)` 与对应 26.x 规则：只拦 `import` 会漏掉静态调用点

### Changed

- **core 的 ID 出口收口到 imp** - `WildWindCore.id(String)` 返回类型由 `Identifier` 改为 `ImpId`，内部转调 `ImpIds.idOf(...)`；6 处调用点改为 `id(...).unwrap()` 或 `ImpId` 字段：
  - `client/entity/model/SpiderlingModel`、`client/entity/renderer/layer/SpiderlingEyesLayer`（`EYES` 改为 `ImpId` 字段）
  - `worldgen/{ModBiomeModifiers,ModConfiguredFeatures,ModPlacedFeatures}`
  - `client/screen/FletchingTableScreen` 的两个贴图常量改为 `ImpId`，`blit(...)` 处取 `unwrap()`
  - `WildWindCore` 与 `FletchingTableScreen` 不再 import `net.minecraft.resources.Identifier`
- **1.20 族锚点由 1.20.4 改为 1.20.6（NF 20.6.144，Java 21）** - 数据组件与 `ValueInput/ValueOutput` 自 1.20.5 才引入，而矢术系统整个建立在数据组件上；实测 `DataComponents` 在 1.20.6 可用、1.20.4 不可用。锚定 1.20.4 会迫使为旧版重写一套 NBT 实现

### Fixed

- **`javaReleaseFor` 用错版本** - 原先按族锚点推导 Java 级别，导致 `-PtargetMcVersion=1.20.6` 被错判为 Java 17；改为按实际目标版本推导
- **预处理器解析失败** - JavaParser 默认语言级别不认识 `instanceof` 模式匹配等现代语法；改为 `ParserConfiguration.LanguageLevel.BLEEDING_EDGE`

---

## [Unreleased] - 2026.11.13

### Added

- **源码预处理器（ADR-0004，取代 ADR-0003 的纯 APT 机制）** - 新增 `SourcePreprocessor`（`imp-processor` 模块，基于 JavaParser），在 javac 之前改写源码，实现"所有版本写在同一个类里"：
  - 版本裁剪：按 `@Since` / `@Until`（含 `until` 区间、`exclude` 空洞）删除该版本不存在的成员
  - 符号改名：`@Name` 同时改写声明与调用点；同一旧名映射到不同新名时**直接报错**而非猜测
  - 外部改名表 `gradle/imp-renames.txt`（`族 => 旧名 => 新名 => 说明`）：用于 Minecraft 自身跨版本改名，按族过滤、按简单名匹配
  - Gradle 接入：`preprocessImpSources`（JavaExec）→ `build/preprocessed/java`，`compileJava` 依赖它
- **`@Name` 注解** - `@Target({TYPE, METHOD, FIELD, CONSTRUCTOR})`，SOURCE 保留策略；让源码可用顺手的名字书写，打包时改成目标版本真实名字
- **单类多版本样板** - `ImpIds` 与 `imp/sample/SampleScreen` 改为三段并排实现：
  `@Since("26.1") @Name("id")` → `Identifier.parse`；`@Since("1.21") @Until("26.1") @Name("id")` → `ResourceLocation.fromNamespaceAndPath`；`@Until("1.21") @Name("id")` → `new ResourceLocation(...)`。三个产物里的方法都叫 `id`
- **预处理器回归测试** - `SourcePreprocessorTest` 8 项：三分支各自只留一段、区间/空洞裁剪、声明与调用点改名、`@Name` 歧义报错、外部改名表生效与族过滤、非法目标版本报错
- **imp API 允许返回 MC 原生类型** - `ImpIds.id(...)` / `ImpIds.of(...)` 改为直接返回 `Identifier`（26.x）或 `ResourceLocation`（1.20/1.21），三族各自只有一份确定签名；新增 `idString` / `ofString` 返回 `String` 供存字段与字符串比较。调用方用 `var` 接原生返回版本，禁止在内容模块显式书写这些类型（否则版本分叉泄漏到上层）
- **返回类型分叉的回归测试** - `allowsVaryingReturnTypesAcrossVersionBranches`：同名声明的返回类型按版本不同，每次只保留一个分支即可编译——这是"imp 可返回 MC 原生类型"的实现依据

### Changed

- **退役按族分目录的实现** - 删除上一轮试点的 `imp/src/family/{1.20,1.21,26.x}`、`FamilyBridge` 与 `FamilyBridgeContract`；跨族差异全部收敛进单类的注解组合。版本族概念仅保留用于依赖与工具链选择
- **APT 降级为辅助** - `ImpManifest` 与 `@ParamSince` 重载分派仅服务"参数差异"；成员级差异改由删除处理，不再用 `if (ImpManifest.HAS_X)` 分流

### Fixed

- **`presentIn` 变量遮蔽** - `since` 同时作为注解节点与版本字符串变量导致编译失败，重命名为 `sinceVersion`

---

## [Unreleased] - 2026.11.12

### Added

- **imp 中间层基础设施（ADR-0003 阶段一/二）** - 新增 `imp-annotations` 与 `imp-processor` 两个纯 Java 子项目，不参与 moddev/NeoForge 配置（根 build.gradle.kts 的 `impInfraProjects` 提前返回）：
  - `imp-annotations`：`@Since` / `@Until`（field/method/构造器/类型）与 `@ParamSince`（参数）注解，保留策略 SOURCE，零产物开销；`ImpVersionSpec` 提供 seg 数值比较语义（`26.1 < 26.2 < 26.2.1`），非数字版本号直接抛错
  - `imp-processor`：标准 APT 处理器，按 `-AimpTargetMcVersion`（回退 `targetMcVersion` 属性）生成 `org.polaris2023.wildwind.imp.runtime.ImpManifest` 常量类（`HAS_*` / `UNTIL_*` 布尔，编译期内联）；处理器注册用 auto-service 生成 `META-INF/services`；代码生成用纯文本拼装，处理器 jar 自包含（无 javapoet 传递依赖）
  - `ParamSinceValidator`：带 `@ParamSince` 参数的方法必须在同类型中存在「同名、同返回类型、其余参数按序一致」的回退重载，否则编译报错（重载分派的前提）
  - `settings.gradle.kts`：imp 基础设施项目不再重定向 projectDir 到 `modules/`，使用根目录标准布局
- **端到端验证** - 冒烟测试（临时目录 javac）：target=26.2 与 26.1 产出正确翻转的 `ImpManifest`；缺失回退重载时编译失败并输出指引信息
- **注解区间与空洞表达** - `@Since` 增加 `until()` 与 `exclude()`，`@Until` 增加 `since()` 与 `exclude()`：支持「区间存在」（`[start, end)`）与「区间内特定版本缺失」（如 `@Since(value="26.1", exclude={"26.2"})` 表示 26.1 有、26.2 中途移除、26.3 恢复）；`exclude` 为精确版本匹配（`ImpVersionSpec.matchesAny`）；起始版本不早于结束版本时编译报错
- **imp 中间层本体（ADR-0003 阶段三）** - 新增 `imp` 子项目（根目录标准布局，非 mod）：
  - 自行应用 moddev 插件并配置 `neoForge { version = neoVersion }`，因此**可以真正 import `net.minecraft.*` / `net.neoforged.*`**（ADR 前提：版本适配实现放在 imp 内）
  - 依赖 `compileOnly project(":imp-annotations")` + `annotationProcessor project(":imp-processor")`，JavaCompile 注入 `-AimpTargetMcVersion`（含回退 key `-AtargetMcVersion`）
  - `imp/sample/SampleScreen`：模板示例，同时作为构建期自检——三种差异（成员存在 `@Since`、成员移除 `@Until`、参数差异 `@ParamSince` + 回退重载）每次构建都会真实编译一遍
  - 三个 mod 模块改为 `api(project(":imp"))`；jar 阶段把 imp 字节码合并进 mod 产物（`tasks.named<Jar>("jar") { from(imp.main.output) }`），否则发布后运行期 `NoClassDefFoundError`
- **产物名带目标版本** - mod jar 改为 `ww_core-mc26.2-1.0.0.jar` 形式（imp 为 `ww-imp-mc26.2-1.0.0.jar`），CI 矩阵各版本产物可直接区分

### Fixed

- **imp 子项目 projectDir 错位** - settings.gradle.kts 原循环把所有 include 的子项目都重定向到 `modules/<名>`，导致 `imp-annotations` 源码不被识别（`compileJava NO-SOURCE`）；已拆分 `modProjects` / `impInfraProjects` 两组，并清理配置缓存后恢复
- **处理器 source 版本写死** - `@SupportedSourceVersion(RELEASE_21)` 在 Java 25 编译时报「不支持 source 版本 21」；改为覆盖 `getSupportedSourceVersion()` 返回 `SourceVersion.latestSupported()`
- **`@SupportedOptions` 前向引用编译失败** - 注解参数引用同类常量时报「找不到符号」，改为直接写字面量 key
- **`@ParamSince` 宿主方法缺少 manifest 常量** - 仅参数带注解的方法此前不生成 `HAS_*` 常量，回退重载无从分派；现已为宿主方法补生成
- **imp 多版本族分区（ADR-0003 阶段 B）** - `imp` 内部按版本族分区，解决"跨大版本 API 改名/改用法"这类注解无法表达的问题：
  - 目录：`imp/src/main`（版本无关 API）+ `imp/src/family/<族>/java`（各族适配实现）；Gradle 依 `targetMcVersion` 只用其一
  - 三族实测通过：`1.20`（MC 1.20.4 / NeoForge 20.4.253 / Java 17）、`1.21`（MC 1.21.1 / NeoForge 21.1.209 / Java 21）、`26.x`（MC 26.2 / NeoForge 26.2.0.75 / Java 25）
  - Java 级别按 MC 版本推导（`<1.20.5`→17，`1.20.5~26`→21，`>=26`→25），工具链统一 JDK 25 + `options.release` 降级；1.20 族产物实测 major version 61
  - 真实跨族差异已跑通：1.21 的 `ResourceLocation` 构造器变为私有（须用 `fromNamespaceAndPath`），26.x 改名为 `Identifier` —— 各族 `FamilyBridge` 分别实现，main 源码零改动
  - `imp` jar 的 MANIFEST 记录 `Imp-Target-MC-Version` / `Imp-Version-Family` / `Imp-Neo-Version`，便于排查产物来源
- **imp 边界守卫（ADR-0003 阶段 A）** - 新增 `checkImpBoundary` 任务与规则文件 `gradle/imp-boundary.txt`（格式 `族列表 => 正则 => 说明`），挂在各 mod 模块 `check` 之前：
  - 校验内容模块未使用只存在于某族的版本分叉 API（首条规则即实测出 1.21 族下 17 处 `Identifier` 违规）
  - 禁止内容模块自行使用 `@Since`/`@Until`/`@ParamSince`（版本注解只能是 imp 的收敛点）
  - 分隔符用 `=>` 而非 `|`，因为正则本身常用 `|`
- **处理器回归测试（ADR-0003 阶段 A）** - `imp-processor` 新增 11 个 JUnit 测试，用 JDK 自带 `javac` 驱动真实编译：
  - `ImpProcessorCompileTest`：`@Since`/`@Until` 翻转、`[start,end)` 区间（含右开边界 26.3）、`exclude` 命中与未命中、`@ParamSince` 宿主常量；三类失败场景（区间倒置、缺回退重载、缺目标版本）
  - `ImpVersionSpecTest`：跨大版本排序、缺失段视为 0（`26.2` == `26.2.0`）、非数字版本报错
- **增量构建验证** - 切换 `-PtargetMcVersion` 时 Gradle 正确重编译 `imp` 并更新嵌入的字节码，无跨族串味

---

## [Unreleased] - 2026.11.10
### Added
- **`ww_core` 凋零玫瑰丛** - 新增 `wither_rose_bush`；补全掉落表与花标签
- **`ww_wilderness` 沼泽方块** - 新增 `reeds`、`cattails`、`bog_mud`、`remains`
- **`ww_barren` 砂岩衍生物 / 灰烬与植被草稿** - 新增 `soul_sandstone_stairs` / `_slab` / `_wall`、`smooth_soul_sandstone_stairs` / `_slab`、`cut_soul_sandstone_slab`、`quartz_sandstone_stairs` / `_slab` / `_wall`、`smooth_quartz_sandstone_stairs` / `_slab`、`cut_quartz_sandstone_slab`、`niter_block`、`frogbit`、`ash_block`；楼梯 / 台阶 / 墙沿用各自砂岩的侧面 + 顶底贴图

### Changed
- **无贴图方块模型引用自身贴图路径** - 新增 `MissingModels` 模型工具：六面 / 楼梯 / 台阶 / 墙 / 十字植物 / 双层植物统一引用 `ww_xxx:block/<id>`，贴图缺失时客户端自动回退黑紫 missingno，补齐 png 即生效；新增方块同步补全中英语言、创造栏排序与物品标签
- **可疑的灵魂沙贴图重绘** - 以原版 `suspicious_sand_0..3` 为基板、按灵魂沙的明暗色阶重上色，替换原 4 帧 `suspicious_soul_sand_*.png`；生成脚本 `textures_generator/gen_suspicious_soul_sand.ps1`
