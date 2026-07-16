package danger.orespawn.client.model;

import danger.orespawn.entity.Camarasaurus;
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
 * Port of gold {@code ModelCamarasaurus} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelCamarasaurus extends HierarchicalModel<Camarasaurus> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart Body1;
    private final ModelPart Body2;
    private final ModelPart Body3;
    private final ModelPart Body4;
    private final ModelPart Tail0;
    private final ModelPart Neck1;
    private final ModelPart Neck2;
    private final ModelPart Neck3;
    private final ModelPart Head1;
    private final ModelPart Head2;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart BLegupleft;
    private final ModelPart FLegupleft;
    private final ModelPart BLegupright;
    private final ModelPart FLegupright;
    private final ModelPart BLegdownright;
    private final ModelPart FLegdownleft;
    private final ModelPart FLegdownright;
    private final ModelPart BLegdownleft;

    public ModelCamarasaurus(ModelPart root) {
        this(root, 1.5F);
    }

    public ModelCamarasaurus(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.Body1 = root.getChild("Body1");
        this.Body2 = root.getChild("Body2");
        this.Body3 = root.getChild("Body3");
        this.Body4 = root.getChild("Body4");
        this.Tail0 = root.getChild("Tail0");
        this.Neck1 = root.getChild("Neck1");
        this.Neck2 = root.getChild("Neck2");
        this.Neck3 = root.getChild("Neck3");
        this.Head1 = root.getChild("Head1");
        this.Head2 = root.getChild("Head2");
        this.Tail1 = root.getChild("Tail1");
        this.Tail2 = root.getChild("Tail2");
        this.Tail3 = root.getChild("Tail3");
        this.BLegupleft = root.getChild("BLegupleft");
        this.FLegupleft = root.getChild("FLegupleft");
        this.BLegupright = root.getChild("BLegupright");
        this.FLegupright = root.getChild("FLegupright");
        this.BLegdownright = root.getChild("BLegdownright");
        this.FLegdownleft = root.getChild("FLegdownleft");
        this.FLegdownright = root.getChild("FLegdownright");
        this.BLegdownleft = root.getChild("BLegdownleft");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body1",
                CubeListBuilder.create().texOffs(0, 135)
                        .addBox(-6.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        root.addOrReplaceChild("Body2",
                CubeListBuilder.create().texOffs(0, 160)
                        .addBox(-5.0F, 0.0F, 0.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -4.0F, -0.1858931F, 0.0F, 0.0F));
        root.addOrReplaceChild("Body3",
                CubeListBuilder.create().texOffs(0, 177)
                        .addBox(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, -6.0F, -0.3346075F, 0.0F, 0.0F));
        root.addOrReplaceChild("Body4",
                CubeListBuilder.create().texOffs(0, 120)
                        .addBox(-5.0F, 0.0F, 0.0F, 10.0F, 10.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 11.0F));
        root.addOrReplaceChild("Tail0",
                CubeListBuilder.create().texOffs(0, 107)
                        .addBox(-3.0F, -2.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("Neck1",
                CubeListBuilder.create().texOffs(0, 190)
                        .addBox(-3.0F, 0.0F, 0.0F, 6.0F, 6.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -9.0F, -0.4089647F, 0.0F, 0.0F));
        root.addOrReplaceChild("Neck2",
                CubeListBuilder.create().texOffs(0, 202)
                        .addBox(-2.0F, 0.0F, -6.0F, 4.0F, 4.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, -9.0F, -0.5948578F, 0.0F, 0.0F));
        root.addOrReplaceChild("Neck3",
                CubeListBuilder.create().texOffs(0, 214)
                        .addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -5.0F, -15.0F, -0.8179294F, 0.0F, 0.0F));
        root.addOrReplaceChild("Head1",
                CubeListBuilder.create().texOffs(0, 232)
                        .addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -13.0F, -22.0F, -0.1115358F, 0.0F, 0.0F));
        root.addOrReplaceChild("Head2",
                CubeListBuilder.create().texOffs(0, 245)
                        .addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, -13.0F, -27.0F));
        root.addOrReplaceChild("Tail1",
                CubeListBuilder.create().texOffs(0, 93)
                        .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, 19.0F, -0.1115358F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail2",
                CubeListBuilder.create().texOffs(0, 82)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 26.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("Tail3",
                CubeListBuilder.create().texOffs(0, 73)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 4.5F, 34.0F, -0.0371786F, 0.0F, 0.0F));
        root.addOrReplaceChild("BLegupleft",
                CubeListBuilder.create().texOffs(49, 157)
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offsetAndRotation(2.0F, 9.0F, 7.0F, -0.1487195F, 0.0F, 0.0F));
        root.addOrReplaceChild("FLegupleft",
                CubeListBuilder.create().texOffs(49, 141)
                        .addBox(0.0F, 0.0F, -6.0F, 6.0F, 9.0F, 6.0F),
                PartPose.offset(2.0F, 8.0F, 2.0F));
        root.addOrReplaceChild("BLegupright",
                CubeListBuilder.create().texOffs(49, 126)
                        .addBox(-6.0F, 0.0F, 0.0F, 6.0F, 8.0F, 6.0F),
                PartPose.offsetAndRotation(-2.0F, 9.0F, 7.0F, -0.1487144F, 0.0F, 0.0F));
        root.addOrReplaceChild("FLegupright",
                CubeListBuilder.create().texOffs(49, 110)
                        .addBox(-6.0F, 0.0F, -6.0F, 6.0F, 9.0F, 6.0F),
                PartPose.offset(-2.0F, 8.0F, 2.0F));
        root.addOrReplaceChild("BLegdownright",
                CubeListBuilder.create().texOffs(115, 157)
                        .addBox(-5.0F, 7.0F, -1.0F, 5.0F, 8.0F, 5.0F),
                PartPose.offset(-2.0F, 9.0F, 7.0F));
        root.addOrReplaceChild("FLegdownleft",
                CubeListBuilder.create().texOffs(94, 143)
                        .addBox(0.0F, 8.0F, -6.0F, 5.0F, 8.0F, 5.0F),
                PartPose.offset(2.0F, 8.0F, 2.0F));
        root.addOrReplaceChild("FLegdownright",
                CubeListBuilder.create().texOffs(94, 157)
                        .addBox(-5.0F, 8.0F, -6.0F, 5.0F, 8.0F, 5.0F),
                PartPose.offset(-2.0F, 8.0F, 2.0F));
        root.addOrReplaceChild("BLegdownleft",
                CubeListBuilder.create().texOffs(115, 143)
                        .addBox(0.0F, 7.0F, -1.0F, 5.0F, 8.0F, 5.0F),
                PartPose.offset(2.0F, 9.0F, 7.0F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Camarasaurus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void animate(Camarasaurus entity, float f, float f1, float f2, float f3, float f4) {

        float hf = 0.0F;
        float newangle = 0.0F;
        if (f1 > 0.1) {
        newangle = Mth.cos(f2 * 0.4F * this.wingspeed) * (float) Math.PI * 0.25F * f1;
        } else {
        newangle = 0.0F;
        }
        this.FLegupleft.xRot = newangle;
        this.FLegdownleft.xRot = newangle;
        this.FLegupright.xRot = -newangle;
        this.FLegdownright.xRot = -newangle;
        this.BLegupleft.xRot = -0.15F - newangle;
        this.BLegdownleft.xRot = -newangle;
        this.BLegupright.xRot = -0.15F + newangle;
        this.BLegdownright.xRot = newangle;
        hf = entity.getHealth() / entity.getMaxHealth();
        newangle = Mth.cos(f2 * 0.8F * this.wingspeed * hf) * (float) Math.PI * 0.25F * hf;
        if (false /* sitting deferred */) {
        newangle = 0.0F;
        }
        this.Tail0.yRot = newangle * 0.25F;
        this.Tail1.z = this.Tail0.z + (float)Math.cos(this.Tail0.yRot) * 5.0F;
        this.Tail1.x = this.Tail0.x + (float)Math.sin(this.Tail0.yRot) * 5.0F;
        this.Tail1.yRot = newangle * 0.5F;
        this.Tail2.z = this.Tail1.z + (float)Math.cos(this.Tail1.yRot) * 8.0F;
        this.Tail2.x = this.Tail1.x + (float)Math.sin(this.Tail1.yRot) * 8.0F;
        this.Tail2.yRot = newangle * 0.75F;
        this.Tail3.z = this.Tail2.z + (float)Math.cos(this.Tail2.yRot) * 7.0F;
        this.Tail3.x = this.Tail2.x + (float)Math.sin(this.Tail2.yRot) * 7.0F;
        this.Tail3.yRot = newangle * 1.0F;
        this.Neck1.yRot = (float)Math.toRadians(f3) * 0.125F;
        this.Neck2.z = this.Neck1.z;
        this.Neck2.x = this.Neck1.x;
        this.Neck2.yRot = (float)Math.toRadians(f3) * 0.25F;
        this.Neck3.z = this.Neck2.z - (float)Math.cos(this.Neck2.yRot) * 6.0F;
        this.Neck3.x = this.Neck2.x - (float)Math.sin(this.Neck2.yRot) * 6.0F;
        this.Neck3.yRot = (float)Math.toRadians(f3) * 0.38F;
        this.Head1.z = this.Neck3.z - (float)Math.cos(this.Neck3.yRot) * 7.0F;
        this.Head1.x = this.Neck3.x - (float)Math.sin(this.Neck3.yRot) * 7.0F;
        this.Head1.yRot = (float)Math.toRadians(f3);
        this.Head2.z = this.Head1.z - (float)Math.cos(this.Head1.yRot) * 5.0F;
        this.Head2.x = this.Head1.x - (float)Math.sin(this.Head1.yRot) * 5.0F;
        this.Head2.yRot = (float)Math.toRadians(f3);
    }
}
