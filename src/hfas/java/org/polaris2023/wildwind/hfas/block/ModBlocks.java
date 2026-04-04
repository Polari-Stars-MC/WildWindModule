package org.polaris2023.wildwind.hfas.block;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.hfas.HFASMod;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("deprecation")
public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(HFASMod.MOD_ID);

    public static final List<WoodSet> WOOD_SETS = new ArrayList<>();

    public static final WoodSet CINDER = registerWoodSet(
            "cinder",
            MapColor.PODZOL,
            MapColor.COLOR_BROWN,
            MapColor.COLOR_BROWN,
            ModTreeGrower.CINDER.get(),
            ModBlockSetType.CINDER.get(),
            ModWoodType.CINDER.get()
    );

    public static final WoodSet EMBER = registerWoodSet(
            "ember",
            MapColor.COLOR_ORANGE,
            MapColor.TERRACOTTA_ORANGE,
            MapColor.COLOR_ORANGE,
            ModTreeGrower.EMBER.get(),
            ModBlockSetType.EMBER.get(),
            ModWoodType.EMBER.get()
    );



    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private static WoodSet registerWoodSet(
            String name,
            MapColor barkColor,
            MapColor woodColor,
            MapColor plankColor,
            net.minecraft.world.level.block.grower.TreeGrower treeGrower,
            BlockSetType blockSetType,
            WoodType woodType
    ) {
        DeferredBlock<Block> log = BLOCKS.register(name + "_log", () -> log(MapColor.WOOD, barkColor));
        DeferredBlock<Block> wood = BLOCKS.register(name + "_wood", () -> log(barkColor, barkColor));
        DeferredBlock<Block> strippedLog = BLOCKS.register(
                "stripped_" + name + "_log",
                () -> log(MapColor.WOOD, woodColor)
        );
        DeferredBlock<Block> strippedWood = BLOCKS.register(
                "stripped_" + name + "_wood",
                () -> log(woodColor, woodColor)
        );
        DeferredBlock<Block> leaves = BLOCKS.register(name + "_leaves", ModBlocks::leaves);
        DeferredBlock<Block> planks = BLOCKS.register(name + "_planks", () -> planks(plankColor));
        DeferredBlock<Block> stairs = BLOCKS.register(name + "_stairs", () -> legacyStair(planks.get()));
        DeferredBlock<Block> slab = BLOCKS.register(name + "_slab", () -> slab(plankColor));
        DeferredBlock<Block> fence = BLOCKS.register(name + "_fence", () -> fence(plankColor));
        DeferredBlock<Block> fenceGate = BLOCKS.register(name + "_fence_gate", () -> fenceGate(woodType, plankColor));
        DeferredBlock<Block> door = BLOCKS.register(name + "_door", () -> door(blockSetType, plankColor));
        DeferredBlock<Block> trapdoor = BLOCKS.register(name + "_trapdoor", () -> trapdoor(blockSetType, plankColor));
        DeferredBlock<Block> pressurePlate = BLOCKS.register(
                name + "_pressure_plate",
                () -> pressurePlate(blockSetType, plankColor)
        );
        DeferredBlock<Block> button = BLOCKS.register(name + "_button", () -> woodenButton(blockSetType));
        DeferredBlock<Block> sapling = BLOCKS.register(name + "_sapling", () -> sapling(treeGrower));
        DeferredBlock<Block> pottedSapling = BLOCKS.register(
                "potted_" + name + "_sapling",
                () -> pottedSapling(sapling)
        );
        DeferredBlock<Block> sign = BLOCKS.register(name + "_sign", () -> sign(woodType, plankColor));
        DeferredBlock<Block> wallSign = BLOCKS.register(name + "_wall_sign", () -> wallSign(woodType, plankColor));
        DeferredBlock<Block> hangingSign = BLOCKS.register(
                name + "_hanging_sign",
                () -> hangingSign(woodType, plankColor)
        );
        DeferredBlock<Block> wallHangingSign = BLOCKS.register(
                name + "_wall_hanging_sign",
                () -> wallHangingSign(woodType, plankColor)
        );
        WoodSet set = new WoodSet(
                name,
                log,
                wood,
                strippedLog,
                strippedWood,
                leaves,
                planks,
                stairs,
                slab,
                fence,
                fenceGate,
                door,
                trapdoor,
                pressurePlate,
                button,
                sapling,
                pottedSapling,
                sign,
                wallSign,
                hangingSign,
                wallHangingSign
        );
        WOOD_SETS.add(set);
        return set;
    }


    private static Block log(MapColor topMapColor, MapColor sideMapColor) {
        return new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }

    private static Block leaves() {
        return new TintedParticleLeavesBlock(
                0.01F,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isValidSpawn(Blocks::ocelotOrParrot)
                        .isSuffocating(ModBlocks::never)
                        .isViewBlocking(ModBlocks::never)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(ModBlocks::never)
        );
    }

    private static Block planks(MapColor mapColor) {
        return new Block(baseWoodProperties(mapColor));
    }

    private static Block legacyStair(Block baseBlock) {
        return new StairBlock(baseBlock.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(baseBlock));
    }

    private static Block slab(MapColor mapColor) {
        return new SlabBlock(baseWoodProperties(mapColor));
    }

    private static Block fence(MapColor mapColor) {
        return new FenceBlock(baseWoodProperties(mapColor));
    }

    private static Block fenceGate(WoodType woodType, MapColor mapColor) {
        return new FenceGateBlock(woodType, baseWoodProperties(mapColor));
    }

    private static Block door(BlockSetType blockSetType, MapColor mapColor) {
        return new DoorBlock(
                blockSetType,
                BlockBehaviour.Properties.of()
                        .mapColor(mapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(3.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block trapdoor(BlockSetType blockSetType, MapColor mapColor) {
        return new TrapDoorBlock(blockSetType, baseWoodProperties(mapColor).noOcclusion());
    }

    private static Block pressurePlate(BlockSetType blockSetType, MapColor mapColor) {
        return new PressurePlateBlock(blockSetType, baseWoodProperties(mapColor));
    }

    private static Block woodenButton(BlockSetType type) {
        return new ButtonBlock(
                type,
                30,
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .strength(0.5F)
                        .sound(SoundType.WOOD)
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block sapling(net.minecraft.world.level.block.grower.TreeGrower treeGrower) {
        return new SaplingBlock(
                treeGrower,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block pottedSapling(DeferredBlock<Block> sapling) {
        return new FlowerPotBlock(
                () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                sapling,
                BlockBehaviour.Properties.of()
                        .instabreak()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
        );
    }

    private static Block sign(WoodType woodType, MapColor mapColor) {
        return new StandingSignBlock(woodType, BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_SIGN).mapColor(mapColor));
    }

    private static Block wallSign(WoodType woodType, MapColor mapColor) {
        return new WallSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_SIGN).mapColor(mapColor)
                // no loot From sign
        );
    }

    private static Block hangingSign(WoodType woodType, MapColor mapColor) {
        return new CeilingHangingSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN).mapColor(mapColor)
        );
    }

    private static Block wallHangingSign(
            WoodType woodType,
            MapColor mapColor
    ) {
        return new WallHangingSignBlock(
                woodType,
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(mapColor)
                // no loot From hanging sign
        );
    }

    private static BlockBehaviour.Properties baseWoodProperties(MapColor mapColor) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
