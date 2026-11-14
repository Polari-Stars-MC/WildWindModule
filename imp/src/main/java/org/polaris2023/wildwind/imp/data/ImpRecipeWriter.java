package org.polaris2023.wildwind.imp.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「配方发射器」（ADR-0005）。
 *
 * <p>分工：</p>
 * <ul>
 *   <li>{@link ImpRecipeSpec} = <b>生成什么</b>（纯数据，三族共用）</li>
 *   <li>本类 = <b>怎么发射</b>（按版本分段，预处理器裁掉不适用分支，{@code @Name} 把留存段改回同名）</li>
 *   <li>{@link Sinks} = 内容模块提供的极薄适配（交出该版本的 {@code RecipeOutput}、
 *       {@code HolderGetter<Item>} 与 {@code has(...)} 判据）</li>
 * </ul>
 *
 * <p><b>现状</b>：26.x 分支完整实现并经 datagen 产物 diff 验证；旧版（1.20.6 / 1.21.1）分支
 * 已按旧版 {@code *RecipeBuilder} 的真实签名逐条实现并通过编译验证（三族 {@code :imp:build} 均成功），
 * 但旧族尚无 datagen Provider 调用本类，因此<b>还没有运行时 / 产物 diff 验证</b>——
 * 等旧版内容迁移时补，见 ADR-0005 的「实施进度」。</p>
 *
 * <p><b>旧版唯一无法等价表达的是烧炼的 {@code CookingBookCategory}</b>：旧版
 * {@code SimpleCookingRecipeBuilder.smelting} 没有该形参，配方书分类由 MC 内部按产物推导。
 * 本类不假装能控制它，而是把规格里的值与该推导结果做断言（见
 * {@code verifyLegacyCookingCategory}），不一致时显式失败而不是静默产出分类不同的 JSON。</p>
 */
public final class ImpRecipeWriter {

    private ImpRecipeWriter() {}

    /**
     * 内容模块实现的薄适配：把该版本的配方发射上下文以 {@code Object} 交出来。
     *
     * <p>三个方法都返回 {@code Object}，因为 26.x 与旧版的
     * {@code RecipeOutput} / {@code Criterion} 根本不是同一个类，
     * 无法在跨版本签名里表达（本方案唯一的类型安全妥协，见 ADR-0005）。</p>
     */
    public interface Sinks {

        /** 该版本的 {@code RecipeOutput}（通常就是 Provider 的 {@code this.output}）。 */
        Object output();

        /**
         * 该版本的 {@code HolderGetter<Item>}（通常就是 Provider 的 {@code this.items}）。
         *
         * <p>只有 26.x 用得上：旧版 {@code ShapedRecipeBuilder.shaped} 不收 {@code HolderGetter}。
         * 旧版的内容模块可以返回 {@code null}。</p>
         */
        Object items();

        /**
         * 该版本的 {@code RecipeProvider.has(ItemLike)} 判据。
         *
         * @param item 该版本的 {@code Item} 实例
         */
        Object has(Object item);
    }

    /**
     * 按规格逐条发射配方。
     *
     * <p>这里用 {@code instanceof} 链而不是 switch 模式匹配：1.20 族按较低的语言级别编译。</p>
     */
    public static void apply(List<ImpRecipeSpec> specs, Sinks sinks) {
        for (ImpRecipeSpec spec : specs) {
            if (spec instanceof ImpRecipeSpec.Shaped shaped) {
                emitShaped(shaped, sinks);
            } else if (spec instanceof ImpRecipeSpec.Smelting smelting) {
                emitSmelting(smelting, sinks);
            } else {
                throw new IllegalArgumentException("未支持的配方规格：" + spec);
            }
        }
    }

    // ── 26.x：原版新式 RecipeProvider / *RecipeBuilder ────────────────────

