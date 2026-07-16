package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSkate;
import danger.orespawn.entity.Skate;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSkate}: ModelSkate(1.0F), shadow 0.1, scale 0.75.
 * Texture: skatetexture.png (gold Skatetexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class SkateRenderer extends MobRenderer<Skate, ModelSkate> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/skatetexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.75F;

    public SkateRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.1 * 0.75; wingspeed 1.0
        super(context, new ModelSkate(context.bakeLayer(ModelSkate.LAYER_LOCATION), 1.0F), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Skate entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Skate entity) {
        return TEXTURE;
    }
}
