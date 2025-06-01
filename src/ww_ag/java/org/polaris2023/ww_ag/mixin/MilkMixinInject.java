package org.polaris2023.ww_ag.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.polaris2023.ww_ag.common.init.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author baka4n
 * @code @Date 2025/5/27 00:08:04
 */
@Mixin(NeoForgeMod.class)
public class MilkMixinInject {
    @Inject(
            method = "lambda$registerFluids$76",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/fluids/BaseFlowingFluid$Source;<init>(Lnet/neoforged/neoforge/fluids/BaseFlowingFluid$Properties;)V"
            )
    )
    private static void init(RegisterEvent.RegisterHelper<Fluid> helper,
                             CallbackInfo ci,
                             @Local BaseFlowingFluid.Properties properties) {
        properties.block(ModBlocks.MILK);
        // other mod has modified it please using block tag c:milk
    }
}
