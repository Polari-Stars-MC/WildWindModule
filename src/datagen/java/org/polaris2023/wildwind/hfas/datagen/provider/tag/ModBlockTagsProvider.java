package org.polaris2023.wildwind.hfas.datagen.provider.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.polaris2023.wildwind.wilderness.tag.ModBlockTags;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 方块标签生成器
 * 为木质方块添加原版和模组标签
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModBlockTagsProvider extends BlockTagsProvider {
    private final ReleaseModule module;


    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ReleaseModule module) {
        super(output, lookupProvider, module.id());
        this.module = module;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        if (module == ReleaseModule.CORE) {
            addToTag(BlockTags.MINEABLE_WITH_PICKAXE, org.polaris2023.wildwind.core.block.ModBlocks.KILN.get());
            addToTag(BlockTags.MINEABLE_WITH_AXE, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get());
            addToTag(BlockTags.STANDING_SIGNS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get());
            addToTag(BlockTags.WALL_SIGNS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN.get());
            addToTag(BlockTags.CEILING_HANGING_SIGNS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get());
            addToTag(BlockTags.WALL_HANGING_SIGNS, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
            addToTag(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK, net.minecraft.resources.Identifier.withDefaultNamespace("wooden_shelves")), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get());
        }
        // ==================== 焚烬木标签 ====================
        registerWoodTags(
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING.get()
        );

        // ==================== 灵焰木标签 ====================
        registerWoodTags(
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING.get()
        );

        // ==================== 杜鹃木标签 ====================
        registerWoodTags(
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON.get(),
                null, // 杜鹃木使用原版树叶
                null  // 杜鹃木使用原版树苗
        );

        if (module == ReleaseModule.WILDERNESS) {
            // ==================== 其他方块标签 ====================
            // 焦灰草方块 - 泥土类
            addToTag(BlockTags.DIRT, org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT.get());

            // ==================== 实体生成有关 ====================

            addToTag(ModBlockTags.EntityAbout.MUDCRAB_SPAWNABLE_ON, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS);

            addToTag(ModBlockTags.EntityAbout.MUDCRAB_SPAWNABLE_IN_WATER_GROUND, Blocks.CLAY, Blocks.MUD, Blocks.DIRT, Blocks.MUDDY_MANGROVE_ROOTS)
                    .addTag(BlockTags.SAND)
                    .addTag(Tags.Blocks.GRAVELS);

            addToTag(ModBlockTags.EntityAbout.MUDCRAB_PREFERRED_WANDER_BLOCKS,Blocks.MANGROVE_LOG, Blocks.MANGROVE_ROOTS, Blocks.MUDDY_MANGROVE_ROOTS);
        }
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
        if (!module.owns(log)) return;

        // 原木标签 - 用于原木类方块
        addToTag(BlockTags.LOGS, log, wood, strippedLog, strippedWood);

        // 木板标签
        addToTag(BlockTags.PLANKS, planks);

        // 木质楼梯
        addToTag(BlockTags.WOODEN_STAIRS, stairs);

        // 木质台阶
        addToTag(BlockTags.WOODEN_SLABS, slab);

        // 木质栅栏
        addToTag(Tags.Blocks.FENCES_WOODEN, fence);

        // 栅栏标签（所有栅栏）
        // 已默认含有引用"#c:fences/wooden"
//        addToTag(Tags.Blocks.FENCES, fence);

        addToTag(Tags.Blocks.FENCE_GATES_WOODEN, fenceGate);

        // 栅栏门标签（所有栅栏门）
        // 已默认含有引用"#c:fence_gates/wooden"
        // bugjump你真是个尤物物品tag不引用方块又想起来了
//        addToTag(BlockTags.FENCE_GATES)
//                .add(fenceGate);

        // 木质门
        addToTag(BlockTags.WOODEN_DOORS, door);

        // 门标签（所有门）
        // byd这又不引用了
        addToTag(BlockTags.DOORS, door);

        // 活板门
        addToTag(BlockTags.TRAPDOORS, trapdoor);

        // 木质活板门
        addToTag(BlockTags.WOODEN_TRAPDOORS, trapdoor);

        // 木质压力板
        // 压力板tag已引用
        addToTag(BlockTags.WOODEN_PRESSURE_PLATES, pressurePlate);

        // 木质按钮
        addToTag(BlockTags.WOODEN_BUTTONS, button);

        // 按钮标签（所有按钮）
        // 已引用"#minecraft:wooden_buttons"
//        addToTag(BlockTags.BUTTONS)
//                .add(button);

        // 树叶
        if (leaves != null) {
            addToTag(BlockTags.LEAVES, leaves);
        }

        // 树苗
        if (sapling != null) {
            /*
              由addToTag方法桥接。
              TODO_FINISHED: 该block tag key存在缺失问题，已模仿原版行为进行处理，但无法确定这种缺失是否系系统性删除，无法确保对应功能一定可以正常工作。请求测试阶段重点检查。
               ——landis, 2026/9/1
               */
            addToTag(BlockItemTags.SAPLINGS.block(), sapling);
        }

        // 挖掘工具标签
        addToTag(BlockTags.MINEABLE_WITH_AXE, log, wood, strippedLog, strippedWood, planks, stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button);

        // 站立信号（原木可以站立）
        addToTag(BlockTags.OVERWORLD_NATURAL_LOGS, log);

        // 需要工具
        addToTag(BlockTags.NEEDS_STONE_TOOL, log, wood, strippedLog, strippedWood, planks, stairs, slab, fence, fenceGate, door, trapdoor, pressurePlate, button);
    }

    @SuppressWarnings("all")
    public TagAppender<Block> addToTag(TagKey<Block> tag, Block... items) {
        if (items.length == 0) return tag(tag);
        return tag(tag).addAll(Arrays.stream(items).map(Block::builtInRegistryHolder).map(Holder.Reference::getKey));
    }
}
