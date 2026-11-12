package org.polaris2023.wildwind.core.debug;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 开发期调试导出：把本模组全部方块 / 物品 / 生物的属性写成文本报告。
 * <p>
 * 由 {@code debug_all.bat} 触发：启动一个服务端 run，在 {@link ServerStartedEvent} 时读取注册表、
 * 标签与配方的真实数据，导出到 {@code -Dwildwind.debug.export.dir} 指定的目录，然后关闭服务端。
 *
 * @author safe_saffawa
 */
public final class ModDebugExporter {
    public static final String OUTPUT_DIR_PROPERTY = "wildwind.debug.export.dir";
    public static final String EXIT_PROPERTY = "wildwind.debug.export.exit";
    public static final String NAMESPACES_PROPERTY = "wildwind.debug.export.namespaces";

    private static final String DEFAULT_NAMESPACES = "ww_core,ww_wilderness,ww_barren";
    private static final Logger LOGGER = LoggerFactory.getLogger("WildWind/DebugExport");
    private static final Field FORCE_SOLID_ON = lookupForceSolidOn();

    private static final Map<PushReaction, String> PUSH_REACTION_NAMES = Map.of(
            PushReaction.NORMAL, "正常",
            PushReaction.DESTROY, "破坏",
            PushReaction.BLOCK, "阻挡",
            PushReaction.IGNORE, "忽略",
            PushReaction.PUSH_ONLY, "仅推动"
    );

    private static final Map<NoteBlockInstrument, String> INSTRUMENT_NAMES = Map.ofEntries(
            Map.entry(NoteBlockInstrument.HARP, "竖琴/钢琴"),
            Map.entry(NoteBlockInstrument.BASEDRUM, "底鼓"),
            Map.entry(NoteBlockInstrument.SNARE, "小鼓"),
            Map.entry(NoteBlockInstrument.HAT, "踩镲"),
            Map.entry(NoteBlockInstrument.BASS, "贝斯"),
            Map.entry(NoteBlockInstrument.FLUTE, "长笛"),
            Map.entry(NoteBlockInstrument.BELL, "铃铛"),
            Map.entry(NoteBlockInstrument.GUITAR, "吉他"),
            Map.entry(NoteBlockInstrument.CHIME, "管钟"),
            Map.entry(NoteBlockInstrument.XYLOPHONE, "木琴"),
            Map.entry(NoteBlockInstrument.IRON_XYLOPHONE, "铁木琴"),
            Map.entry(NoteBlockInstrument.COW_BELL, "牛铃"),
            Map.entry(NoteBlockInstrument.DIDGERIDOO, "迪吉里杜管"),
            Map.entry(NoteBlockInstrument.BIT, "方波"),
            Map.entry(NoteBlockInstrument.BANJO, "班卓琴"),
            Map.entry(NoteBlockInstrument.PLING, "颤音")
    );

    public static void onServerStarted(ServerStartedEvent event) {
        String outputDir = System.getProperty(OUTPUT_DIR_PROPERTY);
        if (outputDir == null || outputDir.isBlank()) {
            return;
        }
        MinecraftServer server = event.getServer();
        try {
            export(server, Path.of(outputDir));
        } catch (Exception e) {
            LOGGER.error("导出模组属性失败", e);
        } finally {
            if (Boolean.parseBoolean(System.getProperty(EXIT_PROPERTY, "false"))) {
                server.halt(false);
            }
        }
    }

