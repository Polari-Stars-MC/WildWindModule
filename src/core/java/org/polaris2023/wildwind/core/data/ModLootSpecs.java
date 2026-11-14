package org.polaris2023.wildwind.core.data;

import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.block.ModWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.core.registry.ModEntities;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpLootSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心模块的战利品表<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpLootWriter} 按版本处理。</p>
 *
 * <p>条目顺序与原实现严格一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class ModLootSpecs {

    private ModLootSpecs() {}

    // ── 方块 ──────────────────────────────────────────────────────────────

    public static List<ImpLootSpec> blockLoot() {
        List<ImpLootSpec> specs = new ArrayList<>();

        for (WoodFamily family : ModWoodFamilies.ALL) {
            appendWood(specs, family);
        }

        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(ModBlocks.FLETCHING_TABLE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(ModBlocks.ROSE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(ModBlocks.SOUL_ROSE)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(ModBlocks.SOUL_ROSE_BUSH)));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(ModBlocks.WITHER_ROSE_BUSH)));

        return List.copyOf(specs);
    }

    private static void appendWood(List<ImpLootSpec> specs, WoodFamily family) {
        // 木质方块一律掉落自身；台阶在木套件里也是 dropSelf（不是双层掉 2 个的原版台阶表），
        // 与原实现保持一致。
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.log())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.wood())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.strippedLog())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.strippedWood())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.planks())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.stairs())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.slab())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.fence())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.fenceGate())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.pressurePlate())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.button())));
        specs.add(new ImpLootSpec.DropSelf(ImpIds.ofHolder(family.trapdoor())));
        specs.add(new ImpLootSpec.Door(ImpIds.ofHolder(family.door())));
    }

    // ── 实体 ──────────────────────────────────────────────────────────────

    public static List<ImpLootSpec> entityLoot() {
        // 幼蛛：线 0~2 个 + 蜘蛛眼 -1~1 个（负下界是原实现，靠 set_count 归零），两者都吃抢夺加成
        return List.of(new ImpLootSpec.EntityLoot(ImpIds.ofHolder(ModEntities.SPIDERLING), List.of(
            ImpLootSpec.EntityPool.of(ImpIds.fromString("minecraft:string"))
                .withCount(0.0F, 2.0F)
                .withLooting(0.0F, 1.0F),
            ImpLootSpec.EntityPool.of(ImpIds.fromString("minecraft:spider_eye"))
                .withCount(-1.0F, 1.0F)
                .withLooting(0.0F, 1.0F)
                .withKilledByPlayer()
        )));
    }
}
