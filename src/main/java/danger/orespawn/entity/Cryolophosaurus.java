package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
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
 * Gold {@code Cryolophosaurus} 1:1 stats/AI skeleton for NeoForge 1.21.1.
 * Size 0.75×0.75, speed 0.25, health 45, attack 8, armor 10, XP 10.
 */
public class Cryolophosaurus extends Monster {
    private final float moveSpeed = 0.25F;

    public Cryolophosaurus(EntityType<? extends Cryolophosaurus> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 45.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ARMOR, 10.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 18.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35));
        // gold: EntityAIMoveThroughVillage — skipped
        this.goalSelector.addGoal(3, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /** Gold {@code mygetMaxHealth()}. */
    public int mygetMaxHealth() {
        return 45;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(6) == 0 ? SoundsHandler.ENTITY_CRYO_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_CRYO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
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
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: 1/10 chicken, 1/10 uranium, 1/10 titanium, else nothing
        int i = this.random.nextInt(10);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.CHICKEN));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // Gold attackEntityAsMob always applied damage; 1.21 super.doHurtTarget can no-op without MeleeAttackGoal ticks.
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: clear attack target 1/200
        if (this.random.nextInt(200) == 1) {
            this.setTarget(null);
        }
        // gold nextInt(5) == 1
        if (this.random.nextInt(5) != 1) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.getNavigation().moveTo(e, 1.25);
            // gold distanceSq < 5.0; hit if nextInt(12)==0 || nextInt(14)==1
            if (this.distanceToSqr(e) < 5.0
                    && (this.random.nextInt(12) == 0 || this.random.nextInt(14) == 1)) {
                this.doHurtTarget(e);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold: EntitySenses.canSee — required always
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Alosaurus || target instanceof TRex || target instanceof Cryolophosaurus) {
            return false;
        }
        if (target instanceof Ant || target instanceof RedAnt) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
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
     * Gold {@code getCanSpawnHere}: valid light (via super) and (night OR y ≤ 50).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        // gold: !isDaytime || !(y > 50)
        if (level instanceof Level lvl && lvl.isDay() && this.getY() > 50.0) {
            return false;
        }
        return true;
    }
}
