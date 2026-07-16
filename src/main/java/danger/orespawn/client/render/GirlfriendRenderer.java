package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.entity.Girlfriend;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderGirlfriend} extends RenderBiped(ModelBiped, 0.5F).
 * Valentine angry: preRenderCallback scale 5.0 when valentines_day != 0 && feelingBetter == 0.
 * Uses vanilla PLAYER humanoid layer (no custom model layer registration).
 */
@OnlyIn(Dist.CLIENT)
public class GirlfriendRenderer extends HumanoidMobRenderer<Girlfriend, HumanoidModel<Girlfriend>> {
    private static final ResourceLocation FALLBACK =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/girlfriend0.png");

    public GirlfriendRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Girlfriend entity) {
        ResourceLocation tex = entity.getTexture();
        return tex != null ? tex : FALLBACK;
    }

    /** Gold {@code preRenderCallback} / func_77041_b — valentine giant scale 5.0F. */
    @Override
    protected void scale(Girlfriend entity, PoseStack poseStack, float partialTick) {
        if (Girlfriend.VALENTINES_DAY != 0 && entity.getFeelingBetter() == 0) {
            poseStack.scale(5.0F, 5.0F, 5.0F);
        }
    }
}
