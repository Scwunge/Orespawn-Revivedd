package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRotator;
import danger.orespawn.entity.Rotator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRotator}: ModelRotator(0.25F), shadow 0.1*1.0, scale 1.0.
 * Texture: rotatortexture.png
 */
@OnlyIn(Dist.CLIENT)
public class RotatorRenderer extends MobRenderer<Rotator, ModelRotator> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rotatortexture.png");
    /** Gold ClientProxy: {@code new RenderRotator(new ModelRotator(0.25F), 0.1F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public RotatorRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelRotator(context.bakeLayer(ModelRotator.LAYER_LOCATION), 0.25F),
                0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Rotator entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Rotator entity) {
        return TEXTURE;
    }
}
