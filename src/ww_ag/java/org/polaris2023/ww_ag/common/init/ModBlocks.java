package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;
import org.polaris2023.ww_ag.utils.ILanguage;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
public class ModBlocks {
    public static final BlockEntry<DropExperienceBlock> SALT_ORE;
    public static final BlockEntry<DropExperienceBlock> DEEPSLATE_SALT_ORE;
    public static final BlockEntry<Block> SALT_BLOCK;
    public static final BlockEntry<LiquidBlock> MILK;

    static {
        {
            SALT_ORE = b(
                    REGISTRATE
                            .block("salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p)),
                    b -> {
                        b.ww_ag$zh_cn("盐矿石");
                                b.ww_ag$zh_tw("鹽礦石");
                                b.ww_ag$zh_hk("鹽礦石")
                                .lang("Salt ore");
                    }
            )
                    .properties(properties -> properties
                            .mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .defaultBlockstate()
                    .loot((l, b) -> {
                        HolderLookup.RegistryLookup<Enchantment> lookup = l.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                        l.add(b, l.createSilkTouchDispatchTable(b, l.applyExplosionDecay(ModItems.SALT,
                                LootItem.lootTableItem(ModItems.SALT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                        .apply(ApplyBonusCount.addOreBonusCount(lookup.getOrThrow(Enchantments.FORTUNE))))));
                    })
                    .tag(WWBlockTags.ORES$SALT.get())
                    .item()
                    .tab(ModTabs.NATURAL_BLOCKS.key())
                    .tag(WWItemTags.ORES$SALT.get())
                    .build()
                    .register();
        }
        {
            DEEPSLATE_SALT_ORE = b(
                    REGISTRATE
                            .block("deepslate_salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p)),
                    b -> {
                        b.ww_ag$zh_cn("深层盐矿石z");
                                b.ww_ag$zh_hk("深層鹽礦石");

                                b.ww_ag$zh_tw("深層鹽礦石")
                                .lang("Deepslate salt ore");
                    }
            )
                    .properties(p -> p
                            .requiresCorrectToolForDrops()
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5F, 3)
                            .sound(SoundType.DEEPSLATE))
                    .defaultBlockstate()
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .loot((l, b) -> {
                        HolderLookup.RegistryLookup<Enchantment> lookup = l.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                        l.add(b, l.createSilkTouchDispatchTable(b, l.applyExplosionDecay(ModItems.SALT,
                                LootItem.lootTableItem(ModItems.SALT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                        .apply(ApplyBonusCount.addOreBonusCount(lookup.getOrThrow(Enchantments.FORTUNE))))));
                    })
                    .tag(WWBlockTags.ORES$SALT.get())
                    .item()
                    .tab(ModTabs.NATURAL_BLOCKS.key())
                    .tag(WWItemTags.ORES$SALT.get())
                    .build()
                    .register();
        }
        {
            SALT_BLOCK = b(
                    REGISTRATE
                            .block("salt_block", Block::new),
                    b -> {
                        b.ww_ag$zh_cn("盐块");
                        b.ww_ag$zh_tw("鹽塊");
                        b.ww_ag$zh_hk("鹽塊")
                                .lang("Salt block");
                    }
            )
                    .properties(properties -> properties
                            .strength(3F)
                            .requiresCorrectToolForDrops()
                            .isRedstoneConductor((_0, _1, _2) -> true))
                    .defaultBlockstate()
                    .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                    .item()
                    .tab(ModTabs.BUILDING_BLOCK.key())
                    .build()
                    .defaultLoot()
                    .register();
        }
        {
            MILK = REGISTRATE
                    .block("milk", properties ->
                            new LiquidBlock(
                                    (FlowingFluid) NeoForgeMod.MILK.get(),
                                    properties
                                            .mapColor(MapColor.SNOW)
                            ))
                    .blockstate((c, p) -> {
                        p.simpleBlock(c.getEntry(), p.models().getBuilder("milk")
                                .texture("particle", ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still")));
                    })
                    .tag(WWBlockTags.MILK.get())
                    .register();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> BlockBuilder<T, L2Registrate> b(BlockBuilder<T, L2Registrate> builder,
                                                                    NonNullConsumer<ILanguage<Block, T, L2Registrate, BlockBuilder<T, L2Registrate>>> b) {
        b.accept((ILanguage<Block, T, L2Registrate, BlockBuilder<T, L2Registrate>>) builder);
        return builder;
    }

    public static void register() {}
}
