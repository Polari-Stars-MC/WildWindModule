package org.polaris2023.wildwind.wilderness.data;

import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpLootSpec;
import org.polaris2023.wildwind.wilderness.block.WildernessBlocks;
import org.polaris2023.wildwind.wilderness.registry.ModEntities;
import org.polaris2023.wildwind.wilderness.registry.ModItems;

import java.util.List;

/**
 * 荒野模块的战利品表<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpLootWriter} 按版本处理。</p>
 *
 * <p>条目顺序与原实现严格一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class WildernessLootSpecs {

    private WildernessLootSpecs() {}

    // ── 方块 ──────────────────────────────────────────────────────────────

    public static List<ImpLootSpec> blockLoot() {
        return List.of(
            new ImpLootSpec.DropSelf(ImpIds.ofHolder(WildernessBlocks.SOUL_JACK_O_LANTERN)),

            new ImpLootSpec.DropSelf(ImpIds.ofHolder(WildernessBlocks.REEDS)),
            new ImpLootSpec.DropSelf(ImpIds.ofHolder(WildernessBlocks.CATTAILS)),
            new ImpLootSpec.DropSelf(ImpIds.ofHolder(WildernessBlocks.BOG_MUD)),
            new ImpLootSpec.DropSelf(ImpIds.ofHolder(WildernessBlocks.REMAINS))
        );
    }

    // ── 实体 ──────────────────────────────────────────────────────────────

    public static List<ImpLootSpec> entityLoot() {
        return List.of(
            // 泥沼蟹暂无掉落物，但战利品表必须存在（产物里只剩 type + random_sequence）
            new ImpLootSpec.EntityLoot(ImpIds.ofHolder(ModEntities.MUDCRAB), List.of()),

            // 恐鱼：本体可烧炼后掉落；骨粉与獠牙各 5% 概率
            new ImpLootSpec.EntityLoot(ImpIds.ofHolder(ModEntities.PIRANHA), List.of(
                ImpLootSpec.EntityPool.of(ImpIds.ofHolder(ModItems.PIRANHA)).smelted(),
                ImpLootSpec.EntityPool.of(ImpIds.fromString("minecraft:bone_meal")).withChance(0.05F),
                ImpLootSpec.EntityPool.of(ImpIds.ofHolder(ModItems.FANG)).withChance(0.05F)
            ))
        );
    }
}
