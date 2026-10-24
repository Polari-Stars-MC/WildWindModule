package org.polaris2023.wildwind.wilderness;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.polaris2023.wildwind.wilderness.block.ModBlocks;
import org.polaris2023.wildwind.wilderness.registry.*;

@Mod(WildernessMod.MOD_ID)
public final class WildernessMod {
    public static final String MOD_ID = "wild_wind_wilderness";
    public WildernessMod(IEventBus bus) {
        ModBlocks.register(bus);
        ModEntities.register(bus);
        ModItems.register(bus);
        org.polaris2023.wildwind.wilderness.effect.ModEffects.register(bus);
        ModDataComponents.register(bus);
        ModAttributes.register(bus);
        ModMobEffects.register(bus);
        ModPotions.register(bus);
        ModSoundEvents.register(bus);
        ModEntityDataSerializers.register(bus);
        bus.addListener((net.neoforged.neoforge.registries.DataPackRegistryEvent.NewRegistry event) ->
            event.dataPackRegistry(DatadrivenRegistryKey.MUDCRAB_VARIANT,
                org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant.DIRECT_CODEC,
                org.polaris2023.wildwind.wilderness.entity.animal.MudcrabVariant.NETWORK_CODEC));

    }
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
}
