package org.polaris2023.wildwind.hfas.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.client.AzaleaSignState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public abstract class AzaleaSignRendererMixin {
    @Shadow @Final private Font font;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", at = @At("RETURN"))
    private void wildwind$mark(SignBlockEntity entity, SignRenderState state, float partialTick, Vec3 camera,
                               ModelFeatureRenderer.@Nullable CrumblingOverlay overlay, CallbackInfo ci) {
        var block = entity.getBlockState().getBlock();
        ((AzaleaSignState) state).wildwind$setAzalea(block == ModBlocks.AZALEA_SIGN.get()
                || block == ModBlocks.AZALEA_WALL_SIGN.get() || block == ModBlocks.AZALEA_HANGING_SIGN.get()
                || block == ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
    }

    @Inject(method = "submitSignText", at = @At("HEAD"), cancellable = true)
    private void wildwind$text(SignRenderState state, PoseStack pose, SubmitNodeCollector collector, SignText text, CallbackInfo ci) {
        if (!((AzaleaSignState) state).wildwind$isAzalea()) return;
        int color = wildwind$color(text.getColor());
        int dark = ((color >> 16 & 255) * 2 / 5 << 16) | ((color >> 8 & 255) * 2 / 5 << 8) | (color & 255) * 2 / 5;
        int outline = text.hasGlowingText() ? 0xFF000000 | (color == 0 ? 0xF0EBCC : dark) : 0;
        var lines = text.getRenderMessages(state.isTextFilteringEnabled, input -> {
            var split = font.split(input, state.maxTextLineWidth);
            return split.isEmpty() ? FormattedCharSequence.EMPTY : split.getFirst();
        });
        for (int i = 0; i < 4; i++) {
            collector.submitText(pose, -font.width(lines[i]) / 2.0F, i * state.textLineHeight - 2 * state.textLineHeight,
                    lines[i], false, Font.DisplayMode.POLYGON_OFFSET, 15728880,
                    0xFF000000 | (text.hasGlowingText() ? color : dark), 0, outline);
        }
        ci.cancel();
    }

    @Unique private static int wildwind$color(DyeColor color) {
        return switch (color) {
            case BLACK -> 0x000000;
            case RED -> 0xFF0000;
            case GREEN -> 0x00FF00;
            case BROWN -> 0x8B4513;
            case BLUE -> 0x0000FF;
            case PURPLE -> 0xA020F0;
            case CYAN -> 0x00FFFF;
            case LIGHT_GRAY -> 0xD3D3D3;
            case GRAY -> 0x808080;
            case PINK -> 0xFF69B4;
            case LIME -> 0xBFFF00;
            case YELLOW -> 0xFFFF00;
            case LIGHT_BLUE -> 0x9AC0CD;
            case MAGENTA -> 0xFF00FF;
            case ORANGE -> 0xFF681F;
            case WHITE -> 0xFFFFFF;
        };
    }
}
