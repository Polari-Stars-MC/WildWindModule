package org.polaris2023.ww_ag.common.registrate.build;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.datagen.WWDataRepeater;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author baka4n
 * {@code @Date 2025/05/22 08:00:23}
 */
@SuppressWarnings({"CodeBlock2Expr", "unused"})
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockBuilder<T extends Block, P> extends com.tterrag.registrate.builders.BlockBuilder<T, P> {
    protected BlockBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<BlockBehaviour.Properties, T> factory, NonNullSupplier<BlockBehaviour.Properties> initialProperties) {
        super(owner, parent, name, callback, factory, initialProperties);

    }

    public static <T extends Block, P> BlockBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        BlockBuilder<T, P> tpBlockBuilder = new BlockBuilder<>(owner, parent, name, callback, factory, BlockBehaviour.Properties::of);
        tpBlockBuilder.defaultLoot().defaultLang().defaultBlockstate();
        return tpBlockBuilder
                .setData(WWProviderType.ZH_CN, (c, p) ->{
                    p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                })
                .setData(WWProviderType.ZH_TW, (c, p) ->{
                    p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                }).setData(WWProviderType.ZH_HK, (c, p) ->{
                    p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                });
    }

    @Override
    public <D extends RegistrateProvider> BlockBuilder<T, P> setData(ProviderType<? extends D> type, NonNullBiConsumer<DataGenContext<Block, T>, D> cons) {
        return (BlockBuilder<T, P>) super.setData(type, cons);
    }

    public BlockBuilder<T, P> datagen(NonNullBiConsumer<DataGenContext<Block, T>, WWDataRepeater> cons) {
        return setData(WWProviderType.REPEATER, cons);
    }

    public BlockBuilder<T, P> zh_cn(String name) {
        return setData(WWProviderType.ZH_CN, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }
    public BlockBuilder<T, P> zh_tw(String name) {
        return setData(WWProviderType.ZH_TW, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }
    public BlockBuilder<T, P> zh_hk(String name) {
        return setData(WWProviderType.ZH_HK, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }


}
