package org.polaris2023.wildwind.hfas.client;

import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/** Geometry/UVs from the supplied Blockbench files; animation is inherited from PigModel. */
public final class MuddyPigModel extends PigModel {
    public MuddyPigModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition adultLayer() {
        return adultLayer(CubeDeformation.NONE, true);
    }

    public static LayerDefinition saddleLayer() {
        return adultLayer(new CubeDeformation(0.5F), false);
    }

    private static LayerDefinition adultLayer(CubeDeformation deformation, boolean flower) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder head = CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4, -4, -8, 8, 8, 8, deformation)
                .texOffs(16, 16).addBox(-2, 0, -9, 4, 3, 1, deformation);
        if (flower) {
            head.texOffs(24, -4).addBox(0, -10, -9, 0, 6, 6)
                    .texOffs(24, 2).addBox(-3, -10, -6, 6, 6, 0);
        }
        root.addOrReplaceChild("head", head, PartPose.offset(0, 10, -7));
        // Recenter the authored rotated body without changing its transformed geometry or UVs.
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8)
                .addBox(-5, -8, -4, 10, 16, 8, deformation),
                PartPose.offsetAndRotation(0, 14, 0, (float) (Math.PI / 2), 0, 0));
        leg(root, "right_hind_leg", -3, 18, 6, 4, 6, 0, 16, deformation);
        leg(root, "left_hind_leg", 3, 18, 6, 4, 6, 0, 16, deformation);
        leg(root, "right_front_leg", -3, 18, -6, 4, 6, 0, 16, deformation);
        leg(root, "left_front_leg", 3, 18, -6, 4, 6, 0, 16, deformation);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition babyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        // Blockbench Java coordinates are (-x, 24-y, z). Preserve the baby's authored offset.
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.5F, -3, -4.5F, 7, 6, 9), PartPose.offset(-0.5F, 19, 0.5F));
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 15).addBox(-3.5F, -5, -5, 7, 6, 6)
                .texOffs(6, 27).addBox(-1.5F, -2, -6, 3, 2, 1)
                .texOffs(0, 14).addBox(-0.5F, -9, -5, 0, 4, 3)
                .texOffs(0, 17).addBox(-2, -9, -3, 3, 4, 0), PartPose.offset(-0.5F, 19, -2));
        leg(root, "left_hind_leg", 2, 22, 4, 2, 2, 0, 0, CubeDeformation.NONE);
        leg(root, "right_hind_leg", -3, 22, 4, 2, 2, 23, 0, CubeDeformation.NONE);
        leg(root, "left_front_leg", 2, 22, -3, 2, 2, 0, 4, CubeDeformation.NONE);
        leg(root, "right_front_leg", -3, 22, -3, 2, 2, 23, 4, CubeDeformation.NONE);
        return LayerDefinition.create(mesh, 32, 32);
    }

    private static void leg(PartDefinition root, String name, float x, float y, float z,
            float width, float height, int u, int v, CubeDeformation deformation) {
        root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(u, v)
                .addBox(-width / 2, 0, -width / 2, width, height, width, deformation), PartPose.offset(x, y, z));
    }
}
