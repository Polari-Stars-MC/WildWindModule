package org.polaris2023.wildwind.barren.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.barren.BarrenMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 方块注册类
 * 使用NeoForge 26.1的DeferredRegister方式注册方块
 * 注意：方块和物品分开注册，避免延迟注册bug
 *
 * @author baka4n
 * @since 2026/04/15
 */
//TODO 考虑迁移至registry目录下，或将registry目录下的注册条目分散到各目录下
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BarrenMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BarrenMod.MOD_ID);
    public static final DeferredBlock<Block> TINY_CACTUS = BLOCKS.registerBlock(
            "tiny_cactus",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY))
    );

    private static void blockItems() {
        ITEMS.registerSimpleBlockItem(TINY_CACTUS);
    }
    public static void register(IEventBus bus) { BLOCKS.register(bus); blockItems(); ITEMS.register(bus); }
}
