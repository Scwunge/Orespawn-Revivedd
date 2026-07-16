package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
 * Gold {@code CaterKiller} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.9×4.6 (PlayNicely 1.45×2.3 via renderer half-scale), speed 0.35,
 * health 450, attack 32, armor 19, XP 200. Category {@code MONSTER}.
 * Eats leaves/logs, places cobwebs, metamorphoses to Brutalfly when damaged long enough.
 */
public class CaterKiller extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(CaterKiller.class, EntityDataSerializers.BYTE);
    /** Gold datawatcher 21 — OreSpawnMain.PlayNicely mirror for client scale. */
    private static final EntityDataAccessor<Integer> PLAY_NICELY =
            SynchedEntityData.defineId(CaterKiller.class, EntityDataSerializers.INT);

    /** Gold CaterKiller_stats defaults: health 450, attack 32, defense 19. */
    private static final int HEALTH = 450;
    private static final int ATTACK = 32;
    private static final int DEFENSE = 19;

    private final float moveSpeed = 0.35F;
    private int foundmob = 0;
    private int ticker = 0;
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;

    public CaterKiller(EntityType<? extends CaterKiller> type, Level level) {
        super(type, level);
        this.xpReward = 200; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, ATTACK)
                .add(Attributes.ARMOR, DEFENSE)
                .add(Attributes.FOLLOW_RANGE, 40.0); // gold field_70174_ab = 100; combat box 20
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, MoveThroughVillage (skipped), WanderALot(16,1), WatchClosest, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(PLAY_NICELY, OreSpawnMain.PlayNicely);
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

    public int mygetMaxHealth() {
        return HEALTH;
    }

    public int getPlayNicely() {
        return this.entityData.get(PLAY_NICELY);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
        }
        return ret;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 1/3 orespawn:caterkiller_living
        if (this.random.nextInt(3) != 0) {
            return null;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:caterkiller_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:caterkiller_death
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

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: CaterKillerJaw, leather, 10 bone, 6 porkchop
        this.dropItemRand(ModItems.CATERKILLER_JAW.get(), 1);
        this.dropItemRand(Items.LEATHER, 1);
        for (int i = 0; i < 10; i++) {
            this.dropItemRand(Items.BONE, 1);
        }
        for (int i = 0; i < 6; i++) {
            this.dropItemRand(Items.PORKCHOP, 1);
        }
        this.dropItemRand(Items.PORKCHOP, 1);

        int i = 1 + this.random.nextInt(5);
        for (int n = 0; n < i; n++) {
            int roll = this.random.nextInt(20);
            switch (roll) {
                case 0 -> this.dropItemRand(ModItems.ULTIMATE_SWORD.get(), 1);
                case 1 -> this.dropItemRand(ModItems.RUBY.get(), 1);
                case 2 -> this.dropItemRand(Items.EMERALD_BLOCK, 1);
                case 3 -> this.dropItemRand(ModItems.RUBY_SWORD.get(), 1);
                case 4 -> this.dropItemRand(ModItems.RUBY_SHOVEL.get(), 1);
                case 5 -> this.dropItemRand(ModItems.RUBY_PICKAXE.get(), 1);
                case 6 -> this.dropItemRand(ModItems.RUBY_AXE.get(), 1);
                case 7 -> this.dropItemRand(ModItems.RUBY_HOE.get(), 1);
                case 8 -> this.dropItemRand(ModItems.RUBY_HELMET.get(), 1);
                case 9 -> this.dropItemRand(ModItems.RUBY_CHESTPLATE.get(), 1);
                case 10 -> this.dropItemRand(ModItems.RUBY_LEGGINGS.get(), 1);
                case 11 -> this.dropItemRand(ModItems.RUBY_BOOTS.get(), 1);
                case 12 -> this.dropItemRand(ModItems.ULTIMATE_BOW.get(), 1);
                default -> {
                }
            }
        }

        for (int b = 0; b < 25; b++) {
            spawnCreature(level, ModEntities.BUTTERFLY.get(), this.getX(), this.getY() + 1.0, this.getZ());
        }
    }

    private ItemStack dropItemRand(Item index, int count) {
        ItemStack is = new ItemStack(index, count);
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(5) - this.random.nextInt(5),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(5) - this.random.nextInt(5),
                is);
        this.level().addFreshEntity(var3);
        return is;
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

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 1.2;
            double inair = 0.1;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    /** Gold edible forage: leaves, vines, logs (+ custom OreSpawn leaves/logs). */
    private boolean isEdiblePlant(BlockState state) {
        return state.is(BlockTags.LEAVES)
                || state.is(BlockTags.LOGS)
                || state.is(Blocks.VINE)
                || state.is(Blocks.CAVE_VINES)
                || state.is(Blocks.CAVE_VINES_PLANT);
    }

    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isEdiblePlant(bid)) {
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
                if (this.isEdiblePlant(bid)) {
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
                if (this.isEdiblePlant(bid)) {
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
                if (this.isEdiblePlant(bid)) {
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
                if (this.isEdiblePlant(bid)) {
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
                if (this.isEdiblePlant(bid)) {
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
     * Gold {@code updateAITasks}: PlayNicely sync; low-health → Brutalfly metamorphosis;
     * clear cobwebs; combat + cobweb traps; eat leaves/logs.
     */
    @Override
    protected void customServerAiStep() {
        if (this.isDeadOrDying()) {
            return;
        }
        super.customServerAiStep();
        this.entityData.set(PLAY_NICELY, OreSpawnMain.PlayNicely);

        // gold: if health+1 < maxHealth → ticker++; after 2400 spawn Brutalfly + butterflies + die
        if (this.getHealth() + 1.0F < this.getMaxHealth()) {
            this.ticker++;
            if (this.ticker > 2400) {
                spawnCreature(
                        this.level(),
                        ModEntities.BRUTALFLY.get(),
                        this.getX(),
                        this.getY() + 4.0,
                        this.getZ());
                this.playSound(
                        SoundEvents.GENERIC_EXPLODE.value(),
                        1.0F,
                        this.random.nextFloat() * 0.2F + 0.9F);
                for (int i = 0; i < 10; i++) {
                    spawnCreature(
                            this.level(),
                            ModEntities.BUTTERFLY.get(),
                            this.getX(),
                            this.getY() + 1.0 + this.random.nextInt(4),
                            this.getZ());
                }
                this.discard();
                return;
            }
        }

        // gold isInWeb: clear cobwebs in volume, clear stuck flag
        this.clearCobwebsAround();

        if (this.random.nextInt(4) == 0) {
            LivingEntity e = this.getTarget();
            if (e != null && !e.isAlive()) {
                this.setTarget(null);
                e = null;
            }
            if (this.random.nextInt(200) == 0) {
                this.setTarget(null);
            }
            if (e == null) {
                e = this.findSomethingToAttack();
                if (e != null) {
                    this.setTarget(e);
                }
            }
            if (e != null) {
                this.foundmob = 1;
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                // gold: distSq < (5 + width/2)^2
                if (GoldStyleCombat.inMeleeRange(this, e, 5.0)) {
                    this.setAttacking(1);
                    if (this.random.nextInt(3) == 0 || this.random.nextInt(4) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.setAttacking(0);
                    this.getNavigation().moveTo(e, 1.25);
                    if (this.random.nextInt(4) == 0) {
                        double dx = e.getX();
                        double dz = e.getZ();
                        dx += (this.random.nextFloat() - this.random.nextFloat()) * 2.0;
                        dz += (this.random.nextFloat() - this.random.nextFloat()) * 2.0;
                        for (int i = 2; i > -2; i--) {
                            BlockPos above = BlockPos.containing(dx, e.getY() + i + 1, dz);
                            BlockPos below = BlockPos.containing(dx, e.getY() + i, dz);
                            if (this.level().getBlockState(above).isAir()
                                    && !this.level().getBlockState(below).isAir()) {
                                this.level().setBlock(above, Blocks.COBWEB.defaultBlockState(), 3);
                                break;
                            }
                        }
                    }
                }
            } else {
                this.setAttacking(0);
                this.foundmob = 0;
            }
        }

        // gold: (1/8 when hurt OR 1/30) && PlayNicely==0 → forage leaves/logs
        if (((this.random.nextInt(8) == 0 && this.getHealth() < this.mygetMaxHealth())
                        || this.random.nextInt(30) == 0)
                && OreSpawnMain.PlayNicely == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 13; i++) {
                int j = i;
                if (j > 9) {
                    j = 9;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 9) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                if (this.foundmob == 0) {
                    this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                }
                if (this.closest < 81) {
                    if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level().setBlock(
                                new BlockPos(this.tx, this.ty, this.tz),
                                Blocks.AIR.defaultBlockState(),
                                2);
                    }
                    this.heal(2.0F);
                    if (this.random.nextInt(20) == 1) {
                        this.playSound(
                                SoundEvents.PLAYER_BURP,
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.9F);
                    }
                }
            }
        }
    }

    private void clearCobwebsAround() {
        boolean any = false;
        for (int i = -2; i <= 2; i++) {
            for (int j = -1; j < 5; j++) {
                for (int k = -2; k <= 2; k++) {
                    BlockPos pos = BlockPos.containing(this.getX() + i, this.getY() + j, this.getZ() + k);
                    if (this.level().getBlockState(pos).is(Blocks.COBWEB)) {
                        this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        any = true;
                    }
                }
            }
        }
        if (any) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 1.0, 1.0));
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.myCanSee(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.getAbilities().instabuild && !player.isSpectator();
        }
        if (target instanceof CaterKiller) {
            return false;
        }
        if (target instanceof Monster) {
            return true;
        }
        // gold MyUtils.isAttackableNonMob
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(20, 8, 20)
        return GoldStyleCombat.findTarget(this, 20.0, 8.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code MyCanSee}: stepped ray from front; sees through air/cobweb/tallgrass/leaves.
     */
    public boolean myCanSee(LivingEntity e) {
        double xzoff = 2.5;
        int nblks = 10;
        double yawRad = Math.toRadians(this.getYRot());
        double cx = this.getX() - xzoff * Math.sin(yawRad);
        double cz = this.getZ() + xzoff * Math.cos(yawRad);
        float startx = (float) cx;
        float starty = (float) (this.getY() + 3.0);
        float startz = (float) cz;
        float dx = (float) ((e.getX() - startx) / 10.0);
        float dy = (float) ((e.getY() + e.getBbHeight() / 2.0F - starty) / 10.0);
        float dz = (float) ((e.getZ() - startz) / 10.0);
        if (Math.abs(dx) > 1.0F) {
            dy /= Math.abs(dx);
            dz /= Math.abs(dx);
            nblks = (int) (nblks * Math.abs(dx));
            if (dx > 1.0F) {
                dx = 1.0F;
            }
            if (dx < -1.0F) {
                dx = -1.0F;
            }
        }
        if (Math.abs(dy) > 1.0F) {
            dx /= Math.abs(dy);
            dz /= Math.abs(dy);
            nblks = (int) (nblks * Math.abs(dy));
            if (dy > 1.0F) {
                dy = 1.0F;
            }
            if (dy < -1.0F) {
                dy = -1.0F;
            }
        }
        if (Math.abs(dz) > 1.0F) {
            dy /= Math.abs(dz);
            dx /= Math.abs(dz);
            nblks = (int) (nblks * Math.abs(dz));
            if (dz > 1.0F) {
                dz = 1.0F;
            }
            if (dz < -1.0F) {
                dz = -1.0F;
            }
        }
        for (int i = 0; i < nblks; i++) {
            startx += dx;
            starty += dy;
            startz += dz;
            BlockState bid = this.level().getBlockState(BlockPos.containing(startx, starty, startz));
            if (bid.isAir()
                    || bid.is(Blocks.COBWEB)
                    || bid.is(Blocks.SHORT_GRASS)
                    || bid.is(Blocks.TALL_GRASS)
                    || bid.is(BlockTags.LEAVES)
                    || bid.is(BlockTags.REPLACEABLE)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner name CaterKiller; else y≥50, day, 1/10, headroom air/leaves/logs,
     * no nearby CaterKiller in expand(48,16,48).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        // gold spawner check
        for (int k = -3; k < 3; k++) {
            for (int j = -3; j < 3; j++) {
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k);
                    if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof SpawnerBlockEntity) {
                            // accept any spawner near spawn attempt as gold name match stand-in when forced
                            if (spawnType == MobSpawnType.SPAWNER) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        if (this.getY() < 50.0) {
            return false;
        }
        if (this.random.nextInt(10) != 0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }

        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 2; j++) {
                for (int i = 1; i < 5; i++) {
                    BlockState bid =
                            level.getBlockState(BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k));
                    if (!bid.isAir()
                            && !bid.is(BlockTags.LEAVES)
                            && !bid.is(BlockTags.LOGS)) {
                        return false;
                    }
                }
            }
        }

        return level.getEntitiesOfClass(CaterKiller.class, this.getBoundingBox().inflate(48.0, 16.0, 48.0))
                .isEmpty();
    }
}
