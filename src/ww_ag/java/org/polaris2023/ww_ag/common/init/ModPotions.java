package org.polaris2023.ww_ag.common.init;

import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.polaris2023.ww_ag.WWAgMod;

/**
 * @author baka4n
 * @code @Date 2025/5/28 21:09:38
 */
public class ModPotions {
    public static final SimpleEntry<Potion> MILK = WWAgMod.REGISTRATE.potion("milk", Potion::new);
    public static void register() {}
}
