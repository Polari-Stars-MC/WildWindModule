package org.polaris2023.wildwind.hfas.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.Nullable;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.block.ModBlocks;
import org.polaris2023.wildwind.hfas.block.KilnBlock;

/** Inserts a kiln beside the workstation in each of the six vanilla mason-house templates. */
public record MasonKilnProcessor(BlockPos target, Direction facing, Block replacedBlock) implements StructureProcessor {
    public static final MapCodec<MasonKilnProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(MasonKilnProcessor::target),
            Direction.CODEC.fieldOf("facing").forGetter(MasonKilnProcessor::facing),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace").forGetter(MasonKilnProcessor::replacedBlock)
    ).apply(instance, MasonKilnProcessor::new));
    public static final DeferredRegister<MapCodec<? extends StructureProcessor>> PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, HFASMod.MOD_ID);
    static { PROCESSORS.register("mason_kiln", () -> CODEC); }

    public static final Map<Identifier, MasonKilnProcessor> HOUSES = Map.of(
            id("desert/houses/desert_mason_1"), new MasonKilnProcessor(new BlockPos(5, 1, 6), Direction.NORTH, Blocks.AIR),
            id("plains/houses/plains_masons_house_1"), new MasonKilnProcessor(new BlockPos(5, 1, 6), Direction.NORTH, Blocks.AIR),
            id("savanna/houses/savanna_mason_1"), new MasonKilnProcessor(new BlockPos(5, 1, 2), Direction.SOUTH, Blocks.AIR),
            id("snowy/houses/snowy_masons_house_1"), new MasonKilnProcessor(new BlockPos(6, 2, 5), Direction.WEST, Blocks.SPRUCE_PLANKS),
            id("snowy/houses/snowy_masons_house_2"), new MasonKilnProcessor(new BlockPos(5, 1, 2), Direction.SOUTH, Blocks.DIORITE_STAIRS),
            id("taiga/houses/taiga_masons_house_1"), new MasonKilnProcessor(new BlockPos(5, 2, 5), Direction.WEST, Blocks.AIR));

    private static Identifier id(String path) { return Identifier.withDefaultNamespace("village/" + path); }

    @Override
    public MapCodec<MasonKilnProcessor> codec() { return CODEC; }

    @Override
    public StructureTemplate.@Nullable StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos reference,
            StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo processed,
            StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        // Do not overwrite a chest/workstation if a data pack redesigns this template.
        if (!original.pos().equals(target) || !original.state().is(replacedBlock) || !processed.state().is(replacedBlock)) return processed;
        // StructureTemplate applies mirror/rotation to the state after processing; position is already transformed.
        return new StructureTemplate.StructureBlockInfo(processed.pos(),
                ModBlocks.KILN.get().defaultBlockState().setValue(KilnBlock.FACING, facing), null);
    }
}
