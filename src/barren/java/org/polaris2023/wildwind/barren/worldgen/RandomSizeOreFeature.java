package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

/**
 * 随机矿团大小的矿脉特征：每次尝试在 1-10 个方块之间随机。
 *
 * @author mdjfnfn
 */
public class RandomSizeOreFeature extends OreFeature {

    public RandomSizeOreFeature() {
        super(OreConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
        OreConfiguration config = context.config();
        int size = context.random().nextInt(1, 11);
        OreConfiguration randomized = new OreConfiguration(
                config.targetStates,
                size,
                config.discardChanceOnAirExposure
        );
        return super.place(new FeaturePlaceContext<>(
                context.topFeature(),
                context.level(),
                context.chunkGenerator(),
                context.random(),
                context.origin(),
                randomized
        ));
    }
}
