package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelGodzilla;
import danger.orespawn.entity.Godzilla;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGodzilla}: ModelGodzilla(0.2F), shadow 1.0*2.0, scale 2.0.
 * PlayNicely → scale / 4. Texture: {@code textures/entity/godzillatexture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class GodzillaRenderer extends MobRenderer<Godzilla, ModelGodzilla> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/godzillatexture.png");
    /** Gold ClientProxy: {@code new RenderGodzilla(new ModelGodzilla(0.2F), 1.0F, 2.0F)}. */
    private static final float MODEL_SCALE = 2.0F;

    public GodzillaRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelGodzilla(context.bakeLayer(ModelGodzilla.LAYER_LOCATION), 0.2F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Godzilla entity, PoseStack poseStack, float partialTick) {
        // gold preRenderScale: PlayNicely != 0 → scale/4
        if (entity.getPlayNicely() != 0) {
            float s = MODEL_SCALE / 4.0F;
            poseStack.scale(s, s, s);
        } else {
            poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Godzilla entity) {
        return TEXTURE;
    }
}
