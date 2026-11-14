package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.polaris2023.wildwind.core.data.ModLootSpecs;
import org.polaris2023.wildwind.core.datagen.provider.loot.BlockLoot;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.imp.data.ImpLootWriter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 核心模块的战利品表入口：薄适配（ADR-0005）。
 *
 * <p>要生成什么在 {@link ModLootSpecs}，"怎么发射"在 {@link ImpLootWriter}。
 * 实体侧的表由发射器组装（全部是公开 API），这里只交出注册表视图、
 * 26.x 的"是否烧炼后掉落"条件与 {@code protected} 的 {@code add(...)}。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class ModLootProvider extends LootTableProvider {

    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(ModEntityLootSubProvider::new, LootContextParamSets.ENTITY),
                        new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
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
            return ModEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
        }

        @Override
        public void generate() {
            ImpLootWriter.apply(ModLootSpecs.entityLoot(), new ImpLootWriter.Sinks() {

                @Override
                public void addEntity(EntityType<?> type, LootTable.Builder table) {
                    ModEntityLootSubProvider.this.add(type, table);
                }

                @Override
                public Object registries() {
                    return ModEntityLootSubProvider.this.registries;
                }
            });
        }
    }
}
