package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.common.block.StrippableBlock;

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
public interface ILog<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setLog(BlockEntry<RotatedPillarBlock> entry);
    T setStrippedLog(BlockEntry<RotatedPillarBlock> entry);
    default T strippedLog(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory) {
        return strippedLog(copy, __ -> {}, factory);
    }
    default T strippedLog(
            Supplier<Block> copy) {
        return strippedLog(copy, __ -> {}, NonNullUnaryOperator.identity());
    }

    default T strippedLog(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        T self = ww_ag$self();
        BlockBuilder<RotatedPillarBlock, L2Registrate> b = factory.apply(self.registrate.block("stripped_" + self.name + "_log", __ -> {
            BlockBehaviour.Properties properties1 =
                    copy != null ?
                            BlockBehaviour.Properties.ofFullCopy(copy.get())
                            : BlockBehaviour.Properties.of();
            properties.accept(properties1);
            return new RotatedPillarBlock(properties1);
        }))
                .blockstate((c, p) -> p.logBlock(c.get())).tag(self.blockLogs)
                .item()
                .tag(self.itemLogs)
                .build();
        return setStrippedLog(ILanguage
                .convert1(b)
                .ww_ag$zh_cn("去皮" + self.zhCn + "原木")
                .ww_ag$zh_tw("去皮" + self.zhTw + "原木")
                .ww_ag$zh_hk("去皮" + self.zhHk + "原木")
                .ww_ag$self()
                .lang("Stripped " + self.firstUpName() + " Log")
                .register());
    }
    default T strippedLog(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return strippedLog(null ,properties, factory);
    }


    default T strippedLog(
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return strippedLog(__ -> {}, factory);
    }

    default T strippedLog() {
        return strippedLog(NonNullUnaryOperator.identity());
    }


    default T log(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory) {
        return log(copy, __ -> {}, factory);
    }
    default T log(
            Supplier<Block> copy) {
        return log(copy, __ -> {}, NonNullUnaryOperator.identity());
    }


    default T log(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        T self = ww_ag$self();
        BlockBuilder<RotatedPillarBlock, L2Registrate> b = factory.apply(self.registrate.block(self.name + "_log", __ -> {
            BlockBehaviour.Properties properties1 =
                    copy != null ?
                            BlockBehaviour.Properties.ofFullCopy(copy.get())
                            : BlockBehaviour.Properties.of();
            properties.accept(properties1);
            return new StrippableBlock(properties1, () -> self.stripped_log.get());
        })).blockstate((c, p) -> p.logBlock(c.get())).tag(self.blockLogs).item().tag(self.itemLogs).build();
        return setLog(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn + "原木")
                .ww_ag$zh_tw(self.zhTw + "原木")
                .ww_ag$zh_hk(self.zhHk + "原木")
                .ww_ag$self()
                .lang(self.firstUpName() + " Log")
                .register());
    }
    default T log(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return log(null, properties, factory);
    }


    default T log(
            NonNullUnaryOperator<BlockBuilder<RotatedPillarBlock, L2Registrate>> factory
    ) {
        return log(__ -> {}, factory);
    }

    default T log() {
        return log(NonNullUnaryOperator.identity());
    }



}
