package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.polaris2023.wildwind.hfas.datagen.provider.loot.BlockLoot;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootProvider extends LootTableProvider {
    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ReleaseModule module) {
        super(
                output,
                Set.of(),
                providers(module),
                lookupProvider
        );
    }

    private static List<SubProviderEntry> providers(ReleaseModule module) {
        var providers = new java.util.ArrayList<SubProviderEntry>();
        providers.add(new SubProviderEntry(lookup -> new BlockLoot(lookup, module), LootContextParamSets.BLOCK));
        if (module == ReleaseModule.WILDERNESS) providers.add(new SubProviderEntry(ModEntityLootSubProvider::new, LootContextParamSets.ENTITY));
        return List.copyOf(providers);
    }

    private static final class ModEntityLootSubProvider extends EntityLootSubProvider {
        ModEntityLootSubProvider(HolderLookup.Provider registries) {
            super(FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return ModEntities.ENTITY_TYPES.getEntries()
                    .stream()
                    .<EntityType<?>>map(DeferredHolder::get)
                    // Checked-in muddy_pig.json delegates to vanilla pig loot at runtime.
                    .filter(type -> type != ModEntities.MUDDY_PIG.get());
        }

        @Override
        public void generate() {
            this.emptyLoot(ModEntities.MUDCRAB);
            this.add(
                    ModEntities.PIRANHA.get(),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .add(LootItem.lootTableItem(org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get())
                                                    .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot())))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .add(LootItem.lootTableItem(Items.BONE_MEAL))
                                            .when(LootItemRandomChanceCondition.randomChance(0.05F))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .add(LootItem.lootTableItem(org.polaris2023.wildwind.wilderness.registry.ModItems.FANG.get()))
                                            .when(LootItemRandomChanceCondition.randomChance(0.05F))
                            )
            );
        }

        private <T extends Entity> void emptyLoot(DeferredHolder<EntityType<?>, EntityType<T>> entityType) {
            this.add(entityType.get(), LootTable.lootTable());
        }
    }
}
