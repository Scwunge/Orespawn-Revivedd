package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Frog} (EntityAnimal). Size 0.75×0.75, speed 0.1, health 8, attack attr 0
 * (hit deals 3.0), XP 5. Hop + singing anim; hunts small insects when not peaceful.
 * Prince/princess shift-empty-hand transform deferred (needs Girlfriend/Boyfriend).
 */
public class Frog extends Animal {
    private static final EntityDataAccessor<Byte> DATA_SINGING =
            SynchedEntityData.defineId(Frog.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.1F;
    private int singing;
    private int jumpcount;

    public Frog(EntityType<? extends Frog> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SINGING, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAISwimming, EntityAIPanic(1.4), MyEntityAIWander(1.0)
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 10, 1.0));
    }

    public int getSinging() {
        return this.entityData.get(DATA_SINGING);
    }

    public void setSinging(int value) {
        this.entityData.set(DATA_SINGING, (byte) value);
    }

    private void jumpAround() {
        // gold: boost Y + forward along yaw
        Vec3 v = this.getDeltaMovement();
        float f = 0.7F + Math.abs(this.random.nextFloat() * 0.75F);
        float d = (float) Math.toRadians(this.getYRot());
        this.setDeltaMovement(
                v.x - f * Math.sin(d),
                v.y + (0.75F + Math.abs(this.random.nextFloat() * 0.55F)),
                v.z + f * Math.cos(d));
        this.setPos(this.getX(), this.getY() + 0.35, this.getZ());
        this.hasImpulse = true;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (!this.level().isClientSide) {
            if (this.singing != 0) {
                this.singing--;
                if (this.singing <= 0) {
                    this.setSinging(0);
                }
            }
            if (this.jumpcount > 0) {
                this.jumpcount--;
            }
            if (this.jumpcount == 0 && this.random.nextInt(70) == 1) {
                this.jumpAround();
                this.jumpcount = 50;
            }
        }
    }

    public int mygetMaxHealth() {
        return 8;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 50% silence; else singing=35 + orespawn:frog (sound reg deferred)
        if (!this.level().isClientSide) {
            if (this.random.nextInt(2) == 0) {
                return null;
            }
            this.singing = 35;
            this.setSinging(this.singing);
        }
        //  frog1/frog2.ogg present; SoundsHandler frog entry deferred to SoundsHandler registration
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:scorpion_hit
        return SoundsHandler.ENTITY_SCORPION_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:big_splat
        return SoundsHandler.BIG_SPLAT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // silent
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 4× slime_ball
        for (int i = 0; i < 4; i++) {
            this.spawnAtLocation(new ItemStack(Items.SLIME_BALL));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: 3.0 damage; heal 1 if kill
        boolean hit = target.hurt(this.damageSources().mobAttack(this), 3.0F);
        if (target instanceof LivingEntity living && !living.isAlive()) {
            this.heal(1.0F);
        }
        return hit;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        if (!this.level().isClientSide && this.jumpcount <= 0) {
            this.jumpAround();
            this.jumpcount = 25;
        }
        return ret;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: 1/12 seek insect prey when not peaceful
        if (this.random.nextInt(12) == 0 && this.level().getDifficulty() != Difficulty.PEACEFUL) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getNavigation().moveTo(e, 1.25);
                if (this.distanceToSqr(e) < 6.0) {
                    this.doHurtTarget(e);
                }
            }
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
        // gold: Ant, Butterfly, Cricket, Mosquito, Firefly, WormSmall
        return target instanceof Ant
                || target instanceof Butterfly
                || target instanceof Cricket
                || target instanceof Mosquito
                || target instanceof Firefly
                || target instanceof WormSmall;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(8.0, 3.0, 8.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50, daytime, buddies ≤ 5. Dim-5 bias deferred. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        return this.findBuddies() <= 5;
    }

    /** Spawns in water: skip the vanilla "no liquid in bounding box" check (as {@code WaterAnimal} does). */
    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    private int findBuddies() {
        List<Frog> list =
                this.level().getEntitiesOfClass(Frog.class, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
        return list.size();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold createChild null — not breedable
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }
}
