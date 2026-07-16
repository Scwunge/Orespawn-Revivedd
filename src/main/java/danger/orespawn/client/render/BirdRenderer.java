package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelBird;
import danger.orespawn.entity.Bird;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BirdRenderer extends MobRenderer<Bird, ModelBird> {
    private static final ResourceLocation[] TEXTURES = {
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird1.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird2.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird3.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird4.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird5.png"),
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/bird6.png"),
    };

    public BirdRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelBird(context.bakeLayer(ModModelLayers.BIRD), 0.6F), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Bird entity) {
        int t = entity.birdType;
        // gold RenderBird: case 1.5 → bird1.5; default bird6 (covers type 0)
        if (t >= 1 && t <= 5) {
            return TEXTURES[t - 1];
        }
        return TEXTURES[5];
    }

    /** Gold {@code applyRotations}: translate Y by cos(age*0.3)*0.1 bob. */
    @Override
    protected void setupRotations(Bird entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        poseStack.translate(0.0F, Mth.cos(bob * 0.3F) * 0.1F, 0.0F);
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
    }
}
