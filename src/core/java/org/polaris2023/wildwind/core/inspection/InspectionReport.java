package org.polaris2023.wildwind.core.inspection;

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
    private static final List<String> BLOCK_FIELDS = List.of("合适挖掘工具", "硬度", "爆炸抗性", "引燃几率", "烧毁几率", "熔岩可燃性",
            "窒息生物", "红石导体", "压断红石线", "固体方块", "活塞推动行为", "乐器", "方块ID");
    private InspectionReport() {}

    public static List<String> block(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var rows = header("方块", state.getBlock().getName().getString(), level, pos);
        rows.add("合适挖掘工具:" + tools(state, level, pos));
        rows.add("硬度:" + number(state.getDestroySpeed(level, pos)));
        rows.add("爆炸抗性:" + number(state.getBlock().getExplosionResistance()));
        rows.add("引燃几率:" + fireValues(face -> state.getFireSpreadSpeed(level, pos, face)));
        rows.add("烧毁几率:" + fireValues(face -> state.getFlammability(level, pos, face)));
        rows.add("熔岩可燃性:" + flag(Arrays.stream(Direction.values()).anyMatch(face -> state.ignitedByLava(level, pos, face))));
        rows.add("窒息生物:" + flag(state.isSuffocating(level, pos)));
        rows.add("红石导体:" + flag(state.isRedstoneConductor(level, pos)));
        // Vanilla dust checks the conductor above it when connecting up a block step.
        rows.add("压断红石线:" + flag(state.isRedstoneConductor(level, pos)));
        rows.add("固体方块:" + flag(state.isSolid()));
        rows.add("活塞推动行为:" + piston(state, level, pos));
        rows.add("乐器:" + instrument(state.instrument().getSerializedName()));
        rows.add("方块ID:" + BuiltInRegistries.BLOCK.getKey(state.getBlock()));
        var stack = state.getBlock().asItem().getDefaultInstance();
        appendItem(rows, level, stack);
        rows.add("是否可燃:" + flag(Arrays.stream(Direction.values()).anyMatch(face -> state.isFlammable(level, pos, face))));
        rows.add("需正确工具掉落:" + flag(state.requiresCorrectToolForDrops()));
        rows.add("方块状态:");
        var values = state.getValues().map(Object::toString).toList();
        rows.addAll(values.isEmpty() ? List.of("×") : values);
        if (!stack.isEmpty()) SulfurProfile.of(level, stack).appendPhysics(rows);
        return List.copyOf(rows);
    }

    public static List<String> entity(ServerLevel level, Entity target) {
        var rows = header("实体", target.getName().getString(), level, target.blockPosition());
        rows.add("实体ID:" + BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()));
        // Keep the requested schema; block properties do not describe a mob or dropped item.
        for (var field : BLOCK_FIELDS) rows.add(field + ":×");
        var stack = target instanceof ItemEntity item ? item.getItem() : ItemStack.EMPTY;
        if (target instanceof SulfurCube cube) stack = cube.getItemBySlot(EquipmentSlot.BODY);
        appendItem(rows, level, stack);
        if (stack.isEmpty() && target.getType().builtInRegistryHolder().is(RENEWABLE_ENTITIES)) {
            rows.set(rows.indexOf("可再生:×"), "可再生:是");
        }
        rows.add("宽度:" + number(target.getBbWidth()));
        rows.add("高度:" + number(target.getBbHeight()));
        rows.add("火焰免疫:" + flag(target.fireImmune()));
        if (target instanceof LivingEntity living) {
            rows.add("生命值:" + number(living.getHealth()));
            rows.add("最大生命值:" + number(living.getMaxHealth()));
            rows.add("护甲:" + attribute(living, Attributes.ARMOR));
            rows.add("移动速度:" + attribute(living, Attributes.MOVEMENT_SPEED));
            rows.add("攻击伤害:" + attribute(living, Attributes.ATTACK_DAMAGE));
            rows.add("击退抗性:" + attribute(living, Attributes.KNOCKBACK_RESISTANCE));
            rows.add("当前氧气:" + living.getAirSupply());
            rows.add("最大氧气:" + living.getMaxAirSupply());
        } else {
            for (var field : List.of("生命值", "最大生命值", "护甲", "移动速度", "攻击伤害", "击退抗性", "当前氧气", "最大氧气")) rows.add(field + ":×");
        }
        if (target instanceof SulfurCube) {
            rows.add("吸收物品ID:" + (stack.isEmpty() ? "×" : BuiltInRegistries.ITEM.getKey(stack.getItem())));
            if (stack.isEmpty()) rows.set(rows.indexOf("硫方怪原型:×"), "硫方怪原型:默认");
            SulfurProfile.of(level, stack).appendPhysics(rows);
        }
        return List.copyOf(rows);
    }

    private static ArrayList<String> header(String kind, String name, ServerLevel level, BlockPos pos) {
        return new ArrayList<>(List.of("原野之风·属性记录", kind + ":" + name,
                "维度:" + level.dimension().identifier(), "坐标:" + pos.getX() + "," + pos.getY() + "," + pos.getZ()));
    }

    private static void appendItem(List<String> rows, ServerLevel level, ItemStack stack) {
        int fuel = stack.isEmpty() ? 0 : stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
        rows.add("烧炼时间:" + (fuel > 0 ? time(fuel) : "×"));
        rows.add("硫方怪原型:" + (stack.isEmpty() ? "×" : SulfurProfile.of(level, stack).name()));
        rows.add("最大堆叠:" + (stack.isEmpty() ? "×" : stack.getMaxStackSize()));
        rows.add("可再生:" + (stack.is(NON_RENEWABLE_ITEMS) ? "否" : stack.is(RENEWABLE_ITEMS) ? "是" : "×"));
        rows.add("物品ID:" + (stack.isEmpty() ? "×" : BuiltInRegistries.ITEM.getKey(stack.getItem())));
        var recipe = stack.isEmpty() ? "×" : level.recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level)
                .map(holder -> time(holder.value().cookingTime())).orElse("×");
        rows.add("烧炼加工时间:" + recipe);
    }

    public static String piston(BlockState state, ServerLevel level, BlockPos pos) {
        boolean canPush = Arrays.stream(Direction.values()).anyMatch(d -> PistonBaseBlock.isPushable(state, level, pos, d, true, d));
        if (!canPush) return "不可推动，不可拉动";
        if (state.getPistonPushReaction() == PushReaction.DESTROY) return "被推动破坏，不可拉动";
        boolean canPull = Arrays.stream(Direction.values()).anyMatch(d -> PistonBaseBlock.isPushable(state, level, pos, d, false, d.getOpposite()));
        return canPull ? "可推动，可拉动" : "可推动，不可拉动";
    }

    private static String tools(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getDestroySpeed(level, pos) < 0) return "×";
        var names = new ArrayList<String>();
        tool(names, "镐", state, Items.NETHERITE_PICKAXE);
        tool(names, "斧", state, Items.NETHERITE_AXE);
        tool(names, "锹", state, Items.NETHERITE_SHOVEL);
        tool(names, "锄", state, Items.NETHERITE_HOE);
        tool(names, "剪刀", state, Items.SHEARS);
        tool(names, "剑", state, Items.NETHERITE_SWORD);
        return names.isEmpty() ? (state.requiresCorrectToolForDrops() ? "×" : "徒手") : String.join("、", names);
    }

    private static void tool(List<String> names, String name, BlockState state, Item item) {
        var stack = item.getDefaultInstance();
        if (stack.getDestroySpeed(state) > 1 && (!state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state))) names.add(name);
    }

    private static String fireValues(ToIntFunction<Direction> getter) {
        int min = Arrays.stream(Direction.values()).mapToInt(getter).min().orElse(0);
        int max = Arrays.stream(Direction.values()).mapToInt(getter).max().orElse(0);
        if (max == 0) return "×";
        return min == max ? Integer.toString(max) : min + "–" + max + "（随面）";
    }

    private static String instrument(String name) {
        return switch (name) {
            case "harp" -> "竖琴"; case "basedrum" -> "底鼓"; case "snare" -> "军鼓"; case "hat" -> "踩镲";
            case "bass" -> "贝斯"; case "flute" -> "长笛"; case "bell" -> "钟琴"; case "guitar" -> "吉他";
            case "chime" -> "管钟"; case "xylophone" -> "木琴"; case "iron_xylophone" -> "铁木琴";
            case "cow_bell" -> "牛铃"; case "didgeridoo" -> "迪吉里杜管"; case "bit" -> "方波";
            case "banjo" -> "班卓琴"; case "pling" -> "电钢琴";
            default -> name;
        };
    }

    private static String attribute(LivingEntity entity, Holder<Attribute> key) {
        var instance = entity.getAttribute(key);
        return instance == null ? "×" : number(instance.getValue());
    }

    public static String number(double value) {
        return Double.isFinite(value) ? BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() : "×";
    }
    public static String flag(boolean value) { return value ? "是" : "×"; }
    private static String time(int ticks) { return number(ticks / 20.0) + "秒（" + ticks + "刻）"; }
}
