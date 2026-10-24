package org.polaris2023.wildwind.core.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.polaris2023.wildwind.core.inspection.InspectionText;

/** View-only pagination: saved components stay translatable and pre-existing notes stay untouched. */
public final class InspectionBookPages {
    private static final int TEXT_WIDTH = 108;
    private static final int LINES_PER_PAGE = 12;
    private InspectionBookPages() {}

    public static List<Component> layout(List<Component> stored, Font font) {
        var result = new ArrayList<Component>();
        for (var page : stored) {
            if (!(page.getContents() instanceof TranslatableContents translation)
                    || !translation.getKey().equals(InspectionText.PAGE_KEY)) {
                result.add(page);
                continue;
            }
            var lines = font.getSplitter().splitLines(page, TEXT_WIDTH, Style.EMPTY);
            for (int start = 0; start < lines.size(); start += LINES_PER_PAGE) {
                var visible = Component.empty();
                for (int i = start; i < Math.min(start + LINES_PER_PAGE, lines.size()); i++) {
                    if (i != start) visible.append("\n");
                    lines.get(i).visit((style, text) -> {
                        visible.append(Component.literal(text).withStyle(style));
                        return Optional.empty();
                    }, Style.EMPTY);
                }
                result.add(visible);
            }
        }
        return List.copyOf(result);
    }
}
