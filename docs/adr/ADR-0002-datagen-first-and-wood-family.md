# ADR-0002 — datagen-first 资源策略与 WoodFamily 聚合

- 状态：已接受（2026.10.23 引入，2026.10 起全面执行）
- 背景：仓库曾混入 438 个与 datagen 产物同名的手写 JSON、130 个未注册方块的孤儿资源、26 张重复贴图；每个木种需手工维护 13~15 参数的注册列表，模型/战利品/标签各自一份，极易遗漏（活板门不掉落、误加 `needs_stone_tool` 等事故均源于此）。
- 决策：
  1. 可由 datagen 重建的资源一律生成（blockstates/models/items/lang/tags/loot/data），输出 `build/generated/<模块>/{client,server}`，手写资源白名单仅限贴图、GeckoLib json、个别模板模型。
  2. 木质内容由 `WoodFamily` 记录聚合，`ModWoodFamilies.ALL` 是唯一事实来源；所有提供器遍历一次。
  3. 燃料等静态数据走 NeoForge 数据地图（`furnace_fuels`），不用每 tick 事件。
- 后果：
  - 新增木种样板从十余处注册降为"类型 + Family + 贴图"。
  - 手写与生成同名冲突从机制上杜绝（`DuplicatesStrategy.EXCLUDE` + 禁止手写同名文件）。
  - 代价：贡献者必须会跑 `runClientData`/`runServerData`；调试资源问题要区分"生成层"与"手写层"（见 docs/CHECKPOINT_RECOVERY.md §2.1）。
- 替代方案：继续手写 JSON（被否：历史事故证明不可持续）；全部资源手写（被否：量太大）。
