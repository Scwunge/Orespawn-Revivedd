package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
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
 * Gold {@code EmperorScorpion} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.5×3.0, speed 0.35, health 350, attack 20, armor 35, XP 200.
 * Fire immune; hurt i-frames 30; poison melee + knockback; summons Scorpions;
 * model uses {@link RenderInfo} for claw/tail animation state.
 */
public class EmperorScorpion extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(EmperorScorpion.class, EntityDataSerializers.BYTE);

    /** Gold EmperorScorpion_stats defaults: health 350, defense 35, attack 20. */
    private static final int HEALTH = 350;
    private static final int ATTACK = 20;
    private static final int DEFENSE = 35;

    private final float moveSpeed = 0.35F;
    private int hurtTimer;
    private RenderInfo renderdata = new RenderInfo();

    public EmperorScorpion(EntityType<? extends EmperorScorpion> type, Level level) {
        super(type, level);
        this.xpReward = 200;
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    /** Gold {@code setRenderInfo} — copy fields (model mutates then writes back). */
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
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100; scan box 24
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(0.9,false) skipped, WanderALot(14,1),
        // WatchClosest(Player,8), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 14, 1.0));
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

    public int getEmperorScorpionHealth() {
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
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:alo_hurt
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:emperorscorpion_death
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
        // gold: EmperorScorpionScale + item_frame
        this.dropItemRand(ModItems.EMPEROR_SCORPION_SCALE.get(), 1);
        this.dropItemRand(Items.ITEM_FRAME, 1);
        int i = 4 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            this.dropItemRand(Items.OBSIDIAN, 1);
        }
        i = 4 + this.random.nextInt(8);
        for (int n = 0; n < i; n++) {
            this.dropItemRand(Items.ROTTEN_FLESH, 1);
        }
        i = 1 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            int pick = this.random.nextInt(20);
            // gold enchants best-effort simplified (1.21 registry enchants deferred)
            switch (pick) {
                case 0 -> this.dropItemRand(ModItems.ULTIMATE_SWORD.get(), 1);
                case 1 -> this.dropItemRand(Items.DIAMOND, 1);
                case 2 -> this.dropItemRand(Items.DIAMOND_BLOCK, 1);
                case 3 -> this.dropItemRand(Items.IRON_SWORD, 1);
                case 4 -> this.dropItemRand(Items.IRON_SHOVEL, 1);
                case 5 -> this.dropItemRand(Items.IRON_PICKAXE, 1);
                case 6 -> this.dropItemRand(Items.IRON_AXE, 1);
                case 7 -> this.dropItemRand(Items.IRON_HOE, 1);
                case 8 -> this.dropItemRand(Items.IRON_HELMET, 1);
                case 9 -> this.dropItemRand(Items.IRON_CHESTPLATE, 1);
                case 10 -> this.dropItemRand(Items.IRON_LEGGINGS, 1);
                case 11 -> this.dropItemRand(Items.IRON_BOOTS, 1);
                case 12 -> this.dropItemRand(ModItems.ULTIMATE_BOW.get(), 1);
                default -> {
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // gold interact returns false always
        return InteractionResult.PASS;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity living) {
            // gold poison duration: default 6; if EASY then 8
            // (nested NORMAL/HARD under EASY never fire — gold 1:1)
            int var2 = 6;
            if (this.level().getDifficulty() == Difficulty.EASY) {
                var2 = 8;
            }
            if (this.random.nextInt(3) == 1) {
                // gold PotionEffect duration var2*15 ticks
                living.addEffect(new MobEffectInstance(MobEffects.POISON, var2 * 15, 0));
            }
            double ks = 3.0;
            double inair = 0.2;
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
        if (this.hurtTimer > 0) {
            return false;
        }
        boolean ret = false;
        if (!"cactus".equals(source.getMsgId())) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 30;
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
     * Gold {@code updateAITasks}: hurt timer; combat 1/4; look; melee (6+w/2)^2;
     * hit nextInt(4)==0||nextInt(6)==1; path 1.2; 1/20 spawn Scorpion mid-fight;
     * heal 2 on 1/100 when damaged.
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
            if (this.random.nextInt(100) == 0) {
                this.setTarget(null);
            }
            if (e == null) {
                e = this.findSomethingToAttack();
            }
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (GoldStyleCombat.inMeleeRange(this, e, 6.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(6) == 1) {
                        this.doHurtTarget(e);
                        // gold scorpion_attack 1.4/1.0 or scorpion_living 1.0/1.0
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                }
                if (this.random.nextInt(20) == 1) {
                    spawnCreature(
                            this.level(),
                            ModEntities.SCORPION.get(),
                            (this.getX() + e.getX()) / 2.0
                                    + this.random.nextInt(5)
                                    - this.random.nextInt(5),
                            (this.getY() + e.getY()) / 2.0 + 1.01,
                            (this.getZ() + e.getZ()) / 2.0
                                    + this.random.nextInt(5)
                                    - this.random.nextInt(5));
                }
            } else {
                this.setAttacking(0);
            }
        }
        if (this.random.nextInt(100) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(2.0F);
        }
    }

    /** Gold {@code spawnCreature}: create, place, add, finalize. */
    public static Entity spawnCreature(
            Level level, EntityType<? extends Mob> type, double x, double y, double z) {
        if (!(level instanceof ServerLevel server)) {
            return null;
        }
        Entity e = type.create(server);
        if (e != null) {
            e.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
            if (e instanceof Mob mob) {
                mob.finalizeSpawn(
                        server,
                        server.getCurrentDifficultyAt(mob.blockPosition()),
                        MobSpawnType.MOB_SUMMONED,
                        null);
            }
            level.addFreshEntity(e);
        }
        return e;
    }

    /**
     * Gold {@code isSuitableTarget}: skip Enderman/EnderKnight/EnderReaper/Creeper/
     * Scorpion/EmperorScorpion/creative; MyUtils.isIgnoreable deferred.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof EnderMan) {
            return false;
        }
        if (target instanceof EnderKnight) {
            return false;
        }
        if (target instanceof EnderReaper) {
            return false;
        }
        if (target instanceof Creeper) {
            return false;
        }
        if (target instanceof Scorpion) {
            return false;
        }
        if (target instanceof EmperorScorpion) {
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
        // gold box expand(24, 6, 24); GenericTargetSorter nearest first
        List<LivingEntity> list =
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(24.0, 6.0, 24.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name "Emperor Scorpion" OK; else clear headroom
     * (k/j -2.1, i 2.4 air), valid light, night, y≥50, no peer in 20×6×20.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos origin = this.blockPosition();
        for (int k = -2; k < 2; k++) {
            for (int j = -2; j < 2; j++) {
                for (int i = 2; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    BlockState bid = level.getBlockState(p);
                    if (bid.is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            // gold: match spawner entity name "Emperor Scorpion"
                            return true;
                        }
                    }
                    if (!bid.isAir()) {
                        return false;
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
        return level.getEntitiesOfClass(
                        EmperorScorpion.class, this.getBoundingBox().inflate(20.0, 6.0, 20.0), e -> e != this)
                .isEmpty();
    }
}
