package danger.orespawn.client.model;

import danger.orespawn.entity.WormMedium;
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
 * Port of gold {@code ModelWormMedium} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWormMedium extends HierarchicalModel<WormMedium> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart tooth1;
    private final ModelPart tooth2;
    private final ModelPart tooth3;
    private final ModelPart tooth4;
    private final ModelPart head2;

    public ModelWormMedium(ModelPart root) {
        this(root, 1F);
    }

    public ModelWormMedium(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.tooth1 = root.getChild("tooth1");
        this.tooth2 = root.getChild("tooth2");
        this.tooth3 = root.getChild("tooth3");
        this.tooth4 = root.getChild("tooth4");
        this.head2 = root.getChild("head2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(24, 0)
                        .addBox(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(37, 0)
                        .addBox(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(0.0F, 13.0F, 0.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(50, 0)
                        .addBox(-1.5F, -12.0F, -1.5F, 3.0F, 12.0F, 3.0F),
                PartPose.offset(0.0F, 25.0F, 0.0F));
        root.addOrReplaceChild("tooth1",
                CubeListBuilder.create().texOffs(15, 0)
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(1.0F, -11.0F, 0.0F));
        root.addOrReplaceChild("tooth2",
                CubeListBuilder.create().texOffs(5, 0)
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(-1.0F, -11.0F, 0.0F));
        root.addOrReplaceChild("tooth3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -11.0F, 1.0F));
        root.addOrReplaceChild("tooth4",
                CubeListBuilder.create().texOffs(10, 0)
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -11.0F, -1.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(WormMedium entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private void animate(WormMedium entity, float f, float f1, float f2, float f3, float f4) {
        // reset base pivots each frame (gold mutates rotation points)
        this.tail.x = 0.0F; this.tail.y = 25.0F; this.tail.z = 0.0F;
        this.body.x = 0.0F; this.body.y = 13.0F; this.body.z = 0.0F;
        this.head.x = 0.0F; this.head.y = 1.0F; this.head.z = 0.0F;
        this.head2.x = 0.0F; this.head2.y = 0.0F; this.head2.z = 0.0F;

        float newangle = Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.1F;
        this.tail.xRot = newangle;
        float d1 = (float) (Math.sin(newangle) * 12.0);
        float d2 = (float) (Math.cos(newangle) * 12.0);
        this.body.z = this.tail.z - d1;
        newangle = Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.08F;
        this.tail.zRot = newangle;
        float d3 = (float) (Math.cos(newangle) * d2);
        float d4 = (float) (Math.sin(newangle) * d2);
        this.body.x = this.tail.x + d4;
        this.body.y = (float) (this.tail.y - 12.0 + (12.0 - d3));
        newangle = Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.1F;
        this.body.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 12.0);
        d2 = (float) (Math.cos(newangle) * 12.0);
        this.head.z = this.body.z - d1;
        this.head2.z = this.head.z;
        newangle = Mth.cos(f2 * 0.15F) * (float) Math.PI * 0.07F;
        this.body.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.head.x = this.body.x + d4;
        this.head2.x = this.head.x;
        this.head.y = (float) (this.body.y - 12.0 + (12.0 - d3));
        this.head2.y = this.head.y;
        this.head.xRot = this.head2.xRot = 0.62F + Mth.cos(f2 * 0.55F) * (float) Math.PI * 0.15F;
        this.head.zRot = this.head2.zRot = Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.05F;
        newangle = this.head.xRot;
        this.tooth1.xRot = this.tooth2.xRot = this.tooth3.xRot = this.tooth4.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 12.0);
        d2 = (float) (Math.cos(newangle) * 12.0);
        this.tooth1.z = this.tooth2.z = this.tooth3.z = this.tooth4.z = this.head.z - d1;
        newangle = this.head.zRot;
        this.tooth1.zRot = this.tooth2.zRot = this.tooth3.zRot = this.tooth4.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.tooth1.x = this.tooth2.x = this.tooth3.x = this.tooth4.x = this.head.x + d4;
        this.tooth1.y = this.tooth2.y = this.tooth3.y = this.tooth4.y =
                (float) (this.head.y - 12.0 + (12.0 - d3));
        this.tooth1.z++;
        this.tooth2.z--;
        float bite = Mth.cos(f2 * 0.55F) * (float) Math.PI * 0.15F;
        this.tooth1.xRot = this.tooth1.xRot - 0.4F - bite;
        this.tooth2.xRot = this.tooth2.xRot + 0.4F + bite;
        this.tooth3.x++;
        this.tooth4.x--;
        this.tooth3.zRot = this.tooth3.zRot + 0.4F + bite;
        this.tooth4.zRot = this.tooth4.zRot - 0.4F - bite;
    }
}

