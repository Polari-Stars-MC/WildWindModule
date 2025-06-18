package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/18 19:06:20
 */
public interface IFence<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setFenceGate(BlockEntry<FenceGateBlock> entry);
    T setFence(BlockEntry<FenceBlock> entry);

    default PlanksEntry<E> fence(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<FenceBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        BlockBuilder<FenceBlock, L2Registrate> b = build.apply(self.registrate.block(self.name + "_fence", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new FenceBlock(properties1);
                }))
                .blockstate((c, p) -> {
                    ResourceLocation blockTexture = TextureMapping.getBlockTexture(self.planks.get());
                    p.fenceBlock(c.get(), blockTexture);
                    var a = p.models().fenceInventory(c.getId().withSuffix("_inventory").getPath(), blockTexture);
                    p.simpleBlockItem(c.get(), a);
                }).item().tag(ItemTags.FENCES).model((c, p) -> {}).build()
                .recipe((c, p) -> {
                    DataIngredient items = DataIngredient.items(self.planks.asItem());
                    RegistrateRecipeProvider.fenceBuilder(c.get(), items.toVanilla())
                            .unlockedBy(p.safeName(c.getId()), items.getCriterion(p))
                            .save(p, p.safeId(c.getId()));
                });
        return setFence(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"木栅栏")
                .ww_ag$zh_tw(self.zhTw+"木柵欄")
                .ww_ag$zh_hk(self.zhHk+"木柵欄")
                .ww_ag$self()
                .lang(self.firstUpName() + " Fence").register());

    }
    default PlanksEntry<E> fence(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<FenceBlock, L2Registrate>> build
    ) {
        return fence(copy, __ -> {}, build);
    }
    default PlanksEntry<E> fence(Supplier<Block> copy) {
        return fence(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> fence() {
        return fence(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> fence(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<FenceBlock, L2Registrate>> build
    ) {
        return fence(null, properties, build);
    }


    default PlanksEntry<E> fenceGate(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<FenceGateBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        var b = build.apply(self.registrate.block(self.name + "_fence_gate", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new FenceGateBlock(self.wt, properties1);
                }).tag(BlockTags.FENCE_GATES))
                .blockstate((c, p) -> {
                    ;
                    p.fenceGateBlock(c.get(), TextureMapping.getBlockTexture(self.planks.get()));
                }).item()
                .tag(ItemTags.FENCE_GATES)
                .build()
                .recipe((c, p) -> {
                    DataIngredient items = DataIngredient.items(self.planks.asItem());
                    RegistrateRecipeProvider.fenceGateBuilder(c.get(), items.toVanilla())
                            .unlockedBy(p.safeName(c.getId()), items.getCriterion(p))
                            .save(p, p.safeId(c.getId()));
                });
        return setFenceGate(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"木栅栏门")
                .ww_ag$zh_tw(self.zhTw+"木柵欄門")
                .ww_ag$zh_hk(self.zhHk+"木柵欄門")
                .ww_ag$self()
                .lang(self.firstUpName() + " Fence Gate").register());

    }
    default PlanksEntry<E> fenceGate(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<FenceGateBlock, L2Registrate>> build
    ) {
        return fenceGate(copy, __ -> {}, build);
    }
    default PlanksEntry<E> fenceGate(Supplier<Block> copy) {
        return fenceGate(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> fenceGate() {
        return fenceGate(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> fenceGate(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<FenceGateBlock, L2Registrate>> build
    ) {
        return fenceGate(null, properties, build);
    }
}
