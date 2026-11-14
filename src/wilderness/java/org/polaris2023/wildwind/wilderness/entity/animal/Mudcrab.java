package org.polaris2023.wildwind.wilderness.entity.animal;

import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.polaris2023.wildwind.wilderness.entity.ModSpawnPlacementTypes;
import org.polaris2023.wildwind.core.entity.WindupAttackMob;
import org.polaris2023.wildwind.core.entity.ai.goal.ChargingMeleeAttackGoal;
import org.polaris2023.wildwind.imp.client.ImpAnimatedEntity;
import org.polaris2023.wildwind.imp.client.ImpRig;
import org.polaris2023.wildwind.wilderness.registry.*;
import org.polaris2023.wildwind.wilderness.tag.ModBlockTags;
import org.polaris2023.wildwind.wilderness.tag.ModItemTags;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;

/**
 * 泥沼蟹生物实体<p>
 *
 * @author Mon-Landis
 * @author safe_saffawa
 */
public class Mudcrab extends Animal implements Bucketable, WindupAttackMob, ImpRig.Animated {
	/**
	 * 泥沼蟹使用的自定义生成位置类型喵~
	 */
	public static final SpawnPlacementType SPAWN_PLACEMENT = new SpawnPlacementType() {

		@Override
		public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
			return ModSpawnPlacementTypes.IN_WATER_GROUND.isSpawnPositionOk(level, pos, entityType)
					|| SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(level, pos, entityType);
		}

