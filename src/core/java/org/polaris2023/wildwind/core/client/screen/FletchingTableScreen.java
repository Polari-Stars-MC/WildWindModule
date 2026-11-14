package org.polaris2023.wildwind.core.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.polaris2023.wildwind.core.menu.FletchingTableMenu;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.ImpTextureScreen;

import java.util.List;

/**
 * 制箭台界面。
 * <p>
 * 直接引用原版锻造台界面贴图（不复制进模组 jar，避免再分发 Mojang 资源），
 * 槽位布局与之对齐：三个输入 (8,48)/(26,48)/(44,48)，产物 (98,48)，背包 (8,84) 起。
 * <p>
 * 锻造台贴图左上角自带一个锤子装饰，这里用底色盖掉，换成光灵箭图标。
 * <p>
 * 绘制钩子的名字与签名在两族里不同（26.x {@code extractBackground} +
 * {@code GuiGraphicsExtractor} / 旧版 {@code renderBg} + {@code GuiGraphics}），
 * 覆写由 {@link ImpTextureScreen} 按族各写一份；这里只提供「画什么」的纯数据描述。
 *
 * @author safe_saffawa
 */
public class FletchingTableScreen extends ImpTextureScreen<FletchingTableMenu> {
    private static final ImpId TEXTURE =
            ImpIds.idOf("minecraft", "textures/gui/container/smithing.png");
    private static final ImpId ARROW_ICON =
            ImpIds.idOf("minecraft", "textures/item/spectral_arrow.png");

    private static final int BACKGROUND_COLOR = 0xFFC6C6C6;
    /** 原版锻造台贴图总边长（container 贴图固定 256×256） */
    private static final int TEXTURE_SIZE = 256;
    /** 原版锤子装饰在贴图上的范围（含 1px 余量） */
    private static final int HAMMER_X = 6;
    private static final int HAMMER_Y = 7;
    private static final int HAMMER_SIZE = 32;
    /** 光灵箭图标的绘制尺寸（放大到 24×24 后居中于被遮盖的 32×32 区域） */
    private static final int ICON_SIZE = 24;
    private static final int ICON_X = HAMMER_X + (HAMMER_SIZE - ICON_SIZE) / 2;
    private static final int ICON_Y = HAMMER_Y + (HAMMER_SIZE - ICON_SIZE) / 2;
    /** 图标源贴图是 16×16 的物品贴图，放大到 24×24 绘制 */
    private static final int ICON_TEXTURE_SIZE = 16;

    public FletchingTableScreen(FletchingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, TEXTURE, TEXTURE_SIZE,
                // 先盖掉原版锻造台的锤子
                List.of(new Fill(HAMMER_X, HAMMER_Y, HAMMER_SIZE, HAMMER_SIZE, BACKGROUND_COLOR)),
                // 再在原位画光灵箭
                List.of(new Blit(ARROW_ICON, ICON_X, ICON_Y, 0.0F, 0.0F,
                        ICON_SIZE, ICON_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE,
                        ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE)));
        this.titleLabelX = 40;
        this.titleLabelY = 15;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }
}
