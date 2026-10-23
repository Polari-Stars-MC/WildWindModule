package org.polaris2023.wildwind.structures.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.polaris2023.wildwind.structures.StructureModule;

public final class ConfiguredStructure extends Structure {
    public static final MapCodec<ConfiguredStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            settingsCodec(instance),
            Identifier.CODEC.fieldOf("template").forGetter(s -> s.template),
            Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(s -> s.heightmap),
            Codec.intRange(-256, 256).fieldOf("y_offset").forGetter(s -> s.yOffset),
            Codec.STRING.fieldOf("rotation").forGetter(s -> s.rotation),
            Codec.BOOL.fieldOf("include_entities").forGetter(s -> s.includeEntities),
            Codec.INT.fieldOf("min_y").forGetter(s -> s.minY),
            Codec.INT.fieldOf("max_y").forGetter(s -> s.maxY),
            Codec.intRange(0, 512).fieldOf("max_terrain_difference").forGetter(s -> s.maxTerrainDifference),
            Codec.BOOL.fieldOf("avoid_fluids").forGetter(s -> s.avoidFluids),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("ground_blocks").forGetter(s -> s.groundBlocks)
    ).apply(instance, ConfiguredStructure::new));

    private final Identifier template;
    private final Heightmap.Types heightmap;
    private final int yOffset;
    private final String rotation;
    private final boolean includeEntities;
    private final int minY, maxY, maxTerrainDifference;
    private final boolean avoidFluids;
    private final HolderSet<Block> groundBlocks;

    public ConfiguredStructure(StructureSettings settings, Identifier template, Heightmap.Types heightmap,
            int yOffset, String rotation, boolean includeEntities, int minY, int maxY,
            int maxTerrainDifference, boolean avoidFluids, HolderSet<Block> groundBlocks) {
        super(settings);
        this.template = template;
        this.heightmap = heightmap;
        this.yOffset = yOffset;
        this.rotation = rotation;
        this.includeEntities = includeEntities;
        this.minY = minY;
        this.maxY = maxY;
        this.maxTerrainDifference = maxTerrainDifference;
        this.avoidFluids = avoidFluids;
        this.groundBlocks = groundBlocks;
    }

    @Override protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        var loaded = context.structureTemplateManager().get(template);
        if (loaded.isEmpty()) return Optional.empty();
        var structureTemplate = loaded.get();
        Rotation chosenRotation = rotation.equals("random") ? Rotation.getRandom(context.random()) : rotation(rotation);
        var size = structureTemplate.getSize(chosenRotation);
        if (size.getX() < 1 || size.getZ() < 1 || size.getY() < 1
                || size.getX() > 64 || size.getZ() > 64 || size.getY() > 256) return Optional.empty();
        int centerX = context.chunkPos().getMiddleBlockX(), centerZ = context.chunkPos().getMiddleBlockZ();
        int left = centerX - size.getX() / 2, front = centerZ - size.getZ() / 2;
        int right = left + size.getX() - 1, back = front + size.getZ() - 1;
        // Sample four corners and the center before surface decoration. No adjacent chunks are loaded.
        int[][] samples = {{left, front}, {right, front}, {left, back}, {right, back}, {centerX, centerZ}};
        int lowest = Integer.MAX_VALUE, highest = Integer.MIN_VALUE, centerHeight = 0;
        for (int[] sample : samples) {
            int height = context.chunkGenerator().getFirstFreeHeight(sample[0], sample[1], heightmap,
                    context.heightAccessor(), context.randomState());
            if (height < minY || height > maxY) return Optional.empty();
            lowest = Math.min(lowest, height);
            highest = Math.max(highest, height);
            centerHeight = height;
            var column = context.chunkGenerator().getBaseColumn(sample[0], sample[1], context.heightAccessor(), context.randomState());
            var ground = column.getBlock(height - 1);
            if (avoidFluids && (!ground.getFluidState().isEmpty() || !column.getBlock(height).getFluidState().isEmpty())) {
                return Optional.empty();
            }
            if (groundBlocks.size() > 0 && !groundBlocks.contains(ground.typeHolder())) return Optional.empty();
        }
        if (highest - lowest > maxTerrainDifference) return Optional.empty();
        int y = centerHeight + yOffset;
        if (y < context.heightAccessor().getMinY() || y + size.getY() > context.heightAccessor().getMaxY() + 1) {
            return Optional.empty();
        }
        BlockPos corner = new BlockPos(left, y, front);
        BlockPos origin = structureTemplate.getZeroPositionWithTransform(corner, Mirror.NONE, chosenRotation);
        BlockPos locatePosition = new BlockPos(centerX, y, centerZ);
        return Optional.of(new GenerationStub(locatePosition, builder -> builder.addPiece(
                new ConfiguredTemplatePiece(context.structureTemplateManager(), template, origin, chosenRotation, includeEntities))));
    }

    static Rotation rotation(String name) {
        return switch (name) {
            case "none" -> Rotation.NONE;
            case "clockwise_90" -> Rotation.CLOCKWISE_90;
            case "clockwise_180" -> Rotation.CLOCKWISE_180;
            case "counterclockwise_90" -> Rotation.COUNTERCLOCKWISE_90;
            default -> throw new IllegalArgumentException("Unknown structure rotation: " + name);
        };
    }

    @Override public StructureType<?> type() { return StructureModule.STRUCTURE.get(); }
}
