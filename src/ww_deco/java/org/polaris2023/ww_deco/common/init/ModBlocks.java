package org.polaris2023.ww_deco.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.ww_ag.common.init.ModTabs;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_deco.WWDecoMod;

import static org.polaris2023.ww_ag.common.init.ModBlocks.b;
import static org.polaris2023.ww_deco.WWDecoMod.REGISTRATE;


/**
 * @author baka4n
 * @code @Date 2025/6/10 17:41:59
 */
public class ModBlocks {

    public static final PlanksEntry<WWRegistrate> AZALEA;

    static {
        AZALEA = REGISTRATE
                .planks("azalea")
                .planks(() -> Blocks.MANGROVE_PLANKS)
                .registerPlanks(b -> {
                    b.defaultBlockstate();
                    b.item().tab(ModTabs.BUILDING_BLOCK.key()).build();
                    b(b, l -> {
                        l.ww_ag$zh_cn("杜鹃木板");
                        l.ww_ag$zh_tw("杜鵑木板");
                        l.ww_ag$zh_hk("杜鵑木板");
                    });
                });
    }
    public static void register() {}
}
