# 开发指南

本项目面向 Minecraft 26.2、NeoForge 26.2.0.93、JDK 25。使用仓库 Gradle Wrapper；首次下载依赖需要网络。

## 工程结构

| Gradle 项目 | 源码目录 | 用途与依赖 |
| --- | --- | --- |
| `core` | `src/core/` | 模块 0；无需 GeckoLib，不引用两个可选模块 |
| `wilderness` | `src/wilderness/` | 模块 1；依赖 Core 与 GeckoLib 5.5.4 |
| `barren` | `src/barren/` | 模块 2；只依赖 Core |
| `datagen` | `src/datagen/` | 开发用数据生成器；加载全部模块 |
| `verification` | `src/verification/` | 开发用 28 组综合 GameTest；加载全部模块 |
| `smoke` | `src/smoke/` | 开发用安装组合测试；由 `testModules` 选择依赖 |

发行模块有各自的入口、注册器、Mixin、资源、元数据与 JAR。`src/templates/` 是元数据模板。辅助项目不放入发行目录。

核心包 `org.polaris2023.wildwind.core` 包含杜鹃木、窑炉、通用玩法、熔炼和材料；`org.polaris2023.wildwind.structures` 同样位于核心源码中，保留结构兼容命名空间。荒野包为 `org.polaris2023.wildwind.wilderness`，瘠土包为 `org.polaris2023.wildwind.barren`。

核心提供创造分页和 `WildWindModules` 命名空间判断；扩展模块通过事件将自己的物品加入分页。荒野的蟹钳挖掘与核心熔炼事件协作，不要求核心反向引用荒野类型。

## 构建与开发运行

Windows 在仓库根目录执行；其他系统把 `.\gradlew.bat` 改为 `./gradlew`。

```powershell
# 干净检出即可构建；生成资源由构建自动准备
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
.\gradlew.bat generateResources
.\gradlew.bat build collectMods
```

生成器直接写入 `build/generated/datagen/{client,server}/{core,wilderness,barren}/`，发行模块只读取自己的目录。`ReleaseModule` 显式选择模块：模型、配方、掉落、语言、标签与数据映射由对应 Provider 生成，不再使用 Python 后处理，也不向 `src/` 回写生成结果。语言条目统一在 Provider 中声明，包含模块标题及属性书翻译；删除生成项后由数据生成缓存清理过期输出。

`src/<模块>/resources/` 仅保留必要的手工资源，例如模型源文件、纹理、定制模型、配置、Mixin 声明和手工维护的数据标签；已经有生成器的同路径文件不得重复提交。所有三套木材常规配方和对应进度同样通过 datagen 生成。

模型 Provider 检查三个命名空间的所有已注册方块和物品。未注册的船、焚烬木／灵焰木告示牌等源素材保留，不应为它们新增有效配方。`.bbmodel` 源文件不打入发行 JAR。历史占位材质问题见进度文档。

`processResources` 依赖根任务 `generateResources`。为避免“发行 JAR 依赖 datagen，datagen 又依赖发行 JAR”的循环，每个发行模块提供内部 `datagenElements` 配置和 `datagenBootstrapJar`：只包含编译类、元数据与手工资源。生成器通过该内部产物加载注册内容；正式 JAR 再加入生成资源。内部产物不进入 `collectMods`，不能用于玩家安装。

CI 从 `clean build` 验证这条任务链，重复生成并比对 JSON 哈希，检查生成过程没有修改源码，然后运行综合游戏测试和四种安装组合测试。

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

核心 `inspection/` 包提供书与笔记录器，新增字段时同步维护 `BOOK_INSPECTION.md`。实体交互／署名书转换／书本上限／硫方怪物理参数在 `BookInspectionGameTests` 中验证。`EnchantmentTableGameTests` 使用真实附魔台菜单验证熔炼候选、随机预览和最终工具／书本附魔。

属性书在服务端保存 `Component.translatable` 字段及参数；`InspectionBookAccessMixin` 仅对本模组标记的属性页进行客户端排版，保留原始笔记和过滤结果。语言切换后重新打开书时使用当前字体重新分页，不改写书的保存内容。新增文本必须同时更新中英文语言 Provider。
