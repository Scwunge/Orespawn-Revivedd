package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSpitBug;
import danger.orespawn.entity.SpitBug;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSpitBug}: ModelSpitBug(0.55F), shadow 0.55*0.75, scale 0.75.
 * Texture: BlisterBug.png → textures/entity/blisterbug.png.
 */
@OnlyIn(Dist.CLIENT)
public class SpitBugRenderer extends MobRenderer<SpitBug, ModelSpitBug> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/blisterbug.png");
    /** Gold ClientProxy: {@code new RenderSpitBug(new ModelSpitBug(0.55F), 0.55F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public SpitBugRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSpitBug(context.bakeLayer(ModelSpitBug.LAYER_LOCATION), 0.55F), 0.55F * MODEL_SCALE);
    }

    @Override
    protected void scale(SpitBug entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SpitBug entity) {
        return TEXTURE;
    }
}
