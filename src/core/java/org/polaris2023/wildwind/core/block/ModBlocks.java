package org.polaris2023.wildwind.core.block;

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
import org.polaris2023.wildwind.core.CoreMod;
import org.polaris2023.wildwind.core.worldgen.ModTreeGrowers;

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
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CoreMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CoreMod.MOD_ID);
    public static final DeferredBlock<KilnBlock> KILN = BLOCKS.registerBlock("kiln", p -> new KilnBlock(p
            .mapColor(MapColor.STONE).instrument(net.minecraft.world.level.block.state.properties.NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops().strength(3.5F, 3.5F).sound(SoundType.STONE)
            .lightLevel(state -> state.getValue(KilnBlock.LIT) ? 13 : 0).pushReaction(PushReaction.BLOCK)));

    public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType("azalea"));

    public static final WoodType AZALEA_WOOD_TYPE = WoodType.register(new WoodType("wild_wind_core:azalea", AZALEA_BLOCK_SET));

    public static final DeferredBlock<RotatedPillarBlock> AZALEA_LOG = BLOCKS.registerBlock(
            "azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> AZALEA_WOOD = BLOCKS.registerBlock(
            "azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_LOG = BLOCKS.registerBlock(
            "stripped_azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_WOOD = BLOCKS.registerBlock(
            "stripped_azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<Block> AZALEA_PLANKS = BLOCKS.registerBlock(
            "azalea_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StairBlock> AZALEA_STAIRS = BLOCKS.registerBlock(
            "azalea_stairs",
            p -> new StairBlock(AZALEA_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<SlabBlock> AZALEA_SLAB = BLOCKS.registerBlock(
            "azalea_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceBlock> AZALEA_FENCE = BLOCKS.registerBlock(
            "azalea_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<FenceGateBlock> AZALEA_FENCE_GATE = BLOCKS.registerBlock(
            "azalea_fence_gate",
            p -> new FenceGateBlock(AZALEA_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<DoorBlock> AZALEA_DOOR = BLOCKS.registerBlock(
            "azalea_door",
            p -> new DoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<TrapDoorBlock> AZALEA_TRAPDOOR = BLOCKS.registerBlock(
            "azalea_trapdoor",
            p -> new TrapDoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<PressurePlateBlock> AZALEA_PRESSURE_PLATE = BLOCKS.registerBlock(
            "azalea_pressure_plate",
            p -> new PressurePlateBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<ButtonBlock> AZALEA_BUTTON = BLOCKS.registerBlock(
            "azalea_button",
            p -> new ButtonBlock(AZALEA_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    public static final DeferredBlock<StandingSignBlock> AZALEA_SIGN = BLOCKS.registerBlock("azalea_sign",
            p -> new StandingSignBlock(AZALEA_WOOD_TYPE, p), () -> signProperties(Blocks.OAK_SIGN));

    public static final DeferredBlock<WallSignBlock> AZALEA_WALL_SIGN = BLOCKS.registerBlock("azalea_wall_sign",
            p -> new WallSignBlock(AZALEA_WOOD_TYPE, p.overrideLootTable(AZALEA_SIGN.get().getLootTable())), () -> signProperties(Blocks.OAK_WALL_SIGN));

    public static final DeferredBlock<CeilingHangingSignBlock> AZALEA_HANGING_SIGN = BLOCKS.registerBlock("azalea_hanging_sign",
            p -> new CeilingHangingSignBlock(AZALEA_WOOD_TYPE, p), () -> signProperties(Blocks.OAK_HANGING_SIGN));

    public static final DeferredBlock<WallHangingSignBlock> AZALEA_WALL_HANGING_SIGN = BLOCKS.registerBlock("azalea_wall_hanging_sign",
            p -> new WallHangingSignBlock(AZALEA_WOOD_TYPE, p.overrideLootTable(AZALEA_HANGING_SIGN.get().getLootTable())), () -> signProperties(Blocks.OAK_WALL_HANGING_SIGN));

    public static final DeferredBlock<ShelfBlock> AZALEA_SHELF = BLOCKS.registerBlock("azalea_shelf",
            AzaleaShelfBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SHELF)
                    .strength(2.0F, 3.0F).mapColor(MapColor.COLOR_LIGHT_GREEN).pushReaction(PushReaction.BLOCK));
    private static BlockBehaviour.Properties signProperties(Block template) {
        return BlockBehaviour.Properties.ofFullCopy(template).strength(1.0F, 1.0F)
                .mapColor(MapColor.COLOR_LIGHT_GREEN).pushReaction(PushReaction.BLOCK);
    }


    private static void blockItems() {
        ITEMS.registerSimpleBlockItem(AZALEA_LOG);
        ITEMS.registerSimpleBlockItem(AZALEA_WOOD);
        ITEMS.registerSimpleBlockItem(STRIPPED_AZALEA_LOG);
        ITEMS.registerSimpleBlockItem(STRIPPED_AZALEA_WOOD);
        ITEMS.registerSimpleBlockItem(AZALEA_PLANKS);
        ITEMS.registerSimpleBlockItem(AZALEA_STAIRS);
        ITEMS.registerSimpleBlockItem(AZALEA_SLAB);
        ITEMS.registerSimpleBlockItem(AZALEA_FENCE);
        ITEMS.registerSimpleBlockItem(AZALEA_FENCE_GATE);
        ITEMS.registerSimpleBlockItem(AZALEA_DOOR);
        ITEMS.registerSimpleBlockItem(AZALEA_TRAPDOOR);
        ITEMS.registerSimpleBlockItem(AZALEA_PRESSURE_PLATE);
        ITEMS.registerSimpleBlockItem(AZALEA_BUTTON);
        ITEMS.registerItem("azalea_sign", p -> new net.minecraft.world.item.SignItem(AZALEA_SIGN.get(), AZALEA_WALL_SIGN.get(), p.stacksTo(16).useBlockDescriptionPrefix()));
        ITEMS.registerItem("azalea_hanging_sign", p -> new net.minecraft.world.item.HangingSignItem(AZALEA_HANGING_SIGN.get(), AZALEA_WALL_HANGING_SIGN.get(), p.stacksTo(16).useBlockDescriptionPrefix()));
        ITEMS.registerSimpleBlockItem(AZALEA_SHELF, () -> new Item.Properties().component(net.minecraft.core.component.DataComponents.CONTAINER, net.minecraft.world.item.component.ItemContainerContents.EMPTY));
        ITEMS.registerSimpleBlockItem(KILN);
    }
    public static void register(IEventBus bus) { BLOCKS.register(bus); blockItems(); ITEMS.register(bus); }
}
