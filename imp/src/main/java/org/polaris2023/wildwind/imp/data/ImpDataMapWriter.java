package org.polaris2023.wildwind.imp.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「数据映射发射器」（ADR-0005）。
 *
 * <p>分工：</p>
 * <ul>
 *   <li>{@link ImpDataMapSpec} = <b>生成什么</b>（哪张数据映射 + 目标 id + 值；纯数据，三族共用）</li>
 *   <li>本类 = <b>怎么发射</b>（按版本分段，预处理器裁掉不适用分支，{@code @Name} 把留存段改回同名）</li>
 *   <li>{@link Sinks} = 内容模块提供的极薄适配（交出该版本的两张数据映射构建器）</li>
 * </ul>
 *
 * <p><b>产物逐字节不变</b>：JSON 里 {@code values} 的键序由
 * {@code ExtraCodecs.strictUnboundedMap} 在编码时统一排序，与插入顺序无关；
 * {@code replace} 是 {@code optionalFieldOf("replace", false)}，传 {@code false}
 * 时该字段不落盘。因此这里只要保证"条目集合 + 值 + replace 标志"三者与原实现
 * 完全一致，产物就一致。</p>
 *
 * <p><b>三族差异</b>（均经 javap 核实）：</p>
 * <ul>
 *   <li>燃料：{@code DataMapProvider.Builder} 与 {@code FurnaceFuel(int)} 在 1.20.6 /
 *       1.21.1 / 26.2 三族里同形，故三族分支体一致，只有注册表取值入口
 *       （{@code get} → {@code getValue}）与 id 类型（{@code ResourceLocation} →
 *       {@code Identifier}）不同。</li>
 *   <li>去皮：{@code Strippable} 与 {@code NeoForgeDataMaps.STRIPPABLES} 是 26.x 才有的，
 *       1.20.6 / 1.21.1 里连类都没有；旧版去皮走原版 {@code AxeItem.STRIPPABLES}，
 *       不读数据包。因此旧版分支保留显式失败，而不是产出一张无人消费的 JSON。</li>
 * </ul>
 */
public final class ImpDataMapWriter {

    private ImpDataMapWriter() {}

    /**
     * 内容模块实现的薄适配：把该版本的两张数据映射构建器以 {@code Object} 交出来。
     *
     * <p>返回 {@code Object} 是因为值类型 {@code Strippable} 只在 26.x 的 NeoForge
     * 里存在（1.20.6 / 1.21.1 的 builtin 包没有它），把它写进接口签名会让旧族的
     * 本类直接编译失败。实现通常只有一行：
     * {@code return builder(NeoForgeDataMaps.FURNACE_FUELS);}
     * 类型安全由发射器内部的一次强制转换承担（见 {@code emitFuel}）。</p>
     */
    public interface Sinks {

        /** 燃料数据映射的构建器（26.x：{@code DataMapProvider.Builder<FurnaceFuel, Item>}）。 */
        Object furnaceFuels();

        /** 去皮数据映射的构建器（26.x：{@code DataMapProvider.Builder<Strippable, Block>}）。 */
        Object strippables();
    }

    /**
     * 按规格逐条发射。
     *
     * <p>这里用 {@code instanceof} 链而不是 switch 模式匹配：1.20 族按较低的语言级别编译。</p>
     */
    public static void apply(List<ImpDataMapSpec> specs, Sinks sinks) {
        for (ImpDataMapSpec spec : specs) {
            if (spec instanceof ImpDataMapSpec.Fuel fuel) {
                emitFuel(fuel, sinks);
            } else if (spec instanceof ImpDataMapSpec.Strippable strippable) {
                emitStrippable(strippable, sinks);
            } else {
                throw new IllegalArgumentException("未支持的数据映射规格：" + spec);
            }
        }
    }

    // ── 26.x：DataMapProvider.Builder<T, R> ───────────────────────────────

    @Since("26.1")
    @Name("emitFuel")
    @SuppressWarnings("unchecked")
    private static void emitFuelModern(ImpDataMapSpec.Fuel spec, Sinks sinks) {
        var builder = (net.neoforged.neoforge.common.data.DataMapProvider.Builder<
            net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel, Item>) sinks.furnaceFuels();
        // replace = false：与原实现一致，产物里不出现 replace 字段。
        builder.add(itemKey(spec.item()),
            new net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel(spec.burnTime()), false);
    }

    @Since("26.1")
    @Name("emitStrippable")
    @SuppressWarnings("unchecked")
    private static void emitStrippableModern(ImpDataMapSpec.Strippable spec, Sinks sinks) {
        var builder = (net.neoforged.neoforge.common.data.DataMapProvider.Builder<
            net.neoforged.neoforge.registries.datamaps.builtin.Strippable, Block>) sinks.strippables();
        builder.add(blockKey(spec.block()),
            new net.neoforged.neoforge.registries.datamaps.builtin.Strippable(block(spec.strippedBlock())), false);
    }

