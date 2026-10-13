package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelSquidZooka;
import net.minecraft.client.model.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold {@code RenderSquidZooka}: transforms copied 1:1 onto the 1.7.10 hand space ({@link GoldWeaponItemRenderer}). */
@OnlyIn(Dist.CLIENT)
public class SquidZookaItemRenderer extends GoldWeaponItemRenderer {
    public SquidZookaItemRenderer() {
        super("squidzookatexture", "squid_zooka_icon");
    }

    @Override
    protected Model createModel() {
        return new ModelSquidZooka(ModelSquidZooka.createBodyLayer().bakeRoot());
    }

    @Override
    protected void goldFirstPerson(PoseStack pose) {
        glRotate(pose, -30.0F, 0.0F, 1.0F, 0.0F);
        pose.scale(0.35F, 0.35F, 0.35F);
        pose.translate(4.0F, 2.0F, 2.0F);
    }

    @Override
    protected void goldThirdPerson(PoseStack pose) {
        glRotate(pose, 30.0F, 0.0F, 1.0F, 0.0F);
        pose.scale(0.35F, 0.35F, 0.35F);
        pose.translate(2.0F, 8.0F, 2.0F);
    }
}
