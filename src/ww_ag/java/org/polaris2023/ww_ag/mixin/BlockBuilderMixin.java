package org.polaris2023.ww_ag.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.datagen.WWDataRepeater;
import org.polaris2023.ww_ag.utils.IDatagen;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author baka4n
 * @code @Date 2025/5/28 02:06:03
 */
@Mixin(BlockBuilder.class)
public abstract class BlockBuilderMixin<T extends Block, P> extends AbstractBuilder<Block, T, P, BlockBuilder<T, P>> implements
        ILanguage<Block, T, P, BlockBuilder<T, P>>,
        IDatagen<Block, T, P, BlockBuilder<T, P>> {
    public BlockBuilderMixin(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<? extends Registry<Block>> registryKey) {
        super(owner, parent, name, callback, registryKey);
    }

    @Override
    public BlockBuilder<T, P> ww_ag$datagen(NonNullBiConsumer<DataGenContext<Block, T>, WWDataRepeater> consumer) {
        return setData(WWProviderType.REPEATER, consumer);
    }

    @Override
    public BlockBuilder<T, P> ww_ag$zh_cn(String name) {
        return setData(WWProviderType.ZH_CN, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }
    @Override
    public BlockBuilder<T, P> ww_ag$zh_tw(String name) {
        return setData(WWProviderType.ZH_TW, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }
    @Override
    public BlockBuilder<T, P> ww_ag$zh_hk(String name) {
        return setData(WWProviderType.ZH_HK, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }


}
