package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelOstrich;
import danger.orespawn.entity.Ostrich;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderOstrich}: ModelOstrich(0.65F), shadow 0.55, scale 1.0 (baby half).
 * Textures: ostrichtexture.png (+2/3 when hat-activated).
 */
@OnlyIn(Dist.CLIENT)
public class OstrichRenderer extends MobRenderer<Ostrich, ModelOstrich> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ostrichtexture.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ostrichtexture2.png");
    private static final ResourceLocation TEXTURE3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ostrichtexture3.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public OstrichRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelOstrich(context.bakeLayer(ModelOstrich.LAYER_LOCATION), 0.65F), 0.55F * MODEL_SCALE);
    }

    @Override
    protected void scale(Ostrich entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: baby → scale/2
        if (entity.isBaby()) {
            float s = MODEL_SCALE / 2.0F;
            poseStack.scale(s, s, s);
        } else {
            poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Ostrich entity) {
        // gold: activated hat color 2 → texture2, 3 → texture3
        if (entity.get_is_activated() != 0) {
            if (entity.getHatColor() == 2) {
                return TEXTURE2;
            }
            if (entity.getHatColor() == 3) {
                return TEXTURE3;
            }
        }
        return TEXTURE;
    }
}
