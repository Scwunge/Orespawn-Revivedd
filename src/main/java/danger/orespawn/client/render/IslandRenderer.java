package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelIsland;
import danger.orespawn.entity.Island;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderIsland}: ModelIsland(1.0F), shadow par2*par3 = 0.25*1.0, scale 1.0.
 * ClientProxy: {@code new RenderIsland(new ModelIsland(1.0F), 0.25F, 1.0F)}.
 * Texture: textures/entity/island.png
 */
@OnlyIn(Dist.CLIENT)
public class IslandRenderer extends MobRenderer<Island, ModelIsland<Island>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/island.png");
    /** Gold ClientProxy par3 scale. */
    private static final float MODEL_SCALE = 1.0F;

    public IslandRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.25 * 1.0 = 0.25
        super(context, new ModelIsland<>(context.bakeLayer(ModelIsland.LAYER_LOCATION), 1.0F), 0.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(Island entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Island entity) {
        return TEXTURE;
    }
}
