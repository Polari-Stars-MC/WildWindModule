package org.polaris2023.ww_deco.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_deco.WWDecoMod;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/21 13:37:19
 */
public class ModEnumExtensions {
    public static final EnumProxy<Boat.Type> AZALEA = boat(WWDecoMod.MODID,"azalea",() -> ModBlocks.AZALEA);
    public static final EnumProxy<Boat.Type> BAOBAB = boat(WWDecoMod.MODID,"baobab",() -> ModBlocks.BAOBAB);


    public static EnumProxy<Boat.Type> boat(String modid,String name, Supplier<PlanksEntry<WWRegistrate, ?>> supplier) {
        return new EnumProxy<>(
                Boat.Type.class,
                (Supplier<Object>) () -> supplier.get().planks.get(),
                modid + ":" + name,
                (Supplier<Object>) () -> supplier.get().boat.get(),
                (Supplier<Object>) () -> supplier.get().chest_boat.get(),
                (Supplier<Object>) () -> Items.STICK,
                false
        );
    }
}
