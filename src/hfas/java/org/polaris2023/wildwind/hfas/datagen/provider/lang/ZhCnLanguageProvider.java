package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.registry.ModItems;

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


        // 杜鹃木套件
        addBlock(ModBlocks.AZALEA_LOG, "杜鹃木原木");
        addBlock(ModBlocks.AZALEA_WOOD, "杜鹃木");
        addBlock(ModBlocks.STRIPPED_AZALEA_LOG, "去皮杜鹃木原木");
        addBlock(ModBlocks.STRIPPED_AZALEA_WOOD, "去皮杜鹃木");
        addBlock(ModBlocks.AZALEA_PLANKS, "杜鹃木木板");
        addBlock(ModBlocks.AZALEA_STAIRS, "杜鹃木楼梯");
        addBlock(ModBlocks.AZALEA_SLAB, "杜鹃木台阶");
        addBlock(ModBlocks.AZALEA_FENCE, "杜鹃木栅栏");
        addBlock(ModBlocks.AZALEA_FENCE_GATE, "杜鹃木栅栏门");
        addBlock(ModBlocks.AZALEA_DOOR, "杜鹃木门");
        addBlock(ModBlocks.AZALEA_TRAPDOOR, "杜鹃木活板门");
        addBlock(ModBlocks.AZALEA_PRESSURE_PLATE, "杜鹃木压力板");
        addBlock(ModBlocks.AZALEA_BUTTON, "杜鹃木按钮");

        // 其他方块
        addBlock(ModBlocks.SCORCHED_GRASS_BLOCK, "焦灰草方块");
        addBlock(ModBlocks.SCORCHED_DIRT, "焦土");
        addBlock(ModBlocks.SCORCHED_GRASS, "焦灰草丛");
        addBlock(ModBlocks.SCORCHED_TWIG, "焦灰枝条");
        addBlock(ModBlocks.TINY_CACTUS, "仙人球");
        addBlock(ModBlocks.FLETCHING_TABLE, "制箭台");

        // GUI
        add("container.fletching_table", "制箭台");
        // 属性
        add("attributes.ww_hfas.extra_item_pickup_range", "额外物品拾取距离");

        // 延展（reach）效果与药水
        add("effect.ww_hfas.reach", "延展");
        add("item.minecraft.potion.effect.reach", "延展药水");
        add("item.minecraft.splash_potion.effect.reach", "喷溅型延展药水");
        add("item.minecraft.lingering_potion.effect.reach", "滞留型延展药水");
        add("item.minecraft.tipped_arrow.effect.reach", "延展之箭");
        add("item.minecraft.potion.effect.long_reach", "延展药水");
        add("item.minecraft.splash_potion.effect.long_reach", "喷溅型延展药水");
        add("item.minecraft.lingering_potion.effect.long_reach", "滞留型延展药水");
        add("item.minecraft.tipped_arrow.effect.long_reach", "延展之箭");

        // 食人鱼音效字幕
        add("subtitles.entity.piranha.attack", "食人鱼袭击");
        add("subtitles.entity.piranha.death", "食人鱼死亡");

        //创造模式物品栏
        add("mod.wild_wind.name", "原野之风");

        ZhEnCombineLangProvider.addLang(this, "zh_cn");

    }


}

        addBlock(ModBlocks.CINDER_LOG, "����ľԭľ");
        addBlock(ModBlocks.CINDER_WOOD, "����ľ");
        addBlock(ModBlocks.STRIPPED_CINDER_LOG, "ȥƤ����ľԭľ");
        addBlock(ModBlocks.STRIPPED_CINDER_WOOD, "ȥƤ����ľ");
        addBlock(ModBlocks.CINDER_PLANKS, "����ľľ��");
        addBlock(ModBlocks.CINDER_STAIRS, "����ľ¥��");
        addBlock(ModBlocks.CINDER_SLAB, "����ľ̨��");
        addBlock(ModBlocks.CINDER_FENCE, "����ľդ��");
        addBlock(ModBlocks.CINDER_FENCE_GATE, "����ľդ����");
        addBlock(ModBlocks.CINDER_DOOR, "����ľ��");
        addBlock(ModBlocks.CINDER_TRAPDOOR, "����ľ�����");
        addBlock(ModBlocks.CINDER_PRESSURE_PLATE, "����ľѹ����");
        addBlock(ModBlocks.CINDER_BUTTON, "����ľ��ť");
        addBlock(ModBlocks.CINDER_LEAVES, "����ľ��Ҷ");
        addBlock(ModBlocks.CINDER_SAPLING, "����ľ����");
        addBlock(ModBlocks.EMBER_LOG, "���ľԭľ");
        addBlock(ModBlocks.EMBER_WOOD, "���ľ");
        addBlock(ModBlocks.STRIPPED_EMBER_LOG, "ȥƤ���ľԭľ");
        addBlock(ModBlocks.STRIPPED_EMBER_WOOD, "ȥƤ���ľ");
        addBlock(ModBlocks.EMBER_PLANKS, "���ľľ��");
        addBlock(ModBlocks.EMBER_STAIRS, "���ľ¥��");
        addBlock(ModBlocks.EMBER_SLAB, "���ľ̨��");
        addBlock(ModBlocks.EMBER_FENCE, "���ľդ��");
        addBlock(ModBlocks.EMBER_FENCE_GATE, "���ľդ����");
        addBlock(ModBlocks.EMBER_DOOR, "���ľ��");
        addBlock(ModBlocks.EMBER_TRAPDOOR, "���ľ�����");
        addBlock(ModBlocks.EMBER_PRESSURE_PLATE, "���ľѹ����");
        addBlock(ModBlocks.EMBER_BUTTON, "���ľ��ť");
        addBlock(ModBlocks.EMBER_LEAVES, "���ľ��Ҷ");
        addBlock(ModBlocks.EMBER_SAPLING, "���ľ����");