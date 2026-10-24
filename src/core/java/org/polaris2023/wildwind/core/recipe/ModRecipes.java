package org.polaris2023.wildwind.core.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.core.CoreMod;

public final class ModRecipes {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CoreMod.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WoolToStringRecipe>> WOOL_TO_STRING =
            SERIALIZERS.register("wool_to_string", () -> new RecipeSerializer<>(
                    MapCodec.unit(WoolToStringRecipe.INSTANCE), StreamCodec.unit(WoolToStringRecipe.INSTANCE)));

    private ModRecipes() {}
    public static void register(IEventBus bus) { SERIALIZERS.register(bus); }
}
