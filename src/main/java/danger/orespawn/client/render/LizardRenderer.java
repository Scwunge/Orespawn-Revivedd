package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelLizard;
import danger.orespawn.entity.Lizard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderLizard}: ModelLizard(0.65F), shadow 0.75*1.0, scale 1.0 (baby half).
 * Texture: lizard.png; hat variants lizard2/3 when cannon-fodder activated.
 */
@OnlyIn(Dist.CLIENT)
public class LizardRenderer extends MobRenderer<Lizard, ModelLizard> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/lizard.png");
    private static final ResourceLocation TEXTURE2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/lizard2.png");
    private static final ResourceLocation TEXTURE3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/lizard3.png");
    /** Gold ClientProxy: {@code new RenderLizard(new ModelLizard(0.65F), 0.75F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public LizardRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelLizard(context.bakeLayer(ModelLizard.LAYER_LOCATION), 0.65F),
                0.75F * MODEL_SCALE);
    }

    @Override
    protected void scale(Lizard entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? MODEL_SCALE / 2.0F : MODEL_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(Lizard entity) {
        // gold RenderLizard: hat-color textures when cannon-fodder activated
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
