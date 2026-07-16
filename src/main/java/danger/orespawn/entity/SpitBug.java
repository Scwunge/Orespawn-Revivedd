package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code SpitBug} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.0×2.0, speed 0.33, health 100, attack 12, armor 10, XP 50, follow 75.
 * Melee knockback + ranged water-cannon (Acid deferred); jump-at-target; hurt i-frames 15.
 * Model uses {@link #getAttacking()} for jaw anim; texture BlisterBug → blisterbug.png.
 */
public class SpitBug extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(SpitBug.class, EntityDataSerializers.BYTE);

    /** Gold SpitBug_stats defaults: health 100, defense 10, attack 12. */
    private static final int HEALTH = 100;
    private static final int ATTACK = 12;
    private static final int DEFENSE = 10;

    private final float moveSpeed = 0.33F;
    private int hurtTimer;
    private int streamCount;
    private RenderInfo renderdata = new RenderInfo();

    public SpitBug(EntityType<? extends SpitBug> type, Level level) {
        super(type, level);
        this.xpReward = 50;
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    /** Gold {@code setRenderInfo} — copy fields (model may mutate then write back). */
    public void setRenderInfo(RenderInfo r) {
        if (r == null) {
            return;
        }
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
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
                .add(Attributes.MAX_HEALTH, HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, ATTACK)
                .add(Attributes.ARMOR, DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 75.0); // gold field_70174_ab = 75; scan box 12×7×12
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(0.9,false) skipped, WanderALot(14,1),
        // WatchClosest(Player,10), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
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
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold: when airborne, clear path
        if (this.isInAir()) {
            this.getNavigation().stop();
        }
    }

    /** Gold airborne flag used for path clear / jump-at checks. */
    private boolean isInAir() {
        return !this.onGround();
    }

    /**
     * Gold {@code jump}: motionY += 0.75, posY += 0.75, random horizontal burst by yaw.
     */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.75, 0.0));
        this.setPos(this.getX(), this.getY() + 0.75, this.getZ());
        float f = 0.2F + Math.abs(this.random.nextFloat() * 0.45F);
        double yawRad = Math.toRadians(this.yBodyRot);
        this.setDeltaMovement(
                this.getDeltaMovement()
                        .add(-f * Math.sin(yawRad), 0.0, f * Math.cos(yawRad)));
    }

    /** Gold {@code jumpAtEntity}: leap toward target. */
    protected void jumpAtEntity(LivingEntity e) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.75, 0.0));
        this.setPos(this.getX(), this.getY() + 0.75, this.getZ());
        float f = 0.2F + Math.abs(this.random.nextFloat() * 0.25F);
        float d = (float) Math.atan2(e.getX() - this.getX(), e.getZ() - this.getZ());
        this.setDeltaMovement(
                this.getDeltaMovement().add(f * Math.sin(d), 0.0, f * Math.cos(d)));
    }

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getSpitBugHealth() {
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
        //  gold orespawn:clatter 1/4
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:crunch
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:emperorscorpion_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        return 1.5F;
    }

    /** Gold {@code dropItemRand}: scatter x/z ±(0.2)-(0.2), y+1. */
    private ItemStack dropItemRand(Item index, int count) {
        ItemStack stack = new ItemStack(index, count);
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(3) - this.random.nextInt(3),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(3) - this.random.nextInt(3),
                stack);
        this.level().addFreshEntity(item);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: nextInt(10) → iron_ingot / uranium_nugget / titanium_nugget / null
        int i = this.random.nextInt(10);
        if (i == 0) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        } else if (i == 1) {
            this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        } else if (i == 2) {
            this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
        // gold dropFewItems: 1+rand(3) amethyst
        int n = 1 + this.random.nextInt(3);
        for (int k = 0; k < n; k++) {
            this.dropItemRand(ModItems.AMETHYST.get(), 1);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 0.5;
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
        if (this.hurtTimer > 0) {
            return false;
        }
        boolean ret = false;
        // gold ignores cactus + fall
        if (!"cactus".equals(source.getMsgId()) && !"fall".equals(source.getMsgId())) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 15;
            Entity e = source.getEntity();
            if (e instanceof LivingEntity living) {
                this.setTarget(living);
                this.setLastHurtByMob(living);
                this.getNavigation().moveTo(living, 1.2);
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Gold {@code updateAITasks}: hurt timer; combat 1/5; look; jump-at 1/15 if grounded;
     * melee distSq &lt; 9 (hit nextInt(6)==0 || nextInt(7)==1); else path 0.5 + watercanon;
     * heal 1 on 1/150 when damaged.
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
            LivingEntity e = this.getTarget();
            if (e != null && !e.isAlive()) {
                this.setTarget(null);
                e = null;
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (this.random.nextInt(15) == 1 && this.onGround()) {
                    this.jumpAtEntity(e);
                } else if (this.distanceToSqr(e) < 9.0) {
                    this.setAttacking(1);
                    if (this.random.nextInt(6) == 0 || this.random.nextInt(7) == 1) {
                        this.doHurtTarget(e);
                        // gold clatter on target 2/3 of hits
                        if (!this.level().isClientSide && this.random.nextInt(3) != 1) {
                            // deferred: orespawn:clatter
                        }
                    }
                } else if (this.onGround()) {
                    this.getNavigation().moveTo(e, 0.5);
                    this.watercanon(e);
                }
            } else {
                this.setAttacking(0);
            }
        }
        if (this.random.nextInt(150) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }
    }

    /**
     * Gold {@code watercanon}: streams Acid projectiles (stream_count 8).
     * Acid/LaserBall not ported — keep stream/attacking state for jaw anim; spawn deferred.
     */
    private void watercanon(LivingEntity e) {
        if (this.streamCount > 0) {
            this.setAttacking(1);
            // deferred: Acid projectile aim + random.bow sound
            // still play a soft bow-like cue so ranged intent is audible
            this.playSound(
                    SoundEvents.ARROW_SHOOT,
                    0.75F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.streamCount--;
        } else {
            this.setAttacking(0);
        }
        if (this.streamCount <= 0 && this.random.nextInt(7) == 1) {
            this.streamCount = 8;
        }
    }

    /**
     * Gold {@code isSuitableTarget}: skip ignoreables, Ender*, Enderman, Hydrolisc, Creeper,
     * SpitBug, TrooperBug, creative players.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (target instanceof EnderReaper) {
            return false;
        }
        if (target instanceof EnderKnight) {
            return false;
        }
        if (target instanceof EnderMan) {
            return false;
        }
        if (target instanceof Hydrolisc) {
            return false;
        }
        if (target instanceof Creeper) {
            return false;
        }
        if (target instanceof SpitBug) {
            return false;
        }
        // TrooperBug may be concurrent wave — avoid hard class dep if absent
        if (target.getClass().getSimpleName().equals("TrooperBug")) {
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
        // gold box expand(12, 7, 12); GenericTargetSorter nearest first
        List<LivingEntity> list =
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.0, 7.0, 12.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name "Spit Bug" OK; else night-ish,
     * valid monster light, clear headroom (k/j −2.1, i 1.3 air).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos origin = this.blockPosition();
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (level.getBlockState(p).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // best-effort: allow near any spawner (name string API differs)
                            // gold checked getEntityIdName equals "Spit Bug"
                            return true;
                        }
                    }
                }
            }
        }
        // gold: if daytime and nextInt(20) > 1 → false (~10% day spawns)
        if (level instanceof Level lvl && lvl.isDay() && this.random.nextInt(20) > 1) {
            return false;
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        for (int k = -2; k < 2; k++) {
            for (int j = -2; j < 2; j++) {
                for (int i = 1; i < 4; i++) {
                    BlockState bid = level.getBlockState(origin.offset(j, i, k));
                    if (!bid.isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
