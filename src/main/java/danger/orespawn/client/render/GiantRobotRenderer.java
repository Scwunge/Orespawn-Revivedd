package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelGiantRobot;
import danger.orespawn.entity.GiantRobot;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGiantRobot}: ModelGiantRobot(0.25F), shadow 0.99*1.0, scale 1.0.
 * Texture: {@code textures/entity/giantrobottexture.png} (gold GiantRobottexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class GiantRobotRenderer extends MobRenderer<GiantRobot, ModelGiantRobot> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/giantrobottexture.png");
    private static final float MODEL_SCALE = 1.0F;

    public GiantRobotRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelGiantRobot(context.bakeLayer(ModelGiantRobot.LAYER_LOCATION), 0.25F),
                0.99F * MODEL_SCALE);
    }

    @Override
    protected void scale(GiantRobot entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantRobot entity) {
        return TEXTURE;
    }
}
