package org.polaris2023.wildwind.hfas.client.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.polaris2023.wildwind.hfas.client.entity.model.MudcrabModel;
import org.polaris2023.wildwind.hfas.client.entity.renderstate.MudcrabRenderState;
import org.polaris2023.wildwind.hfas.entity.animal.Mudcrab;

import javax.annotation.Nullable;

/**
 * 泥沼蟹实体渲染器喵~
 */
public class MudcrabRenderer extends GeoEntityRenderer<Mudcrab, MudcrabRenderState> {
	/**
	 * 创建泥沼蟹实体渲染器喵~
	 *
	 * @param renderManager 实体渲染上下文喵~
	 */
	public MudcrabRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MudcrabModel());
	}

	//TODO BEFORE PR 前端报错 需要监测编译时是否报错 GeoRenderState由geocklib注入到EntityRenderState类，不由用户开发者自行实现
	//TODO 含需要验证的内容:ai提到该方法存在注释"excluding when re-rendering the model as part of a GeoRenderLayer or external render call"，表示重渲染不会经过这一步，因此不必考虑ReRender情况。
	@Override
	public void scaleModelForRender(RenderPassInfo<MudcrabRenderState> renderPassInfo, float widthScale, float heightScale) {
		if (renderPassInfo.renderState().isBaby) {
			widthScale *= 0.6f;
			heightScale *= 0.6f;
		}

		super.scaleModelForRender(renderPassInfo, widthScale, heightScale);
	}

	@Override
	public void adjustRenderPose(RenderPassInfo<MudcrabRenderState> renderPassInfo) {
		MudcrabRenderState renderState = renderPassInfo.renderState();
		PoseStack poseStack = renderPassInfo.poseStack();

		poseStack.mulPose(Axis.YN.rotationDegrees(renderState.modelYRotOffset));
		float xRot = renderState.modelXRotOffset;
		if (xRot != 0.0f) {
			float xRotProgress = xRot / 90.0f;
			switch (renderState.motionDirection) {
				case NORTH -> poseStack.mulPose(Axis.ZN.rotationDegrees(xRot));
				case SOUTH -> poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
				case WEST -> poseStack.mulPose(Axis.XN.rotationDegrees(xRot));
				case EAST -> poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
			}
			float adjustY = -(renderState.boundingBoxHeight / 2.0f) * xRotProgress;
			float adjustZ = -(renderState.boundingBoxWidth / 2.0f) * xRotProgress;
			poseStack.translate(0.0f, adjustY, adjustZ);
		}

		super.adjustRenderPose(renderPassInfo);
	}

	//TODO 需要审查 考虑下面方法覆写的正确性与必要性 疑似存在使用GeoRenderState添加专用ticket实现信息处理的方案
	@Override
	public MudcrabRenderState createRenderState(Mudcrab animatable, @Nullable Void relatedObject) {
		return new MudcrabRenderState();
	}

	@Override
	protected void extractLivingEntityRenderState(LivingEntity entity, LivingEntityRenderState renderState, float partialTick, ItemModelResolver itemModelResolver) {
		super.extractLivingEntityRenderState(entity, renderState, partialTick, itemModelResolver);
		Mudcrab mudcrab = (Mudcrab)entity;
		MudcrabRenderState mudcrabRenderState = (MudcrabRenderState)renderState;

		mudcrabRenderState.variant = mudcrab.getVariant().value();
		mudcrabRenderState.modelYRotOffset = Mth.lerp(partialTick, mudcrab.clientSidePreModelYRotOffset, mudcrab.clientSideModelYRotOffset);
		mudcrabRenderState.modelXRotOffset = Mth.lerp(partialTick, mudcrab.clientSidePreModelXRotOffset, mudcrab.clientSideModelXRotOffset);
		mudcrabRenderState.motionDirection = mudcrab.getMotionDirection();
	}
}
