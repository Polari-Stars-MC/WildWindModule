package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import org.polaris2023.ww_ag.WWAgMod;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
public class ModItems {
    public static final ItemEntry<Item> SALT = REGISTRATE
            .item("salt", Item::new)
            .model((c, p) -> p.basicItem(c.get()))
            .register();
    public static void register() {}
}
