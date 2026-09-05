package org.polaris2023.wildwind.hfas.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
import org.polaris2023.wildwind.hfas.registry.ModEntities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootProvider extends LootTableProvider {
    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(ModEntityLootSubProvider::new, LootContextParamSets.ENTITY)
                ),
                lookupProvider
        );
    }

    private static final class ModEntityLootSubProvider extends EntityLootSubProvider {
        ModEntityLootSubProvider(HolderLookup.Provider registries) {
            super(FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return ModEntities.ENTITY_TYPES.getEntries()
                    .stream()
                    .map(DeferredHolder::get);
        }

        @Override
        public void generate() {
            this.emptyLoot(ModEntities.MUDCRAB);
        }

        private <T extends Entity> void emptyLoot(DeferredHolder<EntityType<?>, EntityType<T>> entityType) {
            this.add(entityType.get(), LootTable.lootTable());
        }
    }
}
