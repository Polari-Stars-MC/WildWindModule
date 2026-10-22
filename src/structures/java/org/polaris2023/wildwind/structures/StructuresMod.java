package org.polaris2023.wildwind.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.polaris2023.wildwind.structures.command.StructureCommands;
import org.polaris2023.wildwind.structures.config.ConfigStructurePack;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredStructure;
import org.polaris2023.wildwind.structures.worldgen.ConfiguredTemplatePiece;
import org.polaris2023.wildwind.structures.worldgen.GeneratedStructureIndex;

@Mod(StructuresMod.MOD_ID)
public final class StructuresMod {
    public static final String MOD_ID = "ww_structures";
    private static final DeferredRegister<StructureType<?>> TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, MOD_ID);
    private static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, MOD_ID);
    public static final DeferredHolder<StructureType<?>, StructureType<ConfiguredStructure>> STRUCTURE =
            TYPES.register("configured", () -> () -> ConfiguredStructure.CODEC);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> PIECE =
            PIECES.register("template", () -> ConfiguredTemplatePiece::new);

    public StructuresMod(IEventBus bus) {
        TYPES.register(bus);
        PIECES.register(bus);
        bus.addListener(ConfigStructurePack::addPack);
        NeoForge.EVENT_BUS.addListener(StructureCommands::register);
        NeoForge.EVENT_BUS.addListener(GeneratedStructureIndex::onChunkLoad);
    }
}
