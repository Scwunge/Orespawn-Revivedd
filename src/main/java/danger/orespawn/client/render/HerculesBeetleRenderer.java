package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelHerculesBeetle;
import danger.orespawn.entity.HerculesBeetle;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderHerculesBeetle}: ModelHerculesBeetle(1.0F), shadow 0.99*1.1, scale 1.1.
 * Texture: beetletexture.png (gold Beetletexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class HerculesBeetleRenderer extends MobRenderer<HerculesBeetle, ModelHerculesBeetle> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/beetletexture.png");
    /** Gold ClientProxy: {@code new RenderHerculesBeetle(new ModelHerculesBeetle(1.0F), 0.99F, 1.1F)}. */
    private static final float MODEL_SCALE = 1.1F;

    public HerculesBeetleRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelHerculesBeetle(context.bakeLayer(ModelHerculesBeetle.LAYER_LOCATION), 1.0F),
                0.99F * MODEL_SCALE);
    }

    @Override
    protected void scale(HerculesBeetle entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(HerculesBeetle entity) {
        return TEXTURE;
    }
}
