package org.polaris2023.wildwind.wilderness.data;

import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpModelSpec;
import org.polaris2023.wildwind.wilderness.block.WildernessBlocks;

import java.util.List;

/**
 * 荒野模块的模型<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpModelWriter} 按版本处理。</p>
 *
 * <p>本类只承载"无贴图方块"这一部分（原先借用 core datagen 的 {@code MissingModels}）。
 * 借用的代价是运行期 {@code NoClassDefFoundError}：core 的 datagen 类不进 jar，
 * 而 wilderness 的 datagen 运行时加载的是 core 的 <b>jar</b>。改成规格后不再跨模块依赖。</p>
 *
 * <p>顺序与原实现严格一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class WildernessModelSpecs {

    private WildernessModelSpecs() {}

    /** 沼泽方块：暂无贴图，统一使用原版黑紫缺失纹理。 */
    public static List<ImpModelSpec> models() {
        return List.of(
            new ImpModelSpec.Cube(ImpIds.ofHolder(WildernessBlocks.BOG_MUD)),
            new ImpModelSpec.Cube(ImpIds.ofHolder(WildernessBlocks.REMAINS)),
            new ImpModelSpec.Cross(ImpIds.ofHolder(WildernessBlocks.REEDS)),
            new ImpModelSpec.Cross(ImpIds.ofHolder(WildernessBlocks.CATTAILS))
        );
    }
}
