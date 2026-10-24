package org.polaris2023.wildwind.core.inspection;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookPagesTest {
    @Test
    void wrapsLongMixedLanguageIdsWithoutLosingCharactersOrOverflowingPages() {
        String row = "物品ID:wild_wind_wilderness:very_long_item_" + "测试".repeat(1000) + "😀";
        var pages = BookPages.paginate(List.of(row));
        assertTrue(pages.size() > 1);
        assertEquals(row, String.join("", pages).replace("\n", ""));
        for (String page : pages) {
            assertTrue(page.length() <= 1024);
            assertTrue(page.split("\n", -1).length <= 12);
            for (String line : page.split("\n", -1)) {
                assertTrue(line.codePoints().map(cp -> cp >= 32 && cp < 127 ? 6 : 10).sum() <= 108);
            }
        }
    }
}
