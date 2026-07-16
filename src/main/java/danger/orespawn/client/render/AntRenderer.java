package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAnt;
import danger.orespawn.entity.Ant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAnt} for brown ant: {@code new RenderAnt(new ModelAnt(), 0.1F, 0.25F)}.
 * Only {@code glScalef(scale)} — no translate (Y-flip in LivingEntityRenderer would bury a +Y push).
 */
@OnlyIn(Dist.CLIENT)
public class AntRenderer extends MobRenderer<Ant, ModelAnt<Ant>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/ant.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.25F;

    public AntRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.1 * 0.25 = 0.025
        super(context, new ModelAnt<>(context.bakeLayer(ModModelLayers.ANT)), 0.1F * MODEL_SCALE);
    }

    @Override
    protected void scale(Ant entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Ant entity) {
        return TEXTURE;
    }
}
