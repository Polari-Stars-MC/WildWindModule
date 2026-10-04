package org.polaris2023.wildwind.hfas.datagen.provider.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.registry.ModEntities;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

/**
 * 生成模组实体类型标签数据喵~
 */
public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
	/**
	 * 创建实体类型标签提供器喵~
	 *
	 * @param output 输出目标喵~
	 * @param provider 注册表查询提供器喵~
	 */
	public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, HFASMod.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(EntityTypeTags.AQUATIC)
				.add(ModEntities.MUDCRAB.getKey());
		tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
				.add(ModEntities.MUDCRAB.getKey());
		tag(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH)
				.add(ModEntities.MUDCRAB.getKey());
	}
}
