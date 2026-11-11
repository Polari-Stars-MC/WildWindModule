package org.polaris2023.wildwind.core.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.core.registry.ModItems;

/**
 * 核心模块的简体中文语言文件。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class ZhCnLanguageProvider extends LanguageProvider {

    public ZhCnLanguageProvider(PackOutput output) {
        super(output, WildWindCore.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // 杜鹃木套件
        addBlock(ModBlocks.AZALEA_LOG, "杜鹃木原木");
        addBlock(ModBlocks.AZALEA_WOOD, "杜鹃木");
        addBlock(ModBlocks.STRIPPED_AZALEA_LOG, "去皮杜鹃木原木");
        addBlock(ModBlocks.STRIPPED_AZALEA_WOOD, "去皮杜鹃木");
        addBlock(ModBlocks.AZALEA_PLANKS, "杜鹃木木板");
        addBlock(ModBlocks.AZALEA_STAIRS, "杜鹃木楼梯");
        addBlock(ModBlocks.AZALEA_SLAB, "杜鹃木台阶");
        addBlock(ModBlocks.AZALEA_FENCE, "杜鹃木栅栏");
        addBlock(ModBlocks.AZALEA_FENCE_GATE, "杜鹃木栅栏门");
        addBlock(ModBlocks.AZALEA_DOOR, "杜鹃木门");
        addBlock(ModBlocks.AZALEA_TRAPDOOR, "杜鹃木活板门");
        addBlock(ModBlocks.AZALEA_PRESSURE_PLATE, "杜鹃木压力板");
        addBlock(ModBlocks.AZALEA_BUTTON, "杜鹃木按钮");
        addBlock(ModBlocks.AZALEA_SHELF, "杜鹃木展示架");
        addBlock(ModBlocks.AZALEA_SIGN, "杜鹃木告示牌");
        addBlock(ModBlocks.AZALEA_WALL_SIGN, "杜鹃木墙式告示牌");
        addBlock(ModBlocks.AZALEA_HANGING_SIGN, "杜鹃木悬挂告示牌");
        addBlock(ModBlocks.AZALEA_WALL_HANGING_SIGN, "杜鹃木墙式悬挂告示牌");
        addItem(ModItems.AZALEA_BOAT, "杜鹃木船");
        addItem(ModItems.AZALEA_CHEST_BOAT, "杜鹃木运输船");
        addEntityType(ModEntities.AZALEA_BOAT, "杜鹃木船");
        addEntityType(ModEntities.AZALEA_CHEST_BOAT, "杜鹃木运输船");

        // 其他方块
        addBlock(ModBlocks.FLETCHING_TABLE, "制箭台");
        addBlock(ModBlocks.ROSE, "玫瑰");
        addBlock(ModBlocks.SOUL_ROSE, "灵魂玫瑰");
        addBlock(ModBlocks.SOUL_ROSE_BUSH, "灵魂玫瑰丛");
        addBlock(ModBlocks.WITHER_ROSE_BUSH, "凋零玫瑰丛");
        addBlock(ModBlocks.KILN, "窑炉");

        // GUI
        add("container.fletching_table", "制作箭");
        add("tab.ww_core.name", "原野之风");
    }
}
