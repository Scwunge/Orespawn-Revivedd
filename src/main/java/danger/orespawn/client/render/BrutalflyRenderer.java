package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelBrutalfly;
import danger.orespawn.entity.Brutalfly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelBrutalfly}: {@code translate(0,-12)} + {@code scale(12)}.
 * Same 1.21 LivingEntityRenderer -1.501 ordering fix as {@link MothraRenderer}.
 */
@OnlyIn(Dist.CLIENT)
public class BrutalflyRenderer extends MobRenderer<Brutalfly, ModelBrutalfly> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/brutalfly.png");
    private static final float LER_BODY_OFFSET = 1.501F;

    public BrutalflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBrutalfly(context.bakeLayer(ModModelLayers.BRUTALFLY), 0.1F), 0F);
    }

    @Override
    public boolean shouldRender(Brutalfly entity, Frustum camera, double camX, double camY, double camZ) {
        return entity.shouldRender(camX, camY, camZ);
    }

    @Override
    protected void scale(Brutalfly entity, PoseStack poseStack, float partialTick) {
        float s = Brutalfly.RENDER_SCALE;
        poseStack.translate(0.0F, -LER_BODY_OFFSET - s, 0.0F);
        poseStack.scale(s, s, s);
        poseStack.translate(0.0F, LER_BODY_OFFSET, 0.0F);
    }

    @Override
    public void render(
            Brutalfly entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        entity.setInvisible(false);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Brutalfly entity) {
        return TEXTURE;
    }
}
