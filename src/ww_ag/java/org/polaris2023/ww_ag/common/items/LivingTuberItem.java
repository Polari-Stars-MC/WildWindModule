package org.polaris2023.ww_ag.common.items;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.tags.WWSoundTags;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 12:46:06}
 */
@SuppressWarnings("CodeBlock2Expr")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = WWAgMod.MODID)
public class LivingTuberItem extends Item {
    public LivingTuberItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void tick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity item) {
            RandomSource random = item.getRandom();
            Level level = item.level();
            if (level.getGameTime() % 200 == 0) {
                WWSoundTags.GLARE$AMBIENT.tagFor(named -> {
                    level.playLocalSound(item.getX(), item.getY(), item.getZ(), named.get(random.nextInt(named.size())).value(), SoundSource.AMBIENT, 1.0F, 1.0F, true);
                });
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        RandomSource random = entity.getRandom();
        if (level.getGameTime() % 200 == 0) {
            WWSoundTags.GLARE$AMBIENT.tagFor(named -> level.playLocalSound(
                    entity.getX(), entity.getY(), entity.getZ(),
                    named.get(random.nextInt(named.size())).value(),
                    SoundSource.AMBIENT,
                    1, 1,
                    true));
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
        RandomSource random = livingEntity.getRandom();
        if (remainingUseDuration % 20 == 0) {
            WWSoundTags.GLARE$AMBIENT.tagFor(named -> level.playLocalSound(
                    livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    named.get(random.nextInt(named.size())).value(),
                    SoundSource.AMBIENT,
                    1, 1,
                    true));
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        super.onDestroyed(itemEntity, damageSource);
        RandomSource random = itemEntity.getRandom();
        WWSoundTags.GLARE$DEATH.tagFor(named ->
                itemEntity.playSound(named.get(random.nextInt(named.size())).value()));
    }
}
