package org.polaris2023.wildwind.core.inspection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.ItemStack;

/** Uses the same item membership, modifier order and defaults as vanilla SulfurCube. */
public record SulfurProfile(List<Holder.Reference<SulfurCubeArchetype>> matches, boolean hasAbsorbedItem) {
    private static final Map<String, String> NAMES = Map.ofEntries(
            Map.entry("bouncy", "弹性（橡胶球）"), Map.entry("regular", "普通（足球）"),
            Map.entry("slow_flat", "缓慢平移（药球）"), Map.entry("fast_flat", "快速平移（高尔夫球）"),
            Map.entry("light", "轻盈（沙滩球）"), Map.entry("fast_sliding", "快速滑行（冰球）"),
            Map.entry("slow_sliding", "缓慢滑行（冰壶）"), Map.entry("slow_bouncy", "缓慢弹性"),
            Map.entry("sticky", "黏性"), Map.entry("high_resistance", "高阻力"),
            Map.entry("explosive", "爆炸"), Map.entry("hot", "高温"));

    public static SulfurProfile of(ServerLevel level, ItemStack stack) {
        return new SulfurProfile(level.registryAccess().lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE)
                .listElements().filter(holder -> stack.is(holder.value().items())).toList(), !stack.isEmpty());
    }

    public String name() {
        if (matches.isEmpty()) return "默认";
        return matches.stream().map(holder -> {
            var id = holder.key().identifier();
            return id.getNamespace().equals("minecraft") ? NAMES.getOrDefault(id.getPath(), id.toString()) : id.toString();
        }).collect(Collectors.joining("、"));
    }

    public double attribute(Holder<Attribute> attribute) {
        var instance = DefaultAttributes.getSupplier(EntityTypes.SULFUR_CUBE).createInstance(ignored -> {}, attribute);
        if (instance == null) return Double.NaN;
        for (var holder : matches) {
            for (var entry : holder.value().attributeModifiers()) {
                if (entry.attribute().equals(attribute)) instance.addOrUpdateTransientModifier(entry.modifier());
            }
        }
        return instance.getValue();
    }

    public void appendPhysics(List<String> rows) {
        rows.add("硫方怪物理参数");
        rows.add("（当前版本／数据包）");
        rows.add("击退抗性:" + InspectionReport.number(attribute(Attributes.KNOCKBACK_RESISTANCE)));
        rows.add("弹性:" + InspectionReport.number(attribute(Attributes.BOUNCINESS)));
        rows.add("摩擦力修正系数:" + InspectionReport.number(attribute(Attributes.FRICTION_MODIFIER)));
        rows.add("空气阻力修正系数:" + InspectionReport.number(attribute(Attributes.AIR_DRAG_MODIFIER)));
        var knockback = matches.isEmpty() ? SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS
                : matches.getLast().value().knockbackModifiers();
        rows.add("水平击退修正系数:" + InspectionReport.number(knockback.horizontalPower()));
        rows.add("垂直击退修正系数:" + InspectionReport.number(knockback.verticalPower()));
        rows.add("浮力:" + InspectionReport.flag(matches.stream().anyMatch(h -> h.value().buoyant())));
        // Vanilla playerPush derives this coefficient from resistance, only with an absorbed item.
        rows.add("推动幅度:" + InspectionReport.number(hasAbsorbedItem ? Math.max(0, 1 - attribute(Attributes.KNOCKBACK_RESISTANCE)) : 0));
    }
}
