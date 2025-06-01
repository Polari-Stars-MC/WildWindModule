package org.polaris2023.ww_ag.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.datagen.WWDataRepeater;
import org.polaris2023.ww_ag.utils.IDatagen;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static <T extends Block, P> void create(AbstractRegistrate<?> owner,
                                                    P parent,
                                                    String name,
                                                    BuilderCallback callback,
                                                    NonNullFunction<BlockBehaviour.Properties, T> factory,
                                                    CallbackInfoReturnable<BlockBuilder<T, P>> cir) {
        cir.setReturnValue(cir.getReturnValue().blockstate((c, p) -> {
            p.simpleBlock(c.getEntry(), p.models().cubeAll(p.blockTexture(c.getEntry()).getPath(), WWAgMod.REGISTRATE.loc("block/block_placeholder")));
        }));
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
