package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSpiderRobot;
import danger.orespawn.entity.SpiderRobot;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSpiderRobot}: ModelSpiderRobot(1.0F), shadow 0.99*1.0, scale 1.0.
 * Texture: {@code textures/entity/spiderrobottexture.png} (gold SpiderRobottexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class SpiderRobotRenderer extends MobRenderer<SpiderRobot, ModelSpiderRobot> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/spiderrobottexture.png");
    private static final float MODEL_SCALE = 1.0F;

    public SpiderRobotRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSpiderRobot(context.bakeLayer(ModelSpiderRobot.LAYER_LOCATION), 1.0F), 0.99F * MODEL_SCALE);
    }

    @Override
    protected void scale(SpiderRobot entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderRobot entity) {
        return TEXTURE;
    }
}
