package org.polaris2023.wildwind.core.data;

import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.block.ModWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpTagSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心模块的标签<b>规格</b>（ADR-0005）。
 *
 * <p>这里只有"生成什么"——纯数据、零 MC 类型，三个版本族共用同一份。
 * "怎么发射"由 {@link org.polaris2023.wildwind.imp.data.ImpTagWriter} 按版本处理。</p>
 *
 * <p>条目顺序与原实现严格一致，保证 datagen 产物可逐字节对比。</p>
 */
public final class ModTagSpecs {

    private ModTagSpecs() {}

    private static final ImpTagSpec.Kind BLOCK = ImpTagSpec.Kind.BLOCK;
    private static final ImpTagSpec.Kind ITEM = ImpTagSpec.Kind.ITEM;
    private static final ImpTagSpec.Kind ENTITY_TYPE = ImpTagSpec.Kind.ENTITY_TYPE;

    // ── 方块标签 ──────────────────────────────────────────────────────────

    public static List<ImpTagSpec> blockTags() {
        List<ImpTagSpec> specs = new ArrayList<>();

        for (WoodFamily family : ModWoodFamilies.ALL) {
            appendWoodBlockTags(specs, family);
        }

        // 制箭台：工作站类
        specs.add(spec(BLOCK, "mineable/axe", ImpIds.ofHolder(ModBlocks.FLETCHING_TABLE)));

        // 花类
        specs.add(spec(BLOCK, "small_flowers",
            ImpIds.ofHolder(ModBlocks.ROSE),
            ImpIds.ofHolder(ModBlocks.SOUL_ROSE)));
        specs.add(spec(BLOCK, "flowers",
            ImpIds.ofHolder(ModBlocks.SOUL_ROSE_BUSH),
            ImpIds.ofHolder(ModBlocks.WITHER_ROSE_BUSH)));

        return List.copyOf(specs);
    }

    private static void appendWoodBlockTags(List<ImpTagSpec> specs, WoodFamily family) {
        ImpId log = ImpIds.ofHolder(family.log());
        ImpId wood = ImpIds.ofHolder(family.wood());
        ImpId strippedLog = ImpIds.ofHolder(family.strippedLog());
        ImpId strippedWood = ImpIds.ofHolder(family.strippedWood());
        ImpId planks = ImpIds.ofHolder(family.planks());
        ImpId stairs = ImpIds.ofHolder(family.stairs());
        ImpId slab = ImpIds.ofHolder(family.slab());
        ImpId fence = ImpIds.ofHolder(family.fence());
        ImpId fenceGate = ImpIds.ofHolder(family.fenceGate());
        ImpId door = ImpIds.ofHolder(family.door());
        ImpId trapdoor = ImpIds.ofHolder(family.trapdoor());
        ImpId pressurePlate = ImpIds.ofHolder(family.pressurePlate());
        ImpId button = ImpIds.ofHolder(family.button());

        specs.add(spec(BLOCK, "logs", log, wood, strippedLog, strippedWood));
        specs.add(spec(BLOCK, "logs_that_burn", log, wood, strippedLog, strippedWood));
        specs.add(spec(BLOCK, "c", "stripped_logs", strippedLog));
        specs.add(spec(BLOCK, "c", "stripped_woods", strippedWood));

        specs.add(spec(BLOCK, "planks", planks));
        specs.add(spec(BLOCK, "wooden_stairs", stairs));
        specs.add(spec(BLOCK, "wooden_slabs", slab));
        specs.add(spec(BLOCK, "wooden_fences", fence));
        specs.add(spec(BLOCK, "c", "fences/wooden", fence));
        specs.add(spec(BLOCK, "fences", fence));
        specs.add(spec(BLOCK, "c", "fence_gates/wooden", fenceGate));
        specs.add(spec(BLOCK, "fence_gates", fenceGate));
        specs.add(spec(BLOCK, "wooden_doors", door));
        specs.add(spec(BLOCK, "doors", door));
        specs.add(spec(BLOCK, "trapdoors", trapdoor));
        specs.add(spec(BLOCK, "wooden_trapdoors", trapdoor));
        specs.add(spec(BLOCK, "wooden_pressure_plates", pressurePlate));
        specs.add(spec(BLOCK, "wooden_buttons", button));

        if (family.leaves() != null) {
            specs.add(spec(BLOCK, "leaves", ImpIds.ofHolder(family.leaves())));
        }
        if (family.sapling() != null) {
            specs.add(spec(BLOCK, "saplings", ImpIds.ofHolder(family.sapling())));
        }

        specs.add(spec(BLOCK, "mineable/axe",
            log, wood, strippedLog, strippedWood, planks, stairs, slab,
            fence, fenceGate, door, trapdoor, pressurePlate, button));
        specs.add(spec(BLOCK, "overworld_natural_logs", log));
    }

