package danger.orespawn.client.model;

import danger.orespawn.entity.Mothra;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold ModelButterfly geometry for {@code Mothra} (gold uses ModelButterfly).
 */
@OnlyIn(Dist.CLIENT)
public class ModelMothra extends HierarchicalModel<Mothra> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftwing;
    private final ModelPart rightwing;
    private final ModelPart leftwing2;
    private final ModelPart rightwing2;
    private final ModelPart leftwing3;
    private final ModelPart rightwing3;
    private final ModelPart head;
    private final ModelPart leftwing4;
    private final ModelPart rightwing4;

    public ModelMothra(ModelPart root) {
        this(root, 0.1F);
    }

    public ModelMothra(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.leftwing = root.getChild("leftwing");
        this.rightwing = root.getChild("rightwing");
        this.leftwing2 = root.getChild("leftwing2");
        this.rightwing2 = root.getChild("rightwing2");
        this.leftwing3 = root.getChild("leftwing3");
        this.rightwing3 = root.getChild("rightwing3");
        this.head = root.getChild("head");
        this.leftwing4 = root.getChild("leftwing4");
        this.rightwing4 = root.getChild("rightwing4");
    }

    public static LayerDefinition createBodyLayer() {
        return ModelButterfly.createBodyLayer();
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Mothra entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float wing = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.rightwing.zRot = wing;
        this.rightwing2.zRot = wing;
        this.rightwing3.zRot = wing;
        this.rightwing4.zRot = wing;
        this.leftwing.zRot = -wing;
        this.leftwing2.zRot = -wing;
        this.leftwing3.zRot = -wing;
        this.leftwing4.zRot = -wing;
    }
}
