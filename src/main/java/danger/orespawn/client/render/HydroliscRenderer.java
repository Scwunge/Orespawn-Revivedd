package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelHydrolisc;
import danger.orespawn.entity.Hydrolisc;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderHydrolisc}: ModelHydrolisc(0.65F), shadow 0.65*0.65, scale 0.65.
 * Baby gold scale/2. Texture: hydrolisc.png.
 */
@OnlyIn(Dist.CLIENT)
public class HydroliscRenderer extends MobRenderer<Hydrolisc, ModelHydrolisc> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/hydrolisc.png");
    /** Gold ClientProxy: {@code new RenderHydrolisc(new ModelHydrolisc(0.65F), 0.65F, 0.65F)}. */
    private static final float MODEL_SCALE = 0.65F;

    public HydroliscRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelHydrolisc(context.bakeLayer(ModelHydrolisc.LAYER_LOCATION), 0.65F),
                0.65F * MODEL_SCALE);
    }

    @Override
    protected void scale(Hydrolisc entity, PoseStack poseStack, float partialTick) {
        // gold: baby half scale — age not ported; always adult
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Hydrolisc entity) {
        return TEXTURE;
    }
}
