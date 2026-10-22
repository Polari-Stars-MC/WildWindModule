package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.registry.ModCreativeTabs;
import org.polaris2023.wildwind.hfas.registry.ModItems;
import org.polaris2023.wildwind.hfas.registry.ModPotions;

final class SkeletonAndFarmlandGameTests {
    private SkeletonAndFarmlandGameTests() {}

    private static void check(GameTestHelper h, boolean ok, String message) {
        h.assertTrue(ok, Component.literal(message));
    }

    static void skeletonSpawns(GameTestHelper h) {
        var level = h.getLevel();
        level.getRandom().setSeed(428136L);
        for (var type : List.of(EntityTypes.SKELETON, EntityTypes.STRAY, EntityTypes.BOGGED, EntityTypes.PARCHED)) {
            int swords = 0;
            for (int i = 0; i < 200; i++) {
                // Use the real initialization path used by spawn eggs and newly spawned mobs.
                var skeleton = type.create(level, null, h.absolutePos(new BlockPos(2, 2, 2)), EntitySpawnReason.NATURAL, false, false);
                check(h, skeleton != null, "Skeleton variant must initialize");
                var weapon = skeleton.getMainHandItem();
                check(h, weapon.is(Items.BOW) || weapon.is(Items.STONE_SWORD), "Spawn weapon must be bow or stone sword");
                boolean melee = skeleton.getGoalSelector().getAvailableGoals().stream()
                        .anyMatch(goal -> goal.getGoal() instanceof MeleeAttackGoal);
                check(h, melee == weapon.is(Items.STONE_SWORD), "Weapon must select the matching combat AI");
                if (weapon.is(Items.STONE_SWORD)) swords++;
                skeleton.discard();
            }
            check(h, swords >= 5 && swords <= 45, "Fixed-seed spawn sample must contain roughly ten percent swords: " + type + " = " + swords);
        }
        var wither = EntityTypes.WITHER_SKELETON.create(level, null, h.absolutePos(new BlockPos(2, 2, 2)), EntitySpawnReason.NATURAL, false, false);
        check(h, wither.getMainHandItem().is(Items.STONE_SWORD), "Wither skeleton must retain its vanilla equipment");
        wither.discard();
        h.succeed();
    }

    static void skeletonMelee(GameTestHelper h) {
        checkHit(h, h.spawn(EntityTypes.STRAY, 2, 2, 2), MobEffects.SLOWNESS, 600);
        checkHit(h, h.spawn(EntityTypes.BOGGED, 3, 2, 2), MobEffects.POISON, 80);
        checkHit(h, h.spawn(EntityTypes.PARCHED, 4, 2, 2), MobEffects.WEAKNESS, 600);
        var skeleton = h.spawn(EntityTypes.SKELETON, 5, 2, 2);
        skeleton.setNoAi(true);
        skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        var target = h.spawn(EntityTypes.COW, 5, 2, 4);
        target.setNoAi(true);
        check(h, skeleton.doHurtTarget(h.getLevel(), target) && target.getActiveEffects().isEmpty(), "Normal skeleton melee must not add a special effect");
        h.succeed();
    }

    private static void checkHit(GameTestHelper h, AbstractSkeleton skeleton, Holder<MobEffect> effect, int ticks) {
        skeleton.setNoAi(true);
        skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        var target = h.spawn(EntityTypes.COW, 2, 2, 5);
        target.setNoAi(true);
        check(h, skeleton.doHurtTarget(h.getLevel(), target), "Stone sword must successfully damage target");
        var applied = target.getEffect(effect);
        check(h, applied != null && applied.getDuration() == ticks && applied.getAmplifier() == 0,
                "Melee must apply exactly the requested duration and level");
        var invulnerable = h.spawn(EntityTypes.COW, 3, 2, 5);
        invulnerable.setNoAi(true);
        invulnerable.setInvulnerable(true);
        check(h, !skeleton.doHurtTarget(h.getLevel(), invulnerable) && !invulnerable.hasEffect(effect),
                "Failed melee hits must not apply an effect");
    }

