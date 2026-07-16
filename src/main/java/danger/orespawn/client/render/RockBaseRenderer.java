package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelRockBase;
import danger.orespawn.entity.RockBase;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderRockBase}: ModelRockBase(1.0F), shadow 0.0*1.0, scale 1.0.
 * Texture selected by rock_type 1–12.
 */
@OnlyIn(Dist.CLIENT)
public class RockBaseRenderer extends MobRenderer<RockBase, ModelRockBase> {
    private static final ResourceLocation TEX1 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rocktexture.png");
    private static final ResourceLocation TEX2 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rocktexture.png");
    private static final ResourceLocation TEX3 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockredtexture.png");
    private static final ResourceLocation TEX4 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockgreentexture.png");
    private static final ResourceLocation TEX5 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockbluetexture.png");
    private static final ResourceLocation TEX6 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockpurpletexture.png");
    private static final ResourceLocation TEX7 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rocktexture.png");
    private static final ResourceLocation TEX8 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rocktnttexture.png");
    private static final ResourceLocation TEX9 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockcrystaltexture.png");
    private static final ResourceLocation TEX10 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockcrystalgreentexture.png");
    private static final ResourceLocation TEX11 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockcrystalbluetexture.png");
    private static final ResourceLocation TEX12 =
            ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/rockcrystaltnttexture.png");

    /** Gold ClientProxy: {@code new RenderRockBase(new ModelRockBase(1.0F), 0.0F, 1.0F)}. */
    private static final float MODEL_SCALE = 1.0F;

    public RockBaseRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelRockBase(context.bakeLayer(ModelRockBase.LAYER_LOCATION), 1.0F),
                0.0F * MODEL_SCALE);
    }

    @Override
    protected void scale(RockBase entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(RockBase entity) {
        return switch (entity.getRockType()) {
            case 1 -> TEX1;
            case 2 -> TEX2;
            case 3 -> TEX3;
            case 4 -> TEX4;
            case 5 -> TEX5;
            case 6 -> TEX6;
            case 7 -> TEX7;
            case 8 -> TEX8;
            case 9 -> TEX9;
            case 10 -> TEX10;
            case 11 -> TEX11;
            case 12 -> TEX12;
            default -> TEX1;
        };
    }
}
