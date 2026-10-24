package org.polaris2023.wildwind.hfas.test;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import org.polaris2023.wildwind.core.enchantment.MeltingEnchantment;

public final class EnchantmentTableGameTests {
    private static void check(GameTestHelper h, boolean ok, String message) {
        h.assertTrue(ok, Component.literal(message));
    }

    static void melting(GameTestHelper h) {
        var level = h.getLevel();
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var melting = registry.getOrThrow(MeltingEnchantment.MELTING);
        var tablePool = registry.getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE);
        check(h, tablePool.contains(melting), "Melting belongs to the real enchanting-table pool");
        for (var item : List.of(Items.DIAMOND_CHESTPLATE, Items.DIAMOND_SWORD)) {
            check(h, EnchantmentHelper.getAvailableEnchantmentResults(30, new ItemStack(item), tablePool.stream())
                    .stream().noneMatch(e -> e.enchantment().equals(melting)), "Melting is not offered for " + item);
        }

        var player = h.makeMockServerPlayerInLevel();
        var pos = h.absolutePos(new BlockPos(3, 2, 3));
        level.setBlockAndUpdate(pos, Blocks.ENCHANTING_TABLE.defaultBlockState());
        for (var offset : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
            level.setBlockAndUpdate(pos.offset(offset), Blocks.BOOKSHELF.defaultBlockState());
        }
        int meltingId = registry.asHolderIdMap().getId(melting);
        for (var item : List.of(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.BOOK)) {
            boolean obtained = false;
            var menu = new EnchantmentMenu(1, player.getInventory(), ContainerLevelAccess.create(level, pos));
            menu.getSlot(1).set(new ItemStack(Items.LAPIS_LAZULI, 64));
            // Data slot 3 is vanilla's synchronized enchantment seed. Fixed seeds make the
            // test reproducible while exercising real offers and the menu's apply path.
            for (int seed = 0; seed < 2048 && !obtained; seed++) {
                menu.setData(3, seed);
                menu.getSlot(0).set(new ItemStack(item));
                for (int slot = 0; slot < 3; slot++) {
                    if (menu.enchantClue[slot] != meltingId) continue;
                    check(h, menu.clickMenuButton(player, slot), "Enchanting-table offer can be selected");
                    var result = menu.getSlot(0).getItem();
                    var enchantments = result.get(item == Items.BOOK ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS);
                    check(h, enchantments != null && enchantments.getLevel(melting) == 1, "Table actually applies Melting I");
                    check(h, item != Items.BOOK || result.is(Items.ENCHANTED_BOOK), "Table transforms a normal book");
                    obtained = true;
                    break;
                }
            }
            check(h, obtained, "Melting can be randomly obtained from the table on " + item);
        }
        h.succeed();
    }
}
