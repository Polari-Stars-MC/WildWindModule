# 结构生成功能 · 内置于 ww_hfas

为 Minecraft **26.2 / NeoForge 26.2.0.93 / Java 25** 添加可配置的世界结构。本功能与新物品、泥猪共用 `ww_hfas` 模组和同一个 JAR；`ww_structures` 仅作为兼容旧配置与存档的资源命名空间保留。每个结构由一个 JSON 配置与一个原版压缩 NBT 模板组成，无需编写 Java 或手工创建数据包。

## 1. 创建第一个结构

1. 安装统一的 `ww_hfas-mc26.2-1.0.1.jar`（更新旧版时移除原来的独立 `ww_structures` JAR），创建或打开世界，使配置目录初始化，然后关闭世界／服务器。
2. 在实例目录中找到 `config/ww_structures/`。专用服务器使用服务器自己的目录；客户端加入服务器时，以服务器配置为准。
3. 将 `example_ruins.json.disabled` 复制到 `definitions/example_ruins.json`，把 `enabled` 改为 `true`。
4. 保留随附的 `templates/example_ruins.nbt`，或换成自己的结构方块模板。
5. 重启并进入允许的群系，探索新区块。世界必须启用原版的结构生成开关。
6. 用 `/wwstructure search example_ruins` 搜索生成位置；探索该处后用 `/wwstructure locate example_ruins` 查询已生成记录。

目录结构：

```text
config/ww_structures/
├── definitions/
│   ├── example_ruins.json
│   └── forest_house.json
├── templates/
│   ├── example_ruins.nbt
│   └── houses/forest_house.nbt
└── example_ruins.json.disabled
```

只读取 `definitions/` 直属目录下以 `.json` 结尾的文件。模板允许子目录；配置中的路径相对于 `templates/`，使用 `/` 分隔，禁止绝对路径和越界链接。

**配置和模板在世界启动时读取。修改后必须完整关闭并重新打开世界／重启服务器，`/reload` 不更新这些结构配置。** 同一实例中的存档共用配置，坐标索引则按存档、维度分别保存。

## 2. 配置示例

```json
{
  "name": "forest_house",
  "enabled": true,
  "template": "houses/forest_house.nbt",
  "biomes": ["minecraft:plains", "#minecraft:is_forest"],
  "placement": {
    "spacing": 32,
    "separation": 8,
    "salt": 18372954,
    "frequency": 1.0
  },
  "heightmap": "WORLD_SURFACE_WG",
  "y_offset": -1,
  "rotation": "random",
  "include_entities": false,
  "conditions": {
    "min_y": 60,
    "max_y": 160,
    "max_terrain_difference": 6,
    "avoid_fluids": true,
    "ground_blocks": ["minecraft:stone", "#minecraft:dirt", "minecraft:sand"]
  }
}
```

### 基本字段

| 字段 | 默认值 | 含义 |
| --- | --- | --- |
| `name` | 必填 | 唯一名称，匹配 `[a-z0-9_][a-z0-9_-]{0,63}`。生成 ID 为 `ww_structures:<name>`，也是指令使用的名称 |
| `enabled` | `true` | 是否参与自然生成。`false` 仍加载定义和模板，保留已有结构记录 |
| `template` | 必填 | 相对模板文件名，必须是 `.nbt` |
| `biomes` | 必填、非空 | 群系 ID 或以 `#` 开头的群系标签，列表之间为“或”关系 |
| `heightmap` | `WORLD_SURFACE_WG` | 生成高度基准；也支持 `OCEAN_FLOOR_WG`，适合水底结构 |
| `y_offset` | `0` | 在中心采样点第一格空位高度上加的偏移，范围 `-256..256`；地板在模板第 0 层时可设 `-1` 嵌入地面 |
| `rotation` | `random` | `random`、`none`、`clockwise_90`、`clockwise_180`、`counterclockwise_90` |
| `include_entities` | `false` | 是否生成模板保存的实体；方块实体如箱子不受此开关影响 |

群系遵循原版结构起点检查，不要求整个建筑覆盖范围都位于同一群系。没有额外维度过滤：在使用相同群系的自定义维度中也可能生成。下界和末地可填写对应的群系 ID；地表高度模式不提供下界洞穴底面专用搜索。

### 生成管理：`placement`

