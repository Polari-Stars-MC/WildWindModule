package org.polaris2023.ww_ag.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.fluids.FluidType;
import org.polaris2023.ww_ag.WWAgMod;
import org.polaris2023.ww_ag.common.init.ModBlocks;
import org.polaris2023.ww_ag.common.init.ModConfigs;
import org.polaris2023.ww_ag.common.init.ModDataComponents;
import org.polaris2023.ww_ag.common.init.tags.WWBlockTags;
import org.polaris2023.ww_ag.common.init.tags.WWItemTags;

import javax.annotation.Nullable;

/**
 * @author baka4n
 * @code @Date 2025/6/5 22:07:36
 */
@EventBusSubscriber(modid = WWAgMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvents {
    static boolean canBlockContainFluid(@Nullable Player player, Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, NeoForgeMod.MILK.get());
    }

    @SubscribeEvent
    public static void rightMilkPlaceAndGet(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();

        BlockPos pos = event.getPos();//get current block pos
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockState state = level.getBlockState(pos);
        BlockHitResult hitVec = event.getHitVec();
        Direction direction = hitVec.getDirection();
        BlockPos getFacePos = state.canBeReplaced() ?
                pos :
                pos.relative(direction);

        if (ModConfigs.USE_ITEM.milk_block_use.get()) {
            if (stack.is(Items.MILK_BUCKET)) {
                BlockPos posNow = canBlockContainFluid(player, level, pos, state) ? pos : getFacePos;
                stack.shrink(1);
                ResourceLocation location = stack.getOrDefault(ModDataComponents.MILK_TYPE, ModBlocks.MILK.getId());
                event.setCanceled(true);
                Block block = BuiltInRegistries.BLOCK.get(location);
                level.setBlockAndUpdate(posNow, block.defaultBlockState());
                ItemStack defaultInstance = Items.BUCKET.getDefaultInstance();
                player.addItem(defaultInstance);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void a(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ResourceLocation location = stack.getOrDefault(ModDataComponents.MILK_TYPE, ModBlocks.MILK.getId());
        event.getToolTip().add(Component.literal(location.toString()));
    }
}
