package org.polaris2023.wildwind.hfas.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.datagen.provider.*;
import org.polaris2023.wildwind.hfas.datagen.provider.lang.EnUsLanguageProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.lang.ZhCnLanguageProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModBiomeTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModBlockTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModEntityTypeTagsProvider;
import org.polaris2023.wildwind.hfas.datagen.provider.tag.ModItemTagsProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class HFASDatagen {

    @SubscribeEvent
    private static void gatherClient(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        // 语言文件
        event.createProvider(EnUsLanguageProvider::new);
        event.createProvider(ZhCnLanguageProvider::new);
    }

    @SubscribeEvent
    private static void gatherServer(GatherDataEvent.Server event) {
        // 标签
        gatherTagsGen(event);
        // 战利品表
        event.createProvider(ModLootProvider::new);
        // 数据包 ====[注册器看这里]====
        event.createProvider(ModDatapackProvider::new);
        // 数据映射
        event.createProvider(ModDataMapsProvider::new);
        // 配方
        event.createProvider(ModRecipeProvider.Runner::new);
    }

    private static void gatherTagsGen(GatherDataEvent.Server event){
        // 方块标签
        event.createProvider(ModBlockTagsProvider::new);
        // 物品标签
        event.createProvider(ModItemTagsProvider::new);
        // 实体标签
        event.createProvider(ModEntityTypeTagsProvider::new);
        // 群系标签
        event.createProvider(ModBiomeTagsProvider::new);
    }
}
