package org.polaris2023.ww_deco.common.init;

import lombok.experimental.ExtensionMethod;
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
            AZALEA, BAOBAB;

    static {
        AZALEA = REGISTRATE
                .planks("azalea")
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
                .stairs(() -> Blocks.MANGROVE_STAIRS);
        BAOBAB = REGISTRATE
                .planks("baobab")
                .zhCn("猴面包树").zhTw("猴麵包樹").zhHk("猴麵包樹")
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
                .stairs(() -> Blocks.ACACIA_STAIRS);

    }
    public static void register() {}
}
