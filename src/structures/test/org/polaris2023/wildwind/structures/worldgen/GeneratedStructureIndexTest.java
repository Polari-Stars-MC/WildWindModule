package org.polaris2023.wildwind.structures.worldgen;

import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeneratedStructureIndexTest {
    @Test void nearestUsesHorizontalDistanceAndSurvivesSerialization() {
        var id = Identifier.parse("ww_structures:ruins");
        var index = new GeneratedStructureIndex();
        index.record(id, new BlockPos(5, 200, 0));
        index.record(id, new BlockPos(10, 64, 0));
        index.record(Identifier.parse("ww_structures:other"), BlockPos.ZERO);
        assertEquals(new BlockPos(5, 200, 0), index.nearest(id, new BlockPos(0, 64, 0)).orElseThrow());
        var json = GeneratedStructureIndex.CODEC.encodeStart(JsonOps.INSTANCE, index).getOrThrow();
        var restored = GeneratedStructureIndex.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        assertEquals(index.nearest(id, BlockPos.ZERO), restored.nearest(id, BlockPos.ZERO));
        assertTrue(restored.nearest(Identifier.parse("ww_structures:missing"), BlockPos.ZERO).isEmpty());
        restored.record(id, new BlockPos(5, 200, 0));
        assertFalse(restored.isDirty(), "Re-loading the same chunk must not dirty the index");
    }

    @Test void largeCoordinatesDoNotOverflow() {
        assertEquals(3_600_000_000_000_000d, GeneratedStructureIndex.horizontalDistanceSquared(
                new BlockPos(-30_000_000, 0, 0), new BlockPos(30_000_000, 0, 0)));
    }
}
