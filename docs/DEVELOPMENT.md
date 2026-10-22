# 开发指南

本指南对应 `26.1` 分支。项目介绍、版本与模块状态见 [README](../README.md)。

## 环境与构建

- 使用 JDK 25；`build.gradle.kts` 为模块设置了 Java 25 工具链。
- 使用仓库自带的 Gradle Wrapper，无需单独安装 Gradle。
- Gradle 配置了 Foojay 工具链解析器，但首次依赖下载仍需要可用网络。
- 当前启用 `hfas`（`ww_hfas`）和 `structures`（`ww_structures`）两个子项目，可分别构建。

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
| 结构模块单元测试 | `.\gradlew.bat :structures:test` |
| 结构模块游戏测试 | `.\gradlew.bat :structures:runGameTest` |
| 仅构建结构模块 | `.\gradlew.bat :structures:build` |

这些运行入口来自构建脚本中的 `client`、`server`、`clientData`、`serverData` 配置。依赖解析成功是运行任务的前提。

### 数据生成顺序

HFAS 首次构建、清理后构建，或修改数据生成代码后，按顺序执行客户端数据生成、服务端数据生成、`build`。开发运行前也应先生成所需资源。任一步失败时，先解决失败原因再继续。结构模块的资源已直接包含在源码中，无需数据生成，配置与测试说明见 [STRUCTURES.md](STRUCTURES.md)。

| 输出目录 | 来源与内容 |
| --- | --- |
| `build/generated/hfas/client/` | 模型、`en_us` / `zh_cn` 语言文件 |
| `build/generated/hfas/server/` | 方块与物品标签、掉落表、世界生成、数据映射、配方生成入口 |
| `modules/hfas/build/generated/sources/modMetadata/` | 从 `src/templates/` 展开的模组元数据 |
| `modules/hfas/build/libs/` | 构建的 JAR |
| `modules/hfas/build/runs/hfas/` | 开发客户端、服务端及数据生成的运行目录 |

生成资源被加入模块的资源集，但 `build` 不会自动先运行数据生成，`clean` 会删除生成结果。生成器是否输出了完整内容还取决于其具体实现，例如当前箭矢配方生成方法为空。

当前 CI 使用 JDK 25 执行 `./gradlew build`，没有显式执行两类数据生成任务，因此构建通过本身不能证明生成资源完整或玩法可用。`.bbmodel` 文件作为建模源文件保存在源码资源目录中，打包时被排除。

## 源码导航

HFAS Java 包根目录为 `src/hfas/java/org/polaris2023/wildwind/hfas/`。

| 入口 / 目录 | 职责 |
| --- | --- |
| `HFASMod.java` | 模组入口，注册方块、物品、菜单、方块实体、组件与配置 |
| `block/`、`block/entity/` | 方块及制箭台方块实体 |
| `menu/` | 制箭台菜单与槽位逻辑 |
| `arrow/`、`component/` | 箭矢部件定义与数据组件序列化 |
| `config/` | COMMON 配置规范 |
| `event/` | 制箭台替换及燃料事件 |
| `groups/` | 向原版创造模式分类插入物品 |
| `worldgen/` | 树木、放置特征与生物群系修改器 |
| `mixin/` | 杜鹃骨粉生长行为修改 |
| `datagen/` | 客户端 / 服务端数据生成入口和各类 Provider |

修改资源时注意命名空间 `ww_hfas`，同时核对注册 ID、模型纹理引用、语言键、标签与配方。`gradle.properties` 中的 `modId=wild_wind` 没有直接作为 HFAS 的最终 ID；当前构建脚本通过根项目名 `ww` 与模块名 `hfas` 组合得到 `ww_hfas`。

## 模块维护

当前构建约定将 `src/<模块名>/java/` 和 `src/<模块名>/resources/` 接入相应子项目，`modules/<模块名>/` 在配置阶段创建。新增模块需要同步修改 `settings.gradle.kts` 的启用列表，并核对模组入口、资源命名空间、Mixin 配置与共用元数据模板。仅在 README 增加模块名称不会生成可用模块。

## 待完善事项

以下项目根据当前源码整理，是后续开发清单，不代表已完成功能：

- **制箭交互**：补齐客户端 Screen 与注册，核对菜单和方块实体的容器连接、槽位数量及快捷移动索引；当前制箭台没有注册对应方块物品。
- **箭矢玩法**：实现配方生成及实际合成流程，并验证部件数据的保存、网络同步与效果行为。
- **资源一致性**：方块注册使用 `blaze_*` / `soul_*`，现有相关纹理使用 `cinder_*` / `ember_*`；需统一引用与文件命名。资源中的船、告示牌贴图尚不对应已注册的玩法内容。
- **数据生成与打包**：验证两类数据生成任务，补齐资源后考虑将其接入打包流程及 CI，避免发布缺少模型、语言或世界生成 JSON 的产物。
- **存档行为**：验证制箭台对旧区块的替换逻辑、扫描范围与性能；README 已记录默认开启和不会自动回滚的行为。
- **元数据**：替换模组描述模板中的 `Example mod description.`，并由维护者统一 `modLicense` 与仓库许可证声明。
- **其他模块**：`deco`、`adv`、`vpp` 尚未加入当前分支的 Gradle 模块列表，需要分别实现后再更新状态。

## 提交前验证

文档改动应核对相对链接、命令、版本和模块状态，并执行 `git diff --check`。

涉及代码或资源的改动，应按影响范围运行数据生成与构建，再进行对应客户端或服务端验证；世界生成与制箭台替换需要同时关注新世界及已有区块。Pull Request 中应写明实际执行的检查，以及尚未验证的部分。
