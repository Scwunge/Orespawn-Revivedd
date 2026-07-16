package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelMolenoid;
import danger.orespawn.entity.Molenoid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderMolenoid}: ModelMolenoid(0.5F), shadow 1.0*1.0, scale 1.0.
 * Texture: molenoidtexture.png
 */
@OnlyIn(Dist.CLIENT)
public class MolenoidRenderer extends MobRenderer<Molenoid, ModelMolenoid> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/molenoidtexture.png");
    /** Gold ClientProxy: {@code new RenderMolenoid(new ModelMolenoid(0.5F), 1.0F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public MolenoidRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelMolenoid(context.bakeLayer(ModelMolenoid.LAYER_LOCATION), 0.5F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Molenoid entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Molenoid entity) {
        return TEXTURE;
    }
}
