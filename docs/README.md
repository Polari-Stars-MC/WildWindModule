# Wild Wind 文档中心

| 文档 | 内容 | 何时读 |
|---|---|---|
| [ARCHITECTURE.md](./ARCHITECTURE.md) | 技术架构：模块结构、数据流、关键机制 | 理解代码如何组织、新增功能前 |
| [BOUNDARY.md](./BOUNDARY.md) | 模块边界与依赖规则：什么放哪、谁能依赖谁 | 跨模块改动、决定代码归属时 |
| [CHECKPOINT_RECOVERY.md](./CHECKPOINT_RECOVERY.md) | 检查点与恢复手册：中断后如何恢复 | 任务被打断、构建/datagen 失败时 |
| [ROADMAP.md](./ROADMAP.md) | 路线图与待办 | 规划下一步工作 |
| [adr/](./adr/) | 架构决策记录（ADR） | 想知道"当初为什么这么定"（imp 中间层见 ADR-0003） |
| [evidence/](./evidence/) | 验证证据存档 | 交付/审查功能改动时 |

根目录文档：

| 文档 | 内容 |
|---|---|
| [../AGENTS.md](../AGENTS.md) | AI 助手与贡献者协作规范、硬性规则 |
| [../DESIGN.md](../DESIGN.md) | 设计文档：核心决策与理由 |
| [../OPTIMIZATION.md](../OPTIMIZATION.md) | 优化记录与量化方法 |
| [../CHANGE_LOG.MD](../CHANGE_LOG.MD) | 变更日志（每次改动必更新） |

## 阅读顺序建议

新贡献者：README → DESIGN → ARCHITECTURE → BOUNDARY。
AI 助手：AGENTS.md（规则）→ CHANGE_LOG 最新段（当前状态）→ 按需查其余文档。
