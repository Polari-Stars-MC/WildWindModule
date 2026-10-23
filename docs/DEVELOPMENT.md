# 开发指南

本指南对应 Minecraft `26.2` 适配代码。项目介绍、版本与模块状态见 [README](../README.md)。

## 环境与构建

- 使用 JDK 25；`build.gradle.kts` 为模块设置了 Java 25 工具链。
- 使用仓库自带的 Gradle Wrapper，无需单独安装 Gradle。
- Gradle 配置了 Foojay 工具链解析器，但首次依赖下载仍需要可用网络。
- 当前只启用 `hfas`（`ww_hfas`）一个子项目；新物品、泥猪与 `src/structures/` 中的结构功能统一编译和打包。
- 远端生物依赖 GeckoLib 5.5.4；Gradle 从 GeckoLib 官方 Maven 仓库获取依赖，游戏客户端与服务端也需要安装对应 26.2 NeoForge JAR。

以下命令均在仓库根目录执行，Windows 使用 `.\gradlew.bat`，Linux / macOS 使用 `./gradlew`：

| 操作 | Windows PowerShell 命令 |
| --- | --- |
| 查看模块任务 | `.\gradlew.bat :hfas:tasks --all` |
| 生成客户端数据 | `.\gradlew.bat :hfas:runClientData` |
| 生成服务端数据 | `.\gradlew.bat :hfas:runServerData` |
| 构建项目 | `.\gradlew.bat build` |
| 启动开发客户端 | `.\gradlew.bat :hfas:runClient` |
| 启动开发服务端 | `.\gradlew.bat :hfas:runServer` |
| 清理构建输出 | `.\gradlew.bat clean` |
| 全部单元测试（模型与结构） | `.\gradlew.bat :hfas:test` |
| 联合游戏测试（物品、泥猪、结构与新增玩法） | `.\gradlew.bat :hfas:runGameTest` |
| 构建统一模组 | `.\gradlew.bat :hfas:build` |

这些运行入口来自构建脚本中的 `client`、`server`、`clientData`、`serverData` 配置。依赖解析成功是运行任务的前提。

### 数据生成顺序

HFAS 首次构建、清理后构建，或修改数据生成代码后，按顺序执行客户端数据生成、服务端数据生成、`build`。开发运行前也应先生成所需资源。任一步失败时，先解决失败原因再继续。结构功能的资源已直接包含在源码中并随 HFAS 打包，无需单独的数据生成，配置与测试说明见 [STRUCTURES.md](STRUCTURES.md)。

| 输出目录 | 来源与内容 |
| --- | --- |
| `build/generated/hfas/client/` | 模型、`en_us` / `zh_cn` 语言文件 |
| `build/generated/hfas/server/` | 方块与物品标签、掉落表、世界生成、数据映射、配方生成入口 |
| `modules/hfas/build/generated/sources/modMetadata/` | 从 `src/templates/` 展开的模组元数据 |
| `modules/hfas/build/libs/` | 构建的 JAR |
| `modules/hfas/build/runs/hfas/` | 开发客户端、服务端及数据生成的运行目录 |

泥猪的实体贴图、刷怪蛋定义、七个普通物品的贴图与模型、最低限度的中英文翻译与生成／掉落数据直接保存在源码中，单独运行 `:hfas:build` 也能打包；数据生成后的完整翻译及物品模型优先于源码中的备用文件。其他 HFAS 内容仍应遵循上述数据生成顺序。

生成资源被加入模块的资源集，但 `build` 不会自动先运行数据生成，`clean` 会删除生成结果。生成器是否输出了完整内容还取决于其具体实现，例如当前箭矢配方生成方法为空。

CI 使用 JDK 25，按顺序执行客户端数据生成、服务端数据生成、构建及统一 GameTest。`.bbmodel` 文件作为建模源文件保存在源码资源目录中，打包时被排除。生成的 JSON 优先于源码备用资源；未注册的船、焚烬木／灵焰木告示牌、盆栽树苗相关资源过滤规则见 `modules/hfas/build.gradle.kts`。

## 源码导航

HFAS Java 包根目录为 `src/hfas/java/org/polaris2023/wildwind/hfas/`。