| 字段 | 默认值 | 含义 |
| --- | --- | --- |
| `spacing` | `32` | 原版 random-spread 网格间距，单位为区块（每区块 16 格），范围 `1..4096` |
| `separation` | `8` | 网格内候选点的间隔约束，单位为区块；必须 `0 <= separation < spacing` |
| `salt` | 根据名称计算 | `0..2147483647` 的随机盐。不同结构建议使用不同值，减少候选点重合 |
| `frequency` | `1.0` | 候选点通过概率，必须大于 `0` 且不超过 `1`；`0.25` 表示约四分之一候选点继续检查条件 |

间距不是从玩家位置量起的固定距离，也不保证每个网格必定生成。群系、高度和地形条件可能排除候选点。同一个世界种子、相同配置下的候选位置具有确定性；改变盐值、间距或条件只影响尚未生成的区块。不同结构之间没有额外的互斥或防重叠规则。

### 生成条件：`conditions`

所有条件按“且”组合。模块在旋转后模板覆盖范围的四角与中心共 **5 个点**采样基础地形，不加载相邻区块。

| 字段 | 默认值 | 含义 |
| --- | --- | --- |
| `min_y` / `max_y` | `-64` / `320` | 各采样点的第一格空位高度范围，含边界；不是模板最终最低／最高 Y。取值范围 `-2032..2031`，最小值不得超过最大值 |
| `max_terrain_difference` | `8` | 5 点最高与最低高度差的上限，`0..512`。设 `0` 要求采样点等高 |
| `avoid_fluids` | `true` | 排除采样点地面方块或其上方含流体的位置。水底结构通常应设 `false` |
| `ground_blocks` | `[]` | 地面基础方块 ID／标签的允许列表，空数组表示不限；各采样点均须匹配其中之一 |

采样发生在原版地表装饰之前。**`ground_blocks` 检查噪声生成器的基础地形，而非最终可见草皮、积雪或树叶；例如主世界常为 `minecraft:stone`。** 只填 `minecraft:grass_block` 可能导致完全不生成。5 点采样不能保证整个底面无坑洞或无水；模块也不会自动铺地基、平整山体或防止与其他结构相交。

最终模板底部到顶部必须处于当前维度的建筑高度范围内；不满足条件的候选点会被跳过。

## 3. 模板制作

使用原版结构方块的保存模式制作结构。保存后的压缩 `.nbt` 文件通常在存档的 `generated/<命名空间>/structure/` 下，将它复制到 `config/ww_structures/templates/`，然后在配置中填写文件名。文件必须包含原版 `size`、`blocks` 和 `palette`／`palettes` 数据。

- 最大尺寸为 **64 × 256 × 64**（X × Y × Z），压缩文件最大 16 MiB，解压读取预算为 64 MiB。
- 支持方块状态和方块实体 NBT。箱子的物品／战利品表按模板保存的数据处理；模块不会自动配置战利品。
- 模板中的空气会替换覆盖位置的方块；需要保留地形的位置请在保存时使用结构空位方块（structure void）。
- 结构方块本身会被过滤，DATA 标记没有额外的自定义处理逻辑。拼图方块沿用原版模板片段处理，不提供递归拼接建筑功能。
- `include_entities=true` 时实体按原版模板生成逻辑放置；请自行控制模板实体数量。
- 不支持直接加载 `.schem`、`.schematic`、`.litematic` 或 `.snbt`。
- 引用其他模组方块时，客户端和服务器需安装对应模组；移除依赖可能使模板内容无法正确加载。

随附 `example_ruins.nbt` 是 7 × 4 × 7 的苔石基座和四根石砖柱，默认不参与自然生成，可用来验证安装。

## 4. 指令与坐标

这些命令使用与原版 `/locate` 相同的管理员权限（GAMEMASTER，通常为 OP 等级 2）。支持名称补全，结构名可省略 `ww_structures:`。

| 指令 | 作用 |
| --- | --- |
| `/wwstructure list` | 列出已加载的配置结构与当前维度有历史记录的结构名称 |
| `/wwstructure locate forest_house` | 查询当前维度已生成记录中，距离执行位置最近的结构 |
| `/wwstructure search forest_house [radius]` | 使用原版搜索算法查找结构起点，默认半径 `16`，允许 `1..64` |
| `/locate structure ww_structures:forest_house` | 也可以使用原版结构定位指令 |

