# 原野之风 · Wild Wind

Minecraft **26.2 / NeoForge 26.2.0.93 / Java 25** 模组项目。当前已按功能拆成三个可独立打包的模组，模组版本为 `1.0.1`。

[项目进度](PROGRESS.md) · [开发指南](docs/DEVELOPMENT.md) · [上游同步与升级](docs/UPSTREAM_SYNC.md) · [贡献指南](CONTRIBUTING.md) · [来源说明](docs/PROVENANCE.md) · [历史变更](CHANGE_LOG.MD)

## 模块与安装

| 编号 | 模组 | 模组 ID | 当前功能 |
| --- | --- | --- | --- |
| 0 | 原野之风：核心 / Wild Wind:Core | `wild_wind_core` | 杜鹃木及告示牌、悬挂告示牌、展示架；羊毛拆线；挣脱蜘蛛网；骷髅近战；耕地保护；窑炉；熔炼魔咒；结构生成；七种普通材料；书与笔属性记录器 |
| 1 | 原野之风：荒野更新 / Wild Wind:Wilderness Update | `wild_wind_wilderness` | 灵焰木、焚烬木；泥猪、泥沼蟹、恐鱼及相关道具；窒息、沉溺及呼吸规则；焦灰草方块、焦土、焦灰草丛、焦灰枝条；已有延展药水与延展之箭 |
| 2 | 原野之风：瘠土更新 / Wild Wind:Barren Update | `wild_wind_barren` | 仙人球 |

- 核心可单独安装。荒野、瘠土分别依赖同版本核心，可以任选一个，也可以一起安装。
- 安装荒野时，还需要 **GeckoLib 5.5.4 的 Minecraft 26.2 NeoForge 版本**；仅安装核心或核心＋瘠土不需要 GeckoLib。
- 客户端和服务端安装相同组合。所有已安装模块的物品汇入核心的「原野之风」创造分页，保留全局搜索。荒野的万用蟹钳与核心熔炼附魔可以联动。
- 结构生成和七种普通材料暂归核心；结构配置路径、指令和 `ww_structures` 结构资源命名空间继续保留。

**自定义部件制箭系统暂时移除**：制箭台、方块实体、菜单、箭头／箭杆／箭羽数据组件、原版制箭台替换事件及配置均已移除。原版制箭台保持原版行为。已经完成的延展药水与延展之箭仍在荒野更新中。

### 更新旧版本

移除旧 `ww_hfas` 和独立 `ww_structures` JAR，再按需要安装新模块。加载器会阻止新旧模组同时加载。

**此次更换了方块、物品、实体等注册 ID 的命名空间，没有实现旧 `ww_hfas:*` 存档自动迁移。请先备份旧存档，优先在新测试世界验证；旧内容、NBT 模板和数据包中的旧 ID 需要按模块改写。** 恐鱼的路径仍为 `piranha`，完整新 ID 为 `wild_wind_wilderness:piranha`。删除的自定义制箭台也不会自动恢复为原版方块。

## 获取与测试

开启作弊或具备管理员权限后，在创造模式「原野之风」分页获取物品，也可以使用命令：

```mcfunction
/give @s wild_wind_core:azalea_planks
/give @s wild_wind_core:azalea_sign
/give @s wild_wind_core:azalea_hanging_sign
/give @s wild_wind_core:azalea_shelf
/give @s wild_wind_core:kiln
/give @s wild_wind_core:duckweed 64
/enchant @s wild_wind_core:melting 1
/give @s wild_wind_wilderness:blaze_planks
/give @s wild_wind_wilderness:soul_planks
/give @s wild_wind_wilderness:muddy_pig_spawn_egg
/summon wild_wind_wilderness:muddy_pig ~ ~ ~
/summon wild_wind_wilderness:mudcrab ~ ~ ~
/summon wild_wind_wilderness:piranha ~ ~ ~
/effect give @s wild_wind_wilderness:sink 60
/effect give @s wild_wind_wilderness:choke 60
/give @s wild_wind_wilderness:tipped_arrow 64
/give @s wild_wind_barren:tiny_cactus
```

带有对应模块命名空间的命令需要先安装该模块。熔炼命令需手持可附魔工具。

七种普通材料均在核心，暂不具有食用行为：`duckweed` 浮萍、`calamari` 鱿鱼须、`cooked_calamari` 熟鱿鱼须、`cooked_egg` 煎蛋、`baked_seeds` 烤种子、`cooked_frog_legs` 熟蛙腿、`frog_legs` 生蛙腿。

### 书与笔属性记录器（核心）

手持书与笔，**Shift＋右键方块／实体**，直接转换为署名属性书；书名为“目标名称(注册ID)”，作者为玩家，松开 Shift 后右键阅读。记录硬度、抗性、可燃性、活塞行为、挖掘工具、注册 ID、燃料时长、可再生性及硫方怪原型／物理参数等。已有笔记保留，满页不转换；署名后不可追加，下次记录需另一本书与笔。不适用或未知的属性显示“×”，无需管理员权限。

