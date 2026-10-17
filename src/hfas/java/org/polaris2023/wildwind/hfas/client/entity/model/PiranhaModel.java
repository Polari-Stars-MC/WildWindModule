package org.polaris2023.wildwind.hfas.client.entity.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.entity.animal.Piranha;

/**
 * 食人鱼实体的几何模型定义喵~
 */
public class PiranhaModel extends DefaultedEntityGeoModel<Piranha> {

	/**
	 * 创建食人鱼模型实例喵~
	 */
	public PiranhaModel() {
		super(HFASMod.id("piranha"));
	}

	/**
	 * 获取当前食人鱼对应的材质资源喵~
	 */
	@Override
	public Identifier getTextureResource(GeoRenderState state) {
		return HFASMod.id("textures/entity/piranha.png");
	}
}