package org.polaris2023.wildwind.core.inspection;

import java.util.ArrayList;
import java.util.List;

/** Conservative vanilla book layout: 108 pixels wide, 12 nine-pixel lines. */
public final class BookPages {
    private BookPages() {}

    public static List<String> paginate(List<String> rows) {
        var lines = new ArrayList<String>();
        for (String row : rows) {
            for (String paragraph : row.split("\n", -1)) {
                var line = new StringBuilder();
                int width = 0;
                for (int cp : paragraph.codePoints().toArray()) {
                    // Default Latin glyphs fit within six pixels; reserve ten for other glyphs.
                    int glyph = cp >= 32 && cp < 127 ? 6 : 10;
                    if (width + glyph > 108) {
                        lines.add(line.toString());
                        line.setLength(0);
                        width = 0;
                    }
                    if (!Character.isISOControl(cp)) line.appendCodePoint(cp);
                    width += glyph;
                }
                lines.add(line.toString());
            }
        }
        var pages = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i += 12) {
            pages.add(String.join("\n", lines.subList(i, Math.min(i + 12, lines.size()))));
        }
        return List.copyOf(pages);
    }
}
