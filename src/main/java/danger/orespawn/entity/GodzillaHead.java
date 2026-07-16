package danger.orespawn.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code GodzillaHead} / MobzillaHead (EntityLiving) — invisible hitbox companion for
 * {@link Godzilla}. Size 9.9×10.0, health matches Godzilla (4000), speed 1.33, attack 0.
 * Follows Godzilla head offset; relays damage to parent. Fire immune; no-clip.
 * Registry size/attrs set in {@code ModEntities}.
 * Renderer intentionally empty (gold RenderGodzillaHead draws nothing).
 */
public class GodzillaHead extends Mob {
    public static final float GOLD_WIDTH = 9.9F;
    public static final float GOLD_HEIGHT = 10.0F;
    public static final double GOLD_HEALTH = Godzilla.GOLD_HEALTH;
    public static final double GOLD_SPEED = 1.33;
    public static final double GOLD_ATTACK = 0.0;
    public static final double GOLD_FOLLOW = 10000.0;

    /** Cached body — avoid getEntitiesOfClass every tick (major TPS hit with Godzilla). */
    @Nullable
    private Godzilla cachedBody;
    private int bodyScanCooldown;

    public GodzillaHead(EntityType<? extends GodzillaHead> type, Level level) {
        super(type, level);
        this.noPhysics = true; // gold field_70145_X
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // gold empty
    }

    @Override
    public boolean isPushable() {
        return true; // gold canBePushed true
    }

    @Override
    public boolean isPickable() {
        return true; // gold canBeCollidedWith true
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2) {
        // gold: inWall immune
        if (par1DamageSource == this.damageSources().inWall()) {
            return false;
        }

        Entity e = par1DamageSource.getEntity();
        if (e instanceof Godzilla || e instanceof GodzillaHead) {
            return false;
        }
        Entity direct = par1DamageSource.getDirectEntity();
        if (direct instanceof Godzilla || direct instanceof GodzillaHead) {
            return false;
        }

        // relay damage to nearest Godzilla within 32
        Godzilla body = this.resolveBody(true);
        if (body != null) {
            return body.hurt(par1DamageSource, par2);
        }
        return false;
    }

    @Nullable
    private Godzilla resolveBody(boolean forceScan) {
        if (!forceScan
                && this.cachedBody != null
                && this.cachedBody.isAlive()
                && !this.cachedBody.isRemoved()) {
            return this.cachedBody;
        }
        List<Godzilla> bodies =
                this.level().getEntitiesOfClass(Godzilla.class, this.getBoundingBox().inflate(32.0, 32.0, 32.0));
        this.cachedBody = bodies.isEmpty() ? null : bodies.get(0);
        return this.cachedBody;
    }

    @Override
    public void tick() {
        if (this.isRemoved()) {
            return;
        }
        // gold field_70160_al = true each tick (onGround false)
        this.setOnGround(false);
        this.clearFire();

        if (!this.level().isClientSide) {
            // Scan every 10 ticks unless cache missing — avoids entity-world query every tick
            if (this.bodyScanCooldown > 0) {
                this.bodyScanCooldown--;
            }
            boolean needScan = this.bodyScanCooldown <= 0
                    || this.cachedBody == null
                    || !this.cachedBody.isAlive()
                    || this.cachedBody.isRemoved();
            Godzilla var4 = this.resolveBody(needScan);
            if (needScan) {
                this.bodyScanCooldown = 10;
            }
            if (var4 != null) {
                // gold: y = body.y + 16; x/z offset 17 along body yaw
                double y = var4.getY() + 16.0;
                double x = var4.getX() - 17.0 * Math.sin(Math.toRadians(var4.yBodyRot));
                double z = var4.getZ() + 17.0 * Math.cos(Math.toRadians(var4.yBodyRot));
                this.setPos(x, y, z);
                this.setYRot(var4.getYRot());
                this.yBodyRot = var4.yBodyRot;
                this.setDeltaMovement(var4.getDeltaMovement());
                this.setHealth(var4.getHealth());
            } else {
                this.discard();
                return;
            }
        }

        super.tick();
    }

    @Override
    protected void registerGoals() {
        // no AI — pure hitbox companion
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
