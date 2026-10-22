package org.polaris2023.wildwind.structures.worldgen;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.polaris2023.wildwind.structures.StructureModule;

/** Per-dimension index; only full chunks actually loaded into a world are recorded. */
public final class GeneratedStructureIndex extends SavedData {
    public static final Codec<GeneratedStructureIndex> CODEC = Codec.unboundedMap(Identifier.CODEC, BlockPos.CODEC.listOf())
            .xmap(GeneratedStructureIndex::new, GeneratedStructureIndex::snapshot);
    public static final SavedDataType<GeneratedStructureIndex> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(StructureModule.NAMESPACE, "generated_structures"), GeneratedStructureIndex::new, CODEC);
    private final Map<Identifier, Set<BlockPos>> positions = new HashMap<>();

    public GeneratedStructureIndex() {}
    private GeneratedStructureIndex(Map<Identifier, List<BlockPos>> saved) {
        saved.forEach((id, list) -> positions.put(id, new HashSet<>(list)));
    }
    private Map<Identifier, List<BlockPos>> snapshot() {
        Map<Identifier, List<BlockPos>> result = new HashMap<>();
        positions.forEach((id, values) -> result.put(id, new ArrayList<>(values)));
        return result;
    }
    public void record(Identifier id, BlockPos position) {
        if (positions.computeIfAbsent(id, ignored -> new HashSet<>()).add(position.immutable())) setDirty();
    }
    public Set<Identifier> names() { return Set.copyOf(positions.keySet()); }
    public Optional<BlockPos> nearest(Identifier id, BlockPos from) {
        return positions.getOrDefault(id, Set.of()).stream().min(Comparator
                .comparingDouble((BlockPos p) -> horizontalDistanceSquared(p, from))
                .thenComparingInt(BlockPos::getX).thenComparingInt(BlockPos::getZ).thenComparingInt(BlockPos::getY));
    }
    public static double horizontalDistanceSquared(BlockPos a, BlockPos b) {
        double x = (double) a.getX() - b.getX(), z = (double) a.getZ() - b.getZ();
        return x * x + z * z;
    }

    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        var registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        Map<Identifier, BlockPos> found = new HashMap<>();
        event.getChunk().getAllStarts().forEach((structure, start) -> {
            if (!(structure instanceof ConfiguredStructure) || !start.isValid()) return;
            Identifier id = registry.getKey(structure);
            var bounds = start.getBoundingBox();
            if (id != null) found.put(id, new BlockPos(bounds.getCenter().getX(), bounds.minY(), bounds.getCenter().getZ()));
        });
        if (found.isEmpty()) return;
        // Do not touch SavedData or request chunks from the chunk-load callback.
        level.getServer().execute(() -> {
            var index = level.getDataStorage().computeIfAbsent(TYPE);
            found.forEach(index::record);
        });
    }
}
