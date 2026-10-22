package org.polaris2023.wildwind.hfas.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.polaris2023.wildwind.hfas.menu.KilnMenu;

/** Uses Minecraft's furnace artwork while keeping the kiln's server-side input rules. */
public class KilnScreen extends AbstractContainerScreen<KilnMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final Identifier FIRE = Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier PROGRESS = Identifier.withDefaultNamespace("container/furnace/burn_progress");

    public KilnScreen(KilnMenu menu, Inventory inventory, Component title) { super(menu, inventory, title); }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        if (menu.isLit()) {
            int height = Mth.ceil(menu.getLitProgress() * 13) + 1;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FIRE, 14, 14, 0, 14 - height,
                    leftPos + 56, topPos + 50 - height, 14, height);
        }
        int width = Mth.ceil(menu.getBurnProgress() * 24);
        if (width > 0) graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS, 24, 16, 0, 0, leftPos + 79, topPos + 34, width, 16);
    }
}
