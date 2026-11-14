package org.polaris2023.wildwind.barren.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.polaris2023.wildwind.barren.WildWindBarren;
import org.polaris2023.wildwind.barren.block.BarrenBlocks;
import org.polaris2023.wildwind.barren.block.BarrenWoodFamilies;
import org.polaris2023.wildwind.barren.registry.BarrenItems;
import org.polaris2023.wildwind.barren.data.BarrenModelSpecs;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.imp.data.ImpModelWriter;

import java.util.Optional;

/**
 * 瘠土更新的模型生成器：木材套件 / 砂岩 / 沙 / 焦土。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class BarrenModelProvider extends ModelProvider {

    /** 底面旋转 180 度的砂岩模型模板 */
    private static final ModelTemplate ROTATED_TOP_SANDSTONE = new ModelTemplate(
            Optional.of(WildWindBarren.id("block/rotated_top_sandstone").unwrap()),
            Optional.empty(),
            TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public BarrenModelProvider(PackOutput output) {
        super(output, WildWindBarren.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // 木材套件
        for (WoodFamily family : BarrenWoodFamilies.ALL) {
            registerWoodModels(blockModels, family);
        }

        // 告示牌 / 悬挂告示牌 / 展示架
        registerSignSet(blockModels,
                BarrenBlocks.CINDER_PLANKS.get(), BarrenBlocks.STRIPPED_CINDER_LOG.get(),
                BarrenBlocks.CINDER_SIGN.get(), BarrenBlocks.CINDER_WALL_SIGN.get(),
                BarrenBlocks.CINDER_HANGING_SIGN.get(), BarrenBlocks.CINDER_WALL_HANGING_SIGN.get(),
                BarrenBlocks.CINDER_SHELF.get());
        registerSignSet(blockModels,
                BarrenBlocks.EMBER_PLANKS.get(), BarrenBlocks.STRIPPED_EMBER_LOG.get(),
                BarrenBlocks.EMBER_SIGN.get(), BarrenBlocks.EMBER_WALL_SIGN.get(),
                BarrenBlocks.EMBER_HANGING_SIGN.get(), BarrenBlocks.EMBER_WALL_HANGING_SIGN.get(),
                BarrenBlocks.EMBER_SHELF.get());

        // 船物品图标
        itemModels.generateFlatItem(BarrenItems.CINDER_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BarrenItems.CINDER_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BarrenItems.EMBER_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BarrenItems.EMBER_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);

        // 树苗 + 花盆
        blockModels.createPlant(BarrenBlocks.CINDER_SAPLING.get(), BarrenBlocks.POTTED_CINDER_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createPlant(BarrenBlocks.EMBER_SAPLING.get(), BarrenBlocks.POTTED_EMBER_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // 焦土
        createGrassBlock(blockModels, BarrenBlocks.SCORCHED_GRASS_BLOCK.get());
        blockModels.createTrivialCube(BarrenBlocks.SCORCHED_DIRT.get());
        blockModels.createCrossBlock(BarrenBlocks.SCORCHED_GRASS.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createCrossBlock(BarrenBlocks.SCORCHED_TWIG.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createCrossBlock(BarrenBlocks.TINY_CACTUS.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // 流沙 / 可疑沙
        blockModels.createTrivialCube(BarrenBlocks.QUICKSAND.get());
        blockModels.createTrivialCube(BarrenBlocks.RED_QUICKSAND.get());
        blockModels.createBrushableBlock(BarrenBlocks.SUSPICIOUS_RED.get());

        // 砂岩
        createSandstoneSet(blockModels, BarrenBlocks.QUARTZ_SANDSTONE.get(),
                BarrenBlocks.QUARTZ_SANDSTONE_STAIRS.get(),
                BarrenBlocks.QUARTZ_SANDSTONE_SLAB.get(),
                BarrenBlocks.QUARTZ_SANDSTONE_WALL.get());
        createSandstoneSet(blockModels, BarrenBlocks.SOUL_SANDSTONE.get(),
                BarrenBlocks.SOUL_SANDSTONE_STAIRS.get(),
                BarrenBlocks.SOUL_SANDSTONE_SLAB.get(),
                BarrenBlocks.SOUL_SANDSTONE_WALL.get());

        Material quartzTop = TextureMapping.getBlockTexture(BarrenBlocks.QUARTZ_SANDSTONE.get(), "_top");
        Material soulTop = TextureMapping.getBlockTexture(BarrenBlocks.SOUL_SANDSTONE.get(), "_top");
        createRotatedTopSandstone(blockModels, BarrenBlocks.CHISELED_QUARTZ_SANDSTONE.get(), quartzTop);
        createRotatedTopSandstone(blockModels, BarrenBlocks.CUT_QUARTZ_SANDSTONE.get(), quartzTop);
        createRotatedTopSandstone(blockModels, BarrenBlocks.NITRIFIED_QUARTZ_SANDSTONE.get(), quartzTop);
        createRotatedTopSandstone(blockModels, BarrenBlocks.CHISELED_SOUL_SANDSTONE.get(), soulTop);
        createRotatedTopSandstone(blockModels, BarrenBlocks.CUT_SOUL_SANDSTONE.get(), soulTop);
        createRotatedTopSandstone(blockModels, BarrenBlocks.NITRIFIED_SOUL_SANDSTONE.get(), soulTop);
        createVanillaTopBottomSandstone(blockModels, BarrenBlocks.NITRIFIED_RED_SANDSTONE.get(), Blocks.RED_SANDSTONE);
        createVanillaTopBottomSandstone(blockModels, BarrenBlocks.NITRIFIED_SANDSTONE.get(), Blocks.SANDSTONE);

        createSmoothSandstone(blockModels, BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE.get(), quartzTop);
        createSmoothSandstone(blockModels, BarrenBlocks.SMOOTH_SOUL_SANDSTONE.get(), soulTop);
        createStairs(blockModels, BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_STAIRS.get(), quartzTop);
        createSlab(blockModels, BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE.get(), BarrenBlocks.SMOOTH_QUARTZ_SANDSTONE_SLAB.get(), quartzTop);
        createSlab(blockModels, BarrenBlocks.CUT_QUARTZ_SANDSTONE.get(), BarrenBlocks.CUT_QUARTZ_SANDSTONE_SLAB.get(), quartzTop);
        createStairs(blockModels, BarrenBlocks.SMOOTH_SOUL_SANDSTONE_STAIRS.get(), soulTop);
        createSlab(blockModels, BarrenBlocks.SMOOTH_SOUL_SANDSTONE.get(), BarrenBlocks.SMOOTH_SOUL_SANDSTONE_SLAB.get(), soulTop);
        createSlab(blockModels, BarrenBlocks.CUT_SOUL_SANDSTONE.get(), BarrenBlocks.CUT_SOUL_SANDSTONE_SLAB.get(), soulTop);

        blockModels.createTrivialCube(BarrenBlocks.QUARTZ_SAND.get());
        blockModels.createTrivialCube(BarrenBlocks.QUARTZ_QUICKSAND.get());
        blockModels.createTrivialCube(BarrenBlocks.SOUL_QUICKSAND.get());

        blockModels.createBrushableBlock(BarrenBlocks.SUSPICIOUS_QUARTZ_SAND.get());
        blockModels.createBrushableBlock(BarrenBlocks.SUSPICIOUS_SOUL_SAND.get());

        // 灰烬 / 植被（草稿）：暂无贴图，统一使用原版黑紫缺失纹理（规格 + 发射器）
        ImpModelWriter.apply(BarrenModelSpecs.models(), new ImpModelWriter.Generators() {
            @Override
            public Object blockModels() {
                return blockModels;
            }

            @Override
            public Object itemModels() {
                return itemModels;
            }
        });
    }

    private void registerWoodModels(BlockModelGenerators blockModels, WoodFamily family) {
        blockModels.woodProvider(family.log().get()).logWithHorizontal(family.log().get()).wood(family.wood().get());
        blockModels.woodProvider(family.strippedLog().get())
                .logWithHorizontal(family.strippedLog().get())
                .wood(family.strippedWood().get());
        blockModels.family(family.planks().get())
                .stairs(family.stairs().get())
                .slab(family.slab().get())
                .fence(family.fence().get())
                .fenceGate(family.fenceGate().get())
                .pressurePlate(family.pressurePlate().get())
                .button(family.button().get());
        blockModels.createDoor(family.door().get());
        blockModels.createTrapdoor(family.trapdoor().get());
        if (family.hasLeaves()) {
            blockModels.createTintedLeaves(family.leaves().get(), TexturedModel.LEAVES, family.leavesTint());
        }
    }

    /**
     * 告示牌 / 悬挂告示牌复用原版 family 生成器，展示架复用原版 createShelf。
     * <p>
     * 这里直接新建 provider：{@code blockModels.family(planks)} 会重新生成木板本体模型，与已有模型冲突。
     */
    private void registerSignSet(BlockModelGenerators blockModels, Block planks, Block strippedLog,
                                 Block sign, Block wallSign, Block hangingSign, Block wallHangingSign, Block shelf) {
        BlockFamily signFamily = new BlockFamily.Builder(planks)
                .strippedLog(strippedLog)
                .sign(sign, wallSign)
                .hangingSign(hangingSign, wallHangingSign)
                .getFamily();
        blockModels.new BlockFamilyProvider(TexturedModel.CUBE.get(planks).getMapping()).generateFor(signFamily);
        blockModels.createShelf(shelf, strippedLog);
    }

    /**
     * 草方块式模型：侧面 scorched_grass_block_side，顶面 _top，底面使用焦土贴图。
     */
    private void createGrassBlock(BlockModelGenerators blockModels, Block block) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(BarrenBlocks.SCORCHED_DIRT.get()));
        Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        blockModels.registerSimpleItemModel(block.asItem(), model);
    }

    /**
     * 砂岩 + 楼梯 / 台阶 / 墙，统一使用砂岩自身的侧面 / 顶底贴图。
     */
    private void createSandstoneSet(BlockModelGenerators blockModels, Block base, Block stairs, Block slab, Block wall) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(base, "_bottom"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(base, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(base))
                .put(TextureSlot.WALL, TextureMapping.getBlockTexture(base))
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(base));
        BlockModelGenerators.BlockFamilyProvider provider = blockModels.new BlockFamilyProvider(mapping)
                .fullBlock(base, ModelTemplates.CUBE_BOTTOM_TOP);
        blockModels.registerSimpleItemModel(base.asItem(), ModelLocationUtils.getModelLocation(base));
        provider.stairs(stairs).slab(slab).wall(wall);
    }

    /** 单贴图楼梯 */
    private void createStairs(BlockModelGenerators blockModels, Block stairs, Material texture) {
        TextureMapping mapping = TextureMapping.cube(texture);
        Identifier inner = ModelTemplates.STAIRS_INNER.create(stairs, mapping, blockModels.modelOutput);
        Identifier straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, mapping, blockModels.modelOutput);
        Identifier outer = ModelTemplates.STAIRS_OUTER.create(stairs, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs,
                BlockModelGenerators.plainVariant(inner),
                BlockModelGenerators.plainVariant(straight),
                BlockModelGenerators.plainVariant(outer)));
        blockModels.registerSimpleItemModel(stairs.asItem(), straight);
    }

    /** 单贴图台阶，双台阶沿用基底方块模型 */
    private void createSlab(BlockModelGenerators blockModels, Block base, Block slab, Material texture) {
        TextureMapping mapping = TextureMapping.cube(texture);
        Identifier bottom = ModelTemplates.SLAB_BOTTOM.create(slab, mapping, blockModels.modelOutput);
        Identifier top = ModelTemplates.SLAB_TOP.create(slab, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSlab(slab,
                BlockModelGenerators.plainVariant(bottom),
                BlockModelGenerators.plainVariant(top),
                BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(base))));
        blockModels.registerSimpleItemModel(slab.asItem(), bottom);
    }

    private void createRotatedTopSandstone(BlockModelGenerators blockModels, Block block, Material top) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.TOP, top)
                .put(TextureSlot.BOTTOM, top);
        Identifier model = ROTATED_TOP_SANDSTONE.create(block, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        blockModels.registerSimpleItemModel(block.asItem(), model);
    }

    private void createSmoothSandstone(BlockModelGenerators blockModels, Block block, Material top) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, top)
                .put(TextureSlot.TOP, top)
                .put(TextureSlot.BOTTOM, top);
        Identifier model = ROTATED_TOP_SANDSTONE.create(block, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        blockModels.registerSimpleItemModel(block.asItem(), model);
    }

    private void createVanillaTopBottomSandstone(BlockModelGenerators blockModels, Block block, Block vanilla) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(vanilla, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(vanilla, "_bottom"));
        Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        blockModels.registerSimpleItemModel(block.asItem(), model);
    }
}
