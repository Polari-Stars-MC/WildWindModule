# WildWindMod · 原野之风

面向 Minecraft NeoForge 的模块化模组项目，围绕木材、植物、生物、世界生成和制箭玩法扩展原版内容。

本仓库由 [Polari-Stars-MC/WildWindModule](https://github.com/Polari-Stars-MC/WildWindModule) 的当前开发版本整理而来，包含本地已实现的新功能。上游来源、贡献者与整理范围见 [来源说明](docs/PROVENANCE.md)。

> 当前说明对应 Minecraft `26.2` 适配代码，已合并远端 `26.2` 分支的 `26ad2da`。**结构、新物品、泥猪、泥沼蟹、恐鱼及新增玩法均在同一个 `ww_hfas` 模组中；另需安装 GeckoLib 依赖库。**

[项目进度](PROGRESS.md) · [开发指南](docs/DEVELOPMENT.md) · [历史变更](CHANGE_LOG.MD) · [问题反馈](https://github.com/cuccuc377-dev/WildWindMod/issues) · [许可证](LICENSE)

## 版本与环境

| 项目 | 当前配置 |
| --- | --- |
| Minecraft | `26.2`（依赖范围为精确版本 `[26.2]`） |
| 模组加载器 | NeoForge `26.2.0.93` |
| Java 开发工具链 | JDK `25` |
| Gradle Wrapper | `9.4.1` |
| ModDevGradle | `2.0.148` |
| GeckoLib（必需，客户端与服务端） | `5.5.4`，Minecraft 26.2 NeoForge 版本 |
| 模组版本 | `1.0.1`（构建配置值） |
| 统一模组 ID / 主资源命名空间 | `ww_hfas` |
| 结构资源兼容命名空间（不是独立模组） | `ww_structures` |

版本以 [gradle.properties](gradle.properties) 和 [构建脚本](build.gradle.kts) 为准。`1.0.1` 是源码中的版本号，不代表已经发布稳定版。

## 模块规划

| 编号 | 中文名称 | 英文名称 |
| --- | --- | --- |
| 0 | 原野之风：核心 | Wild Wind:Core |
| 1 | 原野之风：荒野更新 | Wild Wind:Wilderness Update |
| 2 | 原野之风：瘠土更新 | Wild Wind:Barren Update |

以上为当前模块编号与命名规划，各模块的功能归属和代码拆分待进一步落实。当前实际启用的 Gradle 子项目仍为 `hfas`，以 `ww_hfas` 统一打包；`src/structures/` 仍是其中的内部结构生成功能，不单独安装。模块启用列表见 [settings.gradle.kts](settings.gradle.kts)。

## HFAS 内容

### 木材与植物

源码已注册三组木质方块：

| 木材 | 注册内容 | 树木相关实现 |
| --- | --- | --- |
| 焚烬木 `blaze` | 15 种方块 | 独立树叶、树苗；配置在下界废土生成 |
| 灵焰木 `soul` | 15 种方块 | 独立树叶、树苗；配置在灵魂沙峡谷生成 |
| 杜鹃木 `azalea` | 13 种方块 | 复用原版杜鹃植物与树叶；配置替换繁茂洞穴的原版杜鹃树生成 |

三组木材均包含原木、木头、去皮原木、去皮木头、木板、楼梯、台阶、栅栏、栅栏门、门、活板门、压力板和按钮。焚烬木、灵焰木另有树叶和树苗。

此外已注册焦灰草方块、焦土、焦灰草丛、焦灰枝条、仙人球，并实现木材去皮映射和部分木质物品的燃料处理。方块物品统一放入创造模式「原野之风」分页。杜鹃施加骨粉的行为通过 Mixin 接入自定义树木生成。

### 泥猪

泥猪 `ww_hfas:muddy_pig` 使用项目提供的成年／幼年 Blockbench 模型与贴图，沿用原版猪的行走、转头动画、成年／幼年音效、食物与基础行为。两只泥猪可繁殖出泥猪幼崽，不与普通猪混种；掉落复用原版猪的掉落表。

- **自然生成**：沼泽 `minecraft:swamp`、红树林沼泽 `minecraft:mangrove_swamp`，生成权重 `10`，每群 `2–4` 只。
- **生成条件**：允许原版动物生成地面、泥巴和沾泥的红树根；使用原版动物亮度条件（大于 `8`）与陆地空间检查，不在水中或实体碰撞空间内生成。仍受原版被动生物数量上限等自然生成规则约束。
- **刷怪蛋**：`ww_hfas:muddy_pig_spawn_egg`，位于创造模式「原野之风」分页；图标复用原版猪刷怪蛋。对成年泥猪使用可生成泥猪幼崽。

```mcfunction
/summon ww_hfas:muddy_pig ~ ~ ~
/summon ww_hfas:muddy_pig ~ ~ ~ {Age:-24000}
/give @s ww_hfas:muddy_pig_spawn_egg
```

资源与数据包调整方法、构建和测试说明见 [泥猪开发说明](docs/MUDDY_PIG.md)。

### 普通物品

以下七个物品均为普通材料，最大堆叠数量为 64，位于创造模式「原野之风」分页，使用提供的 16×16 图标。当前不附加食用、方块放置、合成或掉落行为。

| 中文名称 | 物品 ID |
| --- | --- |
| 浮萍 | `ww_hfas:duckweed` |
| 鱿鱼须 | `ww_hfas:calamari` |
| 熟鱿鱼须 | `ww_hfas:cooked_calamari` |
| 煎蛋 | `ww_hfas:cooked_egg` |
| 烤种子 | `ww_hfas:baked_seeds` |
| 熟蛙腿 | `ww_hfas:cooked_frog_legs` |
| 生蛙腿 | `ww_hfas:frog_legs` |

例如 `/give @s ww_hfas:duckweed 64`。物品注册位于 `registry/ModItems.java`，图标保存在 `assets/ww_hfas/textures/item/`；模型与中英文翻译同时接入数据生成器。

### 杜鹃木告示牌、展示架与延展之箭

杜鹃木新增普通／悬挂式告示牌与展示架，提供原版双面文字、涂蜡、红石联动、快捷栏交换和漏斗交互。延展之箭注册为独立物品与实体，支持合成、制箭师随机交易及村庄英雄礼物。

```mcfunction
/give @s ww_hfas:azalea_sign
/give @s ww_hfas:azalea_hanging_sign
/give @s ww_hfas:azalea_shelf
/give @s ww_hfas:tipped_arrow 64
/give @s ww_hfas:tipped_arrow[potion_contents={potion:"ww_hfas:long_reach"}] 64
```

全部放入「原野之风」分页。配方、两种箭的时长、红石规则与测试说明见 [杜鹃木功能方块与延展之箭](docs/AZALEA_AND_REACH_ARROWS.md)。

### 窑炉

新增功能方块 **窑炉 `ww_hfas:kiln`**，暂时复用原版熔炉的模型、贴图、火焰粒子、声音和界面贴图，位于创造模式「原野之风」分页。

- 红砖按 `空／砖／空`、`砖／熔炉／砖`、`泥坯／泥坯／泥坯` 合成一个窑炉。
- 每 **100 游戏刻（5 秒）** 烧炼一件；兼容符合条件的熔炉配方，排除矿物及金属装备。砂子、黏土球、圆石、仙人掌、木头、湿海绵等可用。
- 支持上下／侧面漏斗、燃料容器返还、经验存储与领取、比较器、铁砧命名及物品谓词锁。燃烧时亮度 13，需要木镐或更好的镐采集，不能被活塞移动。
- 新生成村庄的六种原版石匠屋模板会在切石机旁增加窑炉，原有石匠工作站保留；已生成的房屋不自动修改。

```mcfunction
/give @s ww_hfas:kiln
```

完整规则、燃料用量和测试命令见 [窑炉说明](docs/KILN.md)。

### 从远端合并的内容

`1.0.1` 在保留本地结构与玩法功能的基础上，合并了远端 `26.2` 分支（提交 `26ad2da`，2026 年 10 月 17 日）。包含泥沼蟹 `ww_hfas:mudcrab`、恐鱼 `ww_hfas:piranha`、对应刷怪蛋／生物桶、万用蟹钳、延展效果与药水，以及木材资源和配方更新。生物动画使用 GeckoLib。

泥沼蟹有温带、暖地、寒地三种数据驱动变种，恐鱼沿用远端丛林与沼泽生成设置。两种生物均已接入水下免缺氧名单。原食人鱼现统一显示为「恐鱼（Direfish）」，生鱼、熟鱼、刷怪蛋、鱼桶和音效字幕同步更名；为兼容已有存档，实体与相关物品继续使用原有的 `piranha` 注册 ID。召唤恐鱼使用 `/summon ww_hfas:piranha`。

远端附带的船、焚烬木／灵焰木告示牌和盆栽树苗仍未注册（杜鹃木告示牌已实现），其资源保留在源码中，对应无效配方、掉落表和进度不会打包。合并细节与验证范围见 [合并记录](docs/UPSTREAM_MERGE.md)。

### 合成、蜘蛛网与状态效果

- **羊毛拆线**：任意颜色的一个羊毛与剪刀无序合成两个线。返还原剪刀，每次使用消耗一点耐久，受耐久附魔影响；最后一点耐久耗尽时剪刀损坏。支持背包、工作台与自动合成器。
- **挣脱蜘蛛网**：实体在接触的蜘蛛网中持续移动 `30` 游戏刻后破坏该蜘蛛网；下坠接触时需 `160` 游戏刻。停止移动或离开会重置计时。蜘蛛、洞穴蜘蛛、旁观玩家和正在飞行的创造玩家不会触发。
- **沉溺 `ww_hfas:sink`**：仅对设定名单中的 41 种原版实体及泥猪生效，阻止它们在流体中向上移动，保留水平与向下移动。
- **窒息 `ww_hfas:choke`**：对名单中的 43 种原版实体及泥猪加快水下氧气消耗，I 级为 `2` 倍、II 级为 `3` 倍；保留水下呼吸药水与水下呼吸附魔的保护。蝌蚪、美西螈不加入该标签，单独在离水后加快缺氧；入水后无效。
- **水下免缺氧名单**：按设定为 25 种原版生物提供水下呼吸保护，包括海豚、沼骸和指定亡灵等；即使获得窒息也不会因水下缺氧受伤。完整名单及泥沼蟹／恐鱼的兼容状态见 [生物适用名单](docs/GAMEPLAY.md#生物适用名单)。水下免缺氧与效果适用名单独立；幻翼现在不受沉溺和窒息影响。

### 骷髅近战与耕地保护

- 骷髅、流浪者、沼骸、焦骸（`minecraft:parched`）生成装备时，有 **10%** 概率使用石剑并切换为近战 AI；读档不会重新判定。
- 流浪者近战命中造成 **30 秒缓慢 I**，沼骸造成 **4 秒中毒 I**，焦骸造成 **30 秒虚弱 I**；普通骷髅没有额外近战效果。
- 穿戴具有「摔落缓冲」附魔的盔甲，或具有「缓降」效果的实体，落到耕地上不会踩坏耕地。只保护耕地，伤害继续遵循原版规则；手持附魔盔甲无效。

### 创造模式分页

所有已注册的模组方块物品、材料、食物、生物桶、刷怪蛋和万用蟹钳统一放入「原野之风」分页，同时收录延展药水各形态、延展药箭及熔炼附魔书。原版分类不再重复显示这些内容，全局搜索仍可找到它们。

### 熔炼附魔

**熔炼 `ww_hfas:melting`** 为最高 I 级的工具附魔。玩家用附魔工具挖掘时，按熔炉配方转换掉落物，每熔炼一个输入物品消耗 **1 点经验值（不是 1 级）**。

精准采集优先：工具有精准采集时不熔炼、不扣经验；有时运时先计算掉落数量，再判断熔炼。当前经验不足以处理本次全部可熔炼掉落时，整次保留原掉落且不扣经验。无熔炉配方的物品不受影响。

```mcfunction
/effect give @s ww_hfas:sink 60
/effect give @s ww_hfas:choke 60
/enchant @s ww_hfas:melting 1
```

效果、附魔获取方式、完整规则及测试覆盖见 [新增玩法说明](docs/GAMEPLAY.md)。这些功能与结构、新物品、泥猪一起打包在同一个 `ww_hfas` 模组中。

### 制箭系统（开发中）

目前包含自定义制箭台方块、方块实体、菜单、箭羽／箭杆／箭头枚举和箭矢数据组件。客户端界面与实际合成流程尚未完成，箭矢配方生成方法仍为空；当前不能视作完整可玩的制箭系统。

模型、语言、标签、掉落表和世界生成数据由数据生成器输出；从源码构建前请阅读[开发指南](docs/DEVELOPMENT.md)。纹理与注册 ID 的一致性也仍需整理。

## 安装与配置

### 结构生成模块

安装统一的 `ww_hfas-mc26.2-1.0.1.jar`，并同时安装 `geckolib-neoforge-26.2-5.5.4.jar`。客户端与服务端均需要匹配版本。构建命令为 `./gradlew :hfas:build`（Windows 使用 `.\gradlew.bat`），从源码构建前先完成下方两类数据生成。

从之前的双 JAR 版本更新时，将旧 HFAS JAR 替换为此文件，并移除旧的 `ww_structures` JAR，避免重复注册。已有 `config/ww_structures/`、`ww_structures:<结构名称>` ID 和存档中的坐标记录继续使用，无需改名。

首次创建或打开世界后，会在游戏／服务端实例目录创建：

```text
config/ww_structures/
├── definitions/                 # 每个结构一个 JSON 配置
├── templates/                   # 原版结构方块导出的压缩 .nbt 文件
│   └── example_ruins.nbt         # 随附 7 × 4 × 7 遗迹示例
└── example_ruins.json.disabled   # 配置示例，默认不参与生成
```

把示例配置复制为 `definitions/example_ruins.json`，将 `enabled` 改为 `true`，重启世界／服务器后即可在符合条件的新区块生成。可以配置模板文件名、群系与群系标签、生成间距、概率、随机盐、高度、旋转、地形起伏、流体与基础地形方块条件。

```mcfunction
/wwstructure list
/wwstructure locate example_ruins
/wwstructure search example_ruins 16
```

`locate` 返回当前维度已记录的生成结构中，水平距离最近的一处坐标；记录随存档保存。`search` 使用原版结构搜索，可以查找尚未探索区域中的生成位置。命令需要管理员权限（与原版 `/locate` 一致）。

完整配置、条件含义、结构导出方法、停用规则和测试命令见 **[结构生成模块指南](docs/STRUCTURES.md)**。配置变更需要重启；不会给已生成区块补生成结构。

### HFAS 模块

取得经过验证、与上述 Minecraft / NeoForge 版本匹配的模组 JAR 后，将其放入对应实例的 `mods/` 目录。从源码生成 JAR 的步骤见下方。当前开发状态与待完善项见[开发指南](docs/DEVELOPMENT.md#待完善事项)。

HFAS 注册了一个 COMMON 配置项：

| 配置键 | 默认值 | 作用 |
| --- | --- | --- |
| `worldgen.replaceVanillaFletchingTable` | `true` | 启用服务端原版制箭台替换逻辑 |

替换发生在方块放置事件及区块加载时。**已有存档中加载的区块也会受影响**：当前区块扫描从世界最低建造高度至固定 `Y=200`，将其中的原版制箭台替换为模组制箭台。关闭配置只会停止后续替换，不会自动恢复已经替换的方块。由于制箭流程尚未完成，测试前请结合这一行为选择配置。

## 从源码开始

准备 JDK 25，取得包含本次 26.2 适配的源码后，在仓库根目录执行。首次运行需要联网下载 Gradle、插件与游戏依赖。模组自身版本仍为 `1.0.1`，目标游戏版本为 `26.2`，JAR 文件名包含游戏版本以便区分。

```powershell
# Windows PowerShell
.\gradlew.bat :hfas:runClientData
.\gradlew.bat :hfas:runServerData
.\gradlew.bat build
```

```bash
# Linux / macOS
chmod +x gradlew
./gradlew :hfas:runClientData
./gradlew :hfas:runServerData
./gradlew build
```

数据生成成功后再构建；若生成任务报错，请先处理报错，不要把缺少生成资源的 JAR 当作完整发行包。当前构建脚本未将数据生成任务自动接入 `build`。

HFAS 构建产物位于 `modules/hfas/build/libs/`，按当前配置命名为 `ww_hfas-mc26.2-1.0.1.jar`。开发客户端可通过 `:hfas:runClient` 启动。

## 项目结构

```text
WildWindMod/
├── src/
│   ├── hfas/
│   │   ├── java/                 # HFAS 注册、玩法、配置与数据生成代码
│   │   └── resources/            # 纹理、Blockbench 源文件、Mixin 配置等
│   ├── structures/
│   │   ├── java/                # 配置数据包、结构生成、坐标记录与指令
│   │   ├── resources/           # 随统一 JAR 打包的示例配置、NBT 模板
│   │   ├── test/                # JUnit 配置与持久化测试
│   │   └── gametest/            # 接入 HFAS 的结构游戏测试与配置
│   └── templates/                # 各模块共用的模组元数据模板
├── modules/hfas/                # Gradle 配置时创建的子项目目录
│   └── build/                   # HFAS JAR、生成元数据和开发运行目录
├── build/generated/hfas/        # 数据生成输出（client/、server/）
├── docs/DEVELOPMENT.md           # 开发命令、源码导航与待完善项
├── docs/STRUCTURES.md            # 结构配置与坐标查询指南
├── .github/workflows/build.yml  # GitHub Actions 构建流程
├── build.gradle.kts             # 共用构建配置与模块任务
├── settings.gradle.kts          # 模块启用列表与项目目录映射
├── gradle.properties            # 版本、工具与模组属性
└── CHANGE_LOG.MD                # 历史变更记录
```

`modules/hfas/` 是 Gradle 子项目目录，实际源码位于 `src/hfas/` 和 `src/structures/`，共同编译进一个模组。生成目录不纳入 Git；不要直接修改其中的文件，修改对应的数据生成器后重新生成。

## 参与开发

欢迎通过 Issue 反馈问题、讨论功能，并通过 Pull Request 提交修改。提交前请阅读[开发指南](docs/DEVELOPMENT.md)，注明目标分支、对应 Minecraft / NeoForge 版本、复现步骤及验证结果。历史记录见 [CHANGE_LOG.MD](CHANGE_LOG.MD)，当前实现状态以本分支源码为准。

## 许可证

仓库 [LICENSE](LICENSE) 声明采用分项许可：源码使用 [GPL-3.0-only](LICENSE_CODE)，资源、文档和配置等非代码内容使用 [CC-BY-SA-4.0](LICENSE_ASSETS)。具体条款请阅读对应文件。

当前 `gradle.properties` 中的 `modLicense` 仍为 `All Rights Reserved`，与上述仓库声明存在差异，需维护者统一元数据。
