package danger.orespawn.entity;

import java.util.List;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * Gold {@code QueenHead} (EntityLiving) — invisible hitbox companion for {@link TheQueen}.
 * Size 19.9×10.0, health matches TheQueen (6000), speed 1.33, attack 0.
 * Follows Queen head offset; relays damage to parent TheQueen.
 * Registry size/attrs set in {@code ModEntities}.
 * Renderer intentionally empty (gold RenderQueenHead draws nothing).
 */
public class QueenHead extends Mob {
    public static final float GOLD_WIDTH = 19.9F;
    public static final float GOLD_HEIGHT = 10.0F;
    public static final double GOLD_HEALTH = TheQueen.GOLD_HEALTH;
    public static final double GOLD_SPEED = 1.33;
    public static final double GOLD_ATTACK = 0.0;
    public static final double GOLD_FOLLOW = 64.0;

    public QueenHead(EntityType<? extends QueenHead> type, Level level) {
        super(type, level);
        this.noPhysics = true; // gold field_70145_X
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GOLD_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, GOLD_SPEED)
                .add(Attributes.ATTACK_DAMAGE, GOLD_ATTACK)
                .add(Attributes.FOLLOW_RANGE, GOLD_FOLLOW)
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
    protected void checkFallDamage(
            double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos) {
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
        if (e instanceof TheQueen || e instanceof QueenHead) {
            return false;
        }
        Entity direct = par1DamageSource.getDirectEntity();
        if (direct instanceof TheQueen || direct instanceof QueenHead) {
            return false;
        }

        // relay damage to nearest TheQueen
        List<TheQueen> queens =
                this.level().getEntitiesOfClass(TheQueen.class, this.getBoundingBox().inflate(48.0, 32.0, 48.0));
        if (!queens.isEmpty()) {
            return queens.get(0).hurt(par1DamageSource, par2);
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.isRemoved()) {
            return;
        }
        this.clearFire();

        if (!this.level().isClientSide) {
            // server: attach to nearest TheQueen head position (gold 1:1 offsets)
            List<TheQueen> queens =
                    this.level().getEntitiesOfClass(TheQueen.class, this.getBoundingBox().inflate(32.0, 32.0, 32.0));
            if (!queens.isEmpty()) {
                TheQueen var4 = queens.get(0);
                // gold: y+12; x = qx - 30*sin(yBodyRot°); z = qz + 30*cos(yBodyRot°)
                double y = var4.getY() + 12.0;
                double x = var4.getX() - 30.0 * Math.sin(Math.toRadians(var4.yBodyRot));
                double z = var4.getZ() + 30.0 * Math.cos(Math.toRadians(var4.yBodyRot));
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
