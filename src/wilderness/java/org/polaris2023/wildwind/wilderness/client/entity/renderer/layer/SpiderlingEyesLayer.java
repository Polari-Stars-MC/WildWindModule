package org.polaris2023.wildwind.wilderness.client.entity.renderer.layer;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import net.minecraft.resources.Identifier;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.SpiderlingRenderState;
import org.polaris2023.wildwind.wilderness.entity.monster.Spiderling;

/**
 * 幼蛛发光眼睛图层喵~
 *
 * @author safe_saffawa
 */
public class SpiderlingEyesLayer extends AutoGlowingGeoLayer<Spiderling, Void, SpiderlingRenderState> {
    private static final Identifier EYES = WildWindWilderness.id("textures/entity/spiderling_eyes.png");

    public SpiderlingEyesLayer(GeoRenderer<Spiderling, Void, SpiderlingRenderState> renderer) {
        super(renderer);
    }

    @Override
    protected Identifier getTextureResource(SpiderlingRenderState renderState) {
        return EYES;
    }
}
