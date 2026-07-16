package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSeaMonster;
import danger.orespawn.entity.SeaMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderSeaMonster}: ModelSeaMonster(0.5F), shadow 1.0, scale 1.0.
 * Texture: seamonstertexture.png
 */
@OnlyIn(Dist.CLIENT)
public class SeaMonsterRenderer extends MobRenderer<SeaMonster, ModelSeaMonster> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/seamonstertexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 1.0F;

    public SeaMonsterRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 1.0 * 1.0; wingspeed 0.5
        super(
                context,
                new ModelSeaMonster(context.bakeLayer(ModelSeaMonster.LAYER_LOCATION), 0.5F),
                1.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(SeaMonster entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaMonster entity) {
        return TEXTURE;
    }
}
