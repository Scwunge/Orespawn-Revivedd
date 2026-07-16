package danger.orespawn.client.model;

import danger.orespawn.entity.Robot5;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Port of gold {@code ModelRobot5} (1.7.10 ModelBase, tex 128×128) → 1.21 HierarchicalModel.
 * Cubes/UVs/offsets 1:1. ClientProxy wingspeed {@code 1.0F}.
 * Full gold anim: wheel spin while moving; barrel/ammobox yRot from head yaw.
 * Local {@link #LAYER_LOCATION} (no shared ModModelLayers dependency).
 */
@OnlyIn(Dist.CLIENT)
public class ModelRobot5 extends HierarchicalModel<Robot5> {
    /** Model layer location. */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "robot5"), "main");

    private final float wingspeed;
    private final ModelPart root;
    private final ModelPart lwheel1;
    private final ModelPart lwheel2;
    private final ModelPart rwheel1;
    private final ModelPart rwheel2;
    private final ModelPart axle;
    private final ModelPart drivebox;
    private final ModelPart stand;
    private final ModelPart swivel;
    private final ModelPart barrel1;
    private final ModelPart barrel2;
    private final ModelPart ammobox;

    public ModelRobot5(ModelPart root) {
        this(root, 1.0F);
    }

    public ModelRobot5(ModelPart root, float wingspeed) {
        this.root = root;
        this.wingspeed = wingspeed;
        this.lwheel1 = root.getChild("lwheel1");
        this.lwheel2 = root.getChild("lwheel2");
        this.rwheel1 = root.getChild("rwheel1");
        this.rwheel2 = root.getChild("rwheel2");
        this.axle = root.getChild("axle");
        this.drivebox = root.getChild("drivebox");
        this.stand = root.getChild("stand");
        this.swivel = root.getChild("swivel");
        this.barrel1 = root.getChild("barrel1");
        this.barrel2 = root.getChild("barrel2");
        this.ammobox = root.getChild("ammobox");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // gold tex 128×128 — cubes/UVs/offsets 1:1
        root.addOrReplaceChild("lwheel1",
                CubeListBuilder.create().texOffs(0, 23)
                        .addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.offset(6.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("lwheel2",
                CubeListBuilder.create().texOffs(0, 43)
                        .addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(6.0F, 19.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild("rwheel1",
                CubeListBuilder.create().texOffs(0, 23)
                        .addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.offset(-8.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("rwheel2",
                CubeListBuilder.create().texOffs(0, 43)
                        .addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(-8.0F, 19.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
        root.addOrReplaceChild("axle",
                CubeListBuilder.create().texOffs(42, 0)
                        .addBox(-6.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("drivebox",
                CubeListBuilder.create().texOffs(47, 4)
                        .addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("stand",
                CubeListBuilder.create().texOffs(35, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 18.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("swivel",
                CubeListBuilder.create().texOffs(22, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("barrel1",
                CubeListBuilder.create().texOffs(24, 25)
                        .addBox(-1.0F, -2.0F, -10.0F, 2.0F, 2.0F, 13.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("barrel2",
                CubeListBuilder.create().texOffs(27, 43)
                        .addBox(-0.5F, -1.5F, -19.0F, 1.0F, 1.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("ammobox",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, 3.0F, 4.0F, 3.0F, 5.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Full gold {@code func_78088_a} anim:
     * <ul>
     *   <li>Moving (limbSwingAmount &gt; 0.1): wheels spin via ageInTicks * 0.15 mod 2π (abs)</li>
     *   <li>Idle: wheel xRot = 0 (lwheel2/rwheel2 keep +π/4 offset)</li>
     *   <li>barrel1/barrel2/ammobox yRot = toRadians(netHeadYaw / 2)</li>
     * </ul>
     * {@code wingspeed} retained for ClientProxy parity (gold ModelRobot5(1.0F); unused in formulas).
     */
    @Override
    public void setupAnim(
            Robot5 entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        float f1 = limbSwingAmount;
        float f2 = ageInTicks;
        float f3 = netHeadYaw;
        float newangle;
        if (f1 > 0.1F) {
            newangle = f2 * 0.15F % ((float) Math.PI * 2.0F);
            newangle = Math.abs(newangle);
        } else {
            newangle = 0.0F;
        }

        this.lwheel1.xRot = newangle;
        this.lwheel2.xRot = newangle + (float) (Math.PI / 4);
        this.rwheel1.xRot = newangle;
        this.rwheel2.xRot = newangle + (float) (Math.PI / 4);

        float turretYaw = (float) Math.toRadians(f3 / 2.0F);
        this.barrel1.yRot = turretYaw;
        this.barrel2.yRot = turretYaw;
        this.ammobox.yRot = turretYaw;
        // gold stores wingspeed from ClientProxy (1.0F) but does not scale formulas with it
    }
}
