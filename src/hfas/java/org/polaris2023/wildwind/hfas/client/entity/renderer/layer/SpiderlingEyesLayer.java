package org.polaris2023.wildwind.hfas.client.entity.renderer.layer;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import net.minecraft.resources.Identifier;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.client.entity.renderstate.SpiderlingRenderState;
import org.polaris2023.wildwind.hfas.entity.monster.Spiderling;

/**
 * 幼蛛发光眼睛图层喵~
 */
public class SpiderlingEyesLayer extends AutoGlowingGeoLayer<Spiderling, Void, SpiderlingRenderState> {
	private static final Identifier EYES = HFASMod.id("textures/entity/spiderling_eyes.png");

	public SpiderlingEyesLayer(GeoRenderer<Spiderling, Void, SpiderlingRenderState> renderer) {
		super(renderer);
	}

	@Override
	protected Identifier getTextureResource(SpiderlingRenderState renderState) {
		return EYES;
	}
}
