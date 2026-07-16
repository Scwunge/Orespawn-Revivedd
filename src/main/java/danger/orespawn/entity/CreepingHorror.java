package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
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
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code CreepingHorror} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 0.75×0.5, speed 0.25, health 10, attack 2, armor 3, XP 5.
 * Night ground hunter; despawns during day.
 */
public class CreepingHorror extends Monster {
    private final float moveSpeed = 0.25F;

    public CreepingHorror(EntityType<? extends CreepingHorror> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // CreepingHorror_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0) // CreepingHorror_stats.attack
                .add(Attributes.ARMOR, 3.0) // CreepingHorror_stats.defense
                .add(Attributes.FOLLOW_RANGE, 16.0); // gold field_70174_ab = 10, scan 16
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Panic 1.35, MoveThroughVillage (skipped), WanderALot(10,1),
        // WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35));
        // gold EntityAIMoveThroughVillage skipped
        this.goalSelector.addGoal(3, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: if noDespawnRequired → false; else isDaytime
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.level().isDay();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold: if !noDespawnRequired and day phase (time%24000 <= 11000), 1/500 kill
        if (!this.isPersistenceRequired() && !this.level().isClientSide) {
            long t = this.level().getDayTime() % 24000L;
            if (t <= 11000L && this.random.nextInt(500) == 1) {
                this.discard();
            }
        }
    }

    public int mygetMaxHealth() {
        return 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold orespawn:creepinghorror_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:creepinghorror_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:creepinghorror_dead
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
        // gold getDropItem: nextInt(3) → rotten_flesh / bone / string
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        } else {
            this.spawnAtLocation(new ItemStack(Items.STRING));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code updateAITasks}: clear revenge 1/200; combat 1/5;
     * path 1.25; melee distSq &lt; 5; hit nextInt(12)==0 || nextInt(14)==1.
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
                this.getNavigation().moveTo(e, 1.25);
                // gold: distanceSq < 5.0; hit nextInt(12)==0 || nextInt(14)==1
                if (this.distanceToSqr(e) < 5.0
                        && (this.random.nextInt(12) == 0 || this.random.nextInt(14) == 1)) {
                    this.doHurtTarget(e);
                }
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
        // gold: skip self-type + several unported nightmare allies
        if (target instanceof CreepingHorror) {
            return false;
        }
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null; box expand(16, 4, 16)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 16.0, 4.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light, night only, y ≤ 15 (dim6 always OK — simplified).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        return !(this.getY() > 15.0);
    }
}
