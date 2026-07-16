package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Molenoid} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 3.9Ã—2.6, speed 0.35, health 200, attack 18, armor 12, XP 40.
 * Digs soft earth, dumps dirt mounds, hunts through dirt (custom MyCanSee).
 */
public class Molenoid extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Molenoid.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.35F;

    public Molenoid(EntityType<? extends Molenoid> type, Level level) {
        super(type, level);
        this.xpReward = 40; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0) // Molenoid_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 18.0) // Molenoid_stats.attack
                .add(Attributes.ARMOR, 12.0) // Molenoid_stats.defense
                .add(Attributes.FOLLOW_RANGE, 48.0); // gold field_70174_ab = 100; scan 12
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
        return 200;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: 1/3 orespawn:molenoid_living â€” SoundsHandler
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold orespawn:molenoid_hit
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:molenoid_death
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 1.1F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: MolenoidNose, bone, 10 gold nugget, 6+1 porkchop
        this.dropItemRand(new ItemStack(ModItems.MOLENOID_NOSE.get()));
        this.dropItemRand(new ItemStack(Items.BONE));
        for (int i = 0; i < 10; i++) {
            this.dropItemRand(new ItemStack(Items.GOLD_NUGGET));
        }
        for (int i = 0; i < 6; i++) {
            this.dropItemRand(new ItemStack(Items.PORKCHOP));
        }
        this.dropItemRand(new ItemStack(Items.PORKCHOP));
    }

    private void dropItemRand(ItemStack stack) {
        this.spawnAtLocation(stack);
    }

    /** Gold: ignore inWall damage. */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId()) || source == this.damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code attackEntityAsMob}: attribute damage + knockback (ks=0.8, inair=0.1, Ã—2 if player/dead).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 0.8;
            double inair = 0.1;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (target.isRemoved() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    /**
     * Gold {@code updateAITasks}: combat 1/4; dirt dump near target; dig tunnel ahead; dirt trail while moving.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        LivingEntity e = null;
        if (this.random.nextInt(4) == 0) {
            e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                // gold: distSq < (6 + width/2)^2
                if (GoldStyleCombat.inMeleeRange(this, e, 6.0)) {
                    this.setAttacking(1);
                    // gold: if distSq < 16 â†’ melee on nextInt(4)==0 || nextInt(5)==1; else dump dirt
                    if (this.distanceToSqr(e) < 16.0
                            && (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1)) {
                        this.doHurtTarget(e);
                    } else if (OreSpawnMain.PlayNicely == 0) {
                        int j = 1 + this.random.nextInt(4);
                        for (int k = 0; k < j; k++) {
                            double dx = e.getX()
                                    + (this.random.nextFloat() - this.random.nextFloat()) * 2.0;
                            double dz = e.getZ()
                                    + (this.random.nextFloat() - this.random.nextFloat()) * 2.0;
                            this.placeDirtMound(dx, e.getY(), dz, 4, -3);
                        }
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.25);
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (!this.level().isClientSide) {
            // gold: dirt trail while moving (odds from horizontal speed)
            if (this.random.nextInt(2) == 0) {
                Vec3 m = this.getDeltaMovement();
                double spd = Math.sqrt(m.x * m.x + m.z * m.z);
                if (spd > this.moveSpeed) {
                    spd = this.moveSpeed;
                }
                int odds = (int) (100.0 * spd / this.moveSpeed);
                if (odds > 0
                        && this.random.nextInt(100) < odds
                        && OreSpawnMain.PlayNicely == 0) {
                    double yawRad = Math.toRadians(this.yBodyRot);
                    double dx = this.getX() + 6.0 * Math.sin(yawRad);
                    double dz = this.getZ() - 6.0 * Math.cos(yawRad);
                    dx += (this.random.nextFloat() - this.random.nextFloat()) * 3.0;
                    dz += (this.random.nextFloat() - this.random.nextFloat()) * 3.0;
                    this.placeDirtMound(dx, this.getY(), dz, 4, -4);
                }
            }

            // gold: dig tunnel in facing direction (dir by target height)
            double yawRad = Math.toRadians(this.yBodyRot);
            double dx = this.getX() - 3.0 * Math.sin(yawRad);
            double dz = this.getZ() + 3.0 * Math.cos(yawRad);
            dx += (this.random.nextFloat() - this.random.nextFloat()) * 3.0;
            dz += (this.random.nextFloat() - this.random.nextFloat()) * 3.0;
            int dir = 1;
            if (e != null) {
                if ((int) e.getY() > (int) this.getY()) {
                    dir = 2;
                }
                if ((int) e.getY() < (int) this.getY()) {
                    dir = 0;
                }
            }
            if (OreSpawnMain.PlayNicely == 0) {
                for (int i = dir; i < dir + 3; i++) {
                    BlockPos pos = BlockPos.containing(dx, this.getY() + i, dz);
                    BlockState state = this.level().getBlockState(pos);
                    // gold: soft earth under mobGriefing; MyMoleDirtBlock always (dirt stand-in for mounds)
                    if (isSoftDiggable(state)
                            && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    /**
     * Gold places {@code MyMoleDirtBlock} above solid surface. Caller may wire
     * {@code ModBlocks.MOLE_DIRT}; until then uses dirt as stand-in.
     */
    private void placeDirtMound(double dx, double baseY, double dz, int yMax, int yMin) {
        for (int i = yMax; i > yMin; i--) {
            BlockPos above = BlockPos.containing(dx, baseY + i + 1, dz);
            BlockPos below = BlockPos.containing(dx, baseY + i, dz);
            if (this.level().getBlockState(above).isAir()
                    && !this.level().getBlockState(below).isAir()) {
                this.level().setBlock(above, Blocks.DIRT.defaultBlockState(), 3);
                break;
            }
        }
    }

    private static boolean isSoftDiggable(BlockState state) {
        // gold: dirt, grass, gravel, sand, leaves
        return state.is(Blocks.DIRT)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD)
                || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.SAND);
    }

    /**
     * Gold {@code isSuitableTarget}: MyCanSee; players (not creative); other Molenoid no;
     * EntityMob yes; else isAttackableNonMob â†’ true for non-monster living.
     */
    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.myCanSee(target)) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        if (target instanceof Molenoid) {
            return false;
        }
        if (target instanceof Monster) {
            return true;
        }
        // gold MyUtils.isAttackableNonMob â€” animals etc.
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(12, 6, 12)
        return GoldStyleCombat.findTarget(this, 12.0, 6.0, this::isSuitableTarget);
    }

    /**
     * Gold {@code MyCanSee}: stepped ray from snout; sees through air/dirt/grass/sand/gravel/tall grass
     * / mole dirt (dirt stand-in).
     */
    public boolean myCanSee(LivingEntity e) {
        double xzoff = 2.0;
        int nblks = 10;
        double yawRad = Math.toRadians(this.getYRot());
        double cx = this.getX() - xzoff * Math.sin(yawRad);
        double cz = this.getZ() + xzoff * Math.cos(yawRad);
        float startx = (float) cx;
        float starty = (float) (this.getY() + 1.0);
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
                    || bid.is(Blocks.DIRT)
                    || bid.is(Blocks.GRASS_BLOCK)
                    || bid.is(Blocks.SHORT_GRASS)
                    || bid.is(Blocks.TALL_GRASS)
                    || bid.is(Blocks.SAND)
                    || bid.is(Blocks.RED_SAND)
                    || bid.is(Blocks.GRAVEL)
                    || bid.is(BlockTags.SAND)
                    || bid.is(BlockTags.REPLACEABLE)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Gold {@code getCanSpawnHere}: light; y â‰¥ 50; night; headroom air; no nearby Molenoid in 16.
     * Spawner name check simplified away.
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
        for (int k = -1; k < 1; k++) {
            for (int j = -1; j < 1; j++) {
                for (int i = 1; i < 4; i++) {
                    if (!level.getBlockState(BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k))
                            .isAir()) {
                        return false;
                    }
                }
            }
        }
        return level
                .getEntitiesOfClass(Molenoid.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0))
                .isEmpty();
    }
}
