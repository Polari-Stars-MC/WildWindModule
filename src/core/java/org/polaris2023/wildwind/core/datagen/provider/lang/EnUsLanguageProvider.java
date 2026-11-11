package org.polaris2023.wildwind.core.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.core.registry.ModItems;

/**
 * 核心模块的英文语言文件。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class EnUsLanguageProvider extends LanguageProvider {

    public EnUsLanguageProvider(PackOutput output) {
        super(output, WildWindCore.MOD_ID, "en_us");
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
        addBlock(ModBlocks.AZALEA_SHELF, "Azalea Shelf");
        addBlock(ModBlocks.AZALEA_SIGN, "Azalea Sign");
        addBlock(ModBlocks.AZALEA_WALL_SIGN, "Azalea Wall Sign");
        addBlock(ModBlocks.AZALEA_HANGING_SIGN, "Azalea Hanging Sign");
        addBlock(ModBlocks.AZALEA_WALL_HANGING_SIGN, "Azalea Wall Hanging Sign");
        addItem(ModItems.AZALEA_BOAT, "Azalea Boat");
        addItem(ModItems.AZALEA_CHEST_BOAT, "Azalea Boat with Chest");
        addEntityType(ModEntities.AZALEA_BOAT, "Azalea Boat");
        addEntityType(ModEntities.AZALEA_CHEST_BOAT, "Azalea Boat with Chest");

        // 其他方块
        addBlock(ModBlocks.FLETCHING_TABLE, "Fletching Table");
        addBlock(ModBlocks.ROSE, "Rose");
        addBlock(ModBlocks.SOUL_ROSE, "Soul Rose");
        addBlock(ModBlocks.SOUL_ROSE_BUSH, "Soul Rose Bush");
        addBlock(ModBlocks.WITHER_ROSE_BUSH, "Wither Rose Bush");
        addBlock(ModBlocks.KILN, "Kiln");

        // GUI
        add("container.fletching_table", "Arrow Crafting");
        add("tab.ww_core.name", "Wild Wind");
    }
}
