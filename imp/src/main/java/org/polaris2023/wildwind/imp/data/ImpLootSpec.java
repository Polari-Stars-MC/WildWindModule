package org.polaris2023.wildwind.imp.data;

import org.polaris2023.wildwind.imp.ImpId;

import java.util.List;
import java.util.Objects;

/**
 * 版本无关的「战利品表规格」（ADR-0005）。
 *
 * <p>战利品 datagen 的知识同样分两层：<b>生成什么</b>（哪些方块/实体、什么掉落形状）
 * 与 <b>怎么发射</b>（该版本的 {@code BlockLootSubProvider} / {@code EntityLootSubProvider}
 * 助手与 {@code LootTable} 构建器 API）。前者是纯数据，三族共用；后者随版本变化，
 * 由 {@link ImpLootWriter} 分段承载。</p>
 *
 * <p>本类只描述前者：只用 {@link ImpId} 指代方块/物品/实体，不出现任何 MC 类型。</p>
 *
 * <p><b>变体只覆盖仓库里真实用到的形状</b>（见各模块的 {@code *LootSpecs}）：</p>
 * <ul>
 *   <li>方块：{@link DropSelf}、{@link Door}、{@link Slab}、{@link SilkTouchOnly}、
 *       {@link PottedContents}、{@link Leaves}、{@link NoDrop}</li>
 *   <li>实体：{@link EntityLoot}（若干「单物品池」，带可选的数量/抢夺/烧炼与池条件）</li>
 * </ul>
 *
 * <p>目前没有 ore drops、剪刀专属掉落、方块实体 NBT 掉落等形状的 datagen 用例，
 * 故刻意不预设变体——需要时再加，避免交付"从未被真实产出验证过"的规格。</p>
 */
public sealed interface ImpLootSpec {

    // ── 方块 ──────────────────────────────────────────────────────────────

    /**
     * 掉落方块自身（26.x 的 {@code dropSelf}）。
     *
     * <p>产出一张单池表：池条件 {@code survives_explosion}，条目即该方块。注意
     * {@code 26.x} 的 {@code BlockLootSubProvider} 里"抗爆物品"集合为空，
     * 所以爆炸条件恒被加上。</p>
     */
    record DropSelf(ImpId block) implements ImpLootSpec {
        public DropSelf {
            Objects.requireNonNull(block, "block");
        }
    }

    /** 门：只在下半掉落自身（26.x 的 {@code createDoorTable}）。 */
    record Door(ImpId block) implements ImpLootSpec {
        public Door {
            Objects.requireNonNull(block, "block");
        }
    }

    /** 台阶：双层时掉 2 个（26.x 的 {@code createSlabItemTable}）。 */
    record Slab(ImpId block) implements ImpLootSpec {
        public Slab {
            Objects.requireNonNull(block, "block");
        }
    }

    /**
     * 仅精准采集掉落自身（26.x 的 {@code createSilkTouchOnlyTable}）。
     *
     * <p>用于可疑沙这类"空手挖什么都不掉"的方块。</p>
     */
    record SilkTouchOnly(ImpId block) implements ImpLootSpec {
        public SilkTouchOnly {
            Objects.requireNonNull(block, "block");
        }
    }

    /**
     * 花盆内容物：掉花盆 + 盆里的植物（26.x 的 {@code dropPottedContents}）。
     *
     * @param potted 花盆方块本身（内容物由该方块实例自行决定，故规格只写花盆，
     *               与原实现一致）
     */
    record PottedContents(ImpId potted) implements ImpLootSpec {
        public PottedContents {
            Objects.requireNonNull(potted, "potted");
        }
    }

