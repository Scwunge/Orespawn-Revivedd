package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelGazelle;
import danger.orespawn.entity.Gazelle;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGazelle}: ModelGazelle(0.65F), shadow 0.45, scale 1.0 (baby half).
 * Texture: gazelletexture.png.
 */
@OnlyIn(Dist.CLIENT)
public class GazelleRenderer extends MobRenderer<Gazelle, ModelGazelle> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/gazelletexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public GazelleRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelGazelle(context.bakeLayer(ModelGazelle.LAYER_LOCATION), 0.65F), 0.45F * MODEL_SCALE);
    }

    @Override
    protected void scale(Gazelle entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: baby → scale/2
        if (entity.isBaby()) {
            float s = MODEL_SCALE / 2.0F;
            poseStack.scale(s, s, s);
        } else {
            poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Gazelle entity) {
        return TEXTURE;
    }
}