`locate` 按 **XZ 平面距离**比较，返回结构包围盒底面中心的 X、Y、Z、维度和距离。它查询已保存的坐标索引，不会为搜索额外生成区块。起始区块完成生成并加载后才入库；仅调用 `search` 得到候选位置不会让它自动成为“已生成记录”。跨区块结构的其他部分会随着相邻区块生成而补全。

索引随存档按维度保存，服务器重启后仍保留。玩家拆除建筑不会删除历史记录。旧存档的既有结构会在其起始区块重新加载时补记，模块不扫描整个存档的区域文件，因此不会声称能找到从未记录、从未重新加载的所有旧结构。

`search` 可以访问未探索区块的结构起点，返回原版定位位置（其 Y 可能为原版的占位高度），不保证整座建筑已经生成。`radius` 是原版结构候选网格的搜索圈数，**不是方块数或普通区块半径**；大间距和复杂条件可能使搜索明显耗时。先用小半径，未找到不等于整个世界不存在该结构。

## 5. 修改、停用与错误排查

- 停止继续生成：设置 `enabled=false`，保留 JSON 与模板，重启。已有建筑、结构定义和历史索引会保留；原版搜索可能因不再有放置规则而找不到它，已生成记录仍可用 `locate` 查询。
- 彻底移除／改名：先备份存档。删除定义或修改 `name` 会改变动态注册表 ID，原版可能丢弃旧区块的对应结构元数据；不会替你拆除已有方块。
- 修改模板：关闭世界后修改再重启。尚未生成的区块使用新模板；跨区块结构若已生成一部分，应保留旧模板以避免接缝。不要在存档的 `generated/ww_structures/structure/` 下保存同名模板，否则原版存档模板优先级可能覆盖配置模板。
- 已生成区块不补生成。改变配置后请探索新区块或新建测试世界。
- JSON 拼写错误、参数类型错误、重名、缺失／越界模板、尺寸不合法会带文件名报错并阻止加载，避免悄悄使用错误规则。未知的群系／方块 ID 或缺失标签由原版数据包注册表加载校验，检查服务器日志。
- 找不到结构时先核对 `enabled`、世界结构生成开关、群系、Y 范围、地形起伏、流体和基础方块限制。新区块生成仍遵循概率，不能保证刚进世界就遇到。

## 6. 开发与验证

Windows PowerShell：

```powershell
.\gradlew.bat :hfas:test
.\gradlew.bat :hfas:runGameTest
.\gradlew.bat :hfas:build
```

Linux / macOS 将 `.\gradlew.bat` 换成 `./gradlew`。单元测试覆盖配置边界、路径越界、关闭生成时保留定义、资源输出、模板尺寸与坐标索引序列化。统一 GameTest 模组联合加载新物品、泥猪与结构功能，检查配置数据包加载、原版搜索、实际区块生成、模板方块、片段 NBT 保存恢复和指令执行；不会打包进发行 JAR。

GameTest 使用 `modules/hfas/build/runs/hfas/gameTest/` 下的专用临时世界；重复运行会重置该测试世界。测试报告为该目录的 `results.xml`。开发运行首次需要下载游戏资源；Gradle 的资源下载工具还可能自动安装 JDK 21，游戏与编译继续使用 JDK 25。

测试模组在专用 GameTest 世界启用原版默认关闭的结构生成，并为超平坦预设添加测试结构集合。这些改动只存在于测试资源中。普通超平坦世界若指定了 `structure_overrides`，也需将自己的结构集合加入允许列表；常规默认地形世界没有这项超平坦限制。

若中文 Windows 路径下 JUnit 报 `ClassNotFoundException`，但对应 `.class` 已存在，可临时使用 `.\gradlew.bat :hfas:test "-Dorg.gradle.jvmargs=-Xmx1G -Dfile.encoding=GBK"`，使 Gradle 参数文件与本机 Java 启动器编码一致。源码仍以 UTF-8 编译；此参数无需写入仓库配置。

实际代码在 `src/structures/java/`，直接编入 HFAS，由 `StructureModule.register` 初始化：`config` 负责配置和不可变资源快照，`worldgen` 注册结构类型／模板片段及坐标索引，`command` 注册查询指令。配置资源通过强制启用的服务器数据包 `ww_structures/config` 接入原版世界生成。
