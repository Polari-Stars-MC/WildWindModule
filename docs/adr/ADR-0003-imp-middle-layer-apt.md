# ADR-0003 — 引入 imp 中间层模块（方案 B：注解处理器生成适配层）

- 状态：部分被取代（机制选择改由 [ADR-0004](./ADR-0004-source-preprocessing-and-name.md) 决定；本文的模块划分、边界与版本族设定仍有效）
- 关联：[DESIGN.md](../DESIGN.md) · [BOUNDARY.md](./BOUNDARY.md) · [ADR-0001](./ADR-0001-multi-module-jars.md)

## 背景

Minecraft 每个版本（26.1 / 26.2 / …）的 API 存在成员增删与签名变化，`core` / `wilderness` / `barren` 直接依赖 NeoForge 的类导致跨版本维护需要重复搬运。目标是引入一个中间层模块 **`imp`**：

1. 暴露给上层的 API 是 `imp` 自己的（版本无关），版本差异全部收敛在 imp 内部；
2. 用**注解标记版本存废**（field / method 粒度；参数粒度以重载模拟，见"决策"§3）；
3. 用 `gradle.properties` 的版本标量 `targetMcVersion` 选择目标版本，CI（GitHub Actions）以 matrix 各传一次标量构建，不回改文件。

## 候选方案与取舍

| 方案 | 结论 |
|---|---|
| A 源码级预处理（JavaParser 裁剪） | 可行但成本高（自研 Gradle 插件 + 调用点改写），暂缓 |
| **B APT 生成适配层** | **采用**：纯标准工具链，源码永不修改，IDE 全程可用 |
| C 字节码后处理（ASM） | 否决：参数移除需字节码级副作用分析，与 reobf/mixin 链叠加不可调试 |

选 B 的决定性理由：imp 是**我们自己控制源码**的模块，版本差异可以设计为发生在 imp 自己的 API 层（候选实现 + 分派），而不是直接继承差异化的 MC 类——这是 B 的能力边界，见"约束"§5。

## 决策

### 1. 模块与依赖结构

```
settings.gradle.kts 新增:
  imp            # 中间层，依赖：compileOnly NeoForge + compileOnly imp-annotations
  imp-annotations# 注解定义，保留策略 SOURCE，compileOnly 供下游引用

依赖方向（符合 BOUNDARY.md 单向规则）:
  core / wilderness / barren  ──api──>  imp  ──>  （NeoForge 仅在 imp 内部可见）
```

- 上层模块**禁止直接 import 任何 `net.minecraft.*` / `net.neoforged.*`**（新增到 BOUNDARY.md §1，由 CI 的 grep 检查兜底）。
- imp 的所有公开 API 不引用 MC 类型；需要 MC 类型入参的方法收编为 imp 自己的包装类型（如 `ImpPoseStack`）。

### 2. 注解集（imp-annotations）

```java
@Retention(SOURCE) @Target({FIELD, METHOD, CONSTRUCTOR, TYPE})
public @interface Since(String version);      // 该版本及以后才存在

@Retention(SOURCE) @Target({FIELD, METHOD, CONSTRUCTOR, TYPE})
public @interface Until(String version);      // 该版本起移除

@Retention(SOURCE) @Target({PARAMETER})
public @interface ParamSince(String version); // 参数差异（见 §3）
```

版本比较语义：`targetMcVersion` 与注解值按 semver 段比较；无法解析时报错而非猜测。

### 3. 版本差异的三种表达方式（按优先级）

1. **成员存在性差异** → `@Since` / `@Until` 打在 field / method 上，APT 据此生成 manifest 常量（`ImpManifest.HAS_XXX`）与适配分派。
2. **参数差异** → 不删除参数（APT 无法改既有签名），改为**重载 + 生成适配器分派**：

   ```java
   // 开发者写的（全量保留，任何版本都编译通过）
   public class WWScreen {
       public void render(ImpGuiGraphics g, int legacyFlag) { ... }
       @Since("26.2") public void render(ImpGuiGraphics g) { ... }
   }
   // APT 生成
   public final class WWScreen$Adapter {
       public static void render(WWScreen s, ImpGuiGraphics g) {
           if (ImpManifest.RENDER_NO_FLAG) s.render(g); else s.render(g, 0);
       }
   }
   ```

   旧签名在产物中残留是已知且接受的代价（见"后果"）。
3. **行为差异** → 每版本一个候选实现类，打 `@Since`/`@Until`，APT 生成工厂按 manifest 选择。

### 4. 构建与 CI

