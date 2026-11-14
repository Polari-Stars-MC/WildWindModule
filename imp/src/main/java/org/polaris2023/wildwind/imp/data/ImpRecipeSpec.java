package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 版本无关的「配方规格」（ADR-0005）。
 *
 * <p>datagen 的知识分两层：<b>生成什么</b>（哪些配方、什么形状、什么材料）
 * 与 <b>怎么发射</b>（该版本的 {@code RecipeProvider} / {@code *RecipeBuilder} API）。
 * 前者是纯数据，可跨版本共用；后者因基类与构造器签名不同必须按版本分叉。</p>
 *
 * <p>本类承载前者：只用 {@link ImpId} 描述物品与标签，不出现任何 MC 类型，
 * 因此三个版本族的 datagen 可以消费同一份规格。</p>
 *
 * <p><b>变体只覆盖仓库里真实用到的形状</b>（见各模块的 {@code *RecipeSpecs}）：
 * 工作台有序合成（{@link Shaped}）与熔炉烧炼（{@link Smelting}）。
 * 无序合成、切石机、锻造台等形状目前没有 datagen 用例，故刻意不预设变体——
 * 需要时再加，避免交付"从未被真实产出验证过"的规格形状。</p>
 */
public sealed interface ImpRecipeSpec {

    /** 该配方所属的配方书分类，名字与 MC 的 {@code RecipeCategory} 枚举一一对应。 */
    Category category();

    /**
     * 配方书分类，取值与 MC 的 {@code RecipeCategory} 同名，发射器按名字映射回该版本的枚举。
     *
     * <p>目前实际用到的只有 {@link #TOOLS}（蟹钳）与 {@link #FOOD}（烤恐鱼）。</p>
     */
    enum Category {
        BUILDING_BLOCKS,
        DECORATIONS,
        REDSTONE,
        TRANSPORTATION,
        TOOLS,
        COMBAT,
        FOOD,
        BREWING,
        MISC
    }

    /** 烧炼类配方的配方书分类，名字与 MC 的 {@code CookingBookCategory} 一一对应。 */
    enum CookingCategory {
        FOOD,
        BLOCKS,
        MISC
    }

    /**
     * 工作台有序合成（{@code minecraft:crafting_shaped}）。
     *
     * @param result     产物物品 id
     * @param count      产物数量
     * @param category   配方书分类
     * @param pattern    图案，每一行一个字符串，各行等宽
     * @param key        图案字符 → 物品 id（<b>按插入顺序</b>保留，与原始 define 顺序一致）
     * @param unlockName 解锁判据名，如 {@code has_piranha}
     * @param unlockItem 解锁判据所观察的物品 id（{@code has(item)} 的实参）
     */
    record Shaped(
        ImpId result,
        int count,
        Category category,
        List<String> pattern,
        Map<Character, ImpId> key,
        String unlockName,
        ImpId unlockItem
    ) implements ImpRecipeSpec {

        public Shaped {
            Objects.requireNonNull(result, "result");
            Objects.requireNonNull(category, "category");
            Objects.requireNonNull(unlockName, "unlockName");
            Objects.requireNonNull(unlockItem, "unlockItem");
            pattern = List.copyOf(pattern);
            // 不能用 Map.copyOf：它不保证顺序，而 define 顺序会影响构建器内部状态，
            // 因此改用 LinkedHashMap 保序后再包一层只读视图。
            key = Collections.unmodifiableMap(new LinkedHashMap<>(key));
            if (pattern.isEmpty()) {
                throw new IllegalArgumentException("有序合成的图案不能为空");
            }
            int width = pattern.get(0).length();
            for (String row : pattern) {
                if (row.length() != width) {
                    throw new IllegalArgumentException("有序合成的每行必须等宽：" + pattern);
                }
            }
        }
    }

    /**
     * 熔炉烧炼（{@code minecraft:smelting}）。
     *
     * @param ingredient      输入物品 id
     * @param result          产物物品 id
     * @param category        配方书分类（工作台侧）
     * @param cookingCategory 配方书分类（烧炼侧）
     * @param experience      经验值
     * @param cookingTime     烧炼时间（tick）
     * @param unlockName      解锁判据名
     * @param unlockItem      解锁判据所观察的物品 id
     */
    record Smelting(
        ImpId ingredient,
        ImpId result,
        Category category,
        CookingCategory cookingCategory,
        float experience,
        int cookingTime,
        String unlockName,
        ImpId unlockItem
    ) implements ImpRecipeSpec {

        public Smelting {
            Objects.requireNonNull(ingredient, "ingredient");
            Objects.requireNonNull(result, "result");
            Objects.requireNonNull(category, "category");
            Objects.requireNonNull(cookingCategory, "cookingCategory");
            Objects.requireNonNull(unlockName, "unlockName");
            Objects.requireNonNull(unlockItem, "unlockItem");
        }
    }
}
