package org.polaris2023.ww_ag;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.common.init.*;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.datagen.worldgen.WWBiomeModifyProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWConfiguredFeatureProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWPlaceFeatureProvider;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("CodeBlock2Expr")
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
        NeoForgeMod.enableMilkFluid();
        ModTabs.register();
        ModFluids.register();
        ModBlocks.register();
        ModItems.register();
        ModSounds.register();
        ModPotions.register();
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
        ResourceKey<LootTable> DROP_BAT_WING = ResourceKey.create(Registries.LOOT_TABLE, REGISTRATE.loc("entities/misc/drop_bat_wing"));
        ResourceKey<LootTable> DROP_CALAMARI = ResourceKey.create(Registries.LOOT_TABLE, REGISTRATE.loc("entities/misc/drop_calamari"));
        ResourceKey<LootTable> DROP_GLOWING_CALAMARI = ResourceKey.create(Registries.LOOT_TABLE, REGISTRATE.loc("entities/misc/drop_glowing_calamari"));

        REGISTRATE.addDataGenerator(WWProviderType.GLM, p -> {
            p.add("bats_drops_wing", new AddTableLootModifier(
                    new LootItemCondition[]{
                            p.anyOf(
                                    EntityType.BAT.getDefaultLootTable()
                            )
                    }, DROP_BAT_WING
            ));
            p.add("bats_drops_glowing_calamari", new AddTableLootModifier(
                    new LootItemCondition[]{
                            p.anyOf(
                                    EntityType.GLOW_SQUID.getDefaultLootTable()
                            )
                    }, DROP_GLOWING_CALAMARI
            ));
            p.add("bats_drops_calamari", new AddTableLootModifier(
                    new LootItemCondition[]{
                            p.anyOf(
                                    EntityType.SQUID.getDefaultLootTable()
                            )
                    }, DROP_CALAMARI
            ));

        });
        REGISTRATE.addDataGenerator(ProviderType.LOOT, p -> {
            p.addLootAction(WWProviderType.BASE, base -> {
                {
                    base.add(DROP_BAT_WING, LootTable
                            .lootTable()
                            .withPool(LootPool
                                    .lootPool()
                                    .setRolls(ConstantValue.exactly(1F))
                                    .add(LootItem.lootTableItem(ModItems.BAT_WING))
                                    .apply(SmeltItemFunction.smelted().when(base.shouldSmeltLoot()))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0F, 2F)))
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(base.registry, UniformGenerator.between(0F, 2F)))
                            ));
                }//drops/bat wing
                {
                    base.add(DROP_CALAMARI, LootTable
                            .lootTable()
                            .withPool(LootPool
                                    .lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(ModItems.CALAMARI))
                                    .apply(SmeltItemFunction.smelted().when(base.shouldSmeltLoot()))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 4.0F)))
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(base.registry, UniformGenerator.between(0.0F, 1.0F)
                                    ))
                            ));
                    base.add(DROP_GLOWING_CALAMARI, LootTable
                            .lootTable()
                            .withPool(LootPool
                                    .lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(ModItems.GLOWING_CALAMARI))
                                    .apply(SmeltItemFunction.smelted().when(base.shouldSmeltLoot()))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 4.0F)))
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(base.registry, UniformGenerator.between(0.0F, 1.0F)
                                    ))
                            ));
                }//drops/(glowing/)calamari
            });

        });
        REGISTRATE.addDataGenerator(WWProviderType.REPEATER, p -> {
            p.add(Registries.CONFIGURED_FEATURE, WWConfiguredFeatureProvider::bootstrap);
            p.add(Registries.PLACED_FEATURE, WWPlaceFeatureProvider::bootstrap);
            p.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WWBiomeModifyProvider::bootstrap);
        });
    }

    @SubscribeEvent
    public static void gatherEvent(GatherDataEvent event) {


    }
    @SubscribeEvent
    public static void tab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ModTabs.INGREDIENTS.key())) {
            event.accept(PotionContents.createItemStack(Items.LINGERING_POTION, ModPotions.MILK));
        }
    }

}
