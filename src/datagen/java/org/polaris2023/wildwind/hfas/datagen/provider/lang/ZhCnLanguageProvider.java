package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;

/**
 * 中文语言文件生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ZhCnLanguageProvider extends ModuleLanguageProvider {

    public ZhCnLanguageProvider(PackOutput output, ReleaseModule module) {
        super(output, module, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("inspection.wild_wind_core.book_full", "书的剩余页数不足，未写入。请换一本书与笔。");
        add("inspection.wild_wind_core.book_signed", "已生成署名属性书；松开潜行后右键阅读。");
        add("inspection.wild_wind_core.datapack", "（当前版本／数据包）");
        add("inspection.wild_wind_core.default", "默认");
        add("inspection.wild_wind_core.face_range", "%s–%s（随面）");
        add("inspection.wild_wind_core.field.absorbed_item", "吸收物品ID:%s");
        add("inspection.wild_wind_core.field.air", "当前氧气:%s");
        add("inspection.wild_wind_core.field.air_drag", "空气阻力修正系数:%s");
        add("inspection.wild_wind_core.field.armor", "护甲:%s");
        add("inspection.wild_wind_core.field.attack_damage", "攻击伤害:%s");
        add("inspection.wild_wind_core.field.blast_resistance", "爆炸抗性:%s");
        add("inspection.wild_wind_core.field.block", "方块:%s");
        add("inspection.wild_wind_core.field.block_id", "方块ID:%s");
        add("inspection.wild_wind_core.field.block_state", "方块状态:%s");
        add("inspection.wild_wind_core.field.blocks_redstone", "压断红石线:%s");
        add("inspection.wild_wind_core.field.bounciness", "弹性:%s");
        add("inspection.wild_wind_core.field.buoyant", "浮力:%s");
        add("inspection.wild_wind_core.field.burnable", "是否可燃:%s");
        add("inspection.wild_wind_core.field.cooking_time", "烧炼加工时间:%s");
        add("inspection.wild_wind_core.field.dimension", "维度:%s");
        add("inspection.wild_wind_core.field.entity", "实体:%s");
        add("inspection.wild_wind_core.field.entity_id", "实体ID:%s");
        add("inspection.wild_wind_core.field.fire_immune", "火焰免疫:%s");
        add("inspection.wild_wind_core.field.fire_spread", "引燃几率:%s");
        add("inspection.wild_wind_core.field.flammability", "烧毁几率:%s");
        add("inspection.wild_wind_core.field.friction", "摩擦力修正系数:%s");
        add("inspection.wild_wind_core.field.fuel_time", "烧炼时间:%s");
        add("inspection.wild_wind_core.field.hardness", "硬度:%s");
        add("inspection.wild_wind_core.field.health", "生命值:%s");
        add("inspection.wild_wind_core.field.height", "高度:%s");
        add("inspection.wild_wind_core.field.horizontal_knockback", "水平击退修正系数:%s");
        add("inspection.wild_wind_core.field.ignited_by_lava", "熔岩可燃性:%s");
        add("inspection.wild_wind_core.field.instrument", "乐器:%s");
        add("inspection.wild_wind_core.field.item_id", "物品ID:%s");
        add("inspection.wild_wind_core.field.knockback_resistance", "击退抗性:%s");
        add("inspection.wild_wind_core.field.max_air", "最大氧气:%s");
        add("inspection.wild_wind_core.field.max_health", "最大生命值:%s");
        add("inspection.wild_wind_core.field.max_stack", "最大堆叠:%s");
        add("inspection.wild_wind_core.field.movement_speed", "移动速度:%s");
        add("inspection.wild_wind_core.field.piston", "活塞推动行为:%s");
        add("inspection.wild_wind_core.field.position", "坐标:%s");
        add("inspection.wild_wind_core.field.push_scale", "推动幅度:%s");
        add("inspection.wild_wind_core.field.redstone_conductor", "红石导体:%s");
        add("inspection.wild_wind_core.field.renewable", "可再生:%s");
        add("inspection.wild_wind_core.field.requires_tool", "需正确工具掉落:%s");
        add("inspection.wild_wind_core.field.solid", "固体方块:%s");
        add("inspection.wild_wind_core.field.suffocating", "窒息生物:%s");
        add("inspection.wild_wind_core.field.sulfur_archetype", "硫方怪原型:%s");
        add("inspection.wild_wind_core.field.tools", "合适挖掘工具:%s");
        add("inspection.wild_wind_core.field.vertical_knockback", "垂直击退修正系数:%s");
        add("inspection.wild_wind_core.field.width", "宽度:%s");
        add("inspection.wild_wind_core.heading", "原野之风·属性记录");
        add("inspection.wild_wind_core.instrument.banjo", "班卓琴");
        add("inspection.wild_wind_core.instrument.basedrum", "底鼓");
        add("inspection.wild_wind_core.instrument.bass", "贝斯");
        add("inspection.wild_wind_core.instrument.bell", "钟琴");
        add("inspection.wild_wind_core.instrument.bit", "方波");
        add("inspection.wild_wind_core.instrument.chime", "管钟");
        add("inspection.wild_wind_core.instrument.cow_bell", "牛铃");
        add("inspection.wild_wind_core.instrument.didgeridoo", "迪吉里杜管");
        add("inspection.wild_wind_core.instrument.flute", "长笛");
        add("inspection.wild_wind_core.instrument.guitar", "吉他");
        add("inspection.wild_wind_core.instrument.harp", "竖琴");
        add("inspection.wild_wind_core.instrument.hat", "踩镲");
        add("inspection.wild_wind_core.instrument.iron_xylophone", "铁木琴");
        add("inspection.wild_wind_core.instrument.pling", "电钢琴");
        add("inspection.wild_wind_core.instrument.snare", "军鼓");
        add("inspection.wild_wind_core.instrument.xylophone", "木琴");
        add("inspection.wild_wind_core.no", "否");
        add("inspection.wild_wind_core.page", "%s");
        add("inspection.wild_wind_core.physics", "硫方怪物理参数");
        add("inspection.wild_wind_core.piston.block", "不可推动，不可拉动");
        add("inspection.wild_wind_core.piston.destroy", "被推动破坏，不可拉动");
        add("inspection.wild_wind_core.piston.normal", "可推动，可拉动");
        add("inspection.wild_wind_core.piston.push_only", "可推动，不可拉动");
        add("inspection.wild_wind_core.separator", "、");
        add("inspection.wild_wind_core.time", "%s秒（%s刻）");
        add("inspection.wild_wind_core.tool.axe", "斧");
        add("inspection.wild_wind_core.tool.hand", "徒手");
        add("inspection.wild_wind_core.tool.hoe", "锄");
        add("inspection.wild_wind_core.tool.pickaxe", "镐");
        add("inspection.wild_wind_core.tool.shears", "剪刀");
        add("inspection.wild_wind_core.tool.shovel", "锹");
        add("inspection.wild_wind_core.tool.sword", "剑");
        add("inspection.wild_wind_core.yes", "是");
        add("sulfur_cube_archetype.minecraft.bouncy", "弹性（橡胶球）");
        add("sulfur_cube_archetype.minecraft.explosive", "爆炸");
        add("sulfur_cube_archetype.minecraft.fast_flat", "快速平移（高尔夫球）");
        add("sulfur_cube_archetype.minecraft.fast_sliding", "快速滑行（冰球）");
        add("sulfur_cube_archetype.minecraft.high_resistance", "高阻力");
        add("sulfur_cube_archetype.minecraft.hot", "高温");
        add("sulfur_cube_archetype.minecraft.light", "轻盈（沙滩球）");
        add("sulfur_cube_archetype.minecraft.regular", "普通（足球）");
        add("sulfur_cube_archetype.minecraft.slow_bouncy", "缓慢弹性");
        add("sulfur_cube_archetype.minecraft.slow_flat", "缓慢平移（药球）");
        add("sulfur_cube_archetype.minecraft.slow_sliding", "缓慢滑行（冰壶）");
        add("sulfur_cube_archetype.minecraft.sticky", "黏性");
        add("mod.wild_wind.barren", "原野之风：瘠土更新");
        add("mod.wild_wind.core", "原野之风：核心");
        add("mod.wild_wind.wilderness", "原野之风：荒野更新");
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
