package org.polaris2023.wildwind.structures.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class StructureConfigTest {
    @TempDir Path directory;

    private static JsonObject definition() {
        return JsonParser.parseString("""
                {"name":"ruins", "template":"ruins.nbt", "biomes":["minecraft:plains", "#minecraft:is_forest"]}
                """).getAsJsonObject();
    }

    @Test void defaultsAndNamespacedBiomeTags() {
        var config = StructureDefinition.parse(definition());
        assertEquals(32, config.spacing());
        assertEquals(8, config.separation());
        assertEquals("#minecraft:is_forest", config.biomes().get(1));
        assertFalse(config.includeEntities());
        assertTrue(config.conditions().avoidFluids());
    }

    @Test void rejectsInvalidPlacementAndMistypedOptions() {
        for (String placement : new String[]{"{\"spacing\":8,\"separation\":8}", "{\"frequency\":0}",
                "{\"frequency\":1.1}", "{\"spacing\":32.5}", "{\"salt\":-1}", "{\"spacing\":\"32\"}"}) {
            JsonObject json = definition();
            json.add("placement", JsonParser.parseString(placement));
            assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(json));
        }
        JsonObject typo = definition();
        typo.addProperty("y_offest", 5);
        assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(typo));
        JsonObject booleanString = definition();
        booleanString.addProperty("include_entities", "false");
        assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(booleanString));
    }

    @Test void rejectsInvalidNamesBiomesAndHeightRanges() {
        for (String name : new String[]{"../escape", "MixedCase", "a:b", ""}) {
            JsonObject json = definition();
            json.addProperty("name", name);
            assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(json));
        }
        JsonObject emptyBiomes = definition();
        emptyBiomes.add("biomes", JsonParser.parseString("[]"));
        assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(emptyBiomes));
        JsonObject json = definition();
        json.add("conditions", JsonParser.parseString("{\"min_y\":100,\"max_y\":80}"));
        JsonObject invalidHeight = json;
        assertThrows(IllegalArgumentException.class, () -> StructureDefinition.parse(invalidHeight));
    }

    @Test void templatePathsStayInsideDirectory() throws Exception {
        Path templates = Files.createDirectories(directory.resolve("templates"));
        Files.write(templates.resolve("ruins.nbt"), new byte[0]);
        assertEquals(templates.resolve("ruins.nbt").toRealPath(), StructureDefinition.parse(definition()).resolveTemplate(templates));
        for (String path : new String[]{"../escape.nbt", "C:/escape.nbt", "/escape.nbt", "..\\escape.nbt"}) {
            JsonObject json = definition();
            json.addProperty("template", path);
            var parsed = StructureDefinition.parse(json);
            assertThrows(IllegalArgumentException.class, () -> parsed.resolveTemplate(templates));
        }
    }

    @Test void disabledStructuresKeepTemplatesButNotPlacementAndDuplicateNamesFail() throws Exception {
        ConfigStructurePack.load(directory);
        Path definitions = directory.resolve("definitions");
        JsonObject disabled = definition();
        disabled.addProperty("enabled", false);
        Files.writeString(definitions.resolve("disabled.json"), disabled.toString());
        Files.write(directory.resolve("templates/ruins.nbt"), template(3, 2, 3));
        var disabledResources = ConfigStructurePack.load(directory);
        assertEquals(3, disabledResources.size());
        assertTrue(disabledResources.containsKey(Identifier.parse("ww_structures:structure/ruins.nbt")));
        assertFalse(disabledResources.containsKey(Identifier.parse("ww_structures:worldgen/structure_set/ruins.json")));
        Files.writeString(definitions.resolve("disabled.json"), definition().toString());
        var resources = ConfigStructurePack.load(directory);
        assertEquals(4, resources.size());
        Files.writeString(definitions.resolve("b.json"), definition().toString());
        var error = assertThrows(java.io.IOException.class, () -> ConfigStructurePack.load(directory));
        assertTrue(error.getMessage().contains("Duplicate name"));
    }

    @Test void packContainsTemplateBiomeTagAndRandomSpreadRule() {
        var resources = new HashMap<Identifier, byte[]>();
        byte[] template = {1, 2, 3};
        ConfigStructurePack.addDefinition(resources, StructureDefinition.parse(definition()), template);
        assertArrayEquals(template, resources.get(Identifier.parse("ww_structures:structure/ruins.nbt")));
        var placement = JsonParser.parseString(new String(resources.get(Identifier.parse("ww_structures:worldgen/structure_set/ruins.json")),
                java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonObject("placement");
        assertEquals("minecraft:random_spread", placement.get("type").getAsString());
        assertEquals(32, placement.get("spacing").getAsInt());
    }

    @Test void rejectsMalformedOrOversizeTemplate() throws Exception {
        ConfigStructurePack.validateTemplate(template(3, 2, 3));
        assertThrows(java.io.IOException.class, () -> ConfigStructurePack.validateTemplate(new byte[]{1, 2, 3}));
        assertThrows(java.io.IOException.class, () -> ConfigStructurePack.validateTemplate(template(65, 2, 3)));
        assertThrows(java.io.IOException.class, () -> ConfigStructurePack.validateTemplate(template(3, 0, 3)));
    }

    private static byte[] template(int x, int y, int z) throws Exception {
        CompoundTag root = new CompoundTag();
        ListTag size = new ListTag();
        size.add(IntTag.valueOf(x)); size.add(IntTag.valueOf(y)); size.add(IntTag.valueOf(z));
        root.put("size", size);
        ListTag blocks = new ListTag();
        CompoundTag block = new CompoundTag();
        block.putInt("state", 0);
        ListTag pos = new ListTag();
        for (int i = 0; i < 3; i++) pos.add(IntTag.valueOf(0));
        block.put("pos", pos);
        blocks.add(block);
        root.put("blocks", blocks);
        ListTag palette = new ListTag();
        CompoundTag stone = new CompoundTag();
        stone.putString("Name", "minecraft:stone");
        palette.add(stone);
        root.put("palette", palette);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NbtIo.writeCompressed(root, out);
        return out.toByteArray();
    }
}
