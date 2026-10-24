# 2026-10-24 · 署名属性书与附魔台熔炼

- 核心属性记录器将主手／副手的书与笔直接转换为署名书，书名显示“目标名称(注册ID)”，作者为玩家。保留原始／过滤笔记；满页不转换；已署名书不再次写入。
- 长名称完整保存在物品显示名称中，内部标题遵守原版 32 字符上限，避免存档与同步错误。
- 显式将熔炼加入附魔台候选标签，保留现有等级、权重及经验消耗规则。新增真实附魔台菜单测试，验证四类挖掘工具和普通书的随机获取及附魔应用。

# 2026-10-23 · 书与笔属性记录器

- 核心新增潜行右键方块／实体记录属性，保留书中原有页，剩余空间不足时不写入。
- 支持硬度、基础爆炸抗性、工具、火焰、红石、活塞、注册 ID、燃料／加工时间、可再生名单等字段。
- 读取当前原版硫方怪原型及实际物理属性修改器，支持数据包调整；不适用／未知项显示 ×。
- 新增分页单元测试、3 组 GameTest，并验证核心单独安装和客户端实际字体排版。

---

# 2026-10-23 · 三模组拆分

- 按最新归属拆分 `wild_wind_core`、`wild_wind_wilderness`、`wild_wind_barren`，各有入口、注册代码、资源与发行 JAR。
- 荒野／瘠土分别依赖核心；只有荒野需要 GeckoLib。保留共享创造分页和蟹钳／熔炼联动。
- 杜鹃木、窑炉及通用玩法归核心；两种下界木材、生物、状态效果与焦灰植物归荒野；仙人球归瘠土。
- 暂时移除自定义部件制箭系统及原版制箭台替换行为，保留已完成的延展药水箭。
- 数据生成快照随源码保存；更新 README、进度、开发指南和 CI。
- 本地验证：11 项单元测试、24 组 GameTest、四种服务端安装组合，核心／完整组合客户端资源加载通过。
- 旧 `ww_hfas:*` 注册 ID 无自动存档迁移；升级前备份并用新世界验证。结构配置与结构资源兼容命名空间保留。

---

# Changelog

All notable changes to the Wild Wind Module project will be documented in this file.

---

## 项目总结 - HFAS 模块

### 模组概述
**HFAS (Wild Wind - Fletching Table and Saplings)** 是 Wild Wind Module 的子模块，专注于：
- 制箭台功能扩展
- 自定义木质方块套件
- 世界生成增强

### 核心功能

#### 1. 制箭台替换系统
- **区块加载替换** - 使用 `ChunkEvent.Load` 事件自动替换原版制箭台
- **配置控制** - 通过 `worldgen.replaceVanillaFletchingTable` 配置项控制开关
- **GUI系统** - 自定义制箭台带有交互界面（3个输入槽位）

#### 2. 木质方块套件
| 木材类型         | 方块数量 | 特点        |
|--------------|------|-----------|
| 焚烬木 (Blaze)  | 16种  | 橙色调，下界风格  |
| 灵焰木 (Soul)   | 16种  | 青色调，灵魂主题  |
| 杜鹃木 (Azalea) | 13种  | 浅绿色调，繁茂洞穴 |

每种套件包含：原木、木头、去皮原木、去皮木头、木板、楼梯、台阶、栅栏、栅栏门、门、活板门、压力板、按钮、树叶、树苗

#### 3. 世界生成
- **焚烬木** - 下界废土群系 (Nether Wastes)
- **灵焰木** - 灵魂沙峡谷群系 (Soul Sand Valley)
- **杜鹃木** - 繁茂洞穴群系 (Lush Caves)，替换原版杜鹃树

#### 4. 杜鹃树增强
- **Mixin 注入** - 修改杜鹃花催熟行为，生成杜鹃木原木
- **生物群系修改** - 移除原版杜鹃树，添加自定义杜鹃树
- **保留原版树叶** - 使用原版杜鹃树叶和开花杜鹃树叶

#### 5. 木质方块功能
- **去皮功能** - 使用斧头可将原木/木头去皮
- **堆肥功能** - 树叶和树苗可用于堆肥（30%概率）
- **燃料功能** - 木质方块可作为熔炉燃料（300 tick）

