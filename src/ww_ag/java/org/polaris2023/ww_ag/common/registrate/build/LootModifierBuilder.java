package org.polaris2023.ww_ag.common.registrate.build;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.common.registrate.entry.LootModifierEntry;
import org.polaris2023.ww_ag.datagen.WWGlobalLootModifier;
import org.polaris2023.ww_ag.datagen.loot.WWBaseLootSubProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author baka4n
 * {@code @Date 2025/05/23 14:43:54}
 */
@SuppressWarnings({"CodeBlock2Expr", "unused"})
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LootModifierBuilder<T extends IGlobalLootModifier, P> extends AbstractBuilder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>, P, LootModifierBuilder<T, P>> {
    private final MapCodec<T> codec;
    private LootModifierBuilder(AbstractRegistrate<?> owner,
                                P parent,
                                String name,
                                BuilderCallback callback,
                                MapCodec<T> mapCodec) {
        super(owner, parent, name, callback, NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
        this.codec = mapCodec;
    }

    public static <T extends IGlobalLootModifier, P> LootModifierBuilder<T, P> create(AbstractRegistrate<?> owner,
                                                                                      P parent,
                                                                                      String name,
                                                                                      BuilderCallback callback,
                                                                                      MapCodec<T> mapCodec) {
        return new LootModifierBuilder<>(owner, parent, name, callback, mapCodec);
    }

    @Override
    protected @NonnullType MapCodec<T> createEntry() {
        return codec;
    }

    public LootModifierBuilder<T, P> add(Consumer<WWGlobalLootModifier> consumer) {
        return setData(WWProviderType.GLM, (c, p) -> {
            consumer.accept(p);
        });
    }

    public LootModifierBuilder<T, P> loot(NonNullConsumer<WWBaseLootSubProvider> consumer) {
        return setData(ProviderType.LOOT, (c, p) -> p.addLootAction(WWProviderType.BASE, consumer));
    }

    @Override
    protected LootModifierEntry<T> createEntryWrapper(DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> delegate) {
        return new LootModifierEntry<>(getOwner(), delegate);
    }

    @Override
    public LootModifierEntry<T> register() {
        return (LootModifierEntry<T>) super.register();
    }
}
