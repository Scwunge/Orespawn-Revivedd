package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelFlounder;
import danger.orespawn.entity.Flounder;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderFlounder}: ModelFlounder, shadow 0.1, scale 1.0 (baby half).
 * Texture: floundertexture.png
 */
@OnlyIn(Dist.CLIENT)
public class FlounderRenderer extends MobRenderer<Flounder, ModelFlounder> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/floundertexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 1.0F;

    public FlounderRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.1 * 1.0
        super(context, new ModelFlounder(context.bakeLayer(ModModelLayers.FLOUNDER)), 0.1F * ADULT_SCALE);
    }

    @Override
    protected void scale(Flounder entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? ADULT_SCALE / 2.0F : ADULT_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(Flounder entity) {
        return TEXTURE;
    }
}
