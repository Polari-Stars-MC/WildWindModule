package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.ModWoodFamilies;
import org.polaris2023.wildwind.hfas.block.WoodFamily;
import org.polaris2023.wildwind.hfas.registry.ModItems;

import java.util.function.Supplier;

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
        // ==================== 三种木材套件 ====================
        for (WoodFamily family : ModWoodFamilies.ALL) {
            registerWoodModels(blockModels, family);
        }

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

        // ==================== 独立物品 ====================
        simpleItem(itemModels, ModItems.CRAB_CLAW);

        // 万用蟹钳 - 使用手写 3D 模型（resources 下的 models/item/omni_claw.json），仅生成物品模型定义
        itemModels.itemModelOutput.accept(
                ModItems.OMNI_CLAW.get(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModItems.OMNI_CLAW.get()))
        );
        simpleItem(itemModels, ModItems.MUDCRAB_BUCKET);
        simpleItem(itemModels, ModItems.MUDCRAB_SPAWN_EGG);

        // ==================== 食人鱼 ====================
        registerPiranhaModels(itemModels);
    }

    /**
     * 注册一种木材的方块模型（含树叶与树苗）
     */
    private void registerWoodModels(BlockModelGenerators blockModels, WoodFamily family) {
        // 原木（自动生成物品模型）
        blockModels.woodProvider(family.log().get()).logWithHorizontal(family.log().get()).wood(family.wood().get());

        // 去皮原木（自动生成物品模型）
        blockModels.woodProvider(family.strippedLog().get())
                .logWithHorizontal(family.strippedLog().get())
                .wood(family.strippedWood().get());

        // 木质方块套件（包括木板，自动生成物品模型）
        blockModels.family(family.planks().get())
                .stairs(family.stairs().get())
                .slab(family.slab().get())
                .fence(family.fence().get())
                .fenceGate(family.fenceGate().get())
                .pressurePlate(family.pressurePlate().get())
                .button(family.button().get());

        // 门 / 活板门（自动生成物品模型）
        blockModels.createDoor(family.door().get());
        blockModels.createTrapdoor(family.trapdoor().get());

        // 树叶（物品图标使用常数染色）与树苗，杜鹃木沿用原版，没有自成套的树叶
        if (family.hasLeaves()) {
            blockModels.createTintedLeaves(family.leaves().get(), TexturedModel.LEAVES, family.leavesTint());
            blockModels.createCrossBlock(family.sapling().get(), BlockModelGenerators.PlantType.NOT_TINTED);
        }
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

    private void simpleItem(ItemModelGenerators itemModels, Supplier<? extends Item> item){
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
    }

    private void registerPiranhaModels(ItemModelGenerators itemModels) {
        simpleItem(itemModels, ModItems.PIRANHA);
        simpleItem(itemModels, ModItems.COOKED_PIRANHA);
        simpleItem(itemModels, ModItems.PIRANHA_BUCKET);
        simpleItem(itemModels, ModItems.PIRANHA_SPAWN_EGG);
        simpleItem(itemModels, ModItems.FANG);
    }
}
