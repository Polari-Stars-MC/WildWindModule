package org.polaris2023.ww_ag.datagen;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author baka4n
 * {@code @Date 2025/05/21 21:30:30}
 */
@MethodsReturnNonnullByDefault
public class WWLanguage extends LanguageProvider implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;
    private final String locale;

    public static final Map<String, ProviderType<WWLanguage>> REGISTRATE = new HashMap<>();

    private WWLanguage(AbstractRegistrate<?> parent, PackOutput output, String locale) {
        super(output, parent.getModid(), locale);
        this.parent = parent;
        this.locale = locale;
    }

    public static ProviderType<WWLanguage> create(String locale) {
        ProviderType<WWLanguage> providerType = ProviderType.registerProvider("ww_" + locale, c ->
                new WWLanguage(c.parent(), c.output(), locale));
        REGISTRATE.put(locale, providerType);
        return providerType;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    protected void addTranslations() {
        parent.genData(REGISTRATE.get(locale), this);
    }
}
