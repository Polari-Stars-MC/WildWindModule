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

## [Unreleased] - 2026.11.10

### Added
- **`ww_core` 凋零玫瑰丛** - 新增 `wither_rose_bush`；补全掉落表与花标签
- **`ww_wilderness` 沼泽方块** - 新增 `reeds`、`cattails`、`bog_mud`、`remains`
- **`ww_barren` 砂岩衍生物 / 灰烬与植被草稿** - 新增 `soul_sandstone_stairs` / `_slab` / `_wall`、`smooth_soul_sandstone_stairs` / `_slab`、`cut_soul_sandstone_slab`、`quartz_sandstone_stairs` / `_slab` / `_wall`、`smooth_quartz_sandstone_stairs` / `_slab`、`cut_quartz_sandstone_slab`、`niter_block`、`frogbit`、`ash_block`；楼梯 / 台阶 / 墙沿用各自砂岩的侧面 + 顶底贴图

### Changed
- **无贴图方块模型引用自身贴图路径** - 新增 `MissingModels` 模型工具：六面 / 楼梯 / 台阶 / 墙 / 十字植物 / 双层植物统一引用 `ww_xxx:block/<id>`，贴图缺失时客户端自动回退黑紫 missingno，补齐 png 即生效；新增方块同步补全中英语言、创造栏排序与物品标签
- **可疑的灵魂沙贴图重绘** - 以原版 `suspicious_sand_0..3` 为基板、按灵魂沙的明暗色阶重上色，替换原 4 帧 `suspicious_soul_sand_*.png`；生成脚本 `textures_generator/gen_suspicious_soul_sand.ps1`

---

## [Unreleased] - 2026.11.12

### Added
- **`genData` 聚合任务** - 根项目新增 `genData`，`dependsOn` 三个模块的 `runClientData` + `runServerData`，一条命令跑完全部 datagen；不能把 datagen 挂到 `processResources` 上，因为 data run 依赖 `classes`，会形成循环依赖
- **`debug_all.bat` 一键导出模组属性** - 根目录新增 `debug_all.bat`（可选参数指定输出目录，默认 `build/debug`），内部调用新注册的无界面服务端 run `:barren:runDebugExport`（`--nogui` + `-Dwildwind.debug.export.{dir,exit,namespaces}`），由 `ModDebugExporter` 在 `ServerStartedEvent` 里从注册表 / 标签 / 配方导出 `blocks.txt`、`items.txt`、`entities.txt`、`debug_all.txt` 后自动 `halt`；本机实测导出 105 个方块 / 113 个物品 / 7 个生物。字段口径：合适挖掘工具取 `mineable/pickaxe|axe|shovel|hoe`、`sword_efficient`、`shears_*_breaking_speed` 方块标签；引燃几率 / 烧毁几率取 `getFireSpreadSpeed` / `getFlammability`（即原版 `igniteOdds` / `burnOdds`）；压断红石线按 `RedStoneWireBlock#getConnectingSide` 的口径，用线所在格调用 `isRedstoneConductor` 判定；乐器取 `BlockState#instrument()`；爆炸抗性取 `Block#getExplosionResistance()`；烧炼时间取 `RecipeType.SMELTING` 配方的 `cookingTime`（按输入物品查）；硫方怪原型反查 `minecraft:sulfur_cube_archetype/*` 物品标签命中的原型 id；可再生按「该物品能由某个配方产出」推断（报告头已注明只是参考口径）；中文名 / 英文名直接读各模块 datagen 生成的 `assets/<ns>/lang/{zh_cn,en_us}.json`，因其他模组资源不在本类所在类加载器的可见范围，查找顺序为 本类类加载器 → 系统类加载器 → 上下文类加载器 → 扫描 `java.class.path` 中的目录 / jar
- **「固体方块」用到的私有开关靠反射读取** - 26.2 把 `BlockStateBase#isSolid()`、`blocksMotion()` 连同它们依赖的 `legacySolid` / `forceSolidOff` 字段全部弃用，只剩 `Properties#forceSolidOn` 仍在用，而该字段是私有的、没有公开读取途径；导出器用一次 `getDeclaredField("forceSolidOn")` + `setAccessible` 取到它，取出失败时降级为只按碰撞箱判定并打一条警告，不引入访问转换器、不写 `@SuppressWarnings`

