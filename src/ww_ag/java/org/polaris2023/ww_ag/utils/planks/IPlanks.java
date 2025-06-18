package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/16 22:17:03
 */
@SuppressWarnings("unused")
public interface IPlanks<E extends WWRegistrate,T extends PlanksEntry<E>> extends ISelf<T> {
    T setPlanks(BlockEntry<RotatedPillarBlock> entry);

    default PlanksEntry<E> planks(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        BlockBuilder<RotatedPillarBlock, L2Registrate> b = build.apply(self.registrate.block(self.name + "_planks", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new RotatedPillarBlock(properties1);
                }).tag(BlockTags.PLANKS))
                .defaultBlockstate().simpleItem()
                .recipe((c, p) -> {
                    p.planks(
                            DataIngredient.tag(self.itemLogs),
                            RecipeCategory.BUILDING_BLOCKS,
                            c);
                });
        return setPlanks(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn + "木板")
                .ww_ag$zh_tw(self.zhTw + "木板")
                .ww_ag$zh_hk(self.zhHk + "木板")
                .ww_ag$self()
                .lang(self.firstUpName() + " Planks")
                .register());

    }
    default PlanksEntry<E> planks(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> build
    ) {
        return planks(copy, __ -> {}, build);
    }
    default PlanksEntry<E> planks(Supplier<Block> copy) {
        return planks(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> planks() {
        return planks(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> planks(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> build
    ) {
        return planks(null, properties, build);
    }
}
