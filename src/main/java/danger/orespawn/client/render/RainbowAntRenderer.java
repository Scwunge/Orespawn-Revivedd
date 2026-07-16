package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAnt;
import danger.orespawn.entity.RainbowAnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAnt} for rainbow ant: {@code new RenderAnt(new ModelAnt(), 0.1F, 0.25F)}.
 */
@OnlyIn(Dist.CLIENT)
public class RainbowAntRenderer extends MobRenderer<RainbowAnt, ModelAnt<RainbowAnt>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rainbow_ant.png");
    private static final float MODEL_SCALE = 0.25F;

    public RainbowAntRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAnt<>(context.bakeLayer(ModModelLayers.ANT)), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(RainbowAnt entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(RainbowAnt entity) {
        return TEXTURE;
    }
}
