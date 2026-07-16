package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAnt;
import danger.orespawn.entity.Termite;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAnt} for termite: {@code new RenderAnt(new ModelAnt(), 0.15F, 0.35F)}.
 * Scale only — no Y translate (see {@link AntRenderer}).
 */
@OnlyIn(Dist.CLIENT)
public class TermiteRenderer extends MobRenderer<Termite, ModelAnt<Termite>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/termite.png");
    private static final float MODEL_SCALE = 0.35F;

    public TermiteRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAnt<>(context.bakeLayer(ModModelLayers.ANT)), 0.15F * MODEL_SCALE);
    }

    @Override
    protected void scale(Termite entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Termite entity) {
        return TEXTURE;
    }
}
