package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Robot2} / Robo-Pounder (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.0×6.2, speed 0.3, health 200, attack 22, armor 18, XP 100.
 * Facing-cone melee + block smash; idle "just_for_fun" grief.
 * Texture: {@code robot2.png}. Model uses {@link RenderInfo} for arm swing state.
 */
public class Robot2 extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Robot2.class, EntityDataSerializers.BYTE);

    /** Gold Robot2_stats defaults. */
    private static final int HEALTH = 200;
    private static final int ATTACK = 22;
    private static final int DEFENSE = 18;

    private final float moveSpeed = 0.3F;
    private int justForFun = 0;
    private RenderInfo renderdata = new RenderInfo();

    public Robot2(EntityType<? extends Robot2> type, Level level) {
        super(type, level);
        this.xpReward = 100;
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
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, ATTACK)
                .add(Attributes.ARMOR, DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 200.0); // gold field_70174_ab = 200
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, WanderALot(14,1), MoveThroughVillage skipped, WatchClosest(Player,10), LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 14, 1.0));
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
        //  gold orespawn:robot_living multi-variant
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
        // gold: 2+rand8 iron_block, 5+rand6 iron_ingot, then 5+rand10 junk cases
        int n = 2 + this.random.nextInt(8);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Blocks.IRON_BLOCK));
        }
        n = 5 + this.random.nextInt(6);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
        }
        int i = 5 + this.random.nextInt(10);
        for (int k = 0; k < i; k++) {
            switch (this.random.nextInt(15)) {
                case 0 -> this.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                case 1 -> this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT)); // gold field_151107_aW was gold_ingot in 1.7? actually compass/map — keep simple scrap
                case 2 -> this.spawnAtLocation(new ItemStack(Items.NETHER_STAR)); // gold field_151132_bS nether star
                case 3, 8 -> this.spawnAtLocation(new ItemStack(Blocks.REDSTONE_BLOCK));
                case 4 -> this.spawnAtLocation(new ItemStack(Blocks.DISPENSER));
                case 5 -> this.spawnAtLocation(new ItemStack(Blocks.JUKEBOX)); // field_150320_F furnace? keep junk
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
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        return ret;
    }

    /** Gold destroyBlock under/near target — protected block list. */
    protected void destroyBlockNear(LivingEntity e) {
        double x = e.getX() + this.random.nextFloat() - this.random.nextFloat();
        double y = e.getY() - 1.0;
        double z = e.getZ() + this.random.nextFloat() - this.random.nextFloat();
        tryDestroy(BlockPos.containing(x, y, z));
    }

    protected void destroyNearbyBlocks() {
        for (int i = 0; i < 50; i++) {
            double x = this.getX() + this.random.nextFloat() * 6.5 - this.random.nextFloat() * 6.5;
            double y = this.getY() + 0.1 + this.random.nextFloat() * 8.5;
            double z = this.getZ() + this.random.nextFloat() * 6.5 - this.random.nextFloat() * 6.5;
            tryDestroy(BlockPos.containing(x, y, z));
        }
    }

    private void tryDestroy(BlockPos pos) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }
        BlockState state = this.level().getBlockState(pos);
        if (state.isAir()) {
            return;
        }
        // gold protected: obsidian, bedrock, end_stone, spawner, redstone_block, iron_block, chest
        if (state.is(Blocks.OBSIDIAN)
                || state.is(Blocks.BEDROCK)
                || state.is(Blocks.END_STONE)
                || state.is(Blocks.SPAWNER)
                || state.is(Blocks.REDSTONE_BLOCK)
                || state.is(Blocks.IRON_BLOCK)
                || state.is(Blocks.CHEST)
                || state.is(Blocks.TRAPPED_CHEST)
                || state.is(Blocks.ENDER_CHEST)
                || state.getDestroySpeed(this.level(), pos) < 0) {
            return;
        }
        this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(6) == 1 && OreSpawnMain.PlayNicely == 0) {
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
            if (e != null) {
                double rr = Math.atan2(e.getZ() - this.getZ(), e.getX() - this.getX());
                double rhdir = Math.toRadians((this.getYRot() + 90.0F) % 360.0F);
                double pi = Math.PI;
                double rdd = Math.abs(rr - rhdir) % (pi * 2.0);
                if (rdd > pi) {
                    rdd -= pi * 2.0;
                }
                rdd = Math.abs(rdd);
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (rdd < 1.25) {
                    double reach = 5.0 + e.getBbWidth() / 2.0;
                    if (this.distanceToSqr(e) < reach * reach) {
                        this.setAttacking(1);
                        if (this.random.nextInt(5) == 0 || this.random.nextInt(6) == 1) {
                            this.doHurtTarget(e);
                            for (int i = 0; i < 6; i++) {
                                this.destroyBlockNear(e);
                            }
                        }
                        this.destroyNearbyBlocks();
                    }
                } else {
                    this.setAttacking(0);
                }
                this.getNavigation().moveTo(e, 1.0);
            } else {
                this.setAttacking(0);
            }
        }

        // gold idle smash just_for_fun
        if (this.getAttacking() == 0 && OreSpawnMain.PlayNicely == 0) {
            if (this.random.nextInt(450) == 1) {
                this.justForFun = 50;
            }
            if (this.justForFun > 0) {
                this.justForFun--;
            }
            if (this.justForFun > 0) {
                this.setAttacking(1);
                if (this.random.nextInt(3) == 1) {
                    this.destroyNearbyBlocks();
                }
            } else {
                this.setAttacking(0);
            }
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
        // gold box expand(14, 3, 14)
        return GoldStyleCombat.findTarget(this, 14.0, 3.0, this::isSuitableTarget);
    }

    /**
     * Gold getCanSpawnHere: spawner "Robo-Pounder"; else y≥50, night, clear headroom, light.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos origin = this.blockPosition();
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos p = origin.offset(j, i, k);
                    if (level.getBlockState(p).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be instanceof SpawnerBlockEntity) {
                            return true;
                        }
                    }
                }
            }
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        // gold clear air/tall grass above
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
