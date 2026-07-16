package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRobot1;
import danger.orespawn.entity.Robot1;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRobot1}: ModelRobot1(2.0F), shadow 0.3*1.0, scale 1.0.
 * Texture: {@code robot1.png}.
 */
@OnlyIn(Dist.CLIENT)
public class Robot1Renderer extends MobRenderer<Robot1, ModelRobot1> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robot1.png");
    private static final float MODEL_SCALE = 1.0F;

    public Robot1Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelRobot1(context.bakeLayer(ModelRobot1.LAYER_LOCATION), 2.0F), 0.3F * MODEL_SCALE);
    }

    @Override
    protected void scale(Robot1 entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Robot1 entity) {
        return TEXTURE;
    }
}
