package org.polaris2023.wildwind.wilderness.data;

import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpRecipeSpec;
import org.polaris2023.wildwind.wilderness.registry.ModItems;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 荒野模块的配方<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpRecipeWriter} 按版本处理。</p>
 *
 * <p><b>顺序与原实现严格一致</b>（先烤恐鱼、后蟹钳），图案字符的 define 顺序
 * 也保持一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class WildernessRecipeSpecs {

    private WildernessRecipeSpecs() {}

    /** 荒野模块的全部配方规格（按发射顺序）。 */
    public static List<ImpRecipeSpec> recipes() {
        return List.of(
            smelting(),
            omniClaw()
        );
    }

    // 生恐鱼 -> 熟恐鱼（熔炉）
    private static ImpRecipeSpec smelting() {
        ImpId piranha = ImpIds.ofHolder(ModItems.PIRANHA);
        return new ImpRecipeSpec.Smelting(
            piranha,
            ImpIds.ofHolder(ModItems.COOKED_PIRANHA),
            ImpRecipeSpec.Category.FOOD,
            ImpRecipeSpec.CookingCategory.FOOD,
            0.35F,
            200,
            "has_" + piranha.path(),
            piranha
        );
    }

    // 万用蟹钳：铜锭x6 + 蟹钳x3
    private static ImpRecipeSpec omniClaw() {
        ImpId crabClaw = ImpIds.ofHolder(ModItems.CRAB_CLAW);

        // LinkedHashMap：define 顺序参与构建器内部状态，必须保序
        Map<Character, ImpId> key = new LinkedHashMap<>();
        key.put('I', ImpId.of("minecraft", "copper_ingot"));
        key.put('C', crabClaw);

        return new ImpRecipeSpec.Shaped(
            ImpIds.ofHolder(ModItems.OMNI_CLAW),
            1,
            ImpRecipeSpec.Category.TOOLS,
            List.of("ICC", "IIC", "III"),
            key,
            // 注意：判据名取的是蟹钳、判据物品却是成品（与原实现一致，不要"顺手修正"）
            "has_" + crabClaw.path(),
            ImpIds.ofHolder(ModItems.OMNI_CLAW)
        );
    }
}
