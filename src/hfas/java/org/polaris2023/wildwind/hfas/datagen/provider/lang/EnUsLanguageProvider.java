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
        // 焚烬木套件
        addBlock(ModBlocks.BLAZE_LOG, "Blaze Log");
        addBlock(ModBlocks.BLAZE_WOOD, "Blaze Wood");
        addBlock(ModBlocks.STRIPPED_BLAZE_LOG, "Stripped Blaze Log");
        addBlock(ModBlocks.STRIPPED_BLAZE_WOOD, "Stripped Blaze Wood");
        addBlock(ModBlocks.BLAZE_PLANKS, "Blaze Planks");
        addBlock(ModBlocks.BLAZE_STAIRS, "Blaze Stairs");
        addBlock(ModBlocks.BLAZE_SLAB, "Blaze Slab");
        addBlock(ModBlocks.BLAZE_FENCE, "Blaze Fence");
        addBlock(ModBlocks.BLAZE_FENCE_GATE, "Blaze Fence Gate");
        addBlock(ModBlocks.BLAZE_DOOR, "Blaze Door");
        addBlock(ModBlocks.BLAZE_TRAPDOOR, "Blaze Trapdoor");
        addBlock(ModBlocks.BLAZE_PRESSURE_PLATE, "Blaze Pressure Plate");
        addBlock(ModBlocks.BLAZE_BUTTON, "Blaze Button");
        addBlock(ModBlocks.BLAZE_LEAVES, "Blaze Leaves");
        addBlock(ModBlocks.BLAZE_SAPLING, "Blaze Sapling");

        // 灵焰木套件
        addBlock(ModBlocks.SOUL_LOG, "Soul Log");
        addBlock(ModBlocks.SOUL_WOOD, "Soul Wood");
        addBlock(ModBlocks.STRIPPED_SOUL_LOG, "Stripped Soul Log");
        addBlock(ModBlocks.STRIPPED_SOUL_WOOD, "Stripped Soul Wood");
        addBlock(ModBlocks.SOUL_PLANKS, "Soul Planks");
        addBlock(ModBlocks.SOUL_STAIRS, "Soul Stairs");
        addBlock(ModBlocks.SOUL_SLAB, "Soul Slab");
        addBlock(ModBlocks.SOUL_FENCE, "Soul Fence");
        addBlock(ModBlocks.SOUL_FENCE_GATE, "Soul Fence Gate");
        addBlock(ModBlocks.SOUL_DOOR, "Soul Door");
        addBlock(ModBlocks.SOUL_TRAPDOOR, "Soul Trapdoor");
        addBlock(ModBlocks.SOUL_PRESSURE_PLATE, "Soul Pressure Plate");
        addBlock(ModBlocks.SOUL_BUTTON, "Soul Button");
        addBlock(ModBlocks.SOUL_LEAVES, "Soul Leaves");
        addBlock(ModBlocks.SOUL_SAPLING, "Soul Sapling");

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
    }
}
