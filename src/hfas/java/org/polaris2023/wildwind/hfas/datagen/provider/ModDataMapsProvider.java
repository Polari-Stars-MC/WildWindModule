package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.ModWoodFamilies;
import org.polaris2023.wildwind.hfas.block.WoodFamily;

import java.util.concurrent.CompletableFuture;

public class ModDataMapsProvider extends DataMapProvider {
    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    public ModDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.STRIPPABLES)
                .add(ModBlocks.AZALEA_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_AZALEA_LOG.get()), false)
                .add(ModBlocks.AZALEA_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_AZALEA_WOOD.get()), false)
                .add(ModBlocks.BLAZE_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_BLAZE_LOG.get()), false)
                .add(ModBlocks.BLAZE_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_BLAZE_WOOD.get()), false)
                .add(ModBlocks.SOUL_LOG.getKey(), new Strippable(ModBlocks.STRIPPED_SOUL_LOG.get()), false)
                .add(ModBlocks.SOUL_WOOD.getKey(), new Strippable(ModBlocks.STRIPPED_SOUL_WOOD.get()), false)
        ;

        // 燃料：走 NeoForge 数据地图（neoforge/data_maps/item/furnace_fuels.json），
        // 不再用 FurnaceFuelBurnTimeEvent 硬判。燃烧时间对齐原版木质方块。
        var fuels = builder(NeoForgeDataMaps.FURNACE_FUELS);
        for (WoodFamily family : ModWoodFamilies.ALL) {
            fuels.add(itemKey(family.log().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.wood().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.strippedLog().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.strippedWood().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.planks().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.stairs().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.slab().get()), new FurnaceFuel(150), false);
            fuels.add(itemKey(family.fence().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.fenceGate().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.pressurePlate().get()), new FurnaceFuel(300), false);
            fuels.add(itemKey(family.button().get()), new FurnaceFuel(100), false);
            if (family.sapling() != null) {
                fuels.add(itemKey(family.sapling().get()), new FurnaceFuel(100), false);
            }
        }
    }

    private static ResourceKey<Item> itemKey(Block block) {
        return block.asItem().builtInRegistryHolder().getKey();
    }
}