    private static void export(MinecraftServer server, Path outputDir) throws Exception {
        Set<String> namespaces = namespaces();
        Map<String, String> zh = loadLang(namespaces, "zh_cn");
        Map<String, String> en = loadLang(namespaces, "en_us");
        Map<Item, Integer> smeltingTimes = collectSmeltingTimes(server);
        Set<Item> recipeOutputs = collectRecipeOutputs(server);
        Map<Item, Identifier> sulfurArchetypes = collectSulfurArchetypes(server);

        List<String> blocks = exportBlocks(server, namespaces, zh, en, smeltingTimes, recipeOutputs, sulfurArchetypes);
        List<String> items = exportItems(namespaces, zh, en, smeltingTimes, recipeOutputs, sulfurArchetypes);
        List<String> entities = exportEntities(server.overworld(), namespaces, zh, en);

        String header = header(namespaces, blocks.size(), items.size(), entities.size());
        Files.createDirectories(outputDir);
        write(outputDir.resolve("blocks.txt"), header + String.join("", blocks));
        write(outputDir.resolve("items.txt"), header + String.join("", items));
        write(outputDir.resolve("entities.txt"), header + String.join("", entities));
        write(outputDir.resolve("debug_all.txt"),
                header
                        + "\n########## 方块（" + blocks.size() + "）##########\n\n" + String.join("", blocks)
                        + "\n########## 物品（" + items.size() + "）##########\n\n" + String.join("", items)
                        + "\n########## 生物（" + entities.size() + "）##########\n\n" + String.join("", entities));

        LOGGER.info("已导出 {} 个方块 / {} 个物品 / {} 个生物 -> {}", blocks.size(), items.size(), entities.size(),
                outputDir.toAbsolutePath());
    }

    private static List<String> exportBlocks(MinecraftServer server, Set<String> namespaces,
                                             Map<String, String> zh, Map<String, String> en,
                                             Map<Item, Integer> smeltingTimes, Set<Item> recipeOutputs,
                                             Map<Item, Identifier> sulfurArchetypes) {
        Map<String, Block> sorted = new TreeMap<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            if (namespaces.contains(id.getNamespace())) {
                sorted.put(id.toString(), block);
            }
        }

