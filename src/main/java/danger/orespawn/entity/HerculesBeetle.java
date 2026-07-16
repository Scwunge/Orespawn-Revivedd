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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
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
 * Gold {@code HerculesBeetle} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.25×2.75, speed 0.25, health 250, attack 30, armor 19, XP 200.
 * Fire immune; hurt i-frames 20; heavy vertical knockback; night ground boss.
 */
public class HerculesBeetle extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(HerculesBeetle.class, EntityDataSerializers.BYTE);

    /** Gold HerculesBeetle_stats defaults: health 250, attack 30, defense 19. */
    private static final int HEALTH = 250;
    private static final int ATTACK = 30;
    private static final int DEFENSE = 19;

    private final float moveSpeed = 0.25F;
    private int hurtTimer;

    public HerculesBeetle(EntityType<? extends HerculesBeetle> type, Level level) {
        super(type, level);
        this.xpReward = 200; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, HEALTH) // HerculesBeetle_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, ATTACK) // HerculesBeetle_stats.attack
                .add(Attributes.ARMOR, DEFENSE) // HerculesBeetle_stats.defense
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100; scan 16
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(0.9,false) skipped, WanderALot(14,1),
        // WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold EntityAIMoveThroughVillage skipped
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    /** Gold field_70178_ae isImmuneToFire. */
    @Override
    public boolean fireImmune() {
        return true;
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
    }

    /** Gold jump: motionY += 0.25, posY += 0.5. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
        this.setPos(this.getX(), this.getY() + 0.5, this.getZ());
    }

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getHerculesBeetleHealth() {
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
        //  gold: null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:alo_hurt
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:hercules_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    /** Gold {@code dropItemRand}: scatter x/z ±(0.4)-(0.4), y+1. */
    private ItemStack dropItemRand(Item index, int count) {
        ItemStack stack = new ItemStack(index, count);
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(5) - this.random.nextInt(5),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(5) - this.random.nextInt(5),
                stack);
        this.level().addFreshEntity(item);
        return stack;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: MyBigHammer, leather, beef, random diamond gear
        this.dropItemRand(ModItems.BIG_HAMMER.get(), 1);
        this.dropItemRand(Items.LEATHER, 1);
        int i = 4 + this.random.nextInt(8);
        for (int n = 0; n < i; n++) {
            this.dropItemRand(Items.BEEF, 1);
        }
        i = 1 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            int pick = this.random.nextInt(20);
            // gold enchants best-effort simplified (1.21 registry enchants deferred)
            switch (pick) {
                case 1 -> this.dropItemRand(Items.DIAMOND, 1);
                case 2 -> this.dropItemRand(Items.DIAMOND_BLOCK, 1);
                case 3 -> this.dropItemRand(Items.DIAMOND_SWORD, 1);
                case 4 -> this.dropItemRand(Items.DIAMOND_SHOVEL, 1);
                case 5 -> this.dropItemRand(Items.DIAMOND_PICKAXE, 1);
                case 6 -> this.dropItemRand(Items.DIAMOND_AXE, 1);
                case 7 -> this.dropItemRand(Items.DIAMOND_HOE, 1);
                case 8 -> this.dropItemRand(Items.DIAMOND_HELMET, 1);
                case 9 -> this.dropItemRand(Items.DIAMOND_CHESTPLATE, 1);
                case 10 -> this.dropItemRand(Items.DIAMOND_LEGGINGS, 1);
                case 11 -> this.dropItemRand(Items.DIAMOND_BOOTS, 1);
                default -> {
                    // gold case 0/12+ empty
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // gold interact returns false always
        return InteractionResult.PASS;
    }

    /**
     * Gold {@code attackEntityAsMob}: attribute damage + knockback (ks=0.45, inair=1.25, ×2 if player/dead).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 0.45;
            double inair = 1.25;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            // gold: inair * abs(nextFloat()) for vertical
            target.push(
                    Math.cos(f3) * ks,
                    inair * Math.abs(this.random.nextFloat()),
                    Math.sin(f3) * ks);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // gold hurt_timer i-frames 20; ignore cactus
        if (this.hurtTimer > 0) {
            return false;
        }
        boolean ret = false;
        if (!"cactus".equals(source.getMsgId())) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 20;
            Entity e = source.getEntity();
            if (e instanceof LivingEntity living) {
                this.setTarget(living);
                this.setLastHurtByMob(living);
                this.getNavigation().moveTo(living, 1.2);
            }
        }
        return ret;
    }

    /**
     * Gold {@code updateAITasks}: hurt timer; combat 1/4; look; melee (5+w/2)^2;
     * hit nextInt(3)==0||nextInt(4)==1; path 1.2; heal 2 on 1/150 when damaged.
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
        if (this.random.nextInt(4) == 0) {
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
                // gold: distanceSq < (5.0F + e.width/2)^2
                if (GoldStyleCombat.inMeleeRange(this, e, 5.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(3) == 0 || this.random.nextInt(4) == 1) {
                        this.doHurtTarget(e);
                        // gold scorpion_attack 1.4/1.0 or scorpion_living 1.0/1.0
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                }
            } else {
                this.setAttacking(0);
            }
        }
        if (this.random.nextInt(150) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(2.0F);
        }
    }

    /**
     * Gold {@code isSuitableTarget}: skip Creeper/HerculesBeetle/creative;
     * MyUtils.isIgnoreable deferred.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Creeper) {
            return false;
        }
        if (target instanceof HerculesBeetle) {
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
        // gold box expand(16, 6, 16); GenericTargetSorter nearest first
        List<LivingEntity> list =
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name "Hercules Beetle" OK; else valid light,
     * night, y≥50, clear headroom (k/j -2.1, i 2.4 air), no peer in 16×6×16.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos origin = this.blockPosition();
        // gold: spawner scan k/j -3.2, i 0.4
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    BlockState bid = level.getBlockState(p);
                    if (bid.is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold: match spawner entity name "Hercules Beetle"
                            return true;
                        }
                    }
                }
            }
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        // gold open air above: k/j -2.1, i 2.4
        for (int k = -2; k < 2; k++) {
            for (int j = -2; j < 2; j++) {
                for (int i = 2; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (!level.getBlockState(p).isAir()) {
                        return false;
                    }
                }
            }
        }
        return level.getEntitiesOfClass(
                        HerculesBeetle.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0), e -> e != this)
                .isEmpty();
    }
}
