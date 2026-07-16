package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelWhale;
import danger.orespawn.entity.Whale;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderWhale}: ModelWhale(), shadow 0.1, scale 1.0.
 * Texture: whaletexture.png
 */
@OnlyIn(Dist.CLIENT)
public class WhaleRenderer extends MobRenderer<Whale, ModelWhale> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/whaletexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public WhaleRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.1 * 1.0
        super(context, new ModelWhale(context.bakeLayer(ModelWhale.LAYER_LOCATION)), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Whale entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Whale entity) {
        return TEXTURE;
    }
}
