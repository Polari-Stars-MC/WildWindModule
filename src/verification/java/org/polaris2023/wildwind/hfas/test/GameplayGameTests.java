package org.polaris2023.wildwind.hfas.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.wilderness.effect.ModEffects;
import org.polaris2023.wildwind.core.enchantment.MeltingEnchantment;
import org.polaris2023.wildwind.core.event.CobwebBreaking;
import org.polaris2023.wildwind.core.recipe.WoolToStringRecipe;

public final class GameplayGameTests {
    private GameplayGameTests() {}
    public static void register(IEventBus bus) {
        DeferredRegister<Consumer<GameTestHelper>> tests = DeferredRegister.create(Registries.TEST_FUNCTION, "wild_wind_tests");
        tests.register("wool_recipe", () -> GameplayGameTests::woolRecipe);
        tests.register("crafter_shears", () -> GameplayGameTests::crafterShears);
        tests.register("fluid_effects", () -> GameplayGameTests::fluidEffects);
        tests.register("fluid_entity_rules", () -> FluidEntityRulesGameTests::run);
        tests.register("land_choke", () -> FluidEntityRulesGameTests::landChoke);
        tests.register("azalea_signs", () -> AzaleaAndArrowGameTests::signs);
        tests.register("azalea_shelf", () -> AzaleaAndArrowGameTests::shelf);
        tests.register("azalea_shelf_chain", () -> AzaleaAndArrowGameTests::shelfChain);
        tests.register("reach_arrow_acquisition", () -> AzaleaAndArrowGameTests::acquisition);
        tests.register("reach_arrows", () -> AzaleaAndArrowGameTests::arrows);
        tests.register("kiln", () -> KilnGameTests::cooking);
        tests.register("kiln_inventory", () -> KilnGameTests::inventory);
        tests.register("kiln_villages", () -> KilnGameTests::villages);
        tests.register("upstream_integration", () -> UpstreamIntegrationGameTests::run);
        tests.register("cobweb_timers", () -> GameplayGameTests::cobwebTimers);
        tests.register("cobweb_tick_event", () -> GameplayGameTests::cobwebTickEvent);
        tests.register("melting", () -> GameplayGameTests::melting);
        tests.register("skeleton_spawns", () -> SkeletonAndFarmlandGameTests::skeletonSpawns);
        tests.register("skeleton_melee", () -> SkeletonAndFarmlandGameTests::skeletonMelee);
        tests.register("farmland_protection", () -> SkeletonAndFarmlandGameTests::farmland);
        tests.register("creative_tab", () -> SkeletonAndFarmlandGameTests::creativeTab);
        tests.register("inspection_blocks", () -> BookInspectionGameTests::blocks);
        tests.register("inspection_interactions", () -> BookInspectionGameTests::interactions);
        tests.register("inspection_sulfur", () -> BookInspectionGameTests::sulfur);
        tests.register(bus);
    }
    private static void check(GameTestHelper h, boolean ok, String message) {
        h.assertTrue(ok, Component.literal(message));
    }

