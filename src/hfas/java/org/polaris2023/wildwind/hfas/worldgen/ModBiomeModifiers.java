package org.polaris2023.wildwind.hfas.worldgen;

import java.util.Set;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wildwind.hfas.HFASMod;

/**
 * 生物群系修改器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_BLAZE_TREE = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "add_blaze_tree")
    );

    public static final ResourceKey<BiomeModifier> ADD_SOUL_TREE = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "add_soul_tree")
    );

    // 杜鹃树生成
    public static final ResourceKey<BiomeModifier> ADD_AZALEA_TREE = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "add_azalea_tree")
    );

    // 移除原版杜鹃树
    public static final ResourceKey<BiomeModifier> REMOVE_VANILLA_AZALEA_TREE = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "remove_vanilla_azalea_tree")
    );

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // 焚烬木树生成 - 在下界废土群系
        context.register(ADD_BLAZE_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BLAZE_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        // 灵焰木树生成 - 在灵魂沙峡谷群系
        context.register(ADD_SOUL_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SOUL_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        // 移除原版杜鹃树 - 在繁茂洞穴群系
        context.register(REMOVE_VANILLA_AZALEA_TREE, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.LUSH_CAVES)),
                HolderSet.direct(placedFeatures.getOrThrow(CavePlacements.ROOTED_AZALEA_TREE)),
                Set.of(GenerationStep.Decoration.VEGETAL_DECORATION)
        ));

        // 杜鹃树生成 - 在繁茂洞穴群系
        context.register(ADD_AZALEA_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.LUSH_CAVES)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.AZALEA_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }
}
