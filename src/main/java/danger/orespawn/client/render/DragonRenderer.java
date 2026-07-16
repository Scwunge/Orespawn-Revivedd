package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelDragon;
import danger.orespawn.entity.Dragon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderDragon}: ModelDragon(0.65F), shadow 1.25*1.0, scale 1.0.
 * Texture: {@code textures/entity/dragon.png} / {@code whitedragon.png} by type.
 * ClientProxy: {@code new RenderDragon(new ModelDragon(0.65F), 1.25F, 1.0F)}.
 */
@OnlyIn(Dist.CLIENT)
public class DragonRenderer extends MobRenderer<Dragon, ModelDragon> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/dragon.png");
    private static final ResourceLocation TEXTURE_WHITE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/whitedragon.png");

    /** Gold RenderDragon scale (par3). */
    private static final float MODEL_SCALE = 1.0F;

    public DragonRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelDragon(context.bakeLayer(ModelDragon.LAYER_LOCATION), 0.65F),
                1.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(Dragon entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(Dragon entity) {
        // gold dragontype != 0 → whitedragon.png
        return entity.getDragonType() != 0 ? TEXTURE_WHITE : TEXTURE;
    }
}
