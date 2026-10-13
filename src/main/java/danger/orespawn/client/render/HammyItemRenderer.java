package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelHammy;
import net.minecraft.client.model.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold {@code RenderHammy}: transforms copied 1:1 onto the 1.7.10 hand space ({@link GoldWeaponItemRenderer}). */
@OnlyIn(Dist.CLIENT)
public class HammyItemRenderer extends GoldWeaponItemRenderer {
    public HammyItemRenderer() {
        super("attitudeadjustertexture", "hammy_icon");
    }

    @Override
    protected Model createModel() {
        return new ModelHammy(ModelHammy.createBodyLayer().bakeRoot());
    }

    @Override
    protected void goldFirstPerson(PoseStack pose) {
        glRotate(pose, 70.0F, 0.0F, 1.0F, 0.0F);
        glRotate(pose, 190.0F, 1.0F, 0.0F, 0.0F);
        glRotate(pose, 25.0F, 0.0F, 0.0F, 1.0F);
        pose.scale(0.15F, 0.15F, 0.15F);
        pose.translate(-10.0F, -13.0F, -5.0F);
    }

    @Override
    protected void goldThirdPerson(PoseStack pose) {
        glRotate(pose, 180.0F, 1.0F, 0.0F, 0.0F);
        pose.scale(0.15F, 0.15F, 0.15F);
        pose.translate(6.0F, -20.0F, -4.0F);
    }
}
