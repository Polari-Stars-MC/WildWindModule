package org.polaris2023.ww_ag.common.registrate.build;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author baka4n
 * {@code @Date 2025/05/22 08:40:18}
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemBuilder<T extends Item, P> extends com.tterrag.registrate.builders.ItemBuilder<T, P> {

    public static <T extends Item, P> ItemBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Item.Properties, T> factory) {
        ItemBuilder<T, P> tpItemBuilder = new ItemBuilder<>(owner, parent, name, callback, factory);
        tpItemBuilder.defaultLang().model((c, p) -> {
            if (c.getEntry() instanceof BlockItem || c.getEntry() instanceof BucketItem) {
                p.generated(c::getEntry);
            } else {
                p.generated(c::getEntry, WWAgMod.REGISTRATE.loc("item/item_placeholder"));
            }
        });
        return tpItemBuilder
                .setData(WWProviderType.ZH_CN, (c, p) ->{
                    if (!(c.getEntry() instanceof BlockItem)) {
                        p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                    }
                })
                .setData(WWProviderType.ZH_TW, (c, p) ->{
                    if (!(c.getEntry() instanceof BlockItem)) {
                        p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                    }
                })
                .setData(WWProviderType.ZH_HK, (c, p) ->{
                    if (!(c.getEntry() instanceof BlockItem)) {
                        p.add(c.getEntry().getDescriptionId(), c.getEntry().getDescriptionId());
                    }
                })

                ;
    }

    protected ItemBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Item.Properties, T> factory) {
        super(owner, parent, name, callback, factory);


    }

    @Override
    public <D extends RegistrateProvider> ItemBuilder<T, P> setData(ProviderType<? extends D> type, NonNullBiConsumer<DataGenContext<Item, T>, D> cons) {
        return (ItemBuilder<T, P>) super.setData(type, cons);
    }

    public ItemBuilder<T, P> zh_cn(String name) {
        return setData(WWProviderType.ZH_CN, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }
    public ItemBuilder<T, P> zh_tw(String name) {
        return setData(WWProviderType.ZH_TW, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }
    public ItemBuilder<T, P> zh_hk(String name) {
        return setData(WWProviderType.ZH_HK, (c, p) -> p.add(c.getEntry().getDescriptionId(), name));
    }
}
