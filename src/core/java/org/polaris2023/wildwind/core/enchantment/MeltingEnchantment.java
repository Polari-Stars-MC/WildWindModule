package org.polaris2023.wildwind.core.enchantment;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.polaris2023.wildwind.core.CoreMod;

@EventBusSubscriber(modid = CoreMod.MOD_ID)
public final class MeltingEnchantment {
    public static final ResourceKey<Enchantment> MELTING = ResourceKey.create(Registries.ENCHANTMENT,
            Identifier.fromNamespaceAndPath(CoreMod.MOD_ID, "melting"));
    private MeltingEnchantment() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void meltDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof ServerPlayer player)) return;
        var registry = event.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        if (EnchantmentHelper.getItemEnchantmentLevel(registry.getOrThrow(MELTING), event.getTool()) == 0
                || EnchantmentHelper.getItemEnchantmentLevel(registry.getOrThrow(Enchantments.SILK_TOUCH), event.getTool()) > 0) return;

        // BlockDropsEvent runs after vanilla loot (including Fortune). Plan everything before spending XP.
        List<Conversion> conversions = new ArrayList<>();
        long cost = 0;
        for (var drop : event.getDrops()) {
            var stack = drop.getItem();
            var input = new SingleRecipeInput(stack.copyWithCount(1));
            var recipe = event.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, input, event.getLevel());
            if (recipe.isEmpty()) continue;
            var result = recipe.get().value().assemble(input);
            if (result.isEmpty() || ItemStack.isSameItemSameComponents(stack, result)) continue;
            long outputCount = (long) result.getCount() * stack.getCount();
            if (outputCount > Integer.MAX_VALUE) return;
            cost += stack.getCount();
            conversions.add(new Conversion(drop, result, (int) outputCount));
        }
        if (cost == 0 || cost > Integer.MAX_VALUE || availableExperience(player) < cost) return;
        if (!spendExperience(player, (int) cost)) return;

        for (var conversion : conversions) {
            event.getDrops().remove(conversion.original());
            int remaining = conversion.count();
            while (remaining > 0) {
                int count = Math.min(remaining, conversion.result().getMaxStackSize());
                var original = conversion.original();
                var drop = new ItemEntity(event.getLevel(), original.getX(), original.getY(), original.getZ(),
                        conversion.result().copyWithCount(count));
                drop.setDeltaMovement(original.getDeltaMovement());
                drop.setDefaultPickUpDelay();
                event.getDrops().add(drop);
                remaining -= count;
            }
        }
    }

    /** totalExperience is lifetime accounting and is stale after enchanting or /experience set. */
    public static long availableExperience(ServerPlayer player) {
        long level = player.experienceLevel;
        long base = level <= 16 ? level * level + 6 * level
                : level <= 31 ? (5 * level * level - 81 * level + 720) / 2
                : (9 * level * level - 325 * level + 4440) / 2;
        return base + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    private static boolean spendExperience(ServerPlayer player, int cost) {
        long before = availableExperience(player);
        int level = player.experienceLevel, total = player.totalExperience;
        int progress = Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
        player.giveExperiencePoints(-cost);
        if (availableExperience(player) == before - cost) return true;
        // Respect XP event cancellation/adjustment without providing free smelting or partial charges.
        player.setExperienceLevels(level);
        player.setExperiencePoints(progress);
        player.totalExperience = total;
        return false;
    }

    private record Conversion(ItemEntity original, ItemStack result, int count) {}
}
