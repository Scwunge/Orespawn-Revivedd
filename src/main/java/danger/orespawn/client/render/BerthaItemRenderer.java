package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModModelLayers;
import danger.orespawn.client.model.ModelBertha;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold {@code RenderBertha}: transforms copied 1:1 onto the 1.7.10 hand space ({@link GoldWeaponItemRenderer}). */
@OnlyIn(Dist.CLIENT)
public class BerthaItemRenderer extends GoldWeaponItemRenderer {
    public BerthaItemRenderer() {
        super("bertha", "bertha_icon");
    }

    @Override
    protected Model createModel() {
        return new ModelBertha(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayers.BERTHA));
    }

    @Override
    protected void goldFirstPerson(PoseStack pose) {
        glRotate(pose, 190.0F, 1.0F, 0.0F, 0.0F);
        glRotate(pose, 25.0F, 0.0F, 0.0F, 1.0F);
        pose.scale(0.25F, 0.25F, 0.25F);
        pose.translate(6.0F, 3.0F, -5.0F);
    }

    @Override
    protected void goldThirdPerson(PoseStack pose) {
        glRotate(pose, 90.0F, 1.0F, 0.0F, 0.0F);
        glRotate(pose, -90.0F, 0.0F, 0.0F, 1.0F);
        pose.scale(0.25F, 0.25F, 0.25F);
        pose.translate(-4.0F, 2.0F, -3.0F);
    }
}
