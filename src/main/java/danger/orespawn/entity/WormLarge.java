package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code WormLarge} (EntityMob). Size 1.55×2.5, speed 0.2, health 90, attack 18, armor 14, XP 2050.
 * Spawns 20 small + 20 medium worms once; hunts when no medium worms nearby.
 */
public class WormLarge extends Monster {
    private int wormsSpawned = 0;

    public WormLarge(EntityType<? extends WormLarge> type, Level level) {
        super(type, level);
        this.xpReward = 2050;
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 90.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 18.0)
                .add(Attributes.ARMOR, 14.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMoveThroughVillage — skipped
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public int mygetMaxHealth() {
        return 90;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.BIG_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold onDeath plays ALOSAURUS_DEATH only (not BIG_SPLAT)
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    public void pointAtEntity(LivingEntity e) {
        double d1 = e.getX() - this.getX();
        double d2 = e.getZ() - this.getZ();
        float d = (float) Math.atan2(d2, d1);
        float f2 = (float) (d * 180.0 / Math.PI) - 90.0F;
        this.setYRot(f2);
        this.yBodyRot = f2;
        this.yHeadRot = f2;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        WormMedium worms = findNearest(WormMedium.class, 8.0, 8.0, 8.0);
        Player target = null;
        if (worms == null) {
            target = findNearest(Player.class, 8.0, 8.0, 8.0);
        }
        // gold: (worms != null || target == null) && PlayNicely == 0 → burrow mode
        if ((worms != null || target == null) && OreSpawnMain.PlayNicely == 0) {
            this.noPhysics = true;
            BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + 3.5, this.getZ()));
            if (isTallGrass(bid)) {
                bid = Blocks.AIR.defaultBlockState();
            }
            if (!bid.isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.1, 0.0));
                this.setPos(this.getX(), this.getY() + 0.05F, this.getZ());
                if (!isSoftEarth(bid)) {
                    this.discard();
                    return;
                }
            }
        } else {
            if (target != null) {
                this.pointAtEntity(target);
            }
            BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY(), this.getZ()));
            if (isTallGrass(bid)) {
                bid = Blocks.AIR.defaultBlockState();
            }
            if (!bid.isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
                this.setPos(this.getX(), this.getY() + 0.1F, this.getZ());
            } else {
                this.noPhysics = false;
            }
        }
        if (this.noPhysics) {
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(0.0, m.y - 0.01, 0.0);
            this.zza = 0.0F;
        }
        if (!this.level().isClientSide && this.wormsSpawned == 0) {
            this.wormsSpawned = 1;
            for (int i = 0; i < 20; i++) {
                spawnCreature(
                        this.level(),
                        ModEntities.SMALL_WORM.get(),
                        this.getX() + this.random.nextInt(6) - this.random.nextInt(6),
                        this.getY(),
                        this.getZ() + this.random.nextInt(6) - this.random.nextInt(6));
                spawnCreature(
                        this.level(),
                        ModEntities.MEDIUM_WORM.get(),
                        this.getX() + this.random.nextInt(5) - this.random.nextInt(5),
                        this.getY(),
                        this.getZ() + this.random.nextInt(5) - this.random.nextInt(5));
            }
        }
    }

    @Override
    public void tick() {
        // gold: if (isNoDespawnRequired / func_104002_bU) noClip = false
        if (this.isPersistenceRequired()) {
            this.noPhysics = false;
        }
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.85, m.z);
    }

    /** Gold {@code canTriggerWalking} false → ignore pressure plates / crops. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        if (!this.noPhysics) {
            super.customServerAiStep();
        } else {
            // still need super for living AI bookkeeping lightly
        }
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: combat only when PlayNicely == 0
        if (OreSpawnMain.PlayNicely != 0) {
            return;
        }
        WormMedium worms = findNearest(WormMedium.class, 8.0, 8.0, 8.0);
        if (worms == null) {
            Player target = findNearest(Player.class, 8.0, 6.0, 8.0);
            if (target != null && target.getAbilities().instabuild) {
                target = null;
            }
            if (target != null) {
                this.pointAtEntity(target);
                this.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0);
                if (this.random.nextInt(10) == 1 && this.distanceToSqr(target) < 3.0) {
                    this.doHurtTarget(target);
                    if (this.random.nextInt(4) == 1) {
                        // gold equipment slot 4 = helmet
                        stripEquipment(target, EquipmentSlot.HEAD, 10);
                    }
                    if (this.random.nextInt(4) == 1) {
                        // gold equipment slot 0 = main hand
                        stripEquipment(target, EquipmentSlot.MAINHAND, 10);
                    }
                }
            }
        }
    }

    private void stripEquipment(Player target, EquipmentSlot slot, int wearDivisor) {
        ItemStack stack = target.getItemBySlot(slot);
        if (stack.isEmpty()) {
            return;
        }
        target.setItemSlot(slot, ItemStack.EMPTY);
        int bid = stack.getMaxDamage() - stack.getDamageValue();
        if (stack.isDamageableItem()) {
            if (bid > wearDivisor) {
                bid /= wearDivisor;
            } else {
                bid = 1;
            }
            stack.setDamageValue(Math.min(stack.getMaxDamage() - 1, stack.getDamageValue() + bid));
        }
        ItemEntity drop = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(5) - this.random.nextInt(5),
                this.getY() + 3.0,
                this.getZ() + this.random.nextInt(5) - this.random.nextInt(5),
                stack);
        this.level().addFreshEntity(drop);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (!this.noPhysics) {
            return super.causeFallDamage(fallDistance, multiplier, source);
        }
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        if (!this.noPhysics) {
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    // canBreatheUnderwater() is final on LivingEntity in 1.21 — gold returned true; cannot override

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold: near Large Worm spawner → force ok and mark wormsSpawned
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k);
                    if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof SpawnerBlockEntity) {
                            // spawner entity id check best-effort; mark spawned if any nearby spawner
                            this.wormsSpawned = 1;
                            return true;
                        }
                    }
                }
            }
        }
        if (this.getY() < 50.0) {
            return false;
        }
        WormLarge other = findNearest(WormLarge.class, 32.0, 8.0, 32.0);
        if (other != null) {
            return false;
        }
        for (int i = -6; i <= 6; i++) {
            for (int j = -6; j <= 6; j++) {
                for (int var13 = -2; var13 >= -8; var13--) {
                    BlockState bid = level.getBlockState(BlockPos.containing(this.getX() + i, this.getY() + var13, this.getZ() + j));
                    if (bid.isAir()) {
                        return false;
                    }
                }
            }
        }
        for (int var10 = -6; var10 <= 6; var10++) {
            for (int j = -6; j <= 6; j++) {
                for (int var14 = 2; var14 <= 8; var14++) {
                    BlockState bid = level.getBlockState(BlockPos.containing(this.getX() + var10, this.getY() + var14, this.getZ() + j));
                    if (!bid.isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId()) || source == this.damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("wormsSpawned", this.wormsSpawned);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wormsSpawned = tag.getInt("wormsSpawned");
    }

    public static Entity spawnCreature(Level level, EntityType<? extends Mob> type, double x, double y, double z) {
        if (!(level instanceof ServerLevel server)) {
            return null;
        }
        Entity e = type.create(server);
        if (e != null) {
            e.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
            if (e instanceof Mob mob) {
                mob.finalizeSpawn(server, server.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            }
            level.addFreshEntity(e);
        }
        return e;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        dropItemRand(new ItemStack(Items.NAME_TAG));
        for (int i = 0; i < 6; i++) {
            dropItemRand(new ItemStack(Items.ROTTEN_FLESH));
        }
        for (int i = 0; i < 6; i++) {
            dropItemRand(new ItemStack(Items.LEATHER));
        }
        for (int i = 0; i < 8; i++) {
            dropItemRand(new ItemStack(Blocks.DIRT));
        }
        for (int i = 0; i < 16; i++) {
            dropItemRand(new ItemStack(Items.IRON_INGOT));
        }
        for (int i = 0; i < 5; i++) {
            dropItemRand(new ItemStack(Items.DIAMOND));
        }
        for (int i = 0; i < 4; i++) {
            dropItemRand(new ItemStack(ModItems.URANIUM_NUGGET.get()));
        }
        for (int i = 0; i < 4; i++) {
            dropItemRand(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
        }
        for (int i = 0; i < 2; i++) {
            dropItemRand(new ItemStack(ModItems.WORM_TOOTH.get()));
        }
    }

    private void dropItemRand(ItemStack stack) {
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(4) - this.random.nextInt(4),
                this.getY() + 2.5 + this.random.nextInt(4),
                this.getZ() + this.random.nextInt(4) - this.random.nextInt(4),
                stack);
        this.level().addFreshEntity(var3);
    }

    @Nullable
    private <T extends Entity> T findNearest(Class<T> cls, double dx, double dy, double dz) {
        AABB box = this.getBoundingBox().inflate(dx, dy, dz);
        List<T> list = this.level().getEntitiesOfClass(cls, box);
        T nearest = null;
        double best = Double.MAX_VALUE;
        for (T e : list) {
            if (e == this) {
                continue;
            }
            double d = this.distanceToSqr(e);
            if (d < best) {
                best = d;
                nearest = e;
            }
        }
        return nearest;
    }

    private static boolean isTallGrass(BlockState state) {
        return state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN);
    }

    private static boolean isSoftEarth(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.ROOTED_DIRT) || state.is(Blocks.PODZOL) || state.is(Blocks.MYCELIUM)
                || state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE);
    }
}
