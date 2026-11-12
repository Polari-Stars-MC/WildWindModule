# ADR-0001 — 拆分为三个独立模块 jar

- 状态：已接受（2026.11.02）
- 背景：core / wilderness / barren 最初共用一个 jar（含更早的 ww_hfas 单体时期），内容更新耦合在一个版本线上，玩家无法按需安装，创造栏拥挤。
- 决策：一个 Gradle root（`settings.gradle.kts` include 三个子项目，projectDir 指向 `modules/<名>`），三份源码放 `src/<模块>`，各自产出独立 jar；`wilderness`/`barren` 在生成的 `neoforge.mods.toml` 中声明对 `ww_core` 的 required/AFTER 依赖，Gradle 侧 `implementation project(":core")`；barren 开发期以 `localRuntime` 额外加载 wilderness。
- 后果：
  - 每模块独立版本与创造栏页；只装一个模块时只出现它自己的页。
  - 跨模块共享代码必须下沉 core（见 docs/BOUNDARY.md）。
  - 构建配置集中在根 `build.gradle.kts` 的 `allprojects`，模块级差异用 `if (project.name == ...)` 表达。
- 替代方案：单 jar 多内容（被否：无法独立发布）；多仓库（被否：共享聚合层需要跨仓同步）。