    @Since("26.1")
    @Name("emitShaped")
    @SuppressWarnings("unchecked")
    private static void emitShapedModern(ImpRecipeSpec.Shaped spec, Sinks sinks) {
        var items = (net.minecraft.core.HolderGetter<Item>) sinks.items();
        var category = net.minecraft.data.recipes.RecipeCategory.valueOf(spec.category().name());
        var builder = net.minecraft.data.recipes.ShapedRecipeBuilder.shaped(
            items, category, item(spec.result()), spec.count());

        // define 必须先于 pattern：构建器内部是"先建 key 再解析图案"，
        // 顺序与原实现一致，保证产物不变。
        for (var entry : spec.key().entrySet()) {
            builder.define(entry.getKey(), item(entry.getValue()));
        }
        for (String row : spec.pattern()) {
            builder.pattern(row);
        }

        builder.unlockedBy(spec.unlockName(),
            (net.minecraft.advancements.triggers.Criterion<?>) sinks.has(item(spec.unlockItem())));
        builder.save((net.minecraft.data.recipes.RecipeOutput) sinks.output());
    }

    @Since("26.1")
    @Name("emitSmelting")
    private static void emitSmeltingModern(ImpRecipeSpec.Smelting spec, Sinks sinks) {
        var category = net.minecraft.data.recipes.RecipeCategory.valueOf(spec.category().name());
        var cookingCategory =
            net.minecraft.world.item.crafting.CookingBookCategory.valueOf(spec.cookingCategory().name());
        var ingredient = net.minecraft.world.item.crafting.Ingredient.of(item(spec.ingredient()));

        var builder = net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smelting(
            ingredient, category, cookingCategory, item(spec.result()),
            spec.experience(), spec.cookingTime());

        builder.unlockedBy(spec.unlockName(),
            (net.minecraft.advancements.triggers.Criterion<?>) sinks.has(item(spec.unlockItem())));
        builder.save((net.minecraft.data.recipes.RecipeOutput) sinks.output());
    }

    // ── 1.20 / 1.21：旧版 RecipeProvider / *RecipeBuilder ─────────────────
    //
    // 签名全部用 javap 核实（neoforge-21.1.209-merged.jar 与 neoforge-20.6.144-merged.jar），
    // 两个旧版本在下面这几处**逐行同形**，因此不再切 @Since("1.21")：
    //
    //   net.minecraft.data.recipes.ShapedRecipeBuilder.shaped
    //       (RecipeCategory, ItemLike, int)                      ← 旧版没有 HolderGetter 形参
    //   .define(Character, ItemLike) / (Character, Ingredient) / (Character, TagKey<Item>)
    //   .pattern(String)                                          ← 与 26.x 同形
    //   .unlockedBy(String, Criterion<?>) .save(RecipeOutput)
    //   net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smelting
    //       (Ingredient, RecipeCategory, ItemLike, float, int)     ← 没有 CookingBookCategory 形参
    //   net.minecraft.advancements.Criterion                      ← 旧版位置（26.x 移到 advancements.triggers）
    //   net.minecraft.data.recipes.RecipeOutput.save 由 builder 内部调用
    //
    // 唯一实测差异在 RecipeProvider 本身：1.20.6 的 buildRecipes(RecipeOutput) 是 abstract，
    // 1.21.1 改成非 abstract 且新增 (RecipeOutput, HolderLookup.Provider) 重载。
    // 那是<b>内容模块 Provider 的覆写形态</b>，本发射器不涉及，故此处无 1.20/1.21 分叉。

    @Until("26.1")
    @Name("emitShaped")
    @SuppressWarnings("unchecked")
    private static void emitShapedLegacy(ImpRecipeSpec.Shaped spec, Sinks sinks) {
        var category = net.minecraft.data.recipes.RecipeCategory.valueOf(spec.category().name());
        // 旧版签名是 (RecipeCategory, ItemLike, int)：不需要 HolderGetter，Sinks#items 在此用不到
        var builder = net.minecraft.data.recipes.ShapedRecipeBuilder.shaped(
            category, item(spec.result()), spec.count());

        // define 先于 pattern：与 26.x 分支、与原手写实现对顺序的要求一致
        for (var entry : spec.key().entrySet()) {
            builder.define(entry.getKey(), item(entry.getValue()));
        }
        for (String row : spec.pattern()) {
            builder.pattern(row);
        }

        builder.unlockedBy(spec.unlockName(),
            (net.minecraft.advancements.Criterion<?>) sinks.has(item(spec.unlockItem())));
        builder.save((net.minecraft.data.recipes.RecipeOutput) sinks.output());
    }

