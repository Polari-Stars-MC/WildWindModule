package org.polaris2023.ww_ag;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
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
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.polaris2023.ww_ag.common.init.*;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.datagen.worldgen.WWBiomeModifyProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWConfiguredFeatureProvider;
import org.polaris2023.ww_ag.datagen.worldgen.WWPlaceFeatureProvider;

@SuppressWarnings("CodeBlock2Expr")
@Mod(WWAgMod.MODID)
@EventBusSubscriber(modid = WWAgMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WWAgMod {
    public static final String MODID = "ww_ag";
    public static final WWRegistrate REGISTRATE =
            new WWRegistrate(MODID);
    public static final Reg REG = new Reg(MODID);
    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(MODID, 1);
    public static ResourceLocation cLoc(String path) { return ResourceLocation.fromNamespaceAndPath("c", path); }
    public static ResourceLocation loc(String path) { return REGISTRATE.loc(path); }
    public WWAgMod() {

        NeoForgeMod.enableMilkFluid();
        ModDataComponents.register();
        ModConfigs.register();
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
        REGISTRATE.addDataGenerator(WWProviderType.ZH_CN, p -> {
            p.add(MODID + ".title", "原野之风：农业");
        });
        REGISTRATE.addDataGenerator(WWProviderType.ZH_TW, p -> {
            p.add(MODID + ".title", "風靈荒野：農業");
        });
        REGISTRATE.addDataGenerator(WWProviderType.ZH_HK, p -> {
            p.add(MODID + ".title", "風與草之詩：農業");
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
    public static void componentApply(ModifyDefaultComponentsEvent event) {
        event.modify(Items.MILK_BUCKET, builder ->
                builder.set(ModDataComponents.MILK_TYPE.get(), ModBlocks.MILK.getId()));
    }

}
