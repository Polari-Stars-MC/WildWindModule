package org.polaris2023.ww_ag.common.registrate.entry;

import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.utils.planks.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

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
        IWood<T, PlanksEntry<T>>,
        IButton<T, PlanksEntry<T>>,
        IDoor<T, PlanksEntry<T>>,
        IFence<T, PlanksEntry<T>>{
    public final T registrate;

    public final WoodType wt;
    public final BlockSetType bst;

    public final String name;
    final ResourceLocation logsRl;
    public final TagKey<Block> blockLogs;
    public final TagKey<Item> itemLogs;
    @Setter
    @Accessors(fluent = true)
    public String zhCn,zhTw,zhHk;
    public BlockEntry<RotatedPillarBlock>
            planks,
            stripped_log,
            log,
            stripped_wood,
            wood;
    public BlockEntry<DoorBlock> door;
    public BlockEntry<TrapDoorBlock> trapDoor;
    public BlockEntry<ButtonBlock> button;
    public BlockEntry<FenceGateBlock> fence_gate;
    public BlockEntry<FenceBlock> fence;

    public String firstUpName() {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }
    public PlanksEntry(T registrate, String name) {
        this.registrate = registrate;
        this.name = name;
        logsRl = ResourceLocation.fromNamespaceAndPath("c", name + "_logs");
        blockLogs = BlockTags.create(logsRl);
        itemLogs = ItemTags.create(logsRl);
        var tName = registrate.getModid() + "_" + name;
        bst = BlockSetType.register(new BlockSetType(tName));
        wt = WoodType.register(new WoodType(tName, bst));
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
        if (wood != null) throw new IllegalArgumentException("%s_wood is registered!".formatted(name));
        wood = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setStrippedWood(BlockEntry<RotatedPillarBlock> entry) {
        if (stripped_wood != null) throw new IllegalArgumentException("stripped_%s_wood is registered!".formatted(name));
        stripped_wood = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setButton(BlockEntry<ButtonBlock> entry) {
        if (button != null) throw new IllegalArgumentException("%s_button is registered!".formatted(name));
        button = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setDoor(BlockEntry<DoorBlock> entry) {
        if (door != null) throw new IllegalArgumentException("%s_door is registered!".formatted(name));
        door = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setTrapDoor(BlockEntry<TrapDoorBlock> entry) {
        if (trapDoor != null) throw new IllegalArgumentException("%s_trap_door is registered!".formatted(name));
        trapDoor = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setFenceGate(BlockEntry<FenceGateBlock> entry) {
        if (fence_gate != null) throw new IllegalArgumentException("%s_fence_gate is registered!".formatted(name));
        fence_gate = entry;
        return ww_ag$self();
    }

    @Override
    public PlanksEntry<T> setFence(BlockEntry<FenceBlock> entry) {
        if (fence != null) throw new IllegalArgumentException("%s_fence is registered!".formatted(name));
        fence = entry;
        return ww_ag$self();
    }
}
