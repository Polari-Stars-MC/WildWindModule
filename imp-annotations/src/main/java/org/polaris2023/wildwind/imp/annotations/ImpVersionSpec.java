package org.polaris2023.wildwind.imp.annotations;

/**
 * 版本比较语义（ADR-0003 §2）。
 *
 * <p>Minecraft 26.x 起版本号形如 {@code 26.1} / {@code 26.2}（未来可能 {@code 26.2.1}），
 * 按 {@code .} 分段做数值比较，段数不足视为 0：</p>
 *
 * <pre>
 *   26.1  &lt;  26.2  &lt;  26.2.1  &lt;  27.0
 * </pre>
 *
 * <p>无法解析为全数字段时抛出 {@link IllegalArgumentException}——
 * 处理器宁可编译失败也不猜测版本关系。</p>
 */
public final class ImpVersionSpec {

    private ImpVersionSpec() {}

    /** 解析版本字符串为各段数值。 */
    public static int[] parse(String version) {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("空版本号");
        }
        String[] parts = version.trim().split("\\.");
        int[] segs = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            try {
                segs[i] = Integer.parseInt(p);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法解析版本段 '" + p + "'（来自 '" + version + "'），仅支持全数字分段版本号");
            }
        }
        return segs;
    }

    /** 返回 {@code a >= b}（按语义分段比较）。 */
    public static boolean atLeast(String target, String baseline) {
        return compare(parse(target), parse(baseline)) >= 0;
    }

    /** 返回 {@code a < b}。 */
    public static boolean before(String target, String baseline) {
        return compare(parse(target), parse(baseline)) < 0;
    }

    /** target 是否等于列表中任一版本（用于 exclude 排除列表，精确匹配）。 */
    public static boolean matchesAny(String target, String[] versions) {
        for (String v : versions) {
            if (compare(parse(target), parse(v)) == 0) {
                return true;
            }
        }
        return false;
    }

    static int compare(int[] a, int[] b) {
        int len = Math.max(a.length, b.length);
        for (int i = 0; i < len; i++) {
            int x = i < a.length ? a[i] : 0;
            int y = i < b.length ? b[i] : 0;
            if (x != y) return Integer.compare(x, y);
        }
        return 0;
    }
}
