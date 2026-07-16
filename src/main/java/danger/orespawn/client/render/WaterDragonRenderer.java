package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelWaterDragon;
import danger.orespawn.entity.WaterDragon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderWaterDragon}: ModelWaterDragon(0.5F), shadow 0.85*1.1, scale 1.1.
 * Baby gold scale/2. Texture: waterdragon.png.
 */
@OnlyIn(Dist.CLIENT)
public class WaterDragonRenderer extends MobRenderer<WaterDragon, ModelWaterDragon> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/waterdragon.png");
    /** Gold ClientProxy: {@code new RenderWaterDragon(new ModelWaterDragon(0.5F), 0.85F, 1.1F)}. */
    private static final float MODEL_SCALE = 1.1F;

    public WaterDragonRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelWaterDragon(context.bakeLayer(ModelWaterDragon.LAYER_LOCATION), 0.5F),
                0.85F * MODEL_SCALE);
    }

    @Override
    protected void scale(WaterDragon entity, PoseStack poseStack, float partialTick) {
        // gold: baby half scale — age not ported; always adult
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(WaterDragon entity) {
        return TEXTURE;
    }
}
