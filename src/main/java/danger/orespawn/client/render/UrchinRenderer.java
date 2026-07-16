package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelUrchin;
import danger.orespawn.entity.Urchin;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderUrchin}: ModelUrchin(1.0F), shadow 0.35, scale 1.25.
 * Texture: urchintexture.png
 */
@OnlyIn(Dist.CLIENT)
public class UrchinRenderer extends MobRenderer<Urchin, ModelUrchin> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/urchintexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.25F;

    public UrchinRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.35 * 1.25; wingspeed 1.0
        super(context, new ModelUrchin(context.bakeLayer(ModelUrchin.LAYER_LOCATION), 1.0F), 0.35F * MODEL_SCALE);
    }

    @Override
    protected void scale(Urchin entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Urchin entity) {
        return TEXTURE;
    }
}
