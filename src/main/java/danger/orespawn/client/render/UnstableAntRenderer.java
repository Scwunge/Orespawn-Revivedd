package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAnt;
import danger.orespawn.entity.UnstableAnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAnt} for unstable ant: {@code new RenderAnt(new ModelAnt(), 0.1F, 0.25F)}.
 */
@OnlyIn(Dist.CLIENT)
public class UnstableAntRenderer extends MobRenderer<UnstableAnt, ModelAnt<UnstableAnt>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/unstableant.png");
    private static final float MODEL_SCALE = 0.25F;

    public UnstableAntRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAnt<>(context.bakeLayer(ModModelLayers.ANT)), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(UnstableAnt entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(UnstableAnt entity) {
        return TEXTURE;
    }
}
