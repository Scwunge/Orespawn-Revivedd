package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelCreepingHorror;
import danger.orespawn.entity.CreepingHorror;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderCreepingHorror}: ModelCreepingHorror, shadow 0.45*0.75, scale 0.75.
 * Texture: creepinghorror.png (gold CreepingHorror.png).
 */
@OnlyIn(Dist.CLIENT)
public class CreepingHorrorRenderer extends MobRenderer<CreepingHorror, ModelCreepingHorror> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/creepinghorror.png");
    /** Gold ClientProxy: {@code new RenderCreepingHorror(new ModelCreepingHorror(), 0.45F, 0.75F)}. */
    private static final float MODEL_SCALE = 0.75F;

    public CreepingHorrorRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCreepingHorror(context.bakeLayer(ModModelLayers.CREEPING_HORROR)), 0.45F * MODEL_SCALE);
    }

    @Override
    protected void scale(CreepingHorror entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(CreepingHorror entity) {
        return TEXTURE;
    }
}
