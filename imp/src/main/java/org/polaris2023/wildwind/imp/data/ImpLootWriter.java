package org.polaris2023.wildwind.imp.data;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「战利品表发射器」（ADR-0005）。
 *
 * <p>分工：</p>
 * <ul>
 *   <li>{@link ImpLootSpec} = <b>生成什么</b>（纯数据，三族共用）</li>
 *   <li>本类 = <b>怎么发射</b>（按版本分段，预处理器裁掉不适用分支，{@code @Name} 把留存段改回同名）</li>
 *   <li>{@link Sinks} = 内容模块提供的极薄适配（把该版本的注册表、战利品表接收口与
 *       原版掉落助手以一行转出）</li>
 * </ul>
 *
 * <p><b>为什么方块形状要转手给内容模块</b>：26.x 的 {@code BlockLootSubProvider} 把
 * {@code dropSelf} / {@code createDoorTable} / {@code createSlabItemTable} /
 * {@code createSilkTouchOnlyTable} / {@code dropPottedContents} /
 * {@code createLeavesDrops} / {@code noDrop} 全做成 {@code protected}，且
 * {@code add(Block, LootTable.Builder)} 也是 {@code protected}。imp 与
 * {@code net.minecraft.data.loot} 不同包、也不是它的子类，**编译期就调不到**。
 * 在 imp 里重抄一份原版助手（树叶还含 {@code table_bonus} 的硬编码概率表、
 * {@code NORMAL_LEAVES_STICK_CHANCES}、精准采集/剪刀的组件判据）只会让产物
 * 在 Minecraft 升级时悄悄漂移，与「产物逐字节一致」的硬约束直接冲突。
 * 因此方块侧走"原版助手由子类一行转出"，而<b>实体表在 imp 里真正组装</b>——
 * 实体侧全部是公开 API（{@code LootPool} / {@code LootItem} / 各类函数与条件），
 * 没有这层访问限制。</p>
 *
 * <p><b>两代的差异点（均为 javap 核实）</b>：方块侧七个助手在两代里同名同形
 * （{@code dropSelf} / {@code createDoorTable} / {@code createSlabItemTable} /
 * {@code createSilkTouchOnlyTable} / {@code dropPottedContents} /
 * {@code createLeavesDrops} / {@code noDrop}），所以旧版分支与 26.x 分支<b>体一致</b>，
 * 差别只在"解析 id"的注册表取值入口（旧版 {@code Registry#get}、26.x
 * {@code Registry#getValue}）。实体侧的池与条目构建 API 也一致，唯一实质差异是
 * 抢夺加成函数：1.21 起叫 {@code EnchantedCountIncreaseFunction.lootingMultiplier}、
 * 且多一个 {@code HolderLookup.Provider} 形参；1.20 叫
 * {@code LootingEnchantFunction.lootingMultiplier}、只吃数量提供器。
 * 该差异由 {@code looting(...)} 用
 * {@code @Since("1.21")} / {@code @Until("1.21")} 两段收敛（不用 {@code @link}：
 * 26.x 产物里没有这个成员，链接会悬空）。</p>
 *
 * <p><b>现状</b>：三族分支均为真实实现。26.x 经 datagen 产物 diff 验证；
 * 旧版分支目前只保证<b>编译通过</b>——内容模块尚未支持旧版编译，无法做运行时验证。</p>
 */
public final class ImpLootWriter {

    private ImpLootWriter() {}

    /**
     * 内容模块实现的薄适配：交出该版本的注册表、战利品表接收口与掉落助手。
     *
     * <p>所有方法都有默认实现且默认抛异常：一个模块的方块表与实体表分属两个
     * Provider 类（{@code BlockLootSubProvider} 与 {@code EntityLootSubProvider}），
     * 各自只需要覆写自己那一半。若真的调到了未覆写的那一半，说明规格与生成器
     * 配错了，显式失败比静默产出空表更好。</p>
     */
    public interface Sinks {

        // ── 方块侧：每个方法都只是把该版本的 protected 助手转出来 ──────────

        /** 对应 26.x 的 {@code dropSelf(Block)}。 */
        default void dropSelf(Block block) {
            throw unsupported("dropSelf");
        }

        /** 对应 26.x 的 {@code add(block, createDoorTable(block))}。 */
        default void door(Block block) {
            throw unsupported("door");
        }

        /** 对应 26.x 的 {@code add(slab, createSlabItemTable(slab))}。 */
        default void slab(Block slab) {
            throw unsupported("slab");
        }

        /** 对应 26.x 的 {@code add(block, createSilkTouchOnlyTable(block))}。 */
        default void silkTouchOnly(Block block) {
            throw unsupported("silkTouchOnly");
        }

        /** 对应 26.x 的 {@code dropPottedContents(potted)}。 */
        default void pottedContents(Block potted) {
            throw unsupported("pottedContents");
        }

        /**
         * 对应 26.x 的 {@code add(leaves, createLeavesDrops(leaves, sapling, saplingChance))}。
         *
         * @param saplingChance 单元素概率表
         */
        default void leaves(Block leaves, Block sapling, float saplingChance) {
            throw unsupported("leaves");
        }

        /** 对应 26.x 的 {@code add(block, noDrop())}。 */
        default void noDrop(Block block) {
            throw unsupported("noDrop");
        }

        // ── 实体侧：表由本类组装，适配器只交出接收口与两处版本助手 ──────────

        /** 对应 26.x 的 {@code add(EntityType, LootTable.Builder)}。 */
        default void addEntity(EntityType<?> type, LootTable.Builder table) {
            throw unsupported("addEntity");
        }

        /**
         * 该版本的 {@code HolderLookup.Provider}（抢夺加成函数需要它）。
         *
         * <p>只有 1.21 及以后需要：{@code EnchantedCountIncreaseFunction.lootingMultiplier}
         * 的第一个形参就是它。1.20.6 的 {@code LootingEnchantFunction.lootingMultiplier}
         * 没有这个形参，因此该族的适配器不需要覆写本方法。</p>
         */
        default Object registries() {
            throw unsupported("registries");
        }

        /**
         * 该版本的"是否烧炼后掉落"条件（26.x / 1.21 的 {@code shouldSmeltLoot()}）。
         *
         * <p><b>1.20.6 没有 {@code shouldSmeltLoot()}</b>（javap 核实：
         * {@code EntityLootSubProvider} 在该版本里既无该方法、也无 {@code registries} 字段，
         * 只有 {@code protected static final EntityPredicate.Builder ENTITY_ON_FIRE}）。
         * 1.20.6 的适配器应返回
         * {@code LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)}，
         * 与 1.21 的 {@code shouldSmeltLoot()} 第一分支同义，
         * 这也是原版 {@code VanillaEntityLoot} 在该版本的写法。</p>
         */
        default Object smeltLoot() {
            throw unsupported("smeltLoot");
        }

        private static UnsupportedOperationException unsupported(String kind) {
            return new UnsupportedOperationException(
                "该战利品生成器未提供适配方法：" + kind +
                    "。方块表与实体表分属两个 Provider，请检查规格与生成器是否配对。");
        }
    }

    /**
     * 按规格逐条发射战利品表。
     *
     * <p>这里用 {@code instanceof} 链而不是 switch 模式匹配：1.20 族按较低的语言级别编译。</p>
     */
    public static void apply(List<ImpLootSpec> specs, Sinks sinks) {
        for (ImpLootSpec spec : specs) {
            if (spec instanceof ImpLootSpec.DropSelf dropSelf) {
                emitDropSelf(dropSelf, sinks);
            } else if (spec instanceof ImpLootSpec.Door door) {
                emitDoor(door, sinks);
            } else if (spec instanceof ImpLootSpec.Slab slab) {
                emitSlab(slab, sinks);
            } else if (spec instanceof ImpLootSpec.SilkTouchOnly silkTouchOnly) {
                emitSilkTouchOnly(silkTouchOnly, sinks);
            } else if (spec instanceof ImpLootSpec.PottedContents pottedContents) {
                emitPottedContents(pottedContents, sinks);
            } else if (spec instanceof ImpLootSpec.Leaves leaves) {
                emitLeaves(leaves, sinks);
            } else if (spec instanceof ImpLootSpec.NoDrop noDrop) {
                emitNoDrop(noDrop, sinks);
            } else if (spec instanceof ImpLootSpec.EntityLoot entityLoot) {
                emitEntityLoot(entityLoot, sinks);
            } else {
                throw new IllegalArgumentException("未支持的战利品规格：" + spec);
            }
        }
    }

    // ── 26.x：原版 BlockLootSubProvider / EntityLootSubProvider ───────────

    @Since("26.1")
    @Name("emitDropSelf")
    private static void emitDropSelfModern(ImpLootSpec.DropSelf spec, Sinks sinks) {
        sinks.dropSelf(block(spec.block()));
    }

    @Since("26.1")
    @Name("emitDoor")
    private static void emitDoorModern(ImpLootSpec.Door spec, Sinks sinks) {
        sinks.door(block(spec.block()));
    }

    @Since("26.1")
    @Name("emitSlab")
    private static void emitSlabModern(ImpLootSpec.Slab spec, Sinks sinks) {
        sinks.slab(block(spec.block()));
    }

    @Since("26.1")
    @Name("emitSilkTouchOnly")
    private static void emitSilkTouchOnlyModern(ImpLootSpec.SilkTouchOnly spec, Sinks sinks) {
        sinks.silkTouchOnly(block(spec.block()));
    }

    @Since("26.1")
    @Name("emitPottedContents")
    private static void emitPottedContentsModern(ImpLootSpec.PottedContents spec, Sinks sinks) {
        sinks.pottedContents(block(spec.potted()));
    }

    @Since("26.1")
    @Name("emitLeaves")
    private static void emitLeavesModern(ImpLootSpec.Leaves spec, Sinks sinks) {
        sinks.leaves(block(spec.leaves()), block(spec.sapling()), spec.saplingChance());
    }

    @Since("26.1")
    @Name("emitNoDrop")
    private static void emitNoDropModern(ImpLootSpec.NoDrop spec, Sinks sinks) {
        sinks.noDrop(block(spec.block()));
    }

    @Since("26.1")
    @Name("emitEntityLoot")
    private static void emitEntityLootModern(ImpLootSpec.EntityLoot spec, Sinks sinks) {
        var table = net.minecraft.world.level.storage.loot.LootTable.lootTable();

        // 池与条目的顺序即产物顺序，必须与规格声明顺序一致。
        for (ImpLootSpec.EntityPool pool : spec.pools()) {
            var builder = net.minecraft.world.level.storage.loot.LootPool.lootPool()
                .setRolls(net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1.0F));

            net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> entry =
                net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(item(pool.item()));

            if (pool.count() != null) {
                entry = entry.apply(
                    net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount(uniform(pool.count())));
            }
            if (pool.looting() != null) {
                entry = entry.apply(
                    net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier(
                        (net.minecraft.core.HolderLookup.Provider) sinks.registries(), uniform(pool.looting())));
            }
            if (pool.smelt()) {
                entry = entry.apply(
                    net.minecraft.world.level.storage.loot.functions.SmeltItemFunction.smelted()
                        .when((net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder) sinks.smeltLoot()));
            }

            builder.add(entry);

            if (pool.killedByPlayer()) {
                builder.when(net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition.killedByPlayer());
            }
            if (pool.randomChance() != null) {
                builder.when(net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
                    .randomChance(pool.randomChance().floatValue()));
            }

            table.withPool(builder);
        }

        sinks.addEntity(entityType(spec.entity()), table);
    }

    // ── 1.20 / 1.21：原版 BlockLootSubProvider / EntityLootSubProvider ────
    //
    // 方块侧仍走 Sinks：旧版这七个助手同样是 protected（javap -p
    // net.minecraft.data.loot.BlockLootSubProvider 已核实 1.20.6 与 1.21.1 均如此），
    // 所以"形状选在 imp、构造留在内容模块"的分工与 26.x 完全一致，
    // 这里只负责解析 id 并把形状交给该版本的助手。

    @Until("26.1")
    @Name("emitDropSelf")
    private static void emitDropSelfLegacy(ImpLootSpec.DropSelf spec, Sinks sinks) {
        sinks.dropSelf(block(spec.block()));
    }

    @Until("26.1")
    @Name("emitDoor")
    private static void emitDoorLegacy(ImpLootSpec.Door spec, Sinks sinks) {
        sinks.door(block(spec.block()));
    }

    @Until("26.1")
    @Name("emitSlab")
    private static void emitSlabLegacy(ImpLootSpec.Slab spec, Sinks sinks) {
        sinks.slab(block(spec.block()));
    }

    @Until("26.1")
    @Name("emitSilkTouchOnly")
    private static void emitSilkTouchOnlyLegacy(ImpLootSpec.SilkTouchOnly spec, Sinks sinks) {
        sinks.silkTouchOnly(block(spec.block()));
    }

    @Until("26.1")
    @Name("emitPottedContents")
    private static void emitPottedContentsLegacy(ImpLootSpec.PottedContents spec, Sinks sinks) {
        sinks.pottedContents(block(spec.potted()));
    }

    @Until("26.1")
    @Name("emitLeaves")
    private static void emitLeavesLegacy(ImpLootSpec.Leaves spec, Sinks sinks) {
        sinks.leaves(block(spec.leaves()), block(spec.sapling()), spec.saplingChance());
    }

    @Until("26.1")
    @Name("emitNoDrop")
    private static void emitNoDropLegacy(ImpLootSpec.NoDrop spec, Sinks sinks) {
        sinks.noDrop(block(spec.block()));
    }

    @Until("26.1")
    @Name("emitEntityLoot")
    private static void emitEntityLootLegacy(ImpLootSpec.EntityLoot spec, Sinks sinks) {
        var table = net.minecraft.world.level.storage.loot.LootTable.lootTable();

        // 与 26.x 分支逐句同构：池与条目的顺序即产物顺序。
        for (ImpLootSpec.EntityPool pool : spec.pools()) {
            var builder = net.minecraft.world.level.storage.loot.LootPool.lootPool()
                .setRolls(net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1.0F));

            net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> entry =
                net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(item(pool.item()));

            if (pool.count() != null) {
                entry = entry.apply(
                    net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount(uniform(pool.count())));
            }
            if (pool.looting() != null) {
                entry = entry.apply(looting(pool.looting(), sinks));
            }
            if (pool.smelt()) {
                entry = entry.apply(
                    net.minecraft.world.level.storage.loot.functions.SmeltItemFunction.smelted()
                        .when((net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder) sinks.smeltLoot()));
            }

            builder.add(entry);

            if (pool.killedByPlayer()) {
                builder.when(net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition.killedByPlayer());
            }
            if (pool.randomChance() != null) {
                // 1.20.6 的 randomChance 只有 (float) 重载，1.21.1 起多了 (NumberProvider) 重载；
                // 两者都有 (float)，走它即可两族通吃，产物也一致（都是 {@code random_chance} + 常数）。
                builder.when(net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
                    .randomChance(pool.randomChance().floatValue()));
            }

            table.withPool(builder);
        }

        sinks.addEntity(entityType(spec.entity()), table);
    }

    // ── 抢夺加成：1.21 改名 + 多一个注册表形参 ─────────────────────────────
    //
    // javap 证据：
    //   1.20.6  LootingEnchantFunction.lootingMultiplier(NumberProvider)
    //           → 产物函数类型 minecraft:looting_enchant
    //   1.21.1  EnchantedCountIncreaseFunction.lootingMultiplier(HolderLookup$Provider, NumberProvider)
    //           → 产物函数类型 minecraft:enchanted_count_increase
    // 两代签名不同（形参个数也不同），无法只靠改名收敛，故按版本分段、用 @Name 统一成 looting。

    @Since("1.21")
    @Until("26.1")
    @Name("looting")
    private static net.minecraft.world.level.storage.loot.functions.LootItemFunction.Builder lootingTen21(
            ImpLootSpec.CountRange range, Sinks sinks) {
        return net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier(
            (net.minecraft.core.HolderLookup.Provider) sinks.registries(), uniform(range));
    }

    @Until("1.21")
    @Name("looting")
    private static net.minecraft.world.level.storage.loot.functions.LootItemFunction.Builder lootingTen20(
            ImpLootSpec.CountRange range, Sinks sinks) {
        // 1.20 没有注册表形参，也不需要 registries()：Sinks.registries() 在这个族里不会有人覆写。
        return net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction.lootingMultiplier(uniform(range));
    }

    // ── 注册表解析（两代 API 的名称差异在这里收敛）────────────────────────
    //
    // 取值入口：旧版 Registry#get(ResourceLocation)，26.x 改名 Registry#getValue(Identifier)。

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

    @Since("26.1")
    @Name("item")
    private static net.minecraft.world.item.Item itemModern(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("item")
    private static net.minecraft.world.item.Item itemLegacy(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.get(id.unwrap());
    }

    @Since("26.1")
    @Name("entityType")
    private static EntityType<?> entityTypeModern(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("entityType")
    private static EntityType<?> entityTypeLegacy(ImpId id) {
        return net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(id.unwrap());
    }

    @Since("26.1")
    @Name("uniform")
    private static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator uniformModern(
            ImpLootSpec.CountRange range) {
        return net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between(range.min(), range.max());
    }

    @Until("26.1")
    @Name("uniform")
    private static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator uniformLegacy(
            ImpLootSpec.CountRange range) {
        return net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between(range.min(), range.max());
    }
}
