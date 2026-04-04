package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.WoodSet;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, HFASMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

//        super.registerModels(blockModels, itemModels);
    }

    private void registerWoodSet(BlockModelGenerators blockModel) {
        for (WoodSet woodSet : ModBlocks.WOOD_SETS) {
            
        }
    }
}
