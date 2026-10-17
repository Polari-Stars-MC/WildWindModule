package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 英语语言文件生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class EnUsLanguageProvider extends LanguageProvider {

    public EnUsLanguageProvider(PackOutput output) {
        super(output, HFASMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {


        // 杜鹃木套件
        addBlock(ModBlocks.AZALEA_LOG, "Azalea Log");
        addBlock(ModBlocks.AZALEA_WOOD, "Azalea Wood");
        addBlock(ModBlocks.STRIPPED_AZALEA_LOG, "Stripped Azalea Log");
        addBlock(ModBlocks.STRIPPED_AZALEA_WOOD, "Stripped Azalea Wood");
        addBlock(ModBlocks.AZALEA_PLANKS, "Azalea Planks");
        addBlock(ModBlocks.AZALEA_STAIRS, "Azalea Stairs");
        addBlock(ModBlocks.AZALEA_SLAB, "Azalea Slab");
        addBlock(ModBlocks.AZALEA_FENCE, "Azalea Fence");
        addBlock(ModBlocks.AZALEA_FENCE_GATE, "Azalea Fence Gate");
        addBlock(ModBlocks.AZALEA_DOOR, "Azalea Door");
        addBlock(ModBlocks.AZALEA_TRAPDOOR, "Azalea Trapdoor");
        addBlock(ModBlocks.AZALEA_PRESSURE_PLATE, "Azalea Pressure Plate");
        addBlock(ModBlocks.AZALEA_BUTTON, "Azalea Button");

        // 其他方块
        addBlock(ModBlocks.SCORCHED_GRASS_BLOCK, "Scorched Grass Block");
        addBlock(ModBlocks.SCORCHED_DIRT, "Scorched Dirt");
        addBlock(ModBlocks.SCORCHED_GRASS, "Scorched Grass");
        addBlock(ModBlocks.SCORCHED_TWIG, "Scorched Twig");
        addBlock(ModBlocks.TINY_CACTUS, "Tiny Cactus");
        addBlock(ModBlocks.FLETCHING_TABLE, "Fletching Table");

        // GUI
        add("container.fletching_table", "Fletching Table");
        // 属性
        add("attributes.ww_hfas.extra_item_pickup_range", "Extra Item Pickup Range");

        // Reach effect & potions
        add("effect.ww_hfas.reach", "Reach");
        add("item.minecraft.potion.effect.reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.reach", "Arrow of Reach");
        add("item.minecraft.potion.effect.long_reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.long_reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.long_reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.long_reach", "Arrow of Reach");

        // Piranha sound subtitles
        add("subtitles.entity.piranha.attack", "Piranha attacks");
        add("subtitles.entity.piranha.death", "Piranha dies");

        //创造模式物品栏
        add("mod.wild_wind.name", "Wild Wind");

        ZhEnCombineLangProvider.addLang(this, "en_us");
    }
}

        addBlock(ModBlocks.CINDER_LOG, "Cinder Log");
        addBlock(ModBlocks.CINDER_WOOD, "Cinder Wood");
        addBlock(ModBlocks.STRIPPED_CINDER_LOG, "Stripped Cinder Log");
        addBlock(ModBlocks.STRIPPED_CINDER_WOOD, "Stripped Cinder Wood");
        addBlock(ModBlocks.CINDER_PLANKS, "Cinder Planks");
        addBlock(ModBlocks.CINDER_STAIRS, "Cinder Stairs");
        addBlock(ModBlocks.CINDER_SLAB, "Cinder Slab");
        addBlock(ModBlocks.CINDER_FENCE, "Cinder Fence");
        addBlock(ModBlocks.CINDER_FENCE_GATE, "Cinder Fence Gate");
        addBlock(ModBlocks.CINDER_DOOR, "Cinder Door");
        addBlock(ModBlocks.CINDER_TRAPDOOR, "Cinder Trapdoor");
        addBlock(ModBlocks.CINDER_PRESSURE_PLATE, "Cinder Pressure Plate");
        addBlock(ModBlocks.CINDER_BUTTON, "Cinder Button");
        addBlock(ModBlocks.CINDER_LEAVES, "Cinder Leaves");
        addBlock(ModBlocks.CINDER_SAPLING, "Cinder Sapling");
        addBlock(ModBlocks.EMBER_LOG, "Ember Log");
        addBlock(ModBlocks.EMBER_WOOD, "Ember Wood");
        addBlock(ModBlocks.STRIPPED_EMBER_LOG, "Stripped Ember Log");
        addBlock(ModBlocks.STRIPPED_EMBER_WOOD, "Stripped Ember Wood");
        addBlock(ModBlocks.EMBER_PLANKS, "Ember Planks");
        addBlock(ModBlocks.EMBER_STAIRS, "Ember Stairs");
        addBlock(ModBlocks.EMBER_SLAB, "Ember Slab");
        addBlock(ModBlocks.EMBER_FENCE, "Ember Fence");
        addBlock(ModBlocks.EMBER_FENCE_GATE, "Ember Fence Gate");
        addBlock(ModBlocks.EMBER_DOOR, "Ember Door");
        addBlock(ModBlocks.EMBER_TRAPDOOR, "Ember Trapdoor");
        addBlock(ModBlocks.EMBER_PRESSURE_PLATE, "Ember Pressure Plate");
        addBlock(ModBlocks.EMBER_BUTTON, "Ember Button");
        addBlock(ModBlocks.EMBER_LEAVES, "Ember Leaves");
        addBlock(ModBlocks.EMBER_SAPLING, "Ember Sapling");