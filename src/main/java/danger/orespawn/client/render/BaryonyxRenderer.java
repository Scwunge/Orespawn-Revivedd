package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelBaryonyx;
import danger.orespawn.entity.Baryonyx;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code ModelBaryonyx}:
 * adult {@code glTranslate(0,-0.15,0)} + {@code glScalef(1.1)};
 * child {@code glTranslate(0,0.75,0)} + {@code glScalef(0.5)}.
 */
@OnlyIn(Dist.CLIENT)
public class BaryonyxRenderer extends MobRenderer<Baryonyx, ModelBaryonyx> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/baryonyx.png");
    private static final float ADULT_SCALE = 1.1F;
    private static final float CHILD_SCALE = 0.5F;

    public BaryonyxRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBaryonyx(context.bakeLayer(ModModelLayers.BARYONYX), 1.5F), 0.8F);
    }

    @Override
    protected void scale(Baryonyx entity, PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.translate(0.0F, 0.75F, 0.0F);
            poseStack.scale(CHILD_SCALE, CHILD_SCALE, CHILD_SCALE);
        } else {
            poseStack.translate(0.0F, -0.15F, 0.0F);
            poseStack.scale(ADULT_SCALE, ADULT_SCALE, ADULT_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Baryonyx entity) {
        return TEXTURE;
    }
}