| 入口 / 目录 | 职责 |
| --- | --- |
| `HFASMod.java` | 模组入口，注册方块、物品、菜单、方块实体、组件与配置 |
| `block/`、`block/entity/` | 方块、制箭台及窑炉方块实体 |
| `registry/` | 统一物品、实体、属性、效果、药水、数据组件和创造物品栏注册 |
| `item/` | 万用蟹钳行为、恐鱼食物属性 |
| `entity/`、`client/` | 泥猪、泥沼蟹、恐鱼行为及模型渲染，呼吸／沉溺／窒息实体标签 |
| `menu/` | 制箭台与窑炉菜单、槽位逻辑 |
| `arrow/`、`component/` | 箭矢部件定义与数据组件序列化 |
| `config/` | COMMON 配置规范 |
| `event/` | 制箭台替换、燃料事件、蜘蛛网计时、骷髅近战与耕地保护 |
| `recipe/` | 羊毛与剪刀的无序合成、剪刀返还及耐久 |
| `effect/`、`enchantment/` | 沉溺、窒息效果与消耗经验的熔炼附魔 |
| `worldgen/` | 树木、放置特征、生物群系修改器及石匠屋窑炉处理器 |
| `mixin/` | 杜鹃骨粉生长、自动合成器剪刀耐久、流体中上浮限制、蝌蚪／美西螈离水窒息、铁傀儡窒息耗氧 |
| `datagen/` | 客户端 / 服务端数据生成入口和各类 Provider |

修改资源时注意命名空间 `ww_hfas`，同时核对注册 ID、模型纹理引用、语言键、标签与配方。`gradle.properties` 中的 `modId=wild_wind` 没有直接作为 HFAS 的最终 ID；当前构建脚本通过根项目名 `ww` 与模块名 `hfas` 组合得到 `ww_hfas`。

## 模块维护

模块规划采用编号 **0：原野之风：核心（Wild Wind:Core）**、**1：原野之风：荒野更新（Wild Wind:Wilderness Update）**、**2：原野之风：瘠土更新（Wild Wind:Barren Update）**。功能归属和代码拆分尚待落实，当前构建仍使用 `hfas` 子项目与 `ww_hfas` 模组 ID。

当前构建约定将 `src/<模块名>/java/` 和 `src/<模块名>/resources/` 接入相应子项目。`modules/hfas/build.gradle.kts` 额外接入 `src/structures/` 的代码、资源和测试，由 `HFASMod` 调用 `StructureModule.register`；结构功能没有独立 `@Mod` 入口。`modules/<模块名>/` 在配置阶段创建。新增模块需要同步修改 `settings.gradle.kts` 的启用列表，并核对模组入口、资源命名空间、Mixin 配置与共用元数据模板。仅在 README 增加模块名称不会生成可用模块。

## 待完善事项

以下项目根据当前源码整理，是后续开发清单，不代表已完成功能：

- **制箭交互**：补齐客户端 Screen 与注册，核对菜单和方块实体的容器连接、槽位数量及快捷移动索引；当前制箭台没有注册对应方块物品。
- **箭矢玩法**：实现配方生成及实际合成流程，并验证部件数据的保存、网络同步与效果行为。
- **未完成木材内容**：远端已补入 `blaze_*` / `soul_*` 纹理；船、焚烬木／灵焰木告示牌、盆栽树苗仍未注册；杜鹃木告示牌与展示架已实现，相关不可用数据暂不打包。
- **数据生成与打包**：CI 已显式生成数据并运行测试；本地 `build` 仍不自动触发数据生成，须遵循本页顺序。
- **存档行为**：验证制箭台对旧区块的替换逻辑、扫描范围与性能；README 已记录默认开启和不会自动回滚的行为。
- **元数据**：由维护者统一 `modLicense` 与仓库许可证声明。
- **模块划分**：按编号 0「核心」、1「荒野更新」、2「瘠土更新」明确功能归属，再落实对应的代码与构建调整；当前仍统一打包。

## 提交前验证

新增合成、蜘蛛网、状态效果和熔炼的行为与游戏测试覆盖见 [GAMEPLAY.md](GAMEPLAY.md)。统一游戏测试共 24 组，包含骷髅生成与近战、耕地保护、创造分页、远端内容集成、生物适用名单、两栖生物离水窒息、窑炉烧炼／容器／村庄生成、杜鹃木告示牌／展示架及延展之箭获取与行为，以及原有的物品、泥猪与结构测试。

文档改动应核对相对链接、命令、版本和模块状态，并执行 `git diff --check`。

涉及代码或资源的改动，应按影响范围运行数据生成与构建，再进行对应客户端或服务端验证；世界生成与制箭台替换需要同时关注新世界及已有区块。Pull Request 中应写明实际执行的检查，以及尚未验证的部分。


杜鹃木告示牌和展示架通过 `BlockEntityTypeAddBlocksEvent` 扩展原版方块实体支持，沿用原版网络包、渲染器和库存能力。告示牌文本的客户端 Mixin 仅修改杜鹃木颜色与亮度。`ReachArrowItem` / `ReachArrow` 沿用原版药箭行为；合成与药水掉落函数仅在延展药水时转换物品，村民交易继续使用原版数据池。详细设计与测试见 [AZALEA_AND_REACH_ARROWS.md](AZALEA_AND_REACH_ARROWS.md)。