		@Override
		public BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
			return level.getFluidState(pos).is(Tags.Fluids.WATER) ? pos : SpawnPlacementTypes.ON_GROUND.adjustSpawnPosition(level, pos);
		}
	};

	/** 攻击动画的控制器名（与 {@code AnimationSet#oneShot} 的第一个参数一致）。 */
	public static final String ATTACK_CONTROLLER = "Attack";
	/** 攻击动画的触发键（与 {@code AnimationSet#oneShot} 的第二个参数一致）。 */
	public static final String ATTACK_TRIGGER = "attack";
	/** 打招呼动画的控制器名。 */
	public static final String GREETING_CONTROLLER = "Greeting";
	/** 打招呼动画的触发键。 */
	public static final String GREETING_TRIGGER = "greeting";

	protected static final float CLIENT_SIDE_MAX_MODEL_ROT = 90.0f;
	protected static final int CLIENT_SIDE_MODEL_ROT_TIME = 5;
	protected static final float CLIENT_SIDE_MODEL_ROT_PER_TICK = CLIENT_SIDE_MAX_MODEL_ROT / CLIENT_SIDE_MODEL_ROT_TIME;
	private static final EntityDataAccessor<Holder<MudcrabVariant>> VARIANT_ID = SynchedEntityData.defineId(Mudcrab.class, ModEntityDataSerializers.MUDCRAB_VARIANT.get());
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Mudcrab.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(Mudcrab.class, EntityDataSerializers.BOOLEAN);

	/**
	 * 一次性动画的触发令牌。
	 *
	 * <p>取值为"已触发过的次数"。渲染侧每帧比对它是否变化来检测<b>边沿</b>，
	 * 所以连续两次攻击/打招呼会得到不同的值，动画不会被去重吞掉。</p>
	 *
	 * <p>为什么必须走 {@link SynchedEntityData}：{@link #prepareAttack} 与打招呼判定
	 * 都跑在<b>服务端</b>，而动画只在<b>客户端</b>播放。用普通字段服务端置位后客户端永远看不到
	 * ——这正是 GeckoLib 4.x 的 {@code triggerAnim} 踩过的坑。</p>
	 */
	private static final EntityDataAccessor<Integer> DATA_ATTACK_TRIGGER = SynchedEntityData.defineId(Mudcrab.class, EntityDataSerializers.INT);

	/** 打招呼动画的触发令牌（同上）。 */
	private static final EntityDataAccessor<Integer> DATA_GREETING_TRIGGER = SynchedEntityData.defineId(Mudcrab.class, EntityDataSerializers.INT);

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final int TOTAL_AIR_SUPPLY = 7200;
	private static final int START_FIND_WATER_AIR_SUPPLY = 2400;
	private static final int REHYDRATE_AIR_SUPPLY = 1800;

	/** 本实体登记的动画集（构造期填好，之后只读）。 */
	private final ImpAnimatedEntity.AnimationSet animations = new ImpAnimatedEntity.AnimationSet();

	/**
	 * 渲染侧已经消费到的攻击令牌值（只增不减，永不回落）。
	 *
	 * <p>初值 <b>0</b> 而不是 -1：{@code DATA_ATTACK_TRIGGER} 的同步默认值就是 0，
	 * 若初值取 -1，实体刚出现的第一帧就会被当成"令牌变化过"而多播一次攻击动画。</p>
	 */
	private int consumedAttackTrigger = 0;

	/** 渲染侧已经消费到的打招呼令牌值（同 {@link #consumedAttackTrigger}）。 */
	private int consumedGreetingTrigger = 0;

	/**
	 * 客户端渲染时模型的 Y 轴附加旋转偏移喵~
	 */
	public float clientSideModelYRotOffset;
	/**
	 * 客户端渲染时上一刻模型的 Y 轴附加旋转偏移喵~
	 */
	public float clientSidePreModelYRotOffset = this.clientSideModelYRotOffset;
	/**
	 * 客户端渲染时模型的 X 轴附加旋转偏移喵~
	 */
	public float clientSideModelXRotOffset;
	/**
	 * 客户端渲染时上一刻模型的 X 轴附加旋转偏移喵~
	 */
	public float clientSidePreModelXRotOffset = this.clientSideModelXRotOffset;
	private int preparingToAttack = -1;
	private int greetingTicks = -1;

	/**
	 * 创建泥沼蟹实体喵~
	 *
	 * @param entityType 实体类型喵~
	 * @param level 所在世界喵~
	 */
	public Mudcrab(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		this.setPathfindingMalus(PathType.WATER, 0.0f);
		this.moveControl = new MudcrabMoveControl(this);
		// registerAnimations 在接口里是"每次实例化调用一次"的约定，这里显式完成
		this.registerAnimations(this.animations);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25f));
		this.goalSelector.addGoal(2, new MudcrabFindWaterGoal(this, 1.25f, 12, 80));
		this.goalSelector.addGoal(3, new ChargingMeleeAttackGoal<>(this, 1.2f, true));
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0f));
		this.goalSelector.addGoal(5, new TemptGoal(this, 1.1f, stack -> stack.is(ModItemTags.MUDCRAB_FOOD), false));
		this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1f));
		this.goalSelector.addGoal(7, new MudcrabRandomStrollGoal(this, 1.0f));
		this.goalSelector.addGoal(8, new MudcrabFloatGoal(this, 0.04f));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 7.0f));
		this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CaveSpider.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Silverfish.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true));
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new MudcrabPathNavigation(this, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT_ID, VariantUtils.getDefaultOrAny(this.registryAccess(), MudcrabVariant.TEMPERATE));
		builder.define(FROM_BUCKET, false);
		builder.define(CLIMBING, false);
		builder.define(DATA_ATTACK_TRIGGER, 0);
		builder.define(DATA_GREETING_TRIGGER, 0);
	}

	/**
	 * 计算指定位置对泥沼蟹寻路的吸引值喵~
	 *
	 * @param pos 待评估的位置喵~
	 * @param level 当前关卡读取器喵~
	 * @return 越高表示越适合作为行走目标喵~
	 */
	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level) {
		BlockState state = level.getBlockState(pos.below());
		if (state.is(ModBlockTags.EntityAbout.MUDCRAB_PREFERRED_WANDER_BLOCKS)) return 10.0f;
		if (state.is(ModBlockTags.EntityAbout.MUDCRAB_SPAWNABLE_IN_WATER_GROUND)) return 5.0f;

		return level.getPathfindingCostFromLightLevels(pos);
	}

	/**
	 * 判断当前实体在所在位置是否被阻挡喵~
	 *
	 * @param level 当前关卡读取器喵~
	 * @return 未被阻挡时返回 true 喵~
	 */
	@Override
	public boolean checkSpawnObstruction(LevelReader level) {
		return level.isUnobstructed(this);
	}

	/**
	 * 判断当前是否处于攻击蓄力阶段喵~
	 *
	 * @return 处于攻击蓄力时返回 true 喵~
	 */
	public boolean isPreparingToAttack() {
		return this.preparingToAttack != -1;
	}

	/**
	 * 判断当前是否处于打招呼动画阶段喵~
	 *
	 * @return 正在打招呼时返回 true 喵~
	 */
	public boolean isGreeting() {
		return this.greetingTicks != -1;
	}

	@Override
	public void setPreparingToAttackTick(int tick) {
		this.preparingToAttack = tick;
	}

	@Override
	public int getPreparingToAttackTick() {
		return this.preparingToAttack;
	}

	@Override
	public int getPreparationToAttackDuration() {
		return 12;
	}

	@Override
	public boolean prepareAttack(LivingEntity entity) {
		boolean result = WindupAttackMob.super.prepareAttack(entity);
		if (result) this.triggerAnimation(ATTACK_CONTROLLER, ATTACK_TRIGGER);
		return result;
	}

	protected void updateClientSideVisuals() {
		this.clientSidePreModelYRotOffset = this.clientSideModelYRotOffset;
		this.clientSidePreModelXRotOffset = this.clientSideModelXRotOffset;

		if (this.shouldRotModelY()) {
			if (this.clientSideModelYRotOffset < CLIENT_SIDE_MAX_MODEL_ROT) {
				this.clientSideModelYRotOffset += CLIENT_SIDE_MODEL_ROT_PER_TICK;
			}
		} else if (this.clientSideModelYRotOffset > 0.0 && this.clientSideModelXRotOffset <= 0.0f) {
			this.clientSideModelYRotOffset -= CLIENT_SIDE_MODEL_ROT_PER_TICK;
		}

		if (this.shouldRotModelX()) {
			if (this.clientSideModelXRotOffset < CLIENT_SIDE_MAX_MODEL_ROT) {
				this.clientSideModelXRotOffset += CLIENT_SIDE_MODEL_ROT_PER_TICK;
			}
		} else if (this.clientSideModelXRotOffset > 0.0) {
			this.clientSideModelXRotOffset -= CLIENT_SIDE_MODEL_ROT_PER_TICK;
		}
	}

	protected boolean shouldRotModelY() {
		Vec3 movement = this.getDeltaMovement();
		return ((this.onGround() && Math.abs(movement.x) + Math.abs(movement.z) / 2 > 0.015f && this.walkAnimation.speed() != 0.0f)
				|| (!this.onGround() && this.isClimbing() && Math.abs(movement.y) > 0.005f))
				&& !this.isVisuallySwimming()
				&& !this.isImmobile();
	}

	protected boolean shouldRotModelX() {
		return !this.onGround() && !this.isVisuallySwimming() && this.isClimbing();
	}

	/**
	 * 检查泥沼蟹是否可在水中地面生成喵~
	 *
	 * @param mudcrab 泥沼蟹实体类型喵~
	 * @param level 世界访问器喵~
	 * @param spawnType 生成类型喵~
	 * @param pos 生成位置喵~
	 * @param random 随机源喵~
	 * @return 满足生成条件时返回 true 喵~
	 */
	public static boolean checkMudcrabInWaterGroundSpawnRules(
			EntityType<? extends Mudcrab> mudcrab, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random
	) {
		if (!(EntitySpawnReason.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

		return level.getBlockState(pos.below()).is(ModBlockTags.EntityAbout.MUDCRAB_SPAWNABLE_IN_WATER_GROUND);
	}

	/**
	 * 检查泥沼蟹是否可在陆地生成喵~
	 *
	 * @param mudcrab 泥沼蟹实体类型喵~
	 * @param level 世界访问器喵~
	 * @param spawnType 生成类型喵~
	 * @param pos 生成位置喵~
	 * @param random 随机源喵~
	 * @return 满足生成条件时返回 true 喵~
	 */
	public static boolean checkMudcrabOnGroundSpawnRules(EntityType<? extends Mudcrab> mudcrab, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
		if (!(EntitySpawnReason.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos))) return false;

		return level.getBlockState(pos.below()).is(ModBlockTags.EntityAbout.MUDCRAB_SPAWNABLE_ON);
	}

	/**
	 * 创建泥沼蟹实体的基础属性喵~
	 *
	 * @return 属性构建器喵~
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0d)
				.add(Attributes.MOVEMENT_SPEED, 0.22d)
				.add(Attributes.ATTACK_DAMAGE, 10.0d)
				.add(Attributes.STEP_HEIGHT, 1.0d)
				.add(Attributes.TEMPT_RANGE, 16d);
	}

	/**
	 * 执行每刻更新并维持空气值变化喵~
	 */
	@Override
	public void baseTick() {
		int currentAirSupply = this.getAirSupply();
		super.baseTick();
		if (!this.isNoAi()) this.handleAirSupply(currentAirSupply);
	}

	/**
	 * 获取泥沼蟹最大空气值喵~
	 *
	 * @return 最大空气值喵~
	 */
	@Override
	public int getMaxAirSupply() {
		return TOTAL_AIR_SUPPLY;
	}

	/**
	 * 处理离水状态下的空气值变化喵~
	 *
	 * @param currentAirSupply 当前空气值喵~
	 */
	public void handleAirSupply(int currentAirSupply) {
		//isInWaterRainOrBubble已被删除，使用等效替换
		if (this.isAlive() && (!this.isInWaterOrRain() || this.getInBlockState().is(Blocks.BUBBLE_COLUMN))) {
			this.setAirSupply(currentAirSupply - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().dryOut(), 1.0F);
			}

			return;
		}

		this.setAirSupply(this.getMaxAirSupply());
	}

	/**
	 * 让泥沼蟹在接触到水后恢复部分空气值喵~
	 */
	public void rehydrate() {
		this.setAirSupply(Math.min(this.getAirSupply() + REHYDRATE_AIR_SUPPLY, this.getMaxAirSupply()));
	}

	/**
	 * 判断当前是否需要主动寻找水源喵~
	 *
	 * @return 需要找水时返回 true 喵~
	 */
	public boolean needWater() {
		return this.getAirSupply() < START_FIND_WATER_AIR_SUPPLY;
	}

	/**
	 * 获取水中移动减速系数喵~
	 *
	 * @return 水中减速系数喵~
	 */
	@Override
	protected float getWaterSlowDown() {
		return 0.98f;
	}

	/**
	 * 更新游泳状态喵~
	 */
	@Override
	public void updateSwimming() {
		if (!this.level().isClientSide()) {
			this.setSwimming(this.isEffectiveAi() && this.isInWater());
		}
	}

	/**
	 * 判断是否在视觉上表现为游泳喵~
	 *
	 * @return 是否视觉游泳喵~
	 */
	@Override
	public boolean isVisuallySwimming() {
		return this.isSwimming();
	}

	/**
	 * 判断是否会被流体推动喵~
	 *
	 * @param type 流体类型喵~
	 * @return 不会被推动喵~
	 */
	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	/**
	 * 判断是否需要自定义持久化喵~
	 *
	 * @return 需要时返回 true 喵~
	 */
	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.fromBucket();
	}

	/**
	 * 判断离玩家较远时是否应被清除喵~
	 *
	 * @param distanceToClosestPlayer 最近玩家距离喵~
	 * @return 不应远距离消失时返回 true 喵~
	 */
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	/**
	 * 处理玩家与泥沼蟹的交互喵~
	 *
	 * @param player 玩家喵~
	 * @param hand 使用的手喵~
	 * @return 交互结果喵~
	 */
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
	}

	/**
	 * 完成泥沼蟹生成时的初始化喵~
	 *
	 * @param level 服务端世界喵~
	 * @param difficulty 生成难度喵~
	 * @param spawnType 生成类型喵~
	 * @param spawnGroupData 群组生成数据喵~
	 * @return 生成后的群组数据喵~
	 */
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
		if (spawnType == EntitySpawnReason.BUCKET) return spawnGroupData;

		getVariantByPos((ServerLevelAccessor) this.level(),this.blockPosition()).ifPresent(this::setVariant);

		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	/**
	 * 根据生物群系获取默认泥沼蟹变种喵~
	 *
	 * @return 对应变种喵~
	 */
	protected static Optional<Holder.Reference<MudcrabVariant>> getVariantByPos(ServerLevelAccessor level, BlockPos pos) {
//		if (biome.is(ModBiomeTags.EntityGen.MUDCRABS_WARM)) return VariantUtils.getDefaultOrAny(access,MudcrabVariant.WARM);
//		if (biome.is(ModBiomeTags.EntityGen.MUDCRABS_COLD)) return VariantUtils.getDefaultOrAny(access,MudcrabVariant.COLD);
//
//		return VariantUtils.getDefaultOrAny(access,MudcrabVariant.TEMPERATE);

		return VariantUtils.selectVariantToSpawn(SpawnContext.create(level, pos), DatadrivenRegistryKey.MUDCRAB_VARIANT);
	}

	/**
	 * 判断泥沼蟹是否无视默认行动限制喵~
	 *
	 * @return 处于攻击或打招呼时返回 true 喵~
	 */
	@Override
	protected boolean isImmobile() {
		return super.isImmobile() || this.isPreparingToAttack() || this.isGreeting();
	}

	/**
	 * 判断指定物品是否可作为泥沼蟹食物喵~
	 *
	 * @param stack 待检查物品喵~
	 * @return 可食用时返回 true 喵~
	 */
	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ModItemTags.MUDCRAB_FOOD);
	}

	/**
	 * 判断该泥沼蟹是否来自桶装释放喵~
	 *
	 * @return 来自桶装时返回 true 喵~
	 */
	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	/**
	 * 设置该泥沼蟹是否来自桶装释放喵~
	 *
	 * @param fromBucket 是否来自桶装喵~
	 */
	@Override
	public void setFromBucket(boolean fromBucket) {
		this.entityData.set(FROM_BUCKET, fromBucket);
	}

	/**
	 * 将泥沼蟹数据写入桶物品标签喵~
	 *
	 * @param stack 蟹桶物品栈喵~
	 */
	@Override
	public void saveToBucketTag(ItemStack stack) {
		Bucketable.saveDefaultDataToBucketTag(this, stack);
		CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, tag -> {
			tag.put("Variant", MudcrabVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant()).getOrThrow());
			tag.putInt("Age", this.getAge());
			tag.putBoolean("AgeLocked", this.isAgeLocked());
		});
	}

	/**
	 * 从桶物品标签中读取泥沼蟹数据喵~
	 *
	 * @param tag 桶数据标签喵~
	 */
	@Override
	public void loadFromBucketTag(CompoundTag tag) {
		this.setAge(tag.getIntOr("Age", 0));
		this.setAgeLocked(tag.getBooleanOr("AgeLocked", false));
		if (tag.contains("Variant")) {
			MudcrabVariant.CODEC
					.parse(NbtOps.INSTANCE, tag.get("Variant"))
					.resultOrPartial(LOGGER::error)
					.ifPresent(this::setVariant);
		}
	}

	/**
	 * 获取装载该实体所对应的桶物品喵~
	 *
	 * @return 蟹桶物品栈喵~
	 */
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack((ItemLike) ModItems.MUDCRAB_BUCKET);
	}

	/**
	 * 获取头部 X 轴旋转上限喵~
	 *
	 * @return 头部 X 轴旋转上限喵~
	 */
	@Override
	public int getMaxHeadXRot() {
		return 0;
	}

	/**
	 * 获取头部 Y 轴旋转上限喵~
	 *
	 * @return 头部 Y 轴旋转上限喵~
	 */
	@Override
	public int getMaxHeadYRot() {
		return 0;
	}

	/**
	 * 获取被装桶时播放的音效喵~
	 *
	 * @return 装桶音效喵~
	 */
	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL;
	}

	@Override @Nullable
	protected SoundEvent getAmbientSound() {
		return super.getAmbientSound();
	}

	@Override @Nullable
	protected SoundEvent getDeathSound() {
		return super.getDeathSound();
	}

	@Override @Nullable
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return super.getHurtSound(damageSource);
	}

	@Override
	protected SoundEvent getSwimSplashSound() {
		return super.getSwimSplashSound();
	}

	@Override
	protected SoundEvent getSwimSound() {
		return super.getSwimSound();
	}

	@Override
	protected void ageBoundaryReached() {
		super.ageBoundaryReached();
		if (!this.isBaby() && ((ServerLevel)this.level()).getServer().getGameRules().get(GameRules.MOB_DROPS)) {
			this.spawnAtLocation((ServerLevel)this.level(), ModItems.CRAB_CLAW);
		}
	}

	/**
	 * 生成繁殖后的幼年泥沼蟹实体喵~
	 *
	 * @param level 服务端世界喵~
	 * @param otherParent 另一只亲代喵~
	 * @return 新生成的幼年泥沼蟹，无法生成时返回 null 喵~
	 */
	@Override @Nullable
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		Mudcrab mudcrab = ModEntities.MUDCRAB.get().create(level, EntitySpawnReason.BREEDING);
		if (mudcrab != null) {
			var variantList = WeightedList.<Holder<MudcrabVariant>>builder();
			variantList.add(this.getVariant());
			if (otherParent instanceof Mudcrab otherMudcrab) {
				variantList.add(otherMudcrab.getVariant());
			}
			getVariantByPos((ServerLevelAccessor) this.level(),this.blockPosition()).ifPresent(variantList::add);

			Holder<MudcrabVariant> variant = variantList
					.build()
					.getRandom(mudcrab.getRandom())
					.orElse(MudcrabVariant.lookup(this.level().registryAccess(),MudcrabVariant.TEMPERATE));
			mudcrab.setVariant(variant);
		}

		return mudcrab;
	}

	@Override
	public @Nullable <T> T get(DataComponentType<? extends T> type) {
		return type.equals(ModDataComponents.MUDCRAB_VARIANT.get()) ? (T) getVariant() : super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		this.applyImplicitComponentIfPresent(components, ModDataComponents.MUDCRAB_VARIANT.get());
		super.applyImplicitComponents(components);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == ModDataComponents.MUDCRAB_VARIANT.get()) {
			this.setVariant((Holder<MudcrabVariant>) value);
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.store("MudcarbVariant", MudcrabVariant.CODEC, this.getVariant());
		output.putBoolean("FromBucket", this.fromBucket());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setVariant(input.read("MudcarbVariant", MudcrabVariant.CODEC).orElse(MudcrabVariant.lookup(this.level().registryAccess(),MudcrabVariant.TEMPERATE)));
		this.setFromBucket(input.getBooleanOr("FromBucket", false));
	}

	/**
	 * 设置泥沼蟹的变种喵~
	 *
	 * @param variant 变种喵~
	 */
	public void setVariant(Holder<MudcrabVariant> variant) {
		this.entityData.set(VARIANT_ID, variant);
	}

	/**
	 * 获取泥沼蟹当前的变种喵~
	 *
	 * @return 当前变种喵~
	 */
	public Holder<MudcrabVariant> getVariant() {
		return this.entityData.get(VARIANT_ID);
	}

	// ══════════════════════════════════════════════════════════════════════
	// 动画契约（ImpAnimatedEntity + ImpRig.Animated）
	// ══════════════════════════════════════════════════════════════════════

	/**
	 * 注册动画规则——与迁移前 GeckoLib 的四个控制器逐条对应。
	 *
	 * <h2>迁移前的写法（GeckoLib 5.x）</h2>
	 * <pre>{@code
	 * controllers.add(new AnimationController<>("Move", 3, this::moveAnimController));
	 * controllers.add(new AnimationController<>("Hurt", this::hurtAnimController));
	 * controllers.add(new AnimationController<>("Attack", st -> PlayState.STOP)
	 *         .triggerableAnim("attack", DefaultAnimations.ATTACK_SWING));
	 * controllers.add(new AnimationController<>("Greeting", st -> PlayState.STOP)
	 *         .triggerableAnim("greeting", GREETING_ANIM));
	 *
	 * PlayState moveAnimController(AnimationTest<Mudcrab> state) {
	 *     return this.isInWater()
	 *             ? state.setAndContinue(DefaultAnimations.SWIM)          // move.swim
	 *             : state.isMoving() || this.isClimbing()
	 *               ? state.setAndContinue(DefaultAnimations.WALK)        // move.walk
	 *               : state.setAndContinue(DefaultAnimations.IDLE);       // misc.idle
	 * }
	 *
	 * PlayState hurtAnimController(AnimationTest<Mudcrab> state) {
	 *     if (this.isAlive() && this.hurtTime > 0) return state.setAndContinue(HURT_ANIM); // misc.hurt
	 *     return PlayState.STOP;
	 * }
	 * }</pre>
	 *
	 * <h2>搬过来的映射关系</h2>
	 * <table border="1">
	 *   <caption>GeckoLib → ImpRig</caption>
	 *   <tr><th>GeckoLib</th><th>这里</th></tr>
	 *   <tr><td>{@code state.isInWater()}</td><td>{@code state.isInWater()}（{@link ImpRig.Driver}
	 *       每帧从 {@code Entity#isInWater()} 取，与原写法同源）</td></tr>
	 *   <tr><td>{@code state.isMoving()}</td><td>{@code state.isMoving()}
	 *       （由 {@code walkAnimationSpeed} 判定，见 {@code ImpRig.Driver#tick}）</td></tr>
	 *   <tr><td>{@code this.isClimbing()}</td><td>从 {@code state.entity()} 强转回泥沼蟹后调
	 *       {@link #isClimbing()}（读的是同步字段 {@code CLIMBING}）</td></tr>
	 *   <tr><td>{@code PlayState.STOP}</td><td>selector 返回 {@code null}</td></tr>
	 *   <tr><td>{@code setAndContinue(DefaultAnimations.X)}</td><td>返回动画文件里的键
	 *       （{@code DefaultAnimations.SWIM/WALK/IDLE} 就是 {@code move.swim}/{@code move.walk}/
	 *       {@code misc.idle}，已用字节码核对常量）</td></tr>
	 * </table>
	 *
	 * <h2>为什么 Hurt 的优先级是 1、Move 是 0</h2>
	 * <p>GeckoLib 里每个控制器各自持有一条动画，按加入顺序叠加、后加入者覆盖同名骨骼，
	 * 所以 {@code Hurt}（后加入）会在受击时盖掉 {@code Move} 的姿态。{@link ImpRig.Driver}
	 * 只能选<b>一条</b>循环动画，因此这里用优先级表达同一件事：{@code Hurt}(1) &gt; {@code Move}(0)。
	 * 可观察行为一致——受击时播 {@code misc.hurt}，不播行走/待机。</p>
	 *
	 * <p>迁移前 {@code "Move"} 的构造器第二个实参 3 在 GeckoLib 5.x 里是<b>过渡 tick</b>
	 * （{@code AnimationController(String, int transitionTicks, handler)}，已用 javap 核对），
	 * 不是优先级；{@link ImpRig} 的过渡由 {@code GeoAnimationState} 的"换 clip 即重置时间"
	 * 近似表达，因此那个 3 不再需要搬运。</p>
	 */
	@Override
	public void registerAnimations(ImpAnimatedEntity.AnimationSet set) {
		// 移动 / 游泳 / 待机：三选一，永不停（对应旧 moveAnimController 的三条 setAndContinue）
		set.controller("Move", 0, state -> {
			Mudcrab mudcrab = asMudcrab(state);
			if (mudcrab == null) {
				return null;
			}
			if (state.isInWater()) {
				return "move.swim";
			}
			return state.isMoving() || mudcrab.isClimbing() ? "move.walk" : "misc.idle";
		});
		// 受击：存活且 hurtTime > 0 时播 misc.hurt，否则 null（= 旧的 PlayState.STOP）
		set.controller("Hurt", 1, state -> {
			Mudcrab mudcrab = asMudcrab(state);
			return mudcrab != null && mudcrab.isAlive() && mudcrab.hurtTime > 0 ? "misc.hurt" : null;
		});
		// 攻击 / 打招呼：由 triggerAnimation 插入，播完自动回落到循环动画
		set.oneShot(ATTACK_CONTROLLER, "attack.swing");
		set.oneShot(GREETING_CONTROLLER, "misc.greeting");
	}

	/** selector 收到的实体就是本类实例；类型不符时返回 {@code null}（防御，不抛异常）。 */
	private static @Nullable Mudcrab asMudcrab(ImpAnimatedEntity.State state) {
		return state.entity() instanceof Mudcrab mudcrab ? mudcrab : null;
	}

	/** 渲染器通过这个访问器拿到动画集（{@code registerAnimations} 不回传结果）。 */
	@Override
	public ImpAnimatedEntity.AnimationSet animationSet() {
		return this.animations;
	}

	/**
	 * 取走一次性动画的触发令牌。
	 *
	 * <p>只在令牌值与上次消费的不同时返回 {@code true} 一次。语义与 GeckoLib 的
	 * {@code triggerableAnim} 一致：一次触发对应一次播放，重复触发会重新从 0 播。</p>
	 */
	@Override
	public boolean consumeAnimationTrigger(String controllerName) {
		if (ATTACK_CONTROLLER.equals(controllerName)) {
			int current = this.entityData.get(DATA_ATTACK_TRIGGER);
			if (current == this.consumedAttackTrigger) {
				return false;
			}
			this.consumedAttackTrigger = current;
			return true;
		}
		if (GREETING_CONTROLLER.equals(controllerName)) {
			int current = this.entityData.get(DATA_GREETING_TRIGGER);
			if (current == this.consumedGreetingTrigger) {
				return false;
			}
			this.consumedGreetingTrigger = current;
			return true;
		}
		return false;
	}

	/**
	 * 触发一次性动画（对应 GeckoLib 的 {@code triggerAnim(controller, key)}）。
	 *
	 * @param controllerName 一次性控制器名（{@value #ATTACK_CONTROLLER} 或 {@value #GREETING_CONTROLLER}）
	 * @param animationKey   动画键（{@value #ATTACK_TRIGGER} 或 {@value #GREETING_TRIGGER}）
	 */
	public void triggerAnimation(String controllerName, String animationKey) {
		// 令牌自增而不是置 true：两次相邻触发会得到不同的值，边沿检测不会把它们合并
		if (ATTACK_CONTROLLER.equals(controllerName) && ATTACK_TRIGGER.equals(animationKey)) {
			this.entityData.set(DATA_ATTACK_TRIGGER, this.entityData.get(DATA_ATTACK_TRIGGER) + 1);
		} else if (GREETING_CONTROLLER.equals(controllerName) && GREETING_TRIGGER.equals(animationKey)) {
			this.entityData.set(DATA_GREETING_TRIGGER, this.entityData.get(DATA_GREETING_TRIGGER) + 1);
		}
	}

	/**
	 * 设置泥沼蟹当前是否处于攀爬状态喵~
	 *
	 * @param climbing 是否攀爬喵~
	 */
	public void setClimbing(boolean climbing) {
		this.entityData.set(CLIMBING, climbing);
	}

	/**
	 * 判断泥沼蟹当前是否处于攀爬状态喵~
	 *
	 * @return 攀爬中返回 true 喵~
	 */
	public boolean isClimbing() {
		return this.entityData.get(CLIMBING);
	}

	/**
	 * 判断当前实体是否可攀附喵~
	 *
	 * @return 攀爬中返回 true 喵~
	 */
	@Override
	public boolean onClimbable() {
		return this.isClimbing();
	}

	/**
	 * 执行泥沼蟹的 AI 逻辑与客户端视觉更新喵~
	 */
	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.isAlive()) return;

		if (this.level().isClientSide()) {
			this.updateClientSideVisuals();
			return;
		}

		if (!this.isEffectiveAi()) return;

		this.setClimbing(this.horizontalCollision);

		processPreparingAttack((ServerLevel)this.level() ,this);

		if (this.greetingTicks > 0) {
			if (--this.greetingTicks <= 0) {
				this.greetingTicks = -1;
			}
		}

		if (!this.isInWater() && !this.isGreeting() && this.tickCount % 2 == 0 && this.random.nextFloat() <= 0.001) {
			AABB box = this.getBoundingBox().inflate(6.0, 2.0, 6.0);
			for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
				if (this.level().getBlockState(pos).is(ModBlockTags.EntityAbout.MUDCRAB_PREFERRED_WANDER_BLOCKS)) {
					this.greetingTicks = 27;
					this.triggerAnimation(GREETING_CONTROLLER, GREETING_TRIGGER);
				}
			}
		}
	}

	/**
	 * 屏蔽默认跳跃状态设置，避免泥沼蟹出现不符合预期的跳跃表现喵~
	 *
	 * @param jumping 是否正在跳跃喵~
	 */
	@Override
	public void setJumping(boolean jumping) {
	}

	protected static class MudcrabFloatGoal extends Goal {
		private final Mob mob;
		private final float strength;

		public MudcrabFloatGoal(Mob mob, float strength) {
			this.mob = mob;
			this.strength = strength;
			this.mob.getNavigation().setCanFloat(true);
		}

		@Override
		public boolean canUse() {
			MoveControl moveControl = this.mob.getMoveControl();
			return this.mob.isInFluidType((entity,fluidType, height) -> height > this.mob.getBbHeight() && this.mob.canSwimInFluidType(fluidType))
					&& !this.mob.level().getFluidState(new BlockPos((int) moveControl.getWantedX(), (int) moveControl.getWantedY(), (int) moveControl.getWantedZ())).isEmpty();
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return false;
		}

		@Override
		public void tick() {
			if (this.mob.getRandom().nextFloat() < 0.8f) {
				this.mob.addDeltaMovement(new Vec3(0.0, this.strength, 0.0));
			}
		}
	}

	protected static class MudcrabMoveControl extends MoveControl<Mudcrab> {
		private final Mudcrab mudcrab;

		public MudcrabMoveControl(Mudcrab mudcrab) {
			super(mudcrab);
			this.mudcrab = mudcrab;
		}

		@Override
		public void tick() {
			if (!mudcrab.isInWater()) {
				super.tick();
				return;
			}

			if (this.operation == Operation.MOVE_TO && this.shouldContinueMoveTo()) {
				float modifiedSpeed = (float) (this.speedModifier * this.mudcrab.getAttributeValue(Attributes.MOVEMENT_SPEED));
				this.mudcrab.setSpeed(Mth.lerp(0.125F, this.mudcrab.getSpeed(), modifiedSpeed));
				double relativeX = this.wantedX - this.mudcrab.getX();
				double relativeY = this.wantedY - this.mudcrab.getY();
				double relativeZ = this.wantedZ - this.mudcrab.getZ();
				if (relativeY != 0.0) {
					double distance = Math.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);
					this.mudcrab.setDeltaMovement(this.mudcrab.getDeltaMovement()
							.add(0.0, (double) this.mudcrab.getSpeed() * (relativeY / distance) * 0.1, 0.0)
					);
				}

				if (relativeX != 0.0 || relativeZ != 0.0) {
					float yRot = (float) (Mth.atan2(relativeZ, relativeX) * 180.0F / (float) Math.PI) - CLIENT_SIDE_MAX_MODEL_ROT;
					this.mudcrab.setYRot(this.rotlerp(this.mudcrab.getYRot(), yRot, CLIENT_SIDE_MAX_MODEL_ROT));
					this.mudcrab.yBodyRot = this.mudcrab.getYRot();
				}
			}
		}

		private boolean shouldContinueMoveTo() {
			PathNavigation navigation = this.mudcrab.getNavigation();
			return navigation instanceof MudcrabPathNavigation mudcrabPathNavigation
					? !mudcrabPathNavigation.isDone() || (this.mudcrab.isInWater() && mudcrabPathNavigation.fallbackPos != null)
					: !navigation.isDone();
		}
	}

	protected static class MudcrabFindWaterGoal extends Goal {
		private final Mudcrab mudcrab;
		private final float speedModifier;
		private final int waterSearchRange;
		private final int interval;
		@Nullable
		private BlockPos waterPos;
		private int cooldown;

		public MudcrabFindWaterGoal(Mudcrab mudcrab, float speedModifier, int waterSearchRange, int interval) {
			this.mudcrab = mudcrab;
			this.speedModifier = speedModifier;
			this.waterSearchRange = waterSearchRange;
			this.interval = interval;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!this.mudcrab.needWater()) return false;

			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}

			for (BlockPos pos : BlockPos.betweenClosed(
					(int) (this.mudcrab.getX() - waterSearchRange),
					(int) this.mudcrab.getY() - waterSearchRange,
					(int) this.mudcrab.getZ() - waterSearchRange / 2,
					(int) this.mudcrab.getX() + waterSearchRange,
					(int) this.mudcrab.getY() + waterSearchRange / 2,
					(int) this.mudcrab.getZ() + waterSearchRange
			)) {
				if (this.mudcrab.level().getFluidState(pos).is(FluidTags.WATER)) {
					this.waterPos = pos;
					break;
				}
			}
			this.cooldown = this.interval;

			return waterPos != null;
		}

		@Override
		public void start() {
			this.mudcrab.getNavigation().moveTo(
					this.waterPos.getX(),
					this.waterPos.getY(),
					this.waterPos.getZ(),
					this.speedModifier
			);

			this.waterPos = null;
		}
	}

	protected static class MudcrabRandomStrollGoal extends RandomStrollGoal {
		public MudcrabRandomStrollGoal(PathfinderMob mob, double speedModifier) {
			super(mob, speedModifier);
		}

		@Override
		protected @Nullable Vec3 getPosition() {
			Level level = this.mob.level();
			Vec3 position = null;

			//时间系统重构使得直接判断是否为夜晚不可行 替换为主世界+时间判断
			if ((level.dimension().equals(Level.OVERWORLD) && level.getOverworldClockTime() % 24000L >= 13000L) || level.isRaining()) {
				position = LandRandomPos.getPos(this.mob, 20, 4, pos ->
						level.getBlockState(pos.above()).is(ModBlockTags.EntityAbout.MUDCRAB_PREFERRED_WANDER_BLOCKS)
								? 10.0
								: 0.0
				);
			}

			if (position == null) {
				position = DefaultRandomPos.getPos(this.mob, 10, 7);
			}

			return position;
		}
	}

	protected static class MudcrabPathNavigation extends GroundPathNavigation {
		private final Mudcrab mudcrab;
		@Nullable
		private BlockPos fallbackPos;

		public MudcrabPathNavigation(Mudcrab mudcrab, Level level) {
			super(mudcrab, level);
			this.mudcrab = mudcrab;
		}

		@Override
		protected PathFinder createPathFinder(int maxVisitedNodes) {
			this.nodeEvaluator = new AmphibiousNodeEvaluator(true);
			this.nodeEvaluator.setCanPassDoors(true);
			return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
		}

		@Override
		protected boolean canUpdatePath() {
			return true;
		}

		@Override
		protected Vec3 getTempMobPos() {
			return new Vec3(this.mob.getX(), this.mob.getY(0.5), this.mob.getZ());
		}

		@Override
		public Path createPath(BlockPos pos, int accuracy) {
			this.fallbackPos = pos;
			return super.createPath(pos, accuracy);
		}

		@Override
		public Path createPath(Entity entity, int accuracy) {
			this.fallbackPos = entity.blockPosition();
			return super.createPath(entity, accuracy);
		}

		@Override
		public boolean moveTo(Entity entity, double speed) {
			Path path = this.createPath(entity, 0);
			if (path != null) {
				return this.moveTo(path, speed);
			} else {
				this.fallbackPos = entity.blockPosition();
				this.speedModifier = speed;
				return true;
			}
		}

		@Override
		public void tick() {
			if (!this.isDone()) {
				super.tick();
				return;
			}

			if (forwardToFallbackPos()) {
				MoveControl moveControl = this.mob.getMoveControl();
				moveControl.setWantedPosition(this.fallbackPos.getX(), this.fallbackPos.getY(), this.fallbackPos.getZ(), this.speedModifier);
				return;
			}

			this.fallbackPos = null;
		}

		private boolean forwardToFallbackPos() {
			if (fallbackPos == null) return false;
			if (this.mudcrab.isClimbing()) return !this.mudcrab.verticalCollision;

			double horizontalDistance = fallbackPos.distToCenterSqr(mob.getX(), fallbackPos.getY(), mob.getZ());
			if (horizontalDistance < Mth.square(Math.max(this.mob.getBbWidth(), 1.0))) {
				BlockPos pos = this.mob.blockPosition();
				BlockState state = this.level.getBlockState(pos);
				if (state.getCollisionShape(level, pos).isEmpty()) {
					for (Direction direction : Direction.Plane.HORIZONTAL) {
						BlockPos relativePos = pos.relative(direction);
						if (!level.getBlockState(relativePos).getCollisionShape(level, relativePos).isEmpty()) {
							return true;
						}
					}
				}

				return false;
			}

			return true;
		}

		@Override
		protected double getGroundY(Vec3 vec) {
			return vec.y;
		}

		@Override
		protected boolean canMoveDirectly(Vec3 pos1, Vec3 pos2) {
			return this.mob.isInLiquid() && isClearForMovementBetween(this.mob, pos1, pos2, true);
		}

		@Override
		public boolean isStableDestination(BlockPos pos) {
			return !this.level.getBlockState(pos.below()).isAir();
		}
	}

}
