package org.polaris2023.wildwind.core.inspection;

import static org.polaris2023.wildwind.core.inspection.InspectionText.*;
import net.minecraft.network.chat.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.material.PushReaction;
import org.polaris2023.wildwind.core.CoreMod;

/** Read-only snapshots; never reads container contents, private NBT or executes recipes. */
public final class InspectionReport {
    public static final TagKey<Item> RENEWABLE_ITEMS = TagKey.create(Registries.ITEM, CoreMod.id("inspection/renewable"));
    public static final TagKey<Item> NON_RENEWABLE_ITEMS = TagKey.create(Registries.ITEM, CoreMod.id("inspection/non_renewable"));
    public static final TagKey<EntityType<?>> RENEWABLE_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, CoreMod.id("inspection/renewable"));
    private static final List<String> BLOCK_FIELDS = List.of("tools", "hardness", "blast_resistance", "fire_spread", "flammability", "ignited_by_lava",
            "suffocating", "redstone_conductor", "blocks_redstone", "solid", "piston", "instrument", "block_id");
    private InspectionReport() {}

    public static List<Component> block(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var rows = header("block", state.getBlock().getName(), level, pos);
        rows.add(field("tools", tools(state, level, pos)));
        rows.add(field("hardness", number(state.getDestroySpeed(level, pos))));
        rows.add(field("blast_resistance", number(state.getBlock().getExplosionResistance())));
        rows.add(field("fire_spread", fireValues(face -> state.getFireSpreadSpeed(level, pos, face))));
        rows.add(field("flammability", fireValues(face -> state.getFlammability(level, pos, face))));
        rows.add(field("ignited_by_lava", flag(Arrays.stream(Direction.values()).anyMatch(face -> state.ignitedByLava(level, pos, face)))));
        rows.add(field("suffocating", flag(state.isSuffocating(level, pos))));
        rows.add(field("redstone_conductor", flag(state.isRedstoneConductor(level, pos))));
        // Vanilla dust checks the conductor above it when connecting up a block step.
        rows.add(field("blocks_redstone", flag(state.isRedstoneConductor(level, pos))));
        rows.add(field("solid", flag(state.isSolid())));
        rows.add(field("piston", piston(state, level, pos)));
        rows.add(field("instrument", instrument(state.instrument().getSerializedName())));
        rows.add(field("block_id", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString()));
        var stack = state.getBlock().asItem().getDefaultInstance();
        appendItem(rows, level, stack);
        rows.add(field("burnable", flag(Arrays.stream(Direction.values()).anyMatch(face -> state.isFlammable(level, pos, face)))));
        rows.add(field("requires_tool", flag(state.requiresCorrectToolForDrops())));
        rows.add(field("block_state", ""));
        var values = state.getValues().map(Object::toString).toList();
        rows.addAll(values.isEmpty() ? List.of(UNKNOWN) : values.stream().map(Component::literal).toList());
        if (!stack.isEmpty()) SulfurProfile.of(level, stack).appendPhysics(rows);
        return List.copyOf(rows);
    }

    public static List<Component> entity(ServerLevel level, Entity target) {
        var rows = header("entity", target.getName(), level, target.blockPosition());
        rows.add(field("entity_id", BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).toString()));
        // Keep the requested schema; block properties do not describe a mob or dropped item.
        for (var key : BLOCK_FIELDS) rows.add(field(key, UNKNOWN));
        var stack = target instanceof ItemEntity item ? item.getItem() : ItemStack.EMPTY;
        if (target instanceof SulfurCube cube) stack = cube.getItemBySlot(EquipmentSlot.BODY);
        appendItem(rows, level, stack);
        if (stack.isEmpty() && target.getType().builtInRegistryHolder().is(RENEWABLE_ENTITIES)) {
            rows.set(rows.indexOf(field("renewable", UNKNOWN)), field("renewable", text("yes")));
        }
        rows.add(field("width", number(target.getBbWidth())));
        rows.add(field("height", number(target.getBbHeight())));
        rows.add(field("fire_immune", flag(target.fireImmune())));
        if (target instanceof LivingEntity living) {
            rows.add(field("health", number(living.getHealth())));
            rows.add(field("max_health", number(living.getMaxHealth())));
            rows.add(field("armor", attribute(living, Attributes.ARMOR)));
            rows.add(field("movement_speed", attribute(living, Attributes.MOVEMENT_SPEED)));
            rows.add(field("attack_damage", attribute(living, Attributes.ATTACK_DAMAGE)));
            rows.add(field("knockback_resistance", attribute(living, Attributes.KNOCKBACK_RESISTANCE)));
            rows.add(field("air", living.getAirSupply()));
            rows.add(field("max_air", living.getMaxAirSupply()));
        } else {
            for (var key : List.of("health", "max_health", "armor", "movement_speed", "attack_damage", "knockback_resistance", "air", "max_air")) rows.add(field(key, UNKNOWN));
        }
        if (target instanceof SulfurCube) {
            rows.add(field("absorbed_item", (stack.isEmpty() ? "×" : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())));
            if (stack.isEmpty()) rows.set(rows.indexOf(field("sulfur_archetype", UNKNOWN)), field("sulfur_archetype", text("default")));
            SulfurProfile.of(level, stack).appendPhysics(rows);
        }
        return List.copyOf(rows);
    }

    private static ArrayList<Component> header(String kind, Component name, ServerLevel level, BlockPos pos) {
        return new ArrayList<>(List.of(text("heading"), field(kind, name),
                field("dimension", level.dimension().identifier().toString()), field("position", pos.getX() + "," + pos.getY() + "," + pos.getZ())));
    }

    private static void appendItem(List<Component> rows, ServerLevel level, ItemStack stack) {
        int fuel = stack.isEmpty() ? 0 : stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
        rows.add(field("fuel_time", (fuel > 0 ? time(fuel) : UNKNOWN)));
        rows.add(field("sulfur_archetype", (stack.isEmpty() ? UNKNOWN : SulfurProfile.of(level, stack).name())));
        rows.add(field("max_stack", (stack.isEmpty() ? "×" : stack.getMaxStackSize())));
        rows.add(field("renewable", (stack.is(NON_RENEWABLE_ITEMS) ? text("no") : stack.is(RENEWABLE_ITEMS) ? text("yes") : UNKNOWN)));
        rows.add(field("item_id", (stack.isEmpty() ? "×" : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())));
        var recipe = stack.isEmpty() ? UNKNOWN : level.recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level)
                .map(holder -> time(holder.value().cookingTime())).orElse(UNKNOWN);
        rows.add(field("cooking_time", recipe));
    }

    public static Component piston(BlockState state, ServerLevel level, BlockPos pos) {
        boolean canPush = Arrays.stream(Direction.values()).anyMatch(d -> PistonBaseBlock.isPushable(state, level, pos, d, true, d));
        if (!canPush) return text("piston.block");
        if (state.getPistonPushReaction() == PushReaction.DESTROY) return text("piston.destroy");
        boolean canPull = Arrays.stream(Direction.values()).anyMatch(d -> PistonBaseBlock.isPushable(state, level, pos, d, false, d.getOpposite()));
        return canPull ? text("piston.normal") : text("piston.push_only");
    }

    private static Component tools(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getDestroySpeed(level, pos) < 0) return UNKNOWN;
        var names = new ArrayList<Component>();
        tool(names, text("tool.pickaxe"), state, Items.NETHERITE_PICKAXE);
        tool(names, text("tool.axe"), state, Items.NETHERITE_AXE);
        tool(names, text("tool.shovel"), state, Items.NETHERITE_SHOVEL);
        tool(names, text("tool.hoe"), state, Items.NETHERITE_HOE);
        tool(names, text("tool.shears"), state, Items.SHEARS);
        tool(names, text("tool.sword"), state, Items.NETHERITE_SWORD);
        return names.isEmpty() ? (state.requiresCorrectToolForDrops() ? UNKNOWN : text("tool.hand")) : join(names);
    }

    private static void tool(List<Component> names, Component name, BlockState state, Item item) {
        var stack = item.getDefaultInstance();
        if (stack.getDestroySpeed(state) > 1 && (!state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state))) names.add(name);
    }

    private static Component fireValues(ToIntFunction<Direction> getter) {
        int min = Arrays.stream(Direction.values()).mapToInt(getter).min().orElse(0);
        int max = Arrays.stream(Direction.values()).mapToInt(getter).max().orElse(0);
        if (max == 0) return UNKNOWN;
        return min == max ? Component.literal(Integer.toString(max)) : text("face_range", min, max);
    }

    private static Component instrument(String name) {
        return Component.translatableWithFallback("inspection.wild_wind_core.instrument." + name, name);
    }

    private static String attribute(LivingEntity entity, Holder<Attribute> key) {
        var instance = entity.getAttribute(key);
        return instance == null ? "×" : number(instance.getValue());
    }

    public static String number(double value) {
        return Double.isFinite(value) ? BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() : "×";
    }
    public static Component flag(boolean value) { return value ? text("yes") : UNKNOWN; }
    private static Component time(int ticks) { return text("time", number(ticks / 20.0), ticks); }
}
