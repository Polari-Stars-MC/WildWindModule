# 新增玩法说明

适用于 Minecraft 26.2 / NeoForge。羊毛、蜘蛛网、骷髅、耕地、窑炉和熔炼在核心；沉溺、窒息与呼吸规则在荒野，安装组合见 README。

## 窑炉

`wild_wind_core:kiln` 是同一模组内的功能方块，使用原版熔炉外观，每 100 刻烧炼一件非矿物、非金属装备材料。完整获取方式、漏斗、经验、命名、锁定和村庄生成规则见 [窑炉说明](KILN.md)。

## 羊毛拆线

任意颜色的 **1 个羊毛 + 1 把剪刀 → 2 个线**，无序配方；支持背包 2×2、工作台 3×3 和自动合成器。

- 手动合成后剪刀返还原合成槽；自动合成器会输出线和剪刀，需要自行回收剪刀。
- 每次合成尝试消耗剪刀 1 点耐久，调用原版耐久规则，因此耐久附魔有概率免除本次损耗。
- 剪刀的附魔、自定义名称等组件保留；耗尽最后一点耐久时剪刀消失。
- 每次实际合成都独立判定耐久，连续／Shift 合成同样适用。
- 配方 ID 与序列化类型均为 `wild_wind_core:wool_to_string`。这是自定义特殊配方，直接放入材料即可合成。

## 蜘蛛网挣脱

服务器分别记录每个实体对每个正在接触的蜘蛛网的移动时间：

| 情况 | 所需连续移动时间 |
| --- | --- |
| 通常在网中移动 | 30 游戏刻，正常 20 TPS 下为 1.5 秒 |
| 下坠进入蜘蛛网 | 160 游戏刻，正常 20 TPS 下为 8 秒 |
| 停止位移 | 清零，重新移动后重新计时 |
| 离开该蜘蛛网 | 清零，重新接触后重新计时 |

下坠／通常移动模式在该次连续接触开始移动时确定。多个蜘蛛网分别计时，达到时限只破坏实际接触且完成计时的蜘蛛网，采用正常方块破坏掉落规则。计时不写入存档。

蜘蛛、洞穴蜘蛛、旁观玩家、正在飞行的创造玩家不触发；地面上的创造玩家仍可触发。移动按实际位置变化判断，包括外力推动。

## 状态效果

这次注册的是两个负面状态效果及图标，没有新增药水物品或酿造配方。可通过指令、数据包或其他模组施加。

| 效果 | ID | 行为 |
| --- | --- | --- |
| 沉溺 | `wild_wind_wilderness:sink` | 只对下方沉溺名单生效。接触流体时限制向上位移与向上速度，包括水与熔岩；水平、向下移动仍可进行。更高等级不叠加额外限制。 |
| 窒息 | `wild_wind_wilderness:choke` | 对下方窒息名单中的实体，眼睛浸在水中时加速消耗氧气；I 级 2 倍、II 级 3 倍，依此类推。蝌蚪、美西螈单独在陆地处理。 |

窒息名单中的青蛙、海龟、骆驼尸壳及铁傀儡也会消耗水下氧气。水下呼吸、潮涌能量、鹦鹉螺之息的药水效果及气泡柱仍提供呼吸保护，创造模式无敌玩家也不受影响；水下呼吸附魔跳过消耗的刻仍保持零消耗。普通名单内实体离水后正常恢复氧气。

### 生物适用名单

**不会因水下缺氧窒息的原版生物（25 种，重复的溺尸合并）**：鳕鱼、鲑鱼、热带鱼、河豚、鱿鱼、发光鱿鱼、海豚、溺尸、守卫者、远古守卫者、沼骸、史莱姆、岩浆怪、僵尸、僵尸村民、尸壳、骷髅、流浪者、凋灵骷髅、僵尸猪灵、僵尸疣猪兽、幻翼、僵尸马、骷髅马、凋灵。

这些生物在水下恢复氧气，优先于窒息效果；即使原先氧气已经耗尽，也不会继续因缺氧受伤。此规则不取消离水干燥伤害、火焰伤害或其他非缺氧伤害，也不移除其他生物原有的水下呼吸能力。