    private static void woolRecipe(GameTestHelper h) {
        var level = h.getLevel();
        var recipe = WoolToStringRecipe.INSTANCE;
        var shears = new ItemStack(Items.SHEARS);
        shears.set(DataComponents.CUSTOM_NAME, Component.literal("Test shears"));
        for (var wool : level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.WOOL)) {
            for (int woolSlot = 0; woolSlot < 4; woolSlot++) {
                var stacks = new ArrayList<>(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));
                stacks.set(woolSlot, new ItemStack(wool));
                stacks.set((woolSlot + 1) % 4, shears.copy());
                var input = CraftingInput.of(2, 2, stacks);
                var found = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level);
                check(h, found.isPresent() && found.get().value() instanceof WoolToStringRecipe, "All wool colors and slot orders must match registered recipe");
                check(h, recipe.assemble(input).is(Items.STRING) && recipe.assemble(input).getCount() == 2, "One wool must give two string");
                var remainder = recipe.remainingItems(input, level).stream().filter(s -> !s.isEmpty()).findFirst().orElseThrow();
                check(h, remainder.getDamageValue() == 1 && remainder.getHoverName().getString().equals("Test shears"), "Shears must retain data and take one damage");
                check(h, shears.getDamageValue() == 0, "Crafting must not mutate ingredient during preview");
            }
        }
        check(h, !recipe.matches(CraftingInput.of(2, 2, List.of(new ItemStack(Items.WOOL.white()), shears, new ItemStack(Items.DIRT), ItemStack.EMPTY)), level), "Extra ingredients must fail");
        check(h, !recipe.matches(CraftingInput.of(2, 1, List.of(new ItemStack(Items.WOOL.white()), new ItemStack(Items.STRING))), level), "Wrong tool must fail");
        var broken = shears.copy(); broken.setDamageValue(broken.getMaxDamage() - 1);
        check(h, recipe.remainingItems(CraftingInput.of(2, 1, List.of(new ItemStack(Items.WOOL.white()), broken)), level).stream().allMatch(ItemStack::isEmpty), "Last durability must break the shears");
        var enchanted = shears.copy();
        var unbreaking = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING);
        enchanted.enchant(unbreaking, 3);
        int damage = 0;
        for (int i = 0; i < 256; i++) {
            var remainder = recipe.remainingItems(CraftingInput.of(2, 1, List.of(new ItemStack(Items.WOOL.white()), enchanted)), level).get(1);
            check(h, remainder.getEnchantments().getLevel(unbreaking) == 3, "Unbreaking must survive crafting");
            damage += remainder.getDamageValue();
        }
        check(h, damage > 0 && damage < 160, "Unbreaking III must probabilistically reduce crafting damage");
        var player = (ServerPlayer) h.makeMockServerPlayer(GameType.SURVIVAL);
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        level.getServer().getPlayerList().placeNewPlayer(connection, player,
                net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false));
        var menu = new CraftingMenu(0, player.getInventory(), ContainerLevelAccess.create(level, h.absolutePos(new BlockPos(2, 1, 2))));
        menu.getSlot(1).set(new ItemStack(Items.WOOL.white(), 3));
        menu.getSlot(2).set(shears.copy());
        // Invoke the real result-slot path twice, including CommonHooks crafting-player context.
        for (int i = 0; i < 2; i++) {
            var result = menu.getSlot(0).remove(2);
            check(h, result.is(Items.STRING) && result.getCount() == 2, "Crafting menu must offer the registered output");
            menu.getSlot(0).onTake(player, result);
        }
        check(h, menu.getSlot(1).getItem().getCount() == 1 && menu.getSlot(2).getItem().getDamageValue() == 2, "Repeated crafts must consume one wool and one durability each");
        menu.getSlot(1).set(new ItemStack(Items.WOOL.white(), 3));
        for (int i = 0; i < 3; i++) menu.quickMoveStack(player, 0);
        check(h, menu.getSlot(1).getItem().isEmpty() && menu.getSlot(2).getItem().getDamageValue() == 5,
                "Shift-click crafts must return shears and charge durability for every wool");
        check(h, player.getInventory().countItem(Items.STRING) == 6, "Shift-click must transfer all six string");
        h.succeed();
    }

    private static void crafterShears(GameTestHelper h) {
        var pos = new BlockPos(3, 2, 3);
        h.setBlock(pos, Blocks.CRAFTER);
        var crafter = (CrafterBlockEntity) h.getLevel().getBlockEntity(h.absolutePos(pos));
        crafter.setItem(0, new ItemStack(Items.WOOL.white()));
        crafter.setItem(1, new ItemStack(Items.SHEARS));
        h.setBlock(pos.below(), Blocks.REDSTONE_BLOCK);
        h.runAfterDelay(10, () -> {
            var drops = h.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(h.absolutePos(pos)).inflate(4));
            check(h, drops.stream().anyMatch(e -> e.getItem().is(Items.STRING) && e.getItem().getCount() == 2), "Crafter must eject two string");
            check(h, drops.stream().anyMatch(e -> e.getItem().is(Items.SHEARS) && e.getItem().getDamageValue() == 1), "Crafter must eject damaged shears using server durability rules");
            h.succeed();
        });
    }

    private static void fluidEffects(GameTestHelper h) {
        for (int x = 1; x <= 3; x++) for (int y = 1; y <= 4; y++) for (int z = 1; z <= 3; z++) h.setBlock(x, y, z, Blocks.WATER);
        for (int x = 5; x <= 6; x++) for (int y = 1; y <= 4; y++) for (int z = 1; z <= 3; z++) h.setBlock(x, y, z, Blocks.LAVA);
        var pig = h.spawn(EntityTypes.PIG, 2, 2, 2); pig.setNoAi(true); pig.setNoGravity(true);
        var lavaPig = h.spawn(EntityTypes.PIG, 5, 2, 2); lavaPig.setNoAi(true); lavaPig.setNoGravity(true); lavaPig.setInvulnerable(true);
        pig.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
        pig.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200));
        lavaPig.addEffect(new MobEffectInstance(ModEffects.SINK, 200));
        h.runAfterDelay(3, () -> {
            double y = pig.getY(); pig.move(MoverType.SELF, new Vec3(0, 0.25, 0));
            check(h, pig.getY() == y, "Sink must stop actual upward water movement");
            double lavaY = lavaPig.getY(); lavaPig.move(MoverType.SELF, new Vec3(0, 0.25, 0));
            check(h, lavaPig.getY() == lavaY, "Sink must also stop lava buoyancy");
            pig.move(MoverType.SELF, new Vec3(0.05, -0.05, 0));
            check(h, pig.getY() < y, "Sink must allow downward and horizontal motion");
            pig.setAirSupply(200); CommonHooks.onLivingBreathe(pig, h.getLevel(), 1, 4);
            check(h, pig.getAirSupply() == 198, "Choke I must consume two air per normal breathing tick");
            pig.addEffect(new MobEffectInstance(ModEffects.CHOKE, 200, 1));
            pig.setAirSupply(200); CommonHooks.onLivingBreathe(pig, h.getLevel(), 1, 4);
            check(h, pig.getAirSupply() == 197, "Choke II must consume three air");
            pig.setAirSupply(200); CommonHooks.onLivingBreathe(pig, h.getLevel(), 0, 4);
            check(h, pig.getAirSupply() == 200, "Respiration's skipped consumption must remain skipped");
            pig.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200));
            CommonHooks.onLivingBreathe(pig, h.getLevel(), 1, 4);
            check(h, pig.getAirSupply() == 200, "Water breathing must continue to protect oxygen");
            pig.removeEffect(ModEffects.SINK); double before = pig.getY(); pig.move(MoverType.SELF, new Vec3(0, 0.1, 0));
            check(h, pig.getY() > before, "Removing Sink must restore upward movement");
            h.succeed();
        });
    }

    private static void moveWeb(Entity entity, int ticks, boolean falling) {
        for (int i = 0; i < ticks; i++) {
            entity.setPos(entity.getX() + (falling ? 0 : (i % 2 == 0 ? 0.001 : -0.001)), entity.getY() - (falling ? 0.001 : 0), entity.getZ());
            CobwebBreaking.tick(entity);
        }
    }
    private static void cobwebTimers(GameTestHelper h) {
        var pos = new BlockPos(3, 2, 3);
        h.setBlock(pos, Blocks.COBWEB);
        var pig = h.spawn(EntityTypes.PIG, 3.5F, 2.3F, 3.5F); pig.setNoAi(true); pig.setNoGravity(true); pig.setOnGround(true);
        CobwebBreaking.tick(pig); moveWeb(pig, 29, false);
        h.assertBlockPresent(Blocks.COBWEB, pos);
        CobwebBreaking.tick(pig); // No displacement resets the counter.
        moveWeb(pig, 29, false); h.assertBlockPresent(Blocks.COBWEB, pos);
        moveWeb(pig, 1, false); h.assertBlockPresent(Blocks.AIR, pos);
        h.setBlock(pos, Blocks.COBWEB); pig.setOnGround(false);
        moveWeb(pig, 159, true); h.assertBlockPresent(Blocks.COBWEB, pos);
        moveWeb(pig, 1, true); h.assertBlockPresent(Blocks.AIR, pos);
        h.setBlock(pos, Blocks.COBWEB);
        var spider = h.spawn(EntityTypes.SPIDER, 3.5F, 2.3F, 3.5F);
        var caveSpider = h.spawn(EntityTypes.CAVE_SPIDER, 3.5F, 2.3F, 3.5F);
        for (var entity : List.of(spider, caveSpider)) { CobwebBreaking.tick(entity); moveWeb(entity, 200, false); }
        h.assertBlockPresent(Blocks.COBWEB, pos);
        for (var mode : List.of(GameType.SPECTATOR, GameType.CREATIVE)) {
            var player = h.makeMockPlayer(mode); player.getAbilities().flying = true;
            player.setPos(pig.position()); CobwebBreaking.tick(player); moveWeb(player, 200, false);
            h.assertBlockPresent(Blocks.COBWEB, pos);
        }
        // Counter belongs to the touched block, not a global web timer.
        pig.setOnGround(true); CobwebBreaking.tick(pig); moveWeb(pig, 20, false);
        pig.setPos(h.absoluteVec(new Vec3(6.5, 2.3, 3.5))); CobwebBreaking.tick(pig);
        pig.setPos(h.absoluteVec(new Vec3(3.5, 2.3, 3.5))); CobwebBreaking.tick(pig);
        moveWeb(pig, 29, false); h.assertBlockPresent(Blocks.COBWEB, pos);
        moveWeb(pig, 1, false); h.assertBlockPresent(Blocks.AIR, pos);
        h.succeed();
    }

    private static void cobwebTickEvent(GameTestHelper h) {
        var pos = new BlockPos(3, 2, 3); h.setBlock(pos, Blocks.COBWEB);
        var pig = h.spawn(EntityTypes.PIG, 3.5F, 2.3F, 3.5F); pig.setNoAi(true); pig.setNoGravity(true);
        // Actual entity tick events must drive the timer, even with no explicit calls to the handler.
        for (int i = 1; i <= 35; i++) {
            final int tick = i;
            h.runAfterDelay(i, () -> pig.setPos(pig.getX() + (tick % 2 == 0 ? 0.001 : -0.001), pig.getY(), pig.getZ()));
        }
        h.runAfterDelay(37, () -> { h.assertBlockPresent(Blocks.AIR, pos); h.succeed(); });
    }

    private static BlockDropsEvent drops(GameTestHelper h, ServerPlayer player, ItemStack tool, List<ItemStack> stacks) {
        var entities = new ArrayList<ItemEntity>();
        for (var stack : stacks) entities.add(new ItemEntity(h.getLevel(), 0, 0, 0, stack));
        var event = new BlockDropsEvent(h.getLevel(), h.absolutePos(new BlockPos(3, 1, 3)), Blocks.IRON_ORE.defaultBlockState(), null, entities, player, tool);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }
    private static int count(BlockDropsEvent event, net.minecraft.world.item.Item item) {
        return event.getDrops().stream().map(ItemEntity::getItem).filter(s -> s.is(item)).mapToInt(ItemStack::getCount).sum();
    }
    private static void melting(GameTestHelper h) {
        var level = h.getLevel();
        var player = (ServerPlayer) h.makeMockServerPlayer(GameType.SURVIVAL);
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var tool = new ItemStack(Items.DIAMOND_PICKAXE); tool.enchant(registry.getOrThrow(MeltingEnchantment.MELTING), 1);
        player.setExperienceLevels(0); player.setExperiencePoints(2); player.totalExperience = 9999;
        var event = drops(h, player, tool, List.of(new ItemStack(Items.RAW_IRON, 3)));
        check(h, count(event, Items.RAW_IRON) == 3 && MeltingEnchantment.availableExperience(player) == 2, "Insufficient current XP must preserve entire drop; lifetime total is not fuel");
        player.setExperiencePoints(5);
        event = drops(h, player, tool, List.of(new ItemStack(Items.RAW_IRON, 3), new ItemStack(Items.DIAMOND, 2)));
        check(h, count(event, Items.IRON_INGOT) == 3 && count(event, Items.DIAMOND) == 2 && MeltingEnchantment.availableExperience(player) == 2, "Melt only smeltable drops and charge one point per input");
        var silk = tool.copy(); silk.enchant(registry.getOrThrow(Enchantments.SILK_TOUCH), 1);
        event = drops(h, player, silk, List.of(new ItemStack(Items.IRON_ORE)));
        check(h, count(event, Items.IRON_ORE) == 1 && MeltingEnchantment.availableExperience(player) == 2, "Silk Touch must take precedence without XP use");
        player.setExperienceLevels(1); player.setExperiencePoints(0);
        event = drops(h, player, tool, List.of(new ItemStack(Items.RAW_IRON)));
        check(h, count(event, Items.IRON_INGOT) == 1 && player.experienceLevel == 0 && MeltingEnchantment.availableExperience(player) == 6, "Spend exactly one point across level boundary");
        var fortune = tool.copy(); fortune.enchant(registry.getOrThrow(Enchantments.FORTUNE), 3);
        player.setExperienceLevels(10); player.setExperiencePoints(0);
        var loot = Block.getDrops(Blocks.IRON_ORE.defaultBlockState(), level, h.absolutePos(new BlockPos(3, 1, 3)), null, player, fortune);
        int inputCount = loot.stream().filter(s -> s.is(Items.RAW_IRON)).mapToInt(ItemStack::getCount).sum();
        long before = MeltingEnchantment.availableExperience(player);
        event = drops(h, player, fortune, loot);
        check(h, inputCount > 0 && count(event, Items.IRON_INGOT) == inputCount && MeltingEnchantment.availableExperience(player) == before - inputCount, "Fortune loot must be determined first and fully charged");
        for (int currentLevel : new int[]{16, 17, 30, 31, 32}) {
            player.setExperienceLevels(currentLevel); player.setExperiencePoints(0); before = MeltingEnchantment.availableExperience(player);
            event = drops(h, player, tool, List.of(new ItemStack(Items.RAW_IRON)));
            check(h, count(event, Items.IRON_INGOT) == 1 && MeltingEnchantment.availableExperience(player) == before - 1, "XP formula at level " + currentLevel);
        }
        // Exercise vanilla block dropping, rather than only posting the event directly.
        var pos = h.absolutePos(new BlockPos(5, 2, 5)); before = MeltingEnchantment.availableExperience(player);
        Block.dropResources(Blocks.IRON_ORE.defaultBlockState(), level, pos, null, player, tool);
        check(h, MeltingEnchantment.availableExperience(player) == before - 1
                && level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1)).stream().anyMatch(e -> e.getItem().is(Items.IRON_INGOT)),
                "Vanilla block loot path must invoke Melting");
        h.succeed();
    }
}
