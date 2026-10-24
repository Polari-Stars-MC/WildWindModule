package org.polaris2023.wildwind.hfas.test;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.polaris2023.wildwind.core.CoreMod;
import org.polaris2023.wildwind.wilderness.entity.ReachArrow;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;
import org.polaris2023.wildwind.wilderness.registry.ModMobEffects;
import org.polaris2023.wildwind.wilderness.registry.ModPotions;

final class AzaleaAndArrowGameTests {
    private static void check(GameTestHelper h, boolean value, String message) {
        h.assertTrue(value, Component.literal(message));
    }

    static void signs(GameTestHelper h) {
        var level = h.getLevel();
        var blocks = List.of(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
        int x = 1;
        for (var block : blocks) {
            var rel = new BlockPos(x++, 2, 2);
            var pos = h.absolutePos(rel);
            var state = block.defaultBlockState();
            level.setBlock(pos, state, 2);
            check(h, level.getBlockEntity(pos) instanceof SignBlockEntity, "All sign variants must create native sign entities");
            var sign = (SignBlockEntity) level.getBlockEntity(pos);
            check(h, sign.getType().onlyOpCanSetNbt(), "Sign custom data must retain native administrator-only protection");
            check(h, sign.getType().isValid(state), "Native sign type must accept azalea blocks");
            sign.setText(new SignText().setMessage(0, Component.literal("Front")).setColor(DyeColor.RED).setHasGlowingText(true), true);
            sign.setText(new SignText().setMessage(3, Component.literal("Back")), false);
            sign.setWaxed(true);
            var saved = sign.saveWithFullMetadata(level.registryAccess());
            var loaded = (SignBlockEntity) BlockEntity.loadStatic(pos, state, saved, level.registryAccess());
            check(h, loaded != null && loaded.isWaxed() && loaded.getFrontText().hasGlowingText()
                    && loaded.getFrontText().getColor() == DyeColor.RED
                    && loaded.getBackText().getMessage(3, false).getString().equals("Back"), "Both faces, dye, glow and wax must survive saving");
            check(h, state.getPistonPushReaction() == net.minecraft.world.level.material.PushReaction.BLOCK, "Signs must block pistons");
            check(h, state.getDestroySpeed(level, pos) == 1 && block.getExplosionResistance() == 1, "Sign hardness/resistance must be one");
            check(h, state.getFlammability(level, pos, Direction.UP) == 0, "Signs must not burn in fire");
            check(h, !Block.getDrops(state, level, pos, sign).isEmpty(), "Every sign variant must drop its item");
            check(h, state.hasProperty(BlockStateProperties.WATERLOGGED), "Signs must support waterlogging");
        }
        check(h, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get().asItem().getDefaultInstance().getMaxStackSize() == 16
                && org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get().asItem().getDefaultInstance().getMaxStackSize() == 16, "Signs stack to 16");
        check(h, new ItemStack(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get()).getBurnTime(null, level.fuelValues()) == 200
                && new ItemStack(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get()).getBurnTime(null, level.fuelValues()) == 800, "Sign fuel values must match specification");
        h.succeed();
    }

    static void shelf(GameTestHelper h) {
        var level = h.getLevel();
        var rel = new BlockPos(3, 2, 3);
        var pos = h.absolutePos(rel);
        var state = org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get().defaultBlockState().setValue(ShelfBlock.FACING, Direction.NORTH);
        level.setBlock(pos, state, 3);
        var shelf = (ShelfBlockEntity) level.getBlockEntity(pos);
        check(h, shelf != null && shelf.getType().isValid(state), "Shelf must use a valid native container");
        check(h, !shelf.getAlignItemsToBottom(), "Shelf display alignment must default to centered");
        for (int mask = 0; mask < 8; mask++) {
            for (int slot = 0; slot < 3; slot++) shelf.setItem(slot, (mask & 1 << slot) == 0 ? ItemStack.EMPTY : new ItemStack(Items.DIAMOND, 64));
            check(h, state.getAnalogOutputSignal(level, pos, Direction.SOUTH) == mask, "Shelf comparator must encode all eight occupancy patterns");
            check(h, state.getAnalogOutputSignal(level, pos, Direction.NORTH) == 0, "Comparator only reads from the rear");
        }
        shelf.clearContent();
        var player = h.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.EMERALD, 32));
        h.useBlock(rel, player, new BlockHitResult(new Vec3(pos.getX() + 0.8, pos.getY() + 0.5, pos.getZ()), Direction.NORTH, pos, false));
        check(h, shelf.getItem(0).is(Items.EMERALD) && shelf.getItem(0).getCount() == 32 && player.getMainHandItem().isEmpty(), "Clicking left slot must swap the entire main-hand stack");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND, 7));
        h.useBlock(rel, player, new BlockHitResult(new Vec3(pos.getX() + 0.8, pos.getY() + 0.5, pos.getZ()), Direction.NORTH, pos, false));
        check(h, shelf.getItem(0).is(Items.DIAMOND) && player.getMainHandItem().is(Items.EMERALD), "Occupied slot must return its previous stack");
        var saved = shelf.saveWithFullMetadata(level.registryAccess());
        saved.putBoolean("align_items_to_bottom", true);
        var loaded = (ShelfBlockEntity) BlockEntity.loadStatic(pos, state, saved, level.registryAccess());
        check(h, loaded != null && loaded.getAlignItemsToBottom() && loaded.getItem(0).getCount() == 7, "Alignment and contents must persist");
        h.setBlock(3, 1, 3, Blocks.HOPPER);
        var hopper = (HopperBlockEntity) level.getBlockEntity(pos.below());
        check(h, HopperBlockEntity.suckInItems(level, hopper) && hopper.getItem(0).is(Items.DIAMOND), "Hopper must extract leftmost occupied slot");
        check(h, level.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK, pos, Direction.UP) != null, "Shelf must expose native item capability");
        check(h, state.getFlammability(level, pos, Direction.UP) == 20 && state.getFireSpreadSpeed(level, pos, Direction.UP) == 30, "Shelf fire values must be 30/20");
        check(h, new ItemStack(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get()).getBurnTime(null, level.fuelValues()) == 300, "Shelf fuel value must be 300");
        h.succeed();
    }

    static void shelfChain(GameTestHelper h) {
        var level = h.getLevel();
        for (int count = 1; count <= 3; count++) {
            for (int x = 1; x <= 4; x++) {
                h.setBlock(x, 2, 3, Blocks.AIR);
                h.setBlock(x, 1, 3, Blocks.AIR);
            }
            for (int x = 1; x <= count; x++) {
                h.setBlock(x, 1, 3, Blocks.REDSTONE_BLOCK);
                h.setBlock(x, 2, 3, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get().defaultBlockState()
                        .setValue(ShelfBlock.FACING, Direction.NORTH).setValue(ShelfBlock.POWERED, true));
            }
            var rel = new BlockPos(1, 2, 3);
            var pos = h.absolutePos(rel);
            check(h, org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get().getAllBlocksConnectedTo(level, pos).size() == count, "Powered shelf chain must connect up to three shelves");
            var player = h.makeMockPlayer(GameType.SURVIVAL);
            for (int slot = 0; slot < 9; slot++) player.getInventory().setItem(slot, new ItemStack(Items.DIAMOND, slot + 1));
            h.useBlock(rel, player, new BlockHitResult(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ()), Direction.NORTH, pos, false));
            for (int slot = 0; slot < 9; slot++) check(h, player.getInventory().getItem(slot).isEmpty() == (slot >= 9 - 3 * count), "Powered shelves must swap rightmost 3/6/9 hotbar slots");
            h.useBlock(rel, player, new BlockHitResult(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ()), Direction.NORTH, pos, false));
            for (int slot = 0; slot < 9; slot++) check(h, player.getInventory().getItem(slot).getCount() == slot + 1, "Second swap must restore exact hotbar stacks");
        }
        h.succeed();
    }

    static void arrows(GameTestHelper h) {
        var level = h.getLevel();
        var owner = h.makeMockPlayer(GameType.SURVIVAL);
        var target = EntityTypes.COW.create(level, EntitySpawnReason.COMMAND);
        for (var potion : List.of(ModPotions.REACH, ModPotions.LONG_REACH)) {
            var slots = new ArrayList<ItemStack>();
            for (int i = 0; i < 9; i++) slots.add(new ItemStack(Items.ARROW));
            slots.set(4, PotionContents.createItemStack(Items.LINGERING_POTION, potion));
            var input = CraftingInput.of(3, 3, slots);
            var recipe = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level).orElseThrow();
            var result = recipe.value().assemble(input);
            check(h, result.is(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get()) && result.getCount() == 8, "Vanilla surround recipe must produce eight mod Reach arrows");
            check(h, result.is(ItemTags.ARROWS), "Reach arrows must be valid bow and crossbow ammunition");
            var arrow = org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get().createArrow(level, result, owner, new ItemStack(Items.BOW));
            check(h, arrow.getType() == ModEntities.REACH_ARROW.get() && arrow.getOwner() == owner, "Bow must spawn the custom arrow type with owner");
            check(h, !org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get().isInfinite(result, new ItemStack(Items.BOW), owner), "Infinity must not save potion arrows");
            var hitArrow = new TestArrow(level, h.absoluteVec(new Vec3(2.5, 2.5, 2.5)), result);
            target.removeAllEffects();
            hitArrow.hit(target);
            int expected = potion == ModPotions.REACH ? 450 : 1200;
            check(h, target.getEffect(ModMobEffects.REACH).getDuration() == expected, "Arrow must apply exactly 450/1200 ticks of Reach");
            h.setBlock(2, 2, 2, Blocks.STONE);
            for (int tick = 0; tick < 599; tick++) hitArrow.tick();
            check(h, hitArrow.getPickupItemStackOrigin().is(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get()), "Potion must remain before 600 ground ticks");
            hitArrow.tick();
            check(h, hitArrow.getPickupItemStackOrigin().is(Items.ARROW) && hitArrow.getColor() == -1, "At 600 ground ticks pickup must become an ordinary arrow");
        }
        check(h, DispenserBlock.DISPENSER_REGISTRY.containsKey(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get()), "Dispenser projectile behavior must be registered");
        var dispensed = org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get().asProjectile(level, Vec3.ZERO, new ItemStack(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get()), Direction.NORTH);
        check(h, dispensed.getType() == ModEntities.REACH_ARROW.get() && ((AbstractArrow) dispensed).pickup == AbstractArrow.Pickup.ALLOWED, "Dispensed arrows must be recoverable custom arrows");
        var poison = PotionContents.createItemStack(Items.TIPPED_ARROW, Potions.POISON);
        check(h, org.polaris2023.wildwind.wilderness.arrow.ReachArrows.convert(poison) == poison, "Other potion arrows must remain vanilla");
        h.succeed();
    }

    static void acquisition(GameTestHelper h) {
        var level = h.getLevel();
        var villager = EntityTypes.VILLAGER.create(level, EntitySpawnReason.COMMAND);
        var tag = TagKey.create(Registries.POTION, Identifier.withDefaultNamespace("tradeable"));
        var potions = level.registryAccess().lookupOrThrow(Registries.POTION).getOrThrow(tag);
        check(h, potions.contains(ModPotions.REACH) && potions.contains(ModPotions.LONG_REACH), "Both Reach variants must join the survival potion trade pool");
        var params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                .withParameter(LootContextParams.THIS_ENTITY, villager)
                .withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, net.minecraft.util.Unit.INSTANCE)
                .create(LootContextParamSets.VILLAGER_TRADE);
        var context = new LootContext.Builder(params).withOptionalRandomSeed(271828L).create(java.util.Optional.empty());
        var trades = level.registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE);
        var trade = trades.getOrThrow(ResourceKey.create(Registries.VILLAGER_TRADE,
                Identifier.withDefaultNamespace("fletcher/5/arrow_and_emerald_tipped_arrow"))).value();
        var set = level.registryAccess().lookupOrThrow(Registries.TRADE_SET).getOrThrow(ResourceKey.create(Registries.TRADE_SET,
                Identifier.withDefaultNamespace("fletcher/level_5"))).value();
        check(h, set.calculateNumberOfTrades(context) == 2 && set.getTrades().size() == 3 && !set.allowDuplicates(),
                "Master fletcher must keep vanilla two-of-three trade selection");
        boolean normal = false, longReach = false, other = false;
        for (int i = 0; i < 2048; i++) {
            var offer = trade.getOffer(context);
            check(h, offer != null && offer.getBaseCostA().is(Items.EMERALD) && offer.getBaseCostA().getCount() == 2
                    && offer.getCostB().is(Items.ARROW) && offer.getCostB().getCount() == 5 && offer.getResult().getCount() == 5,
                    "Fletcher potion-arrow cost must remain two emeralds plus five arrows for five arrows");
            var result = offer.getResult();
            if (result.is(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get())) {
                var potion = result.get(DataComponents.POTION_CONTENTS).potion().orElseThrow();
                normal |= potion.is(ModPotions.REACH.getKey());
                longReach |= potion.is(ModPotions.LONG_REACH.getKey());
            } else other |= result.is(Items.TIPPED_ARROW);
        }
        check(h, normal && longReach && other, "Random trade must offer both custom Reach arrows and vanilla alternatives");
        var gift = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
                Identifier.withDefaultNamespace("gameplay/hero_of_the_village/fletcher_gift")));
        var giftParams = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                .withParameter(LootContextParams.THIS_ENTITY, villager).create(LootContextParamSets.GIFT);
        var random = net.minecraft.util.RandomSource.create(314159L);
        boolean reachGift = false, vanillaGift = false;
        for (int i = 0; i < 1024; i++) {
            var results = gift.getRandomItems(giftParams, random);
            check(h, results.size() <= 1, "Reach gift must join the existing random choice, not add an extra gift");
            for (var result : results) {
                reachGift |= result.is(org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get());
                vanillaGift |= result.is(Items.ARROW) || result.is(Items.TIPPED_ARROW);
            }
        }
        check(h, reachGift && vanillaGift, "Hero gifts must include Reach and preserve vanilla choices");
        h.succeed();
    }

    private static class TestArrow extends ReachArrow {
        TestArrow(Level level, Vec3 pos, ItemStack stack) { super(level, pos.x, pos.y, pos.z, stack, null); }
        void hit(LivingEntity target) { doPostHurtEffects(target); }
    }
}
