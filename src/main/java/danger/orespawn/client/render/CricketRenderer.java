package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCricket;
import danger.orespawn.entity.Cricket;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCricket}: ModelCricket(2.5F), shadow 0.15, scale 0.5.
 * Texture: Crickettexture.png
 */
@OnlyIn(Dist.CLIENT)
public class CricketRenderer extends MobRenderer<Cricket, ModelCricket> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/crickettexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.5F;

    public CricketRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCricket(context.bakeLayer(ModModelLayers.CRICKET), 2.5F), 0.15F * MODEL_SCALE);
    }

    @Override
    protected void scale(Cricket entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Cricket entity) {
        return TEXTURE;
    }
}
