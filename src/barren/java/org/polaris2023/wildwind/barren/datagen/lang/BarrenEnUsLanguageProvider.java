package org.polaris2023.wildwind.barren.datagen.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.registry.BarrenEntities;
import org.polaris2023.wildwind.barren.registry.BarrenItems;
import org.polaris2023.wildwind.barren.registry.ModItems;

/**
 * 瘠土更新的英文语言文件。
 *
 * @author safe_saffawa
 */
public class BarrenEnUsLanguageProvider extends LanguageProvider {

    public BarrenEnUsLanguageProvider(PackOutput output) {
        super(output, WildWindBarren.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // 创造栏标题
        add("tab.ww_barren.name", "Barren Update");

        // 焚烬木套件
        addBlock(BarrenBlocks.CINDER_LOG, "Cinder Log");
        addBlock(BarrenBlocks.CINDER_WOOD, "Cinder Wood");
        addBlock(BarrenBlocks.STRIPPED_CINDER_LOG, "Stripped Cinder Log");
        addBlock(BarrenBlocks.STRIPPED_CINDER_WOOD, "Stripped Cinder Wood");
        addBlock(BarrenBlocks.CINDER_PLANKS, "Cinder Planks");
        addBlock(BarrenBlocks.CINDER_STAIRS, "Cinder Stairs");
        addBlock(BarrenBlocks.CINDER_SLAB, "Cinder Slab");
        addBlock(BarrenBlocks.CINDER_FENCE, "Cinder Fence");
        addBlock(BarrenBlocks.CINDER_FENCE_GATE, "Cinder Fence Gate");
        addBlock(BarrenBlocks.CINDER_DOOR, "Cinder Door");
        addBlock(BarrenBlocks.CINDER_TRAPDOOR, "Cinder Trapdoor");
        addBlock(BarrenBlocks.CINDER_PRESSURE_PLATE, "Cinder Pressure Plate");
        addBlock(BarrenBlocks.CINDER_BUTTON, "Cinder Button");
        addBlock(BarrenBlocks.CINDER_SHELF, "Cinder Shelf");
        addBlock(BarrenBlocks.CINDER_SIGN, "Cinder Sign");
        addBlock(BarrenBlocks.CINDER_WALL_SIGN, "Cinder Wall Sign");
        addBlock(BarrenBlocks.CINDER_HANGING_SIGN, "Cinder Hanging Sign");
        addBlock(BarrenBlocks.CINDER_WALL_HANGING_SIGN, "Cinder Wall Hanging Sign");
        addItem(BarrenItems.CINDER_BOAT, "Cinder Boat");
        addItem(BarrenItems.CINDER_CHEST_BOAT, "Cinder Boat with Chest");
        addEntityType(BarrenEntities.CINDER_BOAT, "Cinder Boat");
        addEntityType(BarrenEntities.CINDER_CHEST_BOAT, "Cinder Boat with Chest");
        addBlock(BarrenBlocks.CINDER_LEAVES, "Cinder Leaves");
        addBlock(BarrenBlocks.CINDER_SAPLING, "Cinder Sapling");
        addBlock(BarrenBlocks.POTTED_CINDER_SAPLING, "Potted Cinder Sapling");

        // 灵焰木套件
        addBlock(BarrenBlocks.EMBER_LOG, "Ember Log");
        addBlock(BarrenBlocks.EMBER_WOOD, "Ember Wood");
        addBlock(BarrenBlocks.STRIPPED_EMBER_LOG, "Stripped Ember Log");
        addBlock(BarrenBlocks.STRIPPED_EMBER_WOOD, "Stripped Ember Wood");
        addBlock(BarrenBlocks.EMBER_PLANKS, "Ember Planks");
        addBlock(BarrenBlocks.EMBER_STAIRS, "Ember Stairs");
        addBlock(BarrenBlocks.EMBER_SLAB, "Ember Slab");
        addBlock(BarrenBlocks.EMBER_FENCE, "Ember Fence");
        addBlock(BarrenBlocks.EMBER_FENCE_GATE, "Ember Fence Gate");
        addBlock(BarrenBlocks.EMBER_DOOR, "Ember Door");
        addBlock(BarrenBlocks.EMBER_TRAPDOOR, "Ember Trapdoor");
        addBlock(BarrenBlocks.EMBER_PRESSURE_PLATE, "Ember Pressure Plate");
        addBlock(BarrenBlocks.EMBER_BUTTON, "Ember Button");
        addBlock(BarrenBlocks.EMBER_SHELF, "Ember Shelf");
        addBlock(BarrenBlocks.EMBER_SIGN, "Ember Sign");
        addBlock(BarrenBlocks.EMBER_WALL_SIGN, "Ember Wall Sign");
        addBlock(BarrenBlocks.EMBER_HANGING_SIGN, "Ember Hanging Sign");
        addBlock(BarrenBlocks.EMBER_WALL_HANGING_SIGN, "Ember Wall Hanging Sign");
        addItem(BarrenItems.EMBER_BOAT, "Ember Boat");
        addItem(BarrenItems.EMBER_CHEST_BOAT, "Ember Boat with Chest");
        addEntityType(BarrenEntities.EMBER_BOAT, "Ember Boat");
        addEntityType(BarrenEntities.EMBER_CHEST_BOAT, "Ember Boat with Chest");
        addBlock(BarrenBlocks.EMBER_LEAVES, "Ember Leaves");
        addBlock(BarrenBlocks.EMBER_SAPLING, "Ember Sapling");
        addBlock(BarrenBlocks.POTTED_EMBER_SAPLING, "Potted Ember Sapling");

        // 焦土 / 瘠地
        addBlock(BarrenBlocks.SCORCHED_GRASS_BLOCK, "Scorched Grass Block");
        addBlock(BarrenBlocks.SCORCHED_DIRT, "Scorched Dirt");
        addBlock(BarrenBlocks.SCORCHED_GRASS, "Scorched Grass");
        addBlock(BarrenBlocks.SCORCHED_TWIG, "Scorched Twig");
        addBlock(BarrenBlocks.TINY_CACTUS, "Tiny Cactus");
        addBlock(BarrenBlocks.QUICKSAND, "§4Quicksand");
        addBlock(BarrenBlocks.RED_QUICKSAND, "§4Red Quicksand");
        addBlock(BarrenBlocks.SUSPICIOUS_RED, "§4Suspicious Red Sand");

        // 砂岩 / 沙
        addBlock(BarrenBlocks.CHISELED_QUARTZ_SANDSTONE, "Chiseled Quartz Sandstone");
        addBlock(BarrenBlocks.CHISELED_SOUL_SANDSTONE, "Chiseled Soul Sandstone");
        addBlock(BarrenBlocks.CUT_QUARTZ_SANDSTONE, "Cut Quartz Sandstone");
        addBlock(BarrenBlocks.CUT_SOUL_SANDSTONE, "Cut Soul Sandstone");
        addBlock(BarrenBlocks.NITRIFIED_QUARTZ_SANDSTONE, "Nitrified Quartz Sandstone");
        addBlock(BarrenBlocks.NITRIFIED_RED_SANDSTONE, "Nitrified Red Sandstone");
        addBlock(BarrenBlocks.NITRIFIED_SANDSTONE, "Nitrified Sandstone");
        addBlock(BarrenBlocks.NITRIFIED_SOUL_SANDSTONE, "Nitrified Soul Sandstone");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE, "Quartz Sandstone");
        addBlock(BarrenBlocks.SOUL_SANDSTONE, "Soul Sandstone");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE, "Smooth Quartz Sandstone");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE, "Smooth Soul Sandstone");
        addBlock(BarrenBlocks.QUARTZ_SAND, "Quartz Sand");
        addBlock(BarrenBlocks.QUARTZ_QUICKSAND, "Quartz Quicksand");
        addBlock(BarrenBlocks.SOUL_QUICKSAND, "Soul Quicksand");
        addBlock(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND, "Suspicious Quartz Sand");
        addBlock(BarrenBlocks.SUSPICIOUS_SOUL_SAND, "Suspicious Soul Sand");

        // 灰烬 / 植被（草稿）
        addBlock(BarrenBlocks.FROGBIT, "Frogbit");
        addBlock(BarrenBlocks.ASH_BLOCK, "Ash Block");

        // 灵魂砂岩衍生物 / 硝石
        addBlock(BarrenBlocks.SOUL_SANDSTONE_STAIRS, "Soul Sandstone Stairs");
        addBlock(BarrenBlocks.SOUL_SANDSTONE_SLAB, "Soul Sandstone Slab");
        addBlock(BarrenBlocks.SOUL_SANDSTONE_WALL, "Soul Sandstone Wall");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_STAIRS, "Smooth Soul Sandstone Stairs");
        addBlock(BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB, "Smooth Soul Sandstone Slab");
        addBlock(BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB, "Cut Soul Sandstone Slab");
        addBlock(BarrenBlocks.NITER_BLOCK, "Niter Block");

        // 石英砂岩衍生物
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_STAIRS, "Quartz Sandstone Stairs");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_SLAB, "Quartz Sandstone Slab");
        addBlock(BarrenBlocks.QUARTZ_SANDSTONE_WALL, "Quartz Sandstone Wall");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_STAIRS, "Smooth Quartz Sandstone Stairs");
        addBlock(BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB, "Smooth Quartz Sandstone Slab");
        addBlock(BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB, "Cut Quartz Sandstone Slab");

        addItem(ModItems.ASH, "Ash");
    }
}
