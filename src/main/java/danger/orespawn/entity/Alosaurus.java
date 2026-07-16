package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Alosaurus} 1:1 stats/AI for NeoForge 1.21.1.
 * Size 1.9×3.6, speed 0.35, health 50, attack 18, armor 18, XP 40.
 */
public class Alosaurus extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Alosaurus.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.35F;

    public Alosaurus(EntityType<? extends Alosaurus> type, Level level) {
        super(type, level);
        this.xpReward = 40;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 18.0)
                .add(Attributes.ARMOR, 18.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMoveThroughVillage — skipped; wander covers roam
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
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
        return 50;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? SoundsHandler.ENTITY_ALOSAURUS_LIVING.get() : null;
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
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 10× iron_ingot, 6× beef
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        }
        for (int i = 0; i < 6; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // Gold attackEntityAsMob always applied damage; 1.21 super.doHurtTarget can no-op without MeleeAttackGoal ticks.
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 1.2;
            double inair = 0.1;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    /**
     * Gold {@code updateAITasks} combat (no revenge {@code rt} field on gold Alosaurus).
     * nextInt(5)==0; scan 12×5×12; reach 4+w/2; hit nextInt(4)==0||nextInt(5)==1; path 1.25.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold nextInt(5) == 0
        if (this.random.nextInt(5) != 0) {
            return;
        }
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            // gold: distanceSq < (4.0F + e.width/2)^2
            if (GoldStyleCombat.inMeleeRange(this, e, 4.0)) {
                this.setAttacking(1);
                if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                    this.doHurtTarget(e);
                }
            } else {
                this.getNavigation().moveTo(e, 1.25);
            }
        } else {
            this.setAttacking(0);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (target instanceof Alosaurus || target instanceof Cryolophosaurus) {
            return false;
        }
        // gold: EntitySenses.canSee — required always
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null; box expand(12, 5, 12)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 12.0, 5.0, this::isSuitableTarget);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light (via super), y ≥ 50, night,
     * air column y+1.y+5 at offset (-1,-1), no other Alosaurus in 16×8×16.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        int baseX = (int) this.getX();
        int baseY = (int) this.getY();
        int baseZ = (int) this.getZ();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        // gold: k/j from -1 to <1 (only offset -1,-1)
        for (int k = -1; k < 1; k++) {
            for (int j = -1; j < 1; j++) {
                for (int i = 1; i < 6; i++) {
                    cursor.set(baseX + j, baseY + i, baseZ + k);
                    if (!level.getBlockState(cursor).isAir()) {
                        return false;
                    }
                }
            }
        }
        // gold: getClosestEntity(Alosaurus, expand 16,8,16) == null
        return this.level()
                .getEntitiesOfClass(Alosaurus.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
