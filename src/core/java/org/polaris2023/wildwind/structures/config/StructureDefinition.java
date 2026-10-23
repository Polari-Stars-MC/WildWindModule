package org.polaris2023.wildwind.structures.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Validated user configuration. Registry and template validation happens when building the pack. */
public record StructureDefinition(String name, boolean enabled, String template, List<String> biomes,
        int spacing, int separation, int salt, double frequency, String heightmap,
        int yOffset, String rotation, boolean includeEntities, Conditions conditions) {
    public record Conditions(int minY, int maxY, int maxTerrainDifference,
                             boolean avoidFluids, List<String> groundBlocks) {}

    public static StructureDefinition parse(JsonObject json) {
        keys(json, Set.of("name", "enabled", "template", "biomes", "placement", "heightmap",
                "y_offset", "rotation", "include_entities", "conditions"));
        String name = string(json, "name", null);
        if (!name.matches("[a-z0-9_][a-z0-9_-]{0,63}")) {
            throw new IllegalArgumentException("name must match [a-z0-9_][a-z0-9_-]{0,63}");
        }
        String template = string(json, "template", null);
        if (!template.endsWith(".nbt")) throw new IllegalArgumentException("template must end with .nbt");
        List<String> biomes = identifiers(json, "biomes");
        if (biomes.isEmpty()) throw new IllegalArgumentException("biomes must not be empty");
        JsonObject placement = object(json, "placement");
        keys(placement, Set.of("spacing", "separation", "salt", "frequency"));
        int spacing = integer(placement, "spacing", 32, 1, 4096);
        int separation = integer(placement, "separation", 8, 0, 4095);
        if (separation >= spacing) throw new IllegalArgumentException("separation must be smaller than spacing");
        int salt = integer(placement, "salt", name.hashCode() & Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        double frequency = number(placement, "frequency", 1.0);
        if (!Double.isFinite(frequency) || frequency <= 0 || frequency > 1) {
            throw new IllegalArgumentException("frequency must be > 0 and <= 1 (use enabled=false to disable)");
        }
        String heightmap = string(json, "heightmap", "WORLD_SURFACE_WG");
        if (!Set.of("WORLD_SURFACE_WG", "OCEAN_FLOOR_WG").contains(heightmap)) {
            throw new IllegalArgumentException("heightmap must be WORLD_SURFACE_WG or OCEAN_FLOOR_WG");
        }
        String rotation = string(json, "rotation", "random");
        if (!Set.of("random", "none", "clockwise_90", "clockwise_180", "counterclockwise_90").contains(rotation)) {
            throw new IllegalArgumentException("unsupported rotation: " + rotation);
        }
        JsonObject conditions = object(json, "conditions");
        keys(conditions, Set.of("min_y", "max_y", "max_terrain_difference", "avoid_fluids", "ground_blocks"));
        int minY = integer(conditions, "min_y", -64, -2032, 2031);
        int maxY = integer(conditions, "max_y", 320, -2032, 2031);
        if (minY > maxY) throw new IllegalArgumentException("min_y must be <= max_y");
        return new StructureDefinition(name, bool(json, "enabled", true), template, biomes, spacing, separation, salt, frequency,
                heightmap, integer(json, "y_offset", 0, -256, 256), rotation,
                bool(json, "include_entities", false),
                new Conditions(minY, maxY, integer(conditions, "max_terrain_difference", 8, 0, 512),
                        bool(conditions, "avoid_fluids", true), identifiers(conditions, "ground_blocks")));
    }

    /** Both lexical traversal and symlink escapes are rejected. */
    public Path resolveTemplate(Path directory) throws java.io.IOException {
        Path root = directory.toRealPath();
        Path relative = Path.of(template);
        if (relative.isAbsolute() || template.contains("\\") || template.contains(":")) {
            throw new IllegalArgumentException("template must be a relative path using /");
        }
        Path candidate = root.resolve(relative).normalize();
        if (!candidate.startsWith(root)) throw new IllegalArgumentException("template escapes templates directory");
        Path real = candidate.toRealPath();
        if (!real.startsWith(root)) throw new IllegalArgumentException("template symlink escapes templates directory");
        return real;
    }

    public static boolean bool(JsonObject object, String key, boolean fallback) {
        if (!object.has(key)) return fallback;
        JsonElement value = object.get(key);
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isBoolean()) {
            throw new IllegalArgumentException(key + " must be a boolean");
        }
        return value.getAsBoolean();
    }

    private static void keys(JsonObject object, Set<String> allowed) {
        for (String key : object.keySet()) {
            if (!allowed.contains(key)) throw new IllegalArgumentException("unknown option: " + key);
        }
    }

    private static JsonObject object(JsonObject parent, String key) {
        if (!parent.has(key)) return new JsonObject();
        if (!parent.get(key).isJsonObject()) throw new IllegalArgumentException(key + " must be an object");
        return parent.getAsJsonObject(key);
    }

    private static String string(JsonObject object, String key, String fallback) {
        if (!object.has(key)) {
            if (fallback == null) throw new IllegalArgumentException("missing option: " + key);
            return fallback;
        }
        JsonElement value = object.get(key);
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException(key + " must be a string");
        }
        return value.getAsString();
    }

    private static double number(JsonObject object, String key, double fallback) {
        if (!object.has(key)) return fallback;
        JsonElement value = object.get(key);
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException(key + " must be a number");
        }
        return value.getAsDouble();
    }

    private static int integer(JsonObject object, String key, int fallback, int min, int max) {
        double value = number(object, key, fallback);
        if (!Double.isFinite(value) || value != Math.rint(value) || value < min || value > max) {
            throw new IllegalArgumentException(key + " must be an integer in [" + min + ", " + max + "]");
        }
        return (int) value;
    }

    private static List<String> identifiers(JsonObject object, String key) {
        if (!object.has(key)) return List.of();
        if (!object.get(key).isJsonArray()) throw new IllegalArgumentException(key + " must be an array");
        List<String> values = new ArrayList<>();
        for (JsonElement entry : object.getAsJsonArray(key)) {
            if (!entry.isJsonPrimitive() || !entry.getAsJsonPrimitive().isString()
                    || !entry.getAsString().matches("#?[a-z0-9_.-]+:[a-z0-9/._-]+")) {
                throw new IllegalArgumentException(key + " entries must be namespaced IDs or #tags");
            }
            values.add(entry.getAsString());
        }
        return List.copyOf(values);
    }
}
