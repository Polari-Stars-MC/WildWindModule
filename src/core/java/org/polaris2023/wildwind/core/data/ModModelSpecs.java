package org.polaris2023.wildwind.core.data;

import org.polaris2023.wildwind.core.block.ModBlocks;
import org.polaris2023.wildwind.core.block.ModWoodFamilies;
import org.polaris2023.wildwind.core.block.WoodFamily;
import org.polaris2023.wildwind.core.registry.ModItems;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.data.ImpModelSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心模块的模型<b>规格</b>（ADR-0005）。
 *
 * <p>纯数据、零 MC 类型，三族共用；发射由
 * {@link org.polaris2023.wildwind.imp.data.ImpModelWriter} 按版本处理。</p>
 */
public final class ModModelSpecs {

    private ModModelSpecs() {}

    public static List<ImpModelSpec> models() {
        List<ImpModelSpec> specs = new ArrayList<>();

        for (WoodFamily family : ModWoodFamilies.ALL) {
            specs.add(new ImpModelSpec.WoodSet(
                ImpIds.ofHolder(family.log()),
                ImpIds.ofHolder(family.wood()),
                ImpIds.ofHolder(family.strippedLog()),
                ImpIds.ofHolder(family.strippedWood()),
                ImpIds.ofHolder(family.planks()),
                ImpIds.ofHolder(family.stairs()),
                ImpIds.ofHolder(family.slab()),
                ImpIds.ofHolder(family.fence()),
                ImpIds.ofHolder(family.fenceGate()),
                ImpIds.ofHolder(family.pressurePlate()),
                ImpIds.ofHolder(family.button()),
                ImpIds.ofHolder(family.door()),
                ImpIds.ofHolder(family.trapdoor()),
                family.hasLeaves() ? ImpIds.ofHolder(family.leaves()) : null,
                family.leavesTint()
            ));
        }

        // 制箭台：外观沿用原版制箭台模型
        specs.add(new ImpModelSpec.VanillaLike(
            ImpIds.ofHolder(ModBlocks.FLETCHING_TABLE),
            ImpId.of("minecraft", "fletching_table")));

        // 花：两种十字植物 + 两种双层植物
        specs.add(new ImpModelSpec.Cross(ImpIds.ofHolder(ModBlocks.ROSE)));
        specs.add(new ImpModelSpec.Cross(ImpIds.ofHolder(ModBlocks.SOUL_ROSE)));
        // 灵魂玫瑰丛走内置助手（生成 bottom/top 两个模型），凋零玫瑰丛走自定义单模型：
        // 两者产出确实不同，保持与原实现一致
        specs.add(new ImpModelSpec.BuiltinDoublePlant(ImpIds.ofHolder(ModBlocks.SOUL_ROSE_BUSH)));
        specs.add(new ImpModelSpec.DoublePlant(ImpIds.ofHolder(ModBlocks.WITHER_ROSE_BUSH)));

        // 幼蛛刷怪蛋
        specs.add(new ImpModelSpec.FlatItem(ImpIds.ofHolder(ModItems.SPIDERLING_SPAWN_EGG)));

        return List.copyOf(specs);
    }
}
