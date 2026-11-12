# Wild Wind Modules 原野之风模块包

NeoForge 多模块 Minecraft 模组（Minecraft 26.2 · NeoForge 26.2.0.75 · Java 25），拆分为 `core` / `wilderness` / `barren` 三个独立 jar。

## 文档导航

| 文档 | 内容 |
|:-----|:-----|
| [AGENTS.md](./AGENTS.md) | AI 助手与贡献者协作规范、硬性规则、常用命令 |
| [DESIGN.md](./DESIGN.md) | 设计文档：核心决策与理由 |
| [OPTIMIZATION.md](./OPTIMIZATION.md) | 优化记录与量化方法 |
| [CHANGE_LOG.MD](./CHANGE_LOG.MD) | 变更日志（每次改动必更新） |
| [docs/ARCHITECTURE.md](./docs/ARCHITECTURE.md) | 技术架构与数据流 |
| [docs/BOUNDARY.md](./docs/BOUNDARY.md) | 模块边界与依赖规则 |
| [docs/CHECKPOINT_RECOVERY.md](./docs/CHECKPOINT_RECOVERY.md) | 中断恢复手册 |
| [docs/ROADMAP.md](./docs/ROADMAP.md) | 路线图与待办 |
| [docs/adr/](./docs/adr/) | 架构决策记录（ADR） |
| [docs/evidence/](./docs/evidence/) | 验证证据存档 |

### 快速开始

```bash
./gradlew build                  # 构建三个 jar
./gradlew :core:runClient        # 只跑 core
./gradlew :barren:runClient      # 跑 barren（自动额外加载 wilderness 联调）
./gradlew :<模块>:runClientData  # 生成客户端资源（模型/blockstate/lang）
./gradlew :<模块>:runServerData  # 生成服务端数据（loot/tags/recipe）
```

> 重要约定：datagen 可生成的资源一律不手写；新增木质内容走 `WoodFamily` 聚合。详见 [AGENTS.md](./AGENTS.md)。

### Module List 模块列表

| name |         note         | ch name |  note   |
|:----:|:--------------------:|:-------:|:-------:|
| core |   Wild Wind: Core    |  核心   | 原野之风：核心 |
| wilderness | Wild Wind: Wilderness Update | 荒野更新 | 原野之风：荒野更新 |
| barren | Wild Wind: Barren Update | 瘠土更新 | 原野之风：瘠土更新 |

模块 `modid` 由 `ww_<模块名>` 生成：`ww_core` / `ww_wilderness` / `ww_barren`。
`wilderness` 与 `barren` 在 `neoforge.mods.toml` 中声明依赖 `ww_core`，构建上通过 `implementation project(":core")` 关联。

### 内容归属

| 模块 | 内容 |
|:----:|:-----|
| core（0） | 杜鹃木套件、玫瑰 / 灵魂玫瑰（丛）、制箭台与矢术、幼蛛 |
| wilderness（1） | 泥沼蟹、恐鱼、蟹钳 / 尖牙 / 万用蟹钳、延展药水与状态效果、灵魂南瓜灯 |
| barren（2） | 焚烬木 / 灵焰木套件、石英砂岩 / 灵魂砂岩全套、石英沙与流沙、焦土、仙人球、可疑沙 |

[CHANGES](./CHANGE_LOG.MD)