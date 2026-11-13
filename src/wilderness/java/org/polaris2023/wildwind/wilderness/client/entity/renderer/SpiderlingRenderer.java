package org.polaris2023.wildwind.wilderness.client.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.polaris2023.wildwind.wilderness.client.entity.model.SpiderlingModel;
import org.polaris2023.wildwind.wilderness.client.entity.renderer.layer.SpiderlingEyesLayer;
import org.polaris2023.wildwind.wilderness.client.entity.renderstate.SpiderlingRenderState;
import org.polaris2023.wildwind.wilderness.entity.monster.Spiderling;

import javax.annotation.Nullable;

/**
 * 幼蛛实体渲染器喵~
 *
 * @author safe_saffawa
 */
public class SpiderlingRenderer extends GeoEntityRenderer<Spiderling, SpiderlingRenderState> {

    public SpiderlingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpiderlingModel());
        this.withRenderLayer(SpiderlingEyesLayer::new);
    }

    @Override
    public SpiderlingRenderState createRenderState(Spiderling animatable, @Nullable Void relatedObject) {
        return new SpiderlingRenderState();
    }
}
