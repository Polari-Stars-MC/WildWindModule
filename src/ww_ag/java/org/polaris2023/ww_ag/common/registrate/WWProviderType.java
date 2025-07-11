package org.polaris2023.ww_ag.common.registrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.polaris2023.ww_ag.datagen.*;
import org.polaris2023.ww_ag.datagen.loot.WWBaseLootSubProvider;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 13:46:54}
 */
public interface WWProviderType<T extends RegistrateProvider> extends ProviderType<T> {
    ProviderType<RegistrateTagsProvider.Impl<SoundEvent>> SOUNDS = ProviderType.registerDynamicTag("tags/sound", "sound_events", Registries.SOUND_EVENT);
    ProviderType<WWSoundDefineProvider> SOUND_DEFINE = ProviderType.registerProvider("sound_define", c -> new WWSoundDefineProvider(c.parent(), c.output(), c.fileHelper()));
    ProviderType<WWLanguage> ZH_CN = WWLanguage.create("zh_cn");
    ProviderType<WWLanguage> ZH_TW = WWLanguage.create("zh_tw");
    ProviderType<WWLanguage> ZH_HK = WWLanguage.create("zh_hk");
    ProviderType<WWGlobalLootModifier> GLM = ProviderType.registerProvider("global_loot_modifier", c -> new WWGlobalLootModifier(c.parent(), c.output(), c.provider()));
    ProviderType<WWDataRepeater> REPEATER = ProviderType.registerProvider("ww_data_repeater", c -> new WWDataRepeater(c.parent(), c.output(), c.provider()));
    @SuppressWarnings("deprecation")
    ProviderType<WWConfigGen> CFG = ProviderType.registerProvider("cfg", c -> new WWConfigGen(c.parent(), c.event().getGenerator(), c.provider(), c.parent().getModid()));

    RegistrateLootTableProvider.LootType<WWBaseLootSubProvider> BASE =
            RegistrateLootTableProvider.LootType.register("ww_base", LootContextParamSets.ENTITY, WWBaseLootSubProvider::new);
}
