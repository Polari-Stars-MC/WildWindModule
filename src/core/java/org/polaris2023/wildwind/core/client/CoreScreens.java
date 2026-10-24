package org.polaris2023.wildwind.core.client;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.polaris2023.wildwind.core.CoreMod;
import org.polaris2023.wildwind.core.menu.ModMenus;
@EventBusSubscriber(modid = CoreMod.MOD_ID, value = Dist.CLIENT)
public final class CoreScreens {
    @SubscribeEvent public static void screens(RegisterMenuScreensEvent event) { event.register(ModMenus.KILN.get(), KilnScreen::new); }
}
