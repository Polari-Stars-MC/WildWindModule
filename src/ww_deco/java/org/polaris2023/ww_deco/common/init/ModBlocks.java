package org.polaris2023.ww_deco.common.init;

import lombok.experimental.ExtensionMethod;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.ww_ag.common.init.ModTabs;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.RegUtil;

import static org.polaris2023.ww_deco.WWDecoMod.REGISTRATE;


/**
 * @author baka4n
 * @code @Date 2025/6/10 17:41:59
 */
@ExtensionMethod({RegUtil.class})
public class ModBlocks {

    public static final PlanksEntry<WWRegistrate> AZALEA;

    static {
        REGISTRATE.defaultCreativeTab(ModTabs.BUILDING_BLOCK.key());
        AZALEA = REGISTRATE
                .planks("azalea")
                .planks(() -> Blocks.MANGROVE_PLANKS, b -> ILanguage.convert1(b)
                        .ww_ag$zh_cn("杜鹃木板")
                        .ww_ag$zh_tw("杜鵑木板")
                        .ww_ag$zh_hk("杜鵑木板")
                        .ww_ag$self()
                        .lang("Azalea planks"))
                .strippedLog(() -> Blocks.STRIPPED_MANGROVE_LOG, b -> ILanguage
                        .convert1(b)
                        .ww_ag$zh_cn("去皮杜鹃原木")
                        .ww_ag$zh_tw("去皮杜鵑原木")
                        .ww_ag$zh_hk("去皮杜鵑原木")
                        .ww_ag$self()
                        .lang("Stripped azalea log"))
                .log(() -> Blocks.MANGROVE_LOG, b -> ILanguage.convert1(b)
                        .ww_ag$zh_cn("杜鹃原木")
                        .ww_ag$zh_tw("杜鵑原木")
                        .ww_ag$zh_hk("杜鵑原木")
                        .ww_ag$self()
                        .lang("Azalea log"))
                .strippedWood(() -> Blocks.STRIPPED_MANGROVE_WOOD,  b -> ILanguage.convert1(b)
                        .ww_ag$zh_cn("去皮杜鹃木")
                        .ww_ag$zh_tw("去皮杜鵑木")
                        .ww_ag$zh_hk("去皮杜鵑木")
                        .ww_ag$self()
                        .lang("Stripped azalea wood"))
                .wood(() -> Blocks.MANGROVE_WOOD,  b -> ILanguage.convert1(b)
                        .ww_ag$zh_cn("杜鹃木")
                        .ww_ag$zh_tw("杜鵑木")
                        .ww_ag$zh_hk("杜鵑木")
                        .ww_ag$self()
                        .lang(" Azalea wood"))
                ;
    }
    public static void register() {}
}
