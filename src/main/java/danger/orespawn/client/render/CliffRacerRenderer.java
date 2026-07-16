package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelCliffRacer;
import danger.orespawn.entity.CliffRacer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCliffRacer}: ModelCliffRacer(1.0F), shadow 0.3, scale 1.0.
 * Texture: cliffracertexture.png (gold Cliffracertexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class CliffRacerRenderer extends MobRenderer<CliffRacer, ModelCliffRacer> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cliffracertexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public CliffRacerRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelCliffRacer(context.bakeLayer(ModelCliffRacer.LAYER_LOCATION), 1.0F),
                0.3F * MODEL_SCALE);
    }

    @Override
    protected void scale(CliffRacer entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(CliffRacer entity) {
        return TEXTURE;
    }
}
