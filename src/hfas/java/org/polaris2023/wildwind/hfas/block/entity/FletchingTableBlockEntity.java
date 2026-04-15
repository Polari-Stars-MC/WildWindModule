package org.polaris2023.wildwind.hfas.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 制箭台方块实体
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class FletchingTableBlockEntity extends BlockEntity implements Container {
    public static int CONTAINER_SIZE = 0;
    public enum Slots {
        SLOT_FLETCHING, // 箭羽槽
        SLOT_SHAFT,     // 箭羽槽
        SLOT_HEAD,      // 箭头槽
        SLOT_FUEL,      // 消耗槽
        SLOT_RESULT     // 输出槽
        ;

        public final int slot;

        Slots() {
            slot = CONTAINER_SIZE;
            CONTAINER_SIZE++;
        }

        public Slot toSlot(Container c, int x, int z) {
            return new Slot(c, slot, x, z);
        }

        public ItemStack remove(Container c, int size) {
            return c.removeItem(slot, size);
        }
    }

    private final NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);


    public FletchingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLETCHING_TABLE.get(), pos, state);

    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < items.size() ? items.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < items.size()) {
            items.set(slot, stack);
            if (stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null) {
            return false;
        }
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
    }
}
