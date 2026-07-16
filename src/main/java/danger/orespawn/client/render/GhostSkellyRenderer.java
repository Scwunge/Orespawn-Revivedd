package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelGhostSkelly;
import danger.orespawn.entity.GhostSkelly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGhostSkelly}: ModelGhostSkelly, shadow 0, scale 1.05
 * (ClientProxy {@code new RenderGhostSkelly(new ModelGhostSkelly(), 0.0F, 1.05F)}).
 * Texture: textures/entity/ghostskellytexture.png
 */
@OnlyIn(Dist.CLIENT)
public class GhostSkellyRenderer extends MobRenderer<GhostSkelly, ModelGhostSkelly> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ghostskellytexture.png");

    /** Gold RenderGhostSkelly scale (par3). */
    private static final float MODEL_SCALE = 1.05F;

    public GhostSkellyRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.0 * 1.05 = 0
        super(context, new ModelGhostSkelly(context.bakeLayer(ModModelLayers.GHOST_SKELLY)), 0.0F);
    }

    @Override
    protected void scale(GhostSkelly entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(GhostSkelly entity) {
        return TEXTURE;
    }
}
