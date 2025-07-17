package org.polaris2023.ww_ag.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.ModConfigs;

import static org.polaris2023.ww_ag.common.init.ModItems.FISH_BONE;

/**
 * @author baka4n
 * @code @Date 2025/6/3 16:58:41
 */
@EventBusSubscriber(modid = WWAgMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LivingEvents {

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {

        ItemStack stack = event.getItem();
        Item item = stack.getItem();
        LivingEntity livingEntity = event.getEntity();

        if (!(livingEntity.level() instanceof ServerLevel serverLevel)) return;

        if(
                ModConfigs.USE_ITEM.fish_bone_loot().get() && (stack.is(Tags.Items.FOODS_COOKED_FISH) ||
                        stack.is(Tags.Items.FOODS_RAW_FISH))
        ) {
            if (livingEntity instanceof Player player) {
                player.addItem(FISH_BONE.asStack());
            }
        }

    }
}
