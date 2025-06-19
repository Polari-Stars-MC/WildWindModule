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

    public static final PlanksEntry<WWRegistrate, ?> AZALEA;

    static {
        AZALEA = REGISTRATE
                .planks("azalea")
                .zhCn("杜鹃").zhTw("杜鵑").zhHk("杜鵑")
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
                .sign(() -> Blocks.MANGROVE_SIGN, () -> Blocks.MANGROVE_SIGN)
                .hangingSign(() -> Blocks.MANGROVE_HANGING_SIGN, () -> Blocks.MANGROVE_WALL_HANGING_SIGN)
                .pressurePlate(() -> Blocks.MANGROVE_PRESSURE_PLATE)
                .slab(() -> Blocks.MANGROVE_SLAB)
                .stairs(() -> Blocks.MANGROVE_STAIRS)
                ;

    }
    public static void register() {}
}
