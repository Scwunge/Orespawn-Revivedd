package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import danger.orespawn.entity.Shoes;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Gold {@code RenderShoe} extends {@code RenderSpinner} — billboard spinning item sprite
 * by ShoeId (2 redheels, 3 blackheels, 4 slippers, 5 boots, 6 gamecontroller).
 * Port uses item renderer like ThrownRock / WaterBall when item textures exist.
 */
@OnlyIn(Dist.CLIENT)
public class ShoesRenderer extends EntityRenderer<Shoes> {
    private final ItemRenderer itemRenderer;

    public ShoesRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(Shoes entity, float yaw, float pt, PoseStack pose, MultiBufferSource buf, int light) {
        pose.pushPose();
        pose.scale(0.5F, 0.5F, 0.5F);
        pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pose.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        this.itemRenderer.renderStatic(
                stackForShoeId(entity.getShoeId()),
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

    private static ItemStack stackForShoeId(int id) {
        String name = switch (id) {
            case 2 -> "redheels";
            case 3 -> "blackheels";
            case 4 -> "slippers";
            case 5 -> "boots";
            case 6 -> "gamecontroller";
            default -> "redheels";
        };
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", name));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        // fallbacks if items not registered yet
        return switch (id) {
            case 6 -> new ItemStack(Items.IRON_BOOTS);
            default -> new ItemStack(Items.LEATHER_BOOTS);
        };
    }

    @Override
    public ResourceLocation getTextureLocation(Shoes entity) {
        String name = switch (entity.getShoeId()) {
            case 2 -> "redheels";
            case 3 -> "blackheels";
            case 4 -> "slippers";
            case 5 -> "boots";
            case 6 -> "gamecontroller";
            default -> "redheels";
        };
        return ResourceLocation.fromNamespaceAndPath("orespawn", "textures/item/" + name + ".png");
    }
}
