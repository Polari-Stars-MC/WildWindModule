package org.polaris2023.wildwind.core.inspection;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.polaris2023.wildwind.core.CoreMod;

@EventBusSubscriber(modid = CoreMod.MOD_ID)
public final class BookInspectionHandler {
    private BookInspectionHandler() {}

    private static boolean enabled(PlayerInteractEvent event) {
        return event.getEntity().isShiftKeyDown() && !event.getEntity().isSpectator() && event.getItemStack().is(Items.WRITABLE_BOOK);
    }

    @SubscribeEvent
    public static void block(PlayerInteractEvent.RightClickBlock event) {
        if (!enabled(event)) return;
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        if (event.getEntity() instanceof ServerPlayer player) {
            record(player, event.getItemStack(), InspectionReport.block(player.level(), event.getPos()));
        }
    }

    @SubscribeEvent
    public static void entity(PlayerInteractEvent.EntityInteract event) {
        if (!enabled(event)) return;
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        if (event.getEntity() instanceof ServerPlayer player) {
            record(player, event.getItemStack(), InspectionReport.entity(player.level(), event.getTarget()));
        }
    }

    private static void record(ServerPlayer player, ItemStack book, List<String> rows) {
        var pages = BookPages.paginate(rows);
        if (!append(book, pages)) {
            player.sendOverlayMessage(Component.literal("书的剩余页数不足，未写入。请换一本书与笔。"));
            return;
        }
        player.containerMenu.broadcastChanges();
        player.sendOverlayMessage(Component.literal("已追加 " + pages.size() + " 页属性记录；松开潜行后右键阅读。"));
    }

    /** All-or-nothing append, preserving existing raw/filtered pages and every other component. */
    public static boolean append(ItemStack book, List<String> additions) {
        if (!book.is(Items.WRITABLE_BOOK)) return false;
        var old = book.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
        if (old.pages().size() + additions.size() > WritableBookContent.MAX_PAGES
                || additions.stream().anyMatch(page -> page.length() > WritableBookContent.PAGE_EDIT_LENGTH)) return false;
        var pages = new ArrayList<>(old.pages());
        additions.forEach(page -> pages.add(Filterable.passThrough(page)));
        book.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(pages));
        return true;
    }
}
