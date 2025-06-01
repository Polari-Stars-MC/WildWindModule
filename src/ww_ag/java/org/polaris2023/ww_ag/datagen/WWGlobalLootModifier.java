package org.polaris2023.ww_ag.datagen;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author baka4n
 * {@code @Date 2025/05/23 15:42:05}
 */
@MethodsReturnNonnullByDefault
public class WWGlobalLootModifier extends GlobalLootModifierProvider implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;
    public WWGlobalLootModifier(AbstractRegistrate<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, parent.getModid());
        this.parent = parent;
    }


    @SafeVarargs
    public final LootItemCondition anyOf(ResourceKey<LootTable>... lootTables) {
        return AnyOfCondition.anyOf(
                Arrays.stream(lootTables)
                        .map(ResourceKey::location)
                        .map(LootTableIdCondition::builder)
                        .toArray(LootTableIdCondition.Builder[]::new)
        ).build();
    }



    @Override
    protected void start() {
        parent.genData(WWProviderType.GLM, this);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }
}
