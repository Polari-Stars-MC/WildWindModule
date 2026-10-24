package org.polaris2023.wildwind.structures.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.TreeSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.polaris2023.wildwind.structures.StructureModule;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredStructure;
import org.polaris2023.wildwind.structures.worldgen.GeneratedStructureIndex;

public final class StructureCommands {
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("wwstructure")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("list").executes(context -> {
                    var names = names(context.getSource());
                    context.getSource().sendSuccess(() -> Component.literal("结构名称（配置或当前维度历史记录）：" + String.join(", ", names)), false);
                    return names.size();
                }))
                .then(Commands.literal("locate").then(Commands.argument("name", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(names(context.getSource()), builder))
                        .executes(context -> locate(context.getSource(), StringArgumentType.getString(context, "name")))))
                .then(Commands.literal("search").then(Commands.argument("name", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(names(context.getSource()), builder))
                        .executes(context -> search(context.getSource(), StringArgumentType.getString(context, "name"), 16))
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, 64))
                                .executes(context -> search(context.getSource(), StringArgumentType.getString(context, "name"),
                                        IntegerArgumentType.getInteger(context, "radius")))))));
    }

    private static TreeSet<String> names(CommandSourceStack source) {
        TreeSet<String> names = new TreeSet<>();
        source.registryAccess().lookupOrThrow(Registries.STRUCTURE).listElements()
                .filter(holder -> holder.value() instanceof ConfiguredStructure)
                .forEach(holder -> names.add(holder.key().identifier().toString()));
        source.getLevel().getDataStorage().computeIfAbsent(GeneratedStructureIndex.TYPE).names()
                .forEach(id -> names.add(id.toString()));
        return names;
    }

    private static Identifier id(String name) {
        return Identifier.tryParse(name.contains(":") ? name : StructureModule.NAMESPACE + ":" + name);
    }

    private static int locate(CommandSourceStack source, String name) {
        Identifier id = id(name);
        if (id == null) { source.sendFailure(Component.literal("无效的结构名称：" + name)); return 0; }
        var found = source.getLevel().getDataStorage().computeIfAbsent(GeneratedStructureIndex.TYPE)
                .nearest(id, BlockPos.containing(source.getPosition()));
        if (found.isEmpty()) {
            source.sendFailure(Component.literal("当前维度没有该结构的已生成记录：" + id + "。可用 /wwstructure search " + name + " 寻找生成位置。"));
            return 0;
        }
        return report(source, id, found.get(), "最近的已生成结构");
    }

    private static int search(CommandSourceStack source, String name, int radius) {
        Identifier id = id(name);
        if (id == null) { source.sendFailure(Component.literal("无效的结构名称：" + name)); return 0; }
        var holder = source.registryAccess().lookupOrThrow(Registries.STRUCTURE).get(ResourceKey.create(Registries.STRUCTURE, id));
        if (holder.isEmpty() || !(holder.get().value() instanceof ConfiguredStructure)) {
            source.sendFailure(Component.literal("未启用或不存在的结构：" + id));
            return 0;
        }
        var level = source.getLevel();
        var found = level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(holder.get()),
                BlockPos.containing(source.getPosition()), radius, false);
        if (found == null) {
            source.sendFailure(Component.literal("搜索范围内未找到 " + id + "；请检查群系、生成条件和世界的结构生成开关。"));
            return 0;
        }
        return report(source, id, found.getFirst(), "原版搜索结果（可能尚未生成完整区块）");
    }

    private static int report(CommandSourceStack source, Identifier id, BlockPos position, String label) {
        int distance = (int) Math.sqrt(GeneratedStructureIndex.horizontalDistanceSquared(position, BlockPos.containing(source.getPosition())));
        source.sendSuccess(() -> Component.literal(label + " " + id + "： [" + position.getX() + ", " + position.getY() + ", "
                + position.getZ() + "]，维度 " + source.getLevel().dimension().identifier() + "，水平距离 " + distance + " 格"), false);
        return 1;
    }
}
