package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelThePrinceAdult;
import danger.orespawn.entity.ThePrinceAdult;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderThePrinceAdult}: ModelThePrinceAdult(0.65F), shadow 1.2*1.0, scale 1.0.
 * Texture: {@code textures/entity/thekingtexture.png} (gold TheKingtexture.png).
 * ClientProxy: {@code new RenderThePrinceAdult(new ModelThePrinceAdult(0.65F), 1.2F, 1.0F)}.
 */
@OnlyIn(Dist.CLIENT)
public class ThePrinceAdultRenderer extends MobRenderer<ThePrinceAdult, ModelThePrinceAdult> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/thekingtexture.png");

    /** Gold RenderThePrinceAdult scale (par3). */
    private static final float MODEL_SCALE = 1.0F;

    public ThePrinceAdultRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelThePrinceAdult(context.bakeLayer(ModelThePrinceAdult.LAYER_LOCATION), 0.65F),
                1.2F * MODEL_SCALE);
    }

    @Override
    protected void scale(ThePrinceAdult entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(ThePrinceAdult entity) {
        return TEXTURE;
    }
}