    // ── 物品标签 ──────────────────────────────────────────────────────────

    public static List<ImpTagSpec> itemTags() {
        List<ImpTagSpec> specs = new ArrayList<>();

        for (WoodFamily family : ModWoodFamilies.ALL) {
            appendWoodItemTags(specs, family);
        }

        specs.add(spec(ITEM, "flowers",
            ImpIds.ofHolder(ModBlocks.SOUL_ROSE_BUSH),
            ImpIds.ofHolder(ModBlocks.WITHER_ROSE_BUSH)));

        return List.copyOf(specs);
    }

    private static void appendWoodItemTags(List<ImpTagSpec> specs, WoodFamily family) {
        ImpId log = ImpIds.ofHolder(family.log());
        ImpId wood = ImpIds.ofHolder(family.wood());
        ImpId strippedLog = ImpIds.ofHolder(family.strippedLog());
        ImpId strippedWood = ImpIds.ofHolder(family.strippedWood());
        ImpId planks = ImpIds.ofHolder(family.planks());
        ImpId stairs = ImpIds.ofHolder(family.stairs());
        ImpId slab = ImpIds.ofHolder(family.slab());
        ImpId fence = ImpIds.ofHolder(family.fence());
        ImpId fenceGate = ImpIds.ofHolder(family.fenceGate());
        ImpId door = ImpIds.ofHolder(family.door());
        ImpId trapdoor = ImpIds.ofHolder(family.trapdoor());
        ImpId pressurePlate = ImpIds.ofHolder(family.pressurePlate());
        ImpId button = ImpIds.ofHolder(family.button());

        // 模块自有日志标签：ww_core:<family>_logs（原实现先注册它）
        specs.add(new ImpTagSpec(ITEM, ImpId.of(WildWindCore.MOD_ID, family.name() + "_logs"),
            List.of(log, wood, strippedLog, strippedWood)));

        specs.add(spec(ITEM, "logs", log, wood, strippedLog, strippedWood));
        specs.add(spec(ITEM, "logs_that_burn", log, wood, strippedLog, strippedWood));
        specs.add(spec(ITEM, "c", "stripped_logs", strippedLog));
        specs.add(spec(ITEM, "c", "stripped_woods", strippedWood));

        specs.add(spec(ITEM, "planks", planks));
        specs.add(spec(ITEM, "wooden_stairs", stairs));
        specs.add(spec(ITEM, "wooden_slabs", slab));
        specs.add(spec(ITEM, "wooden_fences", fence));
        specs.add(spec(ITEM, "c", "fences/wooden", fence));
        specs.add(spec(ITEM, "c", "fence_gates/wooden", fenceGate));
        specs.add(spec(ITEM, "fence_gates", fenceGate));
        specs.add(spec(ITEM, "wooden_doors", door));
        specs.add(spec(ITEM, "doors", door));
        specs.add(spec(ITEM, "trapdoors", trapdoor));
        specs.add(spec(ITEM, "wooden_trapdoors", trapdoor));
        specs.add(spec(ITEM, "wooden_pressure_plates", pressurePlate));
        specs.add(spec(ITEM, "wooden_buttons", button));
        specs.add(spec(ITEM, "buttons", button));
    }

    // ── 实体类型标签 ──────────────────────────────────────────────────────

    public static List<ImpTagSpec> entityTypeTags() {
        ImpId spiderling = ImpIds.ofHolder(org.polaris2023.wildwind.core.registry.ModEntities.SPIDERLING);
        return List.of(
            spec(ENTITY_TYPE, "arthropod", spiderling),
            spec(ENTITY_TYPE, "dismounts_underwater", spiderling),
            spec(ENTITY_TYPE, "no_anger_from_wind_charge", spiderling)
        );
    }

    // ── 工具 ──────────────────────────────────────────────────────────────

    private static ImpTagSpec spec(ImpTagSpec.Kind kind, String path, ImpId... values) {
        return ImpTagSpec.of(kind, path, List.of(values));
    }

    private static ImpTagSpec spec(ImpTagSpec.Kind kind, String namespace, String path, ImpId... values) {
        return new ImpTagSpec(kind, ImpId.of(namespace, path), List.of(values));
    }
}
