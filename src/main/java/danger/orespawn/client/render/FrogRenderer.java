package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelFrog;
import danger.orespawn.entity.Frog;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderFrog}: ModelFrog(1.0F), shadow 0.35, scale 1.0.
 * Texture: frogtexture.png (prince/princess variants deferred).
 */
@OnlyIn(Dist.CLIENT)
public class FrogRenderer extends MobRenderer<Frog, ModelFrog> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/frogtexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public FrogRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelFrog(context.bakeLayer(ModelFrog.LAYER_LOCATION), 1.0F), 0.35F * MODEL_SCALE);
    }

    @Override
    protected void scale(Frog entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Frog entity) {
        return TEXTURE;
    }
}
