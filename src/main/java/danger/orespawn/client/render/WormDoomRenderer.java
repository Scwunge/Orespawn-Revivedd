package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelWormDoom;
import danger.orespawn.entity.WormDoom;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelWormDoom.render} multi-pass (CF 1.12):
 * head + teeth once, then <b>100 body segments</b> along the position trail.
 * <p>
 * Previous port only drew stacked cubes at one point → looked like a single cube.
 */
@OnlyIn(Dist.CLIENT)
public class WormDoomRenderer extends MobRenderer<WormDoom, ModelWormDoom> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/wormdoomtexture.png");

    public WormDoomRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWormDoom(context.bakeLayer(ModModelLayers.WORM_DOOM)), 0.0F);
    }

    @Override
    public void render(
            WormDoom entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        if (entity.isInvisible()) {
            return;
        }

        poseStack.pushPose();

        // Standard living-entity orientation (same base as LivingEntityRenderer)
        float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float bob = entity.tickCount + partialTick;
        // 1.21.1: setupRotations(entity, pose, bob, yBodyRot, partialTick, scale)
        this.setupRotations(entity, poseStack, bob, bodyYaw, partialTick, 1.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        float age = entity.tickCount + partialTick;
        this.model.setupAnim(entity, age, 0.0F, age, 0.0F, 0.0F);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTick));

        // --- Head + teeth (gold worm_scale 2 + entity yaw/pitch already via setupRotations) ---
        poseStack.pushPose();
        poseStack.scale(ModelWormDoom.WORM_SCALE, ModelWormDoom.WORM_SCALE, ModelWormDoom.WORM_SCALE);
        this.model.renderHead(poseStack, vc, packedLight, overlay);
        poseStack.popPose();

        // --- 100-pass body trail (gold for-loop) ---
        // Trail deltas are world-space offsets relative to head (lpos[0]).
        // After living Y-flip, apply as local translate (gold glTranslated after flip).
        for (int i = 0; i < 100; i++) {
            double dx = entity.lposx[0] - entity.lposx[i];
            double dy = entity.lposy[0] - entity.lposy[i];
            double dz = entity.lposz[0] - entity.lposz[i];

            poseStack.pushPose();
            // Y flipped in parent matrix: world +Y is local -Y
            poseStack.translate(dx, -dy, dz);

            if (entity.rotyaw[i] != 0.0) {
                poseStack.mulPose(Axis.YP.rotationDegrees((float) entity.rotyaw[i]));
            }
            if (entity.rotpitch[i] != 0.0) {
                poseStack.mulPose(Axis.XP.rotationDegrees((float) entity.rotpitch[i]));
            }

            double scale1 = 1.0;
            if (i > 25) {
                scale1 = (75.0 - (i - 25)) / 75.0;
            }
            scale1 *= 2.0 - Math.cos(Math.toRadians(i * 3.0));
            if (scale1 < 0.01) {
                scale1 = 0.01;
            }

            float ws = ModelWormDoom.WORM_SCALE;
            poseStack.scale(
                    (float) (scale1 * ws),
                    (float) (scale1 * ws),
                    (float) (1.5 * ws));

            this.model.renderBodySegment(poseStack, vc, packedLight, overlay);

            if (i > 75) {
                // gold: glTranslatef(0, 1/worm_scale, 0) then second body pair
                poseStack.translate(0.0F, 1.0F / ws, 0.0F);
                this.model.renderBodySegment(poseStack, vc, packedLight, overlay);
            }

            poseStack.popPose();
        }

        poseStack.popPose();

        // name tag etc. when needed
        if (this.shouldShowName(entity)) {
            this.renderNameTag(entity, entity.getDisplayName(), poseStack, buffer, packedLight, partialTick);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(WormDoom entity) {
        return TEXTURE;
    }
}
