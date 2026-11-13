package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

/**
 * 告示牌 / 悬挂告示牌 / 展示架的共用模型生成，核心与各更新模块共用。
 *
 * @author safe_saffawa
 */
public final class SignModels {

    /**
     * 告示牌与悬挂告示牌复用原版 family 生成器，展示架复用原版 {@code createShelf}。
     * <p>
     * 这里直接新建 provider：{@code blockModels.family(planks)} 会重新生成木板本体模型，与已有模型冲突。
     */
    public static void registerSet(BlockModelGenerators blockModels, Block planks, Block strippedLog,
                                   Block sign, Block wallSign, Block hangingSign, Block wallHangingSign, Block shelf) {
        BlockFamily signFamily = new BlockFamily.Builder(planks)
                .strippedLog(strippedLog)
                .sign(sign, wallSign)
                .hangingSign(hangingSign, wallHangingSign)
                .getFamily();
        blockModels.new BlockFamilyProvider(TexturedModel.CUBE.get(planks).getMapping()).generateFor(signFamily);
        blockModels.createShelf(shelf, strippedLog);
    }

    private SignModels() {
    }
}
