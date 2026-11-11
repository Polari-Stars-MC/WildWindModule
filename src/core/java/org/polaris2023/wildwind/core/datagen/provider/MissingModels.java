package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

/**
 * 暂时缺少贴图的模型生成：模型一律引用方块自身的贴图路径，
 * 贴图不存在时客户端自动显示原版黑紫缺失纹理，之后补上贴图即可直接生效。
 *
 * @author safe_saffawa
 */
public final class MissingModels {

    /** 六面方块 + 物品模型 */
    public static void cube(BlockModelGenerators generators, Block block) {
        Identifier model = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
        generators.registerSimpleItemModel(block.asItem(), model);
    }

    /** 完整方块，可选楼梯 / 台阶 / 墙（均使用基底的贴图路径） */
    public static void stoneSet(BlockModelGenerators generators, Block base,
                                @Nullable Block stairs, @Nullable Block slab, @Nullable Block wall) {
        BlockModelGenerators.BlockFamilyProvider provider = generators.new BlockFamilyProvider(TextureMapping.cube(base))
                .fullBlock(base, ModelTemplates.CUBE_ALL);
        if (stairs != null) {
            provider.stairs(stairs);
        }
        if (slab != null) {
            provider.slab(slab);
        }
        if (wall != null) {
            provider.wall(wall);
        }
    }

    /** 十字植物 + 物品模型 */
    public static void cross(BlockModelGenerators generators, Block block) {
        generators.createCrossBlockWithDefaultItem(block, BlockModelGenerators.PlantType.NOT_TINTED,
                TextureMapping.cross(block));
    }

    /** 双层植物 + 物品模型 */
    public static void doublePlant(BlockModelGenerators generators, Block block) {
        Identifier model = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generators.modelOutput);
        MultiVariant variant = BlockModelGenerators.plainVariant(model);
        generators.createDoubleBlock(block, variant, variant);
        generators.registerSimpleItemModel(block.asItem(), model);
    }

    private MissingModels() {
    }
}
