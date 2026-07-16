package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRubberDucky;
import danger.orespawn.entity.RubberDucky;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRubberDucky}: ModelRubberDucky(1.0F), shadow 0.15, scale 0.75 (baby half).
 * Texture: rubberduckytexture.png (evilrubberduckytexture deferred for killcount≥5).
 */
@OnlyIn(Dist.CLIENT)
public class RubberDuckyRenderer extends MobRenderer<RubberDucky, ModelRubberDucky> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rubberduckytexture.png");
    /** Gold ClientProxy par3 scale */
    private static final float ADULT_SCALE = 0.75F;

    public RubberDuckyRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelRubberDucky(context.bakeLayer(ModelRubberDucky.LAYER_LOCATION), 1.0F),
                0.15F * ADULT_SCALE);
    }

    @Override
    protected void scale(RubberDucky entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? ADULT_SCALE / 2.0F : ADULT_SCALE;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(RubberDucky entity) {
        // gold: killcount ≥ 5 → EvilRubberDuckytexture (deferred optional)
        return TEXTURE;
    }
}
