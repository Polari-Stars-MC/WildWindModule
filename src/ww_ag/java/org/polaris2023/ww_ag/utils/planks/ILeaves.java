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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/18 23:57:12
 */
public interface ILeaves<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setLeaves(BlockEntry<LeavesBlock> entry);
    default T leaves(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<LeavesBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        var b = build.apply(self.registrate.block(self.name + "_leaves", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new LeavesBlock(properties1);
                }))
                .blockstate((c, p) -> {
                            ResourceLocation blockTexture = TextureMapping.getBlockTexture(c.get());
                            var model = p.models().leaves(c.getId().getPath(), blockTexture);
                            p.simpleBlockWithItem(c.get(), model);
                })
                .tag(BlockTags.LEAVES)
                .item().tag(ItemTags.LEAVES).build();
        return setLeaves(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"树叶")
                .ww_ag$zh_tw(self.zhTw+"樹葉")
                .ww_ag$zh_hk(self.zhHk+"樹葉")
                .ww_ag$self()
                .lang(self.firstUpName() + " Leaves").register());

    }
    default T leaves(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<LeavesBlock, L2Registrate>> build
    ) {
        return leaves(copy, __ -> {}, build);
    }
    default T leaves(Supplier<Block> copy) {
        return leaves(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default T leaves() {
        return leaves(__ -> {}, NonNullUnaryOperator.identity());
    }
    default T leaves(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<LeavesBlock, L2Registrate>> build
    ) {
        return leaves(null, properties, build);
    }
}
