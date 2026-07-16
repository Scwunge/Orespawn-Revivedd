package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelTshirt;
import danger.orespawn.entity.Tshirt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTshirt}: ModelTshirt(0.22F), shadow 1.0*0.33, scale 0.33.
 * Texture: tshirttexture.png
 */
@OnlyIn(Dist.CLIENT)
public class TshirtRenderer extends MobRenderer<Tshirt, ModelTshirt> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/tshirttexture.png");
    /** Gold ClientProxy: {@code new RenderTshirt(new ModelTshirt(0.22F), 1.0F, 0.33F)}. */
    private static final float MODEL_SCALE = 0.33F;

    public TshirtRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTshirt(context.bakeLayer(ModelTshirt.LAYER_LOCATION), 0.22F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Tshirt entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Tshirt entity) {
        return TEXTURE;
    }
}
