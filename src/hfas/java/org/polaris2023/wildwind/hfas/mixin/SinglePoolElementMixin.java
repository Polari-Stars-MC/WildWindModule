package org.polaris2023.wildwind.hfas.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.polaris2023.wildwind.hfas.worldgen.MasonKilnProcessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
abstract class SinglePoolElementMixin {
    @Shadow @Final protected Either<Identifier, StructureTemplate> template;

    @Inject(method = "getSettings", at = @At("RETURN"))
    private void wildwind$addMasonKiln(Rotation rotation, BoundingBox box, LiquidSettings liquids, boolean keepJigsaws,
            CallbackInfoReturnable<StructurePlaceSettings> cir) {
        template.left().ifPresent(id -> {
            var processor = MasonKilnProcessor.HOUSES.get(id);
            if (processor != null) cir.getReturnValue().addProcessor(processor);
        });
    }
}
