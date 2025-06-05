package org.polaris2023.ww_ag.common.init;

import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.config.UseItemConfig;

/**
 * @author baka4n
 * @code @Date 2025/6/4 21:36:07
 */
public class ModConfigs {
    public static final
        UseItemConfig USE_ITEM = WWAgMod.REGISTRATE.registerSynced(UseItemConfig::new);

    public static void register() {}
}
