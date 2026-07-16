package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Rat} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 0.25×0.5, speed 0.25, health 5, attack 3, armor 1, XP 5.
 * Optional owner UUID (from RatSword) — follows / teleports to owner, skips friendly targets.
 */
public class Rat extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.25F;
    /** Gold {@code myowner} — player UUID string, or null. */
    @Nullable
    private String myowner = null;

    public Rat(EntityType<? extends Rat> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // Rat_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 3.0) // Rat_stats.attack
                .add(Attributes.ARMOR, 1.0) // Rat_stats.defense
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35));
        // gold EntityAIMoveThroughVillage skipped
        this.goalSelector.addGoal(3, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: if noDespawnRequired → false; else myowner == null
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.myowner == null;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /** Gold jump: motionY += 0.25, posY += 0.25. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y + 0.25, m.z);
        this.setPos(this.getX(), this.getY() + 0.25, this.getZ());
    }

    public int mygetMaxHealth() {
        return 5;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /** Gold {@code setOwner} — store attacking player's UUID string. */
    public void setOwner(LivingEntity e) {
        if (e instanceof Player p) {
            String s = p.getUUID().toString();
            if (s != null) {
                this.myowner = s;
            }
        }
    }

    @Nullable
    public String getOwnerUuidString() {
        return this.myowner;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("MyOwner", this.myowner == null ? "null" : this.myowner);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.myowner = tag.getString("MyOwner");
        if (this.myowner != null && this.myowner.equals("null")) {
            this.myowner = null;
        }
        if (this.myowner != null && this.myowner.isEmpty()) {
            this.myowner = null;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ENTITY_RAT_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_RAT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_RAT_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.45F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: rotten flesh (field_151078_bh)
        this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /** Gold: ignore inWall damage. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId()) || source == this.damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code updateAITasks}: clear revenge 1/200; combat 1/5;
     * melee distSq &lt; 4; hit nextInt(8)==0 || nextInt(7)==1; path 1.25;
     * owner follow/teleport; heal 1/250.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        if (this.random.nextInt(5) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setAttacking(1);
                this.getNavigation().moveTo(e, 1.25);
                // gold: distanceSq < 4.0; hit nextInt(8)==0 || nextInt(7)==1
                if (this.distanceToSqr(e) < 4.0
                        && (this.random.nextInt(8) == 0 || this.random.nextInt(7) == 1)) {
                    this.doHurtTarget(e);
                }
            } else {
                this.setAttacking(0);
                if (this.myowner != null) {
                    Player p = null;
                    try {
                        p = this.level().getPlayerByUUID(java.util.UUID.fromString(this.myowner));
                    } catch (IllegalArgumentException ignored) {
                        // corrupt NBT owner string
                    }
                    if (p != null) {
                        if (this.distanceToSqr(p) > 64.0) {
                            this.getNavigation().moveTo(p, 1.75);
                        }
                        if (this.distanceToSqr(p) > 256.0) {
                            this.teleportTo(
                                    p.getX() + this.random.nextFloat() - this.random.nextFloat(),
                                    p.getY(),
                                    p.getZ() + this.random.nextFloat() - this.random.nextFloat());
                        }
                    }
                }
            }
        }
        if (this.random.nextInt(250) == 1) {
            this.heal(1.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold: skip other rats (+ several unported types)
        if (target instanceof Rat) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
            if (this.myowner != null) {
                if (this.myowner.equals(player.getUUID().toString())) {
                    return false;
                }
                // gold RatPlayerFriendly != 0 → skip players when owned
                if (OreSpawnMain.RatPlayerFriendly != 0) {
                    return false;
                }
            }
        }
        if (this.myowner != null && target instanceof TamableAnimal tameable) {
            if (OreSpawnMain.RatPetFriendly != 0 && tameable.isTame()) {
                return false;
            }
            if (tameable.getOwnerUUID() != null
                    && this.myowner.equals(tameable.getOwnerUUID().toString())) {
                return false;
            }
        } else if (this.myowner != null && target instanceof OwnableEntity ownable) {
            if (ownable.getOwnerUUID() != null
                    && this.myowner.equals(ownable.getOwnerUUID().toString())) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null; box expand(9, 2, 9)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 9.0, 2.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: light + buddies ≤ 8 (+ dim5 air checks skipped for simplicity).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        return this.findBuddies() <= 8;
    }

    private int findBuddies() {
        return this.level()
                .getEntitiesOfClass(Rat.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0))
                .size();
    }
}
