package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
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
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code AttackSquid} (EntityMob). Size 1.0×1.25, speed 0.25, XP 15.
 * Stats defaults {@code get_mobstats("AttackSquid", 10, 8, 0)} — health/attack/defense.
 * Water-seek when dry; proximity combat + buddy follow; watercanon (InkSack/WaterBall) deferred.
 */
public class AttackSquid extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(AttackSquid.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.25F;
    /** Gold {@code wasshot} — SquidZooka projectile timer; despawn when hits 0 after set. */
    private int wasshot = 0;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;
    @Nullable
    private LivingEntity buddy;

    public AttackSquid(EntityType<? extends AttackSquid> type, Level level) {
        super(type, level);
        this.xpReward = 15; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // AttackSquid_stats.health default
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8.0) // AttackSquid_stats.attack default
                .add(Attributes.ARMOR, 0.0) // AttackSquid_stats.defense default
                .add(Attributes.FOLLOW_RANGE, 3.0); // gold field_70174_ab = 3
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(16,1.0), WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    public void setWasShot() {
        this.wasshot = 250;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 10;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:squid_hurt (squid_hurt1-4.ogg)
        return SoundsHandler.ENTITY_SQUID_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:squid_death (squid_death1-2.ogg)
        return SoundsHandler.ENTITY_SQUID_DEATH.get();
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
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    /** Gold {@code fall}: skip fall damage while wasshot active. */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.wasshot != 0) {
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WasShot", this.wasshot);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasshot = tag.getInt("WasShot");
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold switch nextInt(50) rare loot + always 1+rand(3) fish; enchants best-effort simplified
        int var4 = this.random.nextInt(50);
        switch (var4) {
            case 0 -> this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
            case 1 -> this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT));
            case 2 -> this.spawnAtLocation(new ItemStack(Items.EMERALD));
            case 3 -> this.spawnAtLocation(new ItemStack(Items.IRON_CHESTPLATE));
            case 4 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_SWORD));
            case 5 -> this.spawnAtLocation(new ItemStack(Items.IRON_SHOVEL));
            case 6 -> this.spawnAtLocation(new ItemStack(Items.IRON_PICKAXE));
            case 7 -> this.spawnAtLocation(new ItemStack(Items.BOW));
            case 8 -> this.spawnAtLocation(new ItemStack(Items.LEATHER_HELMET));
            case 9 -> this.spawnAtLocation(new ItemStack(Items.LEATHER_CHESTPLATE));
            case 10 -> this.spawnAtLocation(new ItemStack(Items.LEATHER_LEGGINGS));
            case 11 -> this.spawnAtLocation(new ItemStack(Items.LEATHER_BOOTS));
            case 12 -> this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE));
            case 13 -> this.spawnAtLocation(new ItemStack(Items.GOLD_BLOCK));
            case 14 -> this.spawnAtLocation(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
            case 15, 16, 17 -> this.spawnAtLocation(new ItemStack(Items.INK_SAC));
            default -> {
            }
        }
        int i = 1 + this.random.nextInt(3);
        for (int n = 0; n < i; n++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isDeadOrDying()) {
            return false;
        }
        Entity e = source.getEntity();
        // gold: immune to AttackSquid / WaterBall / WaterDragon (projectiles & dragon not ported)
        if (e instanceof AttackSquid) {
            return false;
        }
        if (e instanceof LivingEntity living && !(e instanceof AttackSquid)) {
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        boolean ret = super.hurt(source, amount);
        // gold: on death by player, 1/15 chance spawn Kraken (not ported) when not dim5 and wasshot==0
        return ret;
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} — find nearest water on expanding shell. */
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

    /**
     * Gold {@code updateAITasks}: wasshot countdown; dry water-seek; combat every 1/10;
     * melee distSq &lt; 9; hit nextInt(4)==0 || nextInt(5)==1; else path + watercanon.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.wasshot > 0) {
            this.wasshot--;
            if (this.wasshot == 0) {
                this.discard();
                return;
            }
        }

        // gold: when not in water, seek every 1/10
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
                    // gold heal(-1.0F)
                    this.hurt(this.damageSources().generic(), 1.0F);
                }
                if (this.getHealth() <= 0.0F) {
                    this.discard();
                    return;
                }
            }
        }

        if (this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                if (this.distanceToSqr(e) < 9.0) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                    // gold watercanon(e) — InkSack / WaterBall not ported this wave
                }
            } else {
                if (this.buddy != null && this.buddy.isAlive()) {
                    this.getNavigation().moveTo(this.buddy, 1.0);
                }
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
        // gold Girlfriend / Boyfriend / Lizard not ported
        if (target instanceof Zombie) {
            return true;
        }
        if (target instanceof Villager) {
            return true;
        }
        if (target instanceof Spider || target instanceof CaveSpider) {
            return true;
        }
        if (target instanceof Ghost || target instanceof GhostSkelly) {
            return false;
        }
        if (target instanceof AttackSquid) {
            if (this.random.nextInt(5) == 1) {
                this.buddy = target;
            }
            return false;
        }
        // gold: if wasshot != 0, attack anything else (projectile mode)
        return this.wasshot != 0;
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
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0, 4.0, 10.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /** Gold {@code getCanSpawnHere}: super then y≥50 and daytime. */
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

    /** Spawns in water: skip the vanilla "no liquid in bounding box" check (as {@code WaterAnimal} does). */
    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }
}