**沉溺生效的原版实体（41 种）**：玩家、狐狸、骆驼、骆驼尸壳、鸡、哞菇、牛、驴、马、骡、鹦鹉、猪、兔子、绵羊、嗅探兽、蝌蚪、海龟、流浪商人、山羊、村民、美西螈、猫、青蛙、豹猫、雪傀儡、蜜蜂、熊猫、铁傀儡、羊驼、北极熊、行商羊驼、狼、唤魔者、疣猪兽、猪灵蛮兵、掠夺者、劫掠兽、卫道士、监守者、女巫、猪灵。

**窒息在水中生效的原版实体（43 种）**：玩家、狐狸、骆驼、骆驼尸壳、鸡、哞菇、牛、驴、马、骡、鹦鹉、猪、兔子、绵羊、嗅探兽、海龟、流浪商人、山羊、村民、猫、青蛙、豹猫、雪傀儡、蜜蜂、熊猫、铁傀儡、羊驼、北极熊、行商羊驼、狼、唤魔者、疣猪兽、猪灵蛮兵、掠夺者、劫掠兽、卫道士、监守者、女巫、苦力怕、洞穴蜘蛛、蜘蛛、末影人、猪灵。

**蝌蚪、美西螈的窒息特例**：不放入 `choke_affected` 标签。代码单独检查 `isInWater()`，在水中不触发；离水后将原版缺氧／干燥消耗提高至 I 级 2 倍、II 级 3 倍。氧气耗尽时继续使用原版伤害与重置规则。美西螈仍保留雨水补水及 NoAI 时不干燥的原版行为。入水后恢复原版补氧。

**模组生物**：泥猪 `wild_wind_wilderness:muddy_pig` 继续同时受沉溺和水中窒息影响；泥沼蟹与恐鱼继续免于水下缺氧。

两份效果名单均按实体 ID 精确匹配。幻翼现不受这两个效果影响；哞菇、行商羊驼现已加入两份名单。所有其他未列入的实体不受相应效果的移动限制或额外氧气消耗，包括用户列出的不生效实体。窒息不生效不等于获得新的水下呼吸能力，未获免缺氧保护的生物仍可能正常溺水。哞兽 `moobloom` 和冻尸 `frosted_zombie` 当前未注册，也未添加任何必需标签引用。

模组水下免缺氧兼容项的来源与当前状态：

| 生物 | 实体 ID | 状态 |
| --- | --- | --- |
| 泥沼蟹 | `wild_wind_wilderness:mudcrab` | 已随远端 `26.2` 分支合并，当前版本实际注册并通过水下呼吸测试 |
| 恐鱼（Direfish，原食人鱼） | `wild_wind_wilderness:piranha` | 已实际注册并通过水下呼吸测试；更名后沿用原 ID，兼容已有存档 |

上述两个 ID 以 `required: false` 加入标签。`1.0.1` 已包含泥沼蟹和恐鱼的完整实现；恐鱼、生恐鱼、熟恐鱼、恐鱼刷怪蛋、恐鱼桶及音效字幕均使用新名称，英文为 Direfish。召唤恐鱼使用 `/summon wild_wind_wilderness:piranha`。标签中旧的可选预留项 `wild_wind_core:direfish` 保留兼容，但它不是已注册的实体 ID，也不是召唤别名。安装时需要 GeckoLib 5.5.4 的 Minecraft 26.2 NeoForge 版本。

### 用数据包调整名单

| 标签 | 数据包文件 |
| --- | --- |
| `wild_wind_wilderness:water_breathing` | `data/wild_wind_wilderness/tags/entity_type/water_breathing.json` |
| `wild_wind_wilderness:sink_affected` | `data/wild_wind_wilderness/tags/entity_type/sink_affected.json` |
| `wild_wind_wilderness:choke_affected` | `data/wild_wind_wilderness/tags/entity_type/choke_affected.json` |

在数据包中使用同路径 JSON 即可扩展名单。例如，让自定义生物受到沉溺影响：

```json
{
  "replace": false,
  "values": [
    { "id": "your_mod:your_entity", "required": false }
  ]
}
```

`replace: false` 追加，`replace: true` 替换该标签名单。执行 `/reload` 后生效，标签通过原版数据同步传给客户端。`minecraft:can_breathe_under_water` 标签也引用了免缺氧名单，使原版呼吸判定保持一致。

