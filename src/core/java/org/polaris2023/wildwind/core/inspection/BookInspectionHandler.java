package org.polaris2023.wildwind.core.inspection;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
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
            var block = player.level().getBlockState(event.getPos()).getBlock();
            var name = block.getName().copy().append("(" + BuiltInRegistries.BLOCK.getKey(block) + ")");
            record(player, event.getHand(), name, InspectionReport.block(player.level(), event.getPos()));
        }
    }

    @SubscribeEvent
    public static void entity(PlayerInteractEvent.EntityInteract event) {
        if (!enabled(event)) return;
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        if (event.getEntity() instanceof ServerPlayer player) {
            var target = event.getTarget();
            var name = target.getType().getDescription().copy().append("(" + BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()) + ")");
            record(player, event.getHand(), name, InspectionReport.entity(player.level(), target));
        }
    }

    private static void record(ServerPlayer player, InteractionHand hand, Component name, List<Component> rows) {
        var pages = BookPages.paginate(rows);
        var signed = sign(player.getItemInHand(hand), pages, name, player.getGameProfile().name());
        if (signed.isEmpty()) {
            player.sendOverlayMessage(InspectionText.text("book_full"));
            return;
        }
        player.setItemInHand(hand, signed);
        player.containerMenu.broadcastChanges();
        player.sendOverlayMessage(InspectionText.text("book_signed"));
    }

    /** Prepare a signed copy without mutating the source; an empty result means no room or wrong item. */
    public static ItemStack sign(ItemStack book, List<Component> additions, Component name, String author) {
        if (!book.is(Items.WRITABLE_BOOK)) return ItemStack.EMPTY;
        var old = book.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY);
        if (old.pages().size() + additions.size() > WritableBookContent.MAX_PAGES) return ItemStack.EMPTY;
        if (additions.stream().anyMatch(page -> !WrittenBookContent.CONTENT_CODEC
                .encodeStart(com.mojang.serialization.JsonOps.INSTANCE, page).isSuccess())) return ItemStack.EMPTY;
        var pages = new ArrayList<Filterable<Component>>();
        old.pages().forEach(page -> pages.add(page.map(Component::literal)));
        additions.forEach(page -> pages.add(Filterable.passThrough(page.copy())));
        // Vanilla titles are plain strings limited to 32 characters. Only this fallback title
        // is flattened server-side; the display name and report retain translations and full IDs.
        String title = name.getString();
        if (title.length() > WrittenBookContent.TITLE_MAX_LENGTH) {
            int end = WrittenBookContent.TITLE_MAX_LENGTH;
            if (Character.isHighSurrogate(title.charAt(end - 1))) end--;
            title = title.substring(0, end);
        }
        var signed = book.transmuteCopy(Items.WRITTEN_BOOK);
        signed.remove(DataComponents.WRITABLE_BOOK_CONTENT);
        signed.set(DataComponents.WRITTEN_BOOK_CONTENT,
                new WrittenBookContent(Filterable.passThrough(title), author, 0, List.copyOf(pages), true));
        signed.set(DataComponents.CUSTOM_NAME, name.copy());
        return signed;
    }
}