    static void farmland(GameTestHelper h) {
        var level = h.getLevel();
        var cow = h.spawn(EntityTypes.COW, 2, 2, 2);
        cow.setNoAi(true);
        var pos = h.absolutePos(new BlockPos(3, 1, 3));
        var farmland = (FarmlandBlock) Blocks.FARMLAND;
        var featherFalling = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING);
        var boots = new ItemStack(Items.DIAMOND_BOOTS);
        boots.enchant(featherFalling, 1);
        cow.setItemSlot(EquipmentSlot.FEET, boots);
        level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
        farmland.fallOn(level, level.getBlockState(pos), pos, cow, 6);
        check(h, level.getBlockState(pos).is(Blocks.FARMLAND), "Worn Feather Falling I must protect farmland for non-player entities");
        check(h, cow.getHealth() < cow.getMaxHealth(), "Farmland protection must not cancel remaining fall damage");
        cow.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        cow.setItemSlot(EquipmentSlot.MAINHAND, boots);
        farmland.fallOn(level, level.getBlockState(pos), pos, cow, 6);
        check(h, level.getBlockState(pos).is(Blocks.DIRT), "Holding enchanted armor must not protect farmland");
        level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
        cow.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
        farmland.fallOn(level, level.getBlockState(pos), pos, cow, 6);
        check(h, level.getBlockState(pos).is(Blocks.FARMLAND), "Slow Falling must protect farmland without worn armor");
        cow.removeEffect(MobEffects.SLOW_FALLING);
        farmland.fallOn(level, level.getBlockState(pos), pos, cow, 6);
        check(h, level.getBlockState(pos).is(Blocks.DIRT), "Removing protection must restore trampling");
        h.succeed();
    }

    static void creativeTab(GameTestHelper h) {
        var params = new CreativeModeTab.ItemDisplayParameters(h.getLevel().enabledFeatures(), true, h.getLevel().registryAccess());
        var tab = ModCreativeTabs.WILD_WIND.get();
        tab.buildContents(params);
        for (var register : List.of(ModBlocks.ITEMS, ModItems.ITEMS)) {
            for (var item : register.getEntries()) {
                check(h, tab.getDisplayItems().stream().filter(stack -> stack.is(item.get())).count() == (item == ModItems.TIPPED_ARROW ? 2 : 1),
                        "Every registered mod item/arrow variant must appear once in its own tab: " + item.getId());
            }
        }
        for (var potion : ModPotions.POTIONS.getEntries()) {
            for (var item : List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW)) {
                check(h, tab.contains(org.polaris2023.wildwind.hfas.arrow.ReachArrows.convert(PotionContents.createItemStack(item, potion))), "Mod tab must include each Reach potion and arrow variant");
            }
        }
        for (var key : List.of(CreativeModeTabs.BUILDING_BLOCKS, CreativeModeTabs.NATURAL_BLOCKS,
                CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS, CreativeModeTabs.FOOD_AND_DRINKS, CreativeModeTabs.COMBAT)) {
            var vanilla = BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(key);
            vanilla.buildContents(params);
            check(h, vanilla.getDisplayItems().stream().noneMatch(stack ->
                    BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(HFASMod.MOD_ID)),
                    "Vanilla categories must not contain mod items");
            check(h, vanilla.getDisplayItems().stream().noneMatch(stack -> {
                var contents = stack.get(DataComponents.POTION_CONTENTS);
                return contents != null && contents.potion().isPresent()
                        && contents.potion().get().unwrapKey().orElseThrow().identifier().getNamespace().equals(HFASMod.MOD_ID);
            }), "Vanilla categories must not duplicate mod potions");
        }
        CreativeModeTabs.searchTab().buildContents(params);
        check(h, CreativeModeTabs.searchTab().contains(ModItems.MUDDY_PIG_SPAWN_EGG.toStack())
                && CreativeModeTabs.searchTab().contains(PotionContents.createItemStack(Items.POTION, ModPotions.REACH)),
                "Global creative search must still discover mod contents");
        h.succeed();
    }
}
