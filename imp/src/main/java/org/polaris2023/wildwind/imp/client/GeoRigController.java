package org.polaris2023.wildwind.imp.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.client.bedrock.GeoAnimationState;

import java.util.Map;

/**
 * 把一个 {@link ImpAnimatedEntity.AnimationSet} 与一个 {@link ImpGeoRig.Rig} 桥接起来。
 *
 * <p>职责：每帧根据实体状态求值控制器判定、驱动状态机、把变换写回 {@code ModelPart}。
 * 这是「实体逻辑」与「渲染输出」之间唯一的连接点，因此放在 imp 里共享给三个内容模块。</p>
 *
 * <p>设计上刻意<b>不持有实体引用</b>：渲染器只持有 controller 的求值结果，
 * 避免渲染线程与逻辑线程之间出现悬垂引用。</p>
 */
public final class GeoRigController {

    private final ImpGeoRig.Rig rig;
    private final ImpAnimatedEntity.AnimationSet animations;
    private ImpAnimatedEntity entity;
    private boolean registered;

    public GeoRigController(ImpGeoRig.Rig rig, ImpAnimatedEntity.AnimationSet animations) {
        this.rig = rig;
        this.animations = animations;
    }

    /** 绑定实体（渲染器每帧传入当前要渲染的实体）。 */
    public void bind(Entity entity) {
        if (entity instanceof ImpAnimatedEntity animated) {
            this.entity = animated;
            if (!this.registered) {
                // 只在第一次绑定时把动画注册进状态机：注册表本身与实体无关
                for (Map.Entry<String, String> entry : this.animations.oneShots().entrySet()) {
                    this.rig.register(entry.getKey(), entry.getValue(), 0);
                }
                for (ImpAnimatedEntity.Controller controller : this.animations.controllers().values()) {
                    // 一次性控制器的动画已在上面的循环里注册，这里跳过
                    if (controller.oneShot()) {
                        continue;
                    }
                    // 循环控制器：selector 返回的是**动画文件里的键**（如 "move.walk"），
                    // 所以必须把 clips 里每条动画都按它自己的键注册进状态机，
                    // 否则 looping(key) 查不到 entry，循环动画永远不播。
                    for (String clipKey : this.rig.clips().keySet()) {
                        this.rig.register(clipKey, clipKey, controller.priority());
                    }
                }
                this.registered = true;
            }
        } else {
            this.entity = null;
        }
    }

    /**
     * 每帧推进。
     *
     * @param limbSwing       行走相位
     * @param limbSwingAmount 行走幅度
     */
    public void tick(float limbSwing, float limbSwingAmount) {
        if (this.entity == null) {
            this.rig.update();
            return;
        }
        Entity nativeEntity = (Entity) this.entity;
        boolean moving = limbSwingAmount > 0.02F;
        boolean inWater = nativeEntity.isInWater();
        boolean onGround = nativeEntity.onGround();
        ImpAnimatedEntity.State state = new ImpAnimatedEntity.State(
            nativeEntity, moving, limbSwing, limbSwingAmount, inWater, onGround);

        // 选出优先级最高的、判定为非 null 的循环动画
        String chosen = null;
        int bestPriority = Integer.MIN_VALUE;
        for (ImpAnimatedEntity.Controller controller : this.animations.controllers().values()) {
            if (controller.oneShot() || controller.selector() == null) {
                continue;
            }
            String key = controller.selector().select(state);
            if (key != null && controller.priority() > bestPriority) {
                chosen = key;
                bestPriority = controller.priority();
            }
        }
        if (chosen != null) {
            this.rig.loop(chosen);
        } else {
            this.rig.loop(null);
        }
        this.rig.update();
    }

    /** 触发一次性动画（对应 GeckoLib 的 {@code triggerAnim}）。 */
    public void trigger(String animationKey) {
        this.rig.playOnce(animationKey);
    }

    public ImpGeoRig.Rig rig() {
        return this.rig;
    }

    public ModelPart root() {
        return this.rig.root();
    }

    public GeoAnimationState state() {
        return this.rig.state();
    }

    /** 便捷：给 {@link ImpAnimatedEntity} 的默认触发实现用。 */
    public static void triggerOn(ImpAnimatedEntity entity, String animationKey, Map<Entity, GeoRigController> pool) {
        GeoRigController controller = pool.get((Entity) entity);
        if (controller != null) {
            controller.trigger(animationKey);
        }
    }

    /** 主纹理解析结果（供渲染器查询）。 */
    public record Skinned(ImpId texture, float shadowRadius) {}

    /** 判断实体是否是活体（渲染器需要在两种基类间选择）。 */
    public static boolean isLiving(Entity entity) {
        return entity instanceof LivingEntity;
    }
}
