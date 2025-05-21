package org.polaris2023.ww_ag;

import com.tterrag.registrate.providers.ProviderType;
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

@Mod(WWAgMod.modid)
@EventBusSubscriber(modid = WWAgMod.modid, bus = EventBusSubscriber.Bus.MOD)
public class WWAgMod {
    public static final String modid = "ww_ag";
    public static final WWRegistrate REGISTRATE =
            new WWRegistrate(modid);
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
    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {
        var gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        WWAgDatapackProvider datapack = new WWAgDatapackProvider(output, event.getLookupProvider());
        gen.addProvider(true, datapack);

    }
}
