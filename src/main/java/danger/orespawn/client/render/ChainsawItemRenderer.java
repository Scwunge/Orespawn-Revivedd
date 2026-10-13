package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelChainsaw;
import net.minecraft.client.model.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold {@code RenderChainsaw}: transforms copied 1:1 onto the 1.7.10 hand space ({@link GoldWeaponItemRenderer}). */
@OnlyIn(Dist.CLIENT)
public class ChainsawItemRenderer extends GoldWeaponItemRenderer {
    public ChainsawItemRenderer() {
        super("chainsawtexture", "chainsaw_icon");
    }

    @Override
    protected Model createModel() {
        return new ModelChainsaw(ModelChainsaw.createBodyLayer().bakeRoot());
    }

    @Override
    protected void goldFirstPerson(PoseStack pose) {
        glRotate(pose, 150.0F, 0.0F, 1.0F, 0.0F);
        glRotate(pose, 100.0F, 1.0F, 0.0F, 0.0F);
        pose.scale(0.25F, 0.25F, 0.25F);
        pose.translate(-10.0F, 1.0F, -4.0F);
    }

    @Override
    protected void goldThirdPerson(PoseStack pose) {
        glRotate(pose, 180.0F, 0.0F, 0.0F, 1.0F);
        glRotate(pose, -20.0F, 0.0F, 1.0F, 0.0F);
        glRotate(pose, -20.0F, 1.0F, 0.0F, 0.0F);
        pose.scale(0.25F, 0.25F, 0.25F);
        pose.translate(-3.0F, -3.0F, -2.0F);
    }
}
