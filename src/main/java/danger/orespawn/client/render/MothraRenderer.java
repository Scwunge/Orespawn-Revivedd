package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelMothra;
import danger.orespawn.entity.Mothra;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderMothra} + {@code ModelButterfly(0.1F, 8.0F)}.
 * <p>
 * Gold order inside the living render: flip Y → translate(0,-1.501) → model
 * {@code translate(0,-scale)} → {@code scale(scale)}. In 1.21 {@link MobRenderer}
 * the -1.501 runs <em>after</em> {@link #scale}, so we bake the gold stack as:
 * {@code T(-1.501 - scale) * S(scale) * T(+1.501)} so the post -1.501 restores gold.
 * That puts the body ~1 block above the feet (center of the 5×2 hitbox).
 */
@OnlyIn(Dist.CLIENT)
public class MothraRenderer extends MobRenderer<Mothra, ModelMothra> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/eyemoth.png");
    /** LivingEntityRenderer always applies this after {@link #scale}. */
    private static final float LER_BODY_OFFSET = 1.501F;

    public MothraRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMothra(context.bakeLayer(ModModelLayers.MOTHRA), 0.1F), 0.0F);
    }

    @Override
    public boolean shouldRender(Mothra entity, Frustum camera, double camX, double camY, double camZ) {
        return entity.shouldRender(camX, camY, camZ);
    }

    @Override
    protected void scale(Mothra entity, PoseStack poseStack, float partialTick) {
        float s = Mothra.RENDER_SCALE;
        // Equivalent to gold: after flip, T(-1.501)*T(-s)*S(s) once LER adds T(-1.501) after us
        poseStack.translate(0.0F, -LER_BODY_OFFSET - s, 0.0F);
        poseStack.scale(s, s, s);
        poseStack.translate(0.0F, LER_BODY_OFFSET, 0.0F);
    }

    @Override
    public void render(
            Mothra entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        entity.setInvisible(false);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Mothra entity) {
        return TEXTURE;
    }
}
