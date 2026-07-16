package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Basilisk} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 1.6×3.5, speed 0.4, health 200, attack 24, armor 15, XP 150.
 * Fire immune; hurt i-frames 30; poison melee + nearby slowness; jaw anim via attacking flag.
 */
public class Basilisk extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Basilisk.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.4F;
    private int hurtTimer;

    public Basilisk(EntityType<? extends Basilisk> type, Level level) {
        super(type, level);
        this.xpReward = 150;
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold Basilisk_stats default: health 200, attack 24, defense 15
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 24.0)
                .add(Attributes.ARMOR, 15.0)
                .add(Attributes.FOLLOW_RANGE, 32.0); // gold field_70174_ab = 2000; combat scan 24
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage (skipped), WanderALot(20,1), WatchClosest, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 20, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    /** Gold field_70178_ae isImmuneToFire. */
    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /** Gold livingUpdate: 1/200 heal 1. */
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isDeadOrDying() && this.random.nextInt(200) == 0) {
            this.heal(1.0F);
        }
    }

    /** Gold jump: motionY += 0.25. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
    }

    public int mygetMaxHealth() {
        return 200;
    }

    public int getBasiliskHealth() {
        return (int) this.getHealth();
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 50% orespawn:basilisk_living
        return this.random.nextInt(2) == 0 ? SoundsHandler.ENTITY_BASILISK_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:alo_hurt
        return SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:emperorscorpion_death
        return SoundsHandler.ENTITY_EMPERORSCORPION_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: BasiliskScale, item_frame, 12+r6 emeralds, 8+r5 bones, random emerald gear
        this.spawnAtLocation(new ItemStack(ModItems.BASILISK_SCALE.get()));
        this.spawnAtLocation(new ItemStack(Items.ITEM_FRAME));
        int emeralds = 12 + this.random.nextInt(6);
        for (int i = 0; i < emeralds; i++) {
            this.spawnAtLocation(new ItemStack(Items.EMERALD));
        }
        int bones = 8 + this.random.nextInt(5);
        for (int i = 0; i < bones; i++) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        }
        int rolls = 3 + this.random.nextInt(5);
        for (int i = 0; i < rolls; i++) {
            int pick = this.random.nextInt(15);
            switch (pick) {
                case 1 -> this.spawnAtLocation(new ItemStack(Items.EMERALD));
                case 2 -> this.spawnAtLocation(new ItemStack(Blocks.EMERALD_BLOCK));
                case 3 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_SWORD.get()));
                case 4 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_SHOVEL.get()));
                case 5 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_PICKAXE.get()));
                case 6 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_AXE.get()));
                case 7 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_HOE.get()));
                case 8 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_HELMET.get()));
                case 9 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_CHESTPLATE.get()));
                case 10 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_LEGGINGS.get()));
                case 11 -> this.spawnAtLocation(new ItemStack(ModItems.EMERALD_BOOTS.get()));
                default -> {
                    // gold switch fallthrough / empty cases → no drop
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // gold interact returns false always
        return InteractionResult.PASS;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity living) {
            // gold poison duration by difficulty: EASY 10, NORMAL 12, HARD 14 seconds (else 8)
            int sec = 8;
            Difficulty diff = this.level().getDifficulty();
            if (diff == Difficulty.EASY) {
                sec = 10;
            } else if (diff == Difficulty.NORMAL) {
                sec = 12;
            } else if (diff == Difficulty.HARD) {
                sec = 14;
            }
            if (this.random.nextInt(3) == 0) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, sec * 20, 0));
            }
            double ks = 1.5;
            double inair = 0.15;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold hurt_timer i-frames
        if (this.hurtTimer > 0) {
            return false;
        }
        this.hurtTimer = 30;
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code updateAITasks}: hurt timer; combat 1/5; melee (6+w/2)^2; hit nextInt(3)==0||nextInt(4)==1;
     * path 1.25; apply slowness to target; heal 1/75 when hurt.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }
        if (this.random.nextInt(5) == 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (GoldStyleCombat.inMeleeRange(this, e, 6.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(3) == 0 || this.random.nextInt(4) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.25);
                }
                // gold: always apply heavy slowness while targeting
                e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 5));
            } else {
                this.setAttacking(0);
            }
        }
        if (this.random.nextInt(75) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Basilisk || target instanceof LeafMonster) {
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
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(24, 7, 24)
        return GoldStyleCombat.findTarget(this, 24.0, 7.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name Basilisk OK; else dark, night, headroom clear, no peer in 20×6×20.
     * Spawner tile check simplified to super light + night + space + buddy.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        // gold: air headroom check 3×3×4 above feet
        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 2; j++) {
                for (int i = 1; i < 5; i++) {
                    if (!this.level()
                            .getBlockState(
                                    this.blockPosition().offset(j, i, k))
                            .isAir()) {
                        return false;
                    }
                }
            }
        }
        return this.level()
                .getEntitiesOfClass(Basilisk.class, this.getBoundingBox().inflate(20.0, 6.0, 20.0))
                .isEmpty();
    }
}
