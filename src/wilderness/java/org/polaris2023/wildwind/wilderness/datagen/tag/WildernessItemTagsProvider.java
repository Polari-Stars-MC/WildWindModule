package org.polaris2023.wildwind.wilderness.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.registry.ModItems;
import org.polaris2023.wildwind.wilderness.tag.ModItemTags;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 荒野更新的物品标签生成器。
 *
 * @author safe_saffawa
 */
public class WildernessItemTagsProvider extends ItemTagsProvider {

    public WildernessItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WildWindWilderness.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.ANIMAL_FOODS).addTag(ModItemTags.MUDCRAB_FOOD);
        addToTag(ModItemTags.MUDCRAB_FOOD, Items.SPIDER_EYE);

        addToTag(ItemTags.CAT_FOOD, ModItems.PIRANHA.get());
        addToTag(ItemTags.OCELOT_FOOD, ModItems.PIRANHA.get());
        addToTag(ItemTags.WOLF_FOOD, ModItems.PIRANHA.get(), ModItems.COOKED_PIRANHA.get());
        addToTag(ItemTags.FISHES, ModItems.PIRANHA.get(), ModItems.COOKED_PIRANHA.get());
        addToTag(Tags.Items.FOODS_COOKED_FISH, ModItems.COOKED_PIRANHA.get());
    }

    public void addToTag(TagKey<Item> tag, Item... items) {
        if (items.length == 0) return;
        tag(tag).addAll(Arrays.stream(items).map(WildernessItemTagsProvider::itemKey));
    }

    private static ResourceKey<Item> itemKey(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item)
                .orElseThrow(() -> new IllegalStateException("未注册的物品: " + item));
    }
}
