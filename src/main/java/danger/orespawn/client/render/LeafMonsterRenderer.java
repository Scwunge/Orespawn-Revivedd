package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelLeafMonster;
import danger.orespawn.entity.LeafMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderLeafMonster}: ModelLeafMonster, shadow 0.65*1.0, scale 1.0.
 * Texture: leafmonstertexture.png (gold LeafMonstertexture.png).
 */
@OnlyIn(Dist.CLIENT)
public class LeafMonsterRenderer extends MobRenderer<LeafMonster, ModelLeafMonster> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/leafmonstertexture.png");
    /** Gold ClientProxy: {@code new RenderLeafMonster(new ModelLeafMonster(), 0.65F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public LeafMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelLeafMonster(context.bakeLayer(ModelLeafMonster.LAYER_LOCATION)), 0.65F * MODEL_SCALE);
    }

    @Override
    protected void scale(LeafMonster entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(LeafMonster entity) {
        return TEXTURE;
    }
}
