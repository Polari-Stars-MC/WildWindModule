package org.polaris2023.wildwind.core.mixin;

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
import org.polaris2023.wildwind.core.client.AzaleaSignState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
        ((AzaleaSignState) state).wildwind$setAzalea(block == org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get()
                || block == org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN.get() || block == org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get()
                || block == org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN.get());
    }

    @Inject(method = "submitSignText", at = @At("HEAD"), cancellable = true)
    private void wildwind$text(SignRenderState state, PoseStack pose, SubmitNodeCollector collector, SignText text, CallbackInfo ci) {
        if (!((AzaleaSignState) state).wildwind$isAzalea()) return;
        int color = text.getColor().getTextColor();
        int dark = ((color >> 16 & 255) * 2 / 5 << 16) | ((color >> 8 & 255) * 2 / 5 << 8) | (color & 255) * 2 / 5;
        int outline = text.hasGlowingText() ? 0xFF000000 | (text.getColor() == DyeColor.BLACK ? 0xF0EBCC : dark) : 0;
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

}
