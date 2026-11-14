package org.polaris2023.wildwind.wilderness.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;
import org.polaris2023.wildwind.imp.data.ImpLootWriter;
import org.polaris2023.wildwind.wilderness.block.WildernessBlocks;
import org.polaris2023.wildwind.wilderness.data.WildernessLootSpecs;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 荒野更新的战利品表：薄适配（ADR-0005）。
 *
 * <p>要生成什么在 {@link WildernessLootSpecs}，"怎么发射"在 {@link ImpLootWriter}。
 * 方块侧只把 26.x 的 {@code protected} 助手 {@code dropSelf} 转出去；
 * 实体侧交出注册表视图、{@code shouldSmeltLoot()} 条件与 {@code protected} 的 {@code add(...)}。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class WildernessLootProvider extends LootTableProvider {

    public WildernessLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(WildernessEntityLoot::new, LootContextParamSets.ENTITY),
                        new SubProviderEntry(WildernessBlockLoot::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider
        );
    }

    private static final class WildernessEntityLoot extends EntityLootSubProvider {
        WildernessEntityLoot(HolderLookup.Provider registries) {
            super(FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return ModEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
        }

        @Override
        public void generate() {
            ImpLootWriter.apply(WildernessLootSpecs.entityLoot(), new ImpLootWriter.Sinks() {

                @Override
                public void addEntity(EntityType<?> type, LootTable.Builder table) {
                    WildernessEntityLoot.this.add(type, table);
                }

                @Override
                public Object registries() {
                    return WildernessEntityLoot.this.registries;
                }

                @Override
                public Object smeltLoot() {
                    return WildernessEntityLoot.this.shouldSmeltLoot();
                }
            });
        }
    }

    private static final class WildernessBlockLoot extends net.minecraft.data.loot.BlockLootSubProvider {
        WildernessBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected @NonNull Iterable<Block> getKnownBlocks() {
            return WildernessBlocks.BLOCKS.getEntries().stream().map(h -> (Block) h.value()).toList();
        }

        @Override
        protected void generate() {
            ImpLootWriter.apply(WildernessLootSpecs.blockLoot(), new ImpLootWriter.Sinks() {

                @Override
                public void dropSelf(Block block) {
                    WildernessBlockLoot.this.dropSelf(block);
                }
            });
        }
    }
}
