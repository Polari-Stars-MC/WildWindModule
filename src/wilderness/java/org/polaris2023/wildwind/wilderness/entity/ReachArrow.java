package org.polaris2023.wildwind.wilderness.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.wilderness.mixin.AbstractArrowAccessor;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;

/** Native arrow physics, potion application, persistence and 600-tick decay. */
public class ReachArrow extends Arrow {
    public ReachArrow(EntityType<? extends ReachArrow> type, Level level) {
        super(type, level);
        setPickupItemStack(new ItemStack(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get()));
    }

    public ReachArrow(Level level, double x, double y, double z, ItemStack ammo, @Nullable ItemStack weapon) {
        this(ModEntities.REACH_ARROW.get(), level);
        setPickupItemStack(ammo.copyWithCount(1));
        applyComponentsFromItemStack(ammo);
        if (ammo.has(DataComponents.INTANGIBLE_PROJECTILE)) pickup = Pickup.CREATIVE_ONLY;
        setPos(x, y, z);
        if (weapon != null && level instanceof ServerLevel serverLevel) {
            if (weapon.isEmpty()) throw new IllegalArgumentException("Empty arrow weapon");
            ((AbstractArrowAccessor) this).wildwind$setWeapon(weapon.copy());
            ((AbstractArrowAccessor) this).wildwind$setPiercing((byte) EnchantmentHelper.getPiercingCount(serverLevel, weapon, ammo));
        }
    }
}
