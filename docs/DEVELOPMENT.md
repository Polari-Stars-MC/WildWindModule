# 开发指南

本项目面向 Minecraft 26.2、NeoForge 26.2.0.93、JDK 25。使用仓库 Gradle Wrapper；首次下载依赖需要网络。

## 工程结构

| Gradle 项目 | 源码目录 | 用途与依赖 |
| --- | --- | --- |
| `core` | `src/core/` | 模块 0；无需 GeckoLib，不引用两个可选模块 |
| `wilderness` | `src/wilderness/` | 模块 1；依赖 Core 与 GeckoLib 5.5.4 |
| `barren` | `src/barren/` | 模块 2；只依赖 Core |
| `datagen` | `src/datagen/` | 开发用数据生成器；加载全部模块 |
| `verification` | `src/verification/` | 开发用 27 组综合 GameTest；加载全部模块 |
| `smoke` | `src/smoke/` | 开发用安装组合测试；由 `testModules` 选择依赖 |

发行模块有各自的入口、注册器、Mixin、资源、元数据与 JAR。`src/templates/` 是元数据模板。辅助项目不放入发行目录。

核心包 `org.polaris2023.wildwind.core` 包含杜鹃木、窑炉、通用玩法、熔炼和材料；`org.polaris2023.wildwind.structures` 同样位于核心源码中，保留结构兼容命名空间。荒野包为 `org.polaris2023.wildwind.wilderness`，瘠土包为 `org.polaris2023.wildwind.barren`。

核心提供创造分页和 `WildWindModules` 命名空间判断；扩展模块通过事件将自己的物品加入分页。荒野的蟹钳挖掘与核心熔炼事件协作，不要求核心反向引用荒野类型。

## 构建与开发运行

Windows 在仓库根目录执行；其他系统把 `.\gradlew.bat` 改为 `./gradlew`。

```powershell
# 资源快照随源码保存，干净检出即可构建
.\gradlew.bat build collectMods
# 核心 / 核心+荒野 / 核心+瘠土开发客户端
.\gradlew.bat :core:runClient
.\gradlew.bat :wilderness:runClient
.\gradlew.bat :barren:runClient
# 全部模块开发客户端（加载仅供开发的测试模组）
.\gradlew.bat :smoke:runClient -PtestModules=core,wilderness,barren
```

单个模块产物在 `modules/<模块>/build/libs/`，发行集合在 `build/distributions/mods/`。开发世界、日志和测试报告在对应模块 `build/` 下，不纳入版本控制。

## 数据生成

```powershell
.\gradlew.bat :datagen:runClientData :datagen:runServerData
python scripts/sync_generated_resources.py
.\gradlew.bat build collectMods
```

需要 Python 3.9+。生成输出在 `build/generated/datagen/{client,server}/`；同步脚本按注册命名空间分配到三个模块资源目录，并拆分原版／公共标签、数据映射和语言文件。手工资源保留；修改生成器时应提交生成后的快照。删除某项生成内容时，同时删除其已提交的旧快照，避免旧资源残留。

模型 Provider 检查三个命名空间的所有已注册方块和物品。未注册的船、焚烬木／灵焰木告示牌等源素材保留，不应为它们新增有效配方。`.bbmodel` 源文件不打入发行 JAR。历史占位材质问题见进度文档。

`build` 不会启动游戏数据生成器，避免构建循环；CI 会显式重新生成、同步并检查快照一致性。

## 验证

```powershell
.\gradlew.bat :core:test :wilderness:test
.\gradlew.bat :verification:runGameTest
.\gradlew.bat :smoke:runGameTest -PtestModules=core
.\gradlew.bat :smoke:runGameTest -PtestModules=core,wilderness
.\gradlew.bat :smoke:runGameTest -PtestModules=core,barren
.\gradlew.bat :smoke:runGameTest -PtestModules=core,wilderness,barren
# 可选本机客户端启动检查：进入标题画面并完成资源检查后自动退出
.\gradlew.bat :smoke:runClient -PtestModules=core,wilderness,barren -PclientSmoke=true
```

安装组合测试加载实际发行模块 JAR，核对依赖、GeckoLib、模块内容归属、创造分页及制箭台移除。各组合使用不同运行目录。综合 GameTest 覆盖结构生成／指令、泥猪、羊毛／剪刀、蜘蛛网、状态效果名单、窑炉、骷髅、耕地、创造分页、杜鹃木功能方块、延展箭及蟹钳联动。

若中文 Windows 路径下 JUnit 出现类找不到，而 `.class` 确实存在，可加 `"-Dorg.gradle.jvmargs=-Xmx1G -Dfile.encoding=GBK"` 使 Gradle 参数文件编码匹配本机 Java 启动器；源码始终 UTF-8。

## ID 与存档

新注册内容使用各自 `wild_wind_*` 命名空间。旧 `ww_hfas:*` 内容没有自动存档映射，不要在唯一旧存档上直接升级。结构的 `ww_structures` 配置、结构 ID 和坐标索引保留，但 NBT 模板内引用旧模组方块时仍需更新。完整分配与安装要求见 [README](../README.md)。

自定义部件制箭台和旧替换配置已移除；历史代码可从 Git 历史查阅。已完成的延展药水箭在荒野中保留。

提交前运行 `git diff --check`，按改动范围执行上述测试；服务端自动测试不替代游戏内视觉、多人联机和长期存档验收。

核心 `inspection/` 包提供书与笔记录器，新增字段时同步维护 `BOOK_INSPECTION.md`。实体交互／书本上限／硫方怪物理参数在 `BookInspectionGameTests` 中验证。
