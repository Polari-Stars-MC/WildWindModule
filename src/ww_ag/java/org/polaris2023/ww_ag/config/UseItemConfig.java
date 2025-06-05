package org.polaris2023.ww_ag.config;

import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * @author baka4n
 * @code @Date 2025/6/4 13:06:37
 */

public class UseItemConfig extends ConfigInit {
    public final ModConfigSpec.BooleanValue
            popped_chorus_fruit,
            glistering_melon_slice,
            fish_bone_loot,
            milk_block_use;
    public UseItemConfig(ConfigInit.Builder builder) {
        folder("wild_wind/");

        popped_chorus_fruit = builder.text("Popped chorus fruit (default true)").define("popped_chorus_fruit", true);
        glistering_melon_slice = builder.text("Glistering melon slice (default true)").define("glistering_melon_slice", true);
        fish_bone_loot = builder.text("Fish bone loot (default true)").define("fish_bone_loot", true);
        milk_block_use = builder.text("Milk block use (default true)").define("milk_block_use", true);
    }
}
