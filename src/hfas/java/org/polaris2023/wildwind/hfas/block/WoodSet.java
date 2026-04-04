package org.polaris2023.wildwind.hfas.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

public record WoodSet(
        String name,
        DeferredBlock<Block> log,
        DeferredBlock<Block> wood,
        DeferredBlock<Block> strippedLog,
        DeferredBlock<Block> strippedWood,
        DeferredBlock<Block> leaves,
        DeferredBlock<Block> planks,
        DeferredBlock<Block> stairs,
        DeferredBlock<Block> slab,
        DeferredBlock<Block> fence,
        DeferredBlock<Block> fenceGate,
        DeferredBlock<Block> door,
        DeferredBlock<Block> trapdoor,
        DeferredBlock<Block> pressurePlate,
        DeferredBlock<Block> button,
        DeferredBlock<Block> sapling,
        DeferredBlock<Block> pottedSapling,
        DeferredBlock<Block> sign,
        DeferredBlock<Block> wallSign,
        DeferredBlock<Block> hangingSign,
        DeferredBlock<Block> wallHangingSign
) {
}
