package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelTrooperBug;
import danger.orespawn.entity.TrooperBug;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTrooperBug}: ModelTrooperBug(0.22F), shadow 0.95*1.1, scale 1.1.
 * Texture: textures/entity/trooperbug.png (gold TrooperBug.png).
 */
@OnlyIn(Dist.CLIENT)
public class TrooperBugRenderer extends MobRenderer<TrooperBug, ModelTrooperBug> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/trooperbug.png");
    /** Gold ClientProxy: {@code new RenderTrooperBug(new ModelTrooperBug(0.22F), 0.95F, 1.1F)}. */
    private static final float MODEL_SCALE = 1.1F;

    public TrooperBugRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTrooperBug(context.bakeLayer(ModelTrooperBug.LAYER_LOCATION), 0.22F),
                0.95F * MODEL_SCALE);
    }

    @Override
    protected void scale(TrooperBug entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(TrooperBug entity) {
        return TEXTURE;
    }
}
