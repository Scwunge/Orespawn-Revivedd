package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelBasilisk;
import danger.orespawn.entity.Basilisk;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderBasilisk}: ModelBasilisk(0.3F), shadow 0.5*1.25, scale 1.25.
 * Texture: basilisk.png.
 */
@OnlyIn(Dist.CLIENT)
public class BasiliskRenderer extends MobRenderer<Basilisk, ModelBasilisk> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/basilisk.png");
    /** Gold ClientProxy: {@code new RenderBasilisk(new ModelBasilisk(0.3F), 0.5F, 1.25F)}. */
    private static final float MODEL_SCALE = 1.25F;

    public BasiliskRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelBasilisk(context.bakeLayer(ModelBasilisk.LAYER_LOCATION), 0.3F),
                0.5F * MODEL_SCALE);
    }

    @Override
    protected void scale(Basilisk entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Basilisk entity) {
        return TEXTURE;
    }
}
