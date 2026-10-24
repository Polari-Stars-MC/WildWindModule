package org.polaris2023.wildwind.core.inspection;

import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookPagesTest {
    @Test
    void retainsTranslationsAndLongUnicodeValuesForClientLayout() {
        String id = "wild_wind_wilderness:long_" + "测试".repeat(1000) + "😀";
        var rows = IntStream.range(0, 9)
                .mapToObj(i -> InspectionText.field("item_id", id + i)).toList();
        var pages = BookPages.paginate(rows);
        assertEquals(3, pages.size());
        for (int i = 0; i < pages.size(); i++) {
            var wrapper = assertInstanceOf(TranslatableContents.class, pages.get(i).getContents());
            assertEquals(InspectionText.PAGE_KEY, wrapper.getKey());
            var body = assertInstanceOf(Component.class, wrapper.getArgs()[0]);
            var storedRows = body.getSiblings().stream()
                    .filter(c -> c.getContents() instanceof TranslatableContents).toList();
            assertEquals(rows.subList(i * 4, Math.min(i * 4 + 4, rows.size())), storedRows);
        }
        assertEquals(List.of(), BookPages.paginate(List.of()));
    }
}
