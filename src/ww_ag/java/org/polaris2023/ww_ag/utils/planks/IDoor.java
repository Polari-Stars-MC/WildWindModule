package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/18 16:17:24
 */
public interface IDoor<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setDoor(BlockEntry<DoorBlock> entry);
    T setTrapDoor(BlockEntry<TrapDoorBlock> entry);

    default PlanksEntry<E> door(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<DoorBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        BlockBuilder<DoorBlock, L2Registrate> b = build.apply(self.registrate.block(self.name + "_door", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new DoorBlock(self.bst, properties1);
                }).tag(BlockTags.PLANKS))
                .blockstate((c, p) -> {
                    ResourceLocation blockTextures = c.getId().withPrefix("block/");
                    p.doorBlock(c.get(),
                            blockTextures.withSuffix("_bottom"),
                            blockTextures.withSuffix("_top"));
                }).item().model((c, p) -> {
                    p.generated(c, c.getId().withPrefix("item/"));
                }).build()
                .recipe((c, p) -> {
                    DataIngredient items = DataIngredient.items(self.planks.asItem());
                    RegistrateRecipeProvider.doorBuilder(c.get(), items.toVanilla())
                            .unlockedBy(p.safeName(c.getId()), items.getCriterion(p))
                            .save(p, p.safeId(c.getId()));
                });
        return setDoor(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"木门")
                .ww_ag$zh_tw(self.zhTw+"木門")
                .ww_ag$zh_hk(self.zhHk+"木門")
                .ww_ag$self()
                .lang(self.firstUpName() + " door").register());

    }
    default PlanksEntry<E> door(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<DoorBlock, L2Registrate>> build
    ) {
        return door(copy, __ -> {}, build);
    }
    default PlanksEntry<E> door(Supplier<Block> copy) {
        return door(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> door() {
        return door(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> door(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<DoorBlock, L2Registrate>> build
    ) {
        return door(null, properties, build);
    }


    default PlanksEntry<E> trapDoor(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<TrapDoorBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        BlockBuilder<TrapDoorBlock, L2Registrate> b = build.apply(self.registrate.block(self.name + "_trapdoor", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new TrapDoorBlock(self.bst, properties1);
                }).tag(BlockTags.PLANKS))
                .blockstate((c, p) -> {
                    ResourceLocation blockTextures = c.getId().withPrefix("block/");
                    p.trapdoorBlock(c.get(), blockTextures, true);
                }).item().model((c, p) -> {
                    p.withExistingParent(c.getId().getPath(), c.getId().withPrefix("block/").withSuffix("_bottom"));
                }).build()
                .recipe((c, p) -> {
                    DataIngredient items = DataIngredient.items(self.planks.asItem());
                    RegistrateRecipeProvider.trapdoorBuilder(c.get(), items.toVanilla())
                            .unlockedBy(p.safeName(c.getId()), items.getCriterion(p))
                            .save(p, p.safeId(c.getId()));
                });
        return setTrapDoor(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"木活板门")
                .ww_ag$zh_tw(self.zhTw+"木活板門")
                .ww_ag$zh_hk(self.zhHk+"木活板門")
                .ww_ag$self()
                .lang(self.firstUpName() + " trapdoor").register());

    }
    default PlanksEntry<E> trapDoor(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<TrapDoorBlock, L2Registrate>> build
    ) {
        return trapDoor(copy, __ -> {}, build);
    }
    default PlanksEntry<E> trapDoor(Supplier<Block> copy) {
        return trapDoor(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> trapDoor() {
        return trapDoor(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> trapDoor(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<TrapDoorBlock, L2Registrate>> build
    ) {
        return trapDoor(null, properties, build);
    }




}
