package org.polaris2023.wildwind.core.inspection;

import java.util.List;
import net.minecraft.network.chat.Component;

/** Keep report values structured until the reader's client resolves its language. */
public final class InspectionText {
    public static final String PAGE_KEY = "inspection.wild_wind_core.page";
    public static final Component UNKNOWN = Component.literal("×");
    private InspectionText() {}
    public static Component text(String key, Object... arguments) {
        return Component.translatable("inspection.wild_wind_core." + key, arguments);
    }
    public static Component field(String key, Object value) {
        return text("field." + key, value instanceof Component component ? component : Component.literal(value.toString()));
    }
    public static Component join(List<? extends Component> values) {
        var result = Component.empty();
        for (int i = 0; i < values.size(); i++) {
            if (i != 0) result.append(text("separator"));
            result.append(values.get(i));
        }
        return result;
    }
}