### Fixed
- **GitHub Actions 打出的 jar 缺少 datagen 资源** - `build.gradle.kts` 把 `build/generated/<module>/{client,server}` 作为 `main.resources` 的 srcDir 打包，但 `build/` 被 `.gitignore` 忽略，而 CI 只执行 `./gradlew build`，从不运行 datagen，于是产物只剩 `src/<module>/resources` 里的手写资源；实测与本地完整 jar 对比为严格子集（CI 独有文件 0 个），core 缺 154 个、wilderness 缺 72 个、barren 缺 514 个文件（blockstates / models / items / lang / loot_table / tags / worldgen / data_maps），游戏内表现为方块无模型、物品无图标、方块无掉落、语言键未翻译
- **CI 流程补 datagen 步骤** - `build.yml` 与 `manually build.yml` 改为先 `./gradlew genData` 再 `./gradlew build`；本地验证：删空 `build/generated` 后重跑 `genData` 能完整还原原文件集合（768 个文件一致），再 `build` 得到完整 jar（barren 由 307 KB → 340 KB）
- **手动构建产物是「zip 里套 zip」** - `manually build.yml` 先用 `zip -j` 打成 `ww-<version>.zip` 再交给 `actions/upload-artifact`，而该 action 本身就会把上传内容压成一个 zip，下载下来便是双层压缩包；已删掉手动 zip 步骤，改为直接上传 `dist/*.jar`，下载得到单个 `ww-<version>.zip`，解压即得 core / wilderness / barren 三个 jar

---

## [Unreleased] - 2026.11.13

### Added
- **杜鹃木告示牌 / 悬挂告示牌 / 展示架** - `ww_core` 新增 `azalea_sign` / `azalea_wall_sign` / `azalea_hanging_sign` / `azalea_wall_hanging_sign` / `azalea_shelf`（`StandingSignBlock` / `WallSignBlock` / `CeilingHangingSignBlock` / `WallHangingSignBlock` / `ShelfBlock`），方块实体复用原版 `SIGN` / `HANGING_SIGN` / `SHELF`，在 `WildWindCore` 里通过 `BlockEntityTypeAddBlocksEvent` 登记（`ModBlocks#addBlockEntityBlocks`）；`SignItem` / `HangingSignItem` 用 `useBlockDescriptionPrefix()` 注册（堆叠 16），与焚烬木 / 灵焰木一致
- **杜鹃木船与运输船** - `ModEntities` 新增 `azalea_boat` / `azalea_chest_boat`（原版 `Boat` / `ChestBoat`，尺寸 1.375×0.5625、眼睛高度 0.5625、`clientTrackingRange(10)`；杜鹃木是主世界木材，未加 `fireImmune`），`ModItems` 新增对应 `BoatItem`（`stacksTo(1)`）；`EntitiesClientHandler` 烘焙 `ww_core:boat/azalea` / `ww_core:chest_boat/azalea` 模型层并注册 `BoatRenderer`，贴图沿用已有 `textures/entity/boat/azalea.png`、`textures/entity/chest_boat/azalea.png`
- **杜鹃木系列的配方与解锁进度** - `data/ww_core/recipe` 新增 `azalea_sign`（3） / `azalea_hanging_sign`（6） / `azalea_shelf`（6） / `azalea_boat` / `azalea_chest_boat`；`advancement/recipes` 按原版分组放在 `decorations`（告示牌 / 悬挂告示牌 / 展示架）与新建的 `transportation`（船 / 运输船）
- **杜鹃木告示牌 / 展示架贴图与生成脚本** - `textures_generator/gen_azalea_textures.ps1` 以原版 `oak_sign` / `oak_hanging_sign` / `oak_shelf` 的明暗为准套用杜鹃木底色，生成 `block/azalea_sign.png`、`block/azalea_hanging_sign.png`、`block/azalea_shelf.png`；`gui/signs/azalea.png`（24×26）由 `gen_sign_gui.ps1` 生成，该脚本改为按「木材 → 资源根」表遍历并支持 `-Woods azalea` 只跑指定木材（原有 cinder / ember 输出像素级不变）
- **杜鹃木标签与语言条目** - 方块 / 物品标签补 `signs`、`wall_signs`、`hanging_signs`、`wall_hanging_signs`、`wooden_shelves`、`boats`、`chest_boats` 与 `mineable/axe`；中英语言补墙式告示牌、悬挂告示牌、展示架、船与运输船（含 `entity.*`）；创造栏 `ww_core` 页追加告示牌 / 悬挂告示牌 / 船 / 运输船 / 展示架

