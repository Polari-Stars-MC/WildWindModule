# 验证证据（Evidence）

功能与修复的验证记录存档。**原则：没有证据的"已完成"不算完成。**

## 目录约定

```
evidence/
└── YYYY-MM-DD-<主题>/
    ├── README.md     # 验证说明：改了什么、验证步骤、观察结果、结论
    └── *.png|log|txt # 截图、日志摘录、命令输出
```

## README.md 模板

```markdown
# <标题>（YYYY-MM-DD）

## 变更摘要
- 对应 CHANGE_LOG 条目 / PR：

## 验证步骤
1. `./gradlew build` — 结果：
2. `:模块:runClientData` / `runServerData` — diff 结论：
3. 游戏内验证（世界/命令/观察点）：

## 结果
- 期望 vs 实际：
- 截图 / 日志：见本目录文件

## 结论
（通过 / 有遗留问题，遗留项进 ROADMAP）
```

## 记录清单

（暂无条目。首个条目建议从最近一次 CHANGE_LOG 的 Fixed 项补起，例如制箭台 ResultSlot 取半组修复。）
