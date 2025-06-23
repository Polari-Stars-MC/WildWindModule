package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/19 12:10:42
 */
public interface IPressurePlate<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setPressurePlate(BlockEntry<PressurePlateBlock> entry);

    default T pressurePlate(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<PressurePlateBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        var b = build.apply(self.registrate.block(self.name + "_pressure_plate", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new PressurePlateBlock(self.bst, properties1);
                }))
                .tag(BlockTags.PRESSURE_PLATES)
                .blockstate((c, p) -> {
                    p.pressurePlateBlock(c.get(), TextureMapping.getBlockTexture(self.planks.get()));
                    ModelFile pressurePlate = p.models().getExistingFile(c.getId().withPrefix("block/"));
                    p.simpleBlockItem(c.get(), pressurePlate);
                }).item().model((c, p) -> {

                })
                .tag(ItemTags.WOODEN_PRESSURE_PLATES).build()
                .recipe((c, p) -> {
                    RegistrateRecipeProvider.pressurePlate(p, c.get(), self.planks.asItem());

                });
        return setPressurePlate(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"木门")
                .ww_ag$zh_tw(self.zhTw+"木門")
                .ww_ag$zh_hk(self.zhHk+"木門")
                .ww_ag$self()
                .lang(self.firstUpName() + " Pressure Plate").register());

    }
    default T pressurePlate(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<PressurePlateBlock, L2Registrate>> build
    ) {
        return pressurePlate(copy, __ -> {}, build);
    }
    default T pressurePlate(Supplier<Block> copy) {
        return pressurePlate(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default T pressurePlate() {
        return pressurePlate(__ -> {}, NonNullUnaryOperator.identity());
    }
    default T pressurePlate(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<PressurePlateBlock, L2Registrate>> build
    ) {
        return pressurePlate(null, properties, build);
    }
}
