package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRobot4;
import danger.orespawn.entity.Robot4;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRobot4}: ModelRobot4(1.0F), shadow 1.0*1.0, scale 1.0.
 * Texture: {@code robot4.png} (gold {@code Robot4.png}).
 */
@OnlyIn(Dist.CLIENT)
public class Robot4Renderer extends MobRenderer<Robot4, ModelRobot4> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/robot4.png");
    private static final float MODEL_SCALE = 1.0F;

    public Robot4Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelRobot4(context.bakeLayer(ModelRobot4.LAYER_LOCATION), 1.0F), 1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(Robot4 entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Robot4 entity) {
        return TEXTURE;
    }
}
