# 泥猪 · Muddy Pig

泥猪属于荒野更新（`wild_wind_wilderness`），使用 Minecraft **26.2**、NeoForge **26.2.0.93**、Java **25**。需要核心与 GeckoLib；新材料和结构生成功能由核心提供。

## 资源和动画

- 实体 ID：`wild_wind_wilderness:muddy_pig`。
- 刷怪蛋 ID：`wild_wind_wilderness:muddy_pig_spawn_egg`，复用原版猪刷怪蛋图标。
- 提供的 `pig_muddy.bbmodel`、`pig_muddy_baby.bbmodel` 原样保留在 `src/wilderness/resources/assets/wild_wind_wilderness/models/entity/pig/`，不会打入 JAR。
- 成年 64×64、幼年 32×32 PNG 原样保留在 `src/wilderness/resources/assets/wild_wind_wilderness/textures/entity/pig/`。
- `MuddyPigModel` 将两个模型转换成 Java 模型层。Blockbench 坐标转换为 `(-x, 24-y, z)`，成年身体旋转后重新设置中心；腿部使用关节轴，头饰与头共用动画。
- 模型继承原版 `PigModel`，不引入 GeckoLib 或额外动画库；声音和 AI 继承原版 `Pig`。成年和幼年使用各自的模型／贴图，不重复缩放幼年模型。
- 属性为原版猪的 10 点生命和 0.25 移动速度；食物使用 `minecraft:pig_food` 标签。两只泥猪繁殖出泥猪，普通猪不会与泥猪交配。掉落表引用 `minecraft:entities/pig`。

修改 `.bbmodel` 后，需同步调整 `client/MuddyPigModel.java` 中的几何与 UV；游戏不会直接读取 Blockbench 文件。

## 自然生成与数据包

默认在沼泽和红树林沼泽生成，权重 10、每群 2–4 只。权重是群系生物列表内的相对权重，不代表百分比或每区块固定数量。沿用原版动物亮度大于 8 的条件，以及陆地空间、碰撞、距离和生物数量上限检查。

以下源码资源可作为数据包覆盖目标，命名空间都是 `wild_wind_wilderness`：

| 路径（相对 `src/wilderness/resources/`） | 用途 |
| --- | --- |
| `data/wild_wind_wilderness/neoforge/biome_modifier/add_muddy_pig.json` | `neoforge:add_spawns`，调整权重和每群数量 |
| `data/wild_wind_wilderness/tags/worldgen/biome/spawns_muddy_pig.json` | 默认仅包含 `minecraft:swamp`、`minecraft:mangrove_swamp` |
| `data/wild_wind_wilderness/tags/block/muddy_pig_spawnable_on.json` | `#minecraft:animals_spawnable_on`、泥巴、沾泥的红树根 |
| `data/wild_wind_wilderness/loot_table/entities/muddy_pig.json` | 引用原版猪的掉落表 |

自定义生成群系时，数据包可用 `replace: true` 覆盖群系标签。新增地面仍须满足原版陆地生成的承载与空间检查。更改群系修改器后重启世界／服务器；自然生成仍受原版规则限制，进入群系不保证立即看到泥猪。

## 构建和验证

在仓库根目录使用 JDK 25 按顺序执行：

```powershell
.\gradlew.bat :datagen:runClientData
.\gradlew.bat :datagen:runServerData
python scripts/sync_generated_resources.py
.\gradlew.bat build collectMods
.\gradlew.bat :verification:runGameTest
```

发行 JAR 位于 `build/distributions/mods/`。泥猪需要客户端和服务端都安装匹配版本的核心、荒野 JAR，以及 GeckoLib 5.5.4（26.2 NeoForge）。泥猪本身仍使用原版模型动画代码。不要将测试模组或 `.bbmodel` 作为游戏模组安装。

`build` 包含模型单元测试：检查成年／幼年模型边界和九个原始立方体，比较两种年龄的头部／四腿动画与原版猪。统一 GameTest 模组仅用于 `:verification:runGameTest`，检查实体属性、食物、繁殖、生物蛋、掉落表加载、两个目标群系的实际生成列表，以及草地／泥地／沾泥红树根的生成条件和水／障碍拒绝条件。普通客户端和服务端运行不加载测试模组。

手动检查命令：

```mcfunction
/summon wild_wind_wilderness:muddy_pig ~ ~ ~
/summon wild_wind_wilderness:muddy_pig ~ ~ ~ {Age:-24000}
/give @s wild_wind_wilderness:muddy_pig_spawn_egg
/locate biome minecraft:swamp
/locate biome minecraft:mangrove_swamp
```

观察成年和幼年的纹理、头饰、转头、行走与音效，再到满足亮度和生成空间条件的目标群系观察自然生成。
