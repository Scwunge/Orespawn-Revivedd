package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.entity.ThrownRock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Gold {@code RenderThrownRock} — billboard item sprite by rock type.
 * Textures: {@code textures/item/rock*.png} when items registered; cobble fallback.
 */
public class ThrownRockRenderer extends EntityRenderer<ThrownRock> {
    private final ItemRenderer itemRenderer;

    public ThrownRockRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ThrownRock entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
        pose.pushPose();
        pose.scale(0.5F, 0.5F, 0.5F);
        pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pose.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        this.itemRenderer.renderStatic(
                stackForType(entity.getRockType()),
                ItemDisplayContext.GROUND,
                light,
                OverlayTexture.NO_OVERLAY,
                pose,
                buf,
                entity.level(),
                entity.getId());
        pose.popPose();
        super.render(entity, yaw, pt, pose, buf, light);
    }

    private static ItemStack stackForType(int type) {
        String name = switch (type) {
            case 1 -> "rocksmall";
            case 2 -> "rock";
            case 3 -> "rockred";
            case 4 -> "rockgreen";
            case 5 -> "rockblue";
            case 6 -> "rockpurple";
            case 7 -> "rockspikey";
            case 8 -> "rocktnt";
            case 9 -> "rockcrystalred";
            case 10 -> "rockcrystalgreen";
            case 11 -> "rockcrystalblue";
            case 12 -> "rockcrystaltnt";
            default -> "rocksmall";
        };
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", name));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        return new ItemStack(Items.COBBLESTONE);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownRock entity) {
        String name = switch (entity.getRockType()) {
            case 1 -> "rocksmall";
            case 2 -> "rock";
            case 3 -> "rockred";
            case 4 -> "rockgreen";
            case 5 -> "rockblue";
            case 6 -> "rockpurple";
            case 7 -> "rockspikey";
            case 8 -> "rocktnt";
            case 9 -> "rockcrystalred";
            case 10 -> "rockcrystalgreen";
            case 11 -> "rockcrystalblue";
            case 12 -> "rockcrystaltnt";
            default -> "rocksmall";
        };
        return ResourceLocation.fromNamespaceAndPath("orespawn", "textures/item/" + name + ".png");
    }
}
