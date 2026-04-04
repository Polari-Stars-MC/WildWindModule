package org.polaris2023.wildwind.hfas;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

@Mod(HFASMod.MOD_ID)
@Slf4j
public class HFASMod {
    public static final String MOD_ID = "wild_wind_hfas";

    public HFASMod(IEventBus bus) {
        ModBlocks.register(bus);
    }
}
