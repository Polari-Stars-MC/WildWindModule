package org.polaris2023.wildwind.core.mixin;

import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import org.polaris2023.wildwind.core.client.AzaleaSignState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignRenderState.class)
public abstract class SignRenderStateMixin implements AzaleaSignState {
    @Unique private boolean wildwind$azalea;
    public boolean wildwind$isAzalea() { return wildwind$azalea; }
    public void wildwind$setAzalea(boolean azalea) { wildwind$azalea = azalea; }
}
