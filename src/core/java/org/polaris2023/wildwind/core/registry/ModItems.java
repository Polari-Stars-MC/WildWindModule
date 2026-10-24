package org.polaris2023.wildwind.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.CoreMod;

import java.util.function.Function;
import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CoreMod.MOD_ID);
    public static final DeferredItem<Item> DUCKWEED = register("duckweed");
    public static final DeferredItem<Item> CALAMARI = register("calamari");
    public static final DeferredItem<Item> COOKED_CALAMARI = register("cooked_calamari");
    public static final DeferredItem<Item> COOKED_EGG = register("cooked_egg");
    public static final DeferredItem<Item> BAKED_SEEDS = register("baked_seeds");
    public static final DeferredItem<Item> COOKED_FROG_LEGS = register("cooked_frog_legs");
    public static final DeferredItem<Item> FROG_LEGS = register("frog_legs");
    public static final List<DeferredItem<Item>> MATERIALS = List.of(
            DUCKWEED, CALAMARI, COOKED_CALAMARI, COOKED_EGG, BAKED_SEEDS, COOKED_FROG_LEGS, FROG_LEGS);
    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }


    public static DeferredItem<SpawnEggItem> registerMobEgg(String id, Holder<EntityType<?>> type){
        return ITEMS.registerItem(id, p -> new SpawnEggItem(p.spawnEgg(type.value())));
    }

    public static DeferredItem<Item> register(String id){
        return ITEMS.registerItem(id, Item::new);
    }

    public static <T extends Item> DeferredItem<T> register(String id, Function<Item.Properties,T> builder){
        return ITEMS.registerItem(id, builder);
    }

    public static <T extends Item> DeferredItem<T> register(String id, Function<Item.Properties,T> builder, Function<Item.Properties,Item.Properties> property){
        return ITEMS.registerItem(id, p -> builder.apply(property.apply(p)));
    }
}
