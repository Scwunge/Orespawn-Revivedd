package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRobot5;
import danger.orespawn.entity.Robot5;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRobot5}: ModelRobot5(1.0F), shadow 0.5*1.0, scale 1.0.
 * Texture: {@code textures/entity/robot5texture.png}.
 */
@OnlyIn(Dist.CLIENT)
public class Robot5Renderer extends MobRenderer<Robot5, ModelRobot5> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robot5texture.png");
    private static final float MODEL_SCALE = 1.0F;

    public Robot5Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelRobot5(context.bakeLayer(ModelRobot5.LAYER_LOCATION), 1.0F), 0.5F * MODEL_SCALE);
    }

    @Override
    protected void scale(Robot5 entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Robot5 entity) {
        return TEXTURE;
    }
}
