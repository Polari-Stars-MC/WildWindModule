package org.polaris2023.ww_ag.utils.planks;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.state.BlockState;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/18 23:57:12
 */
public interface ISign<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setSign(BlockEntry<StandingSignBlock> entry);
    T setHangingSign(BlockEntry<CeilingHangingSignBlock> entry);
    T setWallSign(BlockEntry<WallSignBlock> entry);
    T setWallHangingSign(BlockEntry<WallHangingSignBlock> entry);

    default PlanksEntry<E> sign(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<StandingSignBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        var b = build.apply(self.registrate.block(self.name + "_sign", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new StandingSignBlock(self.wt, properties1);
                }))
                .blockstate((c, p) -> {
                            ResourceLocation blockTexture = TextureMapping.getBlockTexture(self.planks.get());
                            var model = p.models().sign(c.getId().getPath(), blockTexture);
                            p.simpleBlockWithItem(c.get(), model);
                        }
                )
                .tag(BlockTags.STANDING_SIGNS);
        return setSign(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"告示牌")
                .ww_ag$zh_tw(self.zhTw+"告示牌")
                .ww_ag$zh_hk(self.zhHk+"告示牌")
                .ww_ag$self()
                .lang(self.firstUpName() + " Sign").register());

    }
    default PlanksEntry<E> sign(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<StandingSignBlock, L2Registrate>> build
    ) {
        return sign(copy, __ -> {}, build);
    }
    default PlanksEntry<E> sign(Supplier<Block> copy) {
        return sign(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> sign() {
        return sign(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> sign(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<StandingSignBlock, L2Registrate>> build
    ) {
        return sign(null, properties, build);
    }

    default PlanksEntry<E> hangingSign(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<CeilingHangingSignBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        var b = build.apply(self.registrate.block(self.name + "_hanging_sign", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new CeilingHangingSignBlock(self.wt, properties1);
                }))
                .blockstate((c, p) -> {
                            ResourceLocation blockTexture = TextureMapping.getBlockTexture(self.stripped_log.get());
                            var model = p.models().sign(c.getId().getPath(), blockTexture);
                            p.simpleBlockWithItem(c.get(), model);
                        }
                )
                .tag(BlockTags.CEILING_HANGING_SIGNS);
        return setHangingSign(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"告示牌")
                .ww_ag$zh_tw(self.zhTw+"告示牌")
                .ww_ag$zh_hk(self.zhHk+"告示牌")
                .ww_ag$self()
                .lang(self.firstUpName() + " Hanging Sign").register());

    }
    default PlanksEntry<E> hangingSign(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<CeilingHangingSignBlock, L2Registrate>> build
    ) {
        return hangingSign(copy, __ -> {}, build);
    }
    default PlanksEntry<E> hangingSigm(Supplier<Block> copy) {
        return hangingSign(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> hangingSign() {
        return hangingSign(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> hangingSign(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<CeilingHangingSignBlock, L2Registrate>> build
    ) {
        return hangingSign(null, properties, build);
    }
}
