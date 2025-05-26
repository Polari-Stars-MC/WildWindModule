package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * @code @Date 2025/5/26 23:58:45
 */
public class ModFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> HONEY;
    static {
        HONEY = REGISTRATE
                .fluid(
                        "honey",
                        REGISTRATE.loc("block/honey_still"),
                        REGISTRATE.loc("block/honey_flow")
                )
                .tag(Tags.Fluids.HONEY)
                .register();
    }
    public static void register() {}
}
