package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.common.block.StrippableBlock;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/23 21:59:41
 */
public interface IWW<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setCrown(BlockEntry<RotatedPillarBlock> entry);
    default T crown(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties
    ) {
        return crown(copy, properties, NonNullUnaryOperator.identity());
    }
    default T crown(
            Supplier<Block> copy
    ) {
        return crown(copy, p -> {}, NonNullUnaryOperator.identity());
    }
    default T crown(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return crown(null, properties, factory);
    }
    default T crown(
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return crown(null, p -> {}, factory);
    }
    default T crown(
            NonNullConsumer<BlockBehaviour.Properties> properties
    ) {
        return crown(null, properties, NonNullUnaryOperator.identity());
    }
    default T crown(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        T self = ww_ag$self();
        BlockBuilder<RotatedPillarBlock, L2Registrate> b = factory.apply(self.registrate.block(self.name + "_crown", __ -> {
            BlockBehaviour.Properties properties1 =
                    copy != null ?
                            BlockBehaviour.Properties.ofFullCopy(copy.get())
                            : BlockBehaviour.Properties.of();
            properties.accept(properties1);
            return new StrippableBlock(properties1, () -> self.stripped_log.get());
        }))
                .blockstate((c, p) -> p.axisBlock(c.get(), p.blockTexture(c.get()), p.blockTexture(self.log.get()).withSuffix("_top")))
                .tag(WWBlockTags.LOG_CROWN.get(), self.blockLogs).item().tag(WWItemTags.LOG_CROWM.get(), self.itemLogs).build();
        return setCrown(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn + "树冠")
                .ww_ag$zh_tw(self.zhTw + "樹冠")
                .ww_ag$zh_hk(self.zhHk + "樹冠")
                .ww_ag$self()
                .lang(self.firstUpName() + " Crown")
                .register());
    }
}
