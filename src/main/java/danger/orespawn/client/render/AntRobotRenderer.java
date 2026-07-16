package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelAntRobot;
import danger.orespawn.entity.AntRobot;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAntRobot}: ModelAntRobot(1.0F), shadow 0.99*1.0, scale 1.0.
 * Texture: {@code textures/entity/antrobottexture.png} (gold AntRobottexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class AntRobotRenderer extends MobRenderer<AntRobot, ModelAntRobot> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/antrobottexture.png");
    private static final float MODEL_SCALE = 1.0F;

    public AntRobotRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAntRobot(context.bakeLayer(ModelAntRobot.LAYER_LOCATION), 1.0F), 0.99F * MODEL_SCALE);
    }

    @Override
    protected void scale(AntRobot entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(AntRobot entity) {
        return TEXTURE;
    }
}
