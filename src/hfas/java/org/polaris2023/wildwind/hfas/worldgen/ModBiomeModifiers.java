package org.polaris2023.wildwind.hfas.worldgen;

import java.util.Set;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.registry.ModEntities;
import org.polaris2023.wildwind.hfas.tag.ModBiomeTags;

/**
 * 生物群系修改器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModBiomeModifiers {



    // 杜鹃树生成
    public static final ResourceKey<BiomeModifier> ADD_AZALEA_TREE = createKey("add_azalea_tree");

    // 移除原版杜鹃树
    public static final ResourceKey<BiomeModifier> REMOVE_VANILLA_AZALEA_TREE = createKey("remove_vanilla_azalea_tree");

    //那啥蟹生成
    public static final ResourceKey<BiomeModifier> ADD_MUDCRAB_SPAWNS = createKey("add_mudcrab_spawns");

    //食人鱼生成
    public static final ResourceKey<BiomeModifier> ADD_PIRANHA_SPAWNS = createKey("add_piranha_spawns");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);



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

        context.register(ADD_MUDCRAB_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
                        WeightedList.of(
                                new MobSpawnSettings.SpawnerData(ModEntities.MUDCRAB.get(), 1, 2)
                        )
                )
        );

        // 食人鱼生成 - 丛林与沼泽群系
        context.register(ADD_PIRANHA_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(ModBiomeTags.EntityGen.PIRANHA_SPAWNS),
                        WeightedList.<MobSpawnSettings.SpawnerData>builder()
                                .add(new MobSpawnSettings.SpawnerData(ModEntities.PIRANHA.get(), 1, 5), 8)
                                .build()
                )
        );
    }

    private static ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                HFASMod.id(name)
        );
    }
}