```mcfunction
# 持续 60 秒，默认 I 级
/effect give @s wild_wind_wilderness:sink 60
/effect give @s wild_wind_wilderness:choke 60
# 窒息 II，指令中的等级参数从 0 开始
/effect give @s wild_wind_wilderness:choke 60 1
/effect clear @s wild_wind_wilderness:sink
/effect clear @s wild_wind_wilderness:choke
```

## 熔炼附魔

- ID：`wild_wind_core:melting`，中文「熔炼」，英文「Melting」，最高 I 级。
- 支持 `minecraft:enchantable/mining` 标签中的工具，包括镐、斧、锹、锄与剪刀。
- 加入原版非宝藏附魔标签，沿用原版相应的附魔台、附魔书交易及随机附魔入口。
- 手持工具执行 `/enchant @s wild_wind_core:melting 1` 可直接添加。

处理顺序如下：

1. 原版先生成挖掘掉落，包含时运的增量。
2. 如果工具有精准采集，直接保留所有原掉落，不扣经验。
3. 查询当前世界的熔炉配方，计算全部可转换掉落需要的经验。
4. 当前可用经验不足时，整次不转换、不扣经验。
5. 经验足够时，扣除经验并替换对应掉落；没有熔炉配方的掉落保持原样。

**每个输入物品需要 1 点经验值，不是 1 级。** 例如时运产出 3 个粗铁时，需要 3 点经验并得到 3 个铁锭；只有 2 点经验时仍掉落 3 个粗铁。配方产出数量大于 1 时，费用仍按输入数量计算。经验会跨等级准确扣除，采用当前等级与经验条余额，不采用可能因附魔或指令而失真的累计经验计数。

熔炼不额外发放熔炉经验。判断发生在方块掉落事件，不会转换玩家背包中已有的物品或生物掉落；没有玩家作为破坏者的自动采矿不触发。精准采集、时运与熔炼可以同时保留在工具上，实际执行仍遵循上述优先级。

## 骷髅生成与近战

骷髅、流浪者、沼骸、焦骸（`minecraft:skeleton`、`minecraft:stray`、`minecraft:bogged`、`minecraft:parched`）在初始化生成装备时独立进行一次 10% 判定，将主手弓替换为石剑。原版会按武器选择近战／远程 AI；读档和重新加载区块不会重新抽取。凋灵骷髅继续使用原版装备规则。

| 生物 | 近战成功命中的效果 |
| --- | --- |
| 骷髅 | 无额外效果 |
| 流浪者 | 缓慢 I，600 游戏刻／30 秒 |
| 沼骸 | 中毒 I，80 游戏刻／4 秒 |
| 焦骸 | 虚弱 I，600 游戏刻／30 秒 |

仅对成功伤害到的活体目标添加效果；失败攻击不触发。箭矢保留各自的原版效果。

测试时可用对应原版刷怪蛋观察生成概率；要直接验证近战，可以生成生物后将其武器换成石剑：

```mcfunction
/summon minecraft:stray ~ ~ ~
/item replace entity @e[type=minecraft:stray,sort=nearest,limit=1] weapon.mainhand with minecraft:stone_sword
```

在生存模式被命中后检查缓慢 I；替换实体 ID 为 `minecraft:bogged` 或 `minecraft:parched` 可检查另外两种效果。

## 耕地保护

生物或玩家满足下列任一条件，摔落到耕地时不会使其退化为泥土：

- 任意已穿戴盔甲槽中有「摔落缓冲」附魔，I 级即可；仅放在手上或背包中无效。
- 当前具有原版「缓降」效果（`minecraft:slow_falling`）。

保护仅阻止摔落踩踏，不取消摔落伤害，不影响缺水、上方被方块阻挡等原版退化规则。可在湿润耕地上分别用普通靴子、摔落缓冲靴子和 `/effect give @s minecraft:slow_falling 60` 对照测试。

## 创造模式物品栏

