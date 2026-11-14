package org.polaris2023.wildwind.core.data;

import org.polaris2023.wildwind.imp.data.ImpRecipeSpec;

import java.util.List;

/**
 * 核心模块的配方<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpRecipeWriter} 按版本处理。</p>
 *
 * <p><b>当前为空</b>：core 的箭矢组合配方尚未启用（与迁移前的
 * {@code ModRecipeProvider.buildRecipes()} 空实现一致）。这里保留入口是为了让
 * Provider 的薄适配形态与其它模块统一，将来加配方只需往本方法里加规格。</p>
 */
public final class ModRecipeSpecs {

    private ModRecipeSpecs() {}

    /** 核心模块的全部配方规格（按发射顺序）。 */
    public static List<ImpRecipeSpec> recipes() {
        return List.of();
    }
}