使用 `/give @s minecraft:writable_book` 获取测试用书。硫方怪分类和数值读取当前游戏数据，可再生性通过数据包标签扩展。详见 [记录器说明](docs/BOOK_INSPECTION.md)。

### 主要玩法

- 任意羊毛＋剪刀无序合成 2 个线；返还剪刀，扣 1 点耐久并受耐久附魔影响，支持自动合成器。
- 非蜘蛛／洞穴蜘蛛实体连续移动 30 刻挣脱接触的蜘蛛网，下坠进入需要 160 刻；停止移动或离开重置。旁观玩家与飞行中的创造玩家不触发。
- 骷髅、流浪者、沼骸、焦骸生成时有 10% 概率换石剑；后三者近战分别造成 30 秒缓慢 I、4 秒中毒 I、30 秒虚弱 I。
- 穿戴摔落缓冲盔甲或具有缓降效果时，落到耕地不会踩坏耕地。
- 窑炉使用原版熔炉外观，每 100 刻烧炼一件符合条件的材料，支持漏斗、比较器、经验、命名及锁定，新生成石匠屋增加窑炉。
- 熔炼 I 可通过附魔台随机获得，适用于挖掘工具，也可附到普通书上。按实际掉落先结算时运；精准采集优先，不进行熔炼。每件消耗 1 点经验，整次经验不足则不触发。
- 泥猪在沼泽与红树林沼泽自然生成，权重 10、每群 2–4 只；使用原版猪的动画、声音与基础行为，支持繁殖。
- 焚烬木、灵焰木树叶的方块和物品按贴图原色显示，不叠加群系或固定染色；落叶粒子分别固定为橙色、青色。保留正常光照与阴影。
- 沉溺限制名单内生物在流体中上浮；窒息加快名单内生物水下缺氧，蝌蚪／美西螈仅离水时生效。完整名单见玩法文档。

## 配置结构生成

启动世界后，在实例目录创建 `config/ww_structures/`：

```text
config/ww_structures/
├── definitions/                 # 每个结构一个 JSON
├── templates/                   # 压缩 .nbt 结构模板，支持子目录
│   └── example_ruins.nbt
└── example_ruins.json.disabled
```

把示例复制为 `definitions/example_ruins.json`，设置 `enabled: true`，重启世界／服务器。配置可指定模板文件、群系、间距、概率、高度、旋转和地形条件；只在符合条件的新区块生成。

```mcfunction
/wwstructure list
/wwstructure locate example_ruins
/wwstructure search example_ruins 16
```

`locate` 查当前维度已记录生成结构的最近坐标；`search` 搜索可能尚未探索的生成位置。详细配置见 [结构生成指南](docs/STRUCTURES.md)。

## 从源码构建

资源快照已随源码保存，普通构建不必先运行数据生成。在仓库根目录执行：

```powershell
.\gradlew.bat build collectMods
```

Linux / macOS 使用 `./gradlew`。三个发行 JAR 收集在 `build/distributions/mods/`：

```text
wild_wind_core-mc26.2-1.0.1.jar
wild_wind_wilderness-mc26.2-1.0.1.jar
wild_wind_barren-mc26.2-1.0.1.jar
```

`datagen`、`verification`、`smoke` 是开发辅助项目，不安装到玩家实例，也不包含在上述发行目录。代码、资源分别位于 `src/core/`、`src/wilderness/`、`src/barren/`。

修改生成器后运行两类数据生成，再将结果同步回模块资源目录：

```powershell
.\gradlew.bat :datagen:runClientData :datagen:runServerData
python scripts/sync_generated_resources.py
.\gradlew.bat build collectMods :verification:runGameTest
```

完整命令、四种安装组合测试与工程约定见 [开发指南](docs/DEVELOPMENT.md)。

## 功能文档

[结构生成](docs/STRUCTURES.md) · [泥猪](docs/MUDDY_PIG.md) · [玩法／效果／附魔](docs/GAMEPLAY.md) · [窑炉](docs/KILN.md) · [杜鹃木功能方块与延展之箭](docs/AZALEA_AND_REACH_ARROWS.md)

## 来源与许可

本项目整理自 `Polari-Stars-MC/WildWindModule`，保留上游来源和贡献者信息，详见 [来源说明](docs/PROVENANCE.md)。仓库分项许可为 [GPL-3.0-only 代码许可](LICENSE_CODE) 与 [CC-BY-SA-4.0 资源许可](LICENSE_ASSETS)，总说明见 [LICENSE](LICENSE)。发行元数据使用相同的 `GPL-3.0-only AND CC-BY-SA-4.0` 许可标识。
