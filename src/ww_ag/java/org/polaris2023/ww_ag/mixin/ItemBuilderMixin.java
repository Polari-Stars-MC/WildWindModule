package org.polaris2023.ww_ag.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author baka4n
 * @code @Date 2025/5/28 01:11:24
 */
@Mixin(ItemBuilder.class)
public abstract class ItemBuilderMixin<T extends Item, P> extends AbstractBuilder<Item, T ,P, ItemBuilder<T, P>> implements ILanguage<Item, T, P, ItemBuilder<T, P>> {


    public ItemBuilderMixin(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<? extends Registry<Item>> registryKey) {
        super(owner, parent, name, callback, registryKey);
    }

    @Override
    public ItemBuilder<T, P> ww_ag$zh_cn(String name) {
        return setData(WWProviderType.ZH_CN, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }

    @Override
    public ItemBuilder<T, P> ww_ag$zh_tw(String name) {
        return setData(WWProviderType.ZH_TW, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }

    @Override
    public ItemBuilder<T, P> ww_ag$zh_hk(String name) {
        return setData(WWProviderType.ZH_HK, (c, p) -> {
            p.add(c.getEntry(), name);
        });
    }
}
