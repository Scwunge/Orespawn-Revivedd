package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Bee} (EntityMob). Size 1.5×2.5, speed 0.32, health 80, attack 12, defense 5, XP 25
 * (defaults from gold {@code get_mobstats("Bee", 80, 12, 5)}). Flying hostile; poison sting 1/3.
 */
public class Bee extends Monster {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);

    /** Gold Bee_stats.health default */
    public static final int GOLD_HEALTH = 80;
    /** Gold Bee_stats.attack default */
    public static final float GOLD_ATTACK = 12.0F;
    /** Gold Bee_stats.defense default */
    public static final double GOLD_ARMOR = 5.0;

    @Nullable
    private BlockPos currentFlightTarget;
    private int stuckCount;
    private int lastX;
    private int lastZ;
    @Nullable
    private Entity revengeTargetEntity;

    public Bee(EntityType<? extends Bee> type, Level level) {
        super(type, level);
        this.xpReward = 25;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    public int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, (byte) value);
    }

    public int mygetMaxHealth() {
        return GOLD_HEALTH;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 0.25F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: orespawn:Beebuzz (beebuzz.ogg)
        return SoundsHandler.ENTITY_BEE_BUZZ.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:dragonfly_hurt
        return SoundsHandler.ENTITY_DRAGONFLY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:alo_death
        return SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    /** Gold {@code collideWithEntity} empty — no shove on contact. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);
        // gold: in water, 1/4 self-hurt
        if (this.isInWater() && this.random.nextInt(4) == 1) {
            this.doHurtTarget(this);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = target.hurt(this.damageSources().mobAttack(this), GOLD_ATTACK);
        if (this.random.nextInt(3) == 1 && target instanceof LivingEntity living) {
            // gold Potion.poison 50 ticks amp 0
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 0));
        }
        return hit;
    }

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

        if (this.lastX == (int) this.getX() && this.lastZ == (int) this.getZ()) {
            this.stuckCount++;
        } else {
            this.stuckCount = 0;
            this.lastX = (int) this.getX();
            this.lastZ = (int) this.getZ();
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }

        if (this.stuckCount > 50
                || this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            this.stuckCount = 0;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(9) + 4;
                int xdir = this.random.nextInt(9) + 4;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir, this.getY() + this.random.nextInt(6) - 3, this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        } else if (this.random.nextInt(15) == 0) {
            LivingEntity e = null;
            if (this.revengeTargetEntity instanceof LivingEntity living && living.isAlive()) {
                e = living;
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                this.setAttacking(1);
                this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY() + 1.0, e.getZ());
                if (this.distanceToSqr(e) < 16.0) {
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.5 - m.x) * 0.30000000149011613,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.5 - m.z) * 0.30000000149011613);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI)
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(1.0F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity && this.currentFlightTarget != null) {
            this.revengeTargetEntity = e;
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
        }
        return ret;
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target.isInWater()) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.isCreative() && !p.isSpectator();
        }
        // gold: Villager, Girlfriend, Boyfriend — GF/BF deferred
        return target instanceof Villager;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(10.0, 6.0, 10.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: y ≥ 50 + daytime; clear air column; dim4 always;
     * spawner-name check deferred.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        int bx = Mth.floor(this.getX());
        int by = Mth.floor(this.getY());
        int bz = Mth.floor(this.getZ());
        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 2; j++) {
                for (int i = 1; i < 5; i++) {
                    if (!level.getBlockState(new BlockPos(bx + j, by + i, bz + k)).isAir()) {
                        return false;
                    }
                }
            }
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 2+0.9 gold_nugget, butter candy (deferred), dandelion, sugar
        int n = 2 + this.random.nextInt(10);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
        }
        n = 2 + this.random.nextInt(10);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.DANDELION));
        }
        n = 2 + this.random.nextInt(10);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.SUGAR));
        }
        // MyButterCandy drops deferred until item is available
    }
}
