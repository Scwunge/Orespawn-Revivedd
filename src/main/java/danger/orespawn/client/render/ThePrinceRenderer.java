package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelThePrince;
import danger.orespawn.entity.ThePrince;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderThePrince}: ModelThePrince(0.65F), shadow 0.75*0.75, scale 0.75.
 * Texture: theprincetexture.png.
 */
@OnlyIn(Dist.CLIENT)
public class ThePrinceRenderer extends MobRenderer<ThePrince, ModelThePrince> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/theprincetexture.png");
    /** Gold ClientProxy: {@code new RenderThePrince(new ModelThePrince(0.65F), 0.75F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public ThePrinceRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelThePrince(context.bakeLayer(ModelThePrince.LAYER_LOCATION), 0.65F),
                0.75F * MODEL_SCALE);
    }

    @Override
    protected void scale(ThePrince entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(ThePrince entity) {
        return TEXTURE;
    }
}
