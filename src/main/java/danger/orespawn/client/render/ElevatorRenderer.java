package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelElevator;
import danger.orespawn.entity.Elevator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderElevator} — plain entity render (boat-style), <b>not</b> {@code MobRenderer}.
 * <p>
 * Gold draws at entity origin with {@code glScalef(-1,-1,1)} only. {@code MobRenderer} applies
 * the biped {@code translate(0,-1.501)} and put the flat deck through the rider's head.
 * HierarchicalModel cubes are already in block-space (pixel/16 baked).
 */
@OnlyIn(Dist.CLIENT)
public class ElevatorRenderer extends EntityRenderer<Elevator> {
    private final ModelElevator model;

    public ElevatorRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.25F; // gold field_76989_e
        this.model = new ModelElevator(context.bakeLayer(ModelElevator.LAYER_LOCATION));
    }

    @Override
    public void render(
            Elevator entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        poseStack.pushPose();
        // Prefer client look-matched yaw for local pilot (entityYaw lagged and rocked)
        float yaw = entity.getRenderYaw(partialTick);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));

        // Pitch/roll from client-smoothed flight angles (see Elevator.tickClient)
        // scale(-1,-1,1) flips X/Y — negate so bank matches the player.
        float flightPitch = entity.getFlightPitch(partialTick);
        float flightRoll = entity.getFlightRoll(partialTick);
        if (Math.abs(flightPitch) > 0.05F || Math.abs(flightRoll) > 0.05F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-flightPitch));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-flightRoll));
        }

        // gold boat rock about X (damage hit feedback)
        float f2 = entity.getTimeSinceHit() - partialTick;
        float f3 = entity.getDamageTaken() - partialTick;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(
                    Mth.sin(f2) * f2 * f3 / 10.0F * entity.getForwardDirection()));
        }

        // gold: glScalef(-1, -1, 1) — flips handedness of pitch/roll above (hence negated)
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTick, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Elevator entity) {
        return entity.getTexture();
    }
}
