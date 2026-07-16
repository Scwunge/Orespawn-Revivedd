package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelVortex;
import danger.orespawn.entity.Vortex;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderVortex}: ModelVortex(0.25F), shadow 0.1*1.0, scale 1.0.
 * Texture: vortextexture.png
 */
@OnlyIn(Dist.CLIENT)
public class VortexRenderer extends MobRenderer<Vortex, ModelVortex> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/vortextexture.png");
    /** Gold ClientProxy: {@code new RenderVortex(new ModelVortex(0.25F), 0.1F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public VortexRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelVortex(context.bakeLayer(ModelVortex.LAYER_LOCATION), 0.25F),
                0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Vortex entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Vortex entity) {
        return TEXTURE;
    }
}
