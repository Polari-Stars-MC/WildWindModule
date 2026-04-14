package org.polaris2023.wildwind.hfas.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

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
public class ModBlocks {
    // 方块注册器
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HFASMod.MOD_ID);
    // 物品注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HFASMod.MOD_ID);

    // ==================== 方块定义 ====================

    // 焦灰草方块 (scorched_grass_block)
    public static final DeferredBlock<Block> SCORCHED_GRASS_BLOCK = BLOCKS.registerBlock(
            "scorched_grass_block",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.6F)
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // 焦土 (scorched_dirt)
    public static final DeferredBlock<Block> SCORCHED_DIRT = BLOCKS.registerBlock(
            "scorched_dirt",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL))
    );

    // 焦灰草丛 (scorched_grass)
    public static final DeferredBlock<Block> SCORCHED_GRASS = BLOCKS.registerBlock(
            "scorched_grass",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 焦灰枝条 (scorched_twig)
    public static final DeferredBlock<Block> SCORCHED_TWIG = BLOCKS.registerBlock(
            "scorched_twig",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BLACK)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 仙人球 (tiny_cactus)
    public static final DeferredBlock<Block> TINY_CACTUS = BLOCKS.registerBlock(
            "tiny_cactus",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY))
    );

    // 制箭台 (fletching_table) - 使用自定义方块类支持GUI
    public static final DeferredBlock<Block> FLETCHING_TABLE = BLOCKS.registerBlock(
            "fletching_table",
            p -> new FletchingTableBlock(p
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD))
    );

    public static void blockItems() {

        {
            final var t = ITEMS.registerSimpleBlockItem(SCORCHED_GRASS_BLOCK);
        }
        {
            final var t = ITEMS.registerSimpleBlockItem(SCORCHED_DIRT);
        }
        {
            final var t = ITEMS.registerSimpleBlockItem(SCORCHED_GRASS);
        }
        {
            final var t = ITEMS.registerSimpleBlockItem(SCORCHED_TWIG);
        }
        {
            final var t = ITEMS.registerSimpleBlockItem(TINY_CACTUS);
        }

    }

    /**
     * 注册方块和物品到事件总线
     * 必须在模组构造函数中调用
     */
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        blockItems();
        ITEMS.register(bus);
    }
}
