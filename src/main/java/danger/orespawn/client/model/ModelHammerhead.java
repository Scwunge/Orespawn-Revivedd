package danger.orespawn.client.model;

import danger.orespawn.entity.Hammerhead;
import danger.orespawn.util.Reference;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelHammerhead} (1.7.10 ModelBase, tex 222×256) → 1.21 HierarchicalModel.
 * Cubes / pivots / UV 1:1. Full gold {@code func_78088_a} leg walk + head/horn attack bob + armour flutter.
 * wingspeed default 0.33 from ClientProxy registration.
 */
@OnlyIn(Dist.CLIENT)
public class ModelHammerhead extends HierarchicalModel<Hammerhead> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hammerhead"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart chest;
    private final ModelPart abdomen;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart neck_armour;
    private final ModelPart horn_base;
    private final ModelPart horn_1;
    private final ModelPart horn_2;
    private final ModelPart horn_R;
    private final ModelPart horn_L;
    private final ModelPart back_armour1;
    private final ModelPart back_armour_2;
    private final ModelPart back_armour_3;
    private final ModelPart back_armour_3R;
    private final ModelPart back_armour_4;
    private final ModelPart back_armour_4R;
    private final ModelPart tail;
    private final ModelPart leg_1R;
    private final ModelPart leg_1;
    private final ModelPart leg_2;
    private final ModelPart leg_2R;
    private final ModelPart leg_3R;
    private final ModelPart leg_3;
    private final ModelPart leg_1Rb;
    private final ModelPart leg_1b;
    private final ModelPart leg_2b;
    private final ModelPart leg_2Rb;
    private final ModelPart leg_3Rb;
    private final ModelPart leg_3b;
    private final ModelPart fan1;
    private final ModelPart Lfan2;
    private final ModelPart Rfan2;
    private final ModelPart Lfan3;
    private final ModelPart Rfan3;
    private final ModelPart Lear;
    private final ModelPart Rear;

    public ModelHammerhead(ModelPart root) {
        this(root, 0.33F);
    }

    public ModelHammerhead(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.chest = root.getChild("chest");
        this.abdomen = root.getChild("abdomen");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.snout = root.getChild("snout");
        this.neck_armour = root.getChild("neck_armour");
        this.horn_base = root.getChild("horn_base");
        this.horn_1 = root.getChild("horn_1");
        this.horn_2 = root.getChild("horn_2");
        this.horn_R = root.getChild("horn_R");
        this.horn_L = root.getChild("horn_L");
        this.back_armour1 = root.getChild("back_armour1");
        this.back_armour_2 = root.getChild("back_armour_2");
        this.back_armour_3 = root.getChild("back_armour_3");
        this.back_armour_3R = root.getChild("back_armour_3R");
        this.back_armour_4 = root.getChild("back_armour_4");
        this.back_armour_4R = root.getChild("back_armour_4R");
        this.tail = root.getChild("tail");
        this.leg_1R = root.getChild("leg_1R");
        this.leg_1 = root.getChild("leg_1");
        this.leg_2 = root.getChild("leg_2");
        this.leg_2R = root.getChild("leg_2R");
        this.leg_3R = root.getChild("leg_3R");
        this.leg_3 = root.getChild("leg_3");
        this.leg_1Rb = root.getChild("leg_1Rb");
        this.leg_1b = root.getChild("leg_1b");
        this.leg_2b = root.getChild("leg_2b");
        this.leg_2Rb = root.getChild("leg_2Rb");
        this.leg_3Rb = root.getChild("leg_3Rb");
        this.leg_3b = root.getChild("leg_3b");
        this.fan1 = root.getChild("fan1");
        this.Lfan2 = root.getChild("Lfan2");
        this.Rfan2 = root.getChild("Rfan2");
        this.Lfan3 = root.getChild("Lfan3");
        this.Rfan3 = root.getChild("Rfan3");
        this.Lear = root.getChild("Lear");
        this.Rear = root.getChild("Rear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 222×256; cubes/pivots/base rotations 1:1
        root.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -1.0F, 0.0F, 19.0F, 16.0F, 17.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.0349066F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create().texOffs(0, 34).addBox(-7.5F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 4.0F, -0.0349066F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(146, 59).addBox(-6.5F, -0.5F, -12.0F, 14.0F, 13.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(101, 59).addBox(-6.0F, -0.5F, -21.0F, 13.0F, 11.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.2094395F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "snout",
                CubeListBuilder.create().texOffs(166, 86).addBox(-4.0F, -6.0F, -27.0F, 9.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.6108652F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "neck_armour",
                CubeListBuilder.create().texOffs(73, 0).addBox(-7.0F, -1.5F, -18.0F, 15.0F, 4.0F, 18.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "horn_base",
                CubeListBuilder.create().texOffs(49, 35).addBox(-7.0F, -1.5F, -27.0F, 15.0F, 5.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "horn_1",
                CubeListBuilder.create().texOffs(122, 23).addBox(-12.0F, -4.5F, -40.0F, 25.0F, 6.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "horn_2",
                CubeListBuilder.create().texOffs(106, 44).addBox(-18.0F, -3.5F, -37.0F, 37.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "horn_R",
                CubeListBuilder.create().texOffs(158, 0).addBox(-26.0F, -5.5F, -38.5F, 8.0F, 7.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1919862F, 0.0F, -0.0174533F));
        root.addOrReplaceChild(
                "horn_L",
                CubeListBuilder.create().texOffs(158, 0).addBox(19.0F, -5.5F, -38.5F, 8.0F, 7.0F, 13.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.1919862F, 0.0F, -0.0174533F));
        root.addOrReplaceChild(
                "back_armour1",
                CubeListBuilder.create().texOffs(0, 98).addBox(-5.0F, -2.5F, -6.0F, 9.0F, 3.0F, 7.0F),
                PartPose.offsetAndRotation(1.0F, -4.0F, -15.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "back_armour_2",
                CubeListBuilder.create().texOffs(0, 65).addBox(-8.0F, -4.5F, -13.0F, 17.0F, 4.0F, 28.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, -0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "back_armour_3",
                CubeListBuilder.create().texOffs(15, 104).addBox(0.5F, -3.5F, -13.0F, 4.0F, 4.0F, 20.0F),
                PartPose.offsetAndRotation(8.0F, 1.0F, -2.0F, 0.0174533F, 0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "back_armour_3R",
                CubeListBuilder.create().texOffs(15, 104).addBox(-3.5F, -3.5F, -13.0F, 4.0F, 4.0F, 20.0F),
                PartPose.offsetAndRotation(-8.0F, 1.0F, -2.0F, 0.0174533F, -0.1570796F, 0.0F));
        root.addOrReplaceChild(
                "back_armour_4",
                CubeListBuilder.create().texOffs(0, 65).addBox(1.5F, -1.5F, -3.0F, 3.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(6.0F, 5.0F, -10.0F, -0.1396263F, 0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "back_armour_4R",
                CubeListBuilder.create().texOffs(0, 65).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 4.0F, 10.0F),
                PartPose.offsetAndRotation(-8.0F, 5.0F, -11.0F, -0.1396263F, -0.3490659F, 0.0F));
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(66, 52).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.0F, 0.5061455F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_1R",
                CubeListBuilder.create().texOffs(71, 102).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(-9.0F, 11.0F, -10.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_1",
                CubeListBuilder.create().texOffs(64, 76).addBox(-1.5F, -2.5F, -3.0F, 5.0F, 10.0F, 6.0F),
                PartPose.offsetAndRotation(9.0F, 11.0F, -10.0F, -0.0872665F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_2",
                CubeListBuilder.create().texOffs(98, 28).addBox(-1.5F, -2.5F, -3.0F, 5.0F, 9.0F, 6.0F),
                PartPose.offsetAndRotation(9.0F, 12.0F, -2.0F, -0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_2R",
                CubeListBuilder.create().texOffs(98, 80).addBox(-1.5F, -2.5F, -3.0F, 5.0F, 9.0F, 6.0F),
                PartPose.offsetAndRotation(-10.0F, 12.0F, -2.0F, -0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_3R",
                CubeListBuilder.create().texOffs(44, 129).addBox(-3.5F, -2.5F, -3.0F, 5.0F, 11.0F, 8.0F),
                PartPose.offsetAndRotation(-7.0F, 9.0F, 14.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_3",
                CubeListBuilder.create().texOffs(44, 99).addBox(-3.5F, -2.5F, -3.0F, 5.0F, 11.0F, 8.0F),
                PartPose.offsetAndRotation(10.0F, 9.0F, 14.0F, -0.3490659F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_1Rb",
                CubeListBuilder.create().texOffs(15, 129).addBox(-2.0F, 5.5F, -3.0F, 4.0F, 8.0F, 5.0F),
                PartPose.offset(-9.0F, 11.0F, -10.0F));
        root.addOrReplaceChild(
                "leg_1b",
                CubeListBuilder.create().texOffs(15, 110).addBox(-1.0F, 5.5F, -3.0F, 4.0F, 8.0F, 5.0F),
                PartPose.offset(9.0F, 11.0F, -10.0F));
        root.addOrReplaceChild(
                "leg_2b",
                CubeListBuilder.create().texOffs(57, 1).addBox(-1.0F, 5.5F, -3.0F, 4.0F, 7.0F, 5.0F),
                PartPose.offsetAndRotation(9.0F, 12.0F, -2.0F, 0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_2Rb",
                CubeListBuilder.create().texOffs(94, 106).addBox(-2.0F, 5.5F, -3.0F, 4.0F, 7.0F, 5.0F),
                PartPose.offsetAndRotation(-9.0F, 12.0F, -2.0F, 0.0523599F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_3Rb",
                CubeListBuilder.create().texOffs(122, 81).addBox(-2.0F, 6.5F, -5.0F, 4.0F, 9.0F, 5.0F),
                PartPose.offsetAndRotation(-8.0F, 9.0F, 14.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "leg_3b",
                CubeListBuilder.create().texOffs(122, 0).addBox(-3.0F, 6.5F, -5.0F, 4.0F, 9.0F, 5.0F),
                PartPose.offsetAndRotation(10.0F, 9.0F, 14.0F, 0.122173F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "fan1",
                CubeListBuilder.create().texOffs(0, 109).addBox(-1.0F, -7.0F, -34.0F, 4.0F, 15.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, -0.1396263F, 0.0F, 0.0F));
        root.addOrReplaceChild(
                "Lfan2",
                CubeListBuilder.create().texOffs(0, 109).addBox(-1.0F, -3.0F, -31.5F, 4.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -14.0F, -0.2094395F, -0.122173F, 0.0F));
        root.addOrReplaceChild(
                "Rfan2",
                CubeListBuilder.create().texOffs(0, 109).addBox(-1.0F, -3.0F, -33.5F, 4.0F, 12.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, -0.2094395F, 0.122173F, 0.0F));
        root.addOrReplaceChild(
                "Lfan3",
                CubeListBuilder.create().texOffs(0, 109).addBox(-1.0F, 4.0F, -32.0F, 4.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, -0.3316126F, -0.2268928F, 0.0F));
        root.addOrReplaceChild(
                "Rfan3",
                CubeListBuilder.create().texOffs(0, 109).addBox(-1.0F, 4.0F, -32.0F, 4.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, -0.3316126F, 0.2443461F, 0.0F));
        root.addOrReplaceChild(
                "Lear",
                CubeListBuilder.create().texOffs(0, 80).addBox(8.5F, 2.5F, -10.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.3665191F, 0.2268928F, 0.0F));
        root.addOrReplaceChild(
                "Rear",
                CubeListBuilder.create().texOffs(0, 80).addBox(-8.5F, 2.5F, -11.0F, 1.0F, 1.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, -12.0F, 0.3665191F, -0.2268928F, 0.0F));

        return LayerDefinition.create(mesh, 222, 256);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(
            Hammerhead entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        // gold func_78088_a — f1=limbSwingAmount, f2=ageInTicks, f3=netHeadYaw
        float newangle = 0.0F;
        float newangle2 = 0.0F;
        if (limbSwingAmount > 0.1F) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.1F * limbSwingAmount;
            newangle2 = Mth.cos((float) (ageInTicks * 1.3F * this.wingspeed + (Math.PI / 4)))
                    * (float) Math.PI
                    * 0.1F
                    * limbSwingAmount;
        } else {
            newangle = 0.0F;
        }

        // legs — walk cycle (field_78795_f → xRot)
        this.leg_1.xRot = -0.087F + newangle;
        this.leg_1b.xRot = newangle;
        this.leg_1R.xRot = -0.087F - newangle;
        this.leg_1Rb.xRot = -newangle;
        this.leg_2.xRot = -0.052F + newangle2;
        this.leg_2b.xRot = newangle2;
        this.leg_2R.xRot = -0.052F - newangle2;
        this.leg_2Rb.xRot = -newangle2;
        this.leg_3.xRot = -0.349F - newangle;
        this.leg_3b.xRot = -newangle;
        this.leg_3R.xRot = -0.349F + newangle;
        this.leg_3Rb.xRot = newangle;

        // neck / head / horns yaw follow head (field_78796_g → yRot)
        this.neck.yRot = (float) Math.toRadians(netHeadYaw) * 0.25F;
        this.neck_armour.yRot = this.neck.yRot;
        this.horn_base.yRot = this.neck.yRot;
        this.horn_1.yRot = this.neck.yRot;
        this.horn_2.yRot = this.neck.yRot;
        this.horn_L.yRot = this.neck.yRot;
        this.horn_R.yRot = this.neck.yRot;
        this.head.yRot = this.neck.yRot;
        this.snout.yRot = this.neck.yRot;
        this.fan1.yRot = this.neck.yRot;
        this.Lfan2.yRot = this.neck.yRot - 0.122F;
        this.Lfan3.yRot = this.neck.yRot - 0.226F;
        this.Rfan2.yRot = this.neck.yRot + 0.122F;
        this.Rfan3.yRot = this.neck.yRot + 0.226F;
        this.Lear.yRot = this.neck.yRot + 0.227F;
        this.Rear.yRot = this.neck.yRot - 0.227F;

        // side armour flutter
        newangle = Mth.cos(ageInTicks * 0.3F * this.wingspeed) * (float) Math.PI * 0.03F;
        this.back_armour_4.yRot = 0.349F + newangle;
        this.back_armour_4R.yRot = -0.349F - newangle;

        // attack head bob
        if (entity.getAttacking() != 0) {
            newangle = Mth.cos(ageInTicks * 1.3F * this.wingspeed) * (float) Math.PI * 0.13F;
        } else {
            newangle = 0.0F;
        }

        this.neck.xRot = newangle + 0.157F;
        this.neck_armour.xRot = newangle + 0.157F;
        this.horn_base.xRot = newangle + 0.087F;
        this.horn_1.xRot = newangle + 0.192F;
        this.horn_2.xRot = newangle + 0.192F;
        this.horn_L.xRot = newangle + 0.192F;
        this.horn_R.xRot = newangle + 0.192F;
        this.head.xRot = newangle + 0.209F;
        this.snout.xRot = newangle + 0.611F;
        this.fan1.xRot = newangle - 0.139F;
        this.Lfan2.xRot = newangle - 0.209F;
        this.Lfan3.xRot = newangle - 0.331F;
        this.Rfan2.xRot = newangle - 0.209F;
        this.Rfan3.xRot = newangle - 0.331F;
        this.Lear.xRot = newangle + 0.366F;
        this.Rear.xRot = newangle + 0.366F;

        // static base parts keep constructor rotations
        this.chest.xRot = 0.0349066F;
        this.abdomen.xRot = -0.0349066F;
        this.back_armour1.xRot = -0.0872665F;
        this.back_armour_2.xRot = -0.122173F;
        this.back_armour_3.xRot = 0.0174533F;
        this.back_armour_3.yRot = 0.1570796F;
        this.back_armour_3R.xRot = 0.0174533F;
        this.back_armour_3R.yRot = -0.1570796F;
        this.back_armour_4.xRot = -0.1396263F;
        this.back_armour_4R.xRot = -0.1396263F;
        this.tail.xRot = 0.5061455F;
        this.horn_R.zRot = -0.0174533F;
        this.horn_L.zRot = -0.0174533F;
    }
}
