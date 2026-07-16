package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelPitchBlack;
import danger.orespawn.entity.PitchBlack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderPitchBlack}: ModelPitchBlack(0.65F), shadow 1.25*1.0, scale = entity scale.
 * Texture: {@code textures/entity/pitchblacktexture.png} (gold PitchBlacktexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class PitchBlackRenderer extends MobRenderer<PitchBlack, ModelPitchBlack> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/pitchblacktexture.png");

    public PitchBlackRenderer(EntityRendererProvider.Context context) {
        // gold ClientProxy: new RenderPitchBlack(new ModelPitchBlack(0.65F), 1.25F, 1.0F)
        super(context, new ModelPitchBlack(context.bakeLayer(ModelPitchBlack.LAYER_LOCATION), 0.65F), 1.25F);
    }

    @Override
    protected void scale(PitchBlack entity, PoseStack poseStack, float partialTick) {
        float pscale = entity.getPitchBlackScale();
        poseStack.scale(pscale, pscale, pscale);
    }

    @Override
    public ResourceLocation getTextureLocation(PitchBlack entity) {
        return TEXTURE;
    }
}
