package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelBandP;
import danger.orespawn.entity.BandP;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderBandP}: ModelBandP(0.4F), shadow 1.0, scale 1.0.
 * Texture: bandptexture.png.
 */
@OnlyIn(Dist.CLIENT)
public class BandPRenderer extends MobRenderer<BandP, ModelBandP> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bandptexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public BandPRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBandP(context.bakeLayer(ModelBandP.LAYER_LOCATION), 0.4F), 1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(BandP entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(BandP entity) {
        return TEXTURE;
    }
}
