package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code SeaMonster} (EntityMob). Size 1.25Ã—2.5, XP 150, follow 30.
 * Stats defaults {@code get_mobstats("SeaMonster", 110, 14, 8)}.
 * Water-seek when dry; faster in water (0.55 vs 0.25); canBreatheUnderwater.
 * Registry size set in {@code ModEntities} .
 */
public class SeaMonster extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(SeaMonster.class, EntityDataSerializers.BYTE);

    private float moveSpeed = 0.25F;
    private int hurtTimer;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public SeaMonster(EntityType<? extends SeaMonster> type, Level level) {
        super(type, level);
        this.moveSpeed = 0.25F;
        this.xpReward = 150; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold SeaMonster_stats default: health 110, attack 14, defense 8
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 110.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 14.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.FOLLOW_RANGE, 30.0); // gold field_70174_ab = 30
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(16,1), WatchClosest Player 10 / Living 8, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
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
        // gold canBreatheUnderwater=true â€” LivingEntity method final in 1.21
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    /** Gold livingUpdate: water 0.55 / land 0.25. */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isInWater()) {
            this.moveSpeed = 0.55F;
        } else {
            this.moveSpeed = 0.25F;
        }
    }

    public int mygetMaxHealth() {
        return 110;
    }

    public int getSeaMonsterHealth() {
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
        // gold: 1/3 orespawn:seamonster_living (ogg present; SoundsHandler deferred)
        if (this.random.nextInt(3) != 0) {
            return null;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:seamonster_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:seamonster_death
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
        // gold: SeaMonsterScale, item_frame, 9+r6 fish, random iron gear / gold ore
        this.spawnAtLocation(new ItemStack(ModItems.SEA_MONSTER_SCALE.get()));
        this.spawnAtLocation(new ItemStack(Items.ITEM_FRAME));
        int fish = 9 + this.random.nextInt(6);
        for (int i = 0; i < fish; i++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
        switch (this.random.nextInt(20)) {
            case 1 -> this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
            case 3 -> this.spawnAtLocation(new ItemStack(Items.IRON_SWORD));
            case 4 -> this.spawnAtLocation(new ItemStack(Items.IRON_SHOVEL));
            case 5 -> this.spawnAtLocation(new ItemStack(Items.IRON_PICKAXE));
            case 6 -> this.spawnAtLocation(new ItemStack(Items.IRON_AXE));
            case 7 -> this.spawnAtLocation(new ItemStack(Items.IRON_HOE));
            case 8 -> this.spawnAtLocation(new ItemStack(Items.IRON_HELMET));
            case 9 -> this.spawnAtLocation(new ItemStack(Items.IRON_CHESTPLATE));
            case 10 -> this.spawnAtLocation(new ItemStack(Items.IRON_LEGGINGS));
            case 11 -> this.spawnAtLocation(new ItemStack(Items.IRON_BOOTS));
            case 13 -> this.spawnAtLocation(new ItemStack(Blocks.GOLD_ORE));
            default -> {
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: super then knockback ks=0.6 inair=0.1 (*2 if dead/player)
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 0.6;
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
        // gold: ignore cactus; hurt_timer 8 i-frames; retarget Living
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = false;
        if (this.hurtTimer <= 0) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 8;
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            if (living instanceof SeaMonster) {
                return false;
            }
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        return ret;
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} â€” nearest water on expanding shell. */
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
     * Gold {@code updateAITasks}: hurt timer; dry water-seek every 1/25 (1 dmg / 1/40 if none);
     * combat 1/5 reach 4+w/2 hit 1/4||1/5; path 1.0; wet heal 1/120.
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

        if (!this.isInWater() && this.random.nextInt(25) == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;

            for (int i = 1; i < 12; i++) {
                int j = i;
                if (j > 10) {
                    j = 10;
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
                if (this.random.nextInt(40) == 1) {
                    this.hurt(this.damageSources().generic(), 1.0F);
                }
                if (this.getHealth() <= 0.0F) {
                    this.discard();
                    return;
                }
            }
        }

        if (this.random.nextInt(5) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (GoldStyleCombat.inMeleeRange(this, e, 4.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.0);
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (this.random.nextInt(120) == 1 && this.isInWater() && this.getHealth() < this.mygetMaxHealth()) {
            this.playSound(SoundEvents.GENERIC_SPLASH, 1.5F, this.random.nextFloat() * 0.2F + 0.9F);
            this.heal(1.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof SeaMonster) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        // gold: EntityMob â†’ true; else MyUtils.isAttackableNonMob
        if (target instanceof Monster) {
            return true;
        }
        return target instanceof Animal || target instanceof Villager;
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
        // gold scan 16Ã—4Ã—16
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0, 4.0, 16.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner "Sea Monster" bypass (not ported);
     * yâ‰¥50, night, valid light (super), no buddy in 16Ã—5Ã—16.
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
        return this.level()
                .getEntitiesOfClass(SeaMonster.class, this.getBoundingBox().inflate(16.0, 5.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