    /**
     * 树叶：剪刀/精准采集合自身，否则按表加成掉树苗，另加木棍池
     * （26.x 的 {@code createLeavesDrops}）。
     *
     * <p>树叶掉落是一条原版合成的整表逻辑（含 {@code table_bonus} 的硬编码概率表），
     * 规格里只声明"哪片树叶配哪个树苗、树苗概率多少"，具体表结构由发射器交给
     * 该版本的原版助手产出，避免在 imp 里重抄一份原版逻辑。</p>
     *
     * @param leaves        树叶方块
     * @param sapling       对应树苗方块
     * @param saplingChance 树苗的概率表首项（现有用例全部是 {@code 0.05F}）
     */
    record Leaves(ImpId leaves, ImpId sapling, float saplingChance) implements ImpLootSpec {
        public Leaves {
            Objects.requireNonNull(leaves, "leaves");
            Objects.requireNonNull(sapling, "sapling");
        }
    }

    /**
     * 没有掉落物，但战利品表必须存在（26.x 的 {@code noDrop}）。
     *
     * <p>原版墙面告示牌就是这种：掉落由方块自身逻辑处理，表里只剩
     * {@code type} 与 {@code random_sequence}。</p>
     */
    record NoDrop(ImpId block) implements ImpLootSpec {
        public NoDrop {
            Objects.requireNonNull(block, "block");
        }
    }

    // ── 实体 ──────────────────────────────────────────────────────────────

    /**
     * 实体的整张战利品表。
     *
     * @param entity 实体类型 id
     * @param pools  掉落池，<b>按声明顺序</b>发射；空列表表示"有表但不掉东西"
     */
    record EntityLoot(ImpId entity, List<EntityPool> pools) implements ImpLootSpec {
        public EntityLoot {
            Objects.requireNonNull(entity, "entity");
            pools = List.copyOf(pools);
        }
    }

    /**
     * 单个掉落池：只含一个物品条目，投掷次数恒为 1。
     *
     * <p>现有三个实体（幼蛛、恐鱼、泥沼蟹）的掉落全部落在这个形状里，
     * 唯一的差别是可选的数量区间、抢夺加成、烧炼函数与池条件，
     * 因此用一组可选字段表达，而不是给每个实体单独造规格。</p>
     *
     * @param item          掉落物品 id
     * @param count         数量区间（{@code set_count} + {@code uniform}），{@code null} 表示不设
     * @param looting       抢夺加成区间（{@code enchanted_count_increase}），{@code null} 表示不设
     * @param smelt         是否接上"烧炼后掉落"函数（{@code furnace_smelt} + 该版本的
     *                      {@code shouldSmeltLoot()} 条件）
     * @param randomChance  池条件 {@code random_chance} 的概率，{@code null} 表示无条件
     * @param killedByPlayer 是否加上池条件 {@code killed_by_player}
     */
    record EntityPool(
        ImpId item,
        CountRange count,
        CountRange looting,
        boolean smelt,
        Float randomChance,
        boolean killedByPlayer
    ) {

        public EntityPool {
            Objects.requireNonNull(item, "item");
        }

        /** 一个"什么都不加"的单物品池。 */
        public static EntityPool of(ImpId item) {
            return new EntityPool(item, null, null, false, null, false);
        }

        public EntityPool withCount(float min, float max) {
            return new EntityPool(item, new CountRange(min, max), looting, smelt, randomChance, killedByPlayer);
        }

        public EntityPool withLooting(float min, float max) {
            return new EntityPool(item, count, new CountRange(min, max), smelt, randomChance, killedByPlayer);
        }

        public EntityPool smelted() {
            return new EntityPool(item, count, looting, true, randomChance, killedByPlayer);
        }

        public EntityPool withChance(float chance) {
            return new EntityPool(item, count, looting, smelt, chance, killedByPlayer);
        }

        public EntityPool withKilledByPlayer() {
            return new EntityPool(item, count, looting, smelt, randomChance, true);
        }
    }

    /** 一个浮点区间，发射为该版本的均匀分布数量提供器（{@code minecraft:uniform}）。 */
    record CountRange(float min, float max) {
        public CountRange {
            if (min > max) {
                throw new IllegalArgumentException("数量区间下界不能大于上界：" + min + " > " + max);
            }
        }
    }
}
