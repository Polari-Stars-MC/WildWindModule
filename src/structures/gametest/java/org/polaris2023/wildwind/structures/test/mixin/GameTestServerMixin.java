package org.polaris2023.wildwind.structures.test.mixin;

import net.minecraft.gametest.framework.GameTestServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Vanilla GameTest disables structure generation. Enable it only in the test mod. */
@Mixin(GameTestServer.class)
public abstract class GameTestServerMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/WorldOptions;<init>(JZZ)V"), index = 1)
    private static boolean structuresTests$enableStructures(boolean original) { return true; }
}
