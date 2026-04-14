package org.polaris2023.wildwind.hfas.worldgen;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.polaris2023.wildwind.hfas.HFASMod;

import java.util.List;

/**
 * 放置特征注册
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> BLAZE_TREE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "blaze_tree_placed")
    );

    public static final ResourceKey<PlacedFeature> SOUL_TREE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "soul_tree_placed")
    );

    // 杜鹃树放置
    public static final ResourceKey<PlacedFeature> AZALEA_TREE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "azalea_tree_placed")
    );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // 焚烬木树放置
        context.register(BLAZE_TREE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.BLAZE_TREE_KEY),
                List.of()
        ));

        // 灵焰木树放置
        context.register(SOUL_TREE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SOUL_TREE_KEY),
                List.of()
        ));

        // 杜鹃树放置
        context.register(AZALEA_TREE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.AZALEA_TREE_KEY),
                List.of(
                        CountPlacement.of(UniformInt.of(1, 2)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(256)),
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.matchesTag(BlockTags.AIR), 12),
                        RandomOffsetPlacement.ofTriangle(0, -1),
                        BiomeFilter.biome()
                )
        ));
    }
}
