package org.polaris2023.ww_deco.common.init;

import lombok.experimental.ExtensionMethod;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.ww_ag.common.init.ModTabs;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.RegUtil;

import static org.polaris2023.ww_deco.WWDecoMod.REGISTRATE;


/**
 * @author baka4n
 * @code @Date 2025/6/10 17:41:59
 */
@ExtensionMethod({RegUtil.class})
public class ModBlocks {

    public static final PlanksEntry<WWRegistrate, ?>
            AZALEA, BAOBAB, PALM;

    static {
        AZALEA = REGISTRATE
                .planks("azalea", () -> ModEnumExtensions.AZALEA)
                .zhCn("杜鹃树").zhTw("杜鵑樹").zhHk("杜鵑樹")
                .defTag(ModTabs.BUILDING_BLOCK.key())
                .planks(() -> Blocks.MANGROVE_PLANKS)
                .strippedLog(() -> Blocks.STRIPPED_MANGROVE_LOG)
                .log(() -> Blocks.MANGROVE_LOG)
                .strippedWood(() -> Blocks.STRIPPED_MANGROVE_WOOD)
                .wood(() -> Blocks.MANGROVE_WOOD)
                .button(() -> Blocks.MANGROVE_BUTTON)
                .door(() -> Blocks.MANGROVE_DOOR)
                .trapDoor(() -> Blocks.MANGROVE_TRAPDOOR)
                .fence(() -> Blocks.MANGROVE_FENCE)
                .fenceGate(() -> Blocks.MANGROVE_FENCE_GATE)
                .sign(() -> Blocks.MANGROVE_SIGN, () -> Blocks.MANGROVE_WALL_SIGN)
                .hangingSign(() -> Blocks.MANGROVE_HANGING_SIGN, () -> Blocks.MANGROVE_WALL_HANGING_SIGN)
                .pressurePlate(() -> Blocks.MANGROVE_PRESSURE_PLATE)
                .slab(() -> Blocks.MANGROVE_SLAB)
                .stairs(() -> Blocks.MANGROVE_STAIRS)
                .defTag(ModTabs.TOOLS_AND_UTILITIES.key())
                .boat()
                .chestBoat()
                .register();
        BAOBAB = REGISTRATE
                .planks("baobab", () -> ModEnumExtensions.BAOBAB)
                .zhCn("猴面包树").zhTw("猴麵包樹").zhHk("猴麵包樹")
                .defTag(ModTabs.BUILDING_BLOCK.key())
                .planks(() -> Blocks.ACACIA_PLANKS)
                .strippedLog(() -> Blocks.STRIPPED_ACACIA_LOG)
                .log(() -> Blocks.ACACIA_LOG)
                .strippedWood(() -> Blocks.STRIPPED_ACACIA_WOOD)
                .wood(() -> Blocks.ACACIA_WOOD)
                .leaves(() -> Blocks.ACACIA_LEAVES)
                .button(() -> Blocks.ACACIA_BUTTON)
                .door(() -> Blocks.ACACIA_DOOR)
                .trapDoor(() -> Blocks.ACACIA_TRAPDOOR)
                .fence(() -> Blocks.ACACIA_FENCE)
                .fenceGate(() -> Blocks.ACACIA_FENCE_GATE)
                .sign(() -> Blocks.ACACIA_SIGN, () -> Blocks.ACACIA_WALL_SIGN)
                .hangingSign(() -> Blocks.ACACIA_HANGING_SIGN, () -> Blocks.ACACIA_WALL_HANGING_SIGN)
                .pressurePlate(() -> Blocks.ACACIA_PRESSURE_PLATE)
                .slab(() -> Blocks.ACACIA_SLAB)
                .stairs(() -> Blocks.ACACIA_STAIRS)
                .defTag(ModTabs.TOOLS_AND_UTILITIES.key())
                .boat()
                .chestBoat()
                .register();
        PALM = REGISTRATE
                .planks("palm", () -> ModEnumExtensions.PALM)
                .zhCn("棕桐").zhTw("棕桐").zhHk("棕桐")
                .defTag(ModTabs.BUILDING_BLOCK.key())
                .planks(() -> Blocks.SPRUCE_PLANKS)
                .strippedLog(() -> Blocks.STRIPPED_SPRUCE_LOG)
                .log(() -> Blocks.SPRUCE_LOG)
                .strippedWood(() -> Blocks.STRIPPED_SPRUCE_WOOD)
                .wood(() -> Blocks.SPRUCE_WOOD)
                .leaves(() -> Blocks.SPRUCE_LEAVES)
                .button(() -> Blocks.SPRUCE_BUTTON)
                .door(() -> Blocks.SPRUCE_DOOR)
                .trapDoor(() -> Blocks.SPRUCE_TRAPDOOR)
                .fence(() -> Blocks.SPRUCE_FENCE)
                .fenceGate(() -> Blocks.SPRUCE_FENCE_GATE)
                .sign(() -> Blocks.SPRUCE_SIGN, () -> Blocks.SPRUCE_WALL_SIGN)
                .hangingSign(() -> Blocks.SPRUCE_HANGING_SIGN, () -> Blocks.SPRUCE_WOOD)
                .slab(() -> Blocks.SPRUCE_SLAB)
                .stairs(() -> Blocks.SPRUCE_STAIRS)
                .defTag(ModTabs.TOOLS_AND_UTILITIES.key())
                .boat()
                .chestBoat()
                .crown(() -> Blocks.SPRUCE_LOG)
                .register()
        
        ;
    }
    public static void register() {}
}
