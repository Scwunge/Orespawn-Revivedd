package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelRat;
import danger.orespawn.entity.Rat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRat}: scale 0.75, shadow 0.1*0.75, texture Rattexture.png.
 */
@OnlyIn(Dist.CLIENT)
public class RatRenderer extends MobRenderer<Rat, ModelRat> {
    /** Gold Rattexture.png (also present as textures/entity/Rattexture.png). */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rattexture.png");
    /** Gold ClientProxy: {@code new RenderRat(new ModelRat(1.0F), 0.1F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public RatRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelRat(context.bakeLayer(ModModelLayers.RAT), 1.0F), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Rat entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Rat entity) {
        return TEXTURE;
    }
}
