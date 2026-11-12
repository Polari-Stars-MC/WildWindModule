# CHECKPOINT_RECOVERY.md — 检查点与恢复手册

当任务被中断（会话结束、构建失败、datagen 半途、资源损坏）时，按本文恢复。核心原则：**先定位最后一致状态，再最小步前进，每步验证。**

## 1. 状态快照点

| 检查点 | 判定方法 | 恢复动作 |
|---|---|---|
| 代码可编译 | `./gradlew build` 全绿 | 失败时看首个编译错误，通常在最近改动文件 |
| datagen 一致 | `runClientData`/`runServerData` 重跑两遍，第二遍后 `build/generated/<模块>/` 与第一遍快照无 diff（generated 不入库，见 BOUNDARY.md §5） | 有 diff → 生成不确定，检查 Provider 是否引用了随机/时间因素；revert 不该变的文件 |
| 客户端可启动 | `runClient` 进主世界，无 crash report | crash 看 `run/…/crash-reports` 或日志，注册类错误先查 mixins.json 与 mods.toml |
| 资源完整 | 游戏内无 missingno、无 TagLoader 报错 | TagLoader 报缺失引用 → 找标签里未注册的 id（教训 BUG-TG-001） |

## 2. 常见中断场景与恢复步骤

### 2.1 datagen 中途失败

1. datagen 失败不会写坏 `build/generated`（HashCache 按文件原子写入），直接修复失败原因重跑即可。
2. 若错误是 `IdentifierException: Non [a-z0-9/._-] character` → 搜代码里的**大写资源 id**（曾出现 `stripped_CINDER_log`），改全小写后重跑；旧产物清空 `build/generated/<模块>` 重建。
3. 若怀疑 `.cache` 污染（生成结果不更新）：确认 `src/**/.cache` 不存在（绝不能提交进 src），必要时删除 `build/generated` 全量重跑。

### 2.2 修改到一半的迁移（如改名木材 id）

以 2026.11.10 木材改名（blaze/soul → cinder/ember）为模板：

1. 单模块内一次改完：方块 → 物品 → 实体 → WoodType → 贴图/模型引用 → 配方 → 标签 → lang。
2. 用 `grep -ri "旧id"` 全仓确认无残留；`grep` 大写字母模式 `\b[A-Z]` 检查资源 id。
3. 重跑两套 datagen，验证 `build/generated` 全量重建。
4. CHANGE_LOG 记录改名映射表。

### 2.3 会话中断（AI 助手恢复工作）

1. 读 `CHANGE_LOG.MD` 最新 Unreleased 段 → 了解进行中的改动。
2. `git status` + `git diff` → 确认工作区改动是否对应日志描述；不对应时先询问用户，不要擅自 revert。
3. 按本文件 §2.1/2.2 的检查点逐步验证后继续。
4. 恢复后每完成一个原子步骤：构建验证 → CHANGE_LOG 补条目 → 继续下一步。

### 2.4 运行时/存档损坏

- 开发用运行目录 `run/`（未提交）与 `build/runs/` 均可随时删除重建：`./gradlew :<模块>:runClient` 会重新生成。
- 旧存档兼容问题（如 `ArrowShaft.fromName` 报 `Unknown arrow shaft`）→ 在编解码入口补旧值映射，禁止要求玩家删档。

### 2.5 Gradle/环境损坏

1. `./gradlew --stop` 杀 daemon；删除 `.gradle/`（项目内缓存，仅本地）。
2. configuration cache 异常 → `./gradlew build --no-configuration-cache` 排查，正常后按报告 `build/reports/configuration-cache` 修复。
3. Gradle 9 资源重复报错 → 确认 `duplicatesStrategy = EXCLUDE` 仍在 build.gradle.kts，检查是否新增了同名 srcDir。

## 3. 恢复后的强制动作

- [ ] `./gradlew build` 通过
- [ ] 两套 datagen 重跑后 diff 干净
- [ ] CHANGE_LOG 已补记录
- [ ] 重大偏离在 `docs/evidence/` 留验证记录
