package org.polaris2023.ww_ag.common.registrate.entry;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/10 15:28:54
 */
@SuppressWarnings("unchecked")
public class PlanksEntry<T extends WWRegistrate> {
    public BlockBuilder<?, L2Registrate> tBuilder;
    public final T registrate;
    public BlockEntry<RotatedPillarBlock> planks;
    public final String name;
    public PlanksEntry(T registrate, String name) {
        this.registrate = registrate;
        this.name = name;
    }

    public PlanksEntry<T> planks(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties
    ) {
        tBuilder = registrate.block(name + "_planks", __ -> {
            BlockBehaviour.Properties properties1 = BlockBehaviour.Properties.ofFullCopy(copy.get());
            properties.accept(properties1);
            return new RotatedPillarBlock(properties1);
        }).tag(BlockTags.PLANKS);
        return this;
    }public PlanksEntry<T> planks(Supplier<Block> copy) {
        tBuilder = registrate.block(name + "_planks", __ -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(copy.get())))
                .tag(BlockTags.PLANKS);
        return this;
    }
    public PlanksEntry<T> planks() {
        tBuilder = registrate.block(name + "_planks", RotatedPillarBlock::new)
                .tag(BlockTags.PLANKS);
        return this;
    }


    public PlanksEntry<T> registerPlanks(NonNullConsumer<BlockBuilder<RotatedPillarBlock, L2Registrate>> consumer) {
        BlockBuilder<RotatedPillarBlock, L2Registrate> tBuilder1 = (BlockBuilder<RotatedPillarBlock, L2Registrate>) tBuilder;
        consumer.accept(tBuilder1);
        planks = tBuilder1.register();
        tBuilder = null;
        return this;
    }
}
