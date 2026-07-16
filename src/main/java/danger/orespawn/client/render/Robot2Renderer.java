package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRobot2;
import danger.orespawn.entity.Robot2;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRobot2}: ModelRobot2(1.0F), shadow 1.0*1.0, scale 1.0.
 * Texture: {@code robot2.png}.
 */
@OnlyIn(Dist.CLIENT)
public class Robot2Renderer extends MobRenderer<Robot2, ModelRobot2> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robot2.png");
    private static final float MODEL_SCALE = 1.0F;

    public Robot2Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelRobot2(context.bakeLayer(ModelRobot2.LAYER_LOCATION), 1.0F), 1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Robot2 entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Robot2 entity) {
        return TEXTURE;
    }
}
