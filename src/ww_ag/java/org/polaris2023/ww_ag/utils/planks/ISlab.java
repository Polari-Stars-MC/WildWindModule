package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/19 16:45:18
 */
public interface ISlab<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setSlab(BlockEntry<SlabBlock> entry);
    default T slab(Supplier<Block> copy, NonNullUnaryOperator<BlockBehaviour.Properties> properties) {
        T self = ww_ag$self();
        return setSlab(ILanguage.convert1(self.registrate.block(self.name + "_slab", __ -> {
                    BlockBehaviour.Properties prop = copy!=null
                            ? BlockBehaviour.Properties.ofFullCopy(copy.get())
                            : BlockBehaviour.Properties.of();
                    return new SlabBlock(properties.apply(prop));
                }))
                .ww_ag$zh_cn(self.zhCn+"木台阶")
                .ww_ag$zh_tw(self.zhTw+"木臺階")
                .ww_ag$zh_hk(self.zhHk+"木臺階")
                .ww_ag$self()
                .tag(BlockTags.SLABS)
                .item().tag(ItemTags.SLABS).model((c, p) -> {}).build()
                .blockstate((c, p) -> {
                    var planks = TextureMapping.getBlockTexture(self.planks.get());
                    ResourceLocation selfC = c.getId().withPrefix("block/");
                    ResourceLocation top = selfC.withSuffix("_top");
                    BlockModelBuilder slab = p.models().slab(selfC.getPath(), planks, planks, planks);
                    p.slabBlock(
                            c.get(),
                            slab,
                            p.models().slabTop(top.getPath(), planks, planks, planks),
                            p.models().getExistingFile(planks)
                    );
                    p.simpleBlockItem(c.get(), slab);
                })
                .recipe((c, p) -> {

                })
                .lang(self.firstUpName()+" Slab")
                .register());
    }
    default T slab(Supplier<Block> copy) {
        return slab(copy, NonNullUnaryOperator.identity());
    }
    default T slab(NonNullUnaryOperator<BlockBehaviour.Properties> p) {
        return slab(null, p);
    }
    default T slab() {
        return slab(NonNullUnaryOperator.identity());
    }

}
