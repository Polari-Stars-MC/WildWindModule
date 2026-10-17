﻿﻿﻿﻿﻿﻿package org.polaris2023.wildwind.hfas.block;

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
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.worldgen.ModTreeGrowers;

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
    // 方块注册器
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HFASMod.MOD_ID);
    // 物品注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HFASMod.MOD_ID);

    // ==================== 方块类型定义 ====================



    // 杜鹃木方块类型
    public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType("azalea"));
    // 杜鹃木木材类型
    public static final WoodType AZALEA_WOOD_TYPE = WoodType.register(new WoodType("azalea", AZALEA_BLOCK_SET));







    // 焚烬木木板
    public static final DeferredBlock<Block> BLAZE_PLANKS = BLOCKS.registerBlock(
            "blaze_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木楼梯
    public static final DeferredBlock<StairBlock> BLAZE_STAIRS = BLOCKS.registerBlock(
            "blaze_stairs",
            p -> new StairBlock(BLAZE_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木台阶
    public static final DeferredBlock<SlabBlock> BLAZE_SLAB = BLOCKS.registerBlock(
            "blaze_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木栅栏
    public static final DeferredBlock<FenceBlock> BLAZE_FENCE = BLOCKS.registerBlock(
            "blaze_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木栅栏门
    public static final DeferredBlock<FenceGateBlock> BLAZE_FENCE_GATE = BLOCKS.registerBlock(
            "blaze_fence_gate",
            p -> new FenceGateBlock(BLAZE_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木门
    public static final DeferredBlock<DoorBlock> BLAZE_DOOR = BLOCKS.registerBlock(
            "blaze_door",
            p -> new DoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木活板门
    public static final DeferredBlock<TrapDoorBlock> BLAZE_TRAPDOOR = BLOCKS.registerBlock(
            "blaze_trapdoor",
            p -> new TrapDoorBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木压力板
    public static final DeferredBlock<PressurePlateBlock> BLAZE_PRESSURE_PLATE = BLOCKS.registerBlock(
            "blaze_pressure_plate",
            p -> new PressurePlateBlock(BLAZE_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木按钮
    public static final DeferredBlock<ButtonBlock> BLAZE_BUTTON = BLOCKS.registerBlock(
            "blaze_button",
            p -> new ButtonBlock(BLAZE_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 焚烬木树叶
    public static final DeferredBlock<LeavesBlock> BLAZE_LEAVES = BLOCKS.registerBlock(
            "blaze_leaves",
            p -> new TintedParticleLeavesBlock(0.01F,p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // 焚烬木树苗
    public static final DeferredBlock<SaplingBlock> BLAZE_SAPLING = BLOCKS.registerBlock(
            "blaze_sapling",
            p -> new SaplingBlock(ModTreeGrowers.BLAZE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );



    // 去皮灵焰木原木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_LOG = BLOCKS.registerBlock(
            "stripped_soul_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮灵焰木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_SOUL_WOOD = BLOCKS.registerBlock(
            "stripped_soul_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木木板
    public static final DeferredBlock<Block> SOUL_PLANKS = BLOCKS.registerBlock(
            "soul_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木楼梯
    public static final DeferredBlock<StairBlock> SOUL_STAIRS = BLOCKS.registerBlock(
            "soul_stairs",
            p -> new StairBlock(SOUL_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木台阶
    public static final DeferredBlock<SlabBlock> SOUL_SLAB = BLOCKS.registerBlock(
            "soul_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木栅栏
    public static final DeferredBlock<FenceBlock> SOUL_FENCE = BLOCKS.registerBlock(
            "soul_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木栅栏门
    public static final DeferredBlock<FenceGateBlock> SOUL_FENCE_GATE = BLOCKS.registerBlock(
            "soul_fence_gate",
            p -> new FenceGateBlock(SOUL_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木门
    public static final DeferredBlock<DoorBlock> SOUL_DOOR = BLOCKS.registerBlock(
            "soul_door",
            p -> new DoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木活板门
    public static final DeferredBlock<TrapDoorBlock> SOUL_TRAPDOOR = BLOCKS.registerBlock(
            "soul_trapdoor",
            p -> new TrapDoorBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木压力板
    public static final DeferredBlock<PressurePlateBlock> SOUL_PRESSURE_PLATE = BLOCKS.registerBlock(
            "soul_pressure_plate",
            p -> new PressurePlateBlock(SOUL_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木按钮
    public static final DeferredBlock<ButtonBlock> SOUL_BUTTON = BLOCKS.registerBlock(
            "soul_button",
            p -> new ButtonBlock(SOUL_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 灵焰木树叶
    public static final DeferredBlock<LeavesBlock> SOUL_LEAVES = BLOCKS.registerBlock(
            "soul_leaves",
            p -> new TintedParticleLeavesBlock(0.01F, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // 灵焰木树苗
    public static final DeferredBlock<SaplingBlock> SOUL_SAPLING = BLOCKS.registerBlock(
            "soul_sapling",
            p -> new SaplingBlock(ModTreeGrowers.SOUL, p
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // ==================== 杜鹃木套件 (azalea_wood) ====================

    // 杜鹃木原木
    public static final DeferredBlock<RotatedPillarBlock> AZALEA_LOG = BLOCKS.registerBlock(
            "azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木
    public static final DeferredBlock<RotatedPillarBlock> AZALEA_WOOD = BLOCKS.registerBlock(
            "azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮杜鹃木原木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_LOG = BLOCKS.registerBlock(
            "stripped_azalea_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 去皮杜鹃木
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_WOOD = BLOCKS.registerBlock(
            "stripped_azalea_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木木板
    public static final DeferredBlock<Block> AZALEA_PLANKS = BLOCKS.registerBlock(
            "azalea_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木楼梯
    public static final DeferredBlock<StairBlock> AZALEA_STAIRS = BLOCKS.registerBlock(
            "azalea_stairs",
            p -> new StairBlock(AZALEA_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木台阶
    public static final DeferredBlock<SlabBlock> AZALEA_SLAB = BLOCKS.registerBlock(
            "azalea_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木栅栏
    public static final DeferredBlock<FenceBlock> AZALEA_FENCE = BLOCKS.registerBlock(
            "azalea_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木栅栏门
    public static final DeferredBlock<FenceGateBlock> AZALEA_FENCE_GATE = BLOCKS.registerBlock(
            "azalea_fence_gate",
            p -> new FenceGateBlock(AZALEA_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木门
    public static final DeferredBlock<DoorBlock> AZALEA_DOOR = BLOCKS.registerBlock(
            "azalea_door",
            p -> new DoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木活板门
    public static final DeferredBlock<TrapDoorBlock> AZALEA_TRAPDOOR = BLOCKS.registerBlock(
            "azalea_trapdoor",
            p -> new TrapDoorBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木压力板
    public static final DeferredBlock<PressurePlateBlock> AZALEA_PRESSURE_PLATE = BLOCKS.registerBlock(
            "azalea_pressure_plate",
            p -> new PressurePlateBlock(AZALEA_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // 杜鹃木按钮
    public static final DeferredBlock<ButtonBlock> AZALEA_BUTTON = BLOCKS.registerBlock(
            "azalea_button",
            p -> new ButtonBlock(AZALEA_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );
    // ����ľԭľ
    public static final DeferredBlock<RotatedPillarBlock> CINDER_LOG = BLOCKS.registerBlock(
            "cinder_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ
    public static final DeferredBlock<RotatedPillarBlock> CINDER_WOOD = BLOCKS.registerBlock(
            "cinder_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ȥƤ����ľԭľ
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CINDER_LOG = BLOCKS.registerBlock(
            "stripped_cinder_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ȥƤ����ľ
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CINDER_WOOD = BLOCKS.registerBlock(
            "stripped_cinder_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľľ��
    public static final DeferredBlock<Block> CINDER_PLANKS = BLOCKS.registerBlock(
            "cinder_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľԭľ
    public static final DeferredBlock<RotatedPillarBlock> EMBER_LOG = BLOCKS.registerBlock(
            "ember_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ
    public static final DeferredBlock<RotatedPillarBlock> EMBER_WOOD = BLOCKS.registerBlock(
            "ember_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ȥƤ���ľԭľ
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EMBER_LOG = BLOCKS.registerBlock(
            "stripped_ember_log",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ȥƤ���ľ
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EMBER_WOOD = BLOCKS.registerBlock(
            "stripped_ember_wood",
            p -> new RotatedPillarBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľľ��
    public static final DeferredBlock<Block> EMBER_PLANKS = BLOCKS.registerBlock(
            "ember_planks",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ¥��
    public static final DeferredBlock<StairBlock> CINDER_STAIRS = BLOCKS.registerBlock(
            "cinder_stairs",
            p -> new StairBlock(CINDER_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ̨��
    public static final DeferredBlock<SlabBlock> CINDER_SLAB = BLOCKS.registerBlock(
            "cinder_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľդ��
    public static final DeferredBlock<FenceBlock> CINDER_FENCE = BLOCKS.registerBlock(
            "cinder_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľդ����
    public static final DeferredBlock<FenceGateBlock> CINDER_FENCE_GATE = BLOCKS.registerBlock(
            "cinder_fence_gate",
            p -> new FenceGateBlock(CINDER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ��
    public static final DeferredBlock<DoorBlock> CINDER_DOOR = BLOCKS.registerBlock(
            "cinder_door",
            p -> new DoorBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ�����
    public static final DeferredBlock<TrapDoorBlock> CINDER_TRAPDOOR = BLOCKS.registerBlock(
            "cinder_trapdoor",
            p -> new TrapDoorBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ����ľѹ����
    public static final DeferredBlock<PressurePlateBlock> CINDER_PRESSURE_PLATE = BLOCKS.registerBlock(
            "cinder_pressure_plate",
            p -> new PressurePlateBlock(CINDER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ��ť
    public static final DeferredBlock<ButtonBlock> CINDER_BUTTON = BLOCKS.registerBlock(
            "cinder_button",
            p -> new ButtonBlock(CINDER_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ����ľ��Ҷ
    public static final DeferredBlock<LeavesBlock> CINDER_LEAVES = BLOCKS.registerBlock(
            "cinder_leaves",
            p -> new LeavesBlock(p
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // ����ľ����
    public static final DeferredBlock<SaplingBlock> CINDER_SAPLING = BLOCKS.registerBlock(
            "cinder_sapling",
            p -> new SaplingBlock(org.polaris2023.wildwind.hfas.worldgen.ModTreeGrowers.CINDER, p
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // ���ľ¥��
    public static final DeferredBlock<StairBlock> EMBER_STAIRS = BLOCKS.registerBlock(
            "ember_stairs",
            p -> new StairBlock(EMBER_PLANKS.get().defaultBlockState(), p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ̨��
    public static final DeferredBlock<SlabBlock> EMBER_SLAB = BLOCKS.registerBlock(
            "ember_slab",
            p -> new SlabBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľդ��
    public static final DeferredBlock<FenceBlock> EMBER_FENCE = BLOCKS.registerBlock(
            "ember_fence",
            p -> new FenceBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľդ����
    public static final DeferredBlock<FenceGateBlock> EMBER_FENCE_GATE = BLOCKS.registerBlock(
            "ember_fence_gate",
            p -> new FenceGateBlock(EMBER_WOOD_TYPE, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ��
    public static final DeferredBlock<DoorBlock> EMBER_DOOR = BLOCKS.registerBlock(
            "ember_door",
            p -> new DoorBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ�����
    public static final DeferredBlock<TrapDoorBlock> EMBER_TRAPDOOR = BLOCKS.registerBlock(
            "ember_trapdoor",
            p -> new TrapDoorBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD))
    );

    // ���ľѹ����
    public static final DeferredBlock<PressurePlateBlock> EMBER_PRESSURE_PLATE = BLOCKS.registerBlock(
            "ember_pressure_plate",
            p -> new PressurePlateBlock(EMBER_BLOCK_SET, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ��ť
    public static final DeferredBlock<ButtonBlock> EMBER_BUTTON = BLOCKS.registerBlock(
            "ember_button",
            p -> new ButtonBlock(EMBER_BLOCK_SET, 30, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5F)
                    .sound(SoundType.WOOD))
    );

    // ���ľ��Ҷ
    public static final DeferredBlock<LeavesBlock> EMBER_LEAVES = BLOCKS.registerBlock(
            "ember_leaves",
            p -> new LeavesBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2F)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .randomTicks())
    );

    // ���ľ����
    public static final DeferredBlock<SaplingBlock> EMBER_SAPLING = BLOCKS.registerBlock(
            "ember_sapling",
            p -> new SaplingBlock(org.polaris2023.wildwind.hfas.worldgen.ModTreeGrowers.EMBER, p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .randomTicks())
    );

    // ==================== 其他方块 ====================

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
    

    

        // 杜鹃木套件物品
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

        // 其他方块物品
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS_BLOCK);
        ITEMS.registerSimpleBlockItem(SCORCHED_DIRT);
        ITEMS.registerSimpleBlockItem(SCORCHED_GRASS);
        ITEMS.registerSimpleBlockItem(SCORCHED_TWIG);
        ITEMS.registerSimpleBlockItem(TINY_CACTUS);
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