    @Until("26.1")
    @Name("emitSmelting")
    @SuppressWarnings("unchecked")
    private static void emitSmeltingLegacy(ImpRecipeSpec.Smelting spec, Sinks sinks) {
        var category = net.minecraft.data.recipes.RecipeCategory.valueOf(spec.category().name());
        var ingredient = net.minecraft.world.item.crafting.Ingredient.of(item(spec.ingredient()));
        Item result = item(spec.result());

        // 旧版 API 没有 CookingBookCategory 形参（见上方 1.20/1.21 说明），
        // 分类由 MC 的 SimpleCookingRecipeBuilder#determineSmeltingRecipeCategory 推导。
        // 这里断言"规格想要的"与"旧版会产出的"一致：不一致说明该规格在旧版根本无法表达，
        // 宁可显式失败，也不静默产出配方书分类不同的 JSON（那正是产物 diff 才能抓到的东西）。
        verifyLegacyCookingCategory(spec, result);

        var builder = net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smelting(
            ingredient, category, result, spec.experience(), spec.cookingTime());

        builder.unlockedBy(spec.unlockName(),
            (net.minecraft.advancements.Criterion<?>) sinks.has(item(spec.unlockItem())));
        builder.save((net.minecraft.data.recipes.RecipeOutput) sinks.output());
    }

    /**
     * 核对旧版会推导出的 {@code CookingBookCategory} 是否与规格一致。
     *
     * <p>推导逻辑逐行对应 1.20.6 / 1.21.1 的
     * {@code SimpleCookingRecipeBuilder.determineSmeltingRecipeCategory(ItemLike)}：</p>
     * <pre>
     * if (item.components().has(DataComponents.FOOD)) return FOOD;
     * return item instanceof BlockItem ? BLOCKS : MISC;
     * </pre>
     * <p>两个旧版 jar 的该方法源码逐字节相同，故本方法不需要版本分叉。</p>
     *
     * @throws IllegalStateException 规格显式要求的分类与旧版推导结果不符时
     */
    @Until("26.1")
    private static void verifyLegacyCookingCategory(ImpRecipeSpec.Smelting spec, Item result) {
        var derived = result.components().has(net.minecraft.core.component.DataComponents.FOOD)
            ? net.minecraft.world.item.crafting.CookingBookCategory.FOOD
            : result instanceof net.minecraft.world.item.BlockItem
                ? net.minecraft.world.item.crafting.CookingBookCategory.BLOCKS
                : net.minecraft.world.item.crafting.CookingBookCategory.MISC;
        var expected = net.minecraft.world.item.crafting.CookingBookCategory.valueOf(
            spec.cookingCategory().name());
        if (derived != expected) {
            throw new IllegalStateException(
                "旧版（1.20.6 / 1.21.1）无法表达该烧炼配方的配方书分类："
                    + "SimpleCookingRecipeBuilder.smelting 没有 CookingBookCategory 形参，"
                    + "分类只能由 MC 按产物推导（food 组件 → FOOD，BlockItem → BLOCKS，否则 MISC）。"
                    + "规格要求 " + expected + "，而产物 " + result + " 会被推导为 " + derived
                    + "。请改规格的 cookingCategory，或为旧版换一个可控制的发射方式。");
        }
    }

    // ── 注册表解析（两代 API 的名称差异在这里收敛）────────────────────────

    @Since("26.1")
    @Name("item")
    private static Item itemModern(ImpId id) {
        return BuiltInRegistries.ITEM.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("item")
    private static Item itemLegacy(ImpId id) {
        return BuiltInRegistries.ITEM.get(id.unwrap());
    }
}
