package org.polaris2023.ww_ag.utils.planks;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.function.Supplier;

import static com.tterrag.registrate.util.nullness.NonNullUnaryOperator.identity;

/**
 * @author baka4n
 * @code @Date 2025/6/18 23:57:12
 */
public interface ISign<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setSign(BlockEntry<StandingSignBlock> entry);
    T setHangingSign(BlockEntry<CeilingHangingSignBlock> entry);
    T setWallSign(BlockEntry<WallSignBlock> entry);
    T setWallHangingSign(BlockEntry<WallHangingSignBlock> entry);

    default T sign(
    ) {
        return sign(identity(), identity());
    }
    default T sign(
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP
    ) {
        return sign(signP, wallSignP, identity());
    }
    default T sign(
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return sign(null, identity(), null, identity(), signIP);
    }
    default T sign(
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return sign(null, signP, null, wallSignP, signIP);
    }
    default T sign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign
    ) {
        return sign(copySign, copyWallSign, identity());
    }
    default T sign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return sign(copySign, copyWallSign, identity(), signIP);
    }
    default T sign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return sign(copySign, identity(), copyWallSign, wallSignP, signIP);
    }
    default T sign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return sign(copySign, signP, copyWallSign, identity(), signIP);
    }default T sign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign
    ) {
        return sign(copySign, signP, copyWallSign, identity(), identity());
    }
    default T sign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {// modid:a_planks  -> modid:a
        T self = ww_ag$self();
        return setWallSign(self.registrate.block(self.name + "_wall_sign",__ -> {
                    BlockBehaviour.Properties p =
                            copyWallSign != null
                                    ?
                                    BlockBehaviour.Properties.ofFullCopy(copyWallSign.get()) :
                                    BlockBehaviour.Properties.of();
                    return new WallSignBlock(self.wt, wallSignP.apply(p));
                })
                .setData(ProviderType.LANG, (c, p) -> {})
                .blockstate((c, p) -> {

                }).register())
                .setSign(ILanguage.convert1(self.registrate.block(self.name +"_sign",__ -> {
                    BlockBehaviour.Properties p =
                            copySign != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copySign.get()) :
                                    BlockBehaviour.Properties.of();
                    return new StandingSignBlock(self.wt, signP.apply(p));
                })
                                .item((b, p) -> new SignItem(signIP.apply(p), b, self.wall_sign.get()))
                                .build()
                                .blockstate((c, p) -> {
                                    var model = p.models().sign(c.getId().getPath(), TextureMapping.getBlockTexture(self.planks.get()));
                                    p.simpleBlock(c.get(), model);
                                    p.simpleBlock(self.wall_sign.get(), model);
                                    p.simpleBlockItem(c.get(), model);
                                }))
                        .ww_ag$zh_cn(self.zhCn+"告示牌")
                        .ww_ag$zh_tw(self.zhTw+"告示牌")
                        .ww_ag$zh_hk(self.zhHk+"告示牌")
                        .ww_ag$self()
                        .lang(self.firstUpName() + " Sign").register());

    }


    default T hangingSign(
    ) {
        return hangingSign(identity(), identity());
    }
    default T hangingSign(
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP
    ) {
        return hangingSign(signP, wallSignP, identity());
    }
    default T hangingSign(
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return hangingSign(null, identity(), null, identity(), signIP);
    }
    default T hangingSign(
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return hangingSign(null, signP, null, wallSignP, signIP);
    }
    default T hangingSign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign
    ) {
        return hangingSign(copySign, copyWallSign, identity());
    }
    default T hangingSign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return hangingSign(copySign, copyWallSign, identity(), signIP);
    }
    default T hangingSign(
            Supplier<Block> copySign,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return hangingSign(copySign, identity(), copyWallSign, wallSignP, signIP);
    }
    default T hangingSign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {
        return hangingSign(copySign, signP, copyWallSign, identity(), signIP);
    }
    default T hangingSign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign
    ) {
        return hangingSign(copySign, signP, copyWallSign, identity(), identity());
    }
    default T hangingSign(
            Supplier<Block> copySign,
            NonNullUnaryOperator<BlockBehaviour.Properties> signP,
            Supplier<Block> copyWallSign,
            NonNullUnaryOperator<BlockBehaviour.Properties> wallSignP,
            NonNullUnaryOperator<Item.Properties> signIP
    ) {// modid:a_planks  -> modid:a
        T self = ww_ag$self();
        return setWallHangingSign(self.registrate.block(self.name + "_wall_hanging_sign",__ -> {
                    BlockBehaviour.Properties p =
                            copyWallSign != null
                                    ?
                                    BlockBehaviour.Properties.ofFullCopy(copyWallSign.get()) :
                                    BlockBehaviour.Properties.of();
                    return new WallHangingSignBlock(self.wt, wallSignP.apply(p));
                })
                .setData(ProviderType.LANG, (c, p) -> {})
                .blockstate((c, p) -> {

                }).register())
                .setHangingSign(ILanguage.convert1(self.registrate.block(self.name +"_hanging_sign",__ -> {
                    BlockBehaviour.Properties p =
                            copySign != null ?
                                    BlockBehaviour.Properties.ofFullCopy(copySign.get()) :
                                    BlockBehaviour.Properties.of();
                    return new CeilingHangingSignBlock(self.wt, signP.apply(p));
                })
                                .item((b, p) -> new HangingSignItem(b, self.wall_hanging_sign.get(), signIP.apply(p)))
                                .build()
                                .blockstate((c, p) -> {
                                    var model = p.models().sign(c.getId().getPath(), TextureMapping.getBlockTexture(self.stripped_log.get()));
                                    p.simpleBlock(c.get(), model);
                                    p.simpleBlock(self.wall_hanging_sign.get(), model);
                                    p.simpleBlockItem(c.get(), model);
                                }))
                        .ww_ag$zh_cn("悬挂式"+self.zhCn+"告示牌")
                        .ww_ag$zh_tw("懸掛式"+self.zhTw+"告示牌")
                        .ww_ag$zh_hk("懸掛式"+self.zhHk+"告示牌")
                        .ww_ag$self()
                        .lang(self.firstUpName() + " Hanging Sign").register());

    }




}
