package org.polaris2023.wildwind.barren.entity;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Supplier;

/**
 * 可在岩浆上行驶的船。
 * <p>
 * 原版 {@code AbstractBoat} 的浮力、划桨、下沉判定全部走 {@code canBoatInFluid(FluidState)}：
 * {@code getStatus()} / {@code checkInWater()} / {@code isUnderwater()} / {@code getWaterLevelAbove()} /
 * {@code checkFallDamage()} / {@code canAddPassenger()}。这里把岩浆也算作可航行流体，
 * 于是岩浆里同样有浮力、能划桨，且不会因为「水下失控」把乘客弹出去。
 * <p>
 * 禁止在 {@link #canBoatInFluid(FluidState)} 内调用 {@code getWaterLevelAbove()} / {@code isUnderWater()}：
 * 它们内部会回调本方法，造成栈溢出。
 */
public class WildWindBoat extends Boat {
    public WildWindBoat(EntityType<? extends Boat> type, Level level, Supplier<Item> dropItem) {
        super(type, level, dropItem);
    }

    @Override
    public boolean canBoatInFluid(FluidState state) {
        return super.canBoatInFluid(state) || state.is(FluidTags.LAVA);
    }
}
