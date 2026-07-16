package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Rotator} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.0×2.0, speed 0.25, health 35, attack 8, armor 10, XP 35 (Rotator_stats defaults).
 * Fire-immune flying crystal hunter; night-only natural spawn; fireworksSpark FX.
 * Texture: {@code textures/entity/rotatortexture.png}.
 */
public class Rotator extends Monster {
    public static final float GOLD_WIDTH = 1.0F;
    public static final float GOLD_HEIGHT = 2.0F;
    public static final double GOLD_HEALTH = 35.0;
    public static final double GOLD_SPEED = 0.25;
    public static final double GOLD_ATTACK = 8.0;
    public static final double GOLD_ARMOR = 10.0;
    public static final int GOLD_XP = 35;
    public static final double GOLD_FOLLOW = 25.0;

    @Nullable
    private BlockPos currentFlightTarget;
    private RenderInfo renderdata = new RenderInfo();
    private int busyFighting = 0;
    /** Gold {@code was_spawnered} — prevents day despawn when from spawner. */
    private int wasSpawnered = 0;

    public Rotator(EntityType<? extends Rotator> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP; // gold field_70728_aV = 35
        // gold field_70178_ae fireImmune — also set on EntityType builder on the EntityType builder
        // gold field_70174_ab = 25 follow
        this.renderdata = new RenderInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW);
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: if noDespawnRequired → false; if busy_fighting → false; else was_spawnered==0
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.busyFighting != 0) {
            return false;
        }
        return this.wasSpawnered == 0;
    }

    public int mygetMaxHealth() {
        return (int) GOLD_HEALTH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold vortexlive
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:glasshit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:glassdead
        return null;
    }

    @Override
    public boolean isPushable() {
        // gold canBePushed true
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
        // gold empty collideWithEntity
    }

    @Override
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        // gold canBreatheUnderwater true — restore air (hard rule: no canBreatheUnderwater override)
        this.setAirSupply(this.getMaxAirSupply());

        if (this.level().isClientSide && this.random.nextInt(10) == 1) {
            this.level()
                    .addParticle(
                            ParticleTypes.FIREWORK,
                            this.getX(),
                            this.getY() + 1.4F,
                            this.getZ(),
                            (this.random.nextFloat() - this.random.nextFloat()) / 4.0F,
                            (this.random.nextFloat() - this.random.nextFloat()) / 4.0F,
                            (this.random.nextFloat() - this.random.nextFloat()) / 4.0F);
        }

        this.busyFighting = 0;
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            double a = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
            this.level()
                    .addParticle(
                            ParticleTypes.FIREWORK,
                            this.getX(),
                            this.getY() + 1.4F,
                            this.getZ(),
                            Math.cos(a),
                            (e.getY() - this.getY()) / 10.0,
                            Math.sin(a));
            this.busyFighting = 1;
        }

        if (!this.isPersistenceRequired() && this.busyFighting == 0 && this.wasSpawnered == 0) {
            long t = this.level().getDayTime() % 24000L;
            if (t < 12000L && this.random.nextInt(400) == 1) {
                this.discard();
            }
        }
    }

    /** Gold {@code canSeeTarget} — ray from y+0.75; MISS means clear. */
    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        // gold: repath if nextInt(300)==0 OR within 2.1 of target; else 1/9 combat steer
        if (this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(10) + 8;
                int xdir = this.random.nextInt(10) + 8;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(6) - 3,
                        this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        } else if (this.random.nextInt(9) == 2) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                double a = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                a += Math.PI / 2;
                this.currentFlightTarget = BlockPos.containing(
                        e.getX() + 2.5 * Math.cos(a), e.getY(), e.getZ() + 2.5 * Math.sin(a));
                if (this.distanceToSqr(e) < 9.0) {
                    this.doHurtTarget(e);
                }
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.4 - m.x) * 0.2,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.4 - m.z) * 0.2);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI)
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold canTriggerWalking true → default false for isIgnoringBlockTriggers
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        // gold: arrows do no damage
        if (e instanceof AbstractArrow) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
        }
        return ret;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        if (target instanceof Termite) {
            return false;
        }
        if (target instanceof Vortex) {
            return false;
        }
        if (target instanceof Rotator) {
            return false;
        }
        if (target instanceof DungeonBeast) {
            return false;
        }
        if (target instanceof Peacock) {
            return false;
        }
        if (target instanceof CrystalCow) {
            return false;
        }
        if (target instanceof Irukandji) {
            return false;
        }
        if (target instanceof Skate) {
            return false;
        }
        if (target instanceof Whale) {
            return false;
        }
        if (target instanceof Flounder) {
            return false;
        }
        if (target instanceof Urchin) {
            return false;
        }
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof LurkingTerror) {
            return false;
        }
        if (target instanceof CloudShark) {
            return false;
        }
        if (target instanceof Mothra) {
            return false;
        }
        if (target instanceof Bee) {
            return false;
        }
        if (target instanceof Mantis) {
            return false;
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(12, 10, 12)
        return GoldStyleCombat.findTarget(this, 12.0, 10.0, this::isSuitableTarget);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem random of 4
        int i = this.random.nextInt(4);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_PINK_INGOT.get()));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.TIGERSEYE_INGOT.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModBlocks.CRYSTAL_COAL.get().asItem()));
        } else {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WasSpawnered", this.wasSpawnered);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasSpawnered = tag.getInt("WasSpawnered");
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner nearby sets was_spawnered; else dark + clear headroom + night.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        for (int k = -2; k <= 2; k++) {
            for (int j = -2; j <= 2; j++) {
                for (int i = 1; i < 4; i++) {
                    BlockPos pos = BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k);
                    if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold matches spawner entity name "Rotator"
                            this.wasSpawnered = 1;
                            return true;
                        }
                    }
                }
            }
        }

        // gold isValidLightLevel / super light
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }

        for (int var10 = -1; var10 <= 1; var10++) {
            for (int j = -1; j <= 1; j++) {
                for (int i = 1; i < 3; i++) {
                    if (!level.getBlockState(BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + var10))
                            .isAir()) {
                        return false;
                    }
                }
            }
        }

        if (level instanceof Level lvl) {
            long t = lvl.getDayTime() % 24000L;
            return t >= 12000L;
        }
        return true;
    }
}
