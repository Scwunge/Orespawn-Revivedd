package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelEnderReaper;
import danger.orespawn.entity.EnderReaper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderEnderReaper}: ModelEnderReaper(0.23), shadow 0.2*1.0, scale 1.0.
 * Texture: enderreapertexture.png (gold EnderReapertexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class EnderReaperRenderer extends MobRenderer<EnderReaper, ModelEnderReaper> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/enderreapertexture.png");
    /** Gold ClientProxy: {@code new RenderEnderReaper(new ModelEnderReaper(0.23F), 0.2F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public EnderReaperRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelEnderReaper(context.bakeLayer(ModelEnderReaper.LAYER_LOCATION), 0.23F), 0.2F * MODEL_SCALE);
    }

    @Override
    protected void scale(EnderReaper entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderReaper entity) {
        return TEXTURE;
    }
}
