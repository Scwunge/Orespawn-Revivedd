package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelIsland;
import danger.orespawn.entity.IslandToo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderIslandToo}: ModelIsland(1.0F), shadow par2*par3 = 0.25*1.0, scale 1.0.
 * ClientProxy: {@code new RenderIslandToo(new ModelIsland(1.0F), 0.25F, 1.0F)}.
 * Texture: textures/entity/islandtoo.png
 */
@OnlyIn(Dist.CLIENT)
public class IslandTooRenderer extends MobRenderer<IslandToo, ModelIsland<IslandToo>> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/islandtoo.png");
    /** Gold ClientProxy par3 scale. */
    private static final float MODEL_SCALE = 1.0F;

    public IslandTooRenderer(EntityRendererProvider.Context context) {
        // gold shadow: par2 * par3 = 0.25 * 1.0 = 0.25
        super(context, new ModelIsland<>(context.bakeLayer(ModelIsland.LAYER_LOCATION), 1.0F), 0.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(IslandToo entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(IslandToo entity) {
        return TEXTURE;
    }
}
