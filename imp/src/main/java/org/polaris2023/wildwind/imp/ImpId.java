package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.Objects;

/**
 * 跨版本稳定的资源 ID 句柄。
 *
 * <p>解决的问题：MC 的资源 ID 类型在 26.x 是 {@code Identifier}、在 1.20/1.21 是
 * {@code ResourceLocation}。内容模块若把它存进字段（字段不能用 {@code var}），
 * 类型名就必然分叉。本类提供<b>稳定的存储类型</b>，原生类型只在 {@code unwrap()}
 * 处按版本分叉。</p>
 *
 * <pre>{@code
 * private static final ImpId ICON = ImpIds.idOf("minecraft", "textures/item/spectral_arrow.png");
 *
 * void render() {
 *     var native = ICON.unwrap();   // 26.x 得到 Identifier，1.21 得到 ResourceLocation
 *     graphics.blit(pipeline, native, ...);
 * }
 * }</pre>
 *
 * <p>约定：内容模块字段一律用本类型（或用 {@link #asString()} 存字符串），
 * 禁止显式书写 {@code Identifier} / {@code ResourceLocation}。</p>
 */
public final class ImpId {

    private final String namespace;
    private final String path;

    private ImpId(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /** 构造 {@code namespace:path}。 */
    public static ImpId of(String namespace, String path) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(path, "path");
        return new ImpId(namespace, path);
    }

    /** 构造 {@code ww:path}。 */
    public static ImpId of(String path) {
        return of("ww", path);
    }

    public String namespace() {
        return namespace;
    }

    public String path() {
        return path;
    }

    /** 跨版本稳定的字符串形式（{@code namespace:path}）。 */
    public String asString() {
        return namespace + ":" + path;
    }

    // ── 原生类型：按版本分叉，调用方用 var 接 ─────────────────────────────

    /** 26.x：{@code ResourceLocation} 已改名为 {@code Identifier}。 */
    @Since("26.1")
    @Name("unwrap")
    public net.minecraft.resources.Identifier unwrapModern() {
        return net.minecraft.resources.Identifier.parse(asString());
    }

    /** 1.21：名字仍是 {@code ResourceLocation}，构造器已私有，须用工厂方法。 */
    @Since("1.21")
    @Until("26.1")
    @Name("unwrap")
    public net.minecraft.resources.ResourceLocation unwrapTen21() {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    /** 1.20：{@code ResourceLocation} 的公有构造器仍可用。 */
    @Until("1.21")
    @Name("unwrap")
    public net.minecraft.resources.ResourceLocation unwrapTen20() {
        return new net.minecraft.resources.ResourceLocation(namespace, path);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ImpId id
            && namespace.equals(id.namespace)
            && path.equals(id.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return asString();
    }
}
