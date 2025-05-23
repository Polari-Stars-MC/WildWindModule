package org.polaris2023.ww_ag.common.registrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import org.polaris2023.ww_ag.datagen.WWGlobalLootModifier;
import org.polaris2023.ww_ag.datagen.WWLanguage;
import org.polaris2023.ww_ag.datagen.WWSoundDefineProvider;

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
}
