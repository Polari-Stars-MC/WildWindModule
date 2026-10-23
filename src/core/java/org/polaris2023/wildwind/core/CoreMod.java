package org.polaris2023.wildwind.core;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.registry.*;

@Mod(CoreMod.MOD_ID)
public final class CoreMod {
    public static final String MOD_ID = "wild_wind_core";
    public CoreMod(IEventBus bus) {
        ModBlocks.register(bus);
        ModItems.register(bus);
        org.polaris2023.wildwind.core.recipe.ModRecipes.register(bus);
        org.polaris2023.wildwind.core.block.entity.ModBlockEntities.register(bus);
        org.polaris2023.wildwind.core.menu.ModMenus.register(bus);
        org.polaris2023.wildwind.core.worldgen.MasonKilnProcessor.PROCESSORS.register(bus);
        org.polaris2023.wildwind.core.registry.ModCreativeTabs.register(bus);
        org.polaris2023.wildwind.structures.StructureModule.register(bus);

    }
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
}
