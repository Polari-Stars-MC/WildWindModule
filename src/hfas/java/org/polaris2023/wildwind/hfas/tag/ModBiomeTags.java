package org.polaris2023.wildwind.hfas.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.polaris2023.wildwind.hfas.HFASMod;

/**
 * 定义模组群系标签喵~
 */
//TODO BEFORE PR 数据驱动接线
public interface ModBiomeTags {

	interface EntityGen{
		/**
		 * 会生成暖色变种泥沼蟹的群系标签喵~
		 */
		TagKey<Biome> MUDCRABS_WARM = create("entity_spawn/mudcrab/warm");
		/**
		 * 会生成冷色变种泥沼蟹的群系标签喵~
		 */
		TagKey<Biome> MUDCRABS_COLD = create("entity_spawn/mudcrab/cold");
	}

	/**
	 * 创建群系标签键喵~
	 *
	 * @param path 标签路径喵~
	 * @return 群系标签键喵~
	 */
	static TagKey<Biome> create(String path) {
		return TagKey.create(Registries.BIOME, HFASMod.id(path));
	}
}