### Changed
- **`WoodType` 注册名带命名空间** - 杜鹃木由 `azalea` 改为 `ww_core:azalea`：原版 `SignEditScreen` / `HangingSignEditScreen` 用 `Identifier.parse(woodType.name() + ".png").withPrefix("textures/gui/signs/")` 拼 GUI 贴图路径，不带冒号会默认落到 `minecraft:`，`ww_core` 下的底板永远读不到
- **`WildWindCore` 注册顺序** - `ModEntities.register(bus)` 提到 `ModItems.register(bus)` 之前，因为船物品在构造时需要 `ModEntities.AZALEA_BOAT.get()`（与 `WildWindBarren` 的注释一致）
- **告示牌模型生成抽到核心模块复用** - 新增 `core/datagen/provider/SignModels#registerSet`，`ModModelProvider` 与 `BarrenModelProvider` 共用同一份告示牌 / 悬挂告示牌 / 展示架模型生成逻辑，删掉 barren 里的私有副本
- **创造栏分页顺序：核心 → 荒野 → 瘠土** - NeoForge 的 `CreativeModeTabRegistry#sortTabs` 对模组 tab 做拓扑排序，没有 `tabsBefore/tabsAfter` 声明时按 Identifier 字母序兜底，于是实际顺序是「瘠土 → 核心 → 荒野」；现在 `ww_wilderness:wilderness` 声明 `withTabsBefore(ww_core:wild_wind)`，`ww_barren:barren` 声明 `withTabsBefore(ww_core:wild_wind, ww_wilderness:wilderness)`，排序图变为 `spawn_eggs → core → wilderness → barren`；原版 10 个分类 tab 正好占满第一页，所以三者是第二页的左起前三格。瘠土对荒野的引用只是字符串 id（瘠土编译期不依赖荒野），未安装荒野时该条排序边会被忽略，核心那条仍然生效
- **幼蛛整体迁到荒野更新** - `Spiderling` 与其 GeckoLib 模型 `SpiderlingModel`、渲染器 `SpiderlingRenderer`、发光眼睛图层 `SpiderlingEyesLayer`、渲染状态 `SpiderlingRenderState`，以及 `spiderling` 实体类型、`spiderling_spawn_egg`、三条音效事件 + `sounds.json` 条目、`EntityAttributeCreationEvent` / `RegisterSpawnPlacementsEvent` 注册、实体类型标签（`arthropod` / `dismounts_underwater` / `no_anger_from_wind_charge`）、实体战利品表（线 0~2、蜘蛛眼 0~1 仅玩家击杀）、刷怪蛋物品模型与中英语言，全部从 `ww_core` 移到 `ww_wilderness`；`assets/ww_core` 下的 `geckolib/models|animations/entity/spiderling.*`、`textures/entity/spiderling*.png`、`textures/item/spiderling_spawn_egg.png` 一并改命名空间搬到 `ww_wilderness`。`ww_core` 侧随之删掉已空的 `ModSoundEvents`、`event/EntitiesHandler`、`ModEntityTypeTagsProvider` 与 `sounds.json`，`ModLootProvider` 不再保留实体子 provider（核心只剩船的 `noLootTable`）；通用的 `WindupAttackMob` / `ChargingMeleeAttackGoal` 仍留在核心模块作为可复用基建
- **实体 id 变更（破坏性）** - `ww_core:spiderling` → `ww_wilderness:spiderling`，刷怪蛋 `ww_core:spiderling_spawn_egg` → `ww_wilderness:spiderling_spawn_egg`；旧存档中的幼蛛、命令 / 战利品表 / 数据包里的旧 id 都会失效，需 `/summon ww_wilderness:spiderling` 重新生成
