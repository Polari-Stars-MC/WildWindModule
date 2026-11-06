package org.polaris2023.wildwind.hfas.client.entity.renderstate;

import net.minecraft.core.Direction;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.polaris2023.wildwind.hfas.entity.animal.MudcrabVariant;
/**
 * @author Mon-Landis
 */

public class MudcrabRenderState extends LivingEntityRenderState {
    public MudcrabVariant variant;
    public float modelYRotOffset;
    public float modelXRotOffset;
    public Direction motionDirection = Direction.NORTH;
}