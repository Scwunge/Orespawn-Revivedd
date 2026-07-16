package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Fairy} (EntityAmbientCreature). Size 0.4×0.8, speed 0.1, health 40, attack attr 3
 * (hit deals 2.0 like gold {@code attackEntityAsMob}). Flies; hunts Monster; follows named owner.
 * fairy_type 0–8 for textures; blinker for night sparkle / glow.
 */
public class Fairy extends AmbientCreature {
    private static final EntityDataAccessor<Integer> DATA_FAIRY_TYPE =
            SynchedEntityData.defineId(Fairy.class, EntityDataSerializers.INT);

    int myBlink;
    int blinker;
    /** Local mirror; authoritative client value is {@link #DATA_FAIRY_TYPE}. */
    public int fairyType;
    @Nullable
    private BlockPos currentFlightTarget;
    /** Gold {@code myowner} — player display name. */
    @Nullable
    private String myOwner;
    /** Extra reliability beyond gold name-only follow. */
    @Nullable
    private java.util.UUID ownerUuid;

    public Fairy(EntityType<? extends Fairy> type, Level level) {
        super(type, level);
        this.myBlink = 20 + this.random.nextInt(20);
        if (!level.isClientSide) {
            this.setFairyType(this.random.nextInt(9));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FAIRY_TYPE, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    public void setFairyType(int type) {
        this.fairyType = type;
        this.entityData.set(DATA_FAIRY_TYPE, type);
    }

    public int getFairyType() {
        return this.entityData.get(DATA_FAIRY_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_FAIRY_TYPE.equals(key)) {
            this.fairyType = this.entityData.get(DATA_FAIRY_TYPE);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0) // gold mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public int mygetMaxHealth() {
        return 40;
    }

    /**
     * Gold {@code setOwner}: only EntityPlayer → store display name.
     * Also stores UUID for modern follow reliability.
     */
    public void setOwner(LivingEntity e) {
        if (e instanceof Player p) {
            String s = p.getGameProfile().getName();
            if (s != null && !s.isEmpty()) {
                this.myOwner = s;
            }
            this.ownerUuid = p.getUUID();
            this.setPersistenceRequired();
        }
    }

    @Nullable
    public String getOwnerName() {
        return this.myOwner;
    }

    /** Gold {@code getBlink}: full bright half-cycle. */
    public float getBlink() {
        return this.blinker < this.myBlink / 2 ? 240.0F : 0.0F;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold attackEntityAsMob: peaceful → false; else fixed 2.0
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        return target.hurt(this.damageSources().mobAttack(this), 2.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.25F;
    }

    @Override
    public float getVoicePitch() {
        return 1.7F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:rat_hit — no dedicated rat_hit holder yet; little_splat stand-in
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.BIG_SPLAT.get();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    /** Gold {@code collideWithEntity} empty. */
    @Override
    protected void doPush(Entity entity) {}

    /** Gold {@code collideWithNearbyEntities} empty. */
    @Override
    protected void pushEntities() {}

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.600000023841, m.z);
        this.blinker++;
        if (this.blinker > this.myBlink) {
            this.blinker = 0;
        }
        // gold: night (dayTime % 24000 >= 12000) client sparks when blinking
        long t = this.level().getDayTime() % 24000L;
        if (t >= 12000L
                && this.level().isClientSide
                && this.random.nextInt(5) == 0
                && this.getBlink() > 1.0F) {
            this.level().addParticle(
                    ParticleTypes.FIREWORK,
                    this.getX(),
                    this.getY() - 0.15F,
                    this.getZ(),
                    (this.random.nextFloat() - this.random.nextFloat()) / 8.0F,
                    -this.random.nextFloat() / 8.0F,
                    (this.random.nextFloat() - this.random.nextFloat()) / 8.0F);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("MyOwner", this.myOwner == null ? "null" : this.myOwner);
        tag.putInt("FairyType", this.getFairyType());
        if (this.ownerUuid != null) {
            tag.putUUID("OwnerUUID", this.ownerUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.myOwner = tag.getString("MyOwner");
        if (this.myOwner != null && this.myOwner.equals("null")) {
            this.myOwner = null;
        }
        // gold wrote "FairyType" but read "fairyType" — accept both
        if (tag.contains("FairyType")) {
            this.setFairyType(tag.getInt("FairyType"));
        } else if (tag.contains("fairyType")) {
            this.setFairyType(tag.getInt("fairyType"));
        }
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUuid = tag.getUUID("OwnerUUID");
        }
        if (this.myOwner != null || this.ownerUuid != null) {
            this.setPersistenceRequired();
        }
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
        // gold: instanceof EntityMob
        return target instanceof Monster;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(8.0, 8.0, 8.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.25, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Nullable
    private Player findOwnerPlayer() {
        if (this.ownerUuid != null) {
            Player byId = this.level().getPlayerByUUID(this.ownerUuid);
            if (byId != null) {
                return byId;
            }
        }
        if (this.myOwner != null && this.level() instanceof ServerLevel server) {
            return server.getServer().getPlayerList().getPlayerByName(this.myOwner);
        }
        return null;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }

        // gold: repath if 1/200 OR close to target; else maybe attack / follow owner
        if (this.random.nextInt(200) != 0
                && !(this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.5F)) {
            if (this.random.nextInt(12) == 0 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
                LivingEntity e = this.findSomethingToAttack();
                if (e != null) {
                    this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                    if (this.distanceToSqr(e) < 6.0) {
                        this.doHurtTarget(e);
                    }
                }
            } else if (this.myOwner != null || this.ownerUuid != null) {
                Player p = this.findOwnerPlayer();
                if (p != null) {
                    if (this.distanceToSqr(p) > 64.0) {
                        this.currentFlightTarget = BlockPos.containing(
                                p.getX() + this.random.nextInt(3) - this.random.nextInt(3),
                                p.getY() + 1.0,
                                p.getZ() + this.random.nextInt(3) - this.random.nextInt(3));
                    }
                    if (this.distanceToSqr(p) > 256.0) {
                        this.teleportTo(
                                p.getX() + this.random.nextFloat() - this.random.nextFloat(),
                                p.getY(),
                                p.getZ() + this.random.nextFloat() - this.random.nextFloat());
                    }
                }
            }
        } else {
            int keepTrying = 25;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(8);
                int xdir = this.random.nextInt(8);
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(5) - 2,
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
        }

        // gold: 1/250 heal 1
        if (this.random.nextInt(250) == 1) {
            this.heal(1.0F);
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.2 - m.x) * 0.1,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.1,
                m.z + (Math.signum(var5) * 0.2 - m.z) * 0.1);
        float var7 =
                (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.2F);
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
        return true;
    }

    /**
     * Gold {@code getCanSpawnHere}: need ≥6 air cells in 3×3 at feet, y ≥ 50.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        int sc = 0;
        BlockPos base = this.blockPosition();
        for (int k = -1; k <= 1; k++) {
            for (int j = -1; j <= 1; j++) {
                if (level.getBlockState(base.offset(j, 0, k)).isAir()) {
                    sc++;
                }
            }
        }
        if (sc < 6) {
            return false;
        }
        return !(this.getY() < 50.0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: if persistent → false; else despawn only when no owner
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.myOwner == null && this.ownerUuid == null;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: CrystalTorch — port stand-in is extreme_torch
        this.spawnAtLocation(new ItemStack(ModBlocks.EXTREME_TORCH.get()));
    }
}
