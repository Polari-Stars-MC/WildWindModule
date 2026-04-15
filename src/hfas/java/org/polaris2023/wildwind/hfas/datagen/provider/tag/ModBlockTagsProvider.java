package org.polaris2023.wildwind.hfas.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

/**
 * 方块标签生成器
 * 为木质方块添加原版和模组标签
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModBlockTagsProvider extends BlockTagsProvider {

    public static ModBlockTagsProvider INSTANCE;

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, HFASMod.MOD_ID);
        INSTANCE = this;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // ==================== 焚烬木标签 ====================
        registerWoodTags(
                ModBlocks.BLAZE_LOG.get(),
                ModBlocks.BLAZE_WOOD.get(),
                ModBlocks.STRIPPED_BLAZE_LOG.get(),
                ModBlocks.STRIPPED_BLAZE_WOOD.get(),
                ModBlocks.BLAZE_PLANKS.get(),
                ModBlocks.BLAZE_STAIRS.get(),
                ModBlocks.BLAZE_SLAB.get(),
                ModBlocks.BLAZE_FENCE.get(),
                ModBlocks.BLAZE_FENCE_GATE.get(),
                ModBlocks.BLAZE_DOOR.get(),
                ModBlocks.BLAZE_TRAPDOOR.get(),
                ModBlocks.BLAZE_PRESSURE_PLATE.get(),
                ModBlocks.BLAZE_BUTTON.get(),
                ModBlocks.BLAZE_LEAVES.get(),
                ModBlocks.BLAZE_SAPLING.get()
        );

        // ==================== 灵焰木标签 ====================
        registerWoodTags(
                ModBlocks.SOUL_LOG.get(),
                ModBlocks.SOUL_WOOD.get(),
                ModBlocks.STRIPPED_SOUL_LOG.get(),
                ModBlocks.STRIPPED_SOUL_WOOD.get(),
                ModBlocks.SOUL_PLANKS.get(),
                ModBlocks.SOUL_STAIRS.get(),
                ModBlocks.SOUL_SLAB.get(),
                ModBlocks.SOUL_FENCE.get(),
                ModBlocks.SOUL_FENCE_GATE.get(),
                ModBlocks.SOUL_DOOR.get(),
                ModBlocks.SOUL_TRAPDOOR.get(),
                ModBlocks.SOUL_PRESSURE_PLATE.get(),
                ModBlocks.SOUL_BUTTON.get(),
                ModBlocks.SOUL_LEAVES.get(),
                ModBlocks.SOUL_SAPLING.get()
        );

        // ==================== 杜鹃木标签 ====================
        registerWoodTags(
                ModBlocks.AZALEA_LOG.get(),
                ModBlocks.AZALEA_WOOD.get(),
                ModBlocks.STRIPPED_AZALEA_LOG.get(),
                ModBlocks.STRIPPED_AZALEA_WOOD.get(),
                ModBlocks.AZALEA_PLANKS.get(),
                ModBlocks.AZALEA_STAIRS.get(),
                ModBlocks.AZALEA_SLAB.get(),
                ModBlocks.AZALEA_FENCE.get(),
                ModBlocks.AZALEA_FENCE_GATE.get(),
                ModBlocks.AZALEA_DOOR.get(),
                ModBlocks.AZALEA_TRAPDOOR.get(),
                ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                ModBlocks.AZALEA_BUTTON.get(),
                null, // 杜鹃木使用原版树叶
                null  // 杜鹃木使用原版树苗
        );

        // ==================== 其他方块标签 ====================
        // 焦灰草方块 - 泥土类
        tag(BlockTags.DIRT)
                .add(ModBlocks.SCORCHED_GRASS_BLOCK.get())
                .add(ModBlocks.SCORCHED_DIRT.get());

        // 制箭台 - 工作站
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.FLETCHING_TABLE.get());
    }

    /**
     * 注册木质方块标签
     */
    private void registerWoodTags(
            Block log, Block wood,
            Block strippedLog, Block strippedWood,
            Block planks, Block stairs, Block slab,
            Block fence, Block fenceGate,
            Block door, Block trapdoor,
            Block pressurePlate, Block button,
            Block leaves, Block sapling) {

        // 原木标签 - 用于原木类方块
        tag(BlockTags.LOGS)
                .add(log)
                .add(wood)
                .add(strippedLog)
                .add(strippedWood);

        // 木板标签
        tag(BlockTags.PLANKS)
                .add(planks);

        // 木质楼梯
        tag(BlockTags.WOODEN_STAIRS)
                .add(stairs);

        // 木质台阶
        tag(BlockTags.WOODEN_SLABS)
                .add(slab);

        // 木质栅栏
        tag(Tags.Blocks.FENCES_WOODEN)
                .add(fence);

        // 栅栏标签（所有栅栏）
        tag(Tags.Blocks.FENCES)
                .add(fence);

        tag(Tags.Blocks.FENCE_GATES_WOODEN)
                .add(fenceGate);

        // 栅栏门标签（所有栅栏门）
        tag(BlockTags.FENCE_GATES)
                .add(fenceGate);

        // 木质门
        tag(BlockTags.WOODEN_DOORS)
                .add(door);

        // 门标签（所有门）
        tag(BlockTags.DOORS)
                .add(door);

        // 活板门
        tag(BlockTags.TRAPDOORS)
                .add(trapdoor);

        // 木质活板门
        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(trapdoor);

        // 木质压力板
        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(pressurePlate);

        // 木质按钮
        tag(BlockTags.WOODEN_BUTTONS)
                .add(button);

        // 按钮标签（所有按钮）
        tag(BlockTags.BUTTONS)
                .add(button);

        // 树叶
        if (leaves != null) {
            tag(BlockTags.LEAVES)
                    .add(leaves);
        }

        // 树苗
        if (sapling != null) {
            tag(BlockTags.SAPLINGS)
                    .add(sapling);
        }

        // 挖掘工具标签
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(log, wood, strippedLog, strippedWood)
                .add(planks, stairs, slab, fence, fenceGate)
                .add(door, trapdoor, pressurePlate, button);

        // 站立信号（原木可以站立）
        tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(log);

        // 需要工具
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(log, wood, strippedLog, strippedWood)
                .add(planks, stairs, slab, fence, fenceGate)
                .add(door, trapdoor, pressurePlate, button);
    }
}