        List<String> result = new ArrayList<>(sorted.size());
        BlockPos pos = BlockPos.ZERO;
        ServerLevel level = server.overworld();
        for (Map.Entry<String, Block> entry : sorted.entrySet()) {
            Block block = entry.getValue();
            Item item = block.asItem();
            boolean hasItem = item != Items.AIR;

            BlockState state = block.defaultBlockState();
            StringBuilder sb = new StringBuilder(512);
            sb.append("===== ").append(entry.getKey()).append(" =====\n");
            line(sb, "物品中文名（英文名）", name(item, block, zh, en));
            line(sb, "合适挖掘工具", miningTools(block));
            line(sb, "硬度", destroyTime(block));
            line(sb, "爆炸抗性", num(state.getExplosionResistance(level, pos, null)));
            line(sb, "引燃几率", String.valueOf(state.getFireSpreadSpeed(level, pos, Direction.UP)));
            line(sb, "烧毁几率", String.valueOf(state.getFlammability(level, pos, Direction.UP)));
            line(sb, "熔岩可燃性", yesNo(state.ignitedByLava(level, pos, Direction.UP)));
            line(sb, "窒息生物", yesNo(state.isSuffocating(level, pos)));
            line(sb, "红石导体", yesNo(state.isRedstoneConductor(level, pos)));
            // 红石线正上方放这个方块时红石线会不会被压断：判定位置用的是线所在的那一格
            line(sb, "压断红石线", yesNo(state.isRedstoneConductor(level, pos.below())));
            line(sb, "固体方块", yesNo(isSolid(state, level, pos)));
            line(sb, "活塞推动行为", pushReaction(state.getPistonPushReaction()));
            line(sb, "乐器", instrument(state.instrument()));
            line(sb, "方块ID", entry.getKey());
            line(sb, "烧炼时间", hasItem ? smeltingTime(smeltingTimes.get(item)) : "-");
            line(sb, "硫方怪原型", hasItem ? sulfurArchetype(sulfurArchetypes.get(item)) : "无");
            line(sb, "最大堆叠", hasItem ? String.valueOf(item.getDefaultMaxStackSize()) : "-");
            line(sb, "可再生", hasItem ? yesNo(recipeOutputs.contains(item)) : "-");
            line(sb, "物品ID", hasItem ? BuiltInRegistries.ITEM.getKey(item).toString() : "-");
            sb.append('\n');
            result.add(sb.toString());
        }
        return result;
    }

    private static List<String> exportItems(Set<String> namespaces, Map<String, String> zh, Map<String, String> en,
                                            Map<Item, Integer> smeltingTimes, Set<Item> recipeOutputs,
                                            Map<Item, Identifier> sulfurArchetypes) {
        Map<String, Item> sorted = new TreeMap<>();
        for (Item item : BuiltInRegistries.ITEM) {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            if (namespaces.contains(id.getNamespace())) {
                sorted.put(id.toString(), item);
            }
        }

        List<String> result = new ArrayList<>(sorted.size());
        for (Map.Entry<String, Item> entry : sorted.entrySet()) {
            Item item = entry.getValue();
            StringBuilder sb = new StringBuilder(256);
            sb.append("===== ").append(entry.getKey()).append(" =====\n");
            line(sb, "物品中文名（英文名）", name(item, null, zh, en));
            line(sb, "方块ID", item instanceof BlockItem blockItem
                    ? BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).toString() : "-");
            line(sb, "物品ID", entry.getKey());
            line(sb, "最大堆叠", String.valueOf(item.getDefaultMaxStackSize()));
            line(sb, "烧炼时间", smeltingTime(smeltingTimes.get(item)));
            line(sb, "硫方怪原型", sulfurArchetype(sulfurArchetypes.get(item)));
            line(sb, "可再生", yesNo(recipeOutputs.contains(item)));
            sb.append('\n');
            result.add(sb.toString());
        }
        return result;
    }

    private static List<String> exportEntities(ServerLevel level, Set<String> namespaces, Map<String, String> zh, Map<String, String> en) {
        Map<String, EntityType<?>> sorted = new TreeMap<>();
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if (namespaces.contains(id.getNamespace())) {
                sorted.put(id.toString(), type);
            }
        }

        List<String> result = new ArrayList<>(sorted.size());
        for (Map.Entry<String, EntityType<?>> entry : sorted.entrySet()) {
            EntityType<?> type = entry.getValue();
            StringBuilder sb = new StringBuilder(384);
            sb.append("===== ").append(entry.getKey()).append(" =====\n");
            line(sb, "生物中文名（英文名）", localized(type.getDescriptionId(), zh, en));
            line(sb, "实体ID", entry.getKey());
            line(sb, "分类", type.getCategory().toString());
            line(sb, "尺寸（宽×高×眼高）", num(type.getWidth()) + " × " + num(type.getHeight())
                    + " × " + num(type.getDimensions().eyeHeight()));
            if (DefaultAttributes.hasSupplier(type)) {
                Entity entity = type.create(level, EntitySpawnReason.COMMAND);
                if (entity instanceof LivingEntity living) {
                    try {
                        attribute(sb, living, "生命值", Attributes.MAX_HEALTH);
                        attribute(sb, living, "攻击力", Attributes.ATTACK_DAMAGE);
                        attribute(sb, living, "护甲", Attributes.ARMOR);
                        attribute(sb, living, "移动速度", Attributes.MOVEMENT_SPEED);
                        attribute(sb, living, "跟随范围", Attributes.FOLLOW_RANGE);
                        attribute(sb, living, "击退抗性", Attributes.KNOCKBACK_RESISTANCE);
                    } finally {
                        entity.discard();
                    }
                }
            }
            sb.append('\n');
            result.add(sb.toString());
        }
        return result;
    }

    private static void attribute(StringBuilder sb, LivingEntity entity, String label, Holder<Attribute> attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        line(sb, label, instance == null ? "-" : num(instance.getValue()));
    }

    private static String header(Set<String> namespaces, int blocks, int items, int entities) {
        StringJoiner joiner = new StringJoiner(", ");
        namespaces.stream().sorted().forEach(joiner::add);
        return """
                # 原野之风（Wild Wind）属性导出
                # 生成时间: %s
                # 模块: %s
                # 数量: 方块 %d / 物品 %d / 生物 %d
                # 说明: 数值来自游戏的注册表、标签与配方；"可再生"按「该物品能由某个配方产出」判定，仅供参考。

                """.formatted(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
                joiner, blocks, items, entities);
    }

    private static Set<String> namespaces() {
        String raw = System.getProperty(NAMESPACES_PROPERTY, DEFAULT_NAMESPACES);
        Set<String> result = new HashSet<>();
        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private static Map<String, String> loadLang(Set<String> namespaces, String code) {
        Map<String, String> merged = new HashMap<>();
        for (String namespace : namespaces) {
            String relative = "assets/" + namespace + "/lang/" + code + ".json";
            try (InputStream stream = openResource(relative)) {
                if (stream == null) {
                    continue;
                }
                try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                        merged.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("读取语言文件 {} 失败: {}", relative, e.toString());
            }
        }
        return merged;
    }

    /**
     * 其他模组的资源不在本类所在类加载器的可见范围里，所以这里依次尝试几种查找方式。
     */
    private static InputStream openResource(String relative) throws Exception {
        InputStream stream = ModDebugExporter.class.getResourceAsStream("/" + relative);
        if (stream != null) {
            return stream;
        }
        ClassLoader system = ClassLoader.getSystemClassLoader();
        stream = system == null ? null : system.getResourceAsStream(relative);
        if (stream != null) {
            return stream;
        }
        ClassLoader context = Thread.currentThread().getContextClassLoader();
        stream = context == null ? null : context.getResourceAsStream(relative);
        if (stream != null) {
            return stream;
        }
        for (String entry : System.getProperty("java.class.path", "").split(File.pathSeparator)) {
            if (entry.isBlank()) {
                continue;
            }
            Path path = Path.of(entry);
            if (Files.isDirectory(path)) {
                Path file = path.resolve(relative);
                if (Files.isRegularFile(file)) {
                    return Files.newInputStream(file);
                }
            } else if (Files.isRegularFile(path)) {
                try (ZipFile zip = new ZipFile(path.toFile())) {
                    ZipEntry zipEntry = zip.getEntry(relative);
                    if (zipEntry != null) {
                        return new ByteArrayInputStream(zip.getInputStream(zipEntry).readAllBytes());
                    }
                }
            }
        }
        return null;
    }

    private static Map<Item, Integer> collectSmeltingTimes(MinecraftServer server) {
        Map<Item, Integer> result = new LinkedHashMap<>();
        for (RecipeHolder<SmeltingRecipe> holder : server.getRecipeManager().recipeMap().byType(RecipeType.SMELTING)) {
            int time = holder.value().cookingTime();
            holder.value().input().getValues().stream().forEach(item -> result.putIfAbsent(item.value(), time));
        }
        return result;
    }

    private static Set<Item> collectRecipeOutputs(MinecraftServer server) {
        Set<Item> outputs = new HashSet<>();
        for (RecipeHolder<?> holder : server.getRecipeManager().recipeMap().values()) {
            try {
                for (RecipeDisplay display : holder.value().display()) {
                    collectOutputs(display.result(), outputs);
                }
            } catch (Exception ignored) {
                // 少数配方在服务端生成展示数据时会失败，跳过即可
            }
        }
        return outputs;
    }

    private static void collectOutputs(SlotDisplay display, Set<Item> outputs) {
        switch (display) {
            case SlotDisplay.ItemStackSlotDisplay stack -> {
                Item item = stack.stack().item().value();
                if (item != Items.AIR) {
                    outputs.add(item);
                }
            }
            case SlotDisplay.ItemSlotDisplay item -> outputs.add(item.item().value());
            case SlotDisplay.Composite composite -> composite.contents().forEach(child -> collectOutputs(child, outputs));
            case SlotDisplay.WithRemainder remainder -> collectOutputs(remainder.input(), outputs);
            case SlotDisplay.OnlyWithComponent component -> collectOutputs(component.source(), outputs);
            case SlotDisplay.TagSlotDisplay tag -> resolveTag(tag.tag()).forEach(outputs::add);
            default -> {
            }
        }
    }

    private static List<Item> resolveTag(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.get(tag)
                .map(set -> set.stream().map(Holder::value).toList())
                .orElse(List.of());
    }

    private static Map<Item, Identifier> collectSulfurArchetypes(MinecraftServer server) {
        Map<Item, Identifier> result = new HashMap<>();
        for (Map.Entry<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype> entry
                : server.registryAccess().lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE).entrySet()) {
            Identifier id = entry.getKey().identifier();
            for (Holder<Item> item : entry.getValue().items()) {
                result.putIfAbsent(item.value(), id);
            }
        }
        return result;
    }

    private static String name(Item item, Block block, Map<String, String> zh, Map<String, String> en) {
        if (item != null && item != Items.AIR) {
            return localized(item.getDescriptionId(), zh, en);
        }
        if (block != null) {
            return localized(block.getDescriptionId(), zh, en);
        }
        return "-";
    }

    private static String localized(String descriptionId, Map<String, String> zh, Map<String, String> en) {
        String english = en.getOrDefault(descriptionId, descriptionId);
        String chinese = zh.getOrDefault(descriptionId, english);
        return chinese + "（" + english + "）";
    }

    private static String miningTools(Block block) {
        BlockState state = block.defaultBlockState();
        StringJoiner joiner = new StringJoiner("、");
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            joiner.add("镐");
        }
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            joiner.add("斧");
        }
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            joiner.add("锹");
        }
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
            joiner.add("锄");
        }
        if (state.is(BlockTags.SHEARS_MAJOR_BREAKING_SPEED) || state.is(BlockTags.SHEARS_MINOR_BREAKING_SPEED)) {
            joiner.add("剪刀");
        }
        if (state.is(BlockTags.SWORD_EFFICIENT)) {
            joiner.add("剑");
        }
        String tools = joiner.toString();
        return tools.isEmpty() ? "无" : tools;
    }

    /**
     * 复刻原版 {@code BlockStateBase#calculateSolid}：{@code isSolid()}、{@code blocksMotion()} 以及它们依赖的
     * {@code legacySolid} / {@code forceSolidOff} 字段在 26.2 全部被弃用，只剩 {@code forceSolidOn} 这个开关还在用，
     * 而它是 {@code Properties} 里的私有字段、没有公开读取途径，所以这里用反射取一次。
     */
    private static boolean isSolid(BlockState state, ServerLevel level, BlockPos pos) {
        if (forceSolidOn(state.getBlock())) {
            return true;
        }
        VoxelShape shape = state.getCollisionShape(level, pos);
        if (shape.isEmpty()) {
            return false;
        }
        AABB bounds = shape.bounds();
        return bounds.getSize() >= 0.7291666666666666 || bounds.getYsize() >= 1.0;
    }

    private static boolean forceSolidOn(Block block) {
        if (FORCE_SOLID_ON == null) {
            return false;
        }
        try {
            return FORCE_SOLID_ON.getBoolean(block.properties());
        } catch (IllegalAccessException e) {
            LOGGER.warn("读取 Properties#forceSolidOn 失败: {}", e.toString());
            return false;
        }
    }

    private static Field lookupForceSolidOn() {
        try {
            Field field = BlockBehaviour.Properties.class.getDeclaredField("forceSolidOn");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException | RuntimeException e) {
            LOGGER.warn("找不到 Properties#forceSolidOn，固体方块将只按碰撞箱判定: {}", e.toString());
            return null;
        }
    }

    private static String destroyTime(Block block) {
        float time = block.defaultDestroyTime();
        return time < 0 ? "不可破坏（" + num(time) + "）" : num(time);
    }

    private static String pushReaction(PushReaction reaction) {
        return PUSH_REACTION_NAMES.getOrDefault(reaction, reaction.name()) + " (" + reaction.name() + ")";
    }

    private static String instrument(NoteBlockInstrument instrument) {
        if (instrument == null) {
            return "无";
        }
        return INSTRUMENT_NAMES.getOrDefault(instrument, instrument.name()) + " (" + instrument.getSerializedName() + ")";
    }

    private static String smeltingTime(Integer ticks) {
        return ticks == null ? "-" : num(ticks / 20.0) + " 秒（" + ticks + " tick）";
    }

    private static String sulfurArchetype(Identifier archetype) {
        return archetype == null ? "无" : archetype.toString();
    }

    private static String yesNo(boolean value) {
        return value ? "是" : "否";
    }

    private static String num(double value) {
        if (!Double.isFinite(value)) {
            return String.valueOf(value);
        }
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format(Locale.ROOT, "%.3f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    private static void line(StringBuilder sb, String label, String value) {
        sb.append(label).append(": ").append(value).append('\n');
    }

    private static void write(Path path, String content) throws Exception {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            writer.write(content);
        }
    }

    private ModDebugExporter() {
    }
}