所有已注册的模组方块物品与普通物品集中在「原野之风」分页，ID 为 `wild_wind_core:wild_wind`，图标使用杜鹃木板。另收录两种延展药水的普通／喷溅／滞留形态、对应药箭和熔炼 I 附魔书。原版分类中的重复入口已移除，全局搜索仍能找到模组内容。以后加入 `ModBlocks.ITEMS` 或 `ModItems.ITEMS` 的物品会自动收录。

## 开发与验证

代码分别位于 `src/core/java/org/polaris2023/wildwind/core/` 与 `src/wilderness/java/org/polaris2023/wildwind/wilderness/`：

| 路径 | 用途 |
| --- | --- |
| `recipe/` | 羊毛配方匹配、输出与剪刀耐久 |
| `mixin/CrafterBlockMixin.java` | 自动合成器的剪刀返还与耐久处理 |
| `event/CobwebBreaking.java` | 服务端蜘蛛网接触与移动计时 |
| `effect/` | 状态效果注册、速度限制与氧气消耗 |
| `mixin/EntityFluidMovementMixin.java` | 在实际位移入口限制流体中上浮 |
| `enchantment/MeltingEnchantment.java` | 熔炼掉落计划、经验计算及扣除 |
| `mixin/AbstractSkeletonMixin.java`、`event/SkeletonBehavior.java` | 生成武器判定与近战命中效果；命中入口位于 `MobMixin` |
| `event/FarmlandProtection.java` | 摔落缓冲／缓降的耕地踩踏保护 |
| `registry/ModCreativeTabs.java` | 独立创造分页及原版重复内容移除 |

配方、附魔及附魔标签直接保存在 `src/core/resources/data/`；效果图标位于 `src/wilderness/resources/assets/wild_wind_wilderness/textures/mob_effect/`。中英文翻译同步维护源码备用资源与语言数据生成器。

在 JDK 25 环境执行：

```powershell
.\gradlew.bat :datagen:runClientData
.\gradlew.bat :datagen:runServerData
python scripts/sync_generated_resources.py
.\gradlew.bat build collectMods :verification:runGameTest
```

`GameplayGameTests` 注册 16 组游戏测试：所有颜色羊毛与槽位顺序、手动连续合成及 Shift 转移、剪刀损坏与耐久附魔、红石驱动的自动合成器、水／熔岩中的沉溺、窒息与呼吸保护、蜘蛛网 30／160 刻边界及豁免、实际实体刻事件、精准采集与时运优先级、经验不足、跨等级扣费及原版方块掉落路径。其中 `FluidEntityRulesGameTests` 核对两份效果名单的完整 ID 集合，逐类验证水下免缺氧名单（含泥沼蟹与恐鱼）、沉溺移动限制，以及所有窒息生效实体的实际水中耗氧、等级倍率和呼吸保护。另有 `land_choke` 测试蝌蚪与美西螈的真实陆地耗氧、伤害阈值、移除效果及返回水中的恢复，并确认共享基类中的鳕鱼不受影响。`UpstreamIntegrationGameTests` 检查泥沼蟹变种、生物桶、恐鱼熔炉配方、延展属性，以及万用蟹钳中的熔炼工具通过真实玩家挖掘路径扣费并产生铁锭。`SkeletonAndFarmlandGameTests` 新增四组测试：固定种子下四类骷髅各 200 次真实生成及武器 AI，三种近战效果的时长／等级和失败攻击，真实摔落路径的耕地保护与剩余伤害，以及模组分页完整性、药水去重与全局搜索。`KilnGameTests` 的三组测试验证窑炉的合成与方块属性、100 刻边界、原料排除、燃料及读档、输出阻塞、湿海绵装水、漏斗流向与取出顺序、经验领取、命名锁定、比较器、破坏掉落，以及六种石匠屋各四个旋转方向的真实结构池放置。另有原来的物品、泥猪与结构测试，加上杜鹃木告示牌、展示架、展示架红石联动、延展箭行为及获取途径五组测试，合计 24 组游戏测试。预留的 `direfish` 项只验证不阻断标签加载，无法执行实体行为测试。


## 杜鹃木功能方块与延展之箭

普通／悬挂式告示牌和三槽展示架归核心，独立延展之箭归荒野，全部位于“原野之风”分页。获取命令、配方、红石交换、交易与英雄礼物规则见 [详细说明](AZALEA_AND_REACH_ARROWS.md)。
