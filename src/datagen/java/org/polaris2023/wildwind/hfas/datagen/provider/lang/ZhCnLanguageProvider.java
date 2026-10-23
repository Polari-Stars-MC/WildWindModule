package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.HFASMod;

/**
 * 中文语言文件生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ZhCnLanguageProvider extends LanguageProvider {

    public ZhCnLanguageProvider(PackOutput output) {
        super(output, HFASMod.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("effect.wild_wind_wilderness.sink", "沉溺");
        add("effect.wild_wind_wilderness.choke", "窒息");
        add("enchantment.wild_wind_core.melting", "熔炼");
        add("item.wild_wind_core.duckweed", "浮萍");
        add("item.wild_wind_core.calamari", "鱿鱼须");
        add("item.wild_wind_core.cooked_calamari", "熟鱿鱼须");
        add("item.wild_wind_core.cooked_egg", "煎蛋");
        add("item.wild_wind_core.baked_seeds", "烤种子");
        add("item.wild_wind_core.cooked_frog_legs", "熟蛙腿");
        add("item.wild_wind_core.frog_legs", "生蛙腿");
        add("entity.wild_wind_wilderness.muddy_pig", "泥猪");
        add("item.wild_wind_wilderness.muddy_pig_spawn_egg", "泥猪刷怪蛋");
        // 焚烬木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG, "焚烬木原木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD, "焚烬木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG, "去皮焚烬木原木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD, "去皮焚烬木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS, "焚烬木木板");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS, "焚烬木楼梯");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB, "焚烬木台阶");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE, "焚烬木栅栏");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE, "焚烬木栅栏门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR, "焚烬木门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR, "焚烬木活板门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE, "焚烬木压力板");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON, "焚烬木按钮");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES, "焚烬木树叶");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING, "焚烬木树苗");

        // 灵焰木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG, "灵焰木原木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD, "灵焰木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG, "去皮灵焰木原木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD, "去皮灵焰木");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS, "灵焰木木板");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS, "灵焰木楼梯");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB, "灵焰木台阶");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE, "灵焰木栅栏");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE, "灵焰木栅栏门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR, "灵焰木门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR, "灵焰木活板门");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE, "灵焰木压力板");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON, "灵焰木按钮");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES, "灵焰木树叶");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING, "灵焰木树苗");

        // 杜鹃木套件
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG, "杜鹃木原木");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD, "杜鹃木");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG, "去皮杜鹃木原木");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD, "去皮杜鹃木");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS, "杜鹃木木板");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS, "杜鹃木楼梯");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB, "杜鹃木台阶");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE, "杜鹃木栅栏");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE, "杜鹃木栅栏门");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR, "杜鹃木门");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR, "杜鹃木活板门");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE, "杜鹃木压力板");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON, "杜鹃木按钮");

        // 其他方块
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK, "焦灰草方块");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT, "焦土");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS, "焦灰草丛");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_TWIG, "焦灰枝条");
        addBlock(org.polaris2023.wildwind.barren.block.ModBlocks.TINY_CACTUS, "仙人球");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN, "杜鹃木告示牌");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN, "墙上的杜鹃木告示牌");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN, "悬挂式杜鹃木告示牌");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN, "墙上的悬挂式杜鹃木告示牌");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF, "杜鹃木展示架");
        add("item.wild_wind_wilderness.tipped_arrow", "延展之箭");
        add("item.wild_wind_wilderness.tipped_arrow.effect.reach", "延展之箭");
        add("item.wild_wind_wilderness.tipped_arrow.effect.long_reach", "延展之箭");
        add("entity.wild_wind_wilderness.arrow", "延展之箭");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.KILN, "窑炉");

        // GUI
        // 属性
        add("attributes.wild_wind_wilderness.extra_item_pickup_range", "额外物品拾取距离");

        // 延展（reach）效果与药水
        add("effect.wild_wind_wilderness.reach", "延展");
        add("item.minecraft.potion.effect.reach", "延展药水");
        add("item.minecraft.splash_potion.effect.reach", "喷溅型延展药水");
        add("item.minecraft.lingering_potion.effect.reach", "滞留型延展药水");
        add("item.minecraft.tipped_arrow.effect.reach", "延展之箭");
        add("item.minecraft.potion.effect.long_reach", "延展药水");
        add("item.minecraft.splash_potion.effect.long_reach", "喷溅型延展药水");
        add("item.minecraft.lingering_potion.effect.long_reach", "滞留型延展药水");
        add("item.minecraft.tipped_arrow.effect.long_reach", "延展之箭");

        // 恐鱼音效字幕
        add("subtitles.entity.piranha.attack", "恐鱼袭击");
        add("subtitles.entity.piranha.death", "恐鱼死亡");

        //创造模式物品栏
        add("mod.wild_wind.name", "原野之风");

        ZhEnCombineLangProvider.addLang(this, "zh_cn");

    }


}
