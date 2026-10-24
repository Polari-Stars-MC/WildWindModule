package org.polaris2023.wildwind.structures.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.InclusiveRange;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.resource.EmptyPackResources;
import org.polaris2023.wildwind.structures.StructureModule;

/** Each server repository gets one immutable snapshot, including template bytes. */
public final class ConfigStructurePack extends EmptyPackResources {
    private static final Gson JSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<Identifier, byte[]> resources;

    private ConfigStructurePack(PackLocationInfo info, Map<Identifier, byte[]> resources) {
        super(info, new PackMetadataSection(Component.literal("Wild Wind configured structures"),
                new InclusiveRange<>(SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA))));
        this.resources = resources;
    }

    public static void addPack(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA || event.isTrusted()) return;
        Path root = FMLPaths.CONFIGDIR.get().resolve(StructureModule.NAMESPACE);
        final Map<Identifier, byte[]> snapshot;
        try {
            snapshot = load(root);
        } catch (IOException | RuntimeException exception) {
            // Fail before registries load; silently dropping a bad definition would change the world.
            throw new IllegalStateException("Cannot load structure config at " + root + ": " + exception.getMessage(), exception);
        }
        var supplier = new Pack.ResourcesSupplier() {
            @Override public PackResources openPrimary(PackLocationInfo info) { return new ConfigStructurePack(info, snapshot); }
            @Override public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) { return openPrimary(info); }
        };
        var pack = Pack.readMetaAndCreate(new PackLocationInfo("ww_structures/config",
                Component.literal("Wild Wind · 结构生成配置"), PackSource.DEFAULT, Optional.empty()),
                supplier, PackType.SERVER_DATA, new PackSelectionConfig(true, Pack.Position.TOP, false));
        if (pack == null) throw new IllegalStateException("Could not create structure configuration datapack");
        event.addRepositorySource(consumer -> consumer.accept(pack));
    }

    public static Map<Identifier, byte[]> load(Path root) throws IOException {
        Files.createDirectories(root.resolve("definitions"));
        Files.createDirectories(root.resolve("templates"));
        Path example = root.resolve("example_ruins.json.disabled");
        if (Files.notExists(example)) {
            try (InputStream input = ConfigStructurePack.class.getResourceAsStream("/examples/example_ruins.json")) {
                if (input == null) throw new IOException("Missing bundled example");
                Files.copy(input, example);
            }
        }
        Path exampleTemplate = root.resolve("templates/example_ruins.nbt");
        if (Files.notExists(exampleTemplate)) {
            try (InputStream input = ConfigStructurePack.class.getResourceAsStream("/examples/example_ruins.nbt")) {
                if (input == null) throw new IOException("Missing bundled example template");
                Files.copy(input, exampleTemplate);
            }
        }
        Map<Identifier, byte[]> output = new HashMap<>();
        Set<String> names = new HashSet<>();
        Path definitions = root.resolve("definitions").toRealPath();
        try (var files = Files.list(definitions)) {
            for (Path file : files.filter(p -> p.getFileName().toString().endsWith(".json")).sorted().toList()) {
                try {
                    if (!file.toRealPath().startsWith(definitions)) throw new IOException("Definition symlink escapes directory");
                    if (Files.size(file) > 1024 * 1024) throw new IOException("Definition exceeds 1 MiB");
                    JsonObject json = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).getAsJsonObject();
                    StructureDefinition definition = StructureDefinition.parse(json);
                    if (!names.add(definition.name())) throw new IllegalArgumentException("Duplicate name: " + definition.name());
                    Path template = definition.resolveTemplate(root.resolve("templates"));
                    if (Files.size(template) > 16 * 1024 * 1024) throw new IOException("Template exceeds 16 MiB");
                    byte[] bytes = Files.readAllBytes(template);
                    validateTemplate(bytes);
                    addDefinition(output, definition, bytes);
                } catch (IOException | RuntimeException exception) {
                    throw new IOException(file.getFileName() + ": " + exception.getMessage(), exception);
                }
            }
        }
        LogUtils.getLogger().info("Loaded {} configured Wild Wind structures from {}", names.size(), root);
        return Map.copyOf(output);
    }

    static void validateTemplate(byte[] bytes) throws IOException {
        var nbt = NbtIo.readCompressed(new ByteArrayInputStream(bytes), NbtAccounter.create(64L * 1024 * 1024));
        var size = nbt.getListOrEmpty("size");
        if (size.size() != 3) throw new IOException("Template must have a three-component size");
        int x = size.getInt(0).orElse(0), y = size.getInt(1).orElse(0), z = size.getInt(2).orElse(0);
        if (x < 1 || x > 64 || z < 1 || z > 64 || y < 1 || y > 256) {
            throw new IOException("Template size must be within 64 x 256 x 64");
        }
        var blocks = nbt.getListOrEmpty("blocks");
        var palettes = nbt.getListOrEmpty("palettes");
        var palette = nbt.getListOrEmpty("palette");
        if (!palettes.isEmpty()) palette = palettes.getList(0).orElseThrow(() -> new IOException("Invalid palettes list"));
        if (blocks.isEmpty() || palette.isEmpty()) {
            throw new IOException("Template must contain blocks and palette/palettes (compressed structure NBT required)");
        }
        int paletteSize = palette.size();
        validatePalette(palette);
        for (int i = 0; i < palettes.size(); i++) {
            var variant = palettes.getList(i).orElseThrow(() -> new IOException("Invalid palette variant"));
            if (variant.size() != paletteSize) throw new IOException("Palette variants must have equal length");
            validatePalette(variant);
        }
        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.getCompound(i).orElseThrow(() -> new IOException("Invalid block entry"));
            int state = block.getIntOr("state", -1);
            var pos = block.getListOrEmpty("pos");
            if (state < 0 || state >= paletteSize || pos.size() != 3) throw new IOException("Invalid block state index or position");
            int px = pos.getInt(0).orElse(-1), py = pos.getInt(1).orElse(-1), pz = pos.getInt(2).orElse(-1);
            if (px < 0 || px >= x || py < 0 || py >= y || pz < 0 || pz >= z) {
                throw new IOException("Block position lies outside template size");
            }
        }
    }

    private static void validatePalette(net.minecraft.nbt.ListTag palette) throws IOException {
        for (int i = 0; i < palette.size(); i++) {
            var state = palette.getCompound(i).orElseThrow(() -> new IOException("Invalid palette entry"));
            String name = state.getStringOr("Name", "");
            if (!name.contains(":") || Identifier.tryParse(name) == null) throw new IOException("Invalid palette block ID: " + name);
        }
    }

    static void addDefinition(Map<Identifier, byte[]> output, StructureDefinition definition, byte[] template) {
        String name = definition.name();
        String id = StructureModule.NAMESPACE + ":" + name;
        JsonObject structure = new JsonObject();
        structure.addProperty("type", StructureModule.NAMESPACE + ":configured");
        structure.addProperty("biomes", "#" + id);
        structure.add("spawn_overrides", new JsonObject());
        structure.addProperty("step", "surface_structures");
        structure.addProperty("terrain_adaptation", "none");
        structure.addProperty("template", id);
        structure.addProperty("heightmap", definition.heightmap());
        structure.addProperty("y_offset", definition.yOffset());
        structure.addProperty("rotation", definition.rotation());
        structure.addProperty("include_entities", definition.includeEntities());
        structure.addProperty("min_y", definition.conditions().minY());
        structure.addProperty("max_y", definition.conditions().maxY());
        structure.addProperty("max_terrain_difference", definition.conditions().maxTerrainDifference());
        structure.addProperty("avoid_fluids", definition.conditions().avoidFluids());
        if (definition.conditions().groundBlocks().isEmpty()) structure.add("ground_blocks", new JsonArray());
        else {
            structure.addProperty("ground_blocks", "#" + id);
            putJson(output, "tags/block/" + name + ".json", tag(definition.conditions().groundBlocks()));
        }
        putJson(output, "worldgen/structure/" + name + ".json", structure);
        putJson(output, "tags/worldgen/biome/" + name + ".json", tag(definition.biomes()));
        output.put(resource("structure/" + name + ".nbt"), template);
        // Keep disabled structures and templates registered so existing chunk starts remain readable.
        if (!definition.enabled()) return;
        JsonObject set = new JsonObject();
        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("structure", id);
        entry.addProperty("weight", 1);
        entries.add(entry);
        set.add("structures", entries);
        JsonObject placement = new JsonObject();
        placement.addProperty("type", "minecraft:random_spread");
        placement.addProperty("spacing", definition.spacing());
        placement.addProperty("separation", definition.separation());
        placement.addProperty("salt", definition.salt());
        placement.addProperty("frequency", definition.frequency());
        placement.addProperty("frequency_reduction_method", "default");
        placement.addProperty("spread_type", "linear");
        set.add("placement", placement);
        putJson(output, "worldgen/structure_set/" + name + ".json", set);
    }

    private static JsonObject tag(java.util.List<String> values) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        tag.add("values", JSON.toJsonTree(values));
        return tag;
    }

    private static Identifier resource(String path) { return Identifier.fromNamespaceAndPath(StructureModule.NAMESPACE, path); }
    private static void putJson(Map<Identifier, byte[]> output, String path, JsonObject json) {
        output.put(resource(path), JSON.toJson(json).getBytes(StandardCharsets.UTF_8));
    }

    @Override public Set<String> getNamespaces(PackType type) {
        return type == PackType.SERVER_DATA ? Set.of(StructureModule.NAMESPACE) : Set.of();
    }
    @Override public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
        byte[] bytes = type == PackType.SERVER_DATA ? resources.get(location) : null;
        return bytes == null ? null : () -> new ByteArrayInputStream(bytes);
    }
    @Override public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        if (type != PackType.SERVER_DATA) return;
        resources.forEach((id, bytes) -> {
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(path + "/")) {
                output.accept(id, () -> new ByteArrayInputStream(bytes));
            }
        });
    }
}
