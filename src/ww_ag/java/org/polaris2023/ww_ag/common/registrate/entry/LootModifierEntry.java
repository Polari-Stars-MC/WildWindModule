package org.polaris2023.ww_ag.common.registrate.entry;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * @author baka4n
 * {@code @Date 2025/05/23 14:51:09}
 */
public class LootModifierEntry<T extends IGlobalLootModifier> extends RegistryEntry<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> {

    public LootModifierEntry(AbstractRegistrate<?> owner, DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> key) {
        super(owner, key);
    }
}
