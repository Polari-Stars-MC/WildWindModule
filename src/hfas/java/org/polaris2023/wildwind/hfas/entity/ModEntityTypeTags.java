package org.polaris2023.wildwind.hfas.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.polaris2023.wildwind.hfas.HFASMod;

/** Exact entity-type lists, shared by the server and client through data-pack tags. */
public final class ModEntityTypeTags {
    public static final TagKey<EntityType<?>> WATER_BREATHING = create("water_breathing");
    public static final TagKey<EntityType<?>> SINK_AFFECTED = create("sink_affected");
    public static final TagKey<EntityType<?>> CHOKE_AFFECTED = create("choke_affected");

    private ModEntityTypeTags() {}

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, name));
    }
}
