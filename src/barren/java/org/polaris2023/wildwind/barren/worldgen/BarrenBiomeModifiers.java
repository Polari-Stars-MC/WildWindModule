package org.polaris2023.wildwind.barren.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wildwind.barren.WildWindBarren;

/**
 * 瘠土更新的生物群系修改器：下界废土 / 灵魂沙峡谷的焚烬木与灵焰木，以及灵魂沙峡谷下的灵魂砂岩层。
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public final class BarrenBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_CINDER_TREE = createKey("add_cinder_tree");
    public static final ResourceKey<BiomeModifier> ADD_EMBER_TREE = createKey("add_ember_tree");
    public static final ResourceKey<BiomeModifier> ADD_SOUL_SANDSTONE = createKey("add_soul_sandstone");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_CINDER_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.NETHER_WASTES)),
                HolderSet.direct(placedFeatures.getOrThrow(BarrenPlacedFeatures.CINDER_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_EMBER_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
                HolderSet.direct(placedFeatures.getOrThrow(BarrenPlacedFeatures.EMBER_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        // 顶层修改阶段在所有装饰之后执行，此时地表与洞穴已经生成完毕，可以整块找灵魂沙层
        context.register(ADD_SOUL_SANDSTONE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
                HolderSet.direct(placedFeatures.getOrThrow(BarrenPlacedFeatures.SOUL_SANDSTONE_PLACED_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));
    }

    private static ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WildWindBarren.id(name));
    }

    private BarrenBiomeModifiers() {
    }
}
