package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelEmperorScorpion;
import danger.orespawn.entity.EmperorScorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEmperorScorpion}: ModelEmperorScorpion(0.22F), shadow 0.95*1.5, scale 1.5.
 * Texture: {@code textures/entity/emperorscorpion.png}.
 */
@OnlyIn(Dist.CLIENT)
public class EmperorScorpionRenderer extends MobRenderer<EmperorScorpion, ModelEmperorScorpion> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/emperorscorpion.png");
    /** Gold ClientProxy: {@code new RenderEmperorScorpion(new ModelEmperorScorpion(0.22F), 0.95F, 1.5F)}. */
    private static final float MODEL_SCALE = 1.5F;

    public EmperorScorpionRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelEmperorScorpion(context.bakeLayer(ModelEmperorScorpion.LAYER_LOCATION), 0.22F),
                0.95F * MODEL_SCALE);
    }

    @Override
    protected void scale(EmperorScorpion entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(EmperorScorpion entity) {
        return TEXTURE;
    }
}
