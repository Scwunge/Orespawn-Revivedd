package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code RubberDucky} (EntityTameable). Size 0.33×0.5, speed 0.22, health 5, attack attr 6
 * (melee hit 1.0 / 2.0 when killcount≥5), armor 1, XP 15.
 * <p>
 * Simplified port: Animal + water seek + squid hunt. Full tame/follow/sit, killcount-evil
 * texture/player aggro, and multi-respawn-on-player-kill deferred.
 */
public class RubberDucky extends Animal {
    private static final EntityDataAccessor<Byte> DATA_ATTACKING =
            SynchedEntityData.defineId(RubberDucky.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_KILLCOUNT =
            SynchedEntityData.defineId(RubberDucky.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.22F;
    private int killcount;
    private final RenderInfo renderdata = new RenderInfo();
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public RubberDucky(EntityType<? extends RubberDucky> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ARMOR, 1.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACKING, (byte) 0);
        builder.define(DATA_KILLCOUNT, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: swim, mate×2, follow owner (deferred), tempt wheat seeds, wanderALot 16, watch, idle
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.WHEAT_SEEDS), false));
        this.goalSelector.addGoal(4, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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

    public int getAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(DATA_ATTACKING, (byte) value);
    }

    public int getKillCount() {
        return this.entityData.get(DATA_KILLCOUNT);
    }

    public void setKillCount(int value) {
        this.killcount = value;
        this.entityData.set(DATA_KILLCOUNT, (byte) Mth.clamp(value, 0, 127));
    }

    public int mygetMaxHealth() {
        return 5;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold: float up in water
        if (this.isInWater()) {
            Vec3 m = this.getDeltaMovement();
            double y = m.y + 0.1F;
            if (y < -0.05F) {
                y = -0.05F;
            }
            this.setDeltaMovement(m.x, y, m.z);
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 1/10 duck_hurt else null
        return this.random.nextInt(10) == 1 ? SoundsHandler.ENTITY_DUCK_HURT.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public float getVoicePitch() {
        return 1.2F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: 50% feather, else 50% RubberDuckyEgg else null
        if (this.random.nextInt(2) == 1) {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
        // egg drop deferred until spawn egg item wired
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float dmg = this.getKillCount() >= 5 ? 2.0F : 1.0F;
        return target.hurt(this.damageSources().mobAttack(this), dmg);
    }

    private boolean scanWater(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (bid.is(Blocks.WATER)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x - dx, y + i, z + j));
                if (bid.is(Blocks.WATER)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x - dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }
        for (int var12 = -dx; var12 <= dx; var12++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + var12, y + dy, z + j));
                if (bid.is(Blocks.WATER)) {
                    int d = dy * dy + j * j + var12 * var12;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + var12;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + var12, y - dy, z + j));
                if (bid.is(Blocks.WATER)) {
                    int d = dy * dy + j * j + var12 * var12;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + var12;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }
        for (int var13 = -dx; var13 <= dx; var13++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + var13, y + j, z + dz));
                if (bid.is(Blocks.WATER)) {
                    int d = dz * dz + j * j + var13 * var13;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + var13;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + var13, y + j, z - dz));
                if (bid.is(Blocks.WATER)) {
                    int d = dz * dz + j * j + var13 * var13;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + var13;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }
        return found != 0;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        // gold: seek water when not swimming
        if (!this.isInWater() && this.random.nextInt(50) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 14; i++) {
                int j = i > 5 ? 5 : i;
                if (this.scanWater((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.33);
            }
        }

        if (this.killcount > 0 && this.random.nextInt(200) == 1) {
            this.killcount--;
            this.setKillCount(this.killcount);
        }

        if (this.getHealth() < this.mygetMaxHealth() && this.random.nextInt(300) == 1) {
            this.heal(1.0F);
        }

        if (this.level().getDifficulty() != Difficulty.PEACEFUL && this.random.nextInt(5) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                if (this.distanceToSqr(e) < 12.0) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                }
            } else {
                this.setAttacking(0);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        // gold: AttackSquid (deferred), Squid; killcount≥5 players deferred
        return target instanceof Squid;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        LivingEntity revenge = this.getLastHurtByMob();
        if (revenge != null && revenge.isAlive()) {
            return revenge;
        }
        this.setLastHurtByMob(null);
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(8.0, 4.0, 8.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Killcount", this.killcount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.killcount = tag.getInt("Killcount");
        this.setKillCount(this.killcount);
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50 + daytime; spawner check deferred. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isBaby()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat: wheat seeds (tempt/breed simplified; crystal apple breeding deferred)
        return stack.is(Items.WHEAT_SEEDS);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        Entity baby = this.getType().create(level);
        return baby instanceof RubberDucky ducky ? ducky : null;
    }
}
