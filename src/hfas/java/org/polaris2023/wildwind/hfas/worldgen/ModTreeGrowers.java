package org.polaris2023.wildwind.hfas.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.polaris2023.wildwind.hfas.HFASMod;

import java.util.Optional;

/**
 * 树木生长器注册
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModTreeGrowers {

    // 焚烬木树苗生长器
    public static final TreeGrower BLAZE = new TreeGrower(
            "blaze",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.BLAZE_TREE_KEY),
            Optional.empty()
    );

    // 灵焰木树苗生长器
    public static final TreeGrower SOUL = new TreeGrower(
            "soul",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.SOUL_TREE_KEY),
            Optional.empty()
    );

    // 杜鹃木树苗生长器
    public static final TreeGrower AZALEA = new TreeGrower(
            "azalea",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.AZALEA_TREE_KEY),
            Optional.empty()
    );
}
