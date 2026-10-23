package org.polaris2023.wildwind.hfas.test;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;
import org.polaris2023.wildwind.structures.test.StructureGameTests;

@Mod("wild_wind_tests")
public final class MuddyPigGameTests {
    public MuddyPigGameTests(IEventBus bus) {
        StructureGameTests.register(bus);
        GameplayGameTests.register(bus);
        DeferredRegister<Consumer<GameTestHelper>> functions = DeferredRegister.create(Registries.TEST_FUNCTION, "wild_wind_tests");
        functions.register("entity", () -> MuddyPigGameTests::entity);
        functions.register("spawning", () -> MuddyPigGameTests::spawning);
        functions.register(bus);
    }

    private static void check(GameTestHelper helper, boolean condition, String message) {
        helper.assertTrue(condition, Component.literal(message));
    }

    private static void entity(GameTestHelper helper) {
        var level = helper.getLevel();
        var type = ModEntities.MUDDY_PIG.get();
        var pig = helper.spawn(type, 2, 2, 2);
        var partner = helper.spawn(type, 4, 2, 2);
        check(helper, pig.getMaxHealth() == 10 && pig.getAttributeValue(Attributes.MOVEMENT_SPEED) == 0.25,
                "Muddy pig must receive vanilla pig attributes");
        check(helper, pig.isFood(Items.CARROT.getDefaultInstance()) && !pig.isFood(Items.WHEAT.getDefaultInstance()),
                "Muddy pig must use vanilla pig food tag");
        check(helper, SpawnEggItem.getType(org.polaris2023.wildwind.wilderness.registry.ModItems.MUDDY_PIG_SPAWN_EGG.toStack()) == type,
                "Spawn egg entity component must point to muddy pig");
        var baby = pig.getBreedOffspring(level, partner);
        check(helper, baby != null && baby.getType() == type, "Breeding must create a muddy pig rather than vanilla pig");
        baby.setBaby(true);
        check(helper, baby.isBaby(), "Offspring must support baby state");
        pig.setInLove(null);
        partner.setInLove(null);
        check(helper, pig.canMate(partner) && !pig.canMate(pig), "Two adult muddy pigs must be able to mate");
        var vanilla = helper.spawn(EntityTypes.PIG, 6, 2, 2);
        vanilla.setInLove(null);
        check(helper, !pig.canMate(vanilla) && !vanilla.canMate(pig), "Vanilla pigs must remain a separate breeding species");
        var eggBaby = SpawnEggItem.spawnOffspringFromSpawnEgg(helper.makeMockServerPlayerInLevel(), pig,
                type, level, pig.position(), org.polaris2023.wildwind.wilderness.registry.ModItems.MUDDY_PIG_SPAWN_EGG.toStack());
        check(helper, eggBaby.isPresent() && eggBaby.get().isBaby() && eggBaby.get().getType() == type,
                "Using the spawn egg on an adult must create a muddy pig baby");
        check(helper, level.getServer().reloadableRegistries().getLootTable(pig.getLootTable().orElseThrow())
                != net.minecraft.world.level.storage.loot.LootTable.EMPTY, "Muddy pig loot table must be loaded");
        helper.succeed();
    }

    private static void spawning(GameTestHelper helper) {
        var level = helper.getLevel();
        var type = ModEntities.MUDDY_PIG.get();
        var biomes = level.registryAccess().lookupOrThrow(Registries.BIOME);
        for (var biome : biomes.listElements().toList()) {
            var entries = biome.value().getMobSettings().getMobs(MobCategory.CREATURE).unwrap();
            var matches = entries.stream().filter(entry -> entry.value().type() == type).toList();
            boolean expected = biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP);
            check(helper, matches.size() == (expected ? 1 : 0), "Biome spawn membership: " + biome.key().identifier());
            if (expected) {
                var entry = matches.getFirst();
                check(helper, entry.weight() == 10 && entry.value().minCount() == 2 && entry.value().maxCount() == 4,
                        "Spawn weight and group bounds must be 10 / 2-4");
            }
        }
        var pos = new BlockPos(3, 2, 3);
        var absolute = helper.absolutePos(pos);
        helper.setBlock(pos, Blocks.AIR);
        helper.setBlock(pos.above(), Blocks.AIR);
        helper.setBlock(pos.above(2), Blocks.GLOWSTONE);
        helper.runAfterDelay(5, () -> {
            for (var block : new net.minecraft.world.level.block.Block[]{Blocks.GRASS_BLOCK, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS}) {
                helper.setBlock(pos.below(), block);
                check(helper, SpawnPlacements.isSpawnPositionOk(type, level, absolute)
                        && SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.NATURAL, absolute, level.getRandom()),
                        "Natural spawning must accept " + block);
            }
            helper.setBlock(pos.below(), Blocks.STONE);
            check(helper, !SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.NATURAL, absolute, level.getRandom()),
                    "Natural spawning must reject stone");
            helper.setBlock(pos.below(), Blocks.MUD);
            helper.setBlock(pos, Blocks.WATER);
            check(helper, !SpawnPlacements.isSpawnPositionOk(type, level, absolute), "Natural spawning must reject water");
            helper.setBlock(pos, Blocks.STONE);
            check(helper, !SpawnPlacements.isSpawnPositionOk(type, level, absolute), "Natural spawning must reject obstructed space");
            helper.succeed();
        });
    }
}
