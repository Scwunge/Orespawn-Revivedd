package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Shared renderer for the 1.7.10 {@code IItemRenderer} weapons (Big Bertha, Slice, Royal, Hammy,
 * Battle Axe, Chainsaw, Queen Battle Axe, SquidZooka).
 * <p>
 * Gold's per-weapon numbers ({@code renderSword()} / {@code renderSwordF5()}) only make sense on top
 * of the 1.7.10 hand transforms, so this rebuilds that exact space instead of guessing new poses:
 * <ul>
 *   <li>Third person: undo 1.21's {@code ItemInHandLayer} offsets back to arm space, then apply
 *       1.7.10 {@code RenderPlayer.renderEquippedItems} for a 3D custom renderer (translate,
 *       rotate X20 / Y45, scale -0.375/-0.375/0.375) and Forge's EQUIPPED_BLOCK -0.5 helper.</li>
 *   <li>First person: 1.21 {@code applyItemArmTransform} matches 1.7.10's (0.56, -0.52, -0.72) and
 *       its swing differs only by a trailing Y-45, so re-add 45°, then 1.7.10's scale 0.4 and the
 *       -0.5 helper.</li>
 * </ul>
 * Models were drawn with {@code f5 = 1.0} (1 box unit = 1 GL unit) while 1.21 ModelParts are
 * 1/16 scale, hence the final {@code scale(16)}. Left hands mirror gold's right-hand pose.
 * GUI, ground and item frames use the flat {@code *_icon} sprite (gold left those to vanilla).
 */
@OnlyIn(Dist.CLIENT)
public abstract class GoldWeaponItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final ResourceLocation texture;
    private final ModelResourceLocation icon;
    private Model model;

    protected GoldWeaponItemRenderer(String texture, String icon) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.texture = ResourceLocation.fromNamespaceAndPath("orespawn", "textures/entity/" + texture + ".png");
        this.icon = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("orespawn", "item/" + icon));
    }

    protected abstract Model createModel();

    /** Gold {@code renderSword()} body (EQUIPPED_FIRST_PERSON), without push/pop. */
    protected abstract void goldFirstPerson(PoseStack pose);

    /** Gold {@code renderSwordF5()} body (EQUIPPED), without push/pop. */
    protected abstract void goldThirdPerson(PoseStack pose);

    /** {@code GL11.glRotatef(degrees, x, y, z)} — GL normalises the axis. */
    protected static void glRotate(PoseStack pose, float degrees, float x, float y, float z) {
        pose.mulPose(new Quaternionf().rotationAxis(
                (float) Math.toRadians(degrees), new Vector3f(x, y, z).normalize()));
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            this.model = this.createModel();
        } catch (Exception e) {
            this.model = null;
        }
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose,
            MultiBufferSource buffer, int light, int overlay) {
        if (this.model == null) {
            this.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
        }
        boolean firstPerson = context.firstPerson();
        boolean thirdPerson = context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        if (this.model == null || (!firstPerson && !thirdPerson)) {
            this.renderIcon(stack, context, pose, buffer, light, overlay);
            return;
        }

        boolean left = context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        float side = left ? -1.0F : 1.0F;
        pose.pushPose();
        // ItemRenderer always applies translate(-0.5) before a BEWLR
        pose.translate(0.5F, 0.5F, 0.5F);
        if (firstPerson) {
            pose.mulPose(Axis.YP.rotationDegrees(side * 45.0F));
            if (left) {
                pose.scale(-1.0F, 1.0F, 1.0F);
            }
            pose.scale(0.4F, 0.4F, 0.4F);
            pose.translate(-0.5F, -0.5F, -0.5F);
            this.goldFirstPerson(pose);
        } else {
            // undo ItemInHandLayer: rotX(-90) rotY(180) translate(side/16, 0.125, -0.625)
            pose.translate(-side / 16.0F, -0.125F, 0.625F);
            pose.mulPose(Axis.YP.rotationDegrees(-180.0F));
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (left) {
                pose.scale(-1.0F, 1.0F, 1.0F);
            }
            // 1.7.10 RenderPlayer.renderEquippedItems, 3D custom renderer branch
            pose.translate(-0.0625F, 0.4375F, 0.0625F);
            pose.translate(0.0F, 0.1875F, -0.3125F);
            pose.mulPose(Axis.XP.rotationDegrees(20.0F));
            pose.mulPose(Axis.YP.rotationDegrees(45.0F));
            pose.scale(-0.375F, -0.375F, 0.375F);
            pose.translate(-0.5F, -0.5F, -0.5F);
            this.goldThirdPerson(pose);
        }
        pose.scale(16.0F, 16.0F, 16.0F);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(
                buffer, RenderType.entityCutoutNoCull(this.texture), false, stack.hasFoil());
        this.model.renderToBuffer(pose, consumer, light, overlay, 0xFFFFFFFF);
        pose.popPose();
    }

    /** Flat {@code *_icon} sprite for GUI, dropped items, item frames and heads. */
    private void renderIcon(ItemStack stack, ItemDisplayContext context, PoseStack pose,
            MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel iconModel = mc.getModelManager().getModel(this.icon);
        if (iconModel == null || iconModel == mc.getModelManager().getMissingModel()) {
            return;
        }
        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        // the icon is a normal handheld model, so its own gui/ground/fixed display transforms apply
        mc.getItemRenderer().render(stack, context, false, pose, buffer, light, overlay, iconModel);
        pose.popPose();
    }
}
