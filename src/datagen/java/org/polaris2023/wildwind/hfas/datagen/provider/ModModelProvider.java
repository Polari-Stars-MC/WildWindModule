package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.resources.Identifier;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.hfas.HFASMod;

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
    protected java.util.stream.Stream<? extends net.minecraft.core.Holder<Block>> getKnownBlocks() {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.listElements()
                .filter(holder -> org.polaris2023.wildwind.core.api.WildWindModules.owns(holder.key().identifier().getNamespace()));
    }

    @Override
    protected java.util.stream.Stream<? extends net.minecraft.core.Holder<Item>> getKnownItems() {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.listElements()
                .filter(holder -> org.polaris2023.wildwind.core.api.WildWindModules.owns(holder.key().identifier().getNamespace()));
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(
                org.polaris2023.wildwind.wilderness.registry.ModItems.MUDDY_PIG_SPAWN_EGG.get(),
                ItemModelUtils.plainModel(
                        Identifier.withDefaultNamespace("item/pig_spawn_egg")));
        org.polaris2023.wildwind.core.registry.ModItems.MATERIALS.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        // ==================== 焚烬木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON.get()
        );
        // 焚烬木树叶 - 橙色染色
        blockModels.createTintedLeaves(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES.get(), TexturedModel.LEAVES, 0xFF8000);
        // 焚烬木树苗
        blockModels.createCrossBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // ==================== 灵焰木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON.get()
        );
        // 灵焰木树叶 - 青色染色
        blockModels.createTintedLeaves(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES.get(), TexturedModel.LEAVES, 0x00FFFF);
        // 灵焰木树苗
        blockModels.createCrossBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // ==================== 杜鹃木套件 ====================
        registerWoodModels(blockModels, itemModels,
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
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON.get()
        );

        blockModels.createShelf(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get(), org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get());
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSign(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get(),
                signVariant("azalea_sign_rot_0"), signVariant("azalea_sign_rot_1"), signVariant("azalea_sign_rot_2"), signVariant("azalea_sign_rot_3")));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createHangingSign(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get(),
                signVariant("azalea_hanging_sign_rot_0"), signVariant("azalea_hanging_sign_rot_1"), signVariant("azalea_hanging_sign_rot_2"), signVariant("azalea_hanging_sign_rot_3"),
                signVariant("azalea_hanging_sign_attached_rot_0"), signVariant("azalea_hanging_sign_attached_rot_1"), signVariant("azalea_hanging_sign_attached_rot_2"), signVariant("azalea_hanging_sign_attached_rot_3")));
        blockModels.blockStateOutput.accept(net.minecraft.client.data.models.blockstates.MultiVariantGenerator.dispatch(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN.get(), signVariant("azalea_wall_sign"))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
        blockModels.blockStateOutput.accept(net.minecraft.client.data.models.blockstates.MultiVariantGenerator.dispatch(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN.get(), signVariant("azalea_wall_hanging_sign"))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
        itemModels.generateFlatItem(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get().asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get().asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get(), ItemModelUtils.tintedModel(
                Identifier.withDefaultNamespace("item/tipped_arrow"), new net.minecraft.client.color.item.Potion()));
        // ==================== 其他方块 ====================
        // 焦灰草方块 - 使用简单的方块模型
        createSimpleBlock(blockModels, org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK.get());

        // 焦土 - 使用简单的方块模型
        createSimpleBlock(blockModels, org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT.get());

        // 焦灰草丛 - 使用交叉模型（类似草丛）
        createCrossBlock(blockModels, org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS.get());

        // 焦灰枝条 - 使用交叉模型
        createCrossBlock(blockModels, org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_TWIG.get());

        // 仙人球 - 使用交叉模型
        createCrossBlock(blockModels, org.polaris2023.wildwind.barren.block.ModBlocks.TINY_CACTUS.get());

        // 制箭台 - 使用简单方块模型
        blockModels.blockStateOutput.accept(net.minecraft.client.data.models.blockstates.MultiVariantGenerator.dispatch(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get())
                .with(BlockModelGenerators.createBooleanModelDispatch(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT,
                        BlockModelGenerators.plainVariant(Identifier.withDefaultNamespace("block/furnace_on")),
                        BlockModelGenerators.plainVariant(Identifier.withDefaultNamespace("block/furnace"))))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        itemModels.itemModelOutput.accept(org.polaris2023.wildwind.core.block.ModBlocks.KILN.get().asItem(),
                ItemModelUtils.plainModel(Identifier.withDefaultNamespace("block/furnace")));

        // ==================== 独立物品 ====================
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.CRAB_CLAW);

        // 万用蟹钳 - 使用手写 3D 模型（resources 下的 models/item/omni_claw.json），仅生成物品模型定义
        itemModels.itemModelOutput.accept(
                org.polaris2023.wildwind.wilderness.registry.ModItems.OMNI_CLAW.get(),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(org.polaris2023.wildwind.wilderness.registry.ModItems.OMNI_CLAW.get()))
        );
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.MUDCRAB_BUCKET);
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.MUDCRAB_SPAWN_EGG);

        // ==================== 食人鱼 ====================
        registerPiranhaModels(itemModels);
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

    private static net.minecraft.client.data.models.MultiVariant signVariant(String path) {
        return BlockModelGenerators.plainVariant(HFASMod.id("block/" + path));
    }

    private void simpleItem(ItemModelGenerators itemModels, Supplier<? extends Item> item){
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
    }

    private void registerPiranhaModels(ItemModelGenerators itemModels) {
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA);
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA);
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA_BUCKET);
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA_SPAWN_EGG);
        simpleItem(itemModels, org.polaris2023.wildwind.wilderness.registry.ModItems.FANG);
    }
}
