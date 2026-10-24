package org.polaris2023.wildwind.wilderness.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import org.junit.jupiter.api.Test;
import org.joml.Vector3f;
import static org.junit.jupiter.api.Assertions.*;

class MuddyPigModelTest {
    @Test
    void adultAndBabyBakeWithNineAuthoredCubesAndCorrectBounds() {
        bounds(MuddyPigModel.adultLayer().bakeRoot(), new float[]{-5, 0, -16, 5, 24, 8});
        bounds(MuddyPigModel.babyLayer().bakeRoot(), new float[]{-4, 10, -8, 3, 24, 5});
    }

    private void bounds(ModelPart root, float[] expected) {
        int[] count = {0};
        float[] bounds = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        root.visit(new PoseStack(), (pose, path, index, cube) -> {
            count[0]++;
            for (float x : new float[]{cube.minX, cube.maxX})
                for (float y : new float[]{cube.minY, cube.maxY})
                    for (float z : new float[]{cube.minZ, cube.maxZ}) {
                        Vector3f v = pose.pose().transformPosition(new Vector3f(x / 16, y / 16, z / 16)).mul(16);
                        for (int axis = 0; axis < 3; axis++) {
                            bounds[axis] = Math.min(bounds[axis], v.get(axis));
                            bounds[axis + 3] = Math.max(bounds[axis + 3], v.get(axis));
                        }
                    }
        });
        assertEquals(9, count[0]);
        assertArrayEquals(expected, bounds, 0.0001F);
    }

    @Test
    void bothAgesUseVanillaPigHeadAndWalkAnimation() {
        for (var layer : new net.minecraft.client.model.geom.builders.LayerDefinition[]{
                MuddyPigModel.adultLayer(), MuddyPigModel.babyLayer()}) {
            var root = layer.bakeRoot();
            var vanillaRoot = PigModel.createBodyLayer(CubeDeformation.NONE).bakeRoot();
            var muddy = new MuddyPigModel(root);
            var vanilla = new PigModel(vanillaRoot);
            var state = new PigRenderState();
            state.xRot = 25;
            state.yRot = -30;
            state.walkAnimationPos = 2.5F;
            state.walkAnimationSpeed = 0.7F;
            muddy.setupAnim(state);
            vanilla.setupAnim(state);
            for (String part : new String[]{"head", "right_hind_leg", "left_hind_leg", "right_front_leg", "left_front_leg"}) {
                assertEquals(vanillaRoot.getChild(part).xRot, root.getChild(part).xRot);
                assertEquals(vanillaRoot.getChild(part).yRot, root.getChild(part).yRot);
            }
        }
    }
}
