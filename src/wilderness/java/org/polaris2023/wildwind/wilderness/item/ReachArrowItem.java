package org.polaris2023.wildwind.wilderness.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.wilderness.entity.ReachArrow;
import org.polaris2023.wildwind.wilderness.registry.ModPotions;

public class ReachArrowItem extends TippedArrowItem {
    public ReachArrowItem(Properties properties) {
        super(properties.component(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.REACH))
                .component(DataComponents.POTION_DURATION_SCALE, 0.125F));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity owner, @Nullable ItemStack weapon) {
        var arrow = new ReachArrow(level, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), stack, weapon);
        arrow.setOwner(owner);
        return arrow;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        var arrow = new ReachArrow(level, position.x(), position.y(), position.z(), stack, null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}
