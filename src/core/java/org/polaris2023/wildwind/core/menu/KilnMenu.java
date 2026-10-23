package org.polaris2023.wildwind.core.menu;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.polaris2023.wildwind.core.recipe.KilnRecipes;

public class KilnMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final Level level;

    public KilnMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public KilnMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ModMenus.KILN.get(), id);
        checkContainerSize(container, 3);
        checkContainerDataCount(data, 4);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        addSlot(new Slot(container, 0, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // Recipe lookup is server-authoritative; clients don't receive the full recipe manager.
                return level.isClientSide() || KilnRecipes.find(level, stack).isPresent();
            }
        });
        addSlot(new Slot(container, 1, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) { return isFuel(stack) || stack.is(Items.BUCKET); }
            @Override
            public int getMaxStackSize(ItemStack stack) { return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack); }
        });
        addSlot(new FurnaceResultSlot(inventory.player, container, 2, 116, 35));
        addStandardInventorySlots(inventory, 8, 84);
        addDataSlots(data);
    }

    private boolean isFuel(ItemStack stack) { return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues()) > 0; }

    @Override
    public boolean stillValid(Player player) { return container.stillValid(player); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), original = stack.copy();
        if (index == 2) {
            if (!moveItemStackTo(stack, 3, 39, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, original);
        } else if (index < 3) {
            if (!moveItemStackTo(stack, 3, 39, false)) return ItemStack.EMPTY;
        } else if (!level.isClientSide() && KilnRecipes.find(level, stack).isPresent()) {
            if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        } else if (isFuel(stack)) {
            if (!moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
        } else if (index < 30) {
            if (!moveItemStackTo(stack, 30, 39, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 3, 30, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    public boolean isLit() { return data.get(0) > 0; }
    public float getLitProgress() { return Mth.clamp((float) data.get(0) / Math.max(1, data.get(1)), 0, 1); }
    public float getBurnProgress() { return Mth.clamp((float) data.get(2) / Math.max(1, data.get(3)), 0, 1); }
}
