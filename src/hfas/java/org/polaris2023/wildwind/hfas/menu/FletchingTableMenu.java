package org.polaris2023.wildwind.hfas.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.polaris2023.wildwind.hfas.block.ModBlocks;

/**
 * 制箭台菜单
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class FletchingTableMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public FletchingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), ContainerLevelAccess.NULL);
    }

    public FletchingTableMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(ModMenus.FLETCHING_TABLE.get(), containerId);
        this.container = container;
        this.access = access;

        // 添加容器的槽位
        for (int i = 0; i < 3; i++) {
            this.addSlot(new Slot(container, i, 62 + i * 18, 18));
        }

        // 添加玩家物品栏
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 50 + row * 18));
            }
        }

        // 添加玩家快捷栏
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 108));
        }
    }



    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            if (index < 3) {
                if (!this.moveItemStackTo(slotItem, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.FLETCHING_TABLE.get());
    }
}
