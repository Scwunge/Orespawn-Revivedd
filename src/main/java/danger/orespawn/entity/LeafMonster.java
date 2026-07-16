package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code LeafMonster} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.0×2.5, speed 0.25, health 6, attack 2, armor 1, XP 5.
 * Camouflage idle (snap to block center / 90° yaw) when not attacking; night hunter.
 */
public class LeafMonster extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(LeafMonster.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.25F;

    public LeafMonster(EntityType<? extends LeafMonster> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 6.0) // LeafMonster_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0) // LeafMonster_stats.attack
                .add(Attributes.ARMOR, 1.0) // LeafMonster_stats.defense
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Panic 1.35, HurtByTarget (no wander / look idle)
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    public int mygetMaxHealth() {
        return 6;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    /**
     * Gold {@code fall}: ceil(dist-3), damage sound big/small, damage capped at 2.
     */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        float i = Mth.ceil(fallDistance - 3.0F);
        if (i > 0.0F) {
            if (i > 2.0F) {
                this.playSound(SoundEvents.GENERIC_BIG_FALL, 1.0F, 1.0F);
                i = 2.0F;
            } else {
                this.playSound(SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);
            }
            this.hurt(source, i);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold onUpdate: when not attacking, snap to block center + 90° facing (camouflage tree pose)
        if (this.getAttacking() == 0) {
            int px = (int) this.getX();
            int py = (int) this.getY();
            int pz = (int) this.getZ();
            double x = px;
            double y = py;
            double z = pz;
            if (x > 0.0) {
                x += 0.5;
            }
            if (z > 0.0) {
                z += 0.5;
            }
            if (x < 0.0) {
                x -= 0.5;
            }
            if (z < 0.0) {
                z -= 0.5;
            }
            this.setPos(x, y, z);
            this.setXRot(0.0F);
            int yawSnap = (int) this.yBodyRot;
            yawSnap /= 90;
            float snapped = yawSnap * 90.0F;
            this.setYRot(snapped);
            this.yBodyRot = snapped;
            this.yHeadRot = snapped;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:leaves_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:leaves_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.65F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(3) → oak_log / oak_leaves / rotten_flesh
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Blocks.OAK_LOG));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(Blocks.OAK_LEAVES));
        } else {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code updateAITasks}: clear revenge 1/100; combat 1/4;
     * lookAt 10/10; path 1.25; melee distSq &lt; 5; hit nextInt(8)==0 || nextInt(10)==1.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(100) == 1) {
            this.setLastHurtByMob(null);
        }
        if (this.random.nextInt(4) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                this.setAttacking(1);
                this.getNavigation().moveTo(e, 1.25);
                if (this.distanceToSqr(e) < 5.0
                        && (this.random.nextInt(8) == 0 || this.random.nextInt(10) == 1)) {
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold: ants, butterflies, luna moths, non-creative players
        if (target instanceof Ant) {
            return true;
        }
        if (target instanceof Butterfly) {
            return true;
        }
        if (target instanceof Moth) {
            // gold EntityLunaMoth — Moth is the 1.21 stand-in
            return true;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(4, 6, 4)
        return GoldStyleCombat.findTarget(this, 4.0, 6.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light, night only, y ≥ 50 (dim4 y≤20 deferred), buddies ≤ 4.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        // gold DimensionID4: y > 20 false; else y < 50 false — dim4 deferred
        if (this.getY() < 50.0) {
            return false;
        }
        return this.findBuddies() <= 4;
    }

    private int findBuddies() {
        return this.level()
                .getEntitiesOfClass(LeafMonster.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0))
                .size();
    }
}
