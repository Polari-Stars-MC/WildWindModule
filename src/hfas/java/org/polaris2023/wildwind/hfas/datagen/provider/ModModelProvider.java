package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 模型生成器
 * 用于生成方块和物品的模型文件
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, HFASMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // ==================== 焚烬木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                ModBlocks.BLAZE_BUTTON.get()
        );
        // 焚烬木树叶 - 橙色染色
        blockModels.createTintedLeaves(ModBlocks.BLAZE_LEAVES.get(), TexturedModel.LEAVES, 0xFF8000);
        // 焚烬木树苗
        blockModels.createCrossBlock(ModBlocks.BLAZE_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // ==================== 灵焰木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                ModBlocks.SOUL_BUTTON.get()
        );
        // 灵焰木树叶 - 青色染色
        blockModels.createTintedLeaves(ModBlocks.SOUL_LEAVES.get(), TexturedModel.LEAVES, 0x00FFFF);
        // 灵焰木树苗
        blockModels.createCrossBlock(ModBlocks.SOUL_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // ==================== 杜鹃木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                ModBlocks.AZALEA_BUTTON.get()
        );

        // ==================== 其他方块 ====================
        // 焦灰草方块 - 使用简单的方块模型
        createSimpleBlock(blockModels, ModBlocks.SCORCHED_GRASS_BLOCK.get());

        // 焦土 - 使用简单的方块模型
        createSimpleBlock(blockModels, ModBlocks.SCORCHED_DIRT.get());

        // 焦灰草丛 - 使用交叉模型（类似草丛）
        createCrossBlock(blockModels, ModBlocks.SCORCHED_GRASS.get());

        // 焦灰枝条 - 使用交叉模型
        createCrossBlock(blockModels, ModBlocks.SCORCHED_TWIG.get());

        // 仙人球 - 使用交叉模型
        createCrossBlock(blockModels, ModBlocks.TINY_CACTUS.get());

        // 制箭台 - 使用简单方块模型
        createSimpleBlock(blockModels, ModBlocks.FLETCHING_TABLE.get());
    }

    /**
     * 注册木质方块模型
     */
    private void registerWoodModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels,
                                     Block log, Block wood,
                                     Block strippedLog, Block strippedWood,
                                     Block planks, Block stairs, Block slab,
                                     Block fence, Block fenceGate,
                                     Block door, Block trapdoor,
                                     Block pressurePlate, Block button) {
        // 原木 - 使用 WoodProvider（自动生成物品模型）
        blockModels.woodProvider(log).logWithHorizontal(log).wood(wood);

        // 去皮原木（自动生成物品模型）
        blockModels.woodProvider(strippedLog).logWithHorizontal(strippedLog).wood(strippedWood);

        // 使用 family 创建木质方块套件（包括木板，自动生成物品模型）
        blockModels.family(planks)
                .stairs(stairs)
                .slab(slab)
                .fence(fence)
                .fenceGate(fenceGate)
                .pressurePlate(pressurePlate)
                .button(button);

        // 门（自动生成物品模型）
        blockModels.createDoor(door);

        // 活板门（自动生成物品模型）
        blockModels.createTrapdoor(trapdoor);
    }

    /**
     * 创建简单方块模型（六面相同贴图）
     */
    private void createSimpleBlock(BlockModelGenerators blockModels, Block block) {
        blockModels.createTrivialCube(block);
    }

    /**
     * 创建交叉模型（用于植物类方块）
     */
    private void createCrossBlock(BlockModelGenerators blockModels, Block block) {
        blockModels.createCrossBlock(block, BlockModelGenerators.PlantType.NOT_TINTED);
    }
}