    // ── 1.20 / 1.21：DataMapProvider.Builder<T, R>（与 26.x 同形）─────────
    //
    // 燃料：三族的 DataMapProvider.Builder<T, R> 与 FurnaceFuel 记录完全一致
    // （javap 20.6.144 / 21.1.209 / 26.2.0.75：add(ResourceKey<R>, T, boolean, ICondition...)
    // 与 FurnaceFuel(int) 逐字相同），所以这里只是同一段实现按族各存一份，
    // 让预处理器裁掉不适用的一段——产物与 26.x 分支一致。

    @Until("26.1")
    @Name("emitFuel")
    @SuppressWarnings("unchecked")
    private static void emitFuelLegacy(ImpDataMapSpec.Fuel spec, Sinks sinks) {
        var builder = (net.neoforged.neoforge.common.data.DataMapProvider.Builder<
            net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel, Item>) sinks.furnaceFuels();
        // replace = false：与原实现一致，产物里不出现 replace 字段。
        builder.add(itemKey(spec.item()),
            new net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel(spec.burnTime()), false);
    }

    // ── 1.20 / 1.21：去皮映射没有对应 API，保留显式失败 ────────────────────
    //
    // javap 证据（三族都核对过）：
    //   1.20.6 / 1.21.1 的 merged jar 内不存在
    //     net/neoforged/neoforge/registries/datamaps/builtin/Strippable.class，
    //     且 NeoForgeDataMaps 里只有 COMPOSTABLES / FURNACE_FUELS / MONSTER_ROOM_MOBS /
    //     PARROT_IMITATIONS / RAID_HERO_GIFTS / VIBRATION_FREQUENCIES（1.21.1 另加
    //     OXIDIZABLES / VILLAGER_TYPES / WAXABLES），**没有 STRIPPABLES**；
    //   26.2.0.75 的 universal jar 才有 Strippable 与 NeoForgeDataMaps.STRIPPABLES。
    // 旧版去皮由原版 AxeItem.STRIPPABLES（protected static final Map<Block, Block>，
    // 20.6.144 / 21.1.209 皆有）在代码里维护，根本不读数据包。因此"造一张自注册的
    // DataMapType 把 JSON 写出来"只会得到一个没人读的死文件，属于语义不正确的实现，
    // 故保留显式失败而不是硬凑。

    @Until("26.1")
    @Name("emitStrippable")
    private static void emitStrippableLegacy(ImpDataMapSpec.Strippable spec, Sinks sinks) {
        throw unsupportedStrippable(spec);
    }

    /** 与 {@code emitStrippableLegacy} 同版本存在；26.x 里整段被预处理器裁掉，不留死代码。 */
    @Until("26.1")
    private static UnsupportedOperationException unsupportedStrippable(ImpDataMapSpec.Strippable spec) {
        return new UnsupportedOperationException(
            "旧版（1.20.6 / 1.21.1）无法发射去皮数据映射条目 " + spec.block() + " -> " + spec.strippedBlock() +
                "：缺少数据映射类型。1.20.6 与 1.21.1 的 NeoForge 里没有 " +
                "net.neoforged.neoforge.registries.datamaps.builtin.Strippable 类，NeoForgeDataMaps 也没有 " +
                "STRIPPABLES 字段（Strippable 与 STRIPPABLES 是 26.x 新增）；旧版去皮由原版 " +
                "AxeItem.STRIPPABLES 这张 protected static final Map<Block, Block> 在代码里维护，不读数据包。" +
                "建议：旧版族不要走数据映射，改由内容模块在初始化阶段把「原方块 -> 去皮方块」写进 " +
                "AxeItem.STRIPPABLES（或注册自定义 AxeItem 覆写其行为），不要生成没有消费方的 JSON。" +
                "见 ADR-0005。");
    }

    // ── 注册表解析：取值入口在 26.x 改名（get → getValue），故按版本分叉 ────

    @Since("26.1")
    @Name("itemKey")
    private static ResourceKey<Item> itemKeyModern(ImpId id) {
        Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(id.unwrap());
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getResourceKey(item)
            .orElseThrow(() -> new IllegalStateException("未注册的物品: " + id));
    }

    @Until("26.1")
    @Name("itemKey")
    private static ResourceKey<Item> itemKeyLegacy(ImpId id) {
        Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(id.unwrap());
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getResourceKey(item)
            .orElseThrow(() -> new IllegalStateException("未注册的物品: " + id));
    }

    @Since("26.1")
    @Name("blockKey")
    private static ResourceKey<Block> blockKeyModern(ImpId id) {
        Block block = block(id);
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getResourceKey(block)
            .orElseThrow(() -> new IllegalStateException("未注册的方块: " + id));
    }

    @Until("26.1")
    @Name("blockKey")
    private static ResourceKey<Block> blockKeyLegacy(ImpId id) {
        Block block = block(id);
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getResourceKey(block)
            .orElseThrow(() -> new IllegalStateException("未注册的方块: " + id));
    }

    @Since("26.1")
    @Name("block")
    private static Block blockModern(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("block")
    private static Block blockLegacy(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(id.unwrap());
    }
}
