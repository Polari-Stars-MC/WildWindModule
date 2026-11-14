# datagen 按版本分叉（stage E）（2026-11-13）

> 注：本机日期为 2026-11-13；CHANGE_LOG 中 11-14/15/16 的条目是此前会话的编号，未与本文件对齐。

## 背景与实测事实

datagen 无法经 imp 收敛，原因不是"写法难"，而是**基类本身跨版本不存在**。实测（在 imp 里用探针类编译三次）：

| API | 26.2 | 1.21.1 | 1.20.6 |
|---|---|---|---|
| `net.minecraft.client.data.models.ModelProvider` | ✅ | ❌ 包不存在 | ❌ 包不存在 |
| `net.minecraft.data.models.ModelProvider` | ❌ | ✅ | ✅ |

即模型 datagen 的包在旧版**整个不存在**；tags/loot/recipe/lang 虽同名存在，但构造签名不同、`extends` 的父类也不同。**类声明无法用注解表达分叉**（一个类只能继承一个父类），所以 datagen 只能按版本分目录。

## 实现

1. **datagen 独立 source set**：把 `datagen` 从 `main` 里拆出来，成为 Gradle source set。
   - 目录约定：`src/<模块>/datagen/<族>/java`，族取 `26.x` / `1.21` / `1.20`（与版本族表同名）
   - 现有 34 个 datagen 文件迁至 `src/<模块>/datagen/26.x/java/...`
2. **按族选目录**：`datagen` source set 只挂当前族的目录；旧族目录不存在 → 编译任务 `NO-SOURCE`
3. **跨模块复用**：wilderness/barren 的 datagen 依赖 core 的 Provider 工具（`MissingModels`、`BlockLoot` 等），通过 `gradle.projectsEvaluated` 延迟把 core 的 datagen 输出挂到它们的 classpath（`allprojects` 迭代顺序不保证 core 在前，不能直接取扩展）
4. **运行期接线**：`mods { register(...) { sourceSet(datagen) } }`，使 `GatherDataEvent` 能发现 datagen 类
5. **纳入 check**：`check` 依赖 `compileDatagenJava`，CI 能验证 datagen 可编译

## 踩到的坑（重要）

**LinkageError: loader constraint violation**（`net.minecraft.resources.Identifier` 同时被 `app` 与 `TRANSFORMER` 加载）。

根因：给 datagen source set 直接 `compileClasspath += main.compileClasspath` / `runtimeClasspath += main.runtimeClasspath`，把 `minecraft-patched-*.jar` 一并带进了运行期，MC 类因此出现在 app 加载器上。

修法：改为以 **compileOnly 依赖**形式继承 main 的编译类路径，运行期只加 `main.output`；MC 由 moddev 的 `mods` 块装配。

## 验证结果

| 项 | 结果 |
|---|---|
| `./gradlew clean build`（26.2） | ✅ 通过（含 `compileDatagenJava`） |
| datagen 类是否进入 mod 内容加载器 | ✅ 运行日志显示 `ww_core (composite(folder(classes/java/main), folder(resources/main), folder(classes/java/datagen)))` |
| 实际产出 | ✅ 本次运行生成 **64 个文件**（`build/generated/core/server/data`，tags + loot_table） |
| 旧族是否跳过 datagen | ✅ 目录不存在 → `NO-SOURCE`（机制确认；1.21 端到端无法验证，因为 main 在该族仍不能编译） |

## 已解决：LinkageError（根因 + 修法）

**现象**：`runServerData` 在 tags / loot 之后崩溃：

```
java.lang.LinkageError: loader constraint violation:
loader 'app' wants to load class net.minecraft.resources.Identifier.
A different class with the same name was previously loaded by 'TRANSFORMER'
  at net.minecraft.core.RegistrySetBuilder$RegistryStub.apply
```

**根因**（用 `--info` 抓运行命令确认）：`imp` 被当作**运行期库**解析（日志里 `ww-imp-mc26.2-1.0.0.jar (project :imp)`），因此落在 app 类路径上。FML 只接管 MC/NeoForge 包，**其它包一律委托父加载器（app）**，于是 `org.polaris2023.wildwind.imp.*` 由 app 加载；而 imp 的方法签名里含 `Identifier`，app 就去自己的类路径找 MC 类，撞上 TRANSFORMER 已加载的同名类。

**修法**：

- `api(project(":imp"))` → `compileOnly(project(":imp-annotations"))`
- imp 的预处理输出以**源码目录**形式并入各 mod 的 main source set：`srcDir(imp/build/preprocessed/java)`（排除 `sample`），使其类编译进模块自己的输出、由 mod 加载器加载
- 删除 jar 阶段的字节码合并（不再需要）

**验证**：`:core:runServerData` + `:core:runClientData` 全部跑完

```
Tags(block/item/entity_type) → Loot Tables → Registries → Data Maps → Recipe Provider
Model Definitions → Languages en_us / zh_cn
BUILD SUCCESSFUL
```

这一修复同时解除了"无法做 datagen 产物 diff"的阻塞，是推广 ADR-0005 各 Provider 的前提。

## 价值

拆分最大收益：**datagen 不再阻塞旧版模块构建**。此前 34 个 datagen 文件混在 main 里，意味着 1.20/1.21 族连"编译"这一步都过不去；现在 datagen 成了按需移植的增量工作，旧族可以在 datagen 尚未重写的情况下先编译主代码。
