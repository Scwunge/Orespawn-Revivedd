package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
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
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Cephadrome} (EntityCreature) 1:1 best-effort for NeoForge 1.21.1.
 * Size 2.5×2.25, speed 0.25, health 300, attack 70, armor 16, XP 200.
 * Ground wander + feed-to-ride flight (activity 0/1). Combat vs Monster / untamed pets / hit players.
 * Texture: {@code textures/entity/cephadrome.png}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class Cephadrome extends PathfinderMob implements PlayerRideableJumping {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(Cephadrome.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_ACTIVITY =
            SynchedEntityData.defineId(Cephadrome.class, EntityDataSerializers.BYTE);

    public static final float GOLD_WIDTH = 2.5F;
    public static final float GOLD_HEIGHT = 2.25F;
    public static final double GOLD_HEALTH = 300.0;
    public static final double GOLD_SPEED = 0.25;
    public static final double GOLD_ATTACK = 70.0;
    public static final double GOLD_ARMOR = 16.0;
    public static final int GOLD_XP = 200;
    public static final double GOLD_FOLLOW = 48.0;

    private final float moveSpeed = 0.25F;
    private int updateit = 1;
    private int hurtTimer = 0;
    private int wasfed = 0;
    private int shouldattack = 0;
    private int wingSound = 0;
    private int hitByPlayer = 0;
    private int badmood = 0;
    private boolean playerJumpPending = false;
    private RenderInfo renderdata = new RenderInfo();

    public Cephadrome(EntityType<? extends Cephadrome> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.noCulling = true;
        this.renderdata = new RenderInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(16,1), WatchClosest(Player,9), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 9.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_ACTIVITY, (byte) 0);
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = 0.0F;
        this.renderdata.rf2 = 0.0F;
        this.renderdata.rf3 = 0.0F;
        this.renderdata.rf4 = 0.0F;
        this.renderdata.ri1 = 0;
        this.renderdata.ri2 = 0;
        this.renderdata.ri3 = 0;
        this.renderdata.ri4 = 0;
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        if (r == null) {
            return;
        }
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    public final int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public final void setAttacking(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ATTACKING, (byte) value);
        }
    }

    public final int getActivity() {
        return this.entityData.get(DATA_ACTIVITY);
    }

    public final void setActivity(int value) {
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ACTIVITY, (byte) value);
        }
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    public int getCephadromeHealth() {
        return (int) this.getHealth();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: canDespawn false if noDespawnRequired; else only when no rider
        if (this.isPersistenceRequired()) {
            return false;
        }
        return !this.isVehicle();
    }

    @Override
    public boolean isPushable() {
        return false; // gold canBePushed false
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold no fall / walk damage
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y + 0.1, m.z);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: MothraWings only when activity!=1 and 1/6
        if (this.getActivity() != 1 && this.random.nextInt(6) == 1) {
            return SoundEvents.ENDER_DRAGON_FLAP; // stand-in until SoundsHandler wires MothraWings
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:alo_hurt
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:alo_death
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    /** Gold mountedYOffset: 2.5 */
    public double getPassengersRidingOffset() {
        return 2.5;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity p = this.getFirstPassenger();
        return p instanceof LivingEntity living ? living : null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, net.minecraft.world.entity.EntityDimensions dimensions, float partialTick) {
        // gold updateRiderPosition: f=0.75 along yaw, y = mountedYOffset
        float f = 0.75F;
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
        tag.putInt("CephaWasFed", this.wasfed);
        tag.putInt("CephaAttacking", this.getAttacking());
        tag.putInt("CephaActivity", this.getActivity());
        tag.putInt("CephaHitByPlayer", this.hitByPlayer);
        tag.putInt("CephaBadMood", this.badmood);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasfed = tag.getInt("CephaWasFed");
        this.hitByPlayer = tag.getInt("CephaHitByPlayer");
        this.badmood = tag.getInt("CephaBadMood");
        this.setAttacking(tag.getInt("CephaAttacking"));
        this.setActivity(tag.getInt("CephaActivity"));
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();

        if (this.getActivity() == 1) {
            this.wingSound++;
            if (this.wingSound > 22) {
                if (!this.level().isClientSide) {
                    // gold orespawn:MothraWings 0.5
                    this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 0.5F, 1.0F);
                }
                this.wingSound = 0;
            }
        }

        // gold: when PlayNicely==0, always allow ride (wasfed forced 1 each tick)
        if (OreSpawnMain.PlayNicely == 0) {
            this.wasfed = 1;
        }
    }

    @Override
    public void aiStep() {
        if (this.getActivity() == 0) {
            super.aiStep();
        } else if (this.isDeadOrDying()) {
            super.aiStep();
            return;
        }

        if (this.isDeadOrDying()) {
            return;
        }

        if (this.level().isClientSide) {
            return;
        }

        if (this.getActivity() != 0 && this.getControllingPassenger() instanceof Player player) {
            this.driveFromRider(player);
        }

        if (this.isVehicle() && this.getFirstPassenger() != null && !this.getFirstPassenger().isAlive()) {
            this.ejectPassengers();
        }

        if (this.getActivity() == 1) {
            this.customServerAiStep();
        }
    }

    /** Gold onLivingUpdate boat-style flight when activity!=0 and rider present. */
    private void driveFromRider(Player player) {
        Vec3 dm = this.getDeltaMovement();
        double mx = Mth.clamp(dm.x, -2.0, 2.0);
        double mz = Mth.clamp(dm.z, -2.0, 2.0);
        double my = dm.y;

        double velocity = Math.sqrt(mx * mx + mz * mz);
        double gh = 1.55;
        BlockState below = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - gh, this.getZ()));
        if (!below.isAir()) {
            my += 0.07;
            this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
        } else {
            my -= 0.018;
        }

        double obstruction = 0.0;
        int span = 2 + (int) (velocity * 6.0);
        for (int k = 1; k < span; k++) {
            for (int i = 1; i < span * 2; i++) {
                double dx = i * Math.cos(Math.toRadians(this.getYRot() + 90.0F));
                double dz = i * Math.sin(Math.toRadians(this.getYRot() + 90.0F));
                BlockState bid = this.level().getBlockState(
                        BlockPos.containing(this.getX() + dx, this.getY() - k, this.getZ() + dz));
                if (!bid.isAir()) {
                    obstruction += 0.04;
                }
            }
        }
        my += obstruction * 0.09;
        this.setPos(this.getX(), this.getY() + obstruction * 0.09, this.getZ());
        if (my > 2.0) {
            my = 2.0;
        }

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
        if (velocity > 0.1) {
            double soft = Math.abs(1.5 - velocity);
            soft = Mth.clamp(soft, 0.01, 0.9);
            this.setYRot(player.getYRot() + (float) (relativeG * soft));
        } else {
            this.setYRot(player.getYRot());
        }
        if (my > 0.0) {
            this.setXRot(360.0F - 2.0F * (float) velocity);
        } else {
            this.setXRot(2.0F * (float) velocity);
        }
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        if (this.playerJumpPending) {
            // gold OreSpawnMain.flyup_keystate
            this.playerJumpPending = false;
            my += 0.04;
            my += velocity * 0.05;
        }

        double maxSpeed = 1.15;
        double newVelocity = Math.sqrt(mx * mx + mz * mz);
        double rhm = Math.atan2(mz, mx);
        double rhdir = Math.toRadians((player.getYRot() + 90.0F) % 360.0F);
        double rdv = Math.abs(rhm - rhdir) % (Math.PI * 2.0);
        if (rdv > Math.PI) {
            rdv -= Math.PI * 2.0;
        }
        rdv = Math.abs(rdv);
        if (Math.abs(newVelocity) < 0.01) {
            rdv = 0.0;
        }
        if (rdv > 1.5) {
            newVelocity = -newVelocity;
        }

        float im = player.zza;
        double deltav = 0.0;
        if (Math.abs(im) > 0.001F) {
            if (im > 0.0F) {
                deltav = 0.03;
                if (maxSpeed > 0.85) {
                    deltav += 0.05;
                }
            } else {
                maxSpeed = 0.35;
                deltav = -0.03;
            }
            newVelocity += deltav;
            if (newVelocity >= 0.0) {
                if (newVelocity > maxSpeed) {
                    newVelocity = maxSpeed;
                }
                mx = Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
                mz = Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
            } else {
                if (newVelocity < -maxSpeed) {
                    newVelocity = -maxSpeed;
                }
                double nv = -newVelocity;
                mx = Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * nv;
                mz = Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * nv;
            }
        } else if (newVelocity >= 0.0) {
            mx = Math.cos(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
            mz = Math.sin(Math.toRadians(this.getYRot() + 90.0F)) * newVelocity;
        } else {
            mx = Math.cos(Math.toRadians(this.getYRot() + 270.0F)) * (newVelocity * -1.0);
            mz = Math.sin(Math.toRadians(this.getYRot() + 270.0F)) * (newVelocity * -1.0);
        }

        this.setDeltaMovement(mx, my, mz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 after = this.getDeltaMovement();
        this.setDeltaMovement(after.x * 0.985, after.y * 0.94, after.z * 0.985);

        // gold push nearby collidable entities (not rider)
        AABB box = this.getBoundingBox().inflate(2.25, 2.0, 2.25);
        List<Entity> list = this.level().getEntities(this, box, e -> e != player && e.isAlive() && e.isPushable());
        for (Entity listEntity : list) {
            listEntity.push(this);
        }
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        this.playerJumpPending = true;
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {
        this.playerJumpPending = true;
    }

    @Override
    public void handleStopJump() {
        // no-op
    }

    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.updateit > 0) {
            this.updateit--;
        }
        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }

        if (this.updateit <= 0 && !this.level().isClientSide) {
            this.updateit = 30;
            if (this.isVehicle()) {
                this.setActivity(1);
            } else {
                this.setActivity(0);
            }
        }

        if (this.random.nextInt(100) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(2.0F);
        }

        if (this.getActivity() == 0) {
            super.customServerAiStep();
        }

        if (this.random.nextInt(7) == 1 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            LivingEntity e = this.getTarget();
            if (e != null && !e.isAlive()) {
                this.setTarget(null);
                e = null;
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                double maxdist = 10.0;
                if (this.getActivity() == 0) {
                    this.getNavigation().moveTo(e, 1.7);
                    maxdist = 6.0;
                }
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                this.setAttacking(1);
                if (GoldStyleCombat.inMeleeRange(this, e, maxdist)) {
                    this.doHurtTarget(e);
                } else {
                    // gold Kraken horizontal-only range check deferred (no Kraken port required)
                    double dx = this.getX() - e.getX();
                    double dz = this.getZ() - e.getZ();
                    double horiz = dx * dx + dz * dz;
                    double reach = maxdist + e.getBbWidth() / 2.0F;
                    if (horiz < reach * reach && e.getBbHeight() > 8.0F) {
                        this.doHurtTarget(e);
                    }
                }
            } else if (this.getAttacking() != 0) {
                this.setAttacking(0);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        double ks = 2.5;
        double inair = 0.35;
        float iskraken = 1.0F;
        boolean ret = false;
        if (target instanceof EnderDragon) {
            ret = target.hurt(this.damageSources().mobAttack(this), 70.0F);
        } else if (target instanceof LivingEntity living) {
            // gold Kraken multiplier 1.5 — class may be unported
            if (living.getBbHeight() > 8.0F && living.getBbWidth() > 4.0F) {
                iskraken = 1.5F;
            }
            ret = living.hurt(this.damageSources().mobAttack(this), iskraken * 70.0F);
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return ret;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurtTimer > 0) {
            return false;
        }
        // gold: ignore cactus
        if (source == this.damageSources().cactus()) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        this.hurtTimer = 25;
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
            ret = true;
        }
        if (e instanceof Player && this.getHealth() < this.getMaxHealth() * 9.0F / 10.0F) {
            this.hitByPlayer = 1;
        }
        return ret;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Cephadrome) {
            return false;
        }
        if (target instanceof Monster) {
            return true;
        }
        if (target instanceof Mothra) {
            return true;
        }
        // gold Leon / GammaMetroid / WaterDragon: only if untamed
        if (target instanceof TamableAnimal pet) {
            return !pet.isTame();
        }
        if (target instanceof EnderDragon) {
            return true;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
            if (this.hitByPlayer != 0) {
                return true;
            }
            if (this.badmood != 0) {
                return true;
            }
            if (this.shouldattack > 0) {
                this.shouldattack = 0;
                return true;
            }
            return false;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold expand 16,20,16 + GenericTargetSorter nearest
        AABB box = this.getBoundingBox().inflate(16.0, 20.0, 16.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box, this::isSuitableTarget);
        LivingEntity best = null;
        double bestD = Double.MAX_VALUE;
        for (LivingEntity living : list) {
            double d = this.distanceToSqr(living);
            if (d < bestD) {
                bestD = d;
                best = living;
            }
        }
        return best;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold spawner "Cephadrome" nearby → badmood=1, allow
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k);
                    BlockState st = level.getBlockState(pos);
                    if (st.is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof SpawnerBlockEntity) {
                            this.badmood = 1;
                            return true;
                        }
                    }
                }
            }
        }

        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }

        for (int var10 = -2; var10 < 2; var10++) {
            for (int j = -2; j < 2; j++) {
                for (int i = 1; i < 5; i++) {
                    BlockState bid = level.getBlockState(
                            BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + var10));
                    if (!bid.isAir()) {
                        return false;
                    }
                }
            }
        }

        List<Cephadrome> peers = level.getEntitiesOfClass(
                Cephadrome.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0), e -> e != this);
        return peers.isEmpty();
    }

    private void playTameEffect(boolean happy) {
        for (int i = 0; i < 20; i++) {
            double d0 = this.random.nextGaussian() * 0.08;
            double d1 = this.random.nextGaussian() * 0.08;
            double d2 = this.random.nextGaussian() * 0.08;
            this.level().addParticle(
                    happy ? ParticleTypes.HEART : ParticleTypes.SMOKE,
                    this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                    this.getY() + 0.5 + this.random.nextFloat() * 1.5,
                    this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 2.5F,
                    d0,
                    d1,
                    d2);
        }
    }

    private static boolean isFeedFood(ItemStack stack) {
        // gold: beef, cooked_beef, cooked_chicken
        return stack.is(Items.BEEF) || stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_CHICKEN);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty() && isFeedFood(stack) && this.distanceToSqr(player) < 25.0) {
            if (!this.level().isClientSide) {
                this.heal(this.mygetMaxHealth() - this.getHealth());
            }
            this.wasfed = 1;
            this.shouldattack = 0;
            this.playTameEffect(true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isVehicle() && this.getControllingPassenger() instanceof Player other && other != player) {
            return InteractionResult.PASS;
        }

        if (stack.isEmpty() && this.distanceToSqr(player) < 25.0 && !this.level().isClientSide) {
            if (this.wasfed == 0) {
                this.getNavigation().moveTo(player, 1.2);
                this.shouldattack = 1;
                return InteractionResult.FAIL;
            }
            player.startRiding(this);
            this.wasfed = 0;
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        int i = 4 + this.random.nextInt(6);
        for (int n = 0; n < i; n++) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        }
        i = 4 + this.random.nextInt(6);
        for (int n = 0; n < i; n++) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
        i = 1 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            int roll = this.random.nextInt(20);
            switch (roll) {
                case 0, 3 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_SWORD.get()));
                case 1 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND));
                case 2 -> this.spawnAtLocation(new ItemStack(ModItems.THUNDER_STAFF.get()));
                case 4 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_SHOVEL.get()));
                case 5 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_PICKAXE.get()));
                case 6 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_AXE.get()));
                case 7 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_HOE.get()));
                case 8 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_HELMET.get()));
                case 9 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_CHESTPLATE.get()));
                case 10 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_LEGGINGS.get()));
                case 11 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY_BOOTS.get()));
                case 12, 13, 14, 15, 16, 17 -> this.spawnAtLocation(new ItemStack(ModItems.RUBY.get()));
                default -> {
                }
            }
        }
        // gold default drop: beef
        this.spawnAtLocation(new ItemStack(Items.BEEF));
    }
}
