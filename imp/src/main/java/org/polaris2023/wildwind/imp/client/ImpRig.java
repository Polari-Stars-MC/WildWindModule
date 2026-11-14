package org.polaris2023.wildwind.imp.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 把 {@link ImpGeoRig.Rig} 接进<b>原版渲染管线</b>的装配层——GeckoLib
 * {@code GeoEntityRenderer} + {@code GeoModel} 的替代品。
 *
 * <h2>为什么需要这一层</h2>
 * <p>原版实体渲染管线的三件套（{@code EntityRenderer} / {@code EntityModel} / {@code RenderLayer}）
 * 在 1.20.6、1.21.1、26.2 上的形态完全不同（全部用 {@code javap} 逐类核实）：</p>
 *
 * <table border="1">
 *   <caption>三族渲染体系对照</caption>
 *   <tr><th>能力</th><th>1.20.6 / 1.21.1</th><th>26.2</th></tr>
 *   <tr><td>{@code EntityRenderer} 泛型</td><td>{@code <T>}（1 个）</td>
 *       <td>{@code <T, S extends EntityRenderState>}（2 个）</td></tr>
 *   <tr><td>渲染入口</td><td>{@code render(T, float, float, PoseStack, MultiBufferSource, int)}</td>
 *       <td>{@code submit(S, PoseStack, SubmitNodeCollector, CameraRenderState)}</td></tr>
 *   <tr><td>{@code EntityModel} 泛型参数</td><td><b>实体</b>类型</td><td><b>渲染状态</b>类型</td></tr>
 *   <tr><td>动画入口</td><td>{@code setupAnim(T, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch)}
 *       —— 有实体、有摆幅，<b>单阶段</b></td>
 *       <td>{@code setupAnim(S)} —— 只有状态，<b>两阶段</b>（{@code extractRenderState} → 绘制期
 *       {@code setupAnim}）</td></tr>
 *   <tr><td>{@code Model} 构造器</td><td>只收 {@code RenderType} 函数，root 由子类持有</td>
 *       <td>强制收 {@code ModelPart}（构造期缓存 {@code root.getAllParts()}）</td></tr>
 *   <tr><td>{@code renderToBuffer}</td><td><b>抽象</b>；1.21 收 5 参（int 色），1.20.6 收 8 参（float 色）</td>
 *       <td><b>final</b>，无需覆写</td></tr>
 *   <tr><td>{@code RenderType} 工厂</td><td>{@code client.renderer.RenderType}</td>
 *       <td>{@code client.renderer.rendertype.RenderTypes}（拆包 + 改名）</td></tr>
 *   <tr><td>图层</td><td>{@code RenderLayer#render}（即时绘制）</td>
 *       <td>{@code RenderLayer#submit} + {@code SubmitNodeCollector}（提交式）</td></tr>
 * </table>
 *
 * <h2>26.x 的 {@code submit} 架构做到了什么程度：完整实现，不是占位</h2>
 * <p>26.x 反而是三族里需要写的代码最少的一族：{@code LivingEntityRenderer#submit} 已经做完
 * 姿态求解、{@code state.scale} 缩放、{@code -1,-1,1} 翻转、{@code -1.501} 基准位移、
 * 受伤泛红、光照、图层遍历与阴影，并把"模型怎么画"通过
 * {@code SubmitNodeCollector#submitModel} 交给 {@code ModelFeatureRenderer}；后者在绘制阶段
 * 调用 {@code model.setupAnim(state)} 再 {@code model.renderToBuffer(...)}（已用字节码核实
 * 二者在同一循环体内相邻，所以每帧的 {@code setupAnim} 与绘制是配对执行的）。本类因此只做三件事：
 * 构造期把 {@code rig.root()} 交给 {@code EntityModel}、在 {@code setupAnim(state)} 里推进
 * {@code rig.update()}、{@code getTextureLocation(state)} 给出纹理。</p>
 *
 * <h2>动画驱动：为什么不用 {@link GeoRigController}</h2>
 * <p>{@code GeoRigController} 把控制器名当作<b>动画键</b>去注册
 * （{@code rig.register("__probe__" + name, name, priority)}），而内容模块的 selector 返回的是
 * <b>动画文件里的键</b>（{@code "move.walk"}）。两者对不上，循环动画永远不会真正播放。
 * 该文件属于"已完成基础设施"，不在本次可改范围内，因此由本类的 {@link Driver} 自行完成
 * 「{@code AnimationSet} → {@code Rig}」的桥接：按动画文件自身的键全量注册，selector 的返回值
 * 直接就是键。</p>
 *
 * <h2>26.x 与旧版的状态分工</h2>
 * <ul>
 *   <li><b>26.x</b>：{@code extractRenderState} 把实体引用塞进渲染状态（{@link RenderState#entity}），
 *       模型在绘制期的 {@code setupAnim(S)} 里取回它。</li>
 *   <li><b>1.20.6 / 1.21.1</b>：没有渲染状态阶段，{@code setupAnim(T, ...)} 直接拿到实体与摆幅。
 *       旧族的 {@link RenderState} 因此是<b>空占位类</b>（见其 javadoc）。</li>
 * </ul>
 *
 * <h2>已知能力限制（有意接受）</h2>
 * <ul>
 *   <li><b>动画时钟按实体类型共享</b>：{@code ModelPart} 树与 {@link ImpGeoRig.Rig} 属于渲染器
 *       （每类实体一份），而 26.x 的 {@code EntityModel} 要求构造期就固定 root，无法按实体换骨架。
 *       因此同屏多个同种实体的循环动画<b>相位一致</b>，与 GeckoLib 的 per-entity 缓存不同。
 *       一次性动画（攻击）由实体侧同步的触发令牌驱动，触发时机仍是每实体独立的。</li>
 *   <li><b>动画时间来自真实时间</b>（{@code System.nanoTime()} 差分，见
 *       {@code GeoResources.Binding#update()}），不随实体 {@code tickCount} 推进；实体被剔除
 *       不可见时时钟照走，重新可见时会跳到当时的相位而不是从头播。</li>
 * </ul>
 *
 * @author safe_saffawa
 * @see ImpGeoRig
 * @see ImpAnimatedEntity
 */
public final class ImpRig {

    private ImpRig() {}

    // ══════════════════════════════════════════════════════════════════════
    // 实体侧契约（版本无关）
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 在原版渲染管线上做 Bedrock 骨骼动画的实体契约。
     *
     * <p>在 {@link ImpAnimatedEntity}（"我有哪些动画"）之上再补两点渲染侧真正需要的信息：</p>
     * <ul>
     *   <li>{@link #animationSet()}：让渲染器能读到控制器与一次性动画的登记表。
     *       {@code ImpAnimatedEntity#registerAnimations} 只收一个入参、不回传结果，
     *       渲染器拿不到实体内部的那份表。</li>
     *   <li>{@link #consumeAnimationTrigger}：一次性动画的<b>边沿检测</b>。渲染线程每帧都会问到，
     *       实现方须自行记住"上一次已经取走的令牌"，只在令牌变化时返回 {@code true} 一次。</li>
     * </ul>
     *
     * <p>触发令牌应当是<b>同步字段</b>（{@code SynchedEntityData}）而不是普通字段：内容模块的
     * 攻击起手判定跑在服务端，而动画只在客户端播放。这正是 GeckoLib 4.x 的
     * {@code triggerAnim} 在"服务端调用、客户端不见反应"上踩过的坑。</p>
     */
    public interface Animated extends ImpAnimatedEntity {

        /** 本实体登记的动画集（实现方在构造期填好并持有）。 */
        ImpAnimatedEntity.AnimationSet animationSet();

        /**
         * 取走某个一次性控制器的触发令牌。
         *
         * @param controllerName 一次性控制器的名字（即 {@code AnimationSet#oneShot} 的第一个参数）
         * @return 自上次调用以来令牌发生了变化则 {@code true}
         */
        boolean consumeAnimationTrigger(String controllerName);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 动画驱动（版本无关）
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 把 {@link ImpAnimatedEntity.AnimationSet} 与 {@link ImpGeoRig.Rig} 连起来：每帧求值控制器
     * 判定、选循环动画、处理一次性触发，最后推进并写回 {@code ModelPart}。
     *
     * <p>替代 {@code GeoRigController}（原因见类 javadoc）。刻意<b>不持有实体引用</b>：
     * 实体每帧由渲染状态传进来，避免渲染器与逻辑侧出现悬垂引用。</p>
     */
    public static final class Driver {

        private final ImpGeoRig.Rig rig;
        private boolean registered;

        public Driver(ImpGeoRig.Rig rig) {
            this.rig = rig;
        }

        /**
         * 每帧推进。
         *
         * @param entity          本帧被渲染的实体；{@code null} 时只做"复位到静止姿态"
         * @param isMoving        是否处于移动状态（GeckoLib {@code AnimationTest#isMoving} 的对应物）
         * @param limbSwing       行走相位
         * @param limbSwingAmount 行走幅度
         */
        public void tick(Entity entity, boolean isMoving, float limbSwing, float limbSwingAmount) {
            if (entity instanceof Animated animated) {
                ImpAnimatedEntity.AnimationSet set = animated.animationSet();
                this.ensureRegistered();
                for (Map.Entry<String, String> entry : set.oneShots().entrySet()) {
                    if (animated.consumeAnimationTrigger(entry.getKey())) {
                        this.rig.playOnce(entry.getValue());
                    }
                }
                this.rig.loop(
                    this.select(set, entity, isMoving, limbSwing, limbSwingAmount));
            }
            this.rig.update();
        }

        /** 选出优先级最高的、判定为非 {@code null} 的循环动画；都没有则 {@code null}（停）。 */
        private String select(ImpAnimatedEntity.AnimationSet set, Entity entity,
                              boolean isMoving, float limbSwing, float limbSwingAmount) {
            ImpAnimatedEntity.State state = new ImpAnimatedEntity.State(
                entity, isMoving, limbSwing, limbSwingAmount, entity.isInWater(), entity.onGround());
            String chosen = null;
            int bestPriority = Integer.MIN_VALUE;
            for (ImpAnimatedEntity.Controller controller : set.controllers().values()) {
                if (controller.oneShot() || controller.selector() == null) {
                    continue;
                }
                String key = controller.selector().select(state);
                if (key != null && controller.priority() > bestPriority) {
                    chosen = key;
                    bestPriority = controller.priority();
                }
            }
            return chosen;
        }

        /**
         * 按动画文件自身的键全量注册到状态机。
         *
         * <p>不按控制器名注册：selector 返回的是<b>动画键</b>，而一次性动画登记的是
         * {@code 控制器名 → 动画键}。把 {@code Rig.clips()} 的键原样注册成状态机键，
         * 两条路径就都指的是同一套名字，不会出现"注册了 A、播放 B"的错位。</p>
         */
        private void ensureRegistered() {
            if (this.registered) {
                return;
            }
            for (String clip : this.rig.clips().keySet()) {
                this.rig.register(clip, clip, 0);
            }
            this.registered = true;
        }
    }

    /**
     * 从资源包烘焙骨架。
     *
     * <p>先清空 {@code GeoResources} 的静态缓存：渲染器在<b>每次资源重载</b>时重建
     * （{@code EntityRenderDispatcher#onResourceManagerReload} → {@code EntityRenderers
     * #createEntityRenderers}），若不清缓存，改了 {@code .geo.json} 之后重载仍会拿到旧几何。
     * 几何/动画缺失时 {@link ImpGeoRig#load} 返回空骨架而不是 {@code null}，渲染器不会 NPE。</p>
     */
    public static ImpGeoRig.Rig loadRig(
            net.minecraft.server.packs.resources.ResourceManager manager, ImpId modelId) {
        org.polaris2023.wildwind.imp.client.bedrock.GeoResources.invalidateAll();
        return ImpGeoRig.load(manager, modelId);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 渲染状态
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的渲染状态：在 {@code LivingEntityRenderState} 之上补一个<b>实体引用</b>。
     *
     * <p>为什么必须补：26.x 的 {@code setupAnim}/{@code submit} 阶段拿不到实体，只有状态；
     * 而"走还是站"要问实体。渲染状态由 {@code EntityRenderer#createRenderState()} 按<b>实体实例</b>
     * 创建并复用，所以持有该实体的引用是安全的（不会跨实体）。</p>
     *
     * <p>行走信息不需要额外加字段：{@code LivingEntityRenderState} 自带
     * {@code walkAnimationPos} 与 {@code walkAnimationSpeed}（含 partialTick 插值）。</p>
     */
    @Since("26.1")
    public static class RenderState
        extends net.minecraft.client.renderer.entity.state.LivingEntityRenderState {

        /** 本帧被渲染的实体；由 {@link Renderer#extractRenderState} 填入。 */
        public LivingEntity entity;
    }

    /**
     * 1.20 / 1.21 的渲染状态：<b>占位类（能力降级）</b>。
     *
     * <p><b>不是等价实现</b>：1.20.6 / 1.21.1 的 Minecraft 没有 {@code EntityRenderState} 体系
     * （{@code EntityRenderer} 只有 1 个类型参数，渲染直接在 {@code render(T, ...)} 里完成），
     * 旧族的 {@code setupAnim} 直接收到实体本身，<b>不需要</b>任何状态载体。</p>
     *
     * <p>为什么仍然保留 {@link #entity} 字段：内容模块要写一个"把状态收窄成自己的实体类型"
     * 的访问器（{@code SpiderlingRenderState#spiderling()}），那个方法的源码必须在三个族里都能
     * 编译。旧的占位类若没有这个字段，内容模块就只能靠 {@code @Since} 分段——而版本注解只允许
     * 出现在 imp 里（{@code gradle/imp-boundary.txt} 规则 3）。所以这里保留同名同类型的字段，
     * 让那份源码逐字成立。</p>
     *
     * <p>⚠ <b>旧族上没有任何读者</b>：{@link #entity} 永远是 {@code null}，因为旧族的渲染路径
     * 既不创建渲染状态、也不往这里写。它存在只是为了"能编译"，不是"能用"。</p>
     */
    @Until("26.1")
    public static class RenderState {

        /** 仅用于让内容模块的收窄访问器在旧族也能编译；旧族恒为 {@code null}。 */
        public LivingEntity entity;
    }

    // ══════════════════════════════════════════════════════════════════════
    // 模型
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的模型基类：泛型参数是<b>渲染状态</b>，构造器<b>必须</b>拿到 root。
     *
     * <p>{@code Model(ModelPart, Function<Identifier, RenderType>)} 会在构造期缓存
     * {@code root.getAllParts()}（已用字节码核实），所以 rig 的根 {@code ModelPart} 必须就绪——
     * 这也是骨架在渲染器构造期（而非首次渲染时）烘焙的原因。</p>
     *
     * <p>{@code renderToBuffer} 在 26.x 是 {@code final}，本类不覆写；绘制由
     * {@code ModelFeatureRenderer} 在 submit 节点里完成。</p>
     *
     * @param <T> 实体类型（仅用于把状态里的 {@link RenderState#entity} 收敛到具体类型）
     * @param <S> 渲染状态类型
     */
    @Since("26.1")
    public static class Model<T extends LivingEntity, S extends RenderState>
        extends net.minecraft.client.model.EntityModel<S> {

        private final ImpGeoRig.Rig rig;
        private final Driver driver;
        private final ImpId texture;

        /**
         * @param rig     已烘焙的骨架（{@link #loadRig}）
         * @param texture 主纹理 id
         */
        public Model(ImpGeoRig.Rig rig, ImpId texture) {
            // 26.x：RenderType 工厂搬到了 rendertype.RenderTypes，且此处已在构造期确定 root
            super(rig.root(), tex -> net.minecraft.client.renderer.rendertype.RenderTypes.entityCutout(tex));
            this.rig = rig;
            this.driver = new Driver(rig);
            this.texture = texture;
        }

        /** 骨架门面（注册 / 循环 / 触发动画都从这里走）。 */
        public final ImpGeoRig.Rig rig() {
            return this.rig;
        }

        /** 主纹理 id。 */
        public final ImpId texture() {
            return this.texture;
        }

        /**
         * 每帧推进动画。
         *
         * <p>姿态复位由 {@code GeoResources.Binding#update()} 完成（它对自己管辖的每根骨骼先
         * {@code resetPose()} 再叠加动画变换），所以这里<b>不</b>调 {@code super.setupAnim}——
         * 那只会把同一件事再做一遍。</p>
         */
        @Override
        public void setupAnim(S state) {
            this.driver.tick(state.entity,
                state.walkAnimationSpeed > 0.02F,
                state.walkAnimationPos,
                state.walkAnimationSpeed);
        }
    }

    /**
     * 1.20 / 1.21 的模型基类：泛型参数是<b>实体</b>，{@code renderToBuffer} 必须自己实现。
     *
     * <p>{@code renderToBuffer} 的签名在这两族之间还不一样（1.21 收一个 {@code int} 颜色、
     * 1.20.6 收 4 个 float），所以再切一刀——两段同名、各带 {@code @Since}/{@code @Until}，
     * 由预处理器只保留一段。</p>
     */
    @Until("26.1")
    public static class Model<T extends LivingEntity, S extends RenderState>
        extends net.minecraft.client.model.EntityModel<T> {

        private final ImpGeoRig.Rig rig;
        private final Driver driver;
        private final ImpId texture;

        /** 见 26.x 分支的同名构造器（旧族构造器不收 root，root 由本类持有）。 */
        public Model(ImpGeoRig.Rig rig, ImpId texture) {
            super(tex -> net.minecraft.client.renderer.RenderType.entityCutoutNoCull(tex));
            this.rig = rig;
            this.driver = new Driver(rig);
            this.texture = texture;
        }

        /** 见 26.x 分支。 */
        public final ImpGeoRig.Rig rig() {
            return this.rig;
        }

        /** 见 26.x 分支。 */
        public final ImpId texture() {
            return this.texture;
        }

        /**
         * 每帧推进动画。
         *
         * <p>原版这 5 个参数是为"程序化摆动"准备的；Bedrock 动画由 rig 按真实时间推进，
         * 因此只用 {@code limbSwingAmount} 判断"在不在走"，其余忽略。</p>
         */
        @Override
        public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                              float ageInTicks, float netHeadYaw, float headPitch) {
            this.driver.tick(entity, limbSwingAmount > 0.02F, limbSwing, limbSwingAmount);
        }

        /** 1.21 的签名：{@code renderToBuffer(PoseStack, VertexConsumer, int, int, int)}。 */
        @Since(value = "1.21", until = "26.1")
        @Name("renderToBuffer")
        @Override
        public void renderToBufferTen21(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                        com.mojang.blaze3d.vertex.VertexConsumer buffer,
                                        int packedLight, int packedOverlay, int color) {
            this.rig.root().render(poseStack, buffer, packedLight, packedOverlay, color);
        }

        /** 1.20.6 的签名：{@code renderToBuffer(..., float, float, float, float)}。 */
        @Until("1.21")
        @Name("renderToBuffer")
        @Override
        public void renderToBufferTen20(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                        com.mojang.blaze3d.vertex.VertexConsumer buffer,
                                        int packedLight, int packedOverlay,
                                        float red, float green, float blue, float alpha) {
            this.rig.root().render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 实体渲染器
    // ══════════════════════════════════════════════════════════════════════

    /**
     * 26.x 的实体渲染器基类（两阶段：{@code extractRenderState} → {@code submit}）。
     *
     * <p>继承原版 {@code MobRenderer}：姿态、缩放、翻转、受伤泛红、光照、阴影与名牌全部由
     * {@code LivingEntityRenderer#submit} 完成，本类只决定"用哪个模型、哪张纹理"，
     * 并把实体引用填进渲染状态。</p>
     *
     * @param <T> 实体类型
     * @param <S> 渲染状态类型
     */
    @Since("26.1")
    public static class Renderer<T extends Mob, S extends RenderState>
        extends net.minecraft.client.renderer.entity.MobRenderer<T, S, Model<T, S>> {

        private final Supplier<S> stateFactory;
        private final Model<T, S> rigModel;

        /**
         * @param modelId      模型 id（相对 {@code geckolib/models/}，如 {@code entity/spiderling}）
         * @param texture      主纹理 id
         * @param shadow       阴影半径
         * @param stateFactory 渲染状态工厂（内容模块传自己的状态子类构造器）
         */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, ImpId texture, float shadow, Supplier<S> stateFactory) {
            this(context, new Model<T, S>(loadRig(context.getResourceManager(), modelId), texture),
                shadow, stateFactory);
        }

        /** 用内容模块自己的模型实例（模型类要覆写钩子时走这个）。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        Model<T, S> model, float shadow, Supplier<S> stateFactory) {
            super(context, model, shadow);
            this.rigModel = model;
            this.stateFactory = stateFactory;
        }

        @Override
        public S createRenderState() {
            return this.stateFactory.get();
        }

        @Override
        public void extractRenderState(T entity, S state, float partialTick) {
            super.extractRenderState(entity, state, partialTick);
            // super() 之后状态里才可能有本渲染器自己的子类字段，这里补上实体引用
            state.entity = entity;
        }

        @Override
        public net.minecraft.resources.Identifier getTextureLocation(S state) {
            ImpId override = textureFor(asEntity(state));
            return (override == null ? this.rigModel.texture() : override).unwrap();
        }

        /**
         * 纹理选择钩子：返回 {@code null} 表示用模型默认纹理。
         *
         * <p>参数统一成<b>实体</b>（而不是渲染状态），这样同一个方法签名在两族都能用——
         * 26.x 从渲染状态里取出实体再传进来，旧族本来就有实体。签名统一后，内容模块
         * 只需覆写<b>一个</b>方法，不必写版本注解。</p>
         */
        protected ImpId textureFor(T entity) {
            return null;
        }

        /**
         * 体型缩放钩子：返回缩放倍率，{@code 1.0F} 表示原样。
         *
         * <p>替代 GeckoLib 的 {@code scaleModelForRender}。26.x 在 {@link #submit} 里应用，
         * 旧族在 {@code scale(...)} 里应用。</p>
         */
        protected float scaleFor(T entity) {
            return 1.0F;
        }

        /**
         * 附加旋转钩子：把额外的 Y/X 旋转（弧度）写进 {@code yawPitchShift}。
         *
         * <p>替代原版 {@code setupRotations(...)}（其签名两族不同）。26.x 在
         * {@link #submit} 中应用，旧族在 {@code setupRotations} 中应用。</p>
         */
        protected void rotationFor(T entity, float[] yawPitchShift) {
            // 默认不改
        }

        /** 从渲染状态里取出实体（{@code extractRenderState} 已填入）。 */
        @SuppressWarnings("unchecked")
        private T asEntity(S state) {
            return state == null ? null : (T) state.entity;
        }

        @Override
        public void submit(S state, com.mojang.blaze3d.vertex.PoseStack poseStack,
                           net.minecraft.client.renderer.SubmitNodeCollector collector,
                           net.minecraft.client.renderer.state.level.CameraRenderState camera) {
            T entity = asEntity(state);
            float scale = scaleFor(entity);
            if (scale != 1.0F) {
                poseStack.scale(scale, scale, scale);
            }
            float[] yawPitchShift = new float[] {0F, 0F, 0F};
            rotationFor(entity, yawPitchShift);
            if (yawPitchShift[0] != 0F) {
                poseStack.mulPose(com.mojang.math.Axis.YP.rotation(yawPitchShift[0]));
            }
            if (yawPitchShift[1] != 0F) {
                poseStack.mulPose(com.mojang.math.Axis.XP.rotation(yawPitchShift[1]));
            }
            if (yawPitchShift[2] != 0F) {
                poseStack.translate(0.0D, 0.0D, yawPitchShift[2]);
            }
            super.submit(state, poseStack, collector, camera);
        }

        /** 本渲染器持有的模型（内容模块要直连 rig 时用）。 */
        public Model<T, S> rigModel() {
            return this.rigModel;
        }

        /** 骨架门面：注册 / 循环 / 触发动画都从这里走。 */
        public ImpGeoRig.Rig rig() {
            return this.rigModel.rig();
        }

        /**
         * 追加一层"只画发光纹理"的图层（替代 GeckoLib 的 {@code AutoGlowingGeoLayer}）。
         *
         * <p>26.x 的图层是<b>提交式</b>的，且 {@code submitModel} 会把 {@code setupAnim}
         * 延迟到绘制阶段。这里刻意提交<b>整个模型</b>而不是 {@code submitModelPart}：后者内部用的是
         * {@code Model.Simple}，其 {@code setupAnim} 是空实现，会在"多个同类实体交替提交"时沿用
         * 上一个实体算出的姿态。提交整模型则会在绘制这一节点前重新按本状态求值姿态。</p>
         */
        public Renderer<T, S> withGlowLayer(ImpId glowTexture) {
            this.addLayer(new net.minecraft.client.renderer.entity.layers.RenderLayer<
                    S, Model<T, S>>(this) {
                @Override
                public void submit(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   net.minecraft.client.renderer.SubmitNodeCollector collector,
                                   int packedLight, S state, float yRot, float xRot) {
                    // order(1)：与原版 EyesLayer 一致，压在实体本体之后绘制
                    collector.order(1).submitModel(
                        this.getParentModel(), state, poseStack,
                        net.minecraft.client.renderer.rendertype.RenderTypes.eyes(glowTexture.unwrap()),
                        packedLight,
                        net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                        -1,
                        null);
                }
            });
            return this;
        }
    }

    /**
     * 1.20 / 1.21 的实体渲染器基类（单阶段：{@code render(T, ...)}）。
     *
     * <p>旧族的 {@code LivingEntityRenderer#render} 会先把 {@code model.setupAnim(...)} 调完
     * 再渲染本体与图层，所以动画与图层都用现成的姿态，不需要任何提交缓冲。
     * {@code stateFactory} 只为"构造调用逐字一致"而保留，旧族没有渲染状态阶段，它不会被调用。</p>
     */
    @Until("26.1")
    public static class Renderer<T extends Mob, S extends RenderState>
        extends net.minecraft.client.renderer.entity.MobRenderer<T, Model<T, S>> {

        /** 保留但旧族不使用：1.20.6 / 1.21.1 没有 {@code EntityRenderState} 体系。 */
        private final Supplier<S> stateFactory;
        private final Model<T, S> rigModel;

        /** 见 26.x 分支的同名构造器。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        ImpId modelId, ImpId texture, float shadow, Supplier<S> stateFactory) {
            this(context, new Model<T, S>(loadRig(context.getResourceManager(), modelId), texture),
                shadow, stateFactory);
        }

        /** 见 26.x 分支的同名构造器。 */
        public Renderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context,
                        Model<T, S> model, float shadow, Supplier<S> stateFactory) {
            super(context, model, shadow);
            this.rigModel = model;
            this.stateFactory = stateFactory;
        }

        @Override
        public net.minecraft.resources.ResourceLocation getTextureLocation(T entity) {
            ImpId override = textureFor(entity);
            return (override == null ? this.rigModel.texture() : override).unwrap();
        }

        /** 见 26.x 分支的 {@code textureFor}——签名两族一致，都是实体。 */
        protected ImpId textureFor(T entity) {
            return null;
        }

        /** 见 26.x 分支的 {@code scaleFor}。 */
        protected float scaleFor(T entity) {
            return 1.0F;
        }

        /** 见 26.x 分支的 {@code rotationFor}。 */
        protected void rotationFor(T entity, float[] yawPitchShift) {
            // 默认不改
        }

        /**
         * 旧族的缩放钩子落在 {@code scale(...)} 上。
         *
         * <p>1.20.6 与 1.21.1 的 {@code LivingEntityRenderer#scale} 签名相同
         * （{@code (T, PoseStack, float)}），所以这一处不需要再分族。</p>
         */
        @Override
        protected void scale(T entity, com.mojang.blaze3d.vertex.PoseStack poseStack, float partialTick) {
            float scale = scaleFor(entity);
            if (scale != 1.0F) {
                poseStack.scale(scale, scale, scale);
            }
            super.scale(entity, poseStack, partialTick);
        }

        /**
         * 旧族的附加旋转钩子落在 {@code setupRotations(...)} 上。
         *
         * <p>1.20.6 与 1.21.1 的签名一致：{@code (T, PoseStack, float ageInTicks,
         * float bodyYaw, float yaw, float partialTick)}——六个参数。</p>
         */
        @Override
        protected void setupRotations(T entity, com.mojang.blaze3d.vertex.PoseStack poseStack,
                                      float ageInTicks, float bodyYaw, float yaw, float partialTick) {
            super.setupRotations(entity, poseStack, ageInTicks, bodyYaw, yaw, partialTick);
            float[] yawPitchShift = new float[] {0F, 0F, 0F};
            rotationFor(entity, yawPitchShift);
            if (yawPitchShift[0] != 0F) {
                poseStack.mulPose(com.mojang.math.Axis.YP.rotation(yawPitchShift[0]));
            }
            if (yawPitchShift[1] != 0F) {
                poseStack.mulPose(com.mojang.math.Axis.XP.rotation(yawPitchShift[1]));
            }
            if (yawPitchShift[2] != 0F) {
                poseStack.translate(0.0D, 0.0D, yawPitchShift[2]);
            }
        }

        /** 见 26.x 分支。 */
        public Model<T, S> rigModel() {
            return this.rigModel;
        }

        /** 见 26.x 分支。 */
        public ImpGeoRig.Rig rig() {
            return this.rigModel.rig();
        }

        /**
         * 追加发光图层。
         *
         * <p>旧族是<b>即时绘制式</b>的：{@code getBuffer} + {@code ModelPart#render}。
         * 这里用 4 参 {@code render}（1.20.6 与 1.21.1 都有），避开两族颜色参数不一致
         * （1.21 是 {@code int}、1.20.6 是 4 个 float）的坑。</p>
         */
        public Renderer<T, S> withGlowLayer(ImpId glowTexture) {
            this.addLayer(new net.minecraft.client.renderer.entity.layers.RenderLayer<
                    T, Model<T, S>>(this) {
                @Override
                public void render(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   net.minecraft.client.renderer.MultiBufferSource buffers,
                                   int packedLight, T entity,
                                   float limbSwing, float limbSwingAmount, float partialTick,
                                   float ageInTicks, float netHeadYaw, float headPitch) {
                    com.mojang.blaze3d.vertex.VertexConsumer buffer = buffers.getBuffer(
                        net.minecraft.client.renderer.RenderType.eyes(glowTexture.unwrap()));
                    this.getParentModel().rig().root().render(
                        poseStack, buffer, packedLight,
                        net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
                }
            });
            return this;
        }
    }
}
