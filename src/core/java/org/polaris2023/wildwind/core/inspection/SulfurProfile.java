package org.polaris2023.wildwind.core.inspection;

import java.util.List;
import static org.polaris2023.wildwind.core.inspection.InspectionText.*;
import net.minecraft.network.chat.Component;
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
    public static SulfurProfile of(ServerLevel level, ItemStack stack) {
        return new SulfurProfile(level.registryAccess().lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE)
                .listElements().filter(holder -> stack.is(holder.value().items())).toList(), !stack.isEmpty());
    }

    public Component name() {
        if (matches.isEmpty()) return text("default");
        return join(matches.stream().map(holder -> {
            var id = holder.key().identifier();
            return Component.translatableWithFallback("sulfur_cube_archetype." + id.toLanguageKey(), id.toString());
        }).toList());
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

    public void appendPhysics(List<Component> rows) {
        rows.add(text("physics"));
        rows.add(text("datapack"));
        rows.add(field("knockback_resistance", InspectionReport.number(attribute(Attributes.KNOCKBACK_RESISTANCE))));
        rows.add(field("bounciness", InspectionReport.number(attribute(Attributes.BOUNCINESS))));
        rows.add(field("friction", InspectionReport.number(attribute(Attributes.FRICTION_MODIFIER))));
        rows.add(field("air_drag", InspectionReport.number(attribute(Attributes.AIR_DRAG_MODIFIER))));
        var knockback = matches.isEmpty() ? SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS
                : matches.getLast().value().knockbackModifiers();
        rows.add(field("horizontal_knockback", InspectionReport.number(knockback.horizontalPower())));
        rows.add(field("vertical_knockback", InspectionReport.number(knockback.verticalPower())));
        rows.add(field("buoyant", InspectionReport.flag(matches.stream().anyMatch(h -> h.value().buoyant()))));
        // Vanilla playerPush derives this coefficient from resistance, only with an absorbed item.
        rows.add(field("push_scale", InspectionReport.number(hasAbsorbedItem ? Math.max(0, 1 - attribute(Attributes.KNOCKBACK_RESISTANCE)) : 0)));
    }
}
