package org.polaris2023.ww_ag.common.registrate.entry;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.utils.planks.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/10 15:28:54
 */
@SuppressWarnings({"unused"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class PlanksEntry<T extends WWRegistrate, E extends PlanksEntry<T, E>> implements
        IPlanks<T, E>,
        ILog<T, E>,
        IWood<T, E>,
        IButton<T, E>,
        IDoor<T, E>,
        IFence<T, E>,
        ILeaves<T, E>,
        ISign<T, E>,
        IPressurePlate<T,E>,
        ISlab<T, E>,
        IStairs<T, E>,
        IBoat<T, E>{
    public final T registrate;

    public final WoodType wt;
    public final BlockSetType bst;
    public final Supplier<EnumProxy<Boat.Type>> btp;

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
    public BlockEntry<LeavesBlock> leaves;
    public BlockEntry<StandingSignBlock> sign;
    public BlockEntry<CeilingHangingSignBlock> hanging_sign;
    public BlockEntry<WallSignBlock> wall_sign;
    public BlockEntry<WallHangingSignBlock> wall_hanging_sign;
    public BlockEntry<PressurePlateBlock> pressure_plate;
    public BlockEntry<SlabBlock> slab;
    public BlockEntry<StairBlock> stairs;
    public ItemEntry<BoatItem> boat;
    public ItemEntry<BoatItem> chest_boat;

    public String firstUpName() {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }
    public PlanksEntry(T registrate, String name, Supplier<EnumProxy<Boat.Type>> btp) {
        this.registrate = registrate;
        this.name = name;
        logsRl = ResourceLocation.fromNamespaceAndPath("c", name + "_logs");
        blockLogs = BlockTags.create(logsRl);
        itemLogs = ItemTags.create(logsRl);
        var tName = registrate.getModid() + "_" + name;
        bst = BlockSetType.register(new BlockSetType(tName));
        wt = WoodType.register(new WoodType(tName, bst));
        this.btp = btp;
    }

    @Override
    public E setPlanks(BlockEntry<RotatedPillarBlock> entry) {
        if (planks != null) throw new IllegalArgumentException("%s_planks is registered!".formatted(name));
        planks = entry;
        return ww_ag$self();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E ww_ag$self() {
        return (E) this;
    }

    public E defTag(ResourceKey<CreativeModeTab> key) {
        registrate.defaultCreativeTab(key);
        return ww_ag$self();
    }

    @Override
    public E setLog(BlockEntry<RotatedPillarBlock> entry) {
        if (log != null) throw new IllegalArgumentException("%s_log is registered!".formatted(name));
        log = entry;
        return ww_ag$self();
    }

    @Override
    public E setStrippedLog(BlockEntry<RotatedPillarBlock> entry) {
        if (stripped_log != null) throw new IllegalArgumentException("stripped_%s_log is registered!".formatted(name));
        stripped_log = entry;
        return ww_ag$self();
    }

    @Override
    public E setWood(BlockEntry<RotatedPillarBlock> entry) {
        if (wood != null) throw new IllegalArgumentException("%s_wood is registered!".formatted(name));
        wood = entry;
        return ww_ag$self();
    }

    @Override
    public E setStrippedWood(BlockEntry<RotatedPillarBlock> entry) {
        if (stripped_wood != null) throw new IllegalArgumentException("stripped_%s_wood is registered!".formatted(name));
        stripped_wood = entry;
        return ww_ag$self();
    }

    @Override
    public E setButton(BlockEntry<ButtonBlock> entry) {
        if (button != null) throw new IllegalArgumentException("%s_button is registered!".formatted(name));
        button = entry;
        return ww_ag$self();
    }

    @Override
    public E setDoor(BlockEntry<DoorBlock> entry) {
        if (door != null) throw new IllegalArgumentException("%s_door is registered!".formatted(name));
        door = entry;
        return ww_ag$self();
    }

    @Override
    public E setTrapDoor(BlockEntry<TrapDoorBlock> entry) {
        if (trapDoor != null) throw new IllegalArgumentException("%s_trap_door is registered!".formatted(name));
        trapDoor = entry;
        return ww_ag$self();
    }

    @Override
    public E setFenceGate(BlockEntry<FenceGateBlock> entry) {
        if (fence_gate != null) throw new IllegalArgumentException("%s_fence_gate is registered!".formatted(name));
        fence_gate = entry;
        return ww_ag$self();
    }

    @Override
    public E setFence(BlockEntry<FenceBlock> entry) {
        if (fence != null) throw new IllegalArgumentException("%s_fence is registered!".formatted(name));
        fence = entry;
        return ww_ag$self();
    }

    @Override
    public E setLeaves(BlockEntry<LeavesBlock> entry) {
        if (leaves != null) throw new IllegalArgumentException("%s_leaves is registered!".formatted(name));
        leaves = entry;
        return ww_ag$self();
    }

    @Override
    public E setSign(BlockEntry<StandingSignBlock> entry) {
        if (sign != null) throw new IllegalArgumentException("%s_sign is registered!".formatted(name));
        sign = entry;
        return ww_ag$self();
    }

    @Override
    public E setHangingSign(BlockEntry<CeilingHangingSignBlock> entry) {
        if (hanging_sign != null) throw new IllegalArgumentException("%s_hanging_sign is registered!".formatted(name));
        hanging_sign = entry;
        return ww_ag$self();
    }

    @Override
    public E setWallSign(BlockEntry<WallSignBlock> entry) {
        if (wall_sign != null) throw new IllegalArgumentException("%s_wall_sign is registered!".formatted(name));
        wall_sign = entry;
        return ww_ag$self();
    }

    @Override
    public E setWallHangingSign(BlockEntry<WallHangingSignBlock> entry) {
        if (wall_hanging_sign != null) throw new IllegalArgumentException("%s_wall_hanging_sign is registered!".formatted(name));
        wall_hanging_sign = entry;
        return ww_ag$self();
    }

    @Override
    public E setPressurePlate(BlockEntry<PressurePlateBlock> entry) {
        if (pressure_plate != null) throw new IllegalArgumentException("%s_pressure_plate is registered!".formatted(name));
        pressure_plate = entry;
        return ww_ag$self();
    }

    @Override
    public E setSlab(BlockEntry<SlabBlock> entry) {
        if (slab != null) throw new IllegalArgumentException("%s_slab is registered!".formatted(name));
        slab = entry;
        return ww_ag$self();
    }

    @Override
    public E setStairs(BlockEntry<StairBlock> entry) {
        if (stairs != null) throw new IllegalArgumentException("%s_stairs is registered!".formatted(name));
        stairs = entry;
        return ww_ag$self();
    }

    @Override
    public E setBoat(ItemEntry<BoatItem> entry) {
        if (boat != null) throw new IllegalArgumentException("%s_boat is registered!".formatted(name));
        boat = entry;
        return ww_ag$self();
    }

    @Override
    public E setChestBoat(ItemEntry<BoatItem> entry) {
        if (chest_boat != null) throw new IllegalArgumentException("%s_chest_boat is registered!".formatted(name));
        chest_boat = entry;
        return ww_ag$self();
    }
    public E register() {
        Objects.requireNonNull(registrate.getModEventBus())
                .addListener((BlockEntityTypeAddBlocksEvent event) -> {
            event.modify(BlockEntityType.SIGN,
                    sign.get(),
                    wall_sign.get());
            event.modify(BlockEntityType.HANGING_SIGN,
                    hanging_sign.get(),
                    wall_hanging_sign.get());
        });

        return ww_ag$self();
    }
}
