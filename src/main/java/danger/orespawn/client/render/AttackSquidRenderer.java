package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelAttackSquid;
import danger.orespawn.entity.AttackSquid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderAttackSquid}: ModelAttackSquid(1.0F), shadow 0.25, scale 0.9.
 * Texture: attacksquid.png (gold AttackSquid.png).
 */
@OnlyIn(Dist.CLIENT)
public class AttackSquidRenderer extends MobRenderer<AttackSquid, ModelAttackSquid> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/attacksquid.png");
    /** Gold ClientProxy par3 scale */
    private static final float MODEL_SCALE = 0.9F;

    public AttackSquidRenderer(EntityRendererProvider.Context context) {
        // gold: shadow par2 * par3 = 0.25 * 0.9; wingspeed 1.0
        super(
                context,
                new ModelAttackSquid(context.bakeLayer(ModelAttackSquid.LAYER_LOCATION), 1.0F),
                0.25F * MODEL_SCALE);
    }

    @Override
    protected void scale(AttackSquid entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(AttackSquid entity) {
        return TEXTURE;
    }
}
