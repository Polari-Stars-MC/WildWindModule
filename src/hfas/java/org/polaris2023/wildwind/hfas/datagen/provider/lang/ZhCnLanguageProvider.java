package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 中文语言文件生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ZhCnLanguageProvider extends LanguageProvider {

    public ZhCnLanguageProvider(PackOutput output) {
        super(output, HFASMod.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // 焚烬木套件
        addBlock(ModBlocks.BLAZE_LOG, "焚烬木原木");
        addBlock(ModBlocks.BLAZE_WOOD, "焚烬木");
        addBlock(ModBlocks.STRIPPED_BLAZE_LOG, "去皮焚烬木原木");
        addBlock(ModBlocks.STRIPPED_BLAZE_WOOD, "去皮焚烬木");
        addBlock(ModBlocks.BLAZE_PLANKS, "焚烬木木板");
        addBlock(ModBlocks.BLAZE_STAIRS, "焚烬木楼梯");
        addBlock(ModBlocks.BLAZE_SLAB, "焚烬木台阶");
        addBlock(ModBlocks.BLAZE_FENCE, "焚烬木栅栏");
        addBlock(ModBlocks.BLAZE_FENCE_GATE, "焚烬木栅栏门");
        addBlock(ModBlocks.BLAZE_DOOR, "焚烬木门");
        addBlock(ModBlocks.BLAZE_TRAPDOOR, "焚烬木活板门");
        addBlock(ModBlocks.BLAZE_PRESSURE_PLATE, "焚烬木压力板");
        addBlock(ModBlocks.BLAZE_BUTTON, "焚烬木按钮");
        addBlock(ModBlocks.BLAZE_LEAVES, "焚烬木树叶");
        addBlock(ModBlocks.BLAZE_SAPLING, "焚烬木树苗");

        // 灵焰木套件
        addBlock(ModBlocks.SOUL_LOG, "灵焰木原木");
        addBlock(ModBlocks.SOUL_WOOD, "灵焰木");
        addBlock(ModBlocks.STRIPPED_SOUL_LOG, "去皮灵焰木原木");
        addBlock(ModBlocks.STRIPPED_SOUL_WOOD, "去皮灵焰木");
        addBlock(ModBlocks.SOUL_PLANKS, "灵焰木木板");
        addBlock(ModBlocks.SOUL_STAIRS, "灵焰木楼梯");
        addBlock(ModBlocks.SOUL_SLAB, "灵焰木台阶");
        addBlock(ModBlocks.SOUL_FENCE, "灵焰木栅栏");
        addBlock(ModBlocks.SOUL_FENCE_GATE, "灵焰木栅栏门");
        addBlock(ModBlocks.SOUL_DOOR, "灵焰木门");
        addBlock(ModBlocks.SOUL_TRAPDOOR, "灵焰木活板门");
        addBlock(ModBlocks.SOUL_PRESSURE_PLATE, "灵焰木压力板");
        addBlock(ModBlocks.SOUL_BUTTON, "灵焰木按钮");
        addBlock(ModBlocks.SOUL_LEAVES, "灵焰木树叶");
        addBlock(ModBlocks.SOUL_SAPLING, "灵焰木树苗");

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

        // 其他方块
        addBlock(ModBlocks.SCORCHED_GRASS_BLOCK, "焦灰草方块");
        addBlock(ModBlocks.SCORCHED_DIRT, "焦土");
        addBlock(ModBlocks.SCORCHED_GRASS, "焦灰草丛");
        addBlock(ModBlocks.SCORCHED_TWIG, "焦灰枝条");
        addBlock(ModBlocks.TINY_CACTUS, "仙人球");
        addBlock(ModBlocks.FLETCHING_TABLE, "制箭台");

        // GUI
        add("container.fletching_table", "制箭台");
    }
}
