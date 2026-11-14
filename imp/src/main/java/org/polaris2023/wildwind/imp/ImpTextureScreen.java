package org.polaris2023.wildwind.imp;

import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「贴图容器界面」基类（ADR-0003）。
 *
 * <p>为什么需要它：容器界面的「画背景」钩子在两族里<b>方法名与签名都不同</b>——</p>
 * <ul>
 *   <li>26.x：{@code public void extractBackground(GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick)}，
 *       且 GUI 绘制改成了「RenderPipeline + Identifier」的 {@code GuiGraphicsExtractor}；</li>
 *   <li>1.20 / 1.21：{@code protected void renderBg(GuiGraphics, float partialTick, int mouseX, int mouseY)}，
 *       绘制走 {@code GuiGraphics.blit(ResourceLocation, ...)}，没有 RenderPipeline 概念。</li>
 * </ul>
 *
 * <p>覆写签名里带着版本专属类型，内容模块没法自己写这个覆写——所以覆写留在 imp，
 * 由本类<b>按族各写一份</b>；内容模块只提供「贴图 + 若干矩形填充 + 若干矩形贴图」
 * 这份纯数据描述（{@link Patch}），全程不出现任何版本专属类型。</p>
 *
 * <p>参数约定：所有坐标都是相对界面左上角的偏移，本类负责加上 {@code leftPos/topPos}。</p>
 *
 * @param <M> 菜单类型
 */
public abstract class ImpTextureScreen<M extends net.minecraft.world.inventory.AbstractContainerMenu>
        extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<M> {

    /** 一次矩形纯色填充（相对界面左上角）。 */
    public record Fill(int x, int y, int width, int height, int color) {}

    /**
     * 一次矩形贴图绘制（相对界面左上角）。
     *
     * @param texture      贴图资源 ID
     * @param x            目标左上角 x
     * @param y            目标左上角 y
     * @param u            贴图内的源 u
     * @param v            贴图内的源 v
     * @param width        绘制宽
     * @param height       绘制高
     * @param srcWidth     取样宽（缩放时与 width 不同）
     * @param srcHeight    取样高
     * @param textureWidth 贴图总宽（u/v 的归一化分母）
     * @param textureHeight 贴图总高
     */
    public record Blit(ImpId texture, int x, int y, float u, float v,
                       int width, int height, int srcWidth, int srcHeight,
                       int textureWidth, int textureHeight) {}

    private final ImpId background;
    private final int backgroundTextureSize;
    private final List<Fill> fills;
    private final List<Blit> blits;

    /**
     * @param menu                 菜单
     * @param inventory            玩家背包
     * @param title                标题
     * @param background           背景贴图
     * @param backgroundTextureSize 背景贴图的总边长（原版 container 贴图都是 256×256）
     * @param fills                在背景之上按顺序绘制的纯色矩形
     * @param blits                在填充之上按顺序绘制的贴图矩形
     */
    protected ImpTextureScreen(M menu, net.minecraft.world.entity.player.Inventory inventory,
                               net.minecraft.network.chat.Component title,
                               ImpId background, int backgroundTextureSize,
                               List<Fill> fills, List<Blit> blits) {
        // 三族都有这个三参构造器，且默认尺寸正是 176×166（与原版容器界面一致）
        super(menu, inventory, title);
        this.background = background;
        this.backgroundTextureSize = backgroundTextureSize;
        this.fills = List.copyOf(fills);
        this.blits = List.copyOf(blits);
    }

    /** 26.x：绘制钩子改名为 {@code extractBackground}，参数类型是 {@code GuiGraphicsExtractor}。 */
    @Since("26.1")
    @Override
    public void extractBackground(net.minecraft.client.gui.GuiGraphicsExtractor graphics,
                                  int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        // 26.x 的 blit 需要显式 RenderPipeline；GUI 文本/贴图统一用 GUI_TEXTURED
        graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, this.background.unwrap(),
                this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, this.backgroundTextureSize, this.backgroundTextureSize);
        for (Fill fill : this.fills) {
            graphics.fill(this.leftPos + fill.x(), this.topPos + fill.y(),
                    this.leftPos + fill.x() + fill.width(), this.topPos + fill.y() + fill.height(),
                    fill.color());
        }
        for (Blit blit : this.blits) {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, blit.texture().unwrap(),
                    this.leftPos + blit.x(), this.topPos + blit.y(), blit.u(), blit.v(),
                    blit.width(), blit.height(), blit.srcWidth(), blit.srcHeight(),
                    blit.textureWidth(), blit.textureHeight());
        }
    }

    /**
     * 1.20 / 1.21：绘制钩子是 {@code renderBg}，没有 RenderPipeline 参数，
     * {@code blit} 直接接受 {@code ResourceLocation}。
     */
    @Until("26.1")
    @Override
    protected void renderBg(net.minecraft.client.gui.GuiGraphics graphics,
                            float partialTick, int mouseX, int mouseY) {
        // 旧版 renderBg 是抽象方法，没有 super 实现可调（26.x 的 extractBackground 才有）
        graphics.blit(this.background.unwrap(), this.leftPos, this.topPos,
                0.0F, 0.0F, this.imageWidth, this.imageHeight,
                this.backgroundTextureSize, this.backgroundTextureSize);
        for (Fill fill : this.fills) {
            graphics.fill(this.leftPos + fill.x(), this.topPos + fill.y(),
                    this.leftPos + fill.x() + fill.width(), this.topPos + fill.y() + fill.height(),
                    fill.color());
        }
        for (Blit blit : this.blits) {
            // 旧版的这个重载把「绘制宽高」放在 u/v 之前，且取样宽高单列
            graphics.blit(blit.texture().unwrap(),
                    this.leftPos + blit.x(), this.topPos + blit.y(),
                    blit.width(), blit.height(), blit.u(), blit.v(),
                    blit.srcWidth(), blit.srcHeight(),
                    blit.textureWidth(), blit.textureHeight());
        }
    }
}
