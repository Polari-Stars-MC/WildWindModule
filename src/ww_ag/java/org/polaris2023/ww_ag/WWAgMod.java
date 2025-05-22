package org.polaris2023.ww_ag;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.polaris2023.ww_ag.common.init.ModBlocks;
import org.polaris2023.ww_ag.common.init.ModItems;
import org.polaris2023.ww_ag.common.init.ModSounds;
import org.polaris2023.ww_ag.common.init.ModTabs;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.datagen.WWAgDatapackProvider;

import java.util.Map;
import java.util.Objects;

@Mod(WWAgMod.MODID)
@EventBusSubscriber(modid = WWAgMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWAgMod {
    public static final String MODID = "ww_ag";
    public static final WWRegistrate REGISTRATE =
            new WWRegistrate(MODID);
    public static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
    public WWAgMod() {
        ModTabs.register();
        ModBlocks.register();
        ModItems.register();
        ModSounds.register();
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, blockIntrinsic -> {
            IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block> fungus = blockIntrinsic.addTag(WWBlockTags.FUNGUS.get());
            fungus.add(Blocks.CRIMSON_FUNGUS);
            fungus.add(Blocks.WARPED_FUNGUS);
        });
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, itemIntrinsic -> {
            itemIntrinsic.copy(WWBlockTags.FUNGUS.get(), WWItemTags.FUNGUS.get());
        });
        REGISTRATE.addDataGenerator(WWProviderType.ZH_CN, provider -> {
            Map<String,String> tabTranslate = Map.of(
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.BUILDING_BLOCK.getKey()).location()),
                    "原野之风：建筑方块",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.NATURAL_BLOCKS.getKey()).location()),
                    "原野之风：自然方块",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.INGREDIENTS.getKey()).location()),
                    "原野之风：材料",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.FOOD_AND_DRINK.getKey()).location()),
                    "原野之风：食物与饮品",
                    REGISTRATE.getModid() + ".title",
                    "原野之风：农业"
            );
            for (Map.Entry<String, String> entry : tabTranslate.entrySet()) {
                provider.add(entry.getKey(), entry.getValue());
            }
        });
        REGISTRATE.addDataGenerator(WWProviderType.ZH_TW, provider -> {
            Map<String,String> tabTranslate = Map.of(
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.BUILDING_BLOCK.getKey()).location()),
                    "風靈荒野：建築方塊",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.NATURAL_BLOCKS.getKey()).location()),
                    "風靈荒野：自然方塊",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.INGREDIENTS.getKey()).location()),
                    "風靈荒野：材料",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.FOOD_AND_DRINK.getKey()).location()),
                    "風靈荒野：食物與飲料",
                    REGISTRATE.getModid() + ".title",
                    "風靈荒野：農業"
            );
            for (Map.Entry<String, String> entry : tabTranslate.entrySet()) {
                provider.add(entry.getKey(), entry.getValue());
            }
        });
        REGISTRATE.addDataGenerator(WWProviderType.ZH_HK, provider -> {
            Map<String,String> tabTranslate = Map.of(
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.BUILDING_BLOCK.getKey()).location()),
                    "風與草之詩：建築積木",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.NATURAL_BLOCKS.getKey()).location()),
                    "風與草之詩：自然積木",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.INGREDIENTS.getKey()).location()),
                    "風與草之詩：材料",
                    Util.makeDescriptionId("itemGroup", Objects.requireNonNull(ModTabs.FOOD_AND_DRINK.getKey()).location()),
                    "風與草之詩：食物同飲品",
                    REGISTRATE.getModid() + ".title",
                    "風與草之詩：農業"
            );
            for (Map.Entry<String, String> entry : tabTranslate.entrySet()) {
                provider.add(entry.getKey(), entry.getValue());
            }
        });
    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {
        var gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        WWAgDatapackProvider datapack = new WWAgDatapackProvider(output, event.getLookupProvider());
        gen.addProvider(true, datapack);

    }
}
