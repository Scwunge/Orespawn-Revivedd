package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code EnderReaper} (EntityMob / Enderman-clone) 1:1 for NeoForge 1.21.1.
 * Size 0.7×2.9, speed 0.37, health 90, attack 18, armor 8, XP 5.
 * Stare-aggro range 81; density check no other Reaper in 16×8×16; pumpkin blocks aggro.
 */
public class EnderReaper extends Monster {
    private static final EntityDataAccessor<Byte> DATA_CREEPY =
            SynchedEntityData.defineId(EnderReaper.class, EntityDataSerializers.BYTE);

    private static final ResourceLocation SPEED_MODIFIER_ATTACKING_ID =
            ResourceLocation.fromNamespaceAndPath("orespawn", "ender_reaper_attacking");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING =
            new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 6.2, AttributeModifier.Operation.ADD_VALUE);

    private int teleportDelay;
    private int stareTimer;
    @Nullable
    private LivingEntity lastEntityToAttack;

    public EnderReaper(EntityType<? extends EnderReaper> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 90.0) // EnderReaper_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.37)
                .add(Attributes.ATTACK_DAMAGE, 18.0) // EnderReaper_stats.attack
                .add(Attributes.ARMOR, 8.0) // EnderReaper_stats.defense
                .add(Attributes.FOLLOW_RANGE, 81.0) // gold stare range 81
                .add(Attributes.STEP_HEIGHT, 1.0); // gold field_70138_W
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CREEPY, (byte) 0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    public int mygetMaxHealth() {
        return 90;
    }

    public boolean isScreaming() {
        return this.entityData.get(DATA_CREEPY) > 0;
    }

    public void setScreaming(boolean screaming) {
        this.entityData.set(DATA_CREEPY, (byte) (screaming ? 1 : 0));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
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
        // gold: always ender_pearl; dropFewItems count nextInt(2+looting)
        int k = this.random.nextInt(2);
        for (int l = 0; l < k; l++) {
            this.spawnAtLocation(new ItemStack(Items.ENDER_PEARL));
        }
    }

    private boolean shouldAttackPlayer(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmet.isEmpty() && helmet.is(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        }
        Vec3 view = player.getViewVector(1.0F).normalize();
        Vec3 toMob = new Vec3(
                this.getX() - player.getX(),
                this.getBoundingBox().minY + this.getBbHeight() / 2.0F - (player.getY() + player.getEyeHeight()),
                this.getZ() - player.getZ());
        double d0 = toMob.length();
        toMob = toMob.normalize();
        double d1 = view.dot(toMob);
        return d1 > 1.0 - 0.025 / d0 && player.hasLineOfSight(this);
    }

    /** Gold stare range 81. */
    @Nullable
    private Player findStareTarget() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        Player player = this.level().getNearestPlayer(this, 81.0);
        if (player != null) {
            if (this.shouldAttackPlayer(player)) {
                if (this.stareTimer == 0) {
                    this.level().playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ENDERMAN_STARE,
                            player.getSoundSource(),
                            1.0F,
                            1.0F);
                }
                if (this.stareTimer++ == 5) {
                    this.stareTimer = 0;
                }
                this.setScreaming(true);
                return player;
            }
            this.stareTimer = 0;
            this.setScreaming(false);
        }
        return null;
    }

    @Override
    public void aiStep() {
        if (this.isInWaterOrRain()) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }

        if (this.lastEntityToAttack != this.getTarget()) {
            AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed != null) {
                speed.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
                if (this.getTarget() != null) {
                    speed.addTransientModifier(SPEED_MODIFIER_ATTACKING);
                }
            }
        }
        this.lastEntityToAttack = this.getTarget();

        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(
                        ParticleTypes.PORTAL,
                        this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25,
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                        (this.random.nextDouble() - 0.5) * 2.0,
                        -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5) * 2.0);
            }
        }

        if (this.level().isDay() && !this.level().isClientSide) {
            float f = this.getLightLevelDependentMagicValue();
            if (f > 0.5F
                    && this.level().canSeeSky(this.blockPosition())
                    && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setTarget(null);
                this.setScreaming(false);
                this.teleportRandomly();
            }
        }

        if (this.isInWaterOrRain() || this.isOnFire()) {
            this.setScreaming(false);
            this.teleportRandomly();
        }

        this.setJumping(false);

        if (this.getTarget() != null) {
            this.getLookControl().setLookAt(this.getTarget(), 100.0F, 100.0F);
        }

        if (!this.level().isClientSide && this.isAlive()) {
            if (this.getTarget() == null || this.getTarget() instanceof Player) {
                Player stare = this.findStareTarget();
                if (stare != null) {
                    this.setTarget(stare);
                }
            }

            LivingEntity target = this.getTarget();
            if (target != null) {
                if (target instanceof Player player && this.shouldAttackPlayer(player)) {
                    if (target.distanceToSqr(this) < 16.0) {
                        this.teleportRandomly();
                    }
                    this.teleportDelay = 0;
                } else if (target.distanceToSqr(this) > 256.0
                        && this.teleportDelay++ >= 30
                        && this.teleportTowards(target)) {
                    this.teleportDelay = 0;
                }
            } else {
                this.setScreaming(false);
                this.teleportDelay = 0;
            }
        }

        super.aiStep();
    }

    protected boolean teleportRandomly() {
        if (this.level().isClientSide() || !this.isAlive()) {
            return false;
        }
        double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double d1 = this.getY() + (this.random.nextInt(64) - 32);
        double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        return this.teleport(d0, d1, d2);
    }

    protected boolean teleportTowards(Entity entity) {
        Vec3 vec3 = new Vec3(
                this.getX() - entity.getX(),
                this.getBoundingBox().minY + this.getBbHeight() / 2.0F - entity.getY() + entity.getEyeHeight(),
                this.getZ() - entity.getZ());
        vec3 = vec3.normalize();
        double d0 = 16.0;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - vec3.x * d0;
        double d2 = this.getY() + (this.random.nextInt(16) - 8) - vec3.y * d0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - vec3.z * d0;
        return this.teleport(d1, d2, d3);
    }

    protected boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, y, z);
        while (cursor.getY() > this.level().getMinBuildHeight()
                && !this.level().getBlockState(cursor.below()).blocksMotion()) {
            cursor.move(Direction.DOWN);
        }
        BlockState below = this.level().getBlockState(cursor.below());
        boolean solid = below.blocksMotion();
        boolean water = below.getFluidState().is(FluidTags.WATER);
        if (!solid || water) {
            return false;
        }

        Vec3 old = this.position();
        boolean ok = this.randomTeleport(x, cursor.getY(), z, true);
        if (ok) {
            this.level().gameEvent(GameEvent.TELEPORT, old, GameEvent.Context.of(this));
            if (!this.isSilent()) {
                this.level().playSound(
                        null, old.x, old.y, old.z, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
        return ok;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.setScreaming(true);
        if (source.is(DamageTypeTags.IS_PROJECTILE)
                || (source.getDirectEntity() != null && source.getDirectEntity() != source.getEntity())) {
            for (int i = 0; i < 16; i++) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }
        }
        return super.hurt(source, amount);
    }

    /**
     * Gold {@code getCanSpawnHere}: spawner OR (valid light && night && y ≥ 30 && no other Reaper in 16×8×16).
     * Spawner entity-name match deferred.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        if (this.getY() < 30.0) {
            return false;
        }
        // gold: getClosestEntityWithinAABB(EnderReaper, expand 16,8,16) == null
        AABB box = this.getBoundingBox().inflate(16.0, 8.0, 16.0);
        return level.getEntitiesOfClass(EnderReaper.class, box, e -> e != this).isEmpty();
    }
}
