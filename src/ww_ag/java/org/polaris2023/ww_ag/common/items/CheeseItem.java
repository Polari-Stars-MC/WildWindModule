package org.polaris2023.ww_ag.common.items;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author baka4n
 * {@code @Date 2025/05/25 15:34:04}
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CheeseItem extends Item {
    public CheeseItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(stack, level, livingEntity);
        List<MobEffectInstance> activeEffects = List.copyOf(livingEntity.getActiveEffects());
        activeEffects.stream().filter(instance -> !instance.getEffect().value().isBeneficial())
                .forEach(instance -> {
                    livingEntity.removeEffect(instance.getEffect());
                });// remove debuff
        return stack;
    }
}
