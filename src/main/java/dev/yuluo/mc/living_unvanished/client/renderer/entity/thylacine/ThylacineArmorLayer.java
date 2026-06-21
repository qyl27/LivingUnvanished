package dev.yuluo.mc.living_unvanished.client.renderer.entity.thylacine;

import com.geckolib.cache.model.GeoBone;
import com.geckolib.cache.model.GeoQuad;
import com.geckolib.cache.model.GeoVertex;
import com.geckolib.cache.model.cuboid.CuboidGeoBone;
import com.geckolib.cache.model.cuboid.GeoCube;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.geckolib.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.yuluo.mc.living_unvanished.client.renderer.entity.ModDataTickets;
import dev.yuluo.mc.living_unvanished.entity.thylacine.Thylacine;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ThylacineArmorLayer extends GeoRenderLayer<Thylacine, Void, LivingEntityRenderState> {
    private static final Identifier ARMOR_TEXTURE = IdHelper.modLoc("textures/entity/thylacine_armor.png");
    private static final Identifier ARMOR_TINT_TEXTURE = IdHelper.modLoc("textures/entity/thylacine_armor_tint.png");
    private static final float ARMOR_INFLATION = 0.2F / 16.0F;

    public ThylacineArmorLayer(ThylacineRenderer renderer) {
        super(renderer);
    }

    @Override
    public void submitRenderTask(RenderPassInfo<LivingEntityRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (!renderPassInfo.willRender() || renderPassInfo.renderState().isBaby) {
            return;
        }

        ItemStack bodyArmor = renderPassInfo.getOrDefaultGeckolibData(ModDataTickets.BODY_ARMOR_ITEM, ItemStack.EMPTY);
        if (!bodyArmor.is(Items.WOLF_ARMOR)) {
            return;
        }

        this.submitInflatedArmor(renderPassInfo, renderTasks.order(1), ARMOR_TEXTURE, 0xFFFFFFFF);
        this.submitInflatedArmor(
            renderPassInfo,
            renderTasks.order(2),
            ARMOR_TINT_TEXTURE,
            ARGB.opaque(DyedItemColor.getOrDefault(bodyArmor, 0xFFFFFF)));
    }

    private void submitInflatedArmor(
        RenderPassInfo<LivingEntityRenderState> renderPassInfo,
        OrderedSubmitNodeCollector renderTasks,
        Identifier texture,
        int color
    ) {
        RenderType renderType = this.renderer.getRenderType(renderPassInfo.renderState(), texture);
        if (renderType == null) {
            return;
        }

        int packedLight = renderPassInfo.packedLight();
        int packedOverlay = renderPassInfo.packedOverlay();
        renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType, (pose, vertexConsumer) -> {
            PoseStack poseStack = renderPassInfo.poseStack();

            poseStack.pushPose();
            poseStack.last().set(pose);
            renderPassInfo.renderPosed(() -> this.renderInflatedModel(renderPassInfo, poseStack, vertexConsumer, packedLight, packedOverlay, color));
            poseStack.popPose();
        });
    }

    private void renderInflatedModel(
        RenderPassInfo<LivingEntityRenderState> renderPassInfo,
        PoseStack poseStack,
        VertexConsumer vertexConsumer,
        int packedLight,
        int packedOverlay,
        int color
    ) {
        for (GeoBone bone : renderPassInfo.model().topLevelBones()) {
            poseStack.pushPose();
            RenderUtil.prepMatrixForBoneAndUpdateListeners(poseStack, bone, renderPassInfo);
            this.renderInflatedBone(renderPassInfo, poseStack, vertexConsumer, packedLight, packedOverlay, color, bone);
            this.renderInflatedChildren(renderPassInfo, poseStack, vertexConsumer, packedLight, packedOverlay, color, bone);
            poseStack.popPose();
        }
    }

    private void renderInflatedChildren(
        RenderPassInfo<LivingEntityRenderState> renderPassInfo,
        PoseStack poseStack,
        VertexConsumer vertexConsumer,
        int packedLight,
        int packedOverlay,
        int color,
        GeoBone parent
    ) {
        if (parent.frameSnapshot != null && parent.frameSnapshot.areChildrenHidden()) {
            return;
        }

        for (GeoBone child : parent.children()) {
            poseStack.pushPose();
            RenderUtil.prepMatrixForBoneAndUpdateListeners(poseStack, child, renderPassInfo);
            this.renderInflatedBone(renderPassInfo, poseStack, vertexConsumer, packedLight, packedOverlay, color, child);
            this.renderInflatedChildren(renderPassInfo, poseStack, vertexConsumer, packedLight, packedOverlay, color, child);
            poseStack.popPose();
        }
    }

    private void renderInflatedBone(
        RenderPassInfo<LivingEntityRenderState> renderPassInfo,
        PoseStack poseStack,
        VertexConsumer vertexConsumer,
        int packedLight,
        int packedOverlay,
        int color,
        GeoBone bone
    ) {
        if (!(bone instanceof CuboidGeoBone cuboidBone) || bone.frameSnapshot != null && bone.frameSnapshot.isHidden()) {
            return;
        }

        for (GeoCube cube : cuboidBone.cubes) {
            poseStack.pushPose();
            this.renderInflatedCube(poseStack, vertexConsumer, packedLight, packedOverlay, color, cube);
            poseStack.popPose();
        }
    }

    private void renderInflatedCube(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, GeoCube cube) {
        cube.translateToPivotPoint(poseStack);
        cube.rotate(poseStack);
        cube.translateAwayFromPivotPoint(poseStack);

        Matrix3f normalisedPoseState = poseStack.last().normal();
        Matrix4f poseState = new Matrix4f(poseStack.last().pose());
        GeoQuad[] quads = cube.quads();
        if (quads == null) {
            return;
        }

        for (GeoQuad quad : quads) {
            if (quad == null) {
                continue;
            }

            Vector3f normal = normalisedPoseState.transform(quad.normalVec());

            RenderUtil.fixInvertedFlatCube(cube, normal);
            this.renderInflatedQuad(poseState, normal, vertexConsumer, packedLight, packedOverlay, color, quad);
        }
    }

    private void renderInflatedQuad(
        Matrix4f pose,
        Vector3f normal,
        VertexConsumer vertexConsumer,
        int packedLight,
        int packedOverlay,
        int color,
        GeoQuad quad
    ) {
        for (GeoVertex vertex : quad.vertices()) {
            Vector4f position = pose.transform(new Vector4f(
                vertex.posX() + quad.normalX() * ARMOR_INFLATION,
                vertex.posY() + quad.normalY() * ARMOR_INFLATION,
                vertex.posZ() + quad.normalZ() * ARMOR_INFLATION,
                1));

            vertexConsumer.addVertex(
                position.x(), position.y(), position.z(), color,
                vertex.texU(), vertex.texV(), packedOverlay, packedLight,
                normal.x(), normal.y(), normal.z());
        }
    }
}
