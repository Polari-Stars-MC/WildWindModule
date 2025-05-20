package org.polaris2023.ww_ag.common.init.tags;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 17:36:46}
 */
public enum WWItemTags implements Supplier<TagKey<Item>> {
    ORES$SALT;
    private final TagKey<Item> tag;

    WWItemTags() {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = ItemTags.create(WWAgMod.cLoc(id));

    }

    WWItemTags(Object mod) {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = ItemTags.create(WWAgMod.REGISTRATE.loc(id));
    }

    @Override
    public TagKey<Item> get() {
        return tag;
    }
}
