package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.HFASMod;

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
        add("effect.wild_wind_wilderness.sink", "Sink");
        add("effect.wild_wind_wilderness.choke", "Choke");
        add("enchantment.wild_wind_core.melting", "Melting");
        add("item.wild_wind_core.duckweed", "Duckweed");
        add("item.wild_wind_core.calamari", "Calamari");
        add("item.wild_wind_core.cooked_calamari", "Cooked Calamari");
        add("item.wild_wind_core.cooked_egg", "Fried Egg");
        add("item.wild_wind_core.baked_seeds", "Baked Seeds");
        add("item.wild_wind_core.cooked_frog_legs", "Cooked Frog Legs");
        add("item.wild_wind_core.frog_legs", "Raw Frog Legs");
        add("entity.wild_wind_wilderness.muddy_pig", "Muddy Pig");
        add("item.wild_wind_wilderness.muddy_pig_spawn_egg", "Muddy Pig Spawn Egg");
        // 焚烬木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG, "Blaze Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD, "Blaze Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG, "Stripped Blaze Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD, "Stripped Blaze Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS, "Blaze Planks");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS, "Blaze Stairs");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB, "Blaze Slab");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE, "Blaze Fence");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE, "Blaze Fence Gate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR, "Blaze Door");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR, "Blaze Trapdoor");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE, "Blaze Pressure Plate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON, "Blaze Button");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES, "Blaze Leaves");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING, "Blaze Sapling");

        // 灵焰木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG, "Soul Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD, "Soul Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG, "Stripped Soul Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD, "Stripped Soul Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS, "Soul Planks");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS, "Soul Stairs");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB, "Soul Slab");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE, "Soul Fence");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE, "Soul Fence Gate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR, "Soul Door");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR, "Soul Trapdoor");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE, "Soul Pressure Plate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON, "Soul Button");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES, "Soul Leaves");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING, "Soul Sapling");

        // 杜鹃木套件
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG, "Azalea Log");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD, "Azalea Wood");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG, "Stripped Azalea Log");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD, "Stripped Azalea Wood");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS, "Azalea Planks");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS, "Azalea Stairs");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB, "Azalea Slab");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE, "Azalea Fence");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE, "Azalea Fence Gate");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR, "Azalea Door");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR, "Azalea Trapdoor");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE, "Azalea Pressure Plate");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON, "Azalea Button");

        // 其他方块
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK, "Scorched Grass Block");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT, "Scorched Dirt");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS, "Scorched Grass");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_TWIG, "Scorched Twig");
        addBlock(org.polaris2023.wildwind.barren.block.ModBlocks.TINY_CACTUS, "Tiny Cactus");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN, "Azalea Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN, "Azalea Wall Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN, "Azalea Hanging Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN, "Azalea Wall Hanging Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF, "Azalea Shelf");
        add("item.wild_wind_wilderness.tipped_arrow", "Arrow of Reach");
        add("item.wild_wind_wilderness.tipped_arrow.effect.reach", "Arrow of Reach");
        add("item.wild_wind_wilderness.tipped_arrow.effect.long_reach", "Arrow of Reach");
        add("entity.wild_wind_wilderness.arrow", "Arrow of Reach");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.KILN, "Kiln");

        // GUI
        // 属性
        add("attributes.wild_wind_wilderness.extra_item_pickup_range", "Extra Item Pickup Range");

        // Reach effect & potions
        add("effect.wild_wind_wilderness.reach", "Reach");
        add("item.minecraft.potion.effect.reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.reach", "Arrow of Reach");
        add("item.minecraft.potion.effect.long_reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.long_reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.long_reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.long_reach", "Arrow of Reach");

        // Direfish sound subtitles
        add("subtitles.entity.piranha.attack", "Direfish attacks");
        add("subtitles.entity.piranha.death", "Direfish dies");

        //创造模式物品栏
        add("mod.wild_wind.name", "Wild Wind");

        ZhEnCombineLangProvider.addLang(this, "en_us");
    }
}
