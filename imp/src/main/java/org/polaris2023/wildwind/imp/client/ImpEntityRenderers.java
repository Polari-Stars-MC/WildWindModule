package org.polaris2023.wildwind.imp.client;

import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 版本无关的实体渲染器基类——GeckoLib {@code GeoEntityRenderer} 的替代品。
 *
 * <h2>它解决什么问题</h2>
 * <p>原版实体渲染器在三个版本族里的形态差异极大（全部用 {@code javap} 核实，见下表），
 * 内容模块不可能只写一份：</p>
 *
 * <table border="1">
 *   <caption>三族渲染体系对照（javap 实测）</caption>
 *   <tr><th>能力</th><th>1.20.6 / 1.21.1</th><th>26.2</th></tr>
 *   <tr><td>{@code EntityRenderer} 泛型</td><td>{@code <T extends Entity>}（1 个）</td>
 *       <td>{@code <T extends Entity, S extends EntityRenderState>}（2 个）</td></tr>
 *   <tr><td>渲染入口</td><td>{@code render(T, float, float, PoseStack, MultiBufferSource, int)}</td>
 *       <td>{@code submit(S, PoseStack, SubmitNodeCollector, CameraRenderState)}</td></tr>
 *   <tr><td>取纹理</td><td>{@code getTextureLocation(T)} → {@code ResourceLocation}</td>
 *       <td>{@code getTextureLocation(S)} → {@code Identifier}</td></tr>
 *   <tr><td>{@code LivingEntityRenderer} 泛型</td><td>{@code <T, M extends EntityModel<T>>}</td>
 *       <td>{@code <T, S, M extends EntityModel<? super S>>}</td></tr>
 *   <tr><td>{@code EntityModel} 的泛型参数</td><td>实体类型</td><td><b>渲染状态类型</b></td></tr>
 *   <tr><td>{@code Model#renderToBuffer}</td><td>抽象；1.21 收 5 参，1.20.6 收 8 参</td>
 *       <td><b>final</b>，5 参 {@code int} 颜色</td></tr>
 *   <tr><td>{@code RenderType} 位置</td><td>{@code client.renderer.RenderType}</td>
 *       <td>{@code client.renderer.rendertype.RenderTypes}（拆包 + 改名）</td></tr>
 *   <tr><td>阴影</td><td>渲染器自己画</td><td>调度器按 {@code state.shadowPieces} 统一提交</td></tr>
 * </table>
 *
 * <h2>26.x 的 submit 架构做到了什么程度：完整实现，不是占位</h2>
 * <p>与"新架构只能先凑合"的预期相反，26.x 反而是三族里<b>需要写的代码最少</b>的一族：
 * {@code LivingEntityRenderer#submit} 已经把全部版本无关动作（姿态求解、{@code state.scale}
 * 缩放、{@code -1,-1,1} 翻转、{@code -1.501} 基准位移、受伤泛红
 * {@code getOverlayCoords}、光照、图层遍历、阴影）都写完了，并把"模型怎么画"通过
 * {@code SubmitNodeCollector#submitModel} 延迟给 {@code ModelFeatureRenderer}；
 * 后者在绘制阶段调用 {@code model.setupAnim(state)} 再 {@code model.renderToBuffer(...)}。
 * 所以 26.x 分支只要做三件事：</p>
 * <ol>
 *   <li>把 {@code rig.root()} 在<b>构造期</b>交给 {@code EntityModel}（26.x 的
 *       {@code Model(ModelPart, Function<Identifier,RenderType>)} 强制要求 root，
 *       因为它在构造期缓存 {@code root.getAllParts()}）；</li>
 *   <li>在 {@code setupAnim(state)} 里推进 {@code rig.update()} 并选动画；</li>
 *   <li>{@code getTextureLocation(state)} 返回纹理。</li>
 * </ol>
 * <p>三者的实现都在 {@link ImpGeoModel} 里，且 {@code renderToBuffer} 在 26.x 是
 * {@code final} 的，无需也不能覆写。</p>
 *
 * <h2>为什么还要一个 {@code State}</h2>
 * <p>26.x 的渲染是两阶段的：{@code extractRenderState(entity, state, partialTick)} 采集 →
 * {@code submit(state, ...)} 绘制。{@code submit}/{@code setupAnim} 阶段<b>拿不到实体</b>，
 * 只有状态；而"走/站/攻击该播哪条动画"依赖实体自身字段。因此 {@link State} 在标准
 * {@code LivingEntityRenderState} 之上补了一个实体引用，由本类在
 * {@code extractRenderState} 里填入。状态对象每帧新建、不跨实体复用，持有引用是安全的。</p>
 *
 * <h2>写法约定</h2>
 * <p>版本专属类型一律全限定名、不写 {@code import}（被删掉的成员会连带 import 失效，
 * 导致另一族"包不存在"）。同名同签名的成员用 {@code @Since}/{@code @Until} 分段、
 * 由预处理器只保留一段——与 {@code ImpGeo} 的做法一致，且能绕开"类型出现在泛型实参 /
 * {@code extends} 子句里时 {@code @Name} 不改写"的限制。</p>
 *
 * @author safe_saffawa
 * @see ImpGeoModel
 * @see ImpGeoRig
 */
public final class ImpEntityRenderers {

    private ImpEntityRenderers() {}

    // ══════════════════════════════════════════════════════════════════════
    // 公共工具（三族同签名，无需分段）
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 由模型 id 推导默认纹理 id。
     *
     * <p>与仓库现有资源布局一致：模型 {@code entity/spiderling} 对应
     * {@code geckolib/models/entity/spiderling.geo.json}，纹理是
     * {@code textures/entity/spiderling.png}。因此默认纹理 =
     * {@code textures/<modelId.path()>.png}。</p>
     *
     * @param modelId 模型 id（相对 {@code geckolib/models/} 的路径，如 {@code entity/spiderling}）
     */
    public static ImpId defaultTexture(ImpId modelId) {
        return ImpId.of(modelId.namespace(), "textures/" + modelId.path() + ".png");
    }

    /**
     * 从资源管理器烘焙一个 rig（{@link ImpGeoRig#load} 的门面，让内容模块少一个 import）。
     *
     * <p>几何或动画缺失时返回<b>空骨架</b>而不是 {@code null}：渲染器照常工作，
     * 表现为"看不见的实体"而不是崩溃——与 GeckoLib 的行为不同，但更容易排查。</p>
     */
    public static ImpGeoRig.Rig loadRig(
            net.minecraft.server.packs.resources.ResourceManager manager, ImpId modelId) {
        return ImpGeoRig.load(manager, modelId);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 渲染状态
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的渲染状态：在 {@code LivingEntityRenderState} 之上补一个<b>实体引用</b>。
     *
     * <p>存在的理由见类 javadoc 的「为什么还要一个 State」。旧族没有
     * {@code EntityRenderState} 体系，同名类是空占位。</p>
     */
    @Since("26.1")
    public static class State extends net.minecraft.client.renderer.entity.state.LivingEntityRenderState {

        /** 本帧被渲染的实体；由 {@link Renderer#extractRenderState} 填入。 */
        public net.minecraft.world.entity.LivingEntity entity;
    }

    /** 1.20 / 1.21 的占位：旧族没有渲染状态阶段，模型直接拿到实体。 */
    @Until("26.1")
    public static final class State {

        private State() {}
    }

    // ══════════════════════════════════════════════════════════════════════
    // 模型基座：唯一承载 EntityModel 差异的一层
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的 {@code EntityModel} 适配层。
     *
     * <p>两处关键差异：</p>
     * <ul>
     *   <li>泛型参数是<b>渲染状态</b>（{@code EntityModel<T extends EntityRenderState>}）而不是实体；</li>
     *   <li>构造器<b>强制要求 root</b>（{@code Model(ModelPart, Function<Identifier,RenderType>)}
     *       在构造期缓存 {@code root.getAllParts()}），所以 root 必须在 super() 时就绪。</li>
     * </ul>
     * <p>{@code renderToBuffer} 在 26.x 是 {@code final} 的（{@code Model} 内直接
     * {@code root().render(...)}），本类<b>不</b>覆写它。</p>
     */
    @Since("26.1")
    public abstract static class ModelBase<T extends net.minecraft.world.entity.Entity>
        extends net.minecraft.client.model.EntityModel<
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState> {

        /** 模型根。26.x 构造期即固定，之后不再变。 */
        protected final net.minecraft.client.model.geom.ModelPart root;
        private final ImpId texture;

        /**
         * @param root    模型根 {@code ModelPart}（26.x 必须在构造期给定）
         * @param texture 主纹理 id（本类据此生成 {@code RenderType}，子类不必接触）
         */
        protected ModelBase(net.minecraft.client.model.geom.ModelPart root, ImpId texture) {
            // 26.x：RenderType 工厂搬到了 rendertype.RenderTypes（原 RenderType 类已改名并拆包）
            super(root, tex -> net.minecraft.client.renderer.rendertype.RenderTypes.entityCutout(tex));
            this.root = root;
            this.texture = texture;
        }

        /** 主纹理 id。 */
        public final ImpId texture() {
            return this.texture;
        }

        /** 渲染用的根 {@code ModelPart}。 */
        public final net.minecraft.client.model.geom.ModelPart rootPart() {
            return this.root;
        }

        /**
         * 每帧的动画推进点。
         *
         * @param entity     本帧被渲染的实体；{@code null} 表示状态不是本类自带的 {@link State}
         *                   （例如被别的渲染器复用了模型）
         * @param ageInTicks 实体年龄（含 partialTick），三族语义一致
         */
        protected abstract void onSetupAnim(net.minecraft.world.entity.Entity entity, float ageInTicks);

        @Override
        public final void setupAnim(
                net.minecraft.client.renderer.entity.state.LivingEntityRenderState state) {
            // 只有状态是本类自己的 State 子类时才拿得到实体
            net.minecraft.world.entity.Entity entity = state instanceof State own ? own.entity : null;
            this.onSetupAnim(entity, state.ageInTicks);
        }
    }

    /**
     * 1.20 / 1.21 的 {@code EntityModel} 适配层。
     *
     * <p>差异：泛型参数是<b>实体类型</b>；构造器只收 {@code RenderType} 函数（root 由子类自己持有）；
     * {@code renderToBuffer} 是<b>抽象</b>的，且签名在 1.20.6 与 1.21.1 之间又不一样
     * （1.21 收一个 {@code int} 颜色，1.20.6 收 4 个 float），所以这里再切一刀。</p>
     */
    @Until("26.1")
    public abstract static class ModelBase<T extends net.minecraft.world.entity.Entity>
        extends net.minecraft.client.model.EntityModel<T> {

        /** 模型根。旧族构造器不需要它，但子类渲染时要用。 */
        protected final net.minecraft.client.model.geom.ModelPart root;
        private final ImpId texture;

        /**
         * @param root    模型根 {@code ModelPart}（旧族构造器不接受，这里自行持有，保持三族签名一致）
         * @param texture 主纹理 id
         */
        protected ModelBase(net.minecraft.client.model.geom.ModelPart root, ImpId texture) {
            super(tex -> net.minecraft.client.renderer.RenderType.entityCutoutNoCull(tex));
            this.root = root;
            this.texture = texture;
        }

        /** 主纹理 id。 */
        public final ImpId texture() {
            return this.texture;
        }

        /** 渲染用的根 {@code ModelPart}。 */
        public final net.minecraft.client.model.geom.ModelPart rootPart() {
            return this.root;
        }

        /** 每帧的动画推进点（语义同 26.x 分支）。 */
        protected abstract void onSetupAnim(net.minecraft.world.entity.Entity entity, float ageInTicks);

        @Override
        public final void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                                    float ageInTicks, float netHeadYaw, float headPitch) {
            // 原版这 5 个参数是给"程序化摆动"用的；Bedrock 动画由 rig 按真实时间推进，
            // 因此这里只透传实体与年龄，其余三个参数刻意忽略。
            this.onSetupAnim(entity, ageInTicks);
        }

        /** 1.21 的签名：{@code renderToBuffer(PoseStack, VertexConsumer, int, int, int)}。 */
        @Since(value = "1.21", until = "26.1")
        @Name("renderToBuffer")
        @Override
        public void renderToBufferTen21(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                        com.mojang.blaze3d.vertex.VertexConsumer buffer,
                                        int packedLight, int packedOverlay, int color) {
            this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        }

        /** 1.20.6 的签名：{@code renderToBuffer(..., float, float, float, float)}。 */
        @Until("1.21")
        @Name("renderToBuffer")
        @Override
        public void renderToBufferTen20(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                        com.mojang.blaze3d.vertex.VertexConsumer buffer,
                                        int packedLight, int packedOverlay,
                                        float red, float green, float blue, float alpha) {
            this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 实体渲染器
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的实体渲染器基类。
     *
     * <p>渲染状态用本类自带的 {@link State}（带实体引用），{@code extractRenderState} 阶段
     * 把实体塞进去。渲染细节（姿态 / 缩放 / 翻转 / 受伤泛红 / 光影 / 阴影）全部交给原版
     * {@code LivingEntityRenderer#submit}，本类只决定"用哪个模型、哪张纹理、播哪条动画"。</p>
     */
    @Since("26.1")
    public static class Renderer<T extends net.minecraft.world.entity.LivingEntity>
        extends net.minecraft.client.renderer.entity.LivingEntityRenderer<
            T, State, ImpGeoModel<T>> {

        private final ImpGeoModel<T> geoModel;

        /**
         * @param context 原版渲染器上下文
         * @param modelId 模型 id（相对 {@code geckolib/models/} 的路径，如 {@code entity/spiderling}）
         * @param shadow  阴影半径
         */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, float shadow) {
            this(context, modelId, defaultTexture(modelId), shadow);
        }

        /**
         * @param texture 主纹理 id（{@code null} 时按 {@link #defaultTexture} 从 modelId 推导）
         */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, ImpId texture, float shadow) {
            this(context, new ImpGeoModel<T>(
                loadRig(context.getResourceManager(), modelId),
                texture == null ? defaultTexture(modelId) : texture), shadow);
        }

        /**
         * 用内容模块自己构造的模型（模型类要覆写钩子时走这个）。
         */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpGeoModel<T> model, float shadow) {
            super(context, model, shadow);
            this.geoModel = this.model;
            // super() 之后才能用 this：动画回调必须晚于模型构造
            this.model.setAnimator(this::dispatchAnimate);
        }

        @Override
        public State createRenderState() {
            return new State();
        }

        @Override
        public void extractRenderState(T entity, State state, float partialTick) {
            super.extractRenderState(entity, state, partialTick);
            state.entity = entity;
        }

        @Override
        public net.minecraft.resources.Identifier getTextureLocation(State state) {
            return this.geoModel.texture().unwrap();
        }

        /** 本渲染器持有的模型（内容模块要直连 rig 时用）。 */
        public ImpGeoModel<T> geoModel() {
            return this.geoModel;
        }

        /** 骨架门面：注册 / 循环 / 触发动画都从这里走。 */
        public ImpGeoRig.Rig rig() {
            return this.geoModel.rig();
        }

        /**
         * 每帧的动画选择点。默认什么都不做（保持静止姿态），子类覆写它切换循环动画。
         *
         * <pre>{@code
         * @Override
         * protected void onAnimate(Spiderling entity, float ageInTicks) {
         *     boolean moving = entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4;
         *     this.rig().loop(moving ? "move.walk" : null);
         * }
         * }</pre>
         *
         * @param entity     本帧被渲染的实体（恒为本渲染器注册的实体类型）
         * @param ageInTicks 含 partialTick 的年龄
         */
        protected void onAnimate(T entity, float ageInTicks) {}

        /** 把模型的 {@code (Entity, float)} 回调收敛到带类型的 {@link #onAnimate}。 */
        @SuppressWarnings("unchecked")
        private void dispatchAnimate(net.minecraft.world.entity.Entity entity, float ageInTicks) {
            // T 由实体注册表绑定，此处的向下转型恒成立
            this.onAnimate((T) entity, ageInTicks);
        }

        /**
         * 追加一层"只画发光纹理"的图层（替代 GeckoLib 的 {@code AutoGlowingGeoLayer}）。
         *
         * <p>26.x 的图层是<b>提交式</b>的：{@code submitModelPart} 把 {@code ModelPart}
         * 交给 {@code SubmitNodeCollector}，由渲染阶段统一绘制；用 4 参重载让 tint 与
         * outline 走默认值。</p>
         */
        public Renderer<T> withGlowLayer(ImpId glowTexture) {
            this.addLayer(new net.minecraft.client.renderer.entity.layers.RenderLayer<
                    State, ImpGeoModel<T>>(this) {
                @Override
                public void submit(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   net.minecraft.client.renderer.SubmitNodeCollector collector,
                                   int packedLight, State state, float yRot, float xRot) {
                    collector.submitModelPart(
                        this.getParentModel().rootPart(),
                        poseStack,
                        net.minecraft.client.renderer.rendertype.RenderTypes.eyes(glowTexture.unwrap()),
                        packedLight,
                        net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                        null);
                }
            });
            return this;
        }
    }

    /**
     * 1.20 / 1.21 的实体渲染器基类。
     *
     * <p>旧族的 {@code LivingEntityRenderer#render} 同样是完整实现，并且会调用
     * {@code model.setupAnim(...)} 与 {@code model.renderToBuffer(...)}，所以这里
     * 也只需补"模型 / 纹理 / 动画选择"三件事。</p>
     */
    @Until("26.1")
    public static class Renderer<T extends net.minecraft.world.entity.LivingEntity>
        extends net.minecraft.client.renderer.entity.LivingEntityRenderer<T, ImpGeoModel<T>> {

        private final ImpGeoModel<T> geoModel;

        /** 见 26.x 分支的同名构造器。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, float shadow) {
            this(context, modelId, defaultTexture(modelId), shadow);
        }

        /** 见 26.x 分支的同名构造器。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, ImpId texture, float shadow) {
            this(context, new ImpGeoModel<T>(
                loadRig(context.getResourceManager(), modelId),
                texture == null ? defaultTexture(modelId) : texture), shadow);
        }

        /** 见 26.x 分支的同名构造器。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpGeoModel<T> model, float shadow) {
            super(context, model, shadow);
            this.geoModel = this.model;
            this.model.setAnimator(this::dispatchAnimate);
        }

        @Override
        public net.minecraft.resources.ResourceLocation getTextureLocation(T entity) {
            return this.geoModel.texture().unwrap();
        }

        /** 见 26.x 分支的同名方法。 */
        public ImpGeoModel<T> geoModel() {
            return this.geoModel;
        }

        /** 见 26.x 分支的同名方法。 */
        public ImpGeoRig.Rig rig() {
            return this.geoModel.rig();
        }

        /** 见 26.x 分支的同名方法。 */
        protected void onAnimate(T entity, float ageInTicks) {}

        @SuppressWarnings("unchecked")
        private void dispatchAnimate(net.minecraft.world.entity.Entity entity, float ageInTicks) {
            this.onAnimate((T) entity, ageInTicks);
        }

        /**
         * 追加发光图层。
         *
         * <p>旧族是<b>即时绘制式</b>的：{@code RenderLayer#render} 里直接
         * {@code getBuffer(...)} + {@code ModelPart#render(...)}。这里刻意用 4 参
         * {@code ModelPart#render}（1.20.6 与 1.21.1 都有这个重载），避免踩两族颜色参数
         * 不一致（1.21 是 {@code int}、1.20.6 是 4 个 float）的坑。</p>
         */
        public Renderer<T> withGlowLayer(ImpId glowTexture) {
            this.addLayer(new net.minecraft.client.renderer.entity.layers.RenderLayer<
                    T, ImpGeoModel<T>>(this) {
                @Override
                public void render(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   net.minecraft.client.renderer.MultiBufferSource buffers,
                                   int packedLight, T entity,
                                   float limbSwing, float limbSwingAmount, float partialTick,
                                   float ageInTicks, float netHeadYaw, float headPitch) {
                    com.mojang.blaze3d.vertex.VertexConsumer buffer = buffers.getBuffer(
                        net.minecraft.client.renderer.RenderType.eyes(glowTexture.unwrap()));
                    this.getParentModel().rootPart().render(
                        poseStack, buffer, packedLight,
                        net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
                }
            });
            return this;
        }
    }
}
