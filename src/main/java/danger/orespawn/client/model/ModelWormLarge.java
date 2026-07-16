package danger.orespawn.client.model;

import danger.orespawn.entity.WormLarge;
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
 * Port of gold {@code ModelWormLarge} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWormLarge extends HierarchicalModel<WormLarge> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart head3;
    private final ModelPart head4;
    private final ModelPart head5;
    private final ModelPart neck1;
    private final ModelPart neck4;
    private final ModelPart neck5;
    private final ModelPart neck2;
    private final ModelPart neck3;
    private final ModelPart tail1;
    private final ModelPart tailtip;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tooth1;
    private final ModelPart tooth2;
    private final ModelPart tooth3;
    private final ModelPart tooth4;
    private final ModelPart tooth5;
    private final ModelPart tooth6;
    private final ModelPart tooth7;
    private final ModelPart tooth8;

    public ModelWormLarge(ModelPart root) {
        this(root, 1F);
    }

    public ModelWormLarge(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.head3 = root.getChild("head3");
        this.head4 = root.getChild("head4");
        this.head5 = root.getChild("head5");
        this.neck1 = root.getChild("neck1");
        this.neck4 = root.getChild("neck4");
        this.neck5 = root.getChild("neck5");
        this.neck2 = root.getChild("neck2");
        this.neck3 = root.getChild("neck3");
        this.tail1 = root.getChild("tail1");
        this.tailtip = root.getChild("tailtip");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.tooth1 = root.getChild("tooth1");
        this.tooth2 = root.getChild("tooth2");
        this.tooth3 = root.getChild("tooth3");
        this.tooth4 = root.getChild("tooth4");
        this.tooth5 = root.getChild("tooth5");
        this.tooth6 = root.getChild("tooth6");
        this.tooth7 = root.getChild("tooth7");
        this.tooth8 = root.getChild("tooth8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -8.0F, -20.0F, 16.0F, 16.0F, 20.0F),
                PartPose.offset(0.0F, 0.0F, 10.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(83, 27)
                        .addBox(8.0F, -3.0F, -20.0F, 3.0F, 6.0F, 19.0F),
                PartPose.offset(0.0F, 0.0F, 10.0F));
        root.addOrReplaceChild("head3",
                CubeListBuilder.create().texOffs(9, 65)
                        .addBox(-11.0F, -3.0F, -20.0F, 3.0F, 6.0F, 19.0F),
                PartPose.offset(0.0F, 0.0F, 10.0F));
        root.addOrReplaceChild("head4",
                CubeListBuilder.create().texOffs(77, 0)
                        .addBox(-3.0F, -11.0F, -20.0F, 6.0F, 3.0F, 20.0F),
                PartPose.offset(0.0F, 0.0F, 10.0F));
        root.addOrReplaceChild("head5",
                CubeListBuilder.create().texOffs(10, 39)
                        .addBox(-3.0F, 8.0F, -20.0F, 6.0F, 3.0F, 20.0F),
                PartPose.offset(0.0F, 0.0F, 10.0F));
        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(25, 94)
                        .addBox(-6.0F, -6.0F, -36.0F, 12.0F, 12.0F, 36.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 33.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(25, 146)
                        .addBox(-2.0F, -8.0F, -38.0F, 4.0F, 2.0F, 38.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 33.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(125, 189)
                        .addBox(-2.0F, 6.0F, -31.0F, 4.0F, 2.0F, 31.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 33.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(25, 189)
                        .addBox(6.0F, -2.0F, -34.0F, 2.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 33.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(125, 147)
                        .addBox(-8.0F, -2.0F, -34.0F, 2.0F, 4.0F, 34.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 33.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(145, 21)
                        .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 24.0F),
                PartPose.offset(0.0F, 20.0F, 29.0F));
        root.addOrReplaceChild("tailtip",
                CubeListBuilder.create().texOffs(180, 0)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 19.5F, 52.0F, 0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(145, 56)
                        .addBox(4.0F, -1.0F, 2.0F, 1.0F, 2.0F, 14.0F),
                PartPose.offset(0.0F, 20.0F, 29.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(145, 90)
                        .addBox(-5.0F, -1.0F, 2.0F, 1.0F, 2.0F, 14.0F),
                PartPose.offset(0.0F, 20.0F, 29.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(145, 76)
                        .addBox(-1.0F, -5.0F, 7.0F, 2.0F, 1.0F, 9.0F),
                PartPose.offset(0.0F, 20.0F, 29.0F));
        root.addOrReplaceChild("tooth1",
                CubeListBuilder.create().texOffs(0, 220)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, 9.0F, -10.0F));
        root.addOrReplaceChild("tooth2",
                CubeListBuilder.create().texOffs(0, 210)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(0.0F, -9.0F, -10.0F));
        root.addOrReplaceChild("tooth3",
                CubeListBuilder.create().texOffs(0, 200)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(9.0F, 0.0F, -10.0F));
        root.addOrReplaceChild("tooth4",
                CubeListBuilder.create().texOffs(0, 190)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-9.0F, 0.0F, -10.0F));
        root.addOrReplaceChild("tooth5",
                CubeListBuilder.create().texOffs(0, 180)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-6.0F, -6.0F, -10.0F));
        root.addOrReplaceChild("tooth6",
                CubeListBuilder.create().texOffs(0, 170)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(6.0F, 6.0F, -10.0F));
        root.addOrReplaceChild("tooth7",
                CubeListBuilder.create().texOffs(0, 160)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(6.0F, -6.0F, -10.0F));
        root.addOrReplaceChild("tooth8",
                CubeListBuilder.create().texOffs(0, 150)
                        .addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 7.0F),
                PartPose.offset(-6.0F, 6.0F, -10.0F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(WormLarge entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(WormLarge entity, float f, float f1, float f2, float f3, float f4) {
        this.neck1.x = 0.0F; this.neck1.y = 20.0F; this.neck1.z = 33.0F;
        this.neck2.x = this.neck3.x = this.neck4.x = this.neck5.x = 0.0F;
        this.neck2.y = this.neck3.y = this.neck4.y = this.neck5.y = 20.0F;
        this.neck2.z = this.neck3.z = this.neck4.z = this.neck5.z = 33.0F;
        this.head1.x = 0.0F; this.head1.y = 0.0F; this.head1.z = 10.0F;

        double dist = 32.0;
        float newangle = Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.08F;
        newangle -= 0.698F;
        this.neck1.xRot = newangle;
        float newangle2 = Mth.cos(f2 * 0.15F) * (float) Math.PI * 0.07F;
        this.neck1.yRot = newangle2;
        this.neck2.xRot = this.neck3.xRot = this.neck4.xRot = this.neck5.xRot = this.neck1.xRot;
        this.neck2.yRot = this.neck3.yRot = this.neck4.yRot = this.neck5.yRot = this.neck1.yRot;
        double d1 = Math.cos(newangle) * dist;
        double d2 = Math.sin(newangle) * dist;
        this.head1.z = (float) (this.neck1.z - d1);
        double d3 = Math.sin(newangle2) * d1;
        this.head1.x = (float) (this.neck1.x - d3);
        this.head1.y = (float) (this.neck1.y + d2);
        this.head1.xRot = newangle = Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.15F;
        this.head1.yRot = newangle2 = Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.05F;
        this.head2.x = this.head3.x = this.head4.x = this.head5.x = this.head1.x;
        this.head2.y = this.head3.y = this.head4.y = this.head5.y = this.head1.y;
        this.head2.z = this.head3.z = this.head4.z = this.head5.z = this.head1.z;
        this.head2.xRot = this.head3.xRot = this.head4.xRot = this.head5.xRot = this.head1.xRot;
        this.head2.yRot = this.head3.yRot = this.head4.yRot = this.head5.yRot = this.head1.yRot;
        dist = 19.0;
        d1 = Math.cos(newangle) * dist;
        d2 = Math.sin(newangle) * dist;
        this.tooth1.z = (float) (this.head1.z - d1);
        d3 = Math.sin(newangle2) * d1;
        this.tooth1.x = (float) (this.head1.x - d3);
        this.tooth1.y = (float) (this.head1.y + d2 - 9.0);
        this.tooth2.z = this.tooth1.z;
        this.tooth2.x = this.tooth1.x;
        this.tooth2.y = this.tooth1.y + 18.0F;
        this.tooth3.z = this.tooth1.z;
        this.tooth3.x = this.tooth1.x + 9.0F;
        this.tooth3.y = this.tooth1.y + 9.0F;
        this.tooth4.z = this.tooth1.z;
        this.tooth4.x = this.tooth1.x - 9.0F;
        this.tooth4.y = this.tooth1.y + 9.0F;
        this.tooth5.z = this.tooth1.z;
        this.tooth5.x = this.tooth1.x - 6.0F;
        this.tooth5.y = this.tooth1.y + 9.0F - 6.0F;
        this.tooth6.z = this.tooth1.z;
        this.tooth6.x = this.tooth1.x + 6.0F;
        this.tooth6.y = this.tooth1.y + 9.0F + 6.0F;
        this.tooth7.z = this.tooth1.z;
        this.tooth7.x = this.tooth1.x + 6.0F;
        this.tooth7.y = this.tooth1.y + 9.0F - 6.0F;
        this.tooth8.z = this.tooth1.z;
        this.tooth8.x = this.tooth1.x - 6.0F;
        this.tooth8.y = this.tooth1.y + 9.0F + 6.0F;
        this.tooth1.z = (float) (this.tooth1.z - Math.sin(this.head1.xRot) * 9.0);
        this.tooth2.z = (float) (this.tooth2.z + Math.sin(this.head1.xRot) * 9.0);
        this.tooth3.z = (float) (this.tooth3.z - Math.sin(this.head1.yRot) * 9.0);
        this.tooth4.z = (float) (this.tooth4.z + Math.sin(this.head1.yRot) * 9.0);
        this.tooth7.z = (float) (this.tooth7.z - Math.sin(this.head1.xRot) * 6.0 - Math.sin(this.head1.yRot) * 6.0);
        this.tooth6.z = (float) (this.tooth6.z + Math.sin(this.head1.xRot) * 6.0 - Math.sin(this.head1.yRot) * 6.0);
        this.tooth5.z = (float) (this.tooth5.z - Math.sin(this.head1.xRot) * 6.0 + Math.sin(this.head1.yRot) * 6.0);
        this.tooth8.z = (float) (this.tooth8.z + Math.sin(this.head1.xRot) * 6.0 + Math.sin(this.head1.yRot) * 6.0);
        newangle = Mth.cos(f2 * 0.57F) * (float) Math.PI * 0.35F;
        this.tooth1.xRot = this.head1.xRot + newangle;
        this.tooth2.xRot = this.head1.xRot - newangle;
        this.tooth3.yRot = this.head1.yRot + newangle;
        this.tooth4.yRot = this.head1.yRot - newangle;
        this.tooth5.xRot = this.head1.xRot + newangle;
        this.tooth7.xRot = this.head1.xRot + newangle;
        this.tooth6.xRot = this.head1.xRot - newangle;
        this.tooth8.xRot = this.head1.xRot - newangle;
        this.tooth6.yRot = this.head1.yRot + newangle;
        this.tooth7.yRot = this.head1.yRot + newangle;
        this.tooth5.yRot = this.head1.yRot - newangle;
        this.tooth8.yRot = this.head1.yRot - newangle;
        newangle = Mth.cos(f2 * 0.63F) * (float) Math.PI * 0.15F;
        this.tailtip.xRot = newangle + 0.35F;
        newangle = Mth.cos((float) (f2 * 0.63F + 1.57075)) * (float) Math.PI * 0.15F;
        this.tailtip.yRot = newangle;
    }
}

