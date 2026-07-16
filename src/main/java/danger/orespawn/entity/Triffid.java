package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
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
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Triffid} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.0Ã—4.0, speed 0.13, health 100, attack 20, armor 12, XP 50, follow 75.
 * Closed-shell invulnerability (hurt_timer 300) while open petals enable combat.
 */
public class Triffid extends Monster {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(Triffid.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_OPEN_CLOSED =
            SynchedEntityData.defineId(Triffid.class, EntityDataSerializers.BYTE);

    /** Gold {@code get_mobstats("Triffid", 100, 20, 12)} â†’ health, attack, defense. */
    public static final int STAT_HEALTH = 100;
    public static final int STAT_ATTACK = 20;
    public static final int STAT_DEFENSE = 12;

    private final float moveSpeed = 0.13F;
    private int hurtTimer = 0;
    private RenderInfo renderdata = new RenderInfo();

    public Triffid(EntityType<? extends Triffid> type, Level level) {
        super(type, level);
        this.xpReward = 50;
        // Tall plant mesh
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, STAT_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.13)
                .add(Attributes.ATTACK_DAMAGE, STAT_ATTACK)
                .add(Attributes.ARMOR, STAT_DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 75.0);
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WatchClosest(player,10), LookIdle, HurtByTarget(false)
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_OPEN_CLOSED, (byte) 0);
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

    public final int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public final void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, (byte) value);
    }

    /** Gold datawatcher 21: 0 = closed petals, non-zero = open. */
    public final int getOpenClosed() {
        return this.entityData.get(DATA_OPEN_CLOSED);
    }

    public final void setOpenClosed(int value) {
        this.entityData.set(DATA_OPEN_CLOSED, (byte) value);
    }

    public int mygetMaxHealth() {
        return STAT_HEALTH;
    }

    public int getTriffidHealth() {
        return (int) this.getHealth();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    /** Gold {@code canBePushed} / func_70104_M â†’ false. */
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();

        // gold onUpdate: occasional path toward solid ground strip (Â±5 scan)
        if (this.random.nextInt(100) == 1) {
            int ix = (int) this.getX();
            int iz = (int) this.getZ();
            for (int k = -5; k <= 5; k++) {
                if (!this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - 1.0, this.getZ() + k)).isAir()) {
                    if (k < 0) {
                        iz--;
                    }
                    if (k > 0) {
                        iz++;
                    }
                }
            }
            for (int var7 = -5; var7 <= 5; var7++) {
                if (!this.level()
                        .getBlockState(BlockPos.containing(this.getX() + var7, this.getY() - 1.0, this.getZ()))
                        .isAir()) {
                    if (var7 < 0) {
                        ix--;
                    }
                    if (var7 > 0) {
                        ix++;
                    }
                }
            }
            this.getNavigation().moveTo(ix, this.getY(), iz, 1.0);
        }

        // gold: face nearest target while not in hurt shell
        if (this.hurtTimer <= 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                float yaw = (float) Math.toDegrees(Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX())) - 90.0F;
                while (yaw < 0.0F) {
                    yaw += 360.0F;
                }
                this.setYRot(yaw);
                this.yBodyRot = yaw;
                this.yHeadRot = yaw;
            }
        }
    }

    /**
     * Gold {@code onLivingUpdate}: freeze horizontal motion while hurt_timer &gt; 0 (server).
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.hurtTimer > 0) {
            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(0.0, v.y, 0.0);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold orespawn:triffid_living â€” SoundsHandler
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:triffid_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:triffid_dead
        return null;
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
        // gold getDropItem: 1/3 slime_ball
        if (this.random.nextInt(3) == 0) {
            this.spawnAtLocation(new ItemStack(Items.SLIME_BALL));
        }
        // gold dropFewItems: 4+rand(6) GreenGoo + 1 item_frame
        int i = 4 + this.random.nextInt(6);
        for (int n = 0; n < i; n++) {
            this.spawnAtLocation(new ItemStack(ModItems.GREEN_GOO.get()));
        }
        this.spawnAtLocation(new ItemStack(Items.ITEM_FRAME));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold hurt: only when open (OpenClosed != 0) and hurt_timer &lt;= 0; then close shell 300t.
     * Closed / recovering: damage ignored, still reset timers/attacking.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hurtTimer <= 0 && this.getOpenClosed() != 0) {
            boolean ret = super.hurt(source, amount);
            this.hurtTimer = 300;
            this.setOpenClosed(0);
            this.setAttacking(0);
            return ret;
        }
        this.hurtTimer = 300;
        this.setAttacking(0);
        return false;
    }

    /**
     * Gold {@code updateAITasks}: hurt countdown, heal 1/250, random open 1/80, combat 1/10
     * when open, melee distSq &lt; 25.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.hurtTimer > 0) {
            this.hurtTimer--;
            this.clearFire(); // gold setFire(0)
            this.setOpenClosed(0);
        }

        if (this.random.nextInt(250) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }

        if (this.random.nextInt(80) == 2 && this.hurtTimer <= 0) {
            if (this.random.nextInt(8) == 1) {
                this.setOpenClosed(1);
            } else {
                this.setOpenClosed(0);
            }
        }

        if (this.random.nextInt(10) == 1 && this.hurtTimer <= 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.setOpenClosed(1);
                if (this.distanceToSqr(e) < 25.0) {
                    float yaw =
                            (float) Math.toDegrees(Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX())) - 90.0F;
                    while (yaw < 0.0F) {
                        yaw += 360.0F;
                    }
                    this.setYRot(yaw);
                    this.yBodyRot = yaw;
                    this.yHeadRot = yaw;
                    this.setAttacking(1);
                    this.doHurtTarget(e);
                } else {
                    this.setAttacking(0);
                }
            } else {
                this.setAttacking(0);
            }
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
        if (target instanceof Creeper) {
            return false;
        }
        if (target instanceof EnderReaper) {
            return false;
        }
        if (target instanceof Triffid) {
            return false;
        }
        if (target instanceof TerribleTerror) {
            return false;
        }
        if (target instanceof LurkingTerror) {
            return false;
        }
        if (target instanceof PitchBlack) {
            return false;
        }
        // gold Dragon skip â€” entity not ported yet
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
        // gold box expand(10, 8, 10) + GenericTargetSorter nearest
        return GoldStyleCombat.findTarget(this, 10.0, 8.0, this::isSuitableTarget);
    }

    /** Gold {@code getCanSpawnHere}: always true (super spawn rules still apply via caller). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return true;
    }
}
