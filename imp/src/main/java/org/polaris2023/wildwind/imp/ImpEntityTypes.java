package org.polaris2023.wildwind.imp;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 实体类型注册的版本无关口（ADR-0003）。
 *
 * <p>两族差异：</p>
 * <ul>
 *   <li>26.x 有便捷子类 {@code DeferredRegister.Entities}（{@code createEntities} +
 *       {@code registerEntityType}），且 {@code EntityType.Builder.build} 接受
 *       {@code ResourceKey}；</li>
 *   <li>1.20 / 1.21 只有通用 {@code DeferredRegister.create(Registries.ENTITY_TYPE, id)}，
 *       构建器用 {@code build(String)}，并且<b>没有</b> {@code notInPeaceful()}。</li>
 * </ul>
 *
 * <p>{@code notInPeaceful()} 的语义差异是要点：26.x 把「和平难度下是否消失」做成了
 * {@code EntityType} 的属性；1.20 / 1.21 则把它做在 {@code Mob.shouldDespawnInPeaceful()}
 * 里（{@code Monster} 覆写为 {@code true}）。因此旧版无法在注册处表达这一位，
 * 只能在实体类里覆写该方法——见 {@link ImpEntityTypeSpec#notInPeaceful()} 的说明。</p>
 */
public final class ImpEntityTypes {

    private ImpEntityTypes() {}

    /**
     * 实体类型的注册参数（纯数据，跨版本稳定）。
     *
     * @param width               碰撞箱宽
     * @param height              碰撞箱高
     * @param eyeHeight           视高
     * @param passengerAttachment 乘客挂点高度
     * @param clientTrackingRange 客户端追踪范围（区块）
     * @param notInPeaceful       和平难度下<b>不</b>消失
     */
    public record ImpEntityTypeSpec(float width, float height, float eyeHeight,
                                    float passengerAttachment, int clientTrackingRange,
                                    boolean notInPeaceful) {}

    /** 26.x：用 {@code DeferredRegister.Entities}，返回上转型后可跨版本通用的注册器类型。 */
    @Since("26.1")
    @Name("createRegister")
    public static DeferredRegister<EntityType<?>> createRegisterModern(String modId) {
        return DeferredRegister.createEntities(modId);
    }

    /** 1.20 / 1.21：通用 {@code DeferredRegister}，注册表键走 {@code Registries.ENTITY_TYPE}。 */
    @Until("26.1")
    @Name("createRegister")
    public static DeferredRegister<EntityType<?>> createRegisterLegacy(String modId) {
        return DeferredRegister.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, modId);
    }

    /** 26.x：{@code build(ResourceKey)}。 */
    @Since("26.1")
    @Name("registerEntityType")
    public static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerModern(
            DeferredRegister<EntityType<?>> registry, String name,
            EntityType.EntityFactory<E> factory, MobCategory category,
            ImpEntityTypeSpec spec) {
        return registry.register(name, key -> configure(
                        EntityType.Builder.of(factory, category), spec)
                .build(net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.ENTITY_TYPE, key)));
    }

    /**
     * 1.20 / 1.21：{@code build(String)}。
     *
     * <p>注意：旧版没有 {@code notInPeaceful()}，{@link ImpEntityTypeSpec#notInPeaceful()} 在这里
     * 被忽略——旧版由 {@code Mob.shouldDespawnInPeaceful()} 决定，{@code Monster} 的默认实现是
     * {@code true}。也就是说旧版下该类实体<b>会在和平难度消失</b>，与 26.x 相反；
     * 需要一致时须在实体类里覆写 {@code shouldDespawnInPeaceful()} 返回 {@code false}。</p>
     */
    @Until("26.1")
    @Name("registerEntityType")
    public static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerLegacy(
            DeferredRegister<EntityType<?>> registry, String name,
            EntityType.EntityFactory<E> factory, MobCategory category,
            ImpEntityTypeSpec spec) {
        return registry.register(name, () -> configure(EntityType.Builder.of(factory, category), spec).build(name));
    }

    /** 26.x：带 {@code notInPeaceful()}。 */
    @Since("26.1")
    @Name("configure")
    private static <E extends Entity> EntityType.Builder<E> configureModern(
            EntityType.Builder<E> builder, ImpEntityTypeSpec spec) {
        builder.sized(spec.width(), spec.height())
                .eyeHeight(spec.eyeHeight())
                .passengerAttachments(spec.passengerAttachment())
                .clientTrackingRange(spec.clientTrackingRange());
        if (spec.notInPeaceful()) {
            builder.notInPeaceful();
        }
        return builder;
    }

    /** 1.20 / 1.21：没有 {@code notInPeaceful()}，其余一致（语义见 {@link #registerLegacy}）。 */
    @Until("26.1")
    @Name("configure")
    private static <E extends Entity> EntityType.Builder<E> configureLegacy(
            EntityType.Builder<E> builder, ImpEntityTypeSpec spec) {
        return builder.sized(spec.width(), spec.height())
                .eyeHeight(spec.eyeHeight())
                .passengerAttachments(spec.passengerAttachment())
                .clientTrackingRange(spec.clientTrackingRange());
    }
}
