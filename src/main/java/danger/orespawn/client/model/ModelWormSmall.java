package danger.orespawn.client.model;

import danger.orespawn.entity.WormSmall;
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
 * Port of gold {@code ModelWormSmall} (1.12 ModelBase) → 1.21 HierarchicalModel.
 */
@OnlyIn(Dist.CLIENT)
public class ModelWormSmall extends HierarchicalModel<WormSmall> {
    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;

    public ModelWormSmall(ModelPart root) {
        this(root, 1F);
    }

    public ModelWormSmall(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(6, 0)
                        .addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(WormSmall entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    
    private static final float TAIL_X = 0.0F, TAIL_Y = 24.0F, TAIL_Z = 0.0F;

    private void animate(WormSmall entity, float f, float f1, float f2, float f3, float f4) {
        this.tail.x = TAIL_X;
        this.tail.y = TAIL_Y;
        this.tail.z = TAIL_Z;
        float newangle = Mth.cos(f2 * 0.55F) * (float) Math.PI * 0.15F;
        this.tail.xRot = newangle;
        float d1 = (float) (Math.sin(newangle) * 5.0);
        float d2 = (float) (Math.cos(newangle) * 5.0);
        this.body.z = this.tail.z - d1;
        newangle = Mth.cos(f2 * 0.35F) * (float) Math.PI * 0.1F;
        this.tail.zRot = newangle;
        float d3 = (float) (Math.cos(newangle) * d2);
        float d4 = (float) (Math.sin(newangle) * d2);
        this.body.x = this.tail.x + d4;
        this.body.y = (float) (this.tail.y - 5.0 + (5.0 - d3));
        newangle = Mth.cos(f2 * 0.45F) * (float) Math.PI * 0.15F;
        this.body.xRot = newangle;
        d1 = (float) (Math.sin(newangle) * 5.0);
        d2 = (float) (Math.cos(newangle) * 5.0);
        this.head.z = this.body.z - d1;
        newangle = Mth.cos(f2 * 0.25F) * (float) Math.PI * 0.1F;
        this.body.zRot = newangle;
        d3 = (float) (Math.cos(newangle) * d2);
        d4 = (float) (Math.sin(newangle) * d2);
        this.head.x = this.body.x + d4;
        this.head.y = (float) (this.body.y - 5.0 + (5.0 - d3));
        this.head.xRot = 0.62F + Mth.cos(f2 * 0.65F) * (float) Math.PI * 0.15F;
        this.head.zRot = Mth.cos(f2 * 0.3F) * (float) Math.PI * 0.05F;
    }
}

