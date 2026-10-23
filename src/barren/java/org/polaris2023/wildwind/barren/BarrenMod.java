package org.polaris2023.wildwind.barren;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.polaris2023.wildwind.barren.block.ModBlocks;

@Mod(BarrenMod.MOD_ID)
public final class BarrenMod {
    public static final String MOD_ID = "wild_wind_barren";
    public BarrenMod(IEventBus bus) {
        ModBlocks.register(bus);
    }
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
}
