package org.polaris2023.ww_ag.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author baka4n
 * @code @Date 2025/5/28 01:11:24
 */
@Mixin(ItemBuilder.class)
@SuppressWarnings("unchecked")
public abstract class ItemBuilderMixin<T extends Item, P> extends AbstractBuilder<Item, T ,P, ItemBuilder<T, P>> implements ILanguage<Item, T, P, ItemBuilder<T, P>> {

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static <T extends Item, P> void create(AbstractRegistrate<?> owner,
                               P parent,
                               String name,
                               BuilderCallback callback,
                               NonNullFunction<Item.Properties, T> factory,
                               CallbackInfoReturnable<ItemBuilder<T, P>> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .model((c, p) -> {
                    p.generated(c, WWAgMod.REGISTRATE.loc("item/item_placeholder"));
                }));
    }

    @Override
    public ItemBuilder<T, P> ww_ag$self() {
        return (ItemBuilder<T, P>) (Object) this;
    }

    public ItemBuilderMixin(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<? extends Registry<Item>> registryKey) {
        super(owner, parent, name, callback, registryKey);
    }

    @Override
    public ILanguage<Item, T, P, ItemBuilder<T, P>> ww_ag$zh_cn(String name) {
        return ILanguage.convert1(setData(WWProviderType.ZH_CN, (c, p) -> {
            p.add(c.getEntry(), name);
        }));
    }

    @Override
    public ILanguage<Item, T, P, ItemBuilder<T, P>> ww_ag$zh_hk(String name) {
        return ILanguage.convert1(setData(WWProviderType.ZH_HK, (c, p) -> {
            p.add(c.getEntry(), name);
        }));
    }
    @Override
    public ILanguage<Item, T, P, ItemBuilder<T, P>> ww_ag$zh_tw(String name) {
        return ILanguage.convert1(setData(WWProviderType.ZH_TW, (c, p) -> {
            p.add(c.getEntry(), name);
        }));
    }
}
