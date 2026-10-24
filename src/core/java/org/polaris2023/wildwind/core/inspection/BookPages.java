package org.polaris2023.wildwind.core.inspection;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;

/** Stored pages retain translations; the client lays them out using its actual language and font. */
public final class BookPages {
    private static final int ROWS_PER_STORED_PAGE = 4;
    private BookPages() {}

    public static List<Component> paginate(List<Component> rows) {
        var pages = new ArrayList<Component>();
        for (int start = 0; start < rows.size(); start += ROWS_PER_STORED_PAGE) {
            var content = Component.empty();
            for (int i = start; i < Math.min(start + ROWS_PER_STORED_PAGE, rows.size()); i++) {
                if (i != start) content.append("\n");
                content.append(rows.get(i));
            }
            pages.add(Component.translatable(InspectionText.PAGE_KEY, content));
        }
        return List.copyOf(pages);
    }
}
