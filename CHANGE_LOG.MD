# Changelog

All notable changes to the Wild Wind Module project will be documented in this file.

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
| 木材类型 | 方块数量 | 特点 |
|---------|---------|------|
| 焚烬木 (Blaze) | 16种 | 橙色调，下界风格 |
| 灵焰木 (Soul) | 16种 | 青色调，灵魂主题 |
| 杜鹃木 (Azalea) | 13种 | 浅绿色调，繁茂洞穴 |

每种套件包含：原木、木头、去皮原木、去皮木头、木板、楼梯、台阶、栅栏、栅栏门、门、活板门、压力板、按钮、树叶、树苗

#### 3. 世界生成
- **焚烬木** - 下界废土群系 (Nether Wastes)
- **灵焰木** - 灵魂沙峡谷群系 (Soul Sand Valley)
- **杜鹃木** - 繁茂洞穴群系 (Lush Caves)，替换原版杜鹃树

#### 4. 杜鹃树增强
- **Mixin 注入** - 修改杜鹃花催熟行为，生成杜鹃木原木
- **生物群系修改** - 移除原版杜鹃树，添加自定义杜鹃树
- **保留原版树叶** - 使用原版杜鹃树叶和开花杜鹃树叶

### 技术实现

#### 数据生成
- `HFASDatagen` - 数据生成入口
- `ModModelProvider` - 模型生成（支持染色树叶）
- `ModLootProvider` - 战利品表生成
- `EnUsLanguageProvider` / `ZhCnLanguageProvider` - 语言文件生成

#### 世界生成系统
- `ModConfiguredFeatures` - 配置特征（树木结构）
- `ModPlacedFeatures` - 放置特征
- `ModBiomeModifiers` - 生物群系修改器
- `ModTreeGrowers` - 树苗生长器
- `ModDatapackProvider` - 数据包提供器

#### 自定义类型
- `BlockSetType` - 焚烬木、灵焰木、杜鹃木
- `WoodType` - 对应木材类型
- `TreeGrower` - 自定义树木生长逻辑

### 文件结构
```
src/hfas/java/org/polaris2023/wildwind/hfas/
├── block/                    # 方块注册
│   ├── ModBlocks.java       # 方块定义
│   ├── FletchingTableBlock.java
│   └── entity/              # 方块实体
├── worldgen/                 # 世界生成
│   ├── ModConfiguredFeatures.java
│   ├── ModPlacedFeatures.java
│   ├── ModBiomeModifiers.java
│   ├── ModTreeGrowers.java
│   └── ModWorldGen.java
├── datagen/                  # 数据生成
│   ├── HFASDatagen.java
│   └── provider/
├── event/                    # 事件处理
│   └── FletchingTableReplaceHandler.java
├── mixin/                    # Mixin 注入
│   └── AzaleaBlockMixin.java
├── config/                   # 配置文件
│   └── ModCommonConfig.java
├── menu/                     # GUI 菜单
└── groups/                   # 创造模式物品栏
    └── ModCreativeTabs.java
```

---

## [1.0.0] - 2026.04.15

### Added
- **区块加载替换机制** - 使用 `ChunkEvent.Load` 事件在区块加载时替换制箭台
- **制箭台GUI系统** - `FletchingTableBlock`、`FletchingTableBlockEntity`、`FletchingTableMenu`
- **数据生成系统** - 模型、战利品表、语言文件生成
- **焚烬木套件** - 16种方块（原木、木板、楼梯、门等）
- **灵焰木套件** - 16种方块
- **杜鹃木套件** - 13种方块（不含树叶和树苗，使用原版）
- **树木自然生成** - 焚烬木（下界）、灵焰木（灵魂沙峡谷）、杜鹃木（繁茂洞穴）
- **杜鹃树增强** - Mixin 修改催熟行为，使用杜鹃木原木
- **配置文件系统** - `worldgen.replaceVanillaFletchingTable` 配置项
- **方块放置事件处理器** - `FletchingTableReplaceHandler
- 焦灰草方块、焦土、焦灰草丛、焦灰枝条、仙人球
- 制箭台（带GUI功能）
- 创造模式物品栏自动排序

### Changed
- 移除 `StructureTemplateMixin`，改用事件监听方式
- 使用 `BiomeModifiers.RemoveFeaturesBiomeModifier` 移除原版杜鹃树
- 使用自定义 `BlockSetType` 和 `WoodType`

## 开发计划

### 已完成功能
- [x] 模组制箭台GUI功能
- [x] 世界生成时替换结构内制箭台
- [x] 创造模式物品栏排序
- [x] 焚烬木/灵焰木/杜鹃木套件
- [x] 树木自然生成
- [x] 杜鹃树催熟增强

### 待实现功能
- [ ] 树叶堆肥功能
- [ ] 原木去皮功能
- [ ] 木质方块作为燃料
