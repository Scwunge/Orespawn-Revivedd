package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.client.model.ModelTriffid;
import danger.orespawn.entity.Triffid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTriffid}: ModelTriffid(1.0F), shadow 0.3*1.0, scale 1.0.
 * Texture: triffidtexture.png (gold Triffidtexture.png).
 * Gold model applies {@code GL11.glRotatef(-90, 0, 1, 0)} before draw — mirrored in
 * {@link #setupRotations}.
 */
@OnlyIn(Dist.CLIENT)
public class TriffidRenderer extends MobRenderer<Triffid, ModelTriffid> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/triffidtexture.png");
    /** Gold ClientProxy: {@code new RenderTriffid(new ModelTriffid(1.0F), 0.3F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public TriffidRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTriffid(context.bakeLayer(ModelTriffid.LAYER_LOCATION), 1.0F),
                0.3F * MODEL_SCALE);
    }

    @Override
    protected void scale(Triffid entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    /**
     * Gold {@code ModelTriffid} rotates the mesh -90° about Y before drawing.
     * Without this, the plant faces sideways relative to movement.
     */
    @Override
    protected void setupRotations(
            Triffid entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
    }

    @Override
    public ResourceLocation getTextureLocation(Triffid entity) {
        return TEXTURE;
    }
}
