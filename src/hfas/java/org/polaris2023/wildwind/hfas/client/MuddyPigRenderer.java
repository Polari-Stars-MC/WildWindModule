package org.polaris2023.wildwind.hfas.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.entity.MuddyPig;

public final class MuddyPigRenderer extends MobRenderer<MuddyPig, PigRenderState, MuddyPigModel> {
    public static final ModelLayerLocation ADULT = layer("main");
    public static final ModelLayerLocation BABY = layer("baby");
    public static final ModelLayerLocation SADDLE = layer("saddle");
    private static final Identifier ADULT_TEXTURE = texture("pig_muddy");
    private static final Identifier BABY_TEXTURE = texture("pig_muddy_baby");
    private final AdultAndBabyModelPair<MuddyPigModel> models;

    public MuddyPigRenderer(EntityRendererProvider.Context context) {
        super(context, new MuddyPigModel(context.bakeLayer(ADULT)), 0.7F);
        models = new AdultAndBabyModelPair<>(model, new MuddyPigModel(context.bakeLayer(BABY)));
        addLayer(new SimpleEquipmentLayer<>(this, context.getEquipmentRenderer(),
                EquipmentClientInfo.LayerType.PIG_SADDLE, state -> state.saddle,
                new MuddyPigModel(context.bakeLayer(SADDLE)), null));
    }

    @Override
    public void submit(PigRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        model = models.getModel(state.isBaby);
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    public Identifier getTextureLocation(PigRenderState state) {
        return state.isBaby ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public PigRenderState createRenderState() {
        return new PigRenderState();
    }

    @Override
    public void extractRenderState(MuddyPig entity, PigRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "muddy_pig"), name);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(HFASMod.MOD_ID, "textures/entity/pig/" + name + ".png");
    }
}
