package org.polaris2023.wildwind.imp;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;
import java.util.Optional;

/**
 * 世界生成里两个跨版本写法差异的收敛口（ADR-0003）。
 *
 * <p>差异一：加权方块状态提供器。26.x 用 {@code net.minecraft.util.random.WeightedList}，
 * 1.20 / 1.21 用 {@code net.minecraft.util.random.SimpleWeightedRandomList}——
 * 两者 API 形状相同（{@code builder().add(state, weight).build()}），但类型不同且互不相容。</p>
 *
 * <p>差异二：{@code TreeConfiguration.Builder} 的「树干下方放什么」。26.x 把它做成
 * 构造器最后一个参数 {@code belowTrunkProvider}（另有静态工具
 * {@code TreeConfiguration.defaultPlaceBelowTreeTrunkProvider}）；1.20 / 1.21 用
 * {@code dirt(BlockStateProvider)} 链式设置，没有那个静态工具。</p>
 */
public final class ImpWorldGen {

    private ImpWorldGen() {}

    /** 一条加权方块状态。 */
    public record WeightedState(BlockState state, int weight) {}

    /** 26.x：{@code WeightedList.Builder}。 */
    @Since("26.1")
    @Name("weightedStates")
    public static BlockStateProvider weightedStatesModern(List<WeightedState> entries) {
        net.minecraft.util.random.WeightedList.Builder<BlockState> builder =
                net.minecraft.util.random.WeightedList.builder();
        for (WeightedState entry : entries) {
            builder.add(entry.state(), entry.weight());
        }
        return new net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider(builder);
    }

    /** 1.20 / 1.21：{@code SimpleWeightedRandomList.Builder}。 */
    @Until("26.1")
    @Name("weightedStates")
    public static BlockStateProvider weightedStatesLegacy(List<WeightedState> entries) {
        net.minecraft.util.random.SimpleWeightedRandomList.Builder<BlockState> builder =
                net.minecraft.util.random.SimpleWeightedRandomList.builder();
        for (WeightedState entry : entries) {
            builder.add(entry.state(), entry.weight());
        }
        return new net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider(builder);
    }

    /**
     * 26.x：{@code belowTrunkProvider} 是构造器尾参。
     *
     * @param belowTrunkState 树干正下方要替换成的方块（原版杜鹃树是缠根泥土）
     */
    @Since("26.1")
    @Name("tree")
    public static TreeConfiguration treeModern(BlockState trunkState, TrunkPlacer trunkPlacer,
                                               BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer,
                                               FeatureSize minimumSize, BlockState belowTrunkState) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(trunkState),
                trunkPlacer,
                foliageProvider,
                foliagePlacer,
                Optional.empty(),
                minimumSize,
                BlockStateProvider.simple(belowTrunkState)
        ).build();
    }

    /** 1.20 / 1.21：同一语义用 {@code dirt(BlockStateProvider)} 表达。 */
    @Until("26.1")
    @Name("tree")
    public static TreeConfiguration treeLegacy(BlockState trunkState, TrunkPlacer trunkPlacer,
                                               BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer,
                                               FeatureSize minimumSize, BlockState belowTrunkState) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(trunkState),
                trunkPlacer,
                foliageProvider,
                foliagePlacer,
                Optional.empty(),
                minimumSize
        ).dirt(BlockStateProvider.simple(belowTrunkState)).build();
    }

    /**
     * 「空气」方块判定谓词（{@code EnvironmentScanPlacement} 的「要扫描到空气」那一侧）。
     *
     * <p>差异：{@code BlockTags.AIR} 是 <b>1.21 才加的</b>标签常量（javap 核对：
     * 20.6.144 的 {@code BlockTags} 没有 {@code AIR}，21.1.209 与 26.2 有）。
     * 1.20.6 里该语义只能落到 {@code BlockPredicate.ONLY_IN_AIR_PREDICATE}。</p>
     *
     * <p><b>语义等价性</b>：1.21+ 的 {@code minecraft:air} 标签内容就是
     * {@code [air, void_air, cave_air]}（见 1.21.1 的 {@code VanillaBlockTagsProvider}：
     * {@code this.tag(BlockTags.AIR).add(Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR)}），
     * 而 {@code ONLY_IN_AIR_PREDICATE} 在 1.20.6 里定义为
     * {@code matchesBlocks(Blocks.AIR)}——<b>只认 {@code Blocks.AIR}</b>，不含洞穴空气。</p>
     *
     * <p>因此旧族的谓词更严格：在洞穴空气处不会命中。对杜鹃树的放置影响是「洞穴空气顶
     * 不算可生长点」，属于<b>已知能力降级</b>，不是随手换个常量。要完全对齐得在 1.20.6 里
     * 手写 {@code matchesBlocks(Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR)}，
     * 这里选择与原版旧族实现保持一致（旧版原版没有该标签，也没有对应放置特征）。</p>
     */
    @Since("26.1")
    @Name("airPredicate")
    public static net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate airPredicateModern() {
        return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesTag(
                net.minecraft.tags.BlockTags.AIR);
    }

    /** 1.20.6：没有 {@code BlockTags.AIR}，退到 {@code ONLY_IN_AIR_PREDICATE}（见上方语义说明）。 */
    @Until("26.1")
    @Name("airPredicate")
    public static net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate airPredicateLegacy() {
        return net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.ONLY_IN_AIR_PREDICATE;
    }
}
