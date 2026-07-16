package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelEasterBunny;
import danger.orespawn.entity.EasterBunny;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEasterBunny}: ModelEasterBunny(0.55F), shadow 0.5, scale 1.0 (baby half).
 * Texture: easterbunnytexture.png.
 */
@OnlyIn(Dist.CLIENT)
public class EasterBunnyRenderer extends MobRenderer<EasterBunny, ModelEasterBunny> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/easterbunnytexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public EasterBunnyRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelEasterBunny(context.bakeLayer(ModelEasterBunny.LAYER_LOCATION), 0.55F),
                0.5F * MODEL_SCALE);
    }

    @Override
    protected void scale(EasterBunny entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: baby → scale/2
        if (entity.isBaby()) {
            float s = MODEL_SCALE / 2.0F;
            poseStack.scale(s, s, s);
        } else {
            poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EasterBunny entity) {
        return TEXTURE;
    }
}
