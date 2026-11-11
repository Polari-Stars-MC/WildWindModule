package org.polaris2023.wildwind.wilderness.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;

/**
 * 荒野更新的方块注册。
 *
 * @author safe_saffawa
 */
public final class WildernessBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WildWindWilderness.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WildWindWilderness.MOD_ID);

    /** 灵魂南瓜灯 */
    public static final DeferredBlock<Block> SOUL_JACK_O_LANTERN = BLOCKS.registerBlock(
            "soul_jack_o_lantern",
            p -> new CarvedPumpkinBlock(p
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(state -> 10))
    );
    public static final DeferredBlock<Block> SILT = BLOCKS.registerBlock(
            "silt",
            p -> new Block(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5F)
                    .sound(SoundType.MUD))
    );

    // ==================== 沼泽装饰 ====================

    /** 芦苇 */
    public static final DeferredBlock<Block> REEDS = BLOCKS.registerBlock(
            "reeds",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 香蒲 */
    public static final DeferredBlock<Block> CATTAILS = BLOCKS.registerBlock(
            "cattails",
            p -> new Block(p
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY))
    );

    /** 泥沼 */
    public static final DeferredBlock<Block> BOG_MUD = BLOCKS.registerBlock(
            "bog_mud",
            p -> new Block(p
                    .mapColor(MapColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.MUD))
    );

    /** 遗迹 */
    public static final DeferredBlock<Block> REMAINS = BLOCKS.registerBlock(
            "remains",
            p -> new Block(p
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(0.6F)
                    .sound(SoundType.BONE_BLOCK))
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        blockItems();
        ITEMS.register(bus);
    }

    public static void blockItems() {
        ITEMS.registerSimpleBlockItem(SOUL_JACK_O_LANTERN);
        ITEMS.registerSimpleBlockItem(SILT);

        ITEMS.registerSimpleBlockItem(REEDS);
        ITEMS.registerSimpleBlockItem(CATTAILS);
        ITEMS.registerSimpleBlockItem(BOG_MUD);
        ITEMS.registerSimpleBlockItem(REMAINS);
    }

    private WildernessBlocks() {
    }
}
