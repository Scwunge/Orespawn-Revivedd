package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModEntities;
import danger.orespawn.items.tools.OrespawnToolMaterial;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7.10 {@code BerthaHit} — short-range slash projectile from Big Bertha swing.
 * hit_type 0 = Bertha (default damage + fire + knockback).
 */
public class BerthaHit extends ThrowableProjectile {
    private int hitType = 0;

    public BerthaHit(EntityType<? extends BerthaHit> type, Level level) {
        super(type, level);
    }

    public BerthaHit(Level level, LivingEntity thrower) {
        super(ModEntities.BERTHA_HIT.get(), thrower, level);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
    }

    public void setHitType(int hitType) {
        this.hitType = hitType;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity e = result.getEntity();
        Entity owner = this.getOwner();
        if (owner == null || e == owner || this.level().isClientSide) {
            this.discard();
            return;
        }

        // 1.7.10: no PVP / no tameables when big_bertha_pvp == 0
        if (OreSpawnMain.big_bertha_pvp == 0) {
            if (e instanceof Player) {
                this.discard();
                return;
            }
            if (e instanceof OwnableEntity ownable && ownable.getOwner() != null) {
                this.discard();
                return;
            }
        }

        // hit_type 0 Bertha, 1 unused, 2 Royal, 3 Hammy (1.7.10 BerthaHit)
        float dmg;
        double rangeSq;
        double ks;
        double inair;
        boolean fire;
        boolean explode = false;
        float explodePower = 0.0F;

        if (this.hitType == 2) {
            // Royal: dist² < 101, dmg 746, ks 1.5, inair 0.25, no fire
            dmg = danger.orespawn.items.tools.Royal.ROYAL_DAMAGE;
            rangeSq = 101.0;
            ks = 1.5;
            inair = e.isAlive() ? 0.25 : 0.5;
            fire = false;
        } else if (this.hitType == 3) {
            // Hammy: dmg 82 + explosion
            dmg = danger.orespawn.items.tools.Hammy.HAMMY_DAMAGE;
            rangeSq = 81.0;
            ks = 2.0;
            inair = e.isAlive() ? 0.3 : 0.6;
            fire = false;
            explode = true;
            explodePower = e instanceof Player ? 1.5F : 2.1F;
        } else {
            // Bertha / Slice default
            dmg = OrespawnToolMaterial.BerthaTools.damage;
            rangeSq = 81.0;
            ks = 2.25;
            inair = e.isAlive() ? 0.35 : 0.7;
            fire = true;
        }

        if (owner.distanceToSqr(e) < rangeSq) {
            e.hurt(this.damageSources().playerAttack((Player) owner), dmg);
            if (fire) {
                e.igniteForSeconds(10);
            }
            double dx = e.getX() - owner.getX();
            double dz = e.getZ() - owner.getZ();
            float ang = (float) Math.atan2(dz, dx);
            e.push(Math.cos(ang) * ks, inair, Math.sin(ang) * ks);
            if (explode && !this.level().isClientSide) {
                this.level().explode(
                        owner,
                        e.getX(),
                        e.getY(),
                        e.getZ(),
                        explodePower,
                        Level.ExplosionInteraction.NONE);
            }
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        // Short life — gold was a swing arc, not a long bolt
        if (this.tickCount > 12) {
            this.discard();
        }
    }

    /** Launch with gold 2× velocity after aim. */
    public void shootFromPlayer(Player player) {
        double xzoff = 2.0;
        double yoff = 1.55;
        float yaw = player.getYRot();
        double x = player.getX() - xzoff * Math.sin(Math.toRadians(yaw));
        double y = player.getY() + yoff;
        double z = player.getZ() + xzoff * Math.cos(Math.toRadians(yaw));
        this.setPos(x, y, z);
        Vec3 look = player.getLookAngle().scale(2.0);
        this.setDeltaMovement(look);
        this.hasImpulse = true;
    }
}
