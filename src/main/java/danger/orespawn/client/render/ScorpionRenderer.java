package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelScorpion;
import danger.orespawn.entity.Scorpion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderScorpion}: ModelScorpion(0.62F), shadow 0.35*0.75, scale 0.75.
 * Texture: scorpion.png (NOT emperorscorpion — different mob).
 */
@OnlyIn(Dist.CLIENT)
public class ScorpionRenderer extends MobRenderer<Scorpion, ModelScorpion> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/scorpion.png");
    /** Gold ClientProxy: {@code new RenderScorpion(new ModelScorpion(0.62F), 0.35F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public ScorpionRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelScorpion(context.bakeLayer(ModelScorpion.LAYER_LOCATION), 0.62F), 0.35F * MODEL_SCALE);
    }

    @Override
    protected void scale(Scorpion entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Scorpion entity) {
        return TEXTURE;
    }
}
