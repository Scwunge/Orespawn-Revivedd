package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelHammerhead;
import danger.orespawn.entity.Hammerhead;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderHammerhead}: ModelHammerhead(0.33F), shadow 1.0, scale 2.5.
 * Texture: hammerheadtexture.png
 */
@OnlyIn(Dist.CLIENT)
public class HammerheadRenderer extends MobRenderer<Hammerhead, ModelHammerhead> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/hammerheadtexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 2.5F;

    public HammerheadRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 1.0 * 2.5; wingspeed 0.33
        super(
                context,
                new ModelHammerhead(context.bakeLayer(ModelHammerhead.LAYER_LOCATION), 0.33F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Hammerhead entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Hammerhead entity) {
        return TEXTURE;
    }
}
