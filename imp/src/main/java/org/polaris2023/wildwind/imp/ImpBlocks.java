package org.polaris2023.wildwind.imp;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.function.Function;

/**
 * 方块类型判定与注册形状的版本无关口（ADR-0003）。
 *
 * <p><b>差异一（判定）</b>：26.x 把「植被方块」的公共基类从 {@code BushBlock} 抽成了
 * {@code VegetationBlock}：{@code BushBlock extends VegetationBlock}，而
 * {@code FlowerBlock} 等改为直接继承 {@code VegetationBlock}。</p>
 *
 * <p>于是 {@code instanceof BushBlock} 在三个族里语义<b>并不相同</b>——1.20/1.21 下花也算
 * {@code BushBlock}，26.x 下不算。直接用任一族的具体类都会静默改变行为，
 * 所以判定必须按族分叉，用各自「该族的植被根类型」表达同一语义。</p>
 *
 * <p><b>差异二（注册）</b>：{@code DeferredRegister.Blocks.registerBlock} 的重载集合不同
 * （javap 核对 20.6.144 / 21.1.209 / 26.2.0.75）：</p>
 * <ul>
 *   <li>1.20.6：只有 {@code registerBlock(String, Function, Properties)}——<b>必须</b>传一个
 *       现成的 {@code Properties} 实例，没有只带工厂的重载；</li>
 *   <li>1.21.1：上面那个 + {@code registerBlock(String, Function)}；</li>
 *   <li>26.2：只带工厂的重载存在，且 {@code Properties} 需要在注册时
 *       {@code setId(ResourceKey)}（由 {@code registerBlock} 内部完成）。</li>
 * </ul>
 * <p>因此内容模块不能直接写 {@code BLOCKS.registerBlock(name, factory)}——1.20.6 会因
 * 「参数个数不同」失败。统一走 {@link #registerBlock}，各族用自己可行的写法，
 * 且 26.x 的 {@code setId} 语义不变。</p>
 */
public final class ImpBlocks {

    private ImpBlocks() {}

    /**
     * 注册一个方块（工厂 + 默认 {@code Properties}）。
     *
     * <p>26.x：走只带工厂的重载，{@code Properties} 由 NeoForge 建好并 {@code setId}，
     * 这正是 26.x 要求的注册方式（方块属性必须自带 registry key）。</p>
     */
    @Since("26.1")
    @Name("registerBlock")
    public static <B extends Block> DeferredBlock<B> registerBlockModern(
            DeferredRegister.Blocks registry, String name,
            Function<BlockBehaviour.Properties, ? extends B> factory) {
        return registry.registerBlock(name, factory);
    }

    /**
     * 1.20 / 1.21：显式传一个 {@code Properties.of()}（1.20.6 没有只带工厂的重载）。
     *
     * <p>与 26.x 的差别只在 {@code setId}——旧版没有这个概念，方块 id 由注册表键决定。</p>
     */
    @Until("26.1")
    @Name("registerBlock")
    public static <B extends Block> DeferredBlock<B> registerBlockLegacy(
            DeferredRegister.Blocks registry, String name,
            Function<BlockBehaviour.Properties, ? extends B> factory) {
        return registry.registerBlock(name, factory, BlockBehaviour.Properties.of());
    }

    /** 26.x：植被根类型是 {@code VegetationBlock}。 */
    @Since("26.1")
    @Name("isVegetation")
    public static boolean isVegetationModern(net.minecraft.world.level.block.state.BlockState state) {
        return state.getBlock() instanceof net.minecraft.world.level.block.VegetationBlock;
    }

    /** 1.20 / 1.21：植被根类型仍是 {@code BushBlock}（花、草、树苗都继承它）。 */
    @Until("26.1")
    @Name("isVegetation")
    public static boolean isVegetationLegacy(net.minecraft.world.level.block.state.BlockState state) {
        return state.getBlock() instanceof net.minecraft.world.level.block.BushBlock;
    }
}
