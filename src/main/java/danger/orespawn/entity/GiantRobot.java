package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code GiantRobot} / Jeffery / Robo-Gunner (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.0×9.75, speed 0.55, health 550, attack 40, armor 18, XP 275.
 * Fire immune; facing-cone LaserBall (SmallFireball stand-in) + heavy melee knockback.
 * Texture: {@code textures/entity/giantrobottexture.png}.
 * Model drives dual-leg / dual-arm IK via {@link RenderGiantRobotInfo}.
 * Registry size/attrs set in {@code ModEntities}.
 */
public class GiantRobot extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(GiantRobot.class, EntityDataSerializers.BYTE);

    /** Gold Jeffery_stats defaults: health 550, attack 40, defense 18. */
    public static final float GOLD_WIDTH = 3.0F;
    public static final float GOLD_HEIGHT = 9.75F;
    public static final double GOLD_HEALTH = 550.0;
    public static final double GOLD_SPEED = 0.55;
    public static final double GOLD_ATTACK = 40.0;
    public static final double GOLD_ARMOR = 18.0;
    public static final int GOLD_XP = 275; // health / 2
    public static final double GOLD_FOLLOW = 40.0;

    private final float moveSpeed = 0.55F;
    private int reloadTicker;
    private RenderGiantRobotInfo renderdata = new RenderGiantRobotInfo();

    public GiantRobot(EntityType<? extends GiantRobot> type, Level level) {
        super(type, level);
        this.xpReward = GOLD_XP;
        this.renderdata = new RenderGiantRobotInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.ARMOR, GOLD_ARMOR)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1), MoveThroughVillage skipped,
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
        this.initLegData();
    }

    private void initLegData() {
        if (this.renderdata == null) {
            this.renderdata = new RenderGiantRobotInfo();
        }
        this.renderdata.hipydisplayangle = 0.0F;
        this.renderdata.hipxdisplayangle = 0.0F;
        this.renderdata.gpcounter = 2000000;
        this.renderdata.thighdisplayangle[0] = 0.0F;
        this.renderdata.thighdisplayangle[1] = 0.0F;
        this.renderdata.shindisplayangle[0] = 0.0F;
        this.renderdata.shindisplayangle[1] = 0.0F;
    }

    public RenderGiantRobotInfo getRenderGiantRobotInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderGiantRobotInfo();
            this.initLegData();
        }
        return this.renderdata;
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
        return (int) GOLD_HEALTH;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /** Gold jump +0.25 Y. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
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
        // gold: 15+rand15 × MyLaserBall×4, then 10+rand10 junk (kits/raygun)
        int var5 = 15 + this.random.nextInt(15);
        for (int var4 = 0; var4 < var5; var4++) {
            this.dropItemRand(ModItems.LASER_BALL.get(), 4);
        }
        int i = 10 + this.random.nextInt(10);
        for (int n = 0; n < i; n++) {
            switch (this.random.nextInt(12)) {
                case 0 -> this.dropItemRand(ModItems.SPIDER_ROBOT_KIT.get(), 1);
                case 1 -> this.dropItemRand(ModItems.ANT_ROBOT_KIT.get(), 1);
                case 2 -> this.dropItemRand(ModItems.RAY_GUN.get(), 1);
                case 3 -> this.dropItemRand(Items.REDSTONE_BLOCK, 1);
                case 4 -> this.dropItemRand(Items.DISPENSER, 1);
                case 5 -> this.dropItemRand(Items.FURNACE, 1);
                case 6 -> this.dropItemRand(Items.PISTON, 1);
                case 7 -> this.dropItemRand(Items.STICKY_PISTON, 1);
                case 8 -> this.dropItemRand(Items.IRON_BLOCK, 1);
                case 9 -> this.dropItemRand(Items.TNT, 1);
                default -> {
                }
            }
        }
    }

    /**
     * Gold {@code attackEntityAsMob}: super first, then knockback ks=2.2, inair=0.25 (×2 player/dead).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 2.2;
            double inair = 0.25;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    /**
     * Gold {@code updateAITasks}: 1/5 tick combat; facing-cone LaserBall + melee within 8+width/2.
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
        if (this.random.nextInt(5) == 0) {
            if (this.random.nextInt(100) == 1) {
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
                    double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                    double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                    double pi = Math.PI;
                    double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                    if (rdd > pi) {
                        rdd -= pi * 2.0;
                    }
                    rdd = Math.abs(rdd);
                    if (rdd < 0.5) {
                        if (this.reloadTicker == 0) {
                            this.fireLaserStandIn(e);
                        }
                        double reach = 8.0 + e.getBbWidth() / 2.0;
                        if (this.distanceToSqr(e) < reach * reach) {
                            this.setAttacking(1);
                            this.doHurtTarget(e);
                        } else {
                            this.setAttacking(0);
                        }
                    }
                    this.getNavigation().moveTo(e, 0.5);
                } else {
                    this.setAttacking(0);
                }
            } else {
                this.setAttacking(0);
            }
        }
    }

    /**
     * Gold LaserBall from head offset yoff=10, xzoff=3.75; special + longer reload when distSq &gt; 100.
     * LaserBall not ported — SmallFireball stand-in (same spawn offsets / reload / launch SFX).
     */
    private void fireLaserStandIn(LivingEntity e) {
        double yoff = 10.0;
        double xzoff = 3.75;
        double sx = this.getX() - xzoff * Math.sin(Math.toRadians(this.getYRot()));
        double sy = this.getY() + yoff;
        double sz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.getYRot()));
        double var3 = e.getX() - sx;
        double var5 = e.getY() - sy;
        double var7 = e.getZ() - sz;
        float var9 = Mth.sqrt((float) (var3 * var3 + var7 * var7)) * 0.2F;
        Vec3 dir = new Vec3(var3, var5 + var9, var7);
        SmallFireball ball = new SmallFireball(this.level(), this, dir.normalize());
        ball.setPos(sx, sy, sz);
        if (this.distanceToSqr(e) > 100.0) {
            // gold setSpecial + reload 25 + fireworks.launch 3.5 / 0.5
            this.reloadTicker = 25;
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 3.5F, 0.5F);
        } else {
            this.reloadTicker = 10;
            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 2.5F, 1.0F);
        }
        this.level().addFreshEntity(ball);
    }

    /** Gold hurt: ignore cactus; set target + revenge on living attacker. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = false;
        if (!"cactus".equals(source.getMsgId())) {
            ret = super.hurt(source, amount);
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.setLastHurtByMob(living);
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
        // gold box expand(16, 12, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 12.0, this::isSuitableTarget);
    }

    /**
     * Gold getCanSpawnHere: y≥50, night, clear headroom, light.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        BlockPos origin = this.blockPosition();
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

    /**
     * Gold {@code RenderGiantRobotInfo} — dual-leg hip/thigh/shin display angles for model.
     */
    public static class RenderGiantRobotInfo {
        public volatile float hipydisplayangle;
        public volatile float hipxdisplayangle;
        public volatile float[] thighdisplayangle = new float[2];
        public volatile float[] shindisplayangle = new float[2];
        public int gpcounter;
    }
}
