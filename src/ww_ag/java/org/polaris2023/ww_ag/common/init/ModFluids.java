package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.world.item.BucketItem;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.polaris2023.ww_ag.common.registrate.build.ItemBuilder;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * @code @Date 2025/5/26 23:58:45
 */
public class ModFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> HONEY;
    static {

        HONEY = fluid(
                "honey",
                BaseFlowingFluid.Source::new,
                p -> p.canConvertToSource(false),
                b -> b.zh_cn("蜂蜜桶")
                        .zh_tw("蜂蜜桶")
                        .zh_hk("蜂蜜桶")
                        .lang("Honey Bucket")
        ).tag(Tags.Fluids.HONEY).register();
    }

    public static FluidBuilder<BaseFlowingFluid.Flowing, L2Registrate> fluid(String name,
                                                                             NonNullFunction<BaseFlowingFluid.Properties, BaseFlowingFluid.Source> source,
                                                                             NonNullConsumer<FluidType.Properties> fluidProperties,
                                                                             NonNullConsumer<ItemBuilder<BucketItem, FluidBuilder<BaseFlowingFluid.Flowing, L2Registrate>>> b) {

        ItemBuilder<BucketItem, FluidBuilder<BaseFlowingFluid.Flowing, L2Registrate>> bucket =
                (ItemBuilder<BucketItem, FluidBuilder<BaseFlowingFluid.Flowing, L2Registrate>>)
                        REGISTRATE.fluid(name,
                                        REGISTRATE.loc("block/" + name + "_still"),
                                        REGISTRATE.loc("block/" + name + "_flow"))
                .source(source)
                .properties(fluidProperties)
                .bucket();
        b.accept(bucket);
        return bucket.build();
    }

    public static void register() {}
}
