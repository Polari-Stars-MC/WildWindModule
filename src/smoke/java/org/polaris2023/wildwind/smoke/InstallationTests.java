package org.polaris2023.wildwind.smoke;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.registry.ModCreativeTabs;

/** Boots each supported installation using its actual release JAR dependencies. */
@Mod("wild_wind_smoke")
public final class InstallationTests {
    public InstallationTests(IEventBus bus) {
        var functions = DeferredRegister.<Consumer<GameTestHelper>>create(Registries.TEST_FUNCTION, "wild_wind_smoke");
        functions.register("installation", () -> InstallationTests::installation);
        functions.register(bus);
    }

    private static void check(GameTestHelper h, boolean condition, String message) {
        h.assertTrue(condition, Component.literal(message));
    }

    private static void installation(GameTestHelper h) {
        Set<String> expected = Arrays.stream(System.getProperty("wildwind.testModules").split(",")).collect(Collectors.toSet());
        for (String module : new String[]{"core", "wilderness", "barren"}) {
            check(h, ModList.get().isLoaded("wild_wind_" + module) == expected.contains(module), "Loaded module: " + module);
        }
        check(h, ModList.get().isLoaded("geckolib") == expected.contains("wilderness"), "Only Wilderness needs GeckoLib");
        check(h, !ModList.get().isLoaded("ww_hfas") && !ModList.get().isLoaded("ww_structures"), "Legacy entrypoints must not load");
        var tab = ModCreativeTabs.WILD_WIND.get();
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(h.getLevel().enabledFeatures(), true, h.getLevel().registryAccess()));
        for (String path : new String[]{"azalea_planks", "azalea_sign", "azalea_hanging_sign", "azalea_shelf", "kiln", "duckweed"}) {
            check(h, tab.contains(BuiltInRegistries.ITEM.getValue(Identifier.parse("wild_wind_core:" + path)).getDefaultInstance()), "Core item in tab: " + path);
        }
        for (String path : new String[]{"blaze_planks", "soul_planks", "scorched_dirt", "muddy_pig_spawn_egg"}) {
            var id = Identifier.parse("wild_wind_wilderness:" + path);
            check(h, BuiltInRegistries.ITEM.containsKey(id) == expected.contains("wilderness"), "Wilderness ownership: " + path);
            if (expected.contains("wilderness")) check(h, tab.contains(BuiltInRegistries.ITEM.getValue(id).getDefaultInstance()), "Wilderness tab entry: " + path);
        }
        check(h, BuiltInRegistries.BLOCK.containsKey(Identifier.parse("wild_wind_barren:tiny_cactus")) == expected.contains("barren"), "Cactus belongs only to Barren");
        check(h, BuiltInRegistries.BLOCK.keySet().stream().noneMatch(id -> id.getNamespace().startsWith("wild_wind_") && id.getPath().equals("fletching_table")), "Custom fletching table removed");
        var inspectionPos = h.absolutePos(new net.minecraft.core.BlockPos(2, 2, 2));
        h.getLevel().setBlockAndUpdate(inspectionPos, net.minecraft.world.level.block.Blocks.OAK_PLANKS.defaultBlockState());
        var report = org.polaris2023.wildwind.core.inspection.InspectionReport.block(h.getLevel(), inspectionPos);
        check(h, report.contains("可再生:是") && report.contains("硫方怪原型:弹性（橡胶球）"), "Inspector works with only Core and its own tags");
        var book = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WRITABLE_BOOK);
        check(h, org.polaris2023.wildwind.core.inspection.BookInspectionHandler.append(book,
                org.polaris2023.wildwind.core.inspection.BookPages.paginate(report)), "Core can persist complete inspection report");
        h.succeed();
    }
}
