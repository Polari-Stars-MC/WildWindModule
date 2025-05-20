package org.polaris2023.ww_ag.common.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.polaris2023.ww_ag.WWAgMod;

import static org.polaris2023.ww_ag.WWAgMod.REGISTRATE;

/**
 * @author baka4n
 * {@code @Date 2025/05/18 14:58:14}
 */
public class ModBlocks {
    public static final BlockEntry<DropExperienceBlock> SALT_ORE;
    public static final BlockEntry<DropExperienceBlock> DEEPSLATE_SALT_ORE;
    public static final BlockEntry<Block> SALT_BLOCK;

    static {
        SALT_ORE = REGISTRATE
                .block("salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p))
                .properties(properties -> properties
                        .mapColor(MapColor.STONE)
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .requiresCorrectToolForDrops()
                        .strength(3))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .blockstate((c, p) ->
                        p.simpleBlock(c.get()))
                .loot((l, b) -> {
                    HolderLookup.RegistryLookup<Enchantment> lookup = l.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                    l.add(b, l.createSilkTouchDispatchTable(b, l.applyExplosionDecay(ModItems.SALT,
                            LootItem.lootTableItem(ModItems.SALT)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                    .apply(ApplyBonusCount.addOreBonusCount(lookup.getOrThrow(Enchantments.FORTUNE))))));
                })
                .item().build()
                .lang("Salt ore")
                .register();
        DEEPSLATE_SALT_ORE = REGISTRATE
                .block("deepslate_salt_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p))
                .properties(p -> p
                        .requiresCorrectToolForDrops()
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .mapColor(MapColor.DEEPSLATE)
                        .strength(4.5F, 3)
                        .sound(SoundType.DEEPSLATE))
                .blockstate((c, p) ->
                        p.simpleBlock(c.get()))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .loot((l, b) -> {
                    HolderLookup.RegistryLookup<Enchantment> lookup = l.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                    l.add(b, l.createSilkTouchDispatchTable(b, l.applyExplosionDecay(ModItems.SALT,
                            LootItem.lootTableItem(ModItems.SALT)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                    .apply(ApplyBonusCount.addOreBonusCount(lookup.getOrThrow(Enchantments.FORTUNE))))));
                })
                .item().build()
                .lang("Deepslate salt ore")
                .register();
        SALT_BLOCK = REGISTRATE
                .block("salt_block", Block::new)
                .properties(properties -> properties
                        .strength(3F)
                        .requiresCorrectToolForDrops()
                        .isRedstoneConductor((_0, _1, _2) -> true))
                .blockstate((c, p) ->
                        p.simpleBlock(c.get()))
                .item().build()
                .defaultLoot()
                .lang("Salt block")

                .register();
    }

    public static void register() {}
}
