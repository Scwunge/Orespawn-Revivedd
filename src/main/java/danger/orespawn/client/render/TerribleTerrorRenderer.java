package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelTerribleTerror;
import danger.orespawn.entity.TerribleTerror;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderTerribleTerror}: ModelTerribleTerror, shadow 0.45*0.75, scale 0.75.
 * Texture: terribleterror.png (gold TerribleTerror.png).
 */
@OnlyIn(Dist.CLIENT)
public class TerribleTerrorRenderer extends MobRenderer<TerribleTerror, ModelTerribleTerror> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/terribleterror.png");
    /** Gold ClientProxy: {@code new RenderTerribleTerror(new ModelTerribleTerror(), 0.45F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public TerribleTerrorRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelTerribleTerror(context.bakeLayer(ModModelLayers.TERRIBLE_TERROR), 1.0F),
                0.45F * MODEL_SCALE);
    }

    @Override
    protected void scale(TerribleTerror entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(TerribleTerror entity) {
        return TEXTURE;
    }
}
