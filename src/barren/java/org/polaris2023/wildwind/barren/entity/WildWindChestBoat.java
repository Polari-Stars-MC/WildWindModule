package org.polaris2023.wildwind.barren.entity;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Supplier;

/**
 * 可在岩浆上行驶的运输船，行为与 {@link WildWindBoat} 一致。
 */
public class WildWindChestBoat extends ChestBoat {
    public WildWindChestBoat(EntityType<? extends ChestBoat> type, Level level, Supplier<Item> dropItem) {
        super(type, level, dropItem);
    }

    @Override
    public boolean canBoatInFluid(FluidState state) {
        return super.canBoatInFluid(state) || state.is(FluidTags.LAVA);
    }
}
