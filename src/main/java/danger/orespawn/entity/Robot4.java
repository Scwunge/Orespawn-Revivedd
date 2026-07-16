package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Robot4} / Robo-Warrior (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.5×4.0, speed 0.34, health 170, attack 12, armor 18, XP 120.
 * Fire immune; shield + was-attacked i-frames; melee knockback + ranged LaserBall
 * (stand-in: SmallFireball until LaserBall ported).
 * Texture: {@code robot4.png}. Model drives shield arm / cannon anim via attacking flag.
 */
public class Robot4 extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Robot4.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> SHIELDING =
            SynchedEntityData.defineId(Robot4.class, EntityDataSerializers.BYTE);

    /** Gold Robot4_stats defaults: health 170, attack 12, defense 18. */
    private static final int HEALTH = 170;
    private static final int ATTACK = 12;
    private static final int DEFENSE = 18;

    private final float moveSpeed = 0.34F;
    private int reloadTicker;
    private int wasAttackedTicker;
    private RenderInfo renderdata = new RenderInfo();

    public Robot4(EntityType<? extends Robot4> type, Level level) {
        super(type, level);
        this.xpReward = 120; // gold field_70728_aV
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

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
                .add(Attributes.MOVEMENT_SPEED, 0.34)
                .add(Attributes.ATTACK_DAMAGE, ATTACK)
                .add(Attributes.ARMOR, DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 120.0); // gold field_70174_ab = 120
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1), MoveThroughVillage(0.9,false) skipped,
        // WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(SHIELDING, (byte) 0);
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

    /** Gold field_70178_ae isImmuneToFire. */
    @Override
    public boolean fireImmune() {
        return true;
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

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getRobot4Health() {
        return (int) this.getHealth();
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    public int getShielding() {
        return this.entityData.get(SHIELDING);
    }

    public void setShielding(int value) {
        this.entityData.set(SHIELDING, (byte) value);
    }

    /** Gold jump +0.25 Y. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
    }

    /**
     * Gold {@code onLivingUpdate}: client smoke trail + reddust when attacking.
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            if (this.random.nextInt(3) == 1) {
                double yaw = Math.toRadians(this.getYRot() + 180.0F);
                this.level().addParticle(
                        ParticleTypes.SMOKE,
                        this.getX() - 1.25 * Math.sin(yaw),
                        this.getY() + 3.0 + this.level().random.nextFloat(),
                        this.getZ() + 1.25 * Math.cos(yaw),
                        0.0,
                        this.level().random.nextFloat() / 2.0,
                        0.0);
            }
            if (this.getAttacking() != 0) {
                double yaw = Math.toRadians(this.getYRot() + 35.0F);
                this.level().addParticle(
                        DustParticleOptions.REDSTONE,
                        this.getX() - 1.55 * Math.sin(yaw),
                        this.getY() + 2.25 + this.level().random.nextFloat(),
                        this.getZ() + 1.55 * Math.cos(yaw),
                        0.0,
                        this.level().random.nextFloat(),
                        0.0);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: 1/4 chance orespawn:robot_living
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:robot_hurt
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:robot_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    /** Gold {@code dropItemRand}: scatter x/z ±(0.1)-(0.1), y+1. */
    private ItemStack dropItemRand(Item index, int count) {
        ItemStack stack = new ItemStack(index, count);
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(2) - this.random.nextInt(2),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(2) - this.random.nextInt(2),
                stack);
        this.level().addFreshEntity(item);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 5+rand10 × MyLaserBall×4, MyRayGun×1, field_151160_bD×1, then junk
        int var5 = 5 + this.random.nextInt(10);
        for (int var4 = 0; var4 < var5; var4++) {
            this.dropItemRand(ModItems.LASER_BALL.get(), 4);
        }
        this.dropItemRand(ModItems.RAY_GUN.get(), 1);
        this.dropItemRand(Items.IRON_INGOT, 1); // gold field_151160_bD scrap stand-in
        int i = 10 + this.random.nextInt(15);
        for (int n = 0; n < i; n++) {
            switch (this.random.nextInt(15)) {
                case 0 -> this.dropItemRand(Items.GOLD_NUGGET, 1);
                case 1 -> this.dropItemRand(Items.GOLD_INGOT, 1);
                case 2 -> this.dropItemRand(Items.NETHER_STAR, 1);
                case 3, 8 -> this.dropItemRand(Items.REDSTONE_BLOCK, 1);
                case 4 -> this.dropItemRand(Items.DISPENSER, 1);
                case 5 -> this.dropItemRand(Items.JUKEBOX, 1);
                case 6 -> this.dropItemRand(Items.PISTON, 1);
                case 7 -> this.dropItemRand(Items.STICKY_PISTON, 1);
                case 9 -> this.dropItemRand(Items.TNT, 1);
                default -> {
                }
            }
        }
    }

    /**
     * Gold {@code attackEntityAsMob}: knockback ks=2.0, inair=0.12 (×2 player/dead) then super.
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (target instanceof LivingEntity) {
            double ks = 2.0;
            double inair = 0.12;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code updateAITasks}: reload/was_attacked timers; 1/8 when reload ready → find target,
     * melee if close, else facing-cone LaserBall (SmallFireball stand-in), path 0.75.
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();
        if (this.reloadTicker > 0) {
            this.reloadTicker--;
        }
        if (this.wasAttackedTicker > 0) {
            this.wasAttackedTicker--;
        }
        if (this.reloadTicker == 0 && this.random.nextInt(8) == 1) {
            if (this.random.nextInt(50) == 1) {
                this.setTarget(null);
            }
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
                if (this.distanceToSqr(e) < 256.0) {
                    if (GoldStyleCombat.inMeleeRange(this, e, 3.0)) {
                        this.doHurtTarget(e);
                    } else {
                        double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                        double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                        double pi = Math.PI;
                        double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                        if (rdd > pi) {
                            rdd -= pi * 2.0;
                        }
                        rdd = Math.abs(rdd);
                        if (rdd < 0.5) {
                            this.fireLaserStandIn(e);
                        }
                        this.setAttacking(1);
                    }
                    this.getNavigation().moveTo(e, 0.75);
                }
            }
        }
        if (this.reloadTicker <= 0 && this.wasAttackedTicker <= 0) {
            this.setAttacking(0);
        }
    }

    /**
     * Gold LaserBall shot from shoulder offset; special + longer reload when distSq &gt; 65.
     * LaserBall not ported — SmallFireball stand-in (same spawn offsets / reload / launch SFX).
     */
    private void fireLaserStandIn(LivingEntity e) {
        double yoff = 2.0;
        double xzoff = 1.75;
        double sx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot() + 45.0F));
        double sy = this.getY() + yoff;
        double sz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot() + 45.0F));
        double var3 = e.getX() - sx;
        double var5 = e.getY() - sy;
        double var7 = e.getZ() - sz;
        float var9 = Mth.sqrt((float) (var3 * var3 + var7 * var7)) * 0.2F;
        Vec3 dir = new Vec3(var3, var5 + var9, var7);
        // gold LaserBall.shoot(…, 2.0F, 4.0F); setSpecial when distSq > 65
        SmallFireball ball = new SmallFireball(this.level(), this, dir.normalize());
        ball.setPos(sx, sy, sz);
        if (this.distanceToSqr(e) > 65.0) {
            // gold setSpecial + reload 30 + fireworks.launch 3.5 / 0.5
            this.reloadTicker = 30;
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 3.5F, 0.5F);
        } else {
            this.reloadTicker = 10;
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 2.5F, 1.0F);
        }
        this.level().addFreshEntity(ball);
    }

    /**
     * Gold hurt: ignore cactus; if shielding or was_attacked_ticker &gt; 0 block; else take damage,
     * set was_attacked=65, attacking=1, target living attacker, path 1.2.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        if (this.getShielding() != 0 || this.wasAttackedTicker != 0) {
            return false;
        }
        this.wasAttackedTicker = 65;
        this.setAttacking(1);
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
            return true;
        }
        return ret;
    }

    /** Gold: not EntityMob, not creative; MyUtils.isIgnoreable deferred. */
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

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(16, 4, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 4.0, this::isSuitableTarget);
    }

    /**
     * Gold getCanSpawnHere: spawner "Robo-Warrior"; else y≥50, night, clear headroom, light.
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
                            return true;
                        }
                    }
                }
            }
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        for (int dz = -1; dz < 1; dz++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 1; dy < 6; dy++) {
                    BlockState bid = level.getBlockState(origin.offset(dx, dy, dz));
                    if (!bid.isAir() && !bid.is(Blocks.SHORT_GRASS) && !bid.is(Blocks.TALL_GRASS)) {
                        return false;
                    }
                }
            }
        }
        return super.checkSpawnRules(level, spawnType);
    }
}
