package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCloudShark;
import danger.orespawn.entity.CloudShark;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCloudShark}: ModelCloudShark(1.0F), shadow 0.5, scale 1.0.
 * Texture: cloudshark.png
 */
@OnlyIn(Dist.CLIENT)
public class CloudSharkRenderer extends MobRenderer<CloudShark, ModelCloudShark> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/cloudshark.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public CloudSharkRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.5 * 1.0; wingspeed 1.0
        super(context, new ModelCloudShark(context.bakeLayer(ModModelLayers.CLOUD_SHARK), 1.0F), 0.5F * MODEL_SCALE);
    }

    @Override
    protected void scale(CloudShark entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(CloudShark entity) {
        return TEXTURE;
    }
}
