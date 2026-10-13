package danger.orespawn.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import danger.orespawn.client.model.ModelQueenBattleAxe;
import net.minecraft.client.model.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Gold {@code RenderQueenBattleAxe}: transforms copied 1:1 onto the 1.7.10 hand space ({@link GoldWeaponItemRenderer}). */
@OnlyIn(Dist.CLIENT)
public class QueenBattleAxeItemRenderer extends GoldWeaponItemRenderer {
    public QueenBattleAxeItemRenderer() {
        super("queenbattleaxetexture", "queen_battle_axe_icon");
    }

    @Override
    protected Model createModel() {
        return new ModelQueenBattleAxe(ModelQueenBattleAxe.createBodyLayer().bakeRoot());
    }

    @Override
    protected void goldFirstPerson(PoseStack pose) {
        glRotate(pose, 50.0F, 0.0F, 1.0F, 0.0F);
        glRotate(pose, 190.0F, 1.0F, 0.0F, 0.0F);
        glRotate(pose, 15.0F, 0.0F, 0.0F, 1.0F);
        pose.scale(0.35F, 0.35F, 0.35F);
        pose.translate(-2.0F, -4.0F, -6.0F);
    }

    @Override
    protected void goldThirdPerson(PoseStack pose) {
        glRotate(pose, 180.0F, 1.0F, 0.25F, 0.0F);
        pose.scale(0.35F, 0.35F, 0.35F);
        pose.translate(3.0F, -8.0F, -2.0F);
    }
}
