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
import org.polaris2023.wildwind.hfas.block.entity.FletchingTableBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

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
        this(containerId, playerInventory, new SimpleContainer(4), ContainerLevelAccess.NULL);
    }

    public FletchingTableMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(ModMenus.FLETCHING_TABLE.get(), containerId);
        this.container = container;
        this.access = access;

        // 箭羽槽 (slot 0)
        this.addSlot(FletchingTableBlockEntity.Slots.SLOT_FLETCHING.toSlot(container, 26, 18));
        // 箭杆槽 (slot 1)
        this.addSlot(FletchingTableBlockEntity.Slots.SLOT_SHAFT.toSlot(container, 62, 18));
        // 箭头槽 (slot 2)
        this.addSlot( FletchingTableBlockEntity.Slots.SLOT_HEAD.toSlot(container, 98, 18));
        // 燃料槽口(slot 3)
        this.addSlot(FletchingTableBlockEntity.Slots.SLOT_FUEL.toSlot(container, 62, 68));
        // 输出槽 (slot 4)
        this.addSlot(this.addSlot(FletchingTableBlockEntity.Slots.SLOT_RESULT.toSlot(container, 134, 18)));

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

    /**
     * 输出槽 - 不能放入物品，取出时消耗材料
     */
    private class ResultSlot extends Slot {
        public ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            // 消耗输入材料
//            container.removeItem(FletchingTableBlockEntity.SLOT_FLETCHING, 1);
//            container.removeItem(FletchingTableBlockEntity.SLOT_SHAFT, 1);
//            container.removeItem(FletchingTableBlockEntity.SLOT_HEAD, 1);
            super.onTake(player, stack);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            // 输出槽 (index 3)
            if (index == 3) {
                if (!this.moveItemStackTo(slotItem, 4, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(player, slotItem);
            }
            // 输入槽 (index 0-2)
            else if (index >= 0 && index < 3) {
                if (!this.moveItemStackTo(slotItem, 4, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // 玩家物品栏
            else {
                // 尝试放入输入槽
                if (!this.moveItemStackTo(slotItem, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
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
