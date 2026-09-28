package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Irukandji} (EntityMob). Size 0.25×0.25, speed 0.15, XP 50.
 * Stats defaults {@code get_mobstats("Irukandji", 1, 20, 0)} — 1 HP, 20 attack, 0 armor.
 * Deadly bare-hand interact (200 dmg). Water-seek when dry. canBreatheUnderwater=true in gold.
 */
public class Irukandji extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Irukandji.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.15F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Irukandji(EntityType<? extends Irukandji> type, Level level) {
        super(type, level);
        this.xpReward = 50; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1.0) // Irukandji_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 20.0) // Irukandji_stats.attack default
                .add(Attributes.ARMOR, 0.0) // Irukandji_stats.defense default
                .add(Attributes.FOLLOW_RANGE, 1.0); // gold field_70174_ab = 1
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MyEntityAIWander(1.0), WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        // gold canBreatheUnderwater=true — LivingEntity method final in 1.21
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    public int mygetMaxHealth() {
        return 1;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:little_splt
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:ratdead
        return SoundsHandler.ENTITY_RAT_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.25F;
    }

    @Override
    public float getVoicePitch() {
        return 2.0F;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /**
     * Gold {@code interact}: empty main hand → 200 damage to player (box jellyfish).
     * Gold {@code getDropItem}: {@code MyIrukandji} ({@code deadirukandji}).
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player != null && player.getItemInHand(hand).isEmpty()) {
            player.hurt(this.damageSources().mobAttack(this), 200.0F);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem → MyIrukandji (deadirukandji)
        this.spawnAtLocation(new ItemStack(ModItems.DEAD_IRUKANDJI.get()));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isDeadOrDying()) {
            return false;
        }
        Entity e = source.getEntity();
        if (e instanceof Player p) {
            if (p.getMainHandItem().isEmpty()) {
                p.hurt(this.damageSources().mobAttack(this), 200.0F);
                return false;
            }
        }
        if (e instanceof LivingEntity living) {
            if (e instanceof Irukandji) {
                return false;
            }
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        return super.hurt(source, amount);
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWater(bid)) {
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
                if (this.isWater(bid)) {
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

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y - dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z - dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
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

        if (!this.isInWater() && this.random.nextInt(10) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;

            for (int i = 1; i < 12; i++) {
                int j = i;
                if (j > 5) {
                    j = 5;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }

            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.33);
            } else {
                if (this.random.nextInt(25) == 1) {
                    this.hurt(this.damageSources().generic(), 1.0F);
                }
                if (this.getHealth() <= 0.0F) {
                    this.discard();
                    return;
                }
            }
        }

        // gold: nextInt(8)==1 combat; melee distSq < 3
        if (this.random.nextInt(8) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                if (this.distanceToSqr(e) < 3.0) {
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
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        // gold: only players
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        LivingEntity current = this.getTarget();
        if (current != null && current.isAlive()) {
            return current;
        }
        this.setTarget(null);

        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0, 4.0, 6.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    private int findBuddies() {
        List<Irukandji> list =
                this.level().getEntitiesOfClass(Irukandji.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0));
        return list.size();
    }

    /** Gold: y≥50, daytime, 1/60 roll, buddies ≤ 2. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.random.nextInt(60) != 1) {
            return false;
        }
        return this.findBuddies() <= 2;
    }

    /** Spawns in water: skip the vanilla "no liquid in bounding box" check (as {@code WaterAnimal} does). */
    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }
}
