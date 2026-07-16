package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelAnt;
import danger.orespawn.entity.RedAnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAnt} for red ant: {@code new RenderAnt(new ModelAnt(), 0.15F, 0.35F)}.
 * Scale only — no Y translate (see {@link AntRenderer}).
 */
@OnlyIn(Dist.CLIENT)
public class RedAntRenderer extends MobRenderer<RedAnt, ModelAnt<RedAnt>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/red_ant.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.35F;

    public RedAntRenderer(EntityRendererProvider.Context context) {
        // gold shadow: 0.15 * 0.35
        super(context, new ModelAnt<>(context.bakeLayer(ModModelLayers.ANT)), 0.15F * MODEL_SCALE);
    }

    @Override
    protected void scale(RedAnt entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(RedAnt entity) {
        return TEXTURE;
    }
}
