package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
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
 * Gold {@code Pointysaurus} 1:1 stats/AI skeleton for NeoForge 1.21.1.
 * Size 2.9×2.9, speed 0.35, health 40, attack 15, armor 10, XP 40.
 * Targets players only (not other monsters).
 */
public class Pointysaurus extends Monster {
    private final float moveSpeed = 0.35F;
    /** Last living entity that damaged us (gold {@code rt}). */
    @Nullable
    private LivingEntity revengeTargetLiving = null;

    public Pointysaurus(EntityType<? extends Pointysaurus> type, Level level) {
        super(type, level);
        this.xpReward = 40;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.ARMOR, 10.0) // func_70658_aO
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
        return 40;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0
                ? danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_LIVING.get()
                : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.9F;
    }

    @Override
    public float getVoicePitch() {
        return 1.5F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 10 bone, 6 beef, 6 rotten_flesh, 6 string
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        }
        for (int i = 0; i < 6; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
        for (int i = 0; i < 6; i++) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
        }
        for (int i = 0; i < 6; i++) {
            this.spawnAtLocation(new ItemStack(Items.STRING));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // Gold attackEntityAsMob always applied damage; 1.21 super.doHurtTarget can no-op without MeleeAttackGoal ticks.
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 0.8;
            double inair = 0.1;
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
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        // gold: getImmediateSource (func_76364_f)
        Entity e = source.getDirectEntity();
        if (e instanceof LivingEntity living) {
            this.revengeTargetLiving = living;
        }
        return ret;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold nextInt(6) == 0
        if (this.random.nextInt(6) != 0) {
            return;
        }
        LivingEntity e = this.revengeTargetLiving;
        // gold: PlayNicely != 0 clears preferred revenge target
        if (OreSpawnMain.PlayNicely != 0) {
            e = null;
        }
        if (e != null) {
            if (!e.isAlive() || this.random.nextInt(250) == 1) {
                e = null;
                this.revengeTargetLiving = null;
            }
            if (e != null && !this.hasLineOfSight(e)) {
                e = null;
            }
        }
        if (e == null) {
            e = this.findSomethingToAttack();
        }
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            // gold: distanceSq < (4.0F + e.width/2)^2
            if (GoldStyleCombat.inMeleeRange(this, e, 4.0)) {
                this.setAttacking(1);
                // gold: nextInt(5)==0 || nextInt(6)==1
                if (this.random.nextInt(5) == 0 || this.random.nextInt(6) == 1) {
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
        if (target instanceof Pointysaurus) {
            return false;
        }
        // gold: skip EntityMob (other monsters) — only attack players
        if (target instanceof Monster) {
            return false;
        }
        // gold: EntitySenses.canSee — required always
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null; box expand(12, 5, 12)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 12.0, 5.0, this::isSuitableTarget);
    }

    /** Gold always returns 1 (stub). */
    public int getAttacking() {
        return 1;
    }

    public void setAttacking(int value) {
        // gold no-op
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light (via super), y ≥ 50, night, 2×2 air column y+1.y+5.
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
        return true;
    }
}
