package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 版本无关的资源 ID 工具。
 *
 * <p><b>返回类型本身也是版本分叉的</b>：26.x 是 {@code Identifier}，1.20/1.21 是
 * {@code ResourceLocation}。三个分支的返回类型不同，但预处理器只保留一个，
 * 因此不存在"同名方法返回不同类型"的冲突——每个产物里 {@code id(...)} 只有一个确定签名。</p>
 *
 * <p>调用方约定：</p>
 * <ul>
 *   <li>要立刻喂给 MC API → 用 {@link #id} / {@link #of}，用 {@code var} 接住
 *       （<code>var id = ImpIds.of("sample");</code>）；不要显式写类型，否则会把版本分叉
 *       泄漏到内容模块；</li>
 *   <li>要存进字段、写进配置、做字符串比较 → 用 {@link #idString} / {@link #ofString}，
 *       它们返回 {@code String}，跨版本完全稳定。</li>
 * </ul>
 *
 * <p>注意：版本分叉的类一律不写 import，成员内用全限定名——被裁掉的分支连带其类型
 * 引用一起消失（Java 不允许注解修饰 import）。</p>
 */
public final class ImpIds {

    private ImpIds() {}

    // ── 返回 MC 原生 ID 类型（各版本不同，调用方用 var 接）──────────────────

    /** 26.x：{@code ResourceLocation} 已改名为 {@code Identifier}。 */
    @Since("26.1")
    @Name("id")
    public static net.minecraft.resources.Identifier idModern(String namespace, String path) {
        return net.minecraft.resources.Identifier.parse(namespace + ":" + path);
    }

    /** 1.21：名字仍是 {@code ResourceLocation}，但构造器已私有，须用工厂方法。 */
    @Since("1.21")
    @Until("26.1")
    @Name("id")
    public static net.minecraft.resources.ResourceLocation idTen21(String namespace, String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    /** 1.20：{@code ResourceLocation} 的公有构造器仍可用。 */
    @Until("1.21")
    @Name("id")
    public static net.minecraft.resources.ResourceLocation idTen20(String namespace, String path) {
        return new net.minecraft.resources.ResourceLocation(namespace, path);
    }

    /** {@code ww:<path>} 的原生 ID 形式（同样按版本分叉）。 */
    @Since("26.1")
    @Name("of")
    public static net.minecraft.resources.Identifier ofModern(String path) {
        return id("ww", path);
    }

    /** 1.21 的 {@code ww:<path>}。 */
    @Since("1.21")
    @Until("26.1")
    @Name("of")
    public static net.minecraft.resources.ResourceLocation ofTen21(String path) {
        return id("ww", path);
    }

    /** 1.20 的 {@code ww:<path>}。 */
    @Until("1.21")
    @Name("of")
    public static net.minecraft.resources.ResourceLocation ofTen20(String path) {
        return id("ww", path);
    }

    // ── 返回 String（跨版本稳定，适合存字段/写配置）───────────────────────

    /**
     * 与 {@link #id} 同参，但返回字符串形式。
     *
     * <p>实现只有一份、不分叉：调用 {@code id(...)} 再取 {@code toString()}，
     * 类型分叉仍被关在 imp 内部。</p>
     */
    public static String idString(String namespace, String path) {
        return id(namespace, path).toString();
    }

    /** {@code ww:<path>} 的字符串形式。 */
    public static String ofString(String path) {
        return idString("ww", path);
    }

    /**
     * 返回<b>跨版本稳定</b>的 {@link ImpId} 句柄。
     *
     * <p>字段里存 ID 时用这个（字段不能用 {@code var}）；需要原生类型时调用
     * {@code ImpId#unwrap()}。</p>
     */
    public static ImpId idOf(String namespace, String path) {
        return ImpId.of(namespace, path);
    }

    /** 返回 {@code ww:<path>} 的稳定句柄。 */
    public static ImpId idOf(String path) {
        return ImpId.of(path);
    }

    /**
     * 解析 {@code namespace:path}；没有冒号时按 {@code minecraft} 处理。
     *
     * <p>用字符串中转可以规避 {@code Identifier} / {@code ResourceLocation} 的返回类型差异，
     * 因此这个方法是单实现、不需要版本分支。</p>
     */
    public static ImpId fromString(String id) {
        int colon = id.indexOf(':');
        return colon < 0
            ? ImpId.of("minecraft", id)
            : ImpId.of(id.substring(0, colon), id.substring(colon + 1));
    }

    /**
     * 从 NeoForge 的 {@code DeferredHolder} 取 {@link ImpId}。
     *
     * <p>{@code getId()} 的返回类型在 26.x 是 {@code Identifier}、旧版是 {@code ResourceLocation}，
     * 这里用 {@code toString()} 中转，所以同样不需要版本分支。</p>
     */
    public static ImpId ofHolder(net.neoforged.neoforge.registries.DeferredHolder<?, ?> holder) {
        return fromString(holder.getId().toString());
    }

    /** 从 {@code TagKey} 取 {@link ImpId}（同样用字符串中转）。 */
    public static ImpId ofTag(net.minecraft.tags.TagKey<?> tag) {
        return fromString(tag.location().toString());
    }

    /**
     * 资源 ID 字符集校验（版本无关，纯字符串逻辑）。
     *
     * @throws IllegalArgumentException 含非法字符时
     */
    public static String validate(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("资源 ID 不能为空");
        }
        if (!id.matches("[a-z0-9_.\\-/:]+")) {
            throw new IllegalArgumentException("资源 ID 含非法字符：" + id);
        }
        return id;
    }
}
