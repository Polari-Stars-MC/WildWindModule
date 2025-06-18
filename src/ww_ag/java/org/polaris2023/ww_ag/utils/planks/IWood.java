package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.util.Lazy;
import org.polaris2023.ww_ag.common.block.StrippedBlock;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/16 22:26:19
 */

@MethodsReturnNonnullByDefault
public interface IWood<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setWood(BlockEntry<RotatedPillarBlock> entry);
    T setStrippedWood(BlockEntry<RotatedPillarBlock> entry);
    default PlanksEntry<E> strippedWood(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory) {
        return strippedWood(copy, __ -> {}, factory);
    }
    default PlanksEntry<E> strippedWood(
            Supplier<Block> copy) {
        return strippedWood(copy, __ -> {}, NonNullUnaryOperator.identity());
    }


    default PlanksEntry<E> strippedWood(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        T self = ww_ag$self();
        BlockBuilder<RotatedPillarBlock, L2Registrate> b = factory.apply(self.registrate.block("stripped_" + self.name + "_wood", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new RotatedPillarBlock(properties1);
                }))
                .tag(self.blockLogs)
                .item().tag(self.itemLogs).build()
                .blockstate((c, p) -> woodModelGen(c, p, TextureMapping.logColumn(self.stripped_log.get())))
                .recipe((c, p) -> p.square(DataIngredient.items(self.stripped_log.asItem()), RecipeCategory.BUILDING_BLOCKS, c, true)).tag(self.blockLogs).item().tag(self.itemLogs).build();
        return setStrippedWood(ILanguage.convert1(b)
                .ww_ag$zh_cn("去皮"+self.zhCn + "木")
                .ww_ag$zh_tw("去皮"+self.zhTw + "木")
                .ww_ag$zh_hk("去皮"+self.zhHk + "木")
                .ww_ag$self()
                .lang("Stripped " + self.name + " wood")
                .register());
    }

    private void woodModelGen(DataGenContext<Block, RotatedPillarBlock> c, RegistrateBlockstateProvider p, TextureMapping logColumn) {
        VariantBlockStateBuilder variantBuilder = p.getVariantBuilder(c.get());
        var m = p.models().cubeColumn(c.getId().getPath(), logColumn.get(TextureSlot.SIDE), logColumn.get(TextureSlot.SIDE));
        variantBuilder.addModels(variantBuilder.partialState()
                                .with(RotatedPillarBlock.AXIS, Direction.Axis.X),
                        ConfiguredModel.builder()
                                .rotationX(90)
                                .rotationY(90)
                                .modelFile(m)
                                .buildLast())
                .addModels(variantBuilder.partialState()
                                .with(RotatedPillarBlock.AXIS, Direction.Axis.Y),
                        ConfiguredModel.builder()
                                .modelFile(m)
                                .buildLast())
                .addModels(variantBuilder.partialState()
                                .with(RotatedPillarBlock.AXIS, Direction.Axis.Z),
                        ConfiguredModel.builder()
                                .modelFile(m)
                                .rotationX(90)
                                .buildLast());
    }

    default PlanksEntry<E> strippedWood(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return strippedWood(null ,properties, factory);
    }


    default PlanksEntry<E> strippedWood(
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return strippedWood(__ -> {}, factory);
    }

    default PlanksEntry<E> strippedWood() {
        return strippedWood(NonNullUnaryOperator.identity());
    }


    default PlanksEntry<E> wood(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory) {
        return wood(copy, __ -> {}, factory);
    }
    default PlanksEntry<E> wood(
            Supplier<Block> copy) {
        return wood(copy, __ -> {}, NonNullUnaryOperator.identity());
    }


    default PlanksEntry<E> wood(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        T self = ww_ag$self();

        BlockBuilder<RotatedPillarBlock, L2Registrate> b = factory.apply(self.registrate.block(self.name + "_wood", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new StrippedBlock(properties1, Lazy.of(() -> () -> self.stripped_wood.get()));
                }))
                .tag(self.blockLogs)
                .blockstate((c, p) -> woodModelGen(c, p, TextureMapping.logColumn(self.log.get())))
                .item().tag(self.itemLogs).build()
                .recipe((c, p) -> p.square(DataIngredient.items(self.log.asItem()), RecipeCategory.BUILDING_BLOCKS, c, true)).tag(self.blockLogs).item().tag(self.itemLogs).build();
        return setWood(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn + "木")
                .ww_ag$zh_tw(self.zhTw + "木")
                .ww_ag$zh_hk(self.zhHk + "木")
                .ww_ag$self()
                .lang(self.firstUpName() + " wood").register());
    }
    default PlanksEntry<E> wood(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return wood(null, properties, factory);
    }


    default PlanksEntry<E> wood(
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return wood(__ -> {}, factory);
    }

    default PlanksEntry<E> wood() {
        return wood(NonNullUnaryOperator.identity());
    }



}
