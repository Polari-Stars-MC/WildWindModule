# 2026-10-24 上游同步记录

## 基线与提交历史

- 来源：`cuccuc377-dev/WildWindMod`，`main`，`eb12f4fed5472c9be5c51606141fff201b873968`。
- 目标：`Polari-Stars-MC/WildWindModule`，`master/NeoForge-26.2`，`85926800c060020dd9afbf3c4a8ab90a2540421c`。
- 集成分支：`sync/wild-wind-modules-20261024`。
- 目标基线是此前已整合的上游 `26ad2da288d7018a1eb39d5bfbf30b2ad0a1520c` 的祖先；此前本地整合基线为 `39e327eca50f258c645097332d76319c9553e8d4`。

来源仓库以独立源码快照开始，没有与目标共享 Git 祖先。此次在目标基线上建立分支，用双亲合并保留目标历史及来源仓库历史；整合树以已包含上游修复的来源版本为基础，再补充同步文档、贡献流程和 CI 加固。没有重写目标分支历史。2026-10-22 的整合范围保留在 [历史记录](UPSTREAM_MERGE.md)，快照来源与署名见 [来源说明](PROVENANCE.md)。

## 交付范围

发行物为三个模组：`wild_wind_core`、`wild_wind_wilderness`、`wild_wind_barren`。荒野与瘠土分别依赖同版本核心；只有荒野需要 GeckoLib。模块归属、功能及命令以 [README](../README.md) 和 [项目进度](../PROGRESS.md) 为准。

此次包含此前完成的功能与测试、直接署名的属性书、附魔台熔炼附魔、保持贴图原色的树叶；移除未完成的自定义部件制箭系统，保留已有延展药水箭。保留许可证原文、上游作者与资源素材。

## 兼容性与升级

这是注册命名空间发生变化的更新，不能视为旧世界的无损替换：

1. 先备份实例与世界，在新的测试世界验证所需安装组合。
2. 移除旧 `ww_hfas` 和独立 `ww_structures` 模组文件，安装核心及需要的扩展。客户端、服务端组合一致。
3. 旧 `ww_hfas:*` 方块、物品、实体没有自动迁移；原有世界内容、NBT 模板和数据包引用需要按新命名空间转换。恐鱼仍使用 `piranha` 路径。
4. `config/ww_structures/`、结构资源命名空间和坐标索引保留，但模板中的旧方块 ID 仍需更新。
5. 删除的自定义制箭台没有自动替换恢复逻辑。回退时恢复备份世界及对应旧 JAR，避免继续使用已由新版本保存的世界。

## 验证与审查

本次提交前执行以下检查；PR 中记录实际结果，GitHub Actions 对合并结果再次执行相同验证：

```powershell
.\gradlew.bat :datagen:runClientData :datagen:runServerData
python scripts/sync_generated_resources.py
git diff --exit-code -- src/core/resources src/wilderness/resources src/barren/resources
.\gradlew.bat build collectMods :verification:runGameTest
.\gradlew.bat :smoke:runGameTest -PtestModules=core
.\gradlew.bat :smoke:runGameTest -PtestModules=core,wilderness
.\gradlew.bat :smoke:runGameTest -PtestModules=core,barren
.\gradlew.bat :smoke:runGameTest -PtestModules=core,wilderness,barren
git diff --check
```

发行集合仅包含三个模组 JAR，不包含测试模组、IDE 设置、本地工具、构建缓存或测试世界。CI 使用只读令牌权限、固定 Actions 提交、超时及重复任务取消，并保留测试报告。

2026-10-24 本地验证：两类数据生成与快照一致性检查、完整构建、12 项 JUnit、28 项综合 GameTest 和四种安装组合测试均通过；三个发行 JAR 的许可元数据与资源排除检查通过。

服务端与数据生成自动检查不等于人工视觉验收、长期游玩或多人兼容性验证。目标分支要求 PR 与至少一位审核者批准；必须等待审查，不绕过保护规则。

已有 PR #33（`26.2-fix` → `master/NeoForge-26.2`）尚未合并，其头提交 `63b06e313b4dcff28b53593a0e99ae7e1335bdf0` 比本次已整合的 `26ad2da` 多 8 个提交，涉及木材生成器整理、标签／配方及恐鱼音效修复。本次不将该待审分支视为已合并内容，也不修改它；审查者应协调两个 PR 的集成顺序，并重新验证重叠部分。
