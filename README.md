# Wild Wind Modules · 原野之风模块包

面向 Minecraft NeoForge 的模块化模组项目，围绕木材、植物、世界生成和制箭玩法扩展原版内容。

> 当前说明对应 `26.1` 分支。项目处于开发阶段，目前仅启用 **HFAS（`ww_hfas`）**；下文区分源码中已有的实现与尚未完成的玩法，具体游戏表现仍需运行验证。

[开发指南](docs/DEVELOPMENT.md) · [历史变更](CHANGE_LOG.MD) · [问题反馈](https://github.com/Polari-Stars-MC/WildWindModule/issues) · [许可证](LICENSE)

## 版本与环境

| 项目 | 当前配置 |
| --- | --- |
| Minecraft | `26.1.2`（依赖范围为精确版本 `[26.1.2]`） |
| 模组加载器 | NeoForge `26.1.2.10-beta` |
| Java 开发工具链 | JDK `25` |
| Gradle Wrapper | `9.4.1` |
| 模组版本 | `1.0.0`（构建配置值） |
| HFAS 模组 ID / 资源命名空间 | `ww_hfas` |

版本以 [gradle.properties](gradle.properties) 和 [构建脚本](build.gradle.kts) 为准。`1.0.0` 是源码中的版本号，不代表已经发布稳定版。

## 模块规划

| 模块 | 名称 | 当前分支状态 |
| --- | --- | --- |
| `hfas` | Wild Wind HFAS | 已接入 Gradle，包含木材、植物、世界生成及制箭系统基础代码 |
| `deco` | Wild Wind Decoration · 装饰 | 旧版模块规划，尚未在当前分支启用 |
| `adv` | Wild Wind Adventure · 冒险 | 旧版模块规划，尚未在当前分支启用 |
| `vpp` | Wild Wind Vanilla++ · 原版修饰 | 旧版模块规划，尚未在当前分支启用 |

模块启用列表见 [settings.gradle.kts](settings.gradle.kts)。

## HFAS 内容

### 木材与植物

源码已注册三组木质方块：

| 木材 | 注册内容 | 树木相关实现 |
| --- | --- | --- |
| 焚烬木 `blaze` | 15 种方块 | 独立树叶、树苗；配置在下界废土生成 |
| 灵焰木 `soul` | 15 种方块 | 独立树叶、树苗；配置在灵魂沙峡谷生成 |
| 杜鹃木 `azalea` | 13 种方块 | 复用原版杜鹃植物与树叶；配置替换繁茂洞穴的原版杜鹃树生成 |

三组木材均包含原木、木头、去皮原木、去皮木头、木板、楼梯、台阶、栅栏、栅栏门、门、活板门、压力板和按钮。焚烬木、灵焰木另有树叶和树苗。

此外已注册焦灰草方块、焦土、焦灰草丛、焦灰枝条、仙人球，并实现木材去皮映射、部分木质物品的燃料处理，以及向原版创造模式分类添加木材的逻辑。杜鹃施加骨粉的行为通过 Mixin 接入自定义树木生成。

### 制箭系统（开发中）

目前包含自定义制箭台方块、方块实体、菜单、箭羽／箭杆／箭头枚举和箭矢数据组件。客户端界面与实际合成流程尚未完成，箭矢配方生成方法仍为空；当前不能视作完整可玩的制箭系统。

模型、语言、标签、掉落表和世界生成数据由数据生成器输出；从源码构建前请阅读[开发指南](docs/DEVELOPMENT.md)。纹理与注册 ID 的一致性也仍需整理。

## 安装与配置

取得经过验证、与上述 Minecraft / NeoForge 版本匹配的模组 JAR 后，将其放入对应实例的 `mods/` 目录。从源码生成 JAR 的步骤见下方。当前开发状态与待完善项见[开发指南](docs/DEVELOPMENT.md#待完善事项)。

HFAS 注册了一个 COMMON 配置项：

| 配置键 | 默认值 | 作用 |
| --- | --- | --- |
| `worldgen.replaceVanillaFletchingTable` | `true` | 启用服务端原版制箭台替换逻辑 |

替换发生在方块放置事件及区块加载时。**已有存档中加载的区块也会受影响**：当前区块扫描从世界最低建造高度至固定 `Y=200`，将其中的原版制箭台替换为模组制箭台。关闭配置只会停止后续替换，不会自动恢复已经替换的方块。由于制箭流程尚未完成，测试前请结合这一行为选择配置。

## 从源码开始

准备 JDK 25，在仓库根目录执行。首次运行需要联网下载 Gradle、插件与游戏依赖。

```powershell
# Windows PowerShell
git clone --branch 26.1 https://github.com/Polari-Stars-MC/WildWindModule.git
cd WildWindModule
.\gradlew.bat :hfas:runClientData
.\gradlew.bat :hfas:runServerData
.\gradlew.bat build
```

```bash
# Linux / macOS
git clone --branch 26.1 https://github.com/Polari-Stars-MC/WildWindModule.git
cd WildWindModule
chmod +x gradlew
./gradlew :hfas:runClientData
./gradlew :hfas:runServerData
./gradlew build
```

数据生成成功后再构建；若生成任务报错，请先处理报错，不要把缺少生成资源的 JAR 当作完整发行包。当前构建脚本未将数据生成任务自动接入 `build`。

HFAS 构建产物位于 `modules/hfas/build/libs/`，按当前配置命名为 `ww_hfas-1.0.0.jar`。开发客户端可通过 `:hfas:runClient` 启动。

## 项目结构

```text
WildWindModule/
├── src/
│   ├── hfas/
│   │   ├── java/                 # HFAS 注册、玩法、配置与数据生成代码
│   │   └── resources/            # 纹理、Blockbench 源文件、Mixin 配置等
│   └── templates/                # 各模块共用的模组元数据模板
├── modules/hfas/                # Gradle 配置时创建的子项目目录
│   └── build/                   # HFAS JAR、生成元数据和开发运行目录
├── build/generated/hfas/        # 数据生成输出（client/、server/）
├── docs/DEVELOPMENT.md           # 开发命令、源码导航与待完善项
├── .github/workflows/build.yml  # GitHub Actions 构建流程
├── build.gradle.kts             # 共用构建配置与模块任务
├── settings.gradle.kts          # 模块启用列表与项目目录映射
├── gradle.properties            # 版本、工具与模组属性
└── CHANGE_LOG.MD                # 历史变更记录
```

`modules/hfas/` 是 Gradle 子项目目录，实际源码集中在 `src/hfas/`。生成目录不纳入 Git；不要直接修改其中的文件，修改对应的数据生成器后重新生成。

## 参与开发

欢迎通过 Issue 反馈问题、讨论功能，并通过 Pull Request 提交修改。提交前请阅读[开发指南](docs/DEVELOPMENT.md)，注明目标分支、对应 Minecraft / NeoForge 版本、复现步骤及验证结果。历史记录见 [CHANGE_LOG.MD](CHANGE_LOG.MD)，当前实现状态以本分支源码为准。

## 许可证

仓库 [LICENSE](LICENSE) 声明采用分项许可：源码使用 [GPL-3.0-only](LICENSE_CODE)，资源、文档和配置等非代码内容使用 [CC-BY-SA-4.0](LICENSE_ASSETS)。具体条款请阅读对应文件。

当前 `gradle.properties` 中的 `modLicense` 仍为 `All Rights Reserved`，与上述仓库声明存在差异，需维护者统一元数据。
