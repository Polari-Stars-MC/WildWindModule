package org.polaris2023.ww_ag.common.registrate;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.L2Core;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.registrate.build.*;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.datagen.WWLanguage;
import org.polaris2023.ww_ag.utils.ILanguage;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
* @author baka4n
* {@code @Date 2025/05/19 22:44:01}
*/
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class WWRegistrate extends L2Registrate {
    private ResourceKey<CreativeModeTab> after = CreativeModeTabs.SPAWN_EGGS;
    public WWRegistrate(String modid) {
        super(modid);
    }

    public SoundBuilder<SoundEvent, L2Registrate> fixRangeSound(String name, float range) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createFixedRangeEvent(loc(name), range)));
    }
    public SoundBuilder<SoundEvent, L2Registrate> variableRangeSound(String name) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createVariableRangeEvent(loc(name))));
    }


    public synchronized SimpleEntry<CreativeModeTab> buildWWCreativeTab(String name,
                                                                        String def,
                                                                        Consumer<CreativeModeTab.Builder> config,
                                                                        int index) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(getModid(), name);
        this.defaultCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, id));
        TabSorter sorter = new TabSorter(index, id);
        return WWAgMod.REGISTRATE.buildCreativeTabImpl(name, this.addLang("itemGroup", id, def), (b) -> {
            config.accept(b);
            sorter.sort(b, index);
        }, l -> {

        });

    }
    public synchronized SimpleEntry<CreativeModeTab> buildWWCreativeTab(String name,
                                                                        String def,
                                                                        Consumer<CreativeModeTab.Builder> config,
                                                                        int index,
                                                                        NonNullConsumer<ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>>> lang) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(getModid(), name);
        this.defaultCreativeTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, id));
        TabSorter sorter = new TabSorter(index, id);
        return WWAgMod.REGISTRATE.buildCreativeTabImpl(name, this.addLang("itemGroup", id, def), (b) -> {
            config.accept(b);
            sorter.sort(b, index);
        }, lang);

    }


    private synchronized SimpleEntry<CreativeModeTab> buildCreativeTabImpl(String name,
                                                                           Component comp,
                                                                           Consumer<CreativeModeTab.Builder> config,
                                                                           NonNullConsumer<ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>>> lang) {
        NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate> generic = this.generic(this.self(), name, Registries.CREATIVE_MODE_TAB, () -> {
            CreativeModeTab.Builder builder = CreativeModeTab.builder().title(comp).withTabsBefore(CreativeModeTabs.SPAWN_EGGS);
            config.accept(builder);
            return builder.build();
        });
        ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>> iLanguage = new ILanguage<>() {

            @Override
            public NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate> ww_ag$self() {
                return generic;
            }

            @Override
            public ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>> ww_ag$zh_cn(String name) {
                generic.setData(WWProviderType.ZH_CN, (c, p) -> {
                    p.add(Util.makeDescriptionId("itemGroup", c.getId()), name);
                });
                return this;
            }
            @Override
            public ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>> ww_ag$zh_tw(String name) {
                generic.setData(WWProviderType.ZH_TW, (c, p) -> {
                    p.add(Util.makeDescriptionId("itemGroup", c.getId()), name);
                });
                return this;
            }
            @Override
            public ILanguage<CreativeModeTab, CreativeModeTab, L2Registrate, NoConfigBuilder<CreativeModeTab, CreativeModeTab, L2Registrate>> ww_ag$zh_hk(String name) {
                generic.setData(WWProviderType.ZH_HK, (c, p) -> {
                    p.add(Util.makeDescriptionId("itemGroup", c.getId()), name);
                });
                return this;
            }


        };

        lang.accept(iLanguage);

        return new SimpleEntry<>(generic.register());
    }

    private static class TabSorter {
        private static final TreeMap<String, TreeMap<Integer, TabSorter>> MAP = new TreeMap<>();
        private final ResourceLocation id;
        public TabSorter(int index, ResourceLocation id) {
            TreeMap<Integer, TabSorter> map = MAP.getOrDefault(id.getNamespace(), new TreeMap<>());
            map.put(index, this);
            MAP.put(id.getNamespace(), map);
            this.id = id;
        }


        public void sort(CreativeModeTab.Builder b, int index) {
            List<ResourceLocation> before = new ArrayList<>(), after = new ArrayList<>();
            var map = MAP.get(id.getNamespace());
            for (var entry : map.entrySet()) {
                Integer key = entry.getKey();
                if (key > index) {
                    after.add(entry.getValue().id);
                } else if (key < index) {
                    before.add(entry.getValue().id);
                }
            }
            b.withTabsAfter(after.toArray(new ResourceLocation[0]));
            b.withTabsBefore(before.toArray(new ResourceLocation[0]));
        }
    }

    public PlanksEntry<WWRegistrate> planks(String name) {
        return new PlanksEntry<>(this, name);
    }

    public <T extends SoundEvent> SoundBuilder<T, L2Registrate> customSound(String name, Function<ResourceLocation, T> function) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> function.apply(loc(name))));
    }

    public <T extends IGlobalLootModifier> LootModifierBuilder<T, L2Registrate> glm(String name, MapCodec<T> codec) {
        return entry(name, callback -> LootModifierBuilder.create(this, self(), name, callback, codec));
    }

}
