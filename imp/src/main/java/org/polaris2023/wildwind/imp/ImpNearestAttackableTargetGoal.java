package org.polaris2023.wildwind.imp;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 鐗堟湰鏃犲叧鐨勩€屾渶杩戝彲鏀诲嚮鐩爣銆嶇洰鏍囧熀绫伙紙ADR-0003锛夈€? *
 * <p>闂锛歿@code NearestAttackableTargetGoal} 鐨勩€岄澶栫瓫閫夋潯浠躲€嶅弬鏁板湪涓ゆ棌閲屾槸涓嶅悓鐨勭被鍨嬧€斺€?/p>
 * <ul>
 *   <li>26.x锛歿@code TargetingConditions.Selector}锛? *       鏂规硶鏄?{@code boolean test(LivingEntity entity, ServerLevel level)}锛?/li>
 *   <li>1.20 / 1.21锛歿@code java.util.function.Predicate<LivingEntity>}锛? *       鍙湁 {@code boolean test(LivingEntity entity)}锛?b>鎷夸笉鍒?level</b>銆?/li>
 * </ul>
 *
 * <p>绛惧悕閲屽甫鐗堟湰涓撳睘绫诲瀷锛屾墍浠ユ瀯閫犲櫒蹇呴』鐣欏湪 imp锛泏@link name} 璁╁唴瀹规ā鍧楀彧鍐欎竴濂? * 鏋勯€犲櫒璋冪敤銆傚唴瀹规ā鍧楃敤涓夋棌閫氱敤鐨?{@link ImpTargetSelector}锛堜袱涓弬鏁帮級涔﹀啓绛涢€夋潯浠躲€?/p>
 *
 * <p><b>闄嶇骇璇存槑</b>锛?.20 / 1.21 鐨?{@code Predicate} 娌℃湁 level 鍙傛暟锛? * 鏈被浼氭妸瀹炰綋鑷繁鎵€鍦ㄧ殑 {@link ServerLevel} 浼犺繘鍘伙紙涓嶅湪鏈嶅姟绔椂浼?{@code null}锛夈€? * 瀵广€屽彧鐪嬪疄浣撴湰韬姸鎬併€嶇殑鏉′欢锛堜緥濡傝閲忔瘮渚嬶級璇箟瀹屽叏涓€鑷达紱鍙湁纭疄闇€瑕? * 銆屾敾鍑昏€呮墍鍦ㄤ笘鐣屻€嶇殑鏉′欢鎵嶄細鍦ㄦ棫鐗堢湅鍒?{@code null}銆?/p>
 *
 * @param <T> 鐩爣瀹炰綋绫诲瀷
 */
public abstract class ImpNearestAttackableTargetGoal<T extends LivingEntity>
        extends net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<T> {

    /**
     * 鐩爣绛涢€夋潯浠讹紙璺ㄧ増鏈ǔ瀹氾級銆?     *
     * <p>26.x 涓嬪畠灏辨槸 {@code TargetingConditions.Selector} 鐨勫舰鐘讹紝
     * 鍥犳鍙互鐩存帴浠ユ柟娉曞紩鐢ㄤ紶鍏ャ€?/p>
     */
    @FunctionalInterface
    public interface ImpTargetSelector {
        boolean test(LivingEntity entity, ServerLevel level);
    }

    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee, boolean mustReach) {
        super(mob, targetType, mustSee, mustReach);
    }

    /** 26.x锛歿@code Selector} 涓?{@link ImpTargetSelector} 褰㈢姸鐩稿悓锛屽彲鐩存帴鏂规硶寮曠敤銆?*/
    @Since("26.1")
    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee,
                                             ImpTargetSelector selector) {
        super(mob, targetType, mustSee, selector::test);
    }

    /** 1.20 / 1.21锛氶€傞厤鍒?{@code Predicate<LivingEntity>}锛宭evel 鍙栬嚜瀹炰綋鑷韩锛堣绫绘敞閲婄殑闄嶇骇璇存槑锛夈€?*/
    @Until("26.1")
    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee,
                                             ImpTargetSelector selector) {
        super(mob, targetType, mustSee, entity -> resolveSelector(selector, entity));
    }

    /** 26.x锛氬甫闅忔満闂撮殧鐨勯噸杞姐€?*/
    @Since("26.1")
    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, int randomInterval,
                                             boolean mustSee, boolean mustReach,
                                             @javax.annotation.Nullable ImpTargetSelector selector) {
        super(mob, targetType, randomInterval, mustSee, mustReach, selector == null ? null : selector::test);
    }

    /** 1.20 / 1.21锛氬甫闅忔満闂撮殧鐨勯噸杞姐€?*/
    @Until("26.1")
    protected ImpNearestAttackableTargetGoal(Mob mob, Class<T> targetType, int randomInterval,
                                             boolean mustSee, boolean mustReach,
                                             @javax.annotation.Nullable ImpTargetSelector selector) {
        super(mob, targetType, randomInterval, mustSee, mustReach,
                selector == null ? null : entity -> resolveSelector(selector, entity));
    }

    /** 鏃х増閫傞厤锛氱敤瀹炰綋鑷韩鎵€鍦ㄧ殑 {@code ServerLevel} 琛ヤ笂缂哄け鐨?level 鍙傛暟銆?*/
    private static boolean resolveSelector(ImpTargetSelector selector, LivingEntity entity) {
        return selector.test(entity, entity.level() instanceof ServerLevel server ? server : null);
    }
}