- `gradle.properties`：新增 `targetMcVersion=26.2`（默认值，本地开发用）。
- APT 生成 `ImpManifest`（`public static final boolean` 常量集），常量在编译期被 javac 内联，无运行时查找开销。
- GitHub Actions `build.yml` 改为 matrix：`strategy.matrix.mc: [26.1, 26.2]`，每轮执行 `./gradlew build -PtargetMcVersion=${{ matrix.mc }}`；**不修改 gradle.properties 文件本身**（避免 commit 污染与竞态）。
- 产物命名带版本：`ww_core-mc26.2.jar`（archivesName 或 classifier）。

### 5. 边界与约束（写入 BOUNDARY.md）

1. imp 允许引用 MC/NeoForge（适配实现必须引用），上层模块不允许——"不用 MC 一分代码"的含义收敛为"**上层不用 MC 一分代码**"。
2. imp 的公开 API 是版本无关的；imp 内部可以做版本分支（manifest / 候选类），但上层看到的签名永远稳定。
3. 差异发生在"MC 类的继承/覆写"（`extends Screen` 后父类签名变了）时，**B 方案不适用**：此类内容暂留各模块（或以 imp 包装类型重设计），这是本方案的能力天花板，记录在案。
4. imp 不向下游传递 MC 传递依赖（`api` 里不含 NeoForge）。

## 实施步骤

1. **阶段一（骨架）**：新增 `imp-annotations` / `imp` 子项目；接入 settings.gradle.kts 与根构建脚本；`core` 依赖 imp；构建与 CI matrix 跑通（此时 imp 为空壳）。
2. **阶段二（APT 最小闭环）**：实现注解处理器（读取 `@Since`/`@Until` → 生成 `ImpManifest` + 一个演示适配器）；用制箭台 GUI 的一个真实差异点做验证靶子。
3. **阶段三（迁移首个功能）**：把 core 中一处跨版本敏感调用迁入 imp，验证上层"零 MC import"约束可执行（CI grep）。
4. **阶段四（规模化）**：按需迁移；把 BOUNDARY.md 增补 imp 条目；ARCHITECTURE.md 增补依赖图。

## 实施记录（2026.11.12）

阶段一至阶段三骨架已落地，实施中确立了以下与原计划不同的细节：

1. **`imp` 自行应用 moddev 插件**：根脚本的 `allprojects` 会把 mod 流程（mods.toml 生成、runs、geckolib 依赖）套到所有非基础设施子项目；`imp` 需要 Minecraft 类但不能是 mod，因此根脚本将其排除，由 `imp/build.gradle.kts` 自行 `id("net.neoforged.moddev")` + `neoForge { version = neoVersion }`——这样 `net.minecraft.*` 才真正可用（ADR 前提）。
2. **`imp` 字节码并入 mod 产物**：`imp` 不是独立 mod 也没有 jarJar 元数据，若只做 `api(project(":imp"))`，发布后运行期必然 `NoClassDefFoundError`。现于根脚本对三个 mod 项目加 `tasks.named<Jar>("jar") { from(imp.main.output) }` 合并字节码。
3. **产物名带版本**：mod jar 改为 `ww_core-mc26.2-1.0.0.jar`，imp jar 为 `ww-imp-mc26.2-1.0.0.jar`，CI 矩阵产物可直接区分。
4. **`@ParamSince` 的 manifest 条目**：仅参数带注解的方法也要生成 `HAS_*` 常量，否则回退重载没有分派依据；已补生成。
5. **注解能力扩展**：在 `@Since`/`@Until` 上补 `until()`/`since()` 与 `exclude()`，以表达「区间存在」和「区间内特定版本缺失」（版本中途移除又恢复的情形）。
6. **处理器 source 版本**：`@SupportedSourceVersion` 写死会在更高 JDK 上编译失败，改为 `getSupportedSourceVersion()` 返回 `latestSupported()`。
7. **模板示例常驻**：`imp/sample/SampleScreen` 作为"活的文档 + 构建期自检"保留，使处理器每次构建都真实运行；真实迁移落地后可删。

## 后果
- ✅ 标准工具链，零 Gradle 插件开发；源码永不修改，IDE 体验无损。
- ✅ 每版本一轮完整构建，编译错误即版本不兼容的忠实报告。
- ✅ manifest 常量编译期内联，运行时零反射零开销。
- ⚠️ 产物含全历史签名（旧重载残留），jar 略胖，mixin refmap 需以 targetMcVersion 对应轮次的产物为准。
- ⚠️ 调用方需面向 imp 的接口/工厂编程，imp API 设计自由度受限（少用静态方法与公开构造器）。
- ⚠️ MC 类继承层面的差异无法用本方案吸收，此类代码暂不迁移。
- 📌 若未来"参数真移除"成为刚需，重启方案 A（源码裁剪插件）作为 imp 构建的增量演进，注解集可直接复用。
