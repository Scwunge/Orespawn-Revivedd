package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRobot3;
import danger.orespawn.entity.Robot3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRobot3}: ModelRobot3(1.0F), shadow 1.0*0.5, scale 0.5.
 * Texture: {@code robot3.png}.
 */
@OnlyIn(Dist.CLIENT)
public class Robot3Renderer extends MobRenderer<Robot3, ModelRobot3> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robot3.png");
    private static final float MODEL_SCALE = 0.5F;

    public Robot3Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelRobot3(context.bakeLayer(ModelRobot3.LAYER_LOCATION), 1.0F), 1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Robot3 entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Robot3 entity) {
        return TEXTURE;
    }
}
