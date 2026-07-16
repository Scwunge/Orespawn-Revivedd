package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelGhost;
import danger.orespawn.entity.Ghost;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGhost}: ModelGhost, shadow par2*par3 = 0, scale 0.65
 * (ClientProxy {@code new RenderGhost(new ModelGhost(), 0.0F, 0.65F)}).
 * Texture: textures/entity/ghosttexture.png
 */
@OnlyIn(Dist.CLIENT)
public class GhostRenderer extends MobRenderer<Ghost, ModelGhost> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ghosttexture.png");

    /** Gold RenderGhost scale (par3). */
    private static final float MODEL_SCALE = 0.65F;

    public GhostRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.0 * 0.65 = 0
        super(context, new ModelGhost(context.bakeLayer(ModModelLayers.GHOST)), 0.0F);
    }

    @Override
    protected void scale(Ghost entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Ghost entity) {
        return TEXTURE;
    }
}
