package org.polaris2023.ww_ag.utils;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;


/**
 * @author baka4n
 * @code @Date 2025/6/14 00:41:00
 */
@SuppressWarnings("unchecked")
public class RegUtil {
    public static WWRegistrate defTab(WWRegistrate registrate, ResourceKey<CreativeModeTab> key) {
        return (WWRegistrate) registrate.defaultCreativeTab(key);
    }
    public static <P extends Item> ILanguage<Item, P, L2Registrate, ItemBuilder<P, L2Registrate>> itemReg(
            L2Registrate registrate,
            String name,
            NonNullFunction<Item.Properties, P> factory
    ) {
        return (ILanguage<Item, P, L2Registrate, ItemBuilder<P, L2Registrate>>) registrate.item(name, factory);
    }

    public static <P extends Block> ILanguage<Block, P, L2Registrate, BlockBuilder<P, L2Registrate>> blockReg(
            L2Registrate registrate,
            String name,
            NonNullFunction<BlockBehaviour.Properties, P> facotey
    ) {
        return (ILanguage<Block, P, L2Registrate, BlockBuilder<P, L2Registrate>>) registrate.block(name, facotey);
    }
}

