package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/19 17:35:13
 */
public interface IStairs<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setStairs(BlockEntry<StairBlock> entry);
    default T stairs(Supplier<Block> copy, NonNullUnaryOperator<BlockBehaviour.Properties> properties) {
        T self = ww_ag$self();
        return setStairs(ILanguage.convert1(self.registrate.block(self.name + "_stairs", __ -> {
                    BlockBehaviour.Properties prop = copy!=null
                            ? BlockBehaviour.Properties.ofFullCopy(copy.get())
                            : BlockBehaviour.Properties.of();
                    return new StairBlock(self.planks.getDefaultState(), properties.apply(prop));
                }))
                .ww_ag$zh_cn(self.zhCn+"木楼梯")
                .ww_ag$zh_tw(self.zhTw+"木樓梯")
                .ww_ag$zh_hk(self.zhHk+"木樓梯")
                .ww_ag$self()
                .tag(BlockTags.SLABS)
                .item().tag(ItemTags.SLABS).model((c, p) -> {}).build()
                .blockstate((c, p) -> {
                    var planks = TextureMapping.getBlockTexture(self.planks.get());
                    ResourceLocation selfC = c.getId().withPrefix("block/");

                    BlockModelBuilder stairs = p.models().stairs(selfC.getPath(), planks, planks, planks);
                    p.stairsBlock(
                            c.get(),
                            stairs,
                            p.models().stairsInner(selfC.withSuffix("_inner").getPath(), planks, planks, planks),
                            p.models().stairsOuter(selfC.withSuffix("_outer").getPath(), planks, planks, planks)
                    );
                    p.simpleBlockItem(c.get(), stairs);
                })
                .recipe((c, p) -> {

                })
                .lang(self.firstUpName()+" Stairs")
                .register());
    }
    default T stairs(Supplier<Block> copy) {
        return stairs(copy, NonNullUnaryOperator.identity());
    }
    default T stairs(NonNullUnaryOperator<BlockBehaviour.Properties> p) {
        return stairs(null, p);
    }
    default T stairs() {
        return stairs(NonNullUnaryOperator.identity());
    }
}
