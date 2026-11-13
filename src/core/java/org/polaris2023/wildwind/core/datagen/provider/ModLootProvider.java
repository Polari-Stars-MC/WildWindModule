package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.polaris2023.wildwind.core.datagen.provider.loot.BlockLoot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 核心模块的战利品表入口（核心暂无自带掉落的实体，船都是 noLootTable）。
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
                        new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider
        );
    }
}
