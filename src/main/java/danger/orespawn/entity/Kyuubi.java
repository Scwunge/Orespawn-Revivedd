package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Kyuubi} (EntityMob) 1:1 combat/AI for NeoForge 1.21.1.
 * Size 0.5×1.25, speed 0.25, health 125, attack attr 10, melee strength 3, armor 10, XP 30.
 * Fire-themed ranged attacker ({@link SmallFireball}); no MeleeAttackGoal.
 */
public class Kyuubi extends Monster {
    private final float moveSpeed = 0.25F;

    public Kyuubi(EntityType<? extends Kyuubi> type, Level level) {
        super(type, level);
        this.xpReward = 30;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 125.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ARMOR, 10.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35));
        // gold: EntityAIMoveThroughVillage — skipped (no village path helper; Alien pattern)
        // gold: EntityAIWander — WaterAvoidingRandomStrollGoal
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        // gold: no MeleeAttackGoal — combat is customServerAiStep fireball
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    /**
     * Gold {@code onLivingUpdate}: 1/10 REDSTONE+LAVA particles, setFire(5);
     * if in water, self-attack + smoke particles.
     */
    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (this.random.nextInt(10) != 1) {
            return;
        }
        if (this.level().isClientSide) {
            // gold: EnumParticleTypes.REDSTONE + LAVA at y+2
            this.level().addParticle(
                    DustParticleOptions.REDSTONE, this.getX(), this.getY() + 2.0, this.getZ(), 0.0, 0.0, 0.0);
            this.level().addParticle(
                    ParticleTypes.LAVA, this.getX(), this.getY() + 2.0, this.getZ(), 0.0, 0.0, 0.0);
            if (this.isInWater()) {
                // gold: SMOKE_NORMAL + SMOKE_LARGE at y+1.75 and y+2
                this.level().addParticle(
                        ParticleTypes.SMOKE, this.getX(), this.getY() + 1.75, this.getZ(), 0.0, 0.0, 0.0);
                this.level().addParticle(
                        ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.75, this.getZ(), 0.0, 0.0, 0.0);
                this.level().addParticle(
                        ParticleTypes.SMOKE, this.getX(), this.getY() + 2.0, this.getZ(), 0.0, 0.0, 0.0);
                this.level().addParticle(
                        ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 2.0, this.getZ(), 0.0, 0.0, 0.0);
            }
        } else {
            // gold: setFire(5); if in water, attackEntityAsMob(self)
            this.igniteForSeconds(5);
            if (this.isInWater()) {
                this.doHurtTarget(this);
            }
        }
    }

    public int mygetMaxHealth() {
        return 125;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ENTITY_KYUUBI_LIVING.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
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
        // gold dropFewItems: 10 coal, 3 redstone_block, 4 quartz_block
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.COAL));
        }
        for (int i = 0; i < 3; i++) {
            this.spawnAtLocation(new ItemStack(Blocks.REDSTONE_BLOCK));
        }
        for (int i = 0; i < 4; i++) {
            this.spawnAtLocation(new ItemStack(Blocks.QUARTZ_BLOCK));
        }
        // gold getDropItem: 1/6 gold_nugget, 1/6 uranium, 1/6 titanium, else null
        int r = this.random.nextInt(6);
        if (r == 0) {
            this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
        } else if (r == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (r == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold getAttackStrength = 3 (not attribute 10)
        return target.hurt(this.damageSources().mobAttack(this), 3.0F);
    }

    /**
     * Gold {@code setAttackTarget}: alive, canSee, not EntityMob/PigZombie, not creative player.
     * PigZombie was EntityMob in gold (dead branch); Monster covers zombified piglin etc.
     */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null && target != this && target.isAlive() && this.hasLineOfSight(target)) {
            if (target instanceof Monster) {
                return;
            }
            if (target instanceof Player player && player.getAbilities().instabuild) {
                return;
            }
            super.setTarget(target);
        } else if (target == null) {
            super.setTarget(null);
        }
    }

    /**
     * Gold {@code updateAITasks}: clear revenge 1/200; 1/10 scan → look, path 1.25,
     * SmallFireball if distSq &lt; 64 and (1/6 or 1/8).
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: setRevengeTarget(null) 1/200
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
        // gold: nextInt(10) == 1
        if (this.random.nextInt(10) != 1) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            this.getNavigation().moveTo(e, 1.25);
            // gold: distanceSq < 64 && (nextInt(6)==0 || nextInt(8)==1)
            if (this.distanceToSqr(e) < 64.0
                    && (this.random.nextInt(6) == 0 || this.random.nextInt(8) == 1)) {
                Vec3 dir = new Vec3(
                        e.getX() - this.getX(),
                        e.getY() + 0.75 - (this.getY() + 1.25),
                        e.getZ() - this.getZ());
                SmallFireball ball = new SmallFireball(this.level(), this, dir.normalize());
                ball.setPos(this.getX(), this.getY() + 1.25, this.getZ());
                this.playSound(
                        SoundEvents.BLAZE_SHOOT,
                        0.75F,
                        1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
                this.level().addFreshEntity(ball);
            }
        }
    }

    /**
     * Gold {@code isSuitableTarget}: alive, canSee, not EntityMob, not PigZombie, not creative.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Monster) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return true;
    }

    /**
     * Gold {@code findSomethingToAttack}: PlayNicely != 0 → null;
     * expand(12, 4, 12) + GenericTargetSorter → {@link GoldStyleCombat#findTarget}.
     */
    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 12.0, 4.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: always true (nether biome weights in datapack).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return true;
    }
}
