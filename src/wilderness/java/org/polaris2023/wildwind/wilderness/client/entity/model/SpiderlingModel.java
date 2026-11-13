package org.polaris2023.wildwind.wilderness.client.entity.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.entity.monster.Spiderling;

/**
 * 幼蛛实体的几何模型定义喵~
 *
 * @author safe_saffawa
 */
public class SpiderlingModel extends DefaultedEntityGeoModel<Spiderling> {

    public SpiderlingModel() {
        super(WildWindWilderness.id("spiderling"));
    }

    @Override
    public Identifier getTextureResource(GeoRenderState state) {
        return WildWindWilderness.id("textures/entity/spiderling.png");
    }
}
