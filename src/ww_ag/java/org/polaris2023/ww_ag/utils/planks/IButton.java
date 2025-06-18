package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import lombok.experimental.ExtensionMethod;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;
import org.polaris2023.ww_ag.utils.RegUtil;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/6/18 03:41:44
 */
public interface IButton<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setButton(BlockEntry<ButtonBlock> entry);
    default PlanksEntry<E> button(
            Supplier<Block> copy,
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<ButtonBlock, L2Registrate>> build
    ) {
        T self = ww_ag$self();
        BlockBuilder<ButtonBlock, L2Registrate> b = build.apply(self.registrate.block(self.name + "_button", __ -> {
                    BlockBehaviour.Properties properties1 =
                            copy != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copy.get())
                                    : BlockBehaviour.Properties.of();
                    properties.accept(properties1);
                    return new ButtonBlock(self.bst, 30, properties1);
                }))
                .blockstate((c, p) -> {
                    ResourceLocation blockTexture = TextureMapping.getBlockTexture(self.planks.get());
                    p.buttonBlock(c.get(), blockTexture);
                    BlockModelBuilder inventory = p.models().buttonInventory(c.getId().getPath() + "_inventory", blockTexture);
                    p.simpleBlockItem(c.get(), inventory);
                }
                )
                .tag(BlockTags.BUTTONS)
                .item().tag(ItemTags.BUTTONS).build()
                .recipe((c, p) -> {
                    DataIngredient items = DataIngredient.items(self.planks.asItem());
                    RegistrateRecipeProvider.buttonBuilder(c.get(), items.toVanilla())
                            .unlockedBy(p.safeName(c.getId()), items.getCriterion(p))
                            .save(p, p.safeId(c.getId()));
                });
        return setButton(ILanguage.convert1(b)
                .ww_ag$zh_cn(self.zhCn+"按钮")
                .ww_ag$zh_tw(self.zhTw+"按鈕")
                .ww_ag$zh_hk(self.zhHk+"按鈕")
                .ww_ag$self()
                .lang(self.firstUpName() + " Button").register());

    }
    default PlanksEntry<E> button(
            Supplier<Block> copy,
            NonNullUnaryOperator<BlockBuilder<ButtonBlock, L2Registrate>> build
    ) {
        return button(copy, __ -> {}, build);
    }
    default PlanksEntry<E> button(Supplier<Block> copy) {
        return button(copy, __ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> button() {
        return button(__ -> {}, NonNullUnaryOperator.identity());
    }
    default PlanksEntry<E> button(
            NonNullConsumer<BlockBehaviour.Properties> properties,
            NonNullUnaryOperator<BlockBuilder<ButtonBlock, L2Registrate>> build
    ) {
        return button(null, properties, build);
    }
}
