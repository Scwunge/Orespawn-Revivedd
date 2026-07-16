package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSeaViper;
import danger.orespawn.entity.SeaViper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSeaViper}: ModelSeaViper(0.5F), shadow 1.0, scale 1.0.
 * Texture: seavipertexture.png
 */
@OnlyIn(Dist.CLIENT)
public class SeaViperRenderer extends MobRenderer<SeaViper, ModelSeaViper> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/seavipertexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public SeaViperRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 1.0 * 1.0; wingspeed 0.5
        super(
                context,
                new ModelSeaViper(context.bakeLayer(ModelSeaViper.LAYER_LOCATION), 0.5F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(SeaViper entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaViper entity) {
        return TEXTURE;
    }
}
