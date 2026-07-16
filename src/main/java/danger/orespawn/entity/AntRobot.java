package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code AntRobot} (EntityLiving) 1:1 best-effort for NeoForge 1.21.1.
 * Size 2.75×1.25, speed 0.3, health 300, attack 30, armor 16, XP 150.
 * Unowned: hostile gold-style combat + feet stomp. Owned: rideable, iron-ingot heal.
 * Full client leg IK ({@link SpiderLegInfo}) drives {@code ModelAntRobot} setupAnim.
 * Texture: {@code textures/entity/antrobottexture.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class AntRobot extends PathfinderMob {
    private static final EntityDataAccessor<Integer> ATTACKING =
            SynchedEntityData.defineId(AntRobot.class, EntityDataSerializers.INT);

    /** Gold AntRobot_stats defaults: health 300, attack 30, defense 16. */
    public static final float GOLD_WIDTH = 2.75F;
    public static final float GOLD_HEIGHT = 1.25F;
    public static final double GOLD_HEALTH = 300.0;
    public static final double GOLD_SPEED = 0.3;
    public static final double GOLD_ATTACK = 30.0;
    public static final double GOLD_ARMOR = 16.0;
    public static final int GOLD_XP = 150; // health / 2

    private final float moveSpeed = 0.3F;
    private SpiderLegInfo renderdata = new SpiderLegInfo();
    private int didonce = 0;
    private int rideTicker = 0;
    private int owned = 0;
    private int playing = 0;

    public AntRobot(EntityType<? extends AntRobot> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.renderdata = new SpiderLegInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.STEP_HEIGHT, 1.5);
    }

    @Override
    protected void registerGoals() {
        // gold: WatchClosest(Player,12), LookIdle — combat in customServerAiStep
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, 0);
        this.initLegData();
    }

    public void setOwned() {
        this.owned = 1;
    }

    public int getOwned() {
        return this.owned;
    }

    public boolean isOwned() {
        return this.owned != 0;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, value);
    }

    public SpiderLegInfo getRenderSpiderRobotInfo() {
        if (this.renderdata == null) {
            this.renderdata = new SpiderLegInfo();
        }
        return this.renderdata;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // gold canDespawn false
    }

    @Override
    public boolean isPushable() {
        return false; // gold canBePushed false
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isRemoved();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold: no fall / no walk damage
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    /** Gold mountedYOffset: 0.55 + cos(rideTicker * 0.19) * 0.02 */
    public double getPassengersRidingOffset() {
        return 0.55 + Math.cos(this.rideTicker * 0.19F) * 0.02;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && this.isOwned();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity p = this.getFirstPassenger();
        return p instanceof LivingEntity living ? living : null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        // gold updateRiderPosition: f=-1.25 + cos(rideTicker*0.33)*0.05 along yaw
        float f = -1.25F + (float) (Math.cos(this.rideTicker * 0.33F) * 0.05);
        double yaw = Math.toRadians(this.getYRot());
        return new Vec3(-f * Math.sin(yaw), this.getPassengersRidingOffset(), f * Math.cos(yaw));
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.getControllingPassenger() instanceof Player || super.isControlledByLocalInstance();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AntRobotOwned", this.owned);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.owned = tag.getInt("AntRobotOwned");
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        this.clearFire(); // gold setFire(0)
        super.tick();

        // gold onUpdate: feet + melee while ridden
        if (this.level().getDifficulty() != Difficulty.PEACEFUL
                && !this.level().isClientSide
                && this.isVehicle()
                && this.random.nextInt(50) == 0) {
            this.feetFindSomethingToHit();
        }
        if (this.level().getDifficulty() != Difficulty.PEACEFUL
                && !this.level().isClientSide
                && this.isVehicle()
                && this.random.nextInt(9) == 0) {
            LivingEntity e = this.findSomethingToAttack(1.0F, true);
            if (e != null) {
                if (GoldStyleCombat.inMeleeRange(this, e, 6.0)) {
                    this.setAttacking(1);
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
            }
        }

        this.spawnJetParticles();
    }

    /** Gold onLivingUpdate: hover, ride physics, leg IK (client). */
    @Override
    public void aiStep() {
        if (!this.isVehicle()) {
            super.aiStep();
        }

        Vec3 dm = this.getDeltaMovement();
        double mx = Mth.clamp(dm.x, -1.25, 1.25);
        double my = Mth.clamp(dm.y, -0.85, 0.85);
        double mz = Mth.clamp(dm.z, -1.25, 1.25);
        this.setDeltaMovement(mx, my, mz);

        this.rideTicker = this.rideTicker + this.random.nextInt(3);
        if (this.playing > 0) {
            this.playing--;
        }
        if (this.isVehicle() && this.playing == 0 && this.random.nextInt(80) == 1) {
            // gold orespawn:robotspider; fallback if missing
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.IRON_GOLEM_STEP, SoundSource.NEUTRAL, 0.35F, 1.0F);
            this.playing = 125;
        }

        double gh = this.isVehicle() ? 2.25 : 1.75;
        this.applyHover(gh);

        if (this.level().isClientSide) {
            this.updateLegs();
        } else if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            this.driveFromRider(player);
        } else if (!this.isVehicle()) {
            // gold unowned: damp after move
            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(v.x * 0.8, v.y * 0.98, v.z * 0.8);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (this.isVehicle() && this.getFirstPassenger() != null && !this.getFirstPassenger().isAlive()) {
            this.ejectPassengers();
        }
    }

    private void applyHover(double gh) {
        BlockPos below = BlockPos.containing(this.getX(), this.getY() - gh + (this.isVehicle() ? 0.0 : 1.0), this.getZ());
        BlockState bid = this.level().getBlockState(below);
        if (bid.isAir() && !this.isVehicle()) {
            bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
        }
        if (this.isSolidSurface(bid)) {
            double boost = this.level().isClientSide ? 0.12 : (this.isVehicle() ? 0.06 : 0.15);
            double yBoost = this.level().isClientSide ? 0.12 : (this.isVehicle() ? 0.03 : 0.15);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, boost, 0.0));
            this.setPos(this.getX(), this.getY() + yBoost, this.getZ());
        } else {
            double sink = this.isVehicle() && !this.level().isClientSide ? 0.02 : 0.002;
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -sink, 0.0));
        }
    }

    private boolean isSolidSurface(BlockState state) {
        if (state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA)) {
            return false;
        }
        return state.blocksMotion();
    }

    /** Gold rider livingUpdate steering (best-effort). */
    private void driveFromRider(Player player) {
        double obstruction = 0.0;
        double velocity = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x
                + this.getDeltaMovement().z * this.getDeltaMovement().z);
        int span = 3 + (int) (velocity * 6.0);
        for (int k = 1; k < span; k++) {
            for (int i = 1; i < span * 2; i++) {
                for (int j = -90; j <= 90; j += 30) {
                    double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F + j));
                    double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F + j));
                    BlockState st = this.level().getBlockState(
                            BlockPos.containing(this.getX() + dx, this.getY() - k, this.getZ() + dz));
                    if (this.isSolidSurface(st)) {
                        obstruction += 0.02;
                    }
                }
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, obstruction * 0.05, 0.0));
        this.setPos(this.getX(), this.getY() + obstruction * 0.05, this.getZ());

        double d4 = player.getYRot() % 360.0;
        while (d4 < 0.0) {
            d4 += 360.0;
        }
        double d5 = this.getYRot() % 360.0;
        while (d5 < 0.0) {
            d5 += 360.0;
        }
        double relativeG = (d4 - d5) % 180.0;
        while (relativeG < 0.0) {
            relativeG += 180.0;
        }
        if (relativeG > 90.0) {
            relativeG -= 180.0;
        }
        if (velocity > 0.01) {
            double soft = Math.abs(1.85 - velocity);
            soft = Mth.clamp(soft, 0.01, 0.9);
            this.setYRot(player.getYRot() + (float) (relativeG * soft));
        } else {
            this.setYRot(player.getYRot());
        }
        this.setXRot(0.0F);
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        float im = player.zza;
        double maxSpeed = 0.3;
        double newVel = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x
                + this.getDeltaMovement().z * this.getDeltaMovement().z);
        double rhm = Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x);
        double rhdir = Math.toRadians((player.getYRot() + 90.0F) % 360.0F);
        double rdv = Math.abs(rhm - rhdir) % (Math.PI * 2.0);
        if (rdv > Math.PI) {
            rdv -= Math.PI * 2.0;
        }
        rdv = Math.abs(rdv);
        if (Math.abs(newVel) < 0.01) {
            rdv = 0.0;
        }
        if (rdv > 1.5) {
            newVel = -newVel;
        }

        if (Math.abs(im) > 0.001F) {
            double deltav = im > 0.0F ? 0.05 : -0.05;
            if (im <= 0.0F) {
                maxSpeed = 0.25;
            }
            newVel += deltav;
            if (newVel >= 0.0) {
                newVel = Math.min(newVel, maxSpeed);
                this.setDeltaMovement(
                        Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVel,
                        this.getDeltaMovement().y,
                        Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVel);
            } else {
                newVel = Math.min(-newVel, maxSpeed);
                this.setDeltaMovement(
                        Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * newVel,
                        this.getDeltaMovement().y,
                        Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * newVel);
            }
        } else if (newVel >= 0.0) {
            this.setDeltaMovement(
                    Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVel,
                    this.getDeltaMovement().y,
                    Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVel);
        } else {
            this.setDeltaMovement(
                    Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * (-newVel),
                    this.getDeltaMovement().y,
                    Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * (-newVel));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 v = this.getDeltaMovement();
        this.setDeltaMovement(v.x * 0.98, v.y * 0.98, v.z * 0.98);
    }

    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        // gold updateAITasks: only when no rider
        if (this.isVehicle()) {
            return;
        }
        super.customServerAiStep();
        if (this.owned != 0 || this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }

        if (this.random.nextInt(20) == 0) {
            this.feetFindSomethingToHit();
        }
        if (this.random.nextInt(150) == 0) {
            this.setTarget(null);
        }

        LivingEntity e = this.getTarget();
        if (e != null && !e.isAlive()) {
            this.setTarget(null);
            e = null;
        }
        if (e == null) {
            e = this.findSomethingToAttack(2.0F, false);
            if (e != null) {
                this.setTarget(e);
            }
        }
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            if (this.distanceToSqr(e) > 16.0) {
                double d1 = e.getZ() - this.getZ();
                double d2 = e.getX() - this.getX();
                double dd = Math.atan2(d1, d2);
                this.goThisWay(0.2 * Math.cos(dd), 0.2 * Math.sin(dd));
            }
        } else {
            this.setAttacking(0);
        }

        if (e != null && this.random.nextInt(15) == 0) {
            LivingEntity target = this.getTarget();
            if (target == null) {
                target = this.findSomethingToAttack(2.0F, true);
            }
            if (target != null) {
                if (GoldStyleCombat.inMeleeRange(this, target, 6.0)) {
                    this.setAttacking(1);
                    this.doHurtTarget(target);
                } else {
                    this.setAttacking(0);
                }
            } else {
                this.setAttacking(0);
            }
        }
    }

    public void goThisWay(double mx, double mz) {
        this.setDeltaMovement(mx, this.getDeltaMovement().y, mz);
    }

    @Override
    public boolean isNoAi() {
        // gold isAIEnabled when no rider — keep AI goals when free
        return super.isNoAi();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isOwned()) {
            return InteractionResult.SUCCESS; // gold returns true (consumes interact, no mount)
        }
        // gold: iron_ingot heals up to 100 missing HP within dist sq 25
        if (!stack.isEmpty() && stack.is(Items.IRON_INGOT) && this.distanceToSqr(player) < 25.0) {
            if (!this.level().isClientSide) {
                float missing = this.getMaxHealth() - this.getHealth();
                if (missing > 100.0F) {
                    missing = 100.0F;
                }
                if (missing > 0.0F) {
                    this.heal(missing);
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (this.isVehicle() && this.getFirstPassenger() instanceof Player other && other != player) {
            return InteractionResult.SUCCESS;
        }
        if (!this.level().isClientSide && !this.isVehicle() && this.distanceToSqr(player) < 16.0) {
            player.startRiding(this);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.45F, 1.0F);
            // gold orespawn:robotspidermount 
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        String id = source.getMsgId();
        if ("inWall".equals(id) || "cactus".equals(id) || "inFire".equals(id)
                || "onFire".equals(id) || "magic".equals(id) || "starve".equals(id)) {
            return false;
        }
        if (source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
            return false;
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.getLookControl().setLookAt(e, 20.0F, 20.0F);
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold attackEntityAsMob: full attack dmg + knock
        if (!(target instanceof LivingEntity living)) {
            return false;
        }
        float dmg = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean ret = target.hurt(this.damageSources().mobAttack(this), dmg);
        if (ret) {
            double ks = 0.7;
            double inair = 0.1;
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return ret;
    }

    private void feetFindSomethingToHit() {
        if (OreSpawnMain.PlayNicely != 0) {
            return;
        }
        AABB box = this.getBoundingBox().inflate(10.0, 8.0, 10.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity living : list) {
            if (this.feetIsSuitableTarget(living)) {
                this.feetAttackEntityAsMob(living);
            }
        }
    }

    private boolean feetIsSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (target instanceof AntRobot) {
            return false;
        }
        if (target == this.getFirstPassenger()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        double d1 = target.getX() - this.getX();
        double d2 = target.getY() - this.getY();
        double d3 = target.getZ() - this.getZ();
        double dd = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
        if (dd > 9.0 || dd < 6.0) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        return true;
    }

    public boolean feetAttackEntityAsMob(Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        }
        float dmg = (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 10.0);
        boolean ret = target.hurt(this.damageSources().mobAttack(this), dmg);
        if (ret) {
            double ks = 0.6;
            double inair = 0.1;
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return ret;
    }

    @Nullable
    private LivingEntity findSomethingToAttack(float distmul, boolean dircheck) {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        double xz = 12.0 * distmul;
        AABB box = this.getBoundingBox().inflate(xz, 12.0, xz);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living, dircheck)) {
                return living;
            }
        }
        return null;
    }

    private boolean isSuitableTarget(LivingEntity target, boolean dircheck) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (target instanceof AntRobot) {
            return false;
        }
        if (target == this.getFirstPassenger()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (dircheck) {
            double rr = Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
            double rdd = Math.abs(rr - rhdir) % (Math.PI * 2.0);
            if (rdd > Math.PI) {
                rdd -= Math.PI * 2.0;
            }
            rdd = Math.abs(rdd);
            if (this.distanceToSqr(target) < 36.0) {
                // fall through to player/creative check
            } else if (rdd > 0.75) {
                return false;
            }
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        return true;
    }

    private void spawnJetParticles() {
        float f = 4.0F;
        float dx = (float) (f * Math.cos(Math.toRadians(this.getYRot() - 80.0F)));
        float dz = (float) (f * Math.sin(Math.toRadians(this.getYRot() - 80.0F)));
        float dx2 = (float) (f * Math.cos(Math.toRadians(this.getYRot() - 90.0F)));
        float dz2 = (float) (f * Math.sin(Math.toRadians(this.getYRot() - 90.0F)));
        this.spawnJetBurst(dx, dz, dx2, dz2, f);
        dx = (float) (f * Math.cos(Math.toRadians(this.getYRot() - 100.0F)));
        dz = (float) (f * Math.sin(Math.toRadians(this.getYRot() - 100.0F)));
        this.spawnJetBurst(dx, dz, dx2, dz2, f);
    }

    private void spawnJetBurst(float dx, float dz, float dx2, float dz2, float f) {
        Level level = this.level();
        double px = this.getX() + dx;
        double py = this.getY() + 0.5;
        double pz = this.getZ() + dz;
        if (this.random.nextInt(18) == 0) {
            level.addParticle(ParticleTypes.FLAME, px, py, pz,
                    dx2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) / 10.0F,
                    dz2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F);
        }
        if (this.random.nextInt(7) == 0) {
            level.addParticle(ParticleTypes.SMOKE, px, py, pz,
                    dx2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) / 10.0F,
                    dz2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F);
        }
        if (this.random.nextInt(16) == 0) {
            level.addParticle(ParticleTypes.FIREWORK, px, py, pz,
                    dx2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) / 5.0F,
                    dz2 / f + (this.random.nextFloat() - this.random.nextFloat()) / 20.0F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 7+rand7 random tech scrap (nextInt 12)
        int i = 7 + this.random.nextInt(7);
        for (int n = 0; n < i; n++) {
            switch (this.random.nextInt(12)) {
                case 0 -> this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                case 1 -> this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT));
                case 2 -> this.spawnAtLocation(new ItemStack(Items.NETHER_STAR));
                case 3, 8 -> this.spawnAtLocation(new ItemStack(Blocks.REDSTONE_BLOCK));
                case 4 -> this.spawnAtLocation(new ItemStack(Blocks.DISPENSER));
                case 5 -> this.spawnAtLocation(new ItemStack(Blocks.FURNACE));
                case 6 -> this.spawnAtLocation(new ItemStack(Blocks.PISTON));
                case 7 -> this.spawnAtLocation(new ItemStack(Blocks.STICKY_PISTON));
                case 9 -> this.spawnAtLocation(new ItemStack(Blocks.TNT));
                case 10 -> this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
                default -> {
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null; // gold uses robotspider while ridden only
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    // --- leg IK (gold updateLegs / initLegData / findNewFooting) ---

    private void initLegData() {
        if (this.renderdata == null) {
            this.renderdata = new SpiderLegInfo();
        }
        for (int i = 0; i < 6; i++) {
            this.renderdata.ycurrentangle[i] = 0.0F;
            this.renderdata.ywantedangle[i] = 0.0F;
            this.renderdata.ydisplayangle[i] = 0.0F;
            this.renderdata.yvelocity[i] = 0.0F;
            this.renderdata.ymid[i] = 0.0F;
            this.renderdata.yoff[i] = 0.0F;
            this.renderdata.yrange[i] = 0.0F;
            this.renderdata.udcurrentangle[i] = 0.0F;
            this.renderdata.udwantedangle[i] = 0.0F;
            this.renderdata.uddisplayangle[i] = 0.0F;
            this.renderdata.udvelocity[i] = 0.0F;
            this.renderdata.p1xangle[i] = Math.PI / 4;
            this.renderdata.p2xangle[i] = 0.0;
            this.renderdata.p3xangle[i] = -Math.PI / 4;
            this.renderdata.pxvelocity[i] = 0.0F;
            this.renderdata.foot_xpos[i] = (float) this.getX();
            this.renderdata.foot_ypos[i] = (float) this.getY();
            this.renderdata.foot_zpos[i] = (float) this.getZ();
            this.renderdata.realposx[i] = 0.0F;
            this.renderdata.realposy[i] = 0.0F;
            this.renderdata.realposz[i] = 0.0F;
            this.renderdata.legoff[i] = 0.0F;
            this.renderdata.footup[i] = 1;
            this.renderdata.uppoint[i] = 0.0F;
            this.renderdata.footingticker[i] = 0;
            this.renderdata.gpcounter = 0;
            if (i == 0) {
                this.renderdata.legoff[i] = 0.75F;
                this.renderdata.ymid[i] = 0.0F;
                this.renderdata.yrange[i] = (float) (Math.PI / 12);
                this.renderdata.pairedwith[i] = 1;
                this.renderdata.yoff[i] = -0.75F;
            } else if (i == 1) {
                this.renderdata.legoff[i] = 0.75F;
                this.renderdata.ymid[i] = (float) Math.PI;
                this.renderdata.yrange[i] = (float) (-Math.PI / 12);
                this.renderdata.pairedwith[i] = 0;
                this.renderdata.yoff[i] = -0.75F;
            } else if (i == 2) {
                this.renderdata.legoff[i] = 1.0F;
                this.renderdata.ymid[i] = (float) (-Math.PI / 4);
                this.renderdata.yrange[i] = (float) (Math.PI / 12);
                this.renderdata.pairedwith[i] = 3;
                this.renderdata.yoff[i] = -0.75F;
            } else if (i == 3) {
                this.renderdata.legoff[i] = 1.0F;
                this.renderdata.ymid[i] = (float) (Math.PI * 5.0 / 4.0);
                this.renderdata.yrange[i] = (float) (-Math.PI / 12);
                this.renderdata.pairedwith[i] = 2;
                this.renderdata.yoff[i] = -0.75F;
            } else if (i == 4) {
                this.renderdata.legoff[i] = 1.15F;
                this.renderdata.ymid[i] = (float) (Math.PI / 4);
                this.renderdata.yrange[i] = (float) (Math.PI / 12);
                this.renderdata.pairedwith[i] = 5;
                this.renderdata.yoff[i] = -0.75F;
            } else if (i == 5) {
                this.renderdata.legoff[i] = 1.15F;
                this.renderdata.ymid[i] = (float) (Math.PI * 3.0 / 4.0);
                this.renderdata.yrange[i] = (float) (-Math.PI / 12);
                this.renderdata.pairedwith[i] = 4;
                this.renderdata.yoff[i] = -0.75F;
            }
        }
    }

    private float getNewVelocity(float v, float diff, float curval) {
        float tv = v * 18.0F;
        if (tv < 2.0F) {
            tv = 2.0F;
        }
        if (tv > 8.0F) {
            tv = 8.0F;
        }
        if (diff > 0.0F) {
            if (diff < 0.008726646259971648 * tv) {
                curval = 0.0F;
            } else {
                curval = (float) (curval + 0.004363323129985824 * tv);
                if (diff < 0.06981317007977318 * tv) {
                    curval = (float) ((Math.PI / 180.0) * tv);
                }
                if (diff < 0.03490658503988659 * tv) {
                    curval = (float) (0.008726646259971648 * tv);
                }
                if (curval > 0.06981317007977318 * tv) {
                    curval = (float) (0.06981317007977318 * tv);
                }
            }
        } else if (diff > -0.008726646259971648 * tv) {
            curval = 0.0F;
        } else {
            curval = (float) (curval - 0.004363323129985824 * tv);
            if (diff > -0.06981317007977318 * tv) {
                curval = -((float) ((Math.PI / 180.0) * tv));
            }
            if (diff > -0.03490658503988659 * tv) {
                curval = -((float) (0.008726646259971648 * tv));
            }
            if (curval < -0.06981317007977318 * tv) {
                curval = -((float) (0.06981317007977318 * tv));
            }
        }
        return curval;
    }

    public void updateLegs() {
        if (!this.level().isClientSide) {
            return;
        }
        float yaw = this.getYRot() % 360.0F;
        while (yaw < 0.0F) {
            yaw += 360.0F;
        }
        // keep entity yaw normalized for IK
        this.setYRot(yaw);

        this.renderdata.gpcounter++;
        if (this.didonce == 0) {
            this.didonce = 1;
            this.initLegData();
        }

        float d1 = (float) (this.xo - this.getX());
        float d2 = (float) (this.yo - this.getY());
        float d3 = (float) (this.zo - this.getZ());
        float realv = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);

        for (int var22 = 0; var22 < 6; var22++) {
            int fcount = 0;
            this.renderdata.footingticker[var22]++;
            this.renderdata.realposx[var22] = (float) (
                    this.getX()
                            - this.renderdata.legoff[var22]
                            * Math.sin(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                            + this.renderdata.ymid[var22]));
            this.renderdata.realposz[var22] = (float) (
                    this.getZ()
                            + this.renderdata.legoff[var22]
                            * Math.cos(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                            + this.renderdata.ymid[var22]));
            this.renderdata.realposy[var22] = (float) this.getY() + this.renderdata.yoff[var22];
            int it = this.renderdata.footingticker[var22]
                    + this.renderdata.footingticker[this.renderdata.pairedwith[var22]];
            if (it > 50
                    && this.renderdata.footingticker[var22]
                    > this.renderdata.footingticker[this.renderdata.pairedwith[var22]]) {
                this.renderdata.footingticker[var22] = 0;
            }

            d1 = this.renderdata.realposx[var22] - this.renderdata.foot_xpos[var22];
            d2 = this.renderdata.realposy[var22] - this.renderdata.foot_ypos[var22];
            d3 = this.renderdata.realposz[var22] - this.renderdata.foot_zpos[var22];
            float dd = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            dd *= 16.0F;
            float da = (float) (
                    Math.abs(this.renderdata.ycurrentangle[var22]
                            - (Math.toRadians(Mth.wrapDegrees(this.getYRot())) + this.renderdata.ymid[var22]))
                            % (Math.PI * 2));
            if (da > Math.PI) {
                da = (float) (da - (Math.PI * 2));
            }
            if (da < -Math.PI) {
                da = (float) (da + (Math.PI * 2));
            }
            da = Math.abs(da);
            if (dd > 144.0F
                    || dd < 22.0F
                    || da > Math.abs(this.renderdata.yrange[var22]) * 8.0F / 6.0F
                    || Math.abs(this.renderdata.udcurrentangle[var22]) > 1.25
                    || this.renderdata.footingticker[var22] == 0) {
                this.findNewFooting(var22);
                d1 = this.renderdata.realposx[var22] - this.renderdata.foot_xpos[var22];
                d2 = this.renderdata.realposy[var22] - this.renderdata.foot_ypos[var22];
                d3 = this.renderdata.realposz[var22] - this.renderdata.foot_zpos[var22];
                dd = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                dd *= 16.0F;
            }

            float c1 = (float) (49.0 * Math.cos(this.renderdata.p2xangle[var22] - this.renderdata.p1xangle[var22]));
            float c2 = 49.0F;
            float c3 = (float) (49.0 * Math.cos(this.renderdata.p2xangle[var22] - this.renderdata.p3xangle[var22]));
            float cc = c1 + c2 + c3;
            float diff = cc - dd;
            this.renderdata.pxvelocity[var22] =
                    this.getNewVelocity(realv, (float) (diff * Math.PI / 360.0), this.renderdata.pxvelocity[var22]);
            if (this.renderdata.pxvelocity[var22] == 0.0F || Math.abs(diff) < 8.0F) {
                fcount++;
            }
            this.renderdata.p1xangle[var22] = this.renderdata.p1xangle[var22] + this.renderdata.pxvelocity[var22];
            this.renderdata.p2xangle[var22] = 0.0;
            this.renderdata.p3xangle[var22] = -this.renderdata.p1xangle[var22];

            if (this.renderdata.uppoint[var22] != 0.0F) {
                dd = (float) Math.atan2(dd, (this.renderdata.realposy[var22] - this.renderdata.uppoint[var22]) * 16.0);
            } else {
                dd = (float) Math.atan2(dd, (this.renderdata.realposy[var22] - this.renderdata.foot_ypos[var22]) * 16.0);
            }
            this.renderdata.udwantedangle[var22] = (float) (dd - (Math.PI / 2));
            while (this.renderdata.udwantedangle[var22] > Math.PI) {
                this.renderdata.udwantedangle[var22] = (float) (this.renderdata.udwantedangle[var22] - (Math.PI * 2));
            }
            while (this.renderdata.udwantedangle[var22] < -Math.PI) {
                this.renderdata.udwantedangle[var22] = (float) (this.renderdata.udwantedangle[var22] + (Math.PI * 2));
            }

            double rhm = this.renderdata.udwantedangle[var22];
            double rhdir = this.renderdata.udcurrentangle[var22];
            double rdv = (rhm - rhdir) % (Math.PI * 2);
            while (rdv > Math.PI) {
                rdv -= Math.PI * 2;
            }
            while (rdv < -Math.PI) {
                rdv += Math.PI * 2;
            }
            diff = (float) rdv;
            this.renderdata.udvelocity[var22] =
                    this.getNewVelocity(realv * 2.0F, diff, this.renderdata.udvelocity[var22]);
            if (this.renderdata.udvelocity[var22] == 0.0F || Math.abs(diff) < 0.03490658503988659) {
                this.renderdata.uppoint[var22] = 0.0F;
                fcount++;
            }
            rhdir += this.renderdata.udvelocity[var22];
            while (rhdir > Math.PI) {
                rhdir -= Math.PI * 2;
            }
            while (rhdir < -Math.PI) {
                rhdir += Math.PI * 2;
            }
            dd = this.renderdata.udcurrentangle[var22] = (float) rhdir;
            this.renderdata.uddisplayangle[var22] = dd;

            d1 = this.renderdata.realposx[var22] - this.renderdata.foot_xpos[var22];
            d3 = this.renderdata.realposz[var22] - this.renderdata.foot_zpos[var22];
            dd = (float) Math.atan2(d3, d1);
            rhm = this.renderdata.ywantedangle[var22] = dd;
            rhdir = this.renderdata.ycurrentangle[var22];
            rdv = (rhm - rhdir) % (Math.PI * 2);
            if (rdv > Math.PI) {
                rdv -= Math.PI * 2;
            }
            if (rdv < -Math.PI) {
                rdv += Math.PI * 2;
            }
            diff = (float) rdv;
            this.renderdata.yvelocity[var22] =
                    this.getNewVelocity(realv, diff, this.renderdata.yvelocity[var22]);
            if (this.renderdata.yvelocity[var22] == 0.0F || Math.abs(diff) < 0.03490658503988659) {
                fcount++;
            }
            this.renderdata.ycurrentangle[var22] =
                    this.renderdata.ycurrentangle[var22] + this.renderdata.yvelocity[var22];
            while (this.renderdata.ycurrentangle[var22] > Math.PI) {
                this.renderdata.ycurrentangle[var22] =
                        (float) (this.renderdata.ycurrentangle[var22] - (Math.PI * 2));
            }
            while (this.renderdata.ycurrentangle[var22] < -Math.PI) {
                this.renderdata.ycurrentangle[var22] =
                        (float) (this.renderdata.ycurrentangle[var22] + (Math.PI * 2));
            }
            dd = (float) (this.renderdata.ycurrentangle[var22]
                    - Math.toRadians(Mth.wrapDegrees(this.getYRot()))
                    - (Math.PI / 2));
            while (dd > Math.PI) {
                dd = (float) (dd - (Math.PI * 2));
            }
            while (dd < -Math.PI) {
                dd = (float) (dd + (Math.PI * 2));
            }
            this.renderdata.ydisplayangle[var22] = dd;
            if (fcount == 3) {
                this.renderdata.footup[var22] = 0;
            }
        }
    }

    private void findNewFooting(int i) {
        float f = 9.0F;
        int found = 0;
        float range = 0.0F;
        double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
        double pi = 3.1415926545;
        this.renderdata.footingticker[i] = 0;
        float d1 = (float) (this.getX() - this.xo);
        float d3 = (float) (this.getZ() - this.zo);
        double rhm = Math.atan2(d3, d1);
        double velocity = Math.sqrt(d1 * d1 + d3 * d3);
        double rdv = Math.abs(rhm - rhdir) % (pi * 2.0);
        if (rdv > pi) {
            rdv -= pi * 2.0;
        }
        rdv = Math.abs(rdv);
        if (Math.abs(velocity) < 0.01) {
            rdv = 0.0;
        }
        range = this.renderdata.yrange[i];
        range *= 0.8F;
        if (Math.abs((this.yRotO - this.getYRot()) % 360.0F) > 0.75F) {
            range = 0.0F;
        }
        if (i >= 4) {
            f = 4.0F;
        }
        if (rdv > 1.5) {
            range = -range;
            f = 4.0F;
            if (i >= 4) {
                f = 9.0F;
            }
        }
        if (i == 0 || i == 1) {
            f = 6.0F;
        }

        float fx;
        float deffx = fx = (float) (
                this.renderdata.realposx[i]
                        - f / 2.0F
                        * Math.sin(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                        + this.renderdata.ymid[i]));
        float fz;
        float deffz = fz = (float) (
                this.renderdata.realposz[i]
                        + f / 2.0F
                        * Math.cos(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                        + this.renderdata.ymid[i]));
        float fy;
        float deffy = fy = this.renderdata.realposy[i] - 1.0F;
        float oldf = f;
        int span = 1;

        while (found == 0 && f > 2.5F) {
            fx = (float) (
                    this.renderdata.realposx[i]
                            - f
                            * Math.sin(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                            + this.renderdata.ymid[i]
                            - range));
            fz = (float) (
                    this.renderdata.realposz[i]
                            + f
                            * Math.cos(Math.toRadians(Mth.wrapDegrees(this.getYRot() + 90.0F))
                            + this.renderdata.ymid[i]
                            - range));
            fy = this.renderdata.realposy[i];
            int j = 8;
            outer:
            while (found == 0 && j > -9) {
                for (int m = -span; found == 0 && m <= span; m++) {
                    for (int n = -span; found == 0 && n <= span; n++) {
                        BlockState blk = this.level().getBlockState(BlockPos.containing(fx + m, fy + j, fz + n));
                        if (!blk.isAir() && blk.blocksMotion()) {
                            d1 = this.renderdata.realposx[i] - (fx + m);
                            float d2 = this.renderdata.realposy[i] - (fy + j + 1.0F);
                            d3 = this.renderdata.realposz[i] - (fz + n);
                            float ddn = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                            ddn *= 16.0F;
                            if (!(ddn > 144.0F)) {
                                fy += j + 1;
                                fx += m;
                                fz += n;
                                found = 1;
                            }
                        }
                    }
                }
                j--;
            }
            if (--f < 2.5F && range != 0.0F) {
                range = 0.0F;
                span = 3;
                f = oldf;
            } else {
                break;
            }
        }

        if (found == 0) {
            fx = deffx;
            fy = deffy;
            fz = deffz;
        }

        float sfx = this.renderdata.foot_xpos[i];
        float sfy = this.renderdata.foot_ypos[i];
        float sfz = this.renderdata.foot_zpos[i];
        this.renderdata.foot_xpos[i] = fx;
        this.renderdata.foot_ypos[i] = fy;
        this.renderdata.foot_zpos[i] = fz;
        if (this.renderdata.footup[i] == 0) {
            this.renderdata.footup[i] = 1;
            d1 = sfx - fx;
            float d2 = sfy - fy;
            d3 = sfz - fz;
            float ddn = (float) Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            ddn *= 16.0F;
            d1 = (sfy + fy) / 2.0F;
            if (ddn > 3.0F) {
                d1 += 0.3F;
            }
            if (ddn > 24.0F) {
                d1 += 0.6F;
            }
            if (ddn > 50.0F) {
                d1 += 0.6F;
            }
            this.renderdata.uppoint[i] = d1;
        }
    }

    /**
     * Gold {@code RenderSpiderRobotInfo} (client leg IK state). Nested to avoid extra shared files.
     */
    public static class SpiderLegInfo {
        public float[] ydisplayangle = new float[8];
        public float[] ywantedangle = new float[8];
        public float[] ycurrentangle = new float[8];
        public float[] yvelocity = new float[8];
        public float[] ymid = new float[8];
        public float[] yoff = new float[8];
        public float[] yrange = new float[8];
        public float[] uddisplayangle = new float[8];
        public float[] udwantedangle = new float[8];
        public float[] udcurrentangle = new float[8];
        public float[] udvelocity = new float[8];
        public double[] p1xangle = new double[8];
        public double[] p2xangle = new double[8];
        public double[] p3xangle = new double[8];
        public float[] pxvelocity = new float[8];
        public float[] foot_xpos = new float[8];
        public float[] foot_ypos = new float[8];
        public float[] foot_zpos = new float[8];
        public float[] legoff = new float[8];
        public float[] realposx = new float[8];
        public float[] realposy = new float[8];
        public float[] realposz = new float[8];
        public int[] footup = new int[8];
        public float[] uppoint = new float[8];
        public int[] footingticker = new int[8];
        public int[] pairedwith = new int[8];
        public int gpcounter;
    }
}
