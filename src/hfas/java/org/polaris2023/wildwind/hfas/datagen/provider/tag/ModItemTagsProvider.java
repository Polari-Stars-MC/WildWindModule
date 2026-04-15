package org.polaris2023.wildwind.hfas.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

/**
 * 物品标签生成器
 * 为木质物品添加原版和模组标签
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key(), HFASMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // ==================== 焚烬木物品标签 ====================
        registerWoodItemTags(
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

        // ==================== 灵焰木物品标签 ====================
        registerWoodItemTags(
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

        // ==================== 杜鹃木物品标签 ====================
        registerWoodItemTags(
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
    }

    /**
     * 注册木质物品标签
     */
    private void registerWoodItemTags(
            Block log, Block wood,
            Block strippedLog, Block strippedWood,
            Block planks, Block stairs, Block slab,
            Block fence, Block fenceGate,
            Block door, Block trapdoor,
            Block pressurePlate, Block button,
            Block leaves, Block sapling) {

        // 原木物品标签
        tag(ItemTags.LOGS)
                .add(log.asItem())
                .add(wood.asItem())
                .add(strippedLog.asItem())
                .add(strippedWood.asItem());

        // 木板物品标签
        tag(ItemTags.PLANKS)
                .add(planks.asItem());

        // 木质楼梯物品标签
        tag(ItemTags.WOODEN_STAIRS)
                .add(stairs.asItem());

        // 木质台阶物品标签
        tag(ItemTags.WOODEN_SLABS)
                .add(slab.asItem());

        // 木质栅栏物品标签
        tag(Tags.Items.FENCES_WOODEN)
                .add(fence.asItem());

        // 栅栏物品标签
        tag(Tags.Items.FENCES)
                .add(fence.asItem());

        // 木质栅栏门物品标签
        tag(Tags.Items.FENCE_GATES_WOODEN)
                .add(fenceGate.asItem());

        // 栅栏门物品标签
        tag(ItemTags.FENCE_GATES)
                .add(fenceGate.asItem());

        // 木质门物品标签
        tag(ItemTags.WOODEN_DOORS)
                .add(door.asItem());

        // 门物品标签
        tag(ItemTags.DOORS)
                .add(door.asItem());

        // 活板门物品标签
        tag(ItemTags.TRAPDOORS)
                .add(trapdoor.asItem());

        // 木质活板门物品标签
        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(trapdoor.asItem());

        // 木质压力板物品标签
        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(pressurePlate.asItem());

        // 木质按钮物品标签
        tag(ItemTags.WOODEN_BUTTONS)
                .add(button.asItem());

        // 按钮物品标签
        tag(ItemTags.BUTTONS)
                .add(button.asItem());

        // 树叶物品标签
        if (leaves != null) {
            tag(ItemTags.LEAVES)
                    .add(leaves.asItem());
        }

        // 树苗物品标签
        if (sapling != null) {
            tag(ItemTags.SAPLINGS)
                    .add(sapling.asItem());
        }
    }
}
