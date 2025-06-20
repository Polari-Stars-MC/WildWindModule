package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.experimental.ExtensionMethod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
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
import org.polaris2023.ww_ag.utils.RegUtil;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
@ExtensionMethod({RegUtil.class})
public class ModBlocks {
    public static final BlockEntry<DropExperienceBlock> SALT_ORE;
    public static final BlockEntry<DropExperienceBlock> DEEPSLATE_SALT_ORE;
    public static final BlockEntry<Block> SALT_BLOCK;
    public static final BlockEntry<LiquidBlock> MILK;

    static {
        {
            SALT_ORE = REGISTRATE
                    .defTab(ModTabs.NATURAL_BLOCKS.key())
                    .blockReg("salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p))
                    .ww_ag$zh_cn("盐矿石")
                    .ww_ag$zh_tw("鹽礦石")
                    .ww_ag$zh_hk("鹽礦石")
                    .ww_ag$self()
                    .lang("Salt Ore")
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
                    .tag(WWItemTags.ORES$SALT.get())
                    .build()
                    .register();
        }
        {
            DEEPSLATE_SALT_ORE = REGISTRATE
                    .blockReg("deepslate_salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p))
                    .ww_ag$zh_cn("深层盐矿石")
                    .ww_ag$zh_hk("深層鹽礦石")
                    .ww_ag$zh_tw("深層鹽礦石")
                    .ww_ag$self()
                    .lang("Deepslate Salt Ore")
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
                    .tag(WWItemTags.ORES$SALT.get())
                    .build()
                    .register();
        }
        {

            SALT_BLOCK = REGISTRATE
                    .defTab(ModTabs.BUILDING_BLOCK.key())
                    .blockReg("salt_block", Block::new)
                    .ww_ag$zh_cn("盐块")
                    .ww_ag$zh_tw("鹽塊")
                    .ww_ag$zh_hk("鹽塊")
                    .ww_ag$self()
                    .lang("Salt Block")
                    .properties(properties -> properties
                            .strength(3F)
                            .requiresCorrectToolForDrops()
                            .isRedstoneConductor((_0, _1, _2) -> true))
                    .defaultBlockstate()
                    .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                    .item()
                    .build()
                    .defaultLoot()
                    .register();
        }
        {
            MILK = REGISTRATE
                            .blockReg("milk", properties ->
                                    new LiquidBlock(
                                            (FlowingFluid) NeoForgeMod.MILK.get(),
                                            properties
                                                    .mapColor(MapColor.SNOW)
                                    ))
                    .ww_ag$zh_cn("牛奶")
                    .ww_ag$zh_tw("牛奶")
                    .ww_ag$zh_hk("牛奶")
                    .ww_ag$self()
                    .lang("Milk")
                    .blockstate((c, p) -> {
                        p.simpleBlock(c.getEntry(), p.models().getBuilder("milk")
                                .texture("particle", ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still")));
                    })
                    .tag(WWBlockTags.MILK.get())
                    .register();
        }

    }

    public static void register() {}
}
