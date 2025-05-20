package org.polaris2023.ww_ag.common.init.tags;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.ww_ag.WWAgMod;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author baka4n
 * {@code @Date 2025/05/20 17:36:55}
 */
public enum WWBlockTags implements Supplier<TagKey<Block>> {
    ORES$SALT;
    private final TagKey<Block> tag;

    WWBlockTags() {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = BlockTags.create(WWAgMod.cLoc(id));

    }

    WWBlockTags(Object mod) {
        String id = name().toLowerCase(Locale.ROOT).replace("$", "/");
        tag = BlockTags.create(WWAgMod.REGISTRATE.loc(id));
    }

    @Override
    public TagKey<Block> get() {
        return tag;
    }
}
