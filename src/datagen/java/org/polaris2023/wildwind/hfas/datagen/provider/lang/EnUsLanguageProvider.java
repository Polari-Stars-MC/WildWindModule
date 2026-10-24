package org.polaris2023.wildwind.hfas.datagen.provider.lang;

import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;

/**
 * 英语语言文件生成器
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class EnUsLanguageProvider extends ModuleLanguageProvider {

    public EnUsLanguageProvider(PackOutput output, ReleaseModule module) {
        super(output, module, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("inspection.wild_wind_core.book_full", "Not enough room. Use another book and quill.");
        add("inspection.wild_wind_core.book_signed", "Inspection book signed. Stop sneaking and use it to read.");
        add("inspection.wild_wind_core.datapack", "(Current version / data packs)");
        add("inspection.wild_wind_core.default", "Default");
        add("inspection.wild_wind_core.face_range", "%s–%s (by face)");
        add("inspection.wild_wind_core.field.absorbed_item", "Absorbed item ID: %s");
        add("inspection.wild_wind_core.field.air", "Air supply: %s");
        add("inspection.wild_wind_core.field.air_drag", "Air drag modifier: %s");
        add("inspection.wild_wind_core.field.armor", "Armor: %s");
        add("inspection.wild_wind_core.field.attack_damage", "Attack damage: %s");
        add("inspection.wild_wind_core.field.blast_resistance", "Blast resistance: %s");
        add("inspection.wild_wind_core.field.block", "Block: %s");
        add("inspection.wild_wind_core.field.block_id", "Block ID: %s");
        add("inspection.wild_wind_core.field.block_state", "Block state: %s");
        add("inspection.wild_wind_core.field.blocks_redstone", "Blocks redstone connection: %s");
        add("inspection.wild_wind_core.field.bounciness", "Bounciness: %s");
        add("inspection.wild_wind_core.field.buoyant", "Buoyant: %s");
        add("inspection.wild_wind_core.field.burnable", "Burnable: %s");
        add("inspection.wild_wind_core.field.cooking_time", "Smelting duration: %s");
        add("inspection.wild_wind_core.field.dimension", "Dimension: %s");
        add("inspection.wild_wind_core.field.entity", "Entity: %s");
        add("inspection.wild_wind_core.field.entity_id", "Entity ID: %s");
        add("inspection.wild_wind_core.field.fire_immune", "Fire immune: %s");
        add("inspection.wild_wind_core.field.fire_spread", "Fire spread: %s");
        add("inspection.wild_wind_core.field.flammability", "Flammability: %s");
        add("inspection.wild_wind_core.field.friction", "Friction modifier: %s");
        add("inspection.wild_wind_core.field.fuel_time", "Fuel duration: %s");
        add("inspection.wild_wind_core.field.hardness", "Hardness: %s");
        add("inspection.wild_wind_core.field.health", "Health: %s");
        add("inspection.wild_wind_core.field.height", "Height: %s");
        add("inspection.wild_wind_core.field.horizontal_knockback", "Horizontal knockback modifier: %s");
        add("inspection.wild_wind_core.field.ignited_by_lava", "Ignited by lava: %s");
        add("inspection.wild_wind_core.field.instrument", "Instrument: %s");
        add("inspection.wild_wind_core.field.item_id", "Item ID: %s");
        add("inspection.wild_wind_core.field.knockback_resistance", "Knockback resistance: %s");
        add("inspection.wild_wind_core.field.max_air", "Maximum air: %s");
        add("inspection.wild_wind_core.field.max_health", "Maximum health: %s");
        add("inspection.wild_wind_core.field.max_stack", "Maximum stack: %s");
        add("inspection.wild_wind_core.field.movement_speed", "Movement speed: %s");
        add("inspection.wild_wind_core.field.piston", "Piston behavior: %s");
        add("inspection.wild_wind_core.field.position", "Position: %s");
        add("inspection.wild_wind_core.field.push_scale", "Push scale: %s");
        add("inspection.wild_wind_core.field.redstone_conductor", "Redstone conductor: %s");
        add("inspection.wild_wind_core.field.renewable", "Renewable: %s");
        add("inspection.wild_wind_core.field.requires_tool", "Requires correct tool: %s");
        add("inspection.wild_wind_core.field.solid", "Solid block: %s");
        add("inspection.wild_wind_core.field.suffocating", "Suffocates entities: %s");
        add("inspection.wild_wind_core.field.sulfur_archetype", "Sulfur cube archetype: %s");
        add("inspection.wild_wind_core.field.tools", "Suitable tools: %s");
        add("inspection.wild_wind_core.field.vertical_knockback", "Vertical knockback modifier: %s");
        add("inspection.wild_wind_core.field.width", "Width: %s");
        add("inspection.wild_wind_core.heading", "Wild Wind · Inspection");
        add("inspection.wild_wind_core.instrument.banjo", "Banjo");
        add("inspection.wild_wind_core.instrument.basedrum", "Bass drum");
        add("inspection.wild_wind_core.instrument.bass", "Bass");
        add("inspection.wild_wind_core.instrument.bell", "Bell");
        add("inspection.wild_wind_core.instrument.bit", "Bit");
        add("inspection.wild_wind_core.instrument.chime", "Chimes");
        add("inspection.wild_wind_core.instrument.cow_bell", "Cow bell");
        add("inspection.wild_wind_core.instrument.didgeridoo", "Didgeridoo");
        add("inspection.wild_wind_core.instrument.flute", "Flute");
        add("inspection.wild_wind_core.instrument.guitar", "Guitar");
        add("inspection.wild_wind_core.instrument.harp", "Harp");
        add("inspection.wild_wind_core.instrument.hat", "Hi-hat");
        add("inspection.wild_wind_core.instrument.iron_xylophone", "Iron xylophone");
        add("inspection.wild_wind_core.instrument.pling", "Pling");
        add("inspection.wild_wind_core.instrument.snare", "Snare drum");
        add("inspection.wild_wind_core.instrument.xylophone", "Xylophone");
        add("inspection.wild_wind_core.no", "No");
        add("inspection.wild_wind_core.page", "%s");
        add("inspection.wild_wind_core.physics", "Sulfur cube physics");
        add("inspection.wild_wind_core.piston.block", "Cannot push or pull");
        add("inspection.wild_wind_core.piston.destroy", "Breaks when pushed; cannot pull");
        add("inspection.wild_wind_core.piston.normal", "Can push and pull");
        add("inspection.wild_wind_core.piston.push_only", "Can push; cannot pull");
        add("inspection.wild_wind_core.separator", ", ");
        add("inspection.wild_wind_core.time", "%s seconds (%s ticks)");
        add("inspection.wild_wind_core.tool.axe", "Axe");
        add("inspection.wild_wind_core.tool.hand", "Hand");
        add("inspection.wild_wind_core.tool.hoe", "Hoe");
        add("inspection.wild_wind_core.tool.pickaxe", "Pickaxe");
        add("inspection.wild_wind_core.tool.shears", "Shears");
        add("inspection.wild_wind_core.tool.shovel", "Shovel");
        add("inspection.wild_wind_core.tool.sword", "Sword");
        add("inspection.wild_wind_core.yes", "Yes");
        add("sulfur_cube_archetype.minecraft.bouncy", "Bouncy (rubber ball)");
        add("sulfur_cube_archetype.minecraft.explosive", "Explosive");
        add("sulfur_cube_archetype.minecraft.fast_flat", "Fast flat (golf ball)");
        add("sulfur_cube_archetype.minecraft.fast_sliding", "Fast sliding (hockey puck)");
        add("sulfur_cube_archetype.minecraft.high_resistance", "High resistance");
        add("sulfur_cube_archetype.minecraft.hot", "Hot");
        add("sulfur_cube_archetype.minecraft.light", "Light (beach ball)");
        add("sulfur_cube_archetype.minecraft.regular", "Regular (football)");
        add("sulfur_cube_archetype.minecraft.slow_bouncy", "Slow bouncy");
        add("sulfur_cube_archetype.minecraft.slow_flat", "Slow flat (medicine ball)");
        add("sulfur_cube_archetype.minecraft.slow_sliding", "Slow sliding (curling stone)");
        add("sulfur_cube_archetype.minecraft.sticky", "Sticky");
        add("mod.wild_wind.barren", "Wild Wind:Barren Update");
        add("mod.wild_wind.core", "Wild Wind:Core");
        add("mod.wild_wind.wilderness", "Wild Wind:Wilderness Update");
        add("effect.wild_wind_wilderness.sink", "Sink");
        add("effect.wild_wind_wilderness.choke", "Choke");
        add("enchantment.wild_wind_core.melting", "Melting");
        add("item.wild_wind_core.duckweed", "Duckweed");
        add("item.wild_wind_core.calamari", "Calamari");
        add("item.wild_wind_core.cooked_calamari", "Cooked Calamari");
        add("item.wild_wind_core.cooked_egg", "Fried Egg");
        add("item.wild_wind_core.baked_seeds", "Baked Seeds");
        add("item.wild_wind_core.cooked_frog_legs", "Cooked Frog Legs");
        add("item.wild_wind_core.frog_legs", "Raw Frog Legs");
        add("entity.wild_wind_wilderness.muddy_pig", "Muddy Pig");
        add("item.wild_wind_wilderness.muddy_pig_spawn_egg", "Muddy Pig Spawn Egg");
        // 焚烬木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG, "Blaze Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD, "Blaze Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG, "Stripped Blaze Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD, "Stripped Blaze Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS, "Blaze Planks");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS, "Blaze Stairs");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB, "Blaze Slab");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE, "Blaze Fence");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE, "Blaze Fence Gate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR, "Blaze Door");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR, "Blaze Trapdoor");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE, "Blaze Pressure Plate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON, "Blaze Button");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES, "Blaze Leaves");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING, "Blaze Sapling");

        // 灵焰木套件
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG, "Soul Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD, "Soul Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG, "Stripped Soul Log");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD, "Stripped Soul Wood");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS, "Soul Planks");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS, "Soul Stairs");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB, "Soul Slab");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE, "Soul Fence");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE, "Soul Fence Gate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR, "Soul Door");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR, "Soul Trapdoor");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE, "Soul Pressure Plate");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON, "Soul Button");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES, "Soul Leaves");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING, "Soul Sapling");

        // 杜鹃木套件
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG, "Azalea Log");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD, "Azalea Wood");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG, "Stripped Azalea Log");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD, "Stripped Azalea Wood");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS, "Azalea Planks");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS, "Azalea Stairs");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB, "Azalea Slab");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE, "Azalea Fence");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE, "Azalea Fence Gate");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR, "Azalea Door");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR, "Azalea Trapdoor");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE, "Azalea Pressure Plate");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON, "Azalea Button");

        // 其他方块
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS_BLOCK, "Scorched Grass Block");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_DIRT, "Scorched Dirt");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_GRASS, "Scorched Grass");
        addBlock(org.polaris2023.wildwind.wilderness.block.ModBlocks.SCORCHED_TWIG, "Scorched Twig");
        addBlock(org.polaris2023.wildwind.barren.block.ModBlocks.TINY_CACTUS, "Tiny Cactus");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN, "Azalea Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_SIGN, "Azalea Wall Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN, "Azalea Hanging Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WALL_HANGING_SIGN, "Azalea Wall Hanging Sign");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF, "Azalea Shelf");
        add("item.wild_wind_wilderness.tipped_arrow", "Arrow of Reach");
        add("item.wild_wind_wilderness.tipped_arrow.effect.reach", "Arrow of Reach");
        add("item.wild_wind_wilderness.tipped_arrow.effect.long_reach", "Arrow of Reach");
        add("entity.wild_wind_wilderness.arrow", "Arrow of Reach");
        addBlock(org.polaris2023.wildwind.core.block.ModBlocks.KILN, "Kiln");

        // GUI
        // 属性
        add("attributes.wild_wind_wilderness.extra_item_pickup_range", "Extra Item Pickup Range");

        // Reach effect & potions
        add("effect.wild_wind_wilderness.reach", "Reach");
        add("item.minecraft.potion.effect.reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.reach", "Arrow of Reach");
        add("item.minecraft.potion.effect.long_reach", "Potion of Reach");
        add("item.minecraft.splash_potion.effect.long_reach", "Splash Potion of Reach");
        add("item.minecraft.lingering_potion.effect.long_reach", "Lingering Potion of Reach");
        add("item.minecraft.tipped_arrow.effect.long_reach", "Arrow of Reach");

        // Direfish sound subtitles
        add("subtitles.entity.piranha.attack", "Direfish attacks");
        add("subtitles.entity.piranha.death", "Direfish dies");

        //创造模式物品栏
        add("mod.wild_wind.name", "Wild Wind");

        ZhEnCombineLangProvider.addLang(this, "en_us");
    }
}
