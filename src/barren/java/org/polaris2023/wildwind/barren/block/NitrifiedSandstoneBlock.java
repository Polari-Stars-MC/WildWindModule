package org.polaris2023.wildwind.barren.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 硝化砂岩：被正确工具破坏时掉落硝石并给予 1-5 点经验，精准采集时只掉落自身。
 *
 * @author mdjfnfn
 */
public class NitrifiedSandstoneBlock extends DropExperienceBlock {
    public static final MapCodec<NitrifiedSandstoneBlock> CODEC = simpleCodec(NitrifiedSandstoneBlock::new);

    public NitrifiedSandstoneBlock(Properties properties) {
        super(UniformInt.of(1, 5), properties);
    }

    @Override
    public MapCodec<? extends NitrifiedSandstoneBlock> codec() {
        return CODEC;
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        // 精准采集只掉落方块本身，不再额外给经验
        if (!dropExperience || hasSilkTouch(level, tool)) {
            return;
        }
        super.spawnAfterBreak(state, level, pos, tool, true);
    }

    private static boolean hasSilkTouch(ServerLevel level, ItemStack tool) {
        var enchantments = tool.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null) {
            return false;
        }
        Holder<Enchantment> silkTouch = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.SILK_TOUCH);
        return enchantments.getLevel(silkTouch) > 0;
    }
}
