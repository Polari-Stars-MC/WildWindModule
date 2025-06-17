package org.polaris2023.ww_ag.common.registrate.entry;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.utils.planks.ILog;
import org.polaris2023.ww_ag.utils.planks.IPlanks;
import org.polaris2023.ww_ag.utils.planks.IWood;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author baka4n
 * @code @Date 2025/6/10 15:28:54
 */
@SuppressWarnings({"unused"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PlanksEntry<T extends WWRegistrate> implements
        IPlanks<T, PlanksEntry<T>>,
        ILog<T, PlanksEntry<T>>,
        IWood<T, PlanksEntry<T>> {
    public final T registrate;
    public BlockEntry<RotatedPillarBlock>
            planks,
            stripped_log,
            log,
            stripped_wood,
            wood
    ;
    public final String name;
    final ResourceLocation logsRl;
    public final TagKey<Block> blockLogs;
    public final TagKey<Item> itemLogs;
    public PlanksEntry(T registrate, String name) {
        this.registrate = registrate;
        this.name = name;
        logsRl = ResourceLocation.fromNamespaceAndPath("c", name + "_logs");
        blockLogs = BlockTags.create(logsRl);
        itemLogs = ItemTags.create(logsRl);
    }

    @Override
    public PlanksEntry<T> setPlanks(BlockEntry<RotatedPillarBlock> entry) {
        if (planks != null) throw new IllegalArgumentException("%s_planks is registered!".formatted(name));
        planks = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> ww_ag$self() {
        return this;
    }

    @Override
    public PlanksEntry<T> setLog(BlockEntry<RotatedPillarBlock> entry) {
        if (log != null) throw new IllegalArgumentException("%s_log is registered!".formatted(name));
        log = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setStrippedLog(BlockEntry<RotatedPillarBlock> entry) {
        if (stripped_log != null) throw new IllegalArgumentException("stripped_%s_log is registered!".formatted(name));
        stripped_log = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setWood(BlockEntry<RotatedPillarBlock> entry) {
        if (wood != null) throw new IllegalArgumentException("stripped_%s_log is registered!".formatted(name));
        stripped_wood = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setStrippedWood(BlockEntry<RotatedPillarBlock> entry) {
        if (wood != null) throw new IllegalArgumentException("stripped_%s_log is registered!".formatted(name));
        stripped_wood = entry;
        return ww_ag$self();
    }
}
