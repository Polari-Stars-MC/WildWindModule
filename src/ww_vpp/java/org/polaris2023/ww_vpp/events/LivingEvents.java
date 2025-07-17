package org.polaris2023.ww_vpp.events;

import lombok.experimental.ExtensionMethod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.ModConfigs;
import org.polaris2023.ww_ag.config.UseItemConfig;
import org.polaris2023.ww_ag.utils.TeleportUtil;
import org.polaris2023.ww_vpp.WWVppMod;

/**
 * @author baka4n
 * @code @Date 2025/6/4 08:38:47
 */
@EventBusSubscriber(modid = WWVppMod.MODID, bus = EventBusSubscriber.Bus.GAME)
@ExtensionMethod(TeleportUtil.class)
public class LivingEvents {
    @SubscribeEvent
    @SuppressWarnings("resource")
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = event.getItem();
        LivingEntity livingEntity = event.getEntity();

        if (!(livingEntity.level() instanceof ServerLevel serverLevel)) return;
        if(ModConfigs.USE_ITEM.popped_chorus_fruit().get() && stack.is(Items.POPPED_CHORUS_FRUIT)) {
            if(livingEntity.tryTeleportToSurface(serverLevel, livingEntity.getOnPos()) || livingEntity.randomTeleportAround(serverLevel)) {
                serverLevel.gameEvent(GameEvent.TELEPORT, livingEntity.position(), GameEvent.Context.of(livingEntity));
                SoundSource soundsource;
                SoundEvent soundevent;
                if (livingEntity instanceof Fox) {
                    soundevent = SoundEvents.FOX_TELEPORT;
                    soundsource = SoundSource.NEUTRAL;
                } else {
                    soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    soundsource = SoundSource.PLAYERS;
                }

                serverLevel.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundevent, soundsource);
                livingEntity.resetFallDistance();
            }

            if (livingEntity instanceof Player player) {
                player.resetCurrentImpulseContext();
                player.getCooldowns().addCooldown(stack.getItem(), 20);
            }
        } else if(ModConfigs.USE_ITEM.glistering_melon_slice().get() && stack.is(Items.GLISTERING_MELON_SLICE)) {
            livingEntity.heal(1.0F);
        }

    }
}
