package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.GoldStyleCombat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Vortex} (EntityMob) 1:1 for NeoForge 1.21.1.
 * Size 2.0×4.0, speed 0.35, health 150, attack 26, armor 10, XP 200.
 * Flying wind-pull hunter; night-only natural spawn; smoke FX while fighting.
 */
public class Vortex extends Monster {
    @Nullable
    private BlockPos currentFlightTarget;
    private int winded = 0;
    private int busyFighting = 0;
    /** Gold {@code was_spawnered} — prevents day despawn when from spawner. */
    private int wasSpawnered = 0;

    public Vortex(EntityType<? extends Vortex> type, Level level) {
        super(type, level);
        this.xpReward = 200; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0) // Vortex_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 26.0) // Vortex_stats.attack
                .add(Attributes.ARMOR, 10.0) // Vortex_stats.defense
                .add(Attributes.FOLLOW_RANGE, 64.0); // gold field_70174_ab = 250; scan 16
    }

    @Override
    protected void registerGoals() {
        // gold: no AI goals — pure updateAITasks flight/combat
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: if noDespawnRequired → false; if busy_fighting → false; else was_spawnered==0
        if (this.isPersistenceRequired()) {
            return false;
        }
        if (this.busyFighting != 0) {
            return false;
        }
        return this.wasSpawnered == 0;
    }

    public int mygetMaxHealth() {
        return 150;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold orespawn:vortexlive
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold orespawn:vortexlive
        return null;
    }

    /** Gold {@code canBePushed} true. */
    @Override
    public boolean isPushable() {
        return true;
    }

    /** Gold empty {@code collideWithEntity}. */
    @Override
    protected void doPush(Entity entity) {}

    @Override
    public void tick() {
        super.tick();
        // gold: motionY *= 0.6
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.6, m.z);

        this.busyFighting = 0;
        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.busyFighting = 1;
            if (this.level().isClientSide) {
                for (int i = 0; i < 20; i++) {
                    double d = this.random.nextDouble() * 3.5;
                    d *= d;
                    double dir = this.random.nextDouble() * 2.0 * Math.PI;
                    dir -= Math.PI;
                    double dx = Math.cos(dir) * d / 2.0;
                    double dz = Math.sin(dir) * d / 2.0;
                    dir += Math.PI / 2;
                    this.level()
                            .addParticle(
                                    ParticleTypes.SMOKE,
                                    this.getX() + dx,
                                    this.getY() + 0.75 + d,
                                    this.getZ() + dz,
                                    Math.cos(dir) * this.random.nextFloat() / 4.0,
                                    this.random.nextFloat() / 2.0F,
                                    Math.sin(dir) * this.random.nextFloat() / 4.0);
                }
            }
        }

        if (this.random.nextInt(200) == 1) {
            this.heal(1.0F);
        }

        // gold: if not noDespawnRequired and idle and not spawnered and daytime 1/500 → die
        if (!this.isPersistenceRequired()
                && this.busyFighting == 0
                && this.wasSpawnered == 0) {
            long t = this.level().getDayTime() % 24000L;
            if (t < 12000L && this.random.nextInt(500) == 1) {
                this.discard();
            }
        }
    }

    /** Gold {@code canSeeTarget} — ray from y+0.75; MISS means clear. */
    private boolean canSeeTarget(double pX, double pY, double pZ) {
        return this.level()
                        .clip(new ClipContext(
                                new Vec3(this.getX(), this.getY() + 0.75, this.getZ()),
                                new Vec3(pX, pY, pZ),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this))
                        .getType()
                == HitResult.Type.MISS;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = BlockPos.containing(this.getX(), this.getY(), this.getZ());
        }

        if (this.winded > 0) {
            this.winded--;
        }

        // gold: repath if nextInt(300)==0 OR within 2.1 of target
        if (this.random.nextInt(300) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.1F) {
            int keepTrying = 50;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                int zdir = this.random.nextInt(14) + 10;
                int xdir = this.random.nextInt(14) + 10;
                if (this.random.nextInt(2) == 0) {
                    zdir = -zdir;
                }
                if (this.random.nextInt(2) == 0) {
                    xdir = -xdir;
                }
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + xdir,
                        this.getY() + this.random.nextInt(6) - 3,
                        this.getZ() + zdir);
                bid = this.level().getBlockState(this.currentFlightTarget);
                if (bid.isAir()
                        && !this.canSeeTarget(
                                this.currentFlightTarget.getX(),
                                this.currentFlightTarget.getY(),
                                this.currentFlightTarget.getZ())) {
                    bid = Blocks.STONE.defaultBlockState();
                }
            }
        }

        LivingEntity e = this.findSomethingToAttack();
        if (e != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
            double d = this.distanceToSqr(e);
            // gold wind pull: d < 81 && winded == 0
            if (d < 81.0 && this.winded == 0) {
                double a = Math.atan2(this.getZ() - e.getZ(), this.getX() - e.getX());
                double pm = e instanceof Player ? 2.0 : 1.0;
                double pull = 10.0 - Math.sqrt(d);
                e.push(Math.cos(a) * pull * 0.1F, pull * 0.05F * pm, Math.sin(a) * pull * 0.1F);
            }
            // gold melee: distSq < (4 + width/2)^2 && nextInt(8)==2
            if (GoldStyleCombat.inMeleeRange(this, e, 4.0) && this.random.nextInt(8) == 2) {
                this.doHurtTarget(e);
            }
        }

        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.4 - m.x) * 0.2,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.20000000149011612,
                m.z + (Math.signum(var5) * 0.4 - m.z) * 0.2);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI)
                - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.75F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isIgnoringBlockTriggers() {
        // gold canTriggerWalking true → default false for isIgnoringBlockTriggers
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        Entity e = source.getEntity();
        if (e != null && this.currentFlightTarget != null) {
            this.currentFlightTarget = BlockPos.containing(e.getX(), e.getY(), e.getZ());
        }
        this.winded = 20;
        return ret;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return GoldStyleCombat.dealAttributeDamage(this, target);
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold MyUtils.isIgnoreable deferred
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player player) {
            if (player.isSpectator() || player.getAbilities().instabuild) {
                return false;
            }
        }
        if (target instanceof Vortex) {
            return false;
        }
        if (target instanceof Mothra) {
            return false;
        }
        if (target instanceof Brutalfly) {
            return false;
        }
        if (target instanceof Peacock) {
            return false;
        }
        if (target instanceof Irukandji) {
            return false;
        }
        if (target instanceof Skate) {
            return false;
        }
        if (target instanceof Flounder) {
            return false;
        }
        // gold also skips Rotator, CrystalCow, Whale, Urchin (unported)
        return true;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold box expand(16, 10, 16)
        return GoldStyleCombat.findTarget(this, 16.0, 10.0, this::isSuitableTarget);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: VortexEye, bone; 5+rand(7) random loot table
        this.spawnAtLocation(new ItemStack(ModItems.VORTEX_EYE.get()));
        this.spawnAtLocation(new ItemStack(Items.BONE));
        int i = 5 + this.random.nextInt(7);
        for (int n = 0; n < i; n++) {
            int var3 = this.random.nextInt(10);
            if (var3 == 0) {
                this.spawnAtLocation(new ItemStack(Items.APPLE));
            }
            if (var3 == 1) {
                this.spawnAtLocation(new ItemStack(ModItems.TIGERSEYE_INGOT.get()));
            }
            if (var3 == 2) {
                this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_PINK_INGOT.get()));
            }
            if (var3 == 3) {
                this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
            }
            if (var3 == 4) {
                this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
            }
            if (var3 == 6) {
                this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
            }
            if (var3 == 7) {
                this.spawnAtLocation(new ItemStack(ModItems.DEAD_IRUKANDJI.get()));
            }
            if (var3 == 8) {
                this.spawnAtLocation(new ItemStack(ModBlocks.CRYSTAL_COAL.get().asItem()));
            }
        }
        // gold getDropItem: FairyEgg
        this.spawnAtLocation(new ItemStack(ModItems.FAIRY_EGG.get()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WasSpawnered", this.wasSpawnered);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasSpawnered = tag.getInt("WasSpawnered");
    }

    /**
     * Gold {@code getCanSpawnHere}: air headroom; light; y ≥ 50; night; 50% roll; no nearby Vortex 20.
     * Spawner sets was_spawnered (simplified — spawners can set via NBT).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        for (int k = -2; k <= 2; k++) {
            for (int j = -2; j <= 2; j++) {
                for (int i = 1; i < 4; i++) {
                    if (!level.getBlockState(BlockPos.containing(this.getX() + j, this.getY() + i, this.getZ() + k))
                            .isAir()) {
                        return false;
                    }
                }
            }
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl) {
            long t = lvl.getDayTime() % 24000L;
            if (t < 12000L) {
                return false;
            }
        }
        if (this.random.nextInt(2) != 1) {
            return false;
        }
        return level
                .getEntitiesOfClass(Vortex.class, this.getBoundingBox().inflate(20.0, 16.0, 20.0))
                .isEmpty();
    }
}
