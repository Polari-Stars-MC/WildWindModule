package org.polaris2023.ww_ag.datagen.loot;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateLootTables;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author baka4n
 * {@code @Date 2025/05/23 18:44:27}
 */
@ParametersAreNonnullByDefault
public class WWBaseLootSubProvider implements RegistrateLootTables {
    public final HolderLookup.Provider registry;
    public final AbstractRegistrate<?> registrate;
    public final Consumer<WWBaseLootSubProvider> wwBaseLootSubProviderConsumer;
    public final Map<ResourceKey<LootTable>, LootTable.Builder> loots = new HashMap<>();
    public WWBaseLootSubProvider(HolderLookup.Provider provider, AbstractRegistrate<?> parent, Consumer<WWBaseLootSubProvider> consumer) {
        this.registry = provider;
        this.registrate = parent;
        this.wwBaseLootSubProviderConsumer = consumer;

    }

    public final AnyOfCondition.Builder shouldSmeltLoot() {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registry.lookupOrThrow(Registries.ENCHANTMENT);
        return AnyOfCondition.anyOf(
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))
                ),
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.DIRECT_ATTACKER,
                        EntityPredicate.Builder.entity().equipment(
                                EntityEquipmentPredicate.Builder.equipment().mainhand(
                                        ItemPredicate.Builder.item().withSubPredicate(
                                                ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(
                                                        List.of(new EnchantmentPredicate(registrylookup.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public void add(ResourceKey<LootTable> lootTable, LootTable.Builder lootTableBuilder) {
        loots.put(lootTable, lootTableBuilder);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        wwBaseLootSubProviderConsumer.accept(this);
        for (Map.Entry<ResourceKey<LootTable>, LootTable.Builder> entry : loots.entrySet()) {
            biConsumer.accept(entry.getKey(), entry.getValue());
        }

    }
}
