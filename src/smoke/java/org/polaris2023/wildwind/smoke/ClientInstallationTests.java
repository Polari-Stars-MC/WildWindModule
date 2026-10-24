package org.polaris2023.wildwind.smoke;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.polaris2023.wildwind.core.inspection.BookPages;
import org.polaris2023.wildwind.core.inspection.InspectionText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Opt-in client resource-load check; never packaged in a release mod. */
@EventBusSubscriber(modid = "wild_wind_smoke", value = Dist.CLIENT)
public final class ClientInstallationTests {
    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (!Boolean.getBoolean("wildwind.clientSmoke")) return;
        var client = Minecraft.getInstance();
        if (client.gui.overlay() != null || !(client.gui.screen() instanceof TitleScreen)) return;
        require(client, "wild_wind_core:textures/entity/signs/azalea.png");
        verifyInspectionBooks(client);
        if (ModList.get().isLoaded("wild_wind_wilderness")) {
            require(client, "wild_wind_wilderness:textures/entity/pig/pig_muddy.png");
            require(client, "wild_wind_wilderness:textures/entity/pig/pig_muddy_baby.png");
            require(client, "wild_wind_wilderness:textures/entity/piranha.png");
            require(client, "wild_wind_wilderness:geckolib/models/entity/mudcrab.geo.json");
        }
        LogUtils.getLogger().info("Wild Wind client installation check passed");
        client.stop();
    }

    private static void verifyInspectionBooks(Minecraft client) {
        var originalLanguage = Language.getInstance();
        var longId = "wild_wind_wilderness:long_" + "测试".repeat(250) + "😀";
        var originalNote = Component.literal("Unchanged player note");
        var rows = List.of(
                InspectionText.field("hardness", "2"),
                InspectionText.field("item_id", longId),
                InspectionText.field("fuel_time", InspectionText.text("time", "15", 300)),
                InspectionText.field("piston", InspectionText.text("piston.block")));
        var stored = new ArrayList<Filterable<Component>>();
        stored.add(net.minecraft.server.network.Filterable.passThrough(originalNote));
        BookPages.paginate(rows)
                .forEach(page -> stored.add(net.minecraft.server.network.Filterable.passThrough(page)));
        // Registry component prototypes are loaded on joining a world in 26.2.
        // This isolated holder lets the title-screen test exercise the real book accessor.
        var book = new ItemStack(Holder.direct(Items.WRITTEN_BOOK));
        book.set(DataComponents.WRITTEN_BOOK_CONTENT,
                new WrittenBookContent(Filterable.passThrough("Inspection"), "tester", 0, stored, true));
        try {
            for (String locale : List.of("en_us", "zh_cn")) {
                Language.inject(ClientLanguage.loadFrom(client.getResourceManager(), List.of("en_us", locale), false));
                var access = BookViewScreen.BookAccess.fromItem(book);
                if (access == null) throw new IllegalStateException("Written book accessor returned no content");
                if (!access.pages().getFirst().equals(originalNote)) throw new IllegalStateException("Existing notes changed");
                if (access.getPageCount() <= stored.size()) throw new IllegalStateException("Long translated report was not repaginated");
                for (var page : access.pages().subList(1, access.getPageCount())) {
                    if (client.font.wordWrapHeight(page, 114) > 126) throw new IllegalStateException("Inspector page exceeds book height: " + locale);
                    for (String line : page.getString().split("\\n")) {
                        if (client.font.width(line) > 114) {
                            throw new IllegalStateException("Inspector line exceeds book width: " + locale + ": " + line);
                        }
                    }
                }
                String rendered = access.pages().stream().skip(1).map(Component::getString)
                        .collect(Collectors.joining()).replace("\n", "");
                if (!rendered.contains(longId) || !rendered.contains(locale.equals("zh_cn") ? "硬度:2" : "Hardness: 2")) {
                    throw new IllegalStateException("Translated report lost text: " + locale);
                }
                if (rendered.contains("inspection.wild_wind_core.")) throw new IllegalStateException("Missing inspection translation: " + locale);
                if (!book.get(DataComponents.WRITTEN_BOOK_CONTENT).pages().equals(stored)) {
                    throw new IllegalStateException("Viewing a book modified its stored components");
                }
            }
        } finally {
            Language.inject(originalLanguage);
        }
    }

    private static void require(Minecraft client, String id) {
        if (client.getResourceManager().getResource(Identifier.parse(id)).isEmpty()) {
            throw new IllegalStateException("Missing module resource: " + id);
        }
    }
}
