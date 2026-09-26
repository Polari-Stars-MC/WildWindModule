package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.registry.ModEntities;
import org.polaris2023.wildwind.hfas.registry.ModItems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ZhEnCombineLangProvider {
    private static final Map<Supplier<? extends Item>, String> ITEMS_FOR_ZHCN = new HashMap<>();
    private static final Map<Supplier<? extends Item>, String> ITEMS_FOR_ENUS = new HashMap<>();
    private static final Map<Supplier<? extends Block>, String> BLOCKS_FOR_ZHCN = new HashMap<>();
    private static final Map<Supplier<? extends Block>, String> BLOCKS_FOR_ENUS = new HashMap<>();
    private static final Map<Supplier<? extends EntityType<?>>, String> ENTITIES_FOR_ZHCN = new HashMap<>();
    private static final Map<Supplier<? extends EntityType<?>>, String> ENTITIES_FOR_ENUS = new HashMap<>();
    private static boolean initFlag = false;

    public static void addLang(LanguageProvider provider, String lang){
        if(!initFlag){
            initFlag = true;
            addItems();
            addBlocks();
            addEntities();
        }

        boolean flag = "zh_cn".equals(lang);
        (flag ? ITEMS_FOR_ZHCN : ITEMS_FOR_ENUS).forEach(provider::addItem);
        (flag ? BLOCKS_FOR_ZHCN : BLOCKS_FOR_ENUS).forEach(provider::addBlock);
        (flag ? ENTITIES_FOR_ZHCN : ENTITIES_FOR_ENUS).forEach(provider::addEntityType);
    }


    private static void addItems() {
        addItem(ModItems.MUDCRAB_SPAWN_EGG, "Mudcrab Spawn Egg", "泥沼蟹刷怪蛋");
        addItem(ModItems.MUDCRAB_BUCKET, "Bucket of Mudcrab", "泥沼蟹桶");
        addItem(ModItems.CRAB_CLAW, "Crab Claw", "蟹钳");
        addItem(ModItems.OMNI_CLAW, "Omni Craw", "万用蟹钳");
    }

    private static void addBlocks() {
    }

    private static void addEntities() {
        addEntity(ModEntities.MUDCRAB, "Mudcrab", "泥沼蟹");
    }

    private static void addItem(Supplier<? extends Item> s, String en, String zh){
        ITEMS_FOR_ZHCN.put(s, zh);
        ITEMS_FOR_ENUS.put(s, en);
    }

    private static void addBlock(Supplier<? extends Block> s, String en, String zh){
        BLOCKS_FOR_ZHCN.put(s, zh);
        BLOCKS_FOR_ENUS.put(s, en);
    }

    private static void addEntity(Supplier<? extends EntityType<?>> s, String en, String zh){
        ENTITIES_FOR_ZHCN.put(s, zh);
        ENTITIES_FOR_ENUS.put(s, en);
    }
}
