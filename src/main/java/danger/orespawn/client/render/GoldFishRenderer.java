package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelGoldFish;
import danger.orespawn.entity.GoldFish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGoldFish}: ModelGoldFish(0.7F), shadow 0.2, scale 1.0.
 * Texture: goldfish.png
 */
@OnlyIn(Dist.CLIENT)
public class GoldFishRenderer extends MobRenderer<GoldFish, ModelGoldFish> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/goldfish.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public GoldFishRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.2 * 1.0; wingspeed 0.7
        super(context, new ModelGoldFish(context.bakeLayer(ModModelLayers.GOLD_FISH), 0.7F), 0.2F * MODEL_SCALE);
    }

    @Override
    protected void scale(GoldFish entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldFish entity) {
        return TEXTURE;
    }
}
