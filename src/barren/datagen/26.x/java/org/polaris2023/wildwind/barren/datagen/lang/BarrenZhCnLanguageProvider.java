package org.polaris2023.wildwind.barren.datagen.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.registry.BarrenEntities;
import org.polaris2023.wildwind.barren.registry.BarrenItems;

/**
 * 瘠土更新的简体中文语言文件。
 *
 * @author safe_saffawa
 */
public class BarrenZhCnLanguageProvider extends LanguageProvider {

    public BarrenZhCnLanguageProvider(PackOutput output) {
        super(output, WildWindBarren.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // 创造栏标题
        add("tab.ww_barren.name", "瘠土更新");

        // 焚烬木套件
        addBlock(BarrenBlocks.CINDER_LOG, "焚烬木原木");
        addBlock(BarrenBlocks.CINDER_WOOD, "焚烬木");
        addBlock(BarrenBlocks.STRIPPED_CINDER_LOG, "去皮焚烬木原木");
        addBlock(BarrenBlocks.STRIPPED_CINDER_WOOD, "去皮焚烬木");
        addBlock(BarrenBlocks.CINDER_PLANKS, "焚烬木木板");
        addBlock(BarrenBlocks.CINDER_STAIRS, "焚烬木楼梯");
        addBlock(BarrenBlocks.CINDER_SLAB, "焚烬木台阶");
        addBlock(BarrenBlocks.CINDER_FENCE, "焚烬木栅栏");
        addBlock(BarrenBlocks.CINDER_FENCE_GATE, "焚烬木栅栏门");
        addBlock(BarrenBlocks.CINDER_DOOR, "焚烬木门");
        addBlock(BarrenBlocks.CINDER_TRAPDOOR, "焚烬木活板门");
        addBlock(BarrenBlocks.CINDER_PRESSURE_PLATE, "焚烬木压力板");
        addBlock(BarrenBlocks.CINDER_BUTTON, "焚烬木按钮");
        addBlock(BarrenBlocks.CINDER_SHELF, "焚烬木展示架");
        addBlock(BarrenBlocks.CINDER_SIGN, "焚烬木告示牌");
        addBlock(BarrenBlocks.CINDER_WALL_SIGN, "焚烬木墙式告示牌");
        addBlock(BarrenBlocks.CINDER_HANGING_SIGN, "焚烬木悬挂告示牌");
        addBlock(BarrenBlocks.CINDER_WALL_HANGING_SIGN, "焚烬木墙式悬挂告示牌");
        addItem(BarrenItems.CINDER_BOAT, "焚烬木船");
        addItem(BarrenItems.CINDER_CHEST_BOAT, "焚烬木运输船");
        addEntityType(BarrenEntities.CINDER_BOAT, "焚烬木船");
        addEntityType(BarrenEntities.CINDER_CHEST_BOAT, "焚烬木运输船");
        addBlock(BarrenBlocks.CINDER_LEAVES, "焚烬木树叶");
        addBlock(BarrenBlocks.CINDER_SAPLING, "焚烬木树苗");
        addBlock(BarrenBlocks.POTTED_CINDER_SAPLING, "焚烬木树苗盆栽");

        // 灵焰木套件
        addBlock(BarrenBlocks.EMBER_LOG, "灵焰木原木");
        addBlock(BarrenBlocks.EMBER_WOOD, "灵焰木");
        addBlock(BarrenBlocks.STRIPPED_EMBER_LOG, "去皮灵焰木原木");
        addBlock(BarrenBlocks.STRIPPED_EMBER_WOOD, "去皮灵焰木");
        addBlock(BarrenBlocks.EMBER_PLANKS, "灵焰木木板");
        addBlock(BarrenBlocks.EMBER_STAIRS, "灵焰木楼梯");
        addBlock(BarrenBlocks.EMBER_SLAB, "灵焰木台阶");
        addBlock(BarrenBlocks.EMBER_FENCE, "灵焰木栅栏");
        addBlock(BarrenBlocks.EMBER_FENCE_GATE, "灵焰木栅栏门");
        addBlock(BarrenBlocks.EMBER_DOOR, "灵焰木门");
        addBlock(BarrenBlocks.EMBER_TRAPDOOR, "灵焰木活板门");
        addBlock(BarrenBlocks.EMBER_PRESSURE_PLATE, "灵焰木压力板");
        addBlock(BarrenBlocks.EMBER_BUTTON, "灵焰木按钮");
        addBlock(BarrenBlocks.EMBER_SHELF, "灵焰木展示架");
        addBlock(BarrenBlocks.EMBER_SIGN, "灵焰木告示牌");
        addBlock(BarrenBlocks.EMBER_WALL_SIGN, "灵焰木墙式告示牌");
        addBlock(BarrenBlocks.EMBER_HANGING_SIGN, "灵焰木悬挂告示牌");
        addBlock(BarrenBlocks.EMBER_WALL_HANGING_SIGN, "灵焰木墙式悬挂告示牌");
        addItem(BarrenItems.EMBER_BOAT, "灵焰木船");
        addItem(BarrenItems.EMBER_CHEST_BOAT, "灵焰木运输船");
        addEntityType(BarrenEntities.EMBER_BOAT, "灵焰木船");
        addEntityType(BarrenEntities.EMBER_CHEST_BOAT, "灵焰木运输船");
        addBlock(BarrenBlocks.EMBER_LEAVES, "灵焰木树叶");
        addBlock(BarrenBlocks.EMBER_SAPLING, "灵焰木树苗");
        addBlock(BarrenBlocks.POTTED_EMBER_SAPLING, "灵焰木树苗盆栽");

        // 焦土 / 瘠地
        addBlock(BarrenBlocks.SCORCHED_GRASS_BLOCK, "焦灰草方块");
        addBlock(BarrenBlocks.SCORCHED_DIRT, "焦土");
        addBlock(BarrenBlocks.SCORCHED_GRASS, "焦灰草丛");
        addBlock(BarrenBlocks.SCORCHED_TWIG, "焦灰枝条");
        addBlock(BarrenBlocks.TINY_CACTUS, "仙人球");
        addBlock(BarrenBlocks.QUICKSAND, "§4流沙");
        addBlock(BarrenBlocks.RED_QUICKSAND, "§4红流沙");
        addBlock(BarrenBlocks.SUSPICIOUS_RED, "§4可疑的红沙");

        // 砂岩 / 沙
        addBlock(BarrenBlocks.CHISELED_QUARTZ_SANDSTONE, "雕纹石英砂岩");
        addBlock(BarrenBlocks.CHISELED_SOUL_SANDSTONE, "雕纹灵魂砂岩");
        addBlock(BarrenBlocks.CUT_QUARTZ_SANDSTONE, "切割石英砂岩");
        addBlock(BarrenBlocks.CUT_SOUL_SANDSTONE, "切割灵魂砂岩");
        addBlock(BarrenBlocks.NITRIFIED_QUARTZ_SANDSTONE, "硝化石英砂岩");
        addBlock(BarrenBlocks.NITRIFIED_RED_SANDSTONE, "硝化红砂岩");
        addBlock(BarrenBlocks.NITRIFIED_SANDSTONE, "硝化砂岩");
        addBlock(BarrenBlocks.NITRIFIED_SOUL_SANDSTONE, "硝化灵魂砂岩");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE, "石英砂岩");
        addBlock(BarrenBlocks.SOUL_SANDSTONE, "灵魂砂岩");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE, "平滑石英砂岩");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE, "平滑灵魂砂岩");
        addBlock(BarrenBlocks.QUARTZ_SAND, "石英沙");
        addBlock(BarrenBlocks.QUARTZ_QUICKSAND, "石英流沙");
        addBlock(BarrenBlocks.SOUL_QUICKSAND, "灵魂流沙");
        addBlock(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND, "可疑的珍珠沙");
        addBlock(BarrenBlocks.SUSPICIOUS_SOUL_SAND, "可疑的灵魂沙");

        // 灰烬 / 植被（草稿）
        addBlock(BarrenBlocks.FROGBIT, "浮萤");
        addBlock(BarrenBlocks.ASH_BLOCK, "灰烬块");

        // 灵魂砂岩衍生物 / 硝石
        addBlock(BarrenBlocks.SOUL_SANDSTONE_STAIRS, "灵魂砂岩楼梯");
        addBlock(BarrenBlocks.SOUL_SANDSTONE_SLAB, "灵魂砂岩台阶");
        addBlock(BarrenBlocks.SOUL_SANDSTONE_WALL, "灵魂砂岩墙");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_STAIRS, "平滑灵魂砂岩楼梯");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB, "平滑灵魂砂岩台阶");
        addBlock(BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB, "切制灵魂砂岩台阶");
        addBlock(BarrenBlocks.NITER_BLOCK, "硝石块");

        // 石英砂岩衍生物
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_STAIRS, "石英砂岩楼梯");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_SLAB, "石英砂岩台阶");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_WALL, "石英砂岩墙");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_STAIRS, "平滑石英砂岩楼梯");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB, "平滑石英砂岩台阶");
        addBlock(BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB, "切制石英砂岩台阶");
    }
}
