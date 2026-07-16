package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Robot3} / Robo-Gunner (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.5Ã—5.0, speed 0.35, health 80, attack 16, armor 14, XP 60, follow 40.
 * Ranged laser (gold {@code LaserBall}; interim {@link SmallFireball} until LaserBall wave).
 * Texture: {@code robot3.png}. Model uses {@link RenderInfo} for arm swing state.
 */
public class Robot3 extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Robot3.class, EntityDataSerializers.BYTE);

    /** Gold Robot3_stats defaults: get_mobstats(., 80, 16, 14). */
    private static final int HEALTH = 80;
    private static final int ATTACK = 16;
    private static final int DEFENSE = 14;

    private final float moveSpeed = 0.35F;
    private int reloadTicker = 0;
    private RenderInfo renderdata = new RenderInfo();

    public Robot3(EntityType<? extends Robot3> type, Level level) {
        super(type, level);
        this.xpReward = 60;
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
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, ATTACK)
                .add(Attributes.ARMOR, DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 40.0); // gold field_70174_ab = 40
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1), MoveThroughVillage skipped, WatchClosest(Player,8), LookIdle, HurtByTarget
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

    /** Gold jump +0.25 Y. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
    }

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //   gold orespawn:robot_living multi-variant
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

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: iron_ingot
        this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        // gold: 5+rand6 of MyLaserBallÃ—4
        int var5 = 5 + this.random.nextInt(6);
        for (int var4 = 0; var4 < var5; var4++) {
            this.spawnAtLocation(new ItemStack(ModItems.LASER_BALL.get(), 4));
        }
        // gold: 5+rand10 junk cases (same table as Robot2)
        int i = 5 + this.random.nextInt(10);
        for (int k = 0; k < i; k++) {
            switch (this.random.nextInt(15)) {
                case 0 -> this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                case 1 -> this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT));
                case 2 -> this.spawnAtLocation(new ItemStack(Items.NETHER_STAR));
                case 3, 8 -> this.spawnAtLocation(new ItemStack(Blocks.REDSTONE_BLOCK));
                case 4 -> this.spawnAtLocation(new ItemStack(Blocks.DISPENSER));
                case 5 -> this.spawnAtLocation(new ItemStack(Blocks.JUKEBOX));
                case 6 -> this.spawnAtLocation(new ItemStack(Blocks.PISTON));
                case 7 -> this.spawnAtLocation(new ItemStack(Blocks.STICKY_PISTON));
                case 9 -> this.spawnAtLocation(new ItemStack(Blocks.TNT));
                default -> {
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code updateAITasks}: reload_ticker 35-cycle laser volley when target in facing cone
     * and distSq &lt; 256; path 0.5.
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();
        if (this.reloadTicker > 0) {
            this.reloadTicker--;
            if (this.reloadTicker < 25) {
                this.setAttacking(0);
            }
        }
        if (this.reloadTicker != 0) {
            return;
        }
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
        this.reloadTicker = 35;
        if (e != null) {
            this.getLookControl().setLookAt(e, 10.0F, 10.0F);
            if (this.distanceToSqr(e) < 256.0) {
                double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                double rhdir = Math.toRadians((this.yHeadRot + 90.0F) % 360.0F);
                double pi = Math.PI;
                double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                if (rdd > pi) {
                    rdd -= pi * 2.0;
                }
                rdd = Math.abs(rdd);
                if (rdd < 0.5) {
                    // gold LaserBall: yoff=3.0, xzoff=1.75, shoot 1.4/5.0; interim SmallFireball
                    double yoff = 3.0;
                    double xzoff = 1.75;
                    double yawRad = Math.toRadians(this.yHeadRot);
                    double sx = this.getX() - xzoff * Math.sin(yawRad);
                    double sy = this.getY() + yoff;
                    double sz = this.getZ() + xzoff * Math.cos(yawRad);
                    double dx = e.getX() - sx;
                    double dy = e.getY() - sy;
                    double dz = e.getZ() - sz;
                    float horiz = Mth.sqrt((float) (dx * dx + dz * dz)) * 0.2F;
                    Vec3 dir = new Vec3(dx, dy + horiz, dz);
                    if (dir.lengthSqr() > 1.0E-6) {
                        dir = dir.normalize();
                    }
                    SmallFireball ball = new SmallFireball(this.level(), this, dir);
                    ball.setPos(sx, sy, sz);
                    // gold: fireworks.launch volume 3 pitch 1
                    this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 3.0F, 1.0F);
                    this.level().addFreshEntity(ball);
                    this.setAttacking(1);
                }
                this.getNavigation().moveTo(e, 0.5);
            }
        } else {
            this.setAttacking(0);
        }
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
        // gold box expand(16, 3, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 3.0, this::isSuitableTarget);
    }

    /**
     * Gold getCanSpawnHere: yâ‰¥50, night, clear headroom air/tall grass, light.
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
}
