package org.polaris2023.wildwind.imp;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.Set;

/**
 * 方块实体类型的构造口（ADR-0003）。
 *
 * <p>差异：两个版本<b>构造器形状不兼容</b>——</p>
 * <ul>
 *   <li>26.x：{@code new BlockEntityType<>(supplier, Set<Block>)}（或 {@code (supplier, boolean, Block...)}），
 *       <b>没有</b> {@code BlockEntityType.Builder}；</li>
 *   <li>1.20 / 1.21：直接构造需要多一个 {@code com.mojang.datafixers.types.Type<?>}
 *       （模组方块实体传 {@code null}），便捷写法是
 *       {@code BlockEntityType.Builder.of(supplier, blocks...).build(Type<?>)}。</li>
 * </ul>
 *
 * <p>因此内容模块不能直接写 {@code new BlockEntityType<>(...)}——泛型推断在旧版会因
 * 构造器参数个数不同而失败。</p>
 */
public final class ImpBlockEntityTypes {

    private ImpBlockEntityTypes() {}

    /** 26.x：直接构造，方块集合用不变 {@code Set}。 */
    @Since("26.1")
    @Name("create")
    public static <T extends BlockEntity> BlockEntityType<T> createModern(
            BlockEntityType.BlockEntitySupplier<T> supplier, Block... validBlocks) {
        return new BlockEntityType<>(supplier, Set.of(validBlocks));
    }

    /** 1.20 / 1.21：走 {@code Builder}，{@code Type<?>} 传 {@code null}（模组方块实体没有 DFU 类型）。 */
    @Until("26.1")
    @Name("create")
    public static <T extends BlockEntity> BlockEntityType<T> createLegacy(
            BlockEntityType.BlockEntitySupplier<T> supplier, Block... validBlocks) {
        return BlockEntityType.Builder.of(supplier, validBlocks).build(null);
    }
}
