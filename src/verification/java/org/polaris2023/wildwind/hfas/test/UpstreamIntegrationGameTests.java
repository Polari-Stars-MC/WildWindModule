package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.polaris2023.wildwind.wilderness.component.OmniClawTools;
import org.polaris2023.wildwind.core.enchantment.MeltingEnchantment;
import org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant;
import org.polaris2023.wildwind.wilderness.registry.*;

final class UpstreamIntegrationGameTests {
    private UpstreamIntegrationGameTests() {}

    private static void check(GameTestHelper h, boolean ok, String message) {
        h.assertTrue(ok, Component.literal(message));
    }

    static void run(GameTestHelper h) {
        var level = h.getLevel();
        var crab = h.spawn(ModEntities.MUDCRAB.get(), 2, 2, 2);
        crab.setNoAi(true);
        check(h, crab.getMaxHealth() == 10 && crab.getVariant() != null,
                "Merged mudcrab must spawn with attributes and a registered variant");
        check(h, level.registryAccess().lookupOrThrow(DatadrivenRegistryKey.MUDCRAB_VARIANT).size() == 3,
                "All three mudcrab variants must be present");
        crab.setVariant(MudcrabVariant.lookup(level.registryAccess(), MudcrabVariant.WARM));
        check(h, crab.getVariant().is(MudcrabVariant.WARM)
                && crab.getBucketItemStack().is(org.polaris2023.wildwind.wilderness.registry.ModItems.MUDCRAB_BUCKET.get()), "Mudcrab variant and bucket must work");
        var fish = h.spawn(ModEntities.PIRANHA.get(), 4, 2, 2);
        fish.setNoAi(true);
        check(h, fish.getBucketItemStack().is(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA_BUCKET.get()), "Piranha bucket must remain registered");
        var cooking = level.recipeAccess().getRecipeFor(RecipeType.SMELTING,
                new SingleRecipeInput(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.toStack()), level);
        check(h, cooking.isPresent() && cooking.get().value().assemble(new SingleRecipeInput(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.toStack()))
                .is(org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA.get()), "Upstream piranha smelting recipe must load");
        for (var material : org.polaris2023.wildwind.core.registry.ModItems.MATERIALS) {
            check(h, !material.toStack().has(DataComponents.FOOD), "The seven local materials must stay ordinary items");
        }
        check(h, !ModPotions.REACH.value().getEffects().isEmpty(), "Upstream Reach potion must remain registered");

        var player = (ServerPlayer) h.makeMockServerPlayer(GameType.SURVIVAL);
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        level.getServer().getPlayerList().placeNewPlayer(connection, player,
                net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false));
        player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
        player.setPos(h.absoluteVec(new Vec3(6.5, 2, 6.5)));
        player.addEffect(new MobEffectInstance(ModMobEffects.REACH, 200));
        check(h, player.getAttributeValue(ModAttributes.EXTRA_ITEM_PICKUP_RANGE) == 2,
                "Upstream Reach attribute must work with merged player code");
        player.removeEffect(ModMobEffects.REACH);

        var pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
        pickaxe.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(MeltingEnchantment.MELTING), 1);
        var claw = org.polaris2023.wildwind.wilderness.registry.ModItems.OMNI_CLAW.toStack();
        claw.set(ModDataComponents.OMNI_CLAW_TOOLS.get(), new OmniClawTools(List.of(pickaxe), 0));
        player.setItemInHand(InteractionHand.MAIN_HAND, claw);
        player.setExperienceLevels(1);
        player.setExperiencePoints(0);
        var pos = new BlockPos(5, 2, 5);
        h.setBlock(pos, Blocks.IRON_ORE);
        check(h, player.gameMode.destroyBlock(h.absolutePos(pos)), "Omni Claw must break a real block through the player path");
        check(h, MeltingEnchantment.availableExperience(player) == 6,
                "Melting on an Omni Claw's internal pickaxe must consume exactly one XP point");
        check(h, level.getEntitiesOfClass(ItemEntity.class, new AABB(h.absolutePos(pos)).inflate(1)).stream()
                .anyMatch(e -> e.getItem().is(Items.IRON_INGOT)), "Omni Claw and Melting must produce smelted loot together");
        h.succeed();
    }
}
