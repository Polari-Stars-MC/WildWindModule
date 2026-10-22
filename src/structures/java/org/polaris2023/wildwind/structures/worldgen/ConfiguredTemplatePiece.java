package org.polaris2023.wildwind.structures.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.polaris2023.wildwind.structures.StructuresMod;

/** Vanilla pieces clip blocks/entities to each generating chunk and persist template/rotation. */
public final class ConfiguredTemplatePiece extends TemplateStructurePiece {
    public ConfiguredTemplatePiece(StructureTemplateManager manager, Identifier template, BlockPos position,
            Rotation rotation, boolean includeEntities) {
        super(StructuresMod.PIECE.get(), 0, manager, template, template.toString(), settings(rotation, includeEntities), position);
    }

    public ConfiguredTemplatePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(StructuresMod.PIECE.get(), tag, context.structureTemplateManager(),
                id -> settings(Rotation.valueOf(tag.getStringOr("Rotation", "NONE")), tag.getBooleanOr("IncludeEntities", false)));
    }

    private static StructurePlaceSettings settings(Rotation rotation, boolean includeEntities) {
        return new StructurePlaceSettings().setRotation(rotation).setIgnoreEntities(!includeEntities)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    @Override protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString("Rotation", placeSettings.getRotation().name());
        tag.putBoolean("IncludeEntities", !placeSettings.isIgnoreEntities());
    }

    @Override protected void handleDataMarker(String marker, BlockPos position, ServerLevelAccessor level,
            RandomSource random, BoundingBox bounds) {
        // Custom DATA markers are intentionally not interpreted; structure blocks are filtered out.
    }
}
