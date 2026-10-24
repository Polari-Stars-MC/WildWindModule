package org.polaris2023.wildwind.hfas;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
/** Development-only datagen entry; never distributed to players. */
@Mod(HFASMod.MOD_ID)
public final class HFASMod {
    public static final String MOD_ID = "wild_wind_datagen";
    public HFASMod(IEventBus bus) {}
    public static Identifier id(String path) {
        String module = path.contains("tiny_cactus") ? "barren" :
            java.util.stream.Stream.of("blaze", "soul", "scorched", "muddy_pig", "mudcrab", "piranha", "reach", "sink", "choke", "water_breathing").anyMatch(path::contains) ? "wilderness" : "core";
        return Identifier.fromNamespaceAndPath("wild_wind_" + module, path);
    }
}
