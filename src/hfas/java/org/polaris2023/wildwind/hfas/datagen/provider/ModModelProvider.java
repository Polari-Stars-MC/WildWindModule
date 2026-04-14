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
        // 焚烬木树叶 - 橙色染色
        blockModels.createTintedLeaves(ModBlocks.BLAZE_LEAVES.get(), TexturedModel.LEAVES, 0xFF8000);
        // 焚烬木树苗
        blockModels.createCrossBlock(ModBlocks.BLAZE_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // 灵焰木树叶 - 青色染色
        blockModels.createTintedLeaves(ModBlocks.SOUL_LEAVES.get(), TexturedModel.LEAVES, 0x00FFFF);
        // 灵焰木树苗
        blockModels.createCrossBlock(ModBlocks.SOUL_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

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
