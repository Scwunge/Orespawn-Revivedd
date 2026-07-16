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
 * Gold {@code Nastysaurus} 1:1 stats/AI for NeoForge 1.21.1.
 * Size 2.2×4.6, speed 0.35, health 200, attack 32, armor 17, XP 40.
 */
public class Nastysaurus extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Nastysaurus.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.35F;
    /** Last living entity that damaged us (gold {@code rt}). */
    @Nullable
    private LivingEntity revengeTargetLiving = null;
    /** Gold client animation state for {@code ModelNastysaurus}. */
    private RenderInfo renderdata = new RenderInfo();

    public Nastysaurus(EntityType<? extends Nastysaurus> type, Level level) {
        super(type, level);
        this.xpReward = 40;
    }

    public RenderInfo getRenderInfo() {
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 32.0)
                .add(Attributes.ARMOR, 17.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMoveThroughVillage — skipped
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        this.renderdata.rf1 = 0.0F;
        this.renderdata.rf2 = 0.0F;
        this.renderdata.rf3 = 0.0F;
        this.renderdata.rf4 = 0.0F;
        this.renderdata.ri1 = 0;
        this.renderdata.ri2 = 0;
        this.renderdata.ri3 = 0;
        this.renderdata.ri4 = 0;
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
        return 200;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 1/4 Alosaurus living (no dedicated nasty ambient)
        return this.random.nextInt(4) == 0 ? SoundsHandler.ENTITY_ALOSAURUS_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold has no death override; alosaurus death is the closest wired asset
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
        // gold: 10 iron, 10 rotten_flesh, 10 bone, 10 string
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        }
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
        }
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(new ItemStack(Items.BONE));
        }
        for (int i = 0; i < 10; i++) {
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

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold: ignore cactus
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        // gold: getTrueSource (func_76346_g)
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.revengeTargetLiving = living;
        }
        return ret;
    }

    /**
     * Gold {@code updateAITasks} combat.
     * nextInt(5)==0; prefer rt (PlayNicely=0); clear dead/1/250/no LOS; scan 32×8×32; reach 4.5+w/2;
     * hit nextInt(4)==0||nextInt(5)==1; path 1.25.
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
        // gold: PlayNicely != 0 clears preferred revenge target
        LivingEntity e = this.revengeTargetLiving;
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
            // gold: distanceSq < (4.5F + e.width/2)^2
            if (GoldStyleCombat.inMeleeRange(this, e, 4.5)) {
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
        if (target instanceof Nastysaurus || target instanceof Cryolophosaurus) {
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
        // gold: PlayNicely != 0 → null; box expand(32, 8, 32)
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        return GoldStyleCombat.findTarget(this, 32.0, 8.0, this::isSuitableTarget);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light (via super), y ≥ 50, night,
     * air column y+1.y+5 at offset (-1,-1), no other Nastysaurus in 16×8×16.
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
        // gold: getClosestEntity(Nastysaurus, expand 16,8,16) == null
        return this.level()
                .getEntitiesOfClass(Nastysaurus.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
