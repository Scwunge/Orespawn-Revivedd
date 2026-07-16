package danger.orespawn.client.model;

import danger.orespawn.entity.TerribleTerror;
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
 * Port of gold {@code ModelTerribleTerror} (1.7.10 ModelBase, tex 119×72) → 1.21 HierarchicalModel.
 * Cube sizes/UVs/offsets 1:1. Animation from gold {@code render()}.
 */
@OnlyIn(Dist.CLIENT)
public class ModelTerribleTerror extends HierarchicalModel<TerribleTerror> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart horn1;
    private final ModelPart horn2;
    private final ModelPart snout;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart neck;
    private final ModelPart body;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart fl11;
    private final ModelPart fl12;
    private final ModelPart fl21;
    private final ModelPart fl22;
    private final ModelPart bl21;
    private final ModelPart bl22;
    private final ModelPart bl11;
    private final ModelPart bl12;

    public ModelTerribleTerror(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelTerribleTerror(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.horn1 = root.getChild("horn1");
        this.horn2 = root.getChild("horn2");
        this.snout = root.getChild("snout");
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.neck = root.getChild("neck");
        this.body = root.getChild("body");
        this.wing1 = root.getChild("wing1");
        this.wing2 = root.getChild("wing2");
        this.tail1 = root.getChild("tail1");
        this.tail2 = root.getChild("tail2");
        this.tail3 = root.getChild("tail3");
        this.tail4 = root.getChild("tail4");
        this.fl11 = root.getChild("fl11");
        this.fl12 = root.getChild("fl12");
        this.fl21 = root.getChild("fl21");
        this.fl22 = root.getChild("fl22");
        this.bl21 = root.getChild("bl21");
        this.bl22 = root.getChild("bl22");
        this.bl11 = root.getChild("bl11");
        this.bl12 = root.getChild("bl12");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("horn1",
                CubeListBuilder.create().texOffs(90, 0)
                        .addBox(1.0F, -4.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("horn2",
                CubeListBuilder.create().texOffs(102, 0)
                        .addBox(-1.0F, -4.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-2.0F, -1.0F, -4.0F, 4.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(41, 0)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(42, 5)
                        .addBox(-2.0F, 0.0F, -4.0F, 4.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 17.0F, -6.0F));
        root.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(-1.0F, 18.0F, -2.0F, -2.082002F, 0.0F, 0.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(38, 16)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offset(-1.0F, 17.0F, -4.0F));
        root.addOrReplaceChild("wing1",
                CubeListBuilder.create().texOffs(36, 37)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 11.0F, 15.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, -1.0F, -0.3490659F, 0.0F, -2.356194F));
        root.addOrReplaceChild("wing2",
                CubeListBuilder.create().texOffs(0, 37)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 11.0F, 15.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, -1.0F, -0.3490659F, 0.0F, 2.356194F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(14, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offsetAndRotation(-0.5F, 17.0F, 6.0F, (float) (-Math.PI / 6), 0.0F, 0.0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(14, 8)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F),
                PartPose.offset(-0.5F, 20.0F, 11.0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(17, 16)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offsetAndRotation(-0.5F, 20.0F, 17.0F, 0.0F, -0.6320364F, 0.0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(16, 23)
                        .addBox(-1.0F, 0.5F, 4.0F, 3.0F, 0.0F, 2.0F),
                PartPose.offsetAndRotation(-0.5F, 20.0F, 17.0F, 0.0F, -0.6320364F, 0.0F));
        root.addOrReplaceChild("fl11",
                CubeListBuilder.create().texOffs(0, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 19.0F, -4.0F, 0.3490659F, 0.0F, 0.1745329F));
        root.addOrReplaceChild("fl12",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-0.5F, 1.0F, 1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 19.0F, -4.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("fl21",
                CubeListBuilder.create().texOffs(5, 9)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 19.0F, -4.0F, 0.3490659F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("fl22",
                CubeListBuilder.create().texOffs(5, 13)
                        .addBox(0.5F, 1.0F, 1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 19.0F, -4.0F, (float) (-Math.PI / 12), 0.0F, 0.0F));
        root.addOrReplaceChild("bl21",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 18.0F, 4.0F, -0.3490659F, 0.0F, -0.1745329F));
        root.addOrReplaceChild("bl22",
                CubeListBuilder.create().texOffs(0, 22)
                        .addBox(0.5F, 2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 18.0F, 4.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("bl11",
                CubeListBuilder.create().texOffs(5, 18)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 4.0F, -0.3490659F, 0.0F, 0.1745329F));
        root.addOrReplaceChild("bl12",
                CubeListBuilder.create().texOffs(5, 22)
                        .addBox(-0.5F, 2.0F, -1.0F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 18.0F, 4.0F, 0.1745329F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 119, 72);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            TerribleTerror entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.25F;
        this.wing1.zRot = -2.0F + newangle;
        this.wing2.zRot = 2.0F - newangle;

        newangle = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.jaw.xRot = Math.abs(newangle);

        newangle = Mth.cos(ageInTicks * 1.25F) * (float) Math.PI * 0.35F;
        this.fl21.xRot = 0.349F + newangle;
        this.fl22.xRot = -0.296F + newangle;
        this.bl21.xRot = -0.349F - newangle;
        this.bl22.xRot = 0.174F - newangle;
        this.fl11.xRot = 0.349F - newangle;
        this.fl12.xRot = -0.296F - newangle;
        this.bl11.xRot = -0.349F + newangle;
        this.bl12.xRot = 0.174F + newangle;

        newangle = Mth.cos(ageInTicks * 0.71F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.tail1.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.77F * this.wingspeed) * (float) Math.PI * 0.1F;
        this.tail1.yRot = newangle;

        // gold: follow tail segments along length 6
        float dist = 6.0F;
        dist = (float) (dist * Math.cos(this.tail1.xRot));
        this.tail2.y = (float) (this.tail1.y - Math.sin(this.tail1.xRot) * dist);
        this.tail2.z = (float) (this.tail1.z + Math.sin(this.tail1.yRot) * dist);

        newangle = Mth.cos(ageInTicks * 0.81F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.tail2.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.87F * this.wingspeed) * (float) Math.PI * 0.15F;
        this.tail2.yRot = newangle;

        dist = 6.0F;
        dist = (float) (dist * Math.cos(this.tail2.xRot));
        this.tail3.y = this.tail4.y = (float) (this.tail2.y - Math.sin(this.tail2.xRot) * dist);
        this.tail3.z = this.tail4.z = (float) (this.tail2.z + Math.sin(this.tail2.yRot) * dist);

        newangle = Mth.cos(ageInTicks * 0.91F * this.wingspeed) * (float) Math.PI * 0.2F;
        this.tail3.xRot = this.tail4.xRot = newangle;
        newangle = Mth.cos(ageInTicks * 0.97F * this.wingspeed) * (float) Math.PI * 0.2F;
        this.tail3.yRot = this.tail4.yRot = newangle;
    }
}
