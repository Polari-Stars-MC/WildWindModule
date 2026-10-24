package org.polaris2023.wildwind.wilderness.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.wilderness.WildernessMod;

/**
 * 定义模组方块标签喵~
 */
public interface ModBlockTags {

    interface EntityAbout {
        //泥沼蟹可在其上生成的方块标签喵~
        TagKey<Block> MUDCRAB_SPAWNABLE_ON = BlockTags.create(WildernessMod.id("entity/mudcrab/spawnable_on"));
        //泥沼蟹可在水下地面生成的方块标签喵~
        TagKey<Block> MUDCRAB_SPAWNABLE_IN_WATER_GROUND = BlockTags.create(WildernessMod.id("entity/mudcrab/spawnable_in_water_ground"));
        //泥沼蟹偏好游荡的方块标签喵~
        TagKey<Block> MUDCRAB_PREFERRED_WANDER_BLOCKS = BlockTags.create(WildernessMod.id("entity/mudcrab/preferred_wander_blocks"));

        //蜘蛛卵
        TagKey<Block> SPIDER_COCOONS = BlockTags.create(WildernessMod.id("spider_cocoons"));
    }

}
