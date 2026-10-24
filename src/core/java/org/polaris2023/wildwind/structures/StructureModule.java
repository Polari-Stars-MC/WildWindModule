package org.polaris2023.wildwind.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.structures.command.StructureCommands;
import org.polaris2023.wildwind.structures.config.ConfigStructurePack;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredStructure;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredTemplatePiece;
import org.polaris2023.wildwind.structures.worldgen.GeneratedStructureIndex;

public final class StructureModule {
    // Keep registry and saved-data IDs compatible with the former standalone module.
    public static final String NAMESPACE = "ww_structures";
    private static final DeferredRegister<StructureType<?>> TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, NAMESPACE);
    private static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, NAMESPACE);
    public static final DeferredHolder<StructureType<?>, StructureType<ConfiguredStructure>> STRUCTURE =
            TYPES.register("configured", () -> () -> ConfiguredStructure.CODEC);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> PIECE =
            PIECES.register("template", () -> ConfiguredTemplatePiece::new);

    private StructureModule() {}

    public static void register(IEventBus bus) {
        TYPES.register(bus);
        PIECES.register(bus);
        bus.addListener(ConfigStructurePack::addPack);
        NeoForge.EVENT_BUS.addListener(StructureCommands::register);
        NeoForge.EVENT_BUS.addListener(GeneratedStructureIndex::onChunkLoad);
    }
}
