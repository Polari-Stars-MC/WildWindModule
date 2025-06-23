package org.polaris2023.ww_ag.common.init.tags;

import lombok.experimental.ExtensionMethod;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 17:36:46}
 */
@ExtensionMethod({WWAgMod.class, ItemTags.class})
public enum WWItemTags implements Supplier<TagKey<Item>> {
    ORES$SALT,
    FUNGUS,
    AZALEA_LOGS,
    LOG_CROWM
    ;
    private final TagKey<Item> tag;

    WWItemTags() {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = id.cLoc().create();

    }

    WWItemTags(Object mod) {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = id.loc().create();
    }

    @Override
    public TagKey<Item> get() {
        return tag;
    }
}
