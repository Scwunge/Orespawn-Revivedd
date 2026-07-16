package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelBee;
import danger.orespawn.entity.Bee;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderBee}: ModelBee(2.0F), shadow 0.9, scale 1.1.
 * Texture: beetexture.png
 */
@OnlyIn(Dist.CLIENT)
public class BeeRenderer extends MobRenderer<Bee, ModelBee> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/beetexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.1F;

    public BeeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBee(context.bakeLayer(ModelBee.LAYER_LOCATION), 2.0F), 0.9F * MODEL_SCALE);
    }

    @Override
    protected void scale(Bee entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Bee entity) {
        return TEXTURE;
    }
}
