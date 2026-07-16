package danger.orespawn.client.model;

import danger.orespawn.entity.Rat;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelRat} (1.7.10 ModelBase, tex 64×64) → 1.21 HierarchicalModel.
 * Parts: body, tail1, tail2, lfleg, rfleg, lrleg, rrleg, body2, head, nose, lear, rear.
 */
@OnlyIn(Dist.CLIENT)
public class ModelRat extends HierarchicalModel<Rat> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart lfleg;
    private final ModelPart rfleg;
    private final ModelPart lrleg;
    private final ModelPart rrleg;
    private final ModelPart body2;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart lear;
    private final ModelPart rear;

    public ModelRat(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRat(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.body = root.getChild("body");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.lfleg = root.getChild("lfleg");
        this.rfleg = root.getChild("rfleg");
        this.lrleg = root.getChild("lrleg");
        this.rrleg = root.getChild("rrleg");
        this.body2 = root.getChild("body2");
        this.head = root.getChild("head");
        this.nose = root.getChild("nose");
        this.lear = root.getChild("lear");
        this.rear = root.getChild("rear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(27, 0)
                        .addBox(-2.0F, -1.0F, 0.0F, 5.0F, 3.0F, 10.0F),
                PartPose.offset(0.0F, 20.0F, -3.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 30)
                        .addBox(-0.5F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
                PartPose.offset(0.0F, 21.0F, 7.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 43)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F),
                PartPose.offset(0.0F, 21.0F, 16.0F));
        root.addOrReplaceChild("lfleg",
                CubeListBuilder.create().texOffs(0, 14)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(2.0F, 22.0F, -2.0F));
        root.addOrReplaceChild("rfleg",
                CubeListBuilder.create().texOffs(10, 14)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offset(-2.0F, 22.0F, -2.0F));
        root.addOrReplaceChild("lrleg",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(2.0F, 20.0F, 4.0F));
        root.addOrReplaceChild("rrleg",
                CubeListBuilder.create().texOffs(9, 18)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(-3.0F, 20.0F, 4.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(0.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(27, 17)
                        .addBox(-1.0F, -2.0F, -3.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 22.0F, -4.0F));
        root.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(27, 25)
                        .addBox(0.0F, -1.0F, -5.0F, 1.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 22.0F, -4.0F));
        root.addOrReplaceChild("lear",
                CubeListBuilder.create().texOffs(0, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(1.5F, 19.5F, -4.0F));
        root.addOrReplaceChild("rear",
                CubeListBuilder.create().texOffs(5, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.5F, 19.5F, -4.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Rat entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float newangle;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.7F * this.wingspeed) * (float) Math.PI * 0.25F * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }
        this.rfleg.xRot = newangle;
        this.lfleg.xRot = -newangle;
        this.rrleg.xRot = -newangle;
        this.lrleg.xRot = newangle;

        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(ageInTicks * 1.5F * this.wingspeed) * (float) Math.PI * 0.25F;
        } else {
            newangle = Mth.cos(ageInTicks * 0.4F * this.wingspeed) * (float) Math.PI * 0.05F;
        }
        this.tail1.yRot = newangle * 0.5F;
        this.tail2.yRot = newangle * 1.25F;
        // gold: tail2 follows tail1 yaw offset by length 9
        this.tail2.z = this.tail1.z + (float) Math.cos(this.tail1.yRot) * 9.0F;
        this.tail2.x = this.tail1.x + (float) Math.sin(this.tail1.yRot) * 9.0F;
    }
}
