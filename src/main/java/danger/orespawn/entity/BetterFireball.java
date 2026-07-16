package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Gold {@code BetterFireball} (EntityFireball). Used by Mothra / Brutalfly (not Spyro —
 * Spyro uses vanilla SmallFireball). Registered as {@code better_fireball} for 1.21 spawn/network.
 * <p>
 * Gold behaviour:
 * <ul>
 *   <li>Custom onUpdate: fixed accel vector (dir×0.1), motion starts 0, inertia 0.95 / water 0.8</li>
 *   <li>Smoke trail + water bubbles; self-ignite every tick</li>
 *   <li>Damage 10 (or 5 if {@link #setSmall()}), ignite 5s</li>
 *   <li>Never hit/damage {@link Mothra} or other {@link BetterFireball}</li>
 *   <li>{@link #setNotMe()}: skip Player + Mothra collision; if player impact, discard no explode</li>
 *   <li>Block impact: place fire on adjacent empty block</li>
 *   <li>If not small: explode power {@link #explosionPower} (1 / {@link #setBig()} 2 /
 *       {@link #setReallyBig()} 4), flaming, mobGriefing</li>
 *   <li>Lifetime 600 ticks (gold ticksInAir / ticksAlive cap)</li>
 *   <li>Shooter dead → despawn</li>
 * </ul>
 */
public class BetterFireball extends Fireball {
    /** Gold field_92012_e / ExplosionPower. */
    public int explosionPower = 1;
    /** Gold notme — set by Mothra/Brutalfly shots so fireballs do not hit the shooter side. */
    private int notme = 0;
    /** Gold small — 0.3125 hitbox, half damage, no explosion. */
    private boolean small = false;
    /**
     * When true, impact should not explode/discard (gold early return on Mothra/BetterFireball),
     * or discard already handled (notme + Player).
     */
    private boolean cancelImpact = false;

    /** Gold ticksAlive — incremented while inGround (rare); 600 cap with ticksInAir. */
    private int ticksAlive = 0;
    /** Gold ticksInAir — air flight counter; 600 cap. */
    private int ticksInAir = 0;
    /** Gold inGround flag (copied from EntityFireball; rarely set by this class). */
    private boolean inGround = false;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;

    /**
     * Gold field_70232_b / field_70233_c / field_70230_d — fixed acceleration vector
     * (aim direction × 0.1). Classic EntityFireball motion: {@code motion += accel; motion *= inertia}.
     */
    private double accelX;
    private double accelY;
    private double accelZ;

    public BetterFireball(EntityType<? extends BetterFireball> type, Level level) {
        super(type, level);
    }

    /**
     * Gold ctor: size 1×1, place at owner, motion=0, accel = dir/|dir|×0.1.
     * Callers (Mothra/Brutalfly) then {@code setPos} to mouth offset and {@link #setNotMe()}.
     */
    public BetterFireball(Level level, LivingEntity owner, Vec3 movement) {
        super(ModEntities.BETTER_FIREBALL.get(), level);
        this.setOwner(owner);
        this.moveTo(owner.getX(), owner.getY(), owner.getZ(), owner.getYRot(), owner.getXRot());
        this.reapplyPosition();
        // gold: field_70159_w/x/y = 0
        this.setDeltaMovement(Vec3.ZERO);
        this.assignGoldAcceleration(movement);
        this.hasImpulse = true;
    }

    /** Gold: accel components = par / length * 0.1. */
    private void assignGoldAcceleration(Vec3 movement) {
        double len = Math.sqrt(movement.x * movement.x + movement.y * movement.y + movement.z * movement.z);
        if (len < 1.0E-8) {
            this.accelX = 0.0;
            this.accelY = 0.0;
            this.accelZ = 0.0;
        } else {
            this.accelX = movement.x / len * 0.1;
            this.accelY = movement.y / len * 0.1;
            this.accelZ = movement.z / len * 0.1;
        }
        // keep AbstractHurtingProjectile field coherent for NBT/deflection
        this.accelerationPower = 0.1;
    }

    public void setNotMe() {
        this.notme = 1;
    }

    public void setBig() {
        this.explosionPower = 2;
    }

    public void setReallyBig() {
        this.explosionPower = 4;
    }

    public void setSmall() {
        this.small = true;
        this.refreshDimensions();
    }

    public boolean isSmall() {
        return this.small;
    }

    public int getNotMe() {
        return this.notme;
    }

    /** Gold setSize(0.3125, 0.3125) when small; else EntityType 1.0×1.0. */
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (this.small) {
            return EntityDimensions.scalable(0.3125F, 0.3125F);
        }
        return super.getDimensions(pose);
    }

    /** Gold func_82341_c / EntityFireball motion factor. */
    @Override
    protected float getInertia() {
        return 0.95F;
    }

    /** Gold water branch sets motion factor to 0.8F. */
    @Override
    protected float getLiquidInertia() {
        return 0.8F;
    }

    /** Gold always setFire(1) each tick. */
    @Override
    protected boolean shouldBurn() {
        return true;
    }

    /**
     * Gold {@code func_70071_h_} (full custom EntityFireball tick with OreSpawn filters).
     * Does not call {@code AbstractHurtingProjectile.tick()} (would double-apply modern scalar accel).
     * Entity bookkeeping via {@link #baseTick()}; motion uses fixed gold accel vector.
     */
    @Override
    public void tick() {
        // gold: if (!(ticksAlive < 600 && ticksInAir < 600)) kill
        if (this.ticksAlive >= 600 || this.ticksInAir >= 600) {
            this.discard();
            return;
        }

        Entity shooter = this.getOwner();
        // gold: !remote && shootingEntity != null && shootingEntity.isDead → kill
        if (!this.level().isClientSide && shooter != null && !shooter.isAlive()) {
            this.discard();
            return;
        }
        // modern safety: unloaded chunk / removed owner
        if (!this.level().isClientSide
                && (shooter != null && shooter.isRemoved() || !this.level().hasChunkAt(this.blockPosition()))) {
            this.discard();
            return;
        }

        // gold super.onUpdate() ≈ entity base; then setFire(1)
        this.baseTick();
        if (this.shouldBurn()) {
            this.igniteForSeconds(1.0F);
        }

        if (this.inGround) {
            // gold: if block at x/y/zTile still solid, ticksAlive++
            BlockPos stuck = new BlockPos(this.xTile, this.yTile, this.zTile);
            if (!this.level().getBlockState(stuck).isAir()) {
                this.ticksAlive++;
            }
            this.inGround = false;
            Vec3 damp = this.getDeltaMovement();
            this.setDeltaMovement(
                    damp.x * (double) (this.random.nextFloat() * 0.2F),
                    damp.y * (double) (this.random.nextFloat() * 0.2F),
                    damp.z * (double) (this.random.nextFloat() * 0.2F));
        } else {
            this.ticksInAir++;
        }

        Vec3 from = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 to = from.add(motion);

        // gold: world.rayTraceBlocks(from, to, false)
        HitResult blockHit = this.level().clip(
                new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            to = blockHit.getLocation();
        }

        Entity hitEntity = null;
        double closest = 0.0;
        boolean cancelHit = false;
        float edge = 0.3F;

        // gold: entities in expand(motion).expand(1,1,1)
        AABB search = this.getBoundingBox().expandTowards(motion).inflate(1.0);
        List<Entity> list = this.level().getEntities(this, search);

        for (Entity candidate : list) {
            // gold: if shootingEntity == candidate → clear mop, break
            if (shooter != null && candidate == shooter) {
                cancelHit = true;
                break;
            }
            // gold: BetterFireball → clear mop, break
            if (candidate instanceof BetterFireball) {
                cancelHit = true;
                break;
            }
            // gold: notme && (Player || Mothra) → clear mop, break
            if (this.notme != 0 && (candidate instanceof Player || candidate instanceof Mothra)) {
                cancelHit = true;
                break;
            }
            // gold: canBeCollidedWith && (!isEntityEqual(shooter) || ticksInAir >= 25)
            if (candidate.canBeHitByProjectile()
                    && (shooter == null || !candidate.is(shooter) || this.ticksInAir >= 25)) {
                AABB padded = candidate.getBoundingBox().inflate(edge, edge, edge);
                var clip = padded.clip(from, to);
                if (clip.isPresent()) {
                    double dist = from.distanceTo(clip.get());
                    if (dist < closest || closest == 0.0) {
                        hitEntity = candidate;
                        closest = dist;
                    }
                }
            }
        }

        HitResult impact = null;
        if (cancelHit) {
            impact = null;
        } else if (hitEntity != null) {
            impact = new EntityHitResult(hitEntity);
        } else if (blockHit.getType() != HitResult.Type.MISS) {
            impact = blockHit;
        }

        if (impact != null && impact.getType() != HitResult.Type.MISS) {
            if (!EventHooks.onProjectileImpact(this, impact)) {
                this.onHit(impact);
            }
        }

        // gold: pos += motion
        motion = this.getDeltaMovement();
        double nx = this.getX() + motion.x;
        double ny = this.getY() + motion.y;
        double nz = this.getZ() + motion.z;

        // gold: yaw/pitch from motion + 0.2 lerp (ProjectileUtil 0.2F)
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);

        // gold: getMotionFactor(); water → bubbles + 0.8F
        float inertia = this.getInertia();
        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                float s = 0.25F;
                this.level().addParticle(
                        ParticleTypes.BUBBLE,
                        nx - motion.x * (double) s,
                        ny - motion.y * (double) s,
                        nz - motion.z * (double) s,
                        motion.x,
                        motion.y,
                        motion.z);
            }
            inertia = this.getLiquidInertia();
        }

        // gold: motion += accel; motion *= inertia
        // (classic fixed-vector accel — not modern normalize×scalar)
        double mx = (motion.x + this.accelX) * (double) inertia;
        double my = (motion.y + this.accelY) * (double) inertia;
        double mz = (motion.z + this.accelZ) * (double) inertia;
        this.setDeltaMovement(mx, my, mz);

        // gold: SMOKE at (x, y+0.5, z)
        this.level().addParticle(ParticleTypes.SMOKE, nx, ny + 0.5, nz, 0.0, 0.0, 0.0);

        this.setPos(nx, ny, nz);
    }

    /**
     * Gold tick entity scan mirrors {@link #canHitEntity} for any vanilla path that still uses it
     * (deflection / utilities). Owner always skipped; notme skips Player/Mothra; always skip Mothra
     * for solid hits (onImpact also returns).
     */
    @Override
    protected boolean canHitEntity(Entity target) {
        if (target == this.getOwner()) {
            return false;
        }
        if (target instanceof BetterFireball) {
            return false;
        }
        if (this.notme != 0 && (target instanceof Player || target instanceof Mothra)) {
            return false;
        }
        if (target instanceof Mothra) {
            return false;
        }
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level().isClientSide) {
            return;
        }
        Entity hit = result.getEntity();

        // gold onImpact: BetterFireball → return (no discard, no explode)
        if (hit instanceof BetterFireball) {
            this.cancelImpact = true;
            return;
        }
        // gold onImpact: Mothra → return (no discard, no explode)
        if (hit instanceof Mothra) {
            this.cancelImpact = true;
            return;
        }
        // gold onImpact: notme + Player → kill without explode/damage
        if (this.notme != 0 && hit instanceof Player) {
            this.cancelImpact = true;
            this.discard();
            return;
        }

        // gold empty width*height > 30 check omitted (no-op in decompile)
        Entity owner = this.getOwner();
        DamageSource src = this.damageSources().fireball(this, owner);
        float dmg = this.small ? 5.0F : 10.0F;
        hit.hurt(src, dmg);
        hit.igniteForSeconds(5.0F);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Do not call super — BlockState.onProjectileHit is extra modern behaviour.
        if (this.level().isClientSide) {
            return;
        }
        // gold: offset by hit side, place fire if air (no griefing gate)
        BlockPos pos = result.getBlockPos().relative(result.getDirection());
        if (this.level().isEmptyBlock(pos)) {
            this.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        }
        // remember tile for inGround bookkeeping (gold x/y/zTile)
        BlockPos hitPos = result.getBlockPos();
        this.xTile = hitPos.getX();
        this.yTile = hitPos.getY();
        this.zTile = hitPos.getZ();
    }

    /**
     * Gold onImpact end: if !small explode(null, pos, power, flaming=true, mobGriefing); always die
     * except early returns handled via {@link #cancelImpact}.
     */
    @Override
    protected void onHit(HitResult result) {
        this.cancelImpact = false;
        // Dispatch entity/block hooks without Projectile's redirectable-projectile extras when possible
        HitResult.Type type = result.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
        } else if (type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
        if (this.level().isClientSide) {
            return;
        }
        if (this.cancelImpact) {
            return;
        }
        if (!this.small) {
            // gold: flaming always true; block damage follows mobGriefing
            boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            if (this.getOwner() != null) {
                grief = EventHooks.canEntityGrief(this.level(), this.getOwner());
            }
            this.level().explode(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (float) this.explosionPower,
                    true,
                    grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        }
        this.discard();
    }

    @Override
    public void recreateFromPacket(net.minecraft.network.protocol.game.ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        // Client entities only get motion from the spawn packet — seed gold accel from it
        // so smoke/coast prediction matches server ramp.
        Vec3 m = this.getDeltaMovement();
        if (m.lengthSqr() > 1.0E-8) {
            this.assignGoldAcceleration(m);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ExplosionPower", this.explosionPower);
        tag.putBoolean("Small", this.small);
        tag.putInt("NotMe", this.notme);
        tag.putInt("TicksInAir", this.ticksInAir);
        tag.putInt("TicksAlive", this.ticksAlive);
        // EntityFireball "direction" equivalent
        tag.putDouble("accelX", this.accelX);
        tag.putDouble("accelY", this.accelY);
        tag.putDouble("accelZ", this.accelZ);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ExplosionPower")) {
            this.explosionPower = tag.getInt("ExplosionPower");
        }
        this.small = tag.getBoolean("Small");
        this.notme = tag.getInt("NotMe");
        this.ticksInAir = tag.getInt("TicksInAir");
        this.ticksAlive = tag.getInt("TicksAlive");
        if (tag.contains("accelX")) {
            this.accelX = tag.getDouble("accelX");
            this.accelY = tag.getDouble("accelY");
            this.accelZ = tag.getDouble("accelZ");
        }
        if (this.small) {
            this.refreshDimensions();
        }
    }
}
