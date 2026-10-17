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



    // 杜鹃木树苗生长器
    public static final TreeGrower AZALEA = new TreeGrower(
            "azalea",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.AZALEA_TREE_KEY),
            Optional.empty()
    );
    // ����ľ����������
    public static final TreeGrower CINDER = new TreeGrower(
            "cinder",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.CINDER_TREE_KEY),
            Optional.empty()
    );

    // ���ľ����������
    public static final TreeGrower EMBER = new TreeGrower(
            "ember",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.EMBER_TREE_KEY),
            Optional.empty()
    );
}
