package org.polaris2023.wildwind.hfas.registry;

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
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.component.OmniClawTools;
import org.polaris2023.wildwind.hfas.item.OmniClawItem;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HFASMod.MOD_ID);

    //泥沼蟹刷怪蛋
    public static final DeferredItem<SpawnEggItem> MUDCRAB_SPAWN_EGG = registerMobEgg("mudcrab_spawn_egg",ModEntities.MUDCRAB);

    //泥沼蟹桶
    public static final DeferredItem<MobBucketItem> MUDCRAB_BUCKET = register("mudcrab_bucket",p -> new MobBucketItem(ModEntities.MUDCRAB.get(),Fluids.WATER,SoundEvents.BUCKET_EMPTY,p));

    //蟹钳
    public static final DeferredItem<Item> CRAB_CLAW = register("crab_claw");


    //万用蟹钳
    public static final DeferredItem<OmniClawItem> OMNI_CLAW =
            register("omni_claw", p -> new OmniClawItem(
                    p.stacksTo(1)
                            .attributes(OmniClawItem.createAttributes())
                            .component(ModDataComponents.OMNI_CLAW_TOOLS.get(), OmniClawTools.EMPTY)
            ));

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