### 技术实现

#### 数据生成
| 提供器                    | 功能           |
|------------------------|--------------|
| `HFASDatagen`          | 数据生成入口       |
| `ModModelProvider`     | 模型生成（支持染色树叶） |
| `ModBlockTagsProvider` | 方块标签生成       |
| `ModItemTagsProvider`  | 物品标签生成       |
| `ModLootProvider`      | 战利品表生成       |
| `ModDatapackProvider`  | 数据包提供器       |
| `ModDataMapsProvider`  | 数据映射提供器      |
| `EnUsLanguageProvider` | 英文语言文件       |
| `ZhCnLanguageProvider` | 中文语言文件       |

#### 世界生成系统
| 类                       | 功能         |
|-------------------------|------------|
| `ModConfiguredFeatures` | 配置特征（树木结构） |
| `ModPlacedFeatures`     | 放置特征       |
| `ModBiomeModifiers`     | 生物群系修改器    |
| `ModTreeGrowers`        | 树苗生长器      |

#### 事件处理
| 类                              | 功能         |
|--------------------------------|------------|
| `FletchingTableReplaceHandler` | 区块加载时替换制箭台 |
| `BlockFeaturesHandler`         | 方块功能（堆肥等）  |
| `FuelHandler`                  | 燃料事件处理     |

#### Mixin 注入
| 类                  | 功能        |
|--------------------|-----------|
| `AzaleaBlockMixin` | 修改杜鹃花催熟行为 |

#### 自定义类型
| 类型             | 用途                     |
|----------------|------------------------|
| `BlockSetType` | 焚烬木、灵焰木、杜鹃木的门/活板门/按钮类型 |
| `WoodType`     | 对应木材类型                 |
| `TreeGrower`   | 自定义树木生长逻辑              |

### 文件结构
```
src/hfas/java/org/polaris2023/wildwind/hfas/
├── block/                    # 方块注册
│   ├── ModBlocks.java       # 方块定义
│   ├── FletchingTableBlock.java
│   └── entity/              # 方块实体
│       └── FletchingTableBlockEntity.java
├── worldgen/                 # 世界生成
│   ├── ModConfiguredFeatures.java
│   ├── ModPlacedFeatures.java
│   ├── ModBiomeModifiers.java
│   └── ModTreeGrowers.java
├── datagen/                  # 数据生成
│   ├── HFASDatagen.java
│   └── provider/
│       ├── loot/             # 实体掉落
│       ├── lang/             # 语言系统
│       ├── tag/              # Tag标签
│       ├── ModModelProvider.java
│       ├── ModLootProvider.java
│       ├── ModDatapackProvider.java
│       ├── ModDataMapsProvider.java
├── event/                    # 事件处理
│   ├── FletchingTableReplaceHandler.java
│   ├── BlockFeaturesHandler.java
│   └── FuelHandler.java
├── mixin/                    # Mixin 注入
│   └── AzaleaBlockMixin.java
├── config/                   # 配置文件
│   └── ModCommonConfig.java
├── menu/                     # GUI 菜单
│   └── FletchingTableMenu.java
└── groups/                   # 创造模式物品栏
    └── ModCreativeTabs.java
```

### 生成资源统计
| 类型   | 数量                        |
|------|---------------------------|
| 方块模型 | 118+                      |
| 物品模型 | 60+                       |
| 方块状态 | 45+                       |
| 战利品表 | 45+                       |
| 语言条目 | 90+ (en_us) / 90+ (zh_cn) |

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

## 开发计划

### 已完成功能
- [x] 模组制箭台GUI功能
- [x] 世界生成时替换结构内制箭台
- [x] 创造模式物品栏排序
- [x] 焚烬木/灵焰木/杜鹃木套件
- [x] 树木自然生成
- [x] 杜鹃树催熟增强
- [x] 木头去皮功能
- [x] 树叶堆肥功能
- [x] 木质方块燃料功能
- [x] 模型数据生成
- [x] 标签数据生成

### 待开发功能
- [ ] 更多下界植物
- [ ] 制箭台配方系统
- [ ] 成就系统
