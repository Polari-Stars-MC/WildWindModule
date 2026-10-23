package org.polaris2023.wildwind.core.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.polaris2023.wildwind.core.CoreMod;

@EventBusSubscriber(modid = CoreMod.MOD_ID)
public final class FarmlandProtection {
    private FarmlandProtection() {}

    @SubscribeEvent
    public static void onTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.hasEffect(MobEffects.SLOW_FALLING)) {
            event.setCanceled(true);
            return;
        }
        var featherFalling = entity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FEATHER_FALLING);
        for (var slot : EquipmentSlot.VALUES) {
            if (slot.isArmor() && EnchantmentHelper.getItemEnchantmentLevel(featherFalling, entity.getItemBySlot(slot)) > 0) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
