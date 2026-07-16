package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelPeacock;
import danger.orespawn.entity.Peacock;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderPeacock}: ModelPeacock(0.75F), shadow 0.25, scale 1.0 (baby half).
 * Texture: peacocktexture.png (peacock_1/2 variants optional).
 */
@OnlyIn(Dist.CLIENT)
public class PeacockRenderer extends MobRenderer<Peacock, ModelPeacock> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/peacocktexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public PeacockRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelPeacock(context.bakeLayer(ModelPeacock.LAYER_LOCATION), 0.75F), 0.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(Peacock entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: baby → scale/2
        if (entity.isBaby()) {
            float s = MODEL_SCALE / 2.0F;
            poseStack.scale(s, s, s);
        } else {
            poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Peacock entity) {
        return TEXTURE;
    }
}
