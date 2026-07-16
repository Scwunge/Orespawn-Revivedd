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
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
 * Gold {@code SeaViper} (EntityMob). Size 1.5Ã—2.5, XP 120, follow 30.
 * Stats defaults {@code get_mobstats("SeaViper", 160, 22, 12)}.
 * Water-seek when dry; water speed 0.75 / land 0.25; poison melee; canBreatheUnderwater.
 * Registry size set in {@code ModEntities} .
 */
public class SeaViper extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(SeaViper.class, EntityDataSerializers.BYTE);

    private float moveSpeed = 0.35F;
    private int hurtTimer;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public SeaViper(EntityType<? extends SeaViper> type, Level level) {
        super(type, level);
        this.moveSpeed = 0.35F;
        this.xpReward = 120; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold SeaViper_stats default: health 160, attack 22, defense 12
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 160.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 22.0)
                .add(Attributes.ARMOR, 12.0)
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
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        // gold canBreatheUnderwater=true
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    /** Gold livingUpdate: water 0.75 / land 0.25. */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isInWater()) {
            this.moveSpeed = 0.75F;
        } else {
            this.moveSpeed = 0.25F;
        }
    }

    public int mygetMaxHealth() {
        return 160;
    }

    public int getSeaViperHealth() {
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
        //  gold: 1/2 orespawn:seaviper_living (ogg present; SoundsHandler deferred)
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:seaviper_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:seaviper_death
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
        // gold: SeaViperTongue, item_frame, 9+r6 fish+rotten_flesh, random iron gear
        this.spawnAtLocation(new ItemStack(ModItems.SEA_VIPER_TONGUE.get()));
        this.spawnAtLocation(new ItemStack(Items.ITEM_FRAME));
        int n = 9 + this.random.nextInt(6);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH));
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
        // gold: knockback ks=0.8 inair=0.14; 50% poison duration by difficulty (bug: only EASY branch in gold)
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity living) {
            double ks = 0.8;
            double inair = 0.14;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);

            // gold nested difficulty check is buggy; port sensible duration: EASY 8, NORMAL 10, HARD 12 (else 6)
            int sec = 6;
            Difficulty diff = this.level().getDifficulty();
            if (diff == Difficulty.EASY) {
                sec = 8;
            } else if (diff == Difficulty.NORMAL) {
                sec = 10;
            } else if (diff == Difficulty.HARD) {
                sec = 12;
            }
            if (this.random.nextInt(2) == 1) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, sec * 20, 0));
            }
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        boolean ret = false;
        if (this.hurtTimer <= 0) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 5;
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            if (living instanceof SeaViper) {
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
     * Gold {@code updateAITasks}: hurt timer; dry water-seek 1/25 (1 dmg 1/150 if none);
     * combat 1/5 reach 4.5+w/2 hit 1/2||1/4; path 1.5; wet heal 1/100.
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
                if (this.random.nextInt(150) == 1) {
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
                if (GoldStyleCombat.inMeleeRange(this, e, 4.5)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(2) == 0 || this.random.nextInt(4) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.5);
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (this.random.nextInt(100) == 1 && this.isInWater() && this.getHealth() < this.mygetMaxHealth()) {
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
        if (target instanceof SeaViper) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
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
        // gold scan 18Ã—4Ã—18
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(18.0, 4.0, 18.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner "Sea Viper" bypass (not ported);
     * yâ‰¥50, daytime, no buddy in 16Ã—5Ã—16.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        return this.level()
                .getEntitiesOfClass(SeaViper.class, this.getBoundingBox().inflate(16.0, 5.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
