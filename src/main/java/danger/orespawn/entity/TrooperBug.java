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
 * Gold {@code TrooperBug} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.0×3.5, speed 0.4, health 200, attack 20, armor 15, XP 150.
 * Hurt i-frames 20; knockback melee; jump-at-target; summons Spit Bug (deferred);
 * night ground hostile (Jumpy Bug spawner name).
 */
public class TrooperBug extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(TrooperBug.class, EntityDataSerializers.BYTE);

    /** Gold TrooperBug_stats defaults: health 200, attack 20, defense 15. */
    private static final int HEALTH = 200;
    private static final int ATTACK = 20;
    private static final int DEFENSE = 15;

    private final float moveSpeed = 0.4F;
    private int hurtTimer;
    private RenderInfo renderdata = new RenderInfo();

    public TrooperBug(EntityType<? extends TrooperBug> type, Level level) {
        super(type, level);
        this.xpReward = 150; // gold field_70728_aV
    }

    public RenderInfo getRenderInfo() {
        if (this.renderdata == null) {
            this.renderdata = new RenderInfo();
        }
        return this.renderdata;
    }

    /** Gold {@code setRenderInfo} — copy fields. */
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
                .add(Attributes.MAX_HEALTH, HEALTH) // TrooperBug_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, ATTACK) // TrooperBug_stats.attack
                .add(Attributes.ARMOR, DEFENSE) // TrooperBug_stats.defense
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100; scan 12
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage(0.9,false) skipped, WanderALot(14,1),
        // WatchClosest(Player,10), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold EntityAIMoveThroughVillage skipped
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 14, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
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
        // gold canDespawn: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold: if isAirBorne, clear path
        if (!this.onGround()) {
            this.getNavigation().stop();
        }
    }

    /**
     * Gold {@code jump}: motionY += 1.15, posY += 1.5, forward push from yaw.
     */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 1.15, 0.0));
        this.setPos(this.getX(), this.getY() + 1.5, this.getZ());
        float f = 0.2F + Math.abs(this.random.nextFloat() * 0.45F);
        double yaw = Math.toRadians(this.yBodyRot);
        this.setDeltaMovement(this.getDeltaMovement().add(
                -f * Math.sin(yaw),
                0.0,
                f * Math.cos(yaw)));
    }

    /** Gold {@code jumpAtEntity}: leap toward target. */
    protected void jumpAtEntity(LivingEntity e) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 1.25, 0.0));
        this.setPos(this.getX(), this.getY() + 1.25, this.getZ());
        float f = 0.3F + Math.abs(this.random.nextFloat() * 0.25F);
        float d = (float) Math.atan2(e.getX() - this.getX(), e.getZ() - this.getZ());
        this.setDeltaMovement(this.getDeltaMovement().add(
                f * Math.sin(d),
                0.0,
                f * Math.cos(d)));
    }

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getTrooperBugHealth() {
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
        //  gold: clatter 1/4 else null
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:crunch
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
        // gold: MyJumpyBugScale, magma cream, amethyst loot table
        this.dropItemRand(ModItems.JUMPY_BUG_SCALE.get(), 1);
        this.dropItemRand(Items.MAGMA_CREAM, 1);
        int i = 2 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            this.dropItemRand(ModItems.AMETHYST.get(), 1);
        }
        i = 1 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            int pick = this.random.nextInt(14);
            // gold enchants best-effort simplified (1.21 registry enchants deferred)
            switch (pick) {
                case 2 -> this.dropItemRand(ModItems.AMETHYST.get(), 1); // gold amethyst block fallback
                case 3 -> this.dropItemRand(ModItems.AMETHYST_SWORD.get(), 1);
                case 4 -> this.dropItemRand(ModItems.AMETHYST_SHOVEL.get(), 1);
                case 5 -> this.dropItemRand(ModItems.AMETHYST_PICKAXE.get(), 1);
                case 6 -> this.dropItemRand(ModItems.AMETHYST_AXE.get(), 1);
                case 7 -> this.dropItemRand(ModItems.AMETHYST_HOE.get(), 1);
                case 8 -> this.dropItemRand(ModItems.AMETHYST_HELMET.get(), 1);
                case 9 -> this.dropItemRand(ModItems.AMETHYST_CHESTPLATE.get(), 1);
                case 10 -> this.dropItemRand(ModItems.AMETHYST_LEGGINGS.get(), 1);
                case 11 -> this.dropItemRand(ModItems.AMETHYST_BOOTS.get(), 1);
                default -> {
                    // gold case 0/1/12+ empty
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
     * Gold {@code attackEntityAsMob}: attribute damage + knockback (ks=1.8, inair=0.2, ×2 if player/dead).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 1.8;
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
        // gold hurt_timer i-frames 20; ignore cactus + fall
        if (this.hurtTimer > 0) {
            return false;
        }
        boolean ret = false;
        if (!"cactus".equals(source.getMsgId()) && !"fall".equals(source.getMsgId())) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 20;
            Entity e = source.getEntity();
            if (e instanceof LivingEntity living) {
                this.setTarget(living);
                this.setLastHurtByMob(living);
                this.getNavigation().moveTo(living, 1.2);
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Gold {@code updateAITasks}: hurt timer; combat 1/5; look; jump-at 1/10 grounded;
     * melee (5+w/2)^2; hit nextInt(6)==0||nextInt(7)==1; path 1.2; 1/30 spawn Spit Bug;
     * heal 1 on 1/150 when damaged.
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
        if (this.random.nextInt(5) == 0) {
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
                if (this.random.nextInt(10) == 1 && this.onGround()) {
                    this.jumpAtEntity(e);
                } else if (GoldStyleCombat.inMeleeRange(this, e, 5.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(6) == 0 || this.random.nextInt(7) == 1) {
                        this.doHurtTarget(e);
                        // gold scorpion_attack 1.4/1.0 or clatter 1.0/1.0
                    }
                } else if (this.onGround()) {
                    this.getNavigation().moveTo(e, 1.2);
                }
                // gold: 1/30 spawn Spit Bug mid-fight — deferred until SpitBug entity exists
                // if (this.random.nextInt(30) == 1) { spawn SpitBug at midpoint }
            } else {
                this.setAttacking(0);
            }
        }
        if (this.random.nextInt(150) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }
    }

    /**
     * Gold {@code isSuitableTarget}: skip Hydrolisc/EnderReaper/EnderKnight/Enderman/
     * Creeper/TrooperBug/SpitBug/creative; MyUtils.isIgnoreable deferred.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Hydrolisc) {
            return false;
        }
        if (target instanceof EnderReaper) {
            return false;
        }
        if (target instanceof EnderKnight) {
            return false;
        }
        if (target instanceof EnderMan) {
            return false;
        }
        if (target instanceof Creeper) {
            return false;
        }
        if (target instanceof TrooperBug) {
            return false;
        }
        // gold SpitBug — skip when class exists
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
        // gold box expand(12, 7, 12); GenericTargetSorter nearest first
        List<LivingEntity> list =
                this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.0, 7.0, 12.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name "Jumpy Bug" OK; else valid light,
     * night (day nextInt(20)&gt;1 fail), clear headroom (k/j -2.1, i 1.4 air).
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
                            // gold: match spawner entity name "Jumpy Bug"
                            return true;
                        }
                    }
                }
            }
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        // gold: if daytime && nextInt(20) > 1 → false
        if (level instanceof Level lvl && lvl.isDay() && this.random.nextInt(20) > 1) {
            return false;
        }
        // gold open air above: k/j -2.1, i 1.4
        for (int k = -2; k < 2; k++) {
            for (int j = -2; j < 2; j++) {
                for (int i = 1; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (!level.getBlockState(p).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
