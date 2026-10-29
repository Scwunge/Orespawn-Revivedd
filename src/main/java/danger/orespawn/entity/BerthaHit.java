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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7.10 {@code BerthaHit} — short-range, invisible slash projectile from Big Bertha swing.
 * hit_type 0 = Bertha / Slice (default damage + fire + knockback), 2 = Royal, 3 = Hammy.
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
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        // 1.7.10: Hammy swing that hits no entity still explodes (2.1) when near the owner
        Entity owner = this.getOwner();
        if (!this.level().isClientSide && this.hitType == 3 && owner != null && this.distanceToSqr(owner) < 64.0) {
            this.explode(2.1F);
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

        // 1.7.10: Girlfriend / Boyfriend are always spared; players and tameables only when big_bertha_pvp == 0
        if (e instanceof Girlfriend || e instanceof Boyfriend) {
            this.discard();
            return;
        }
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

        if (this.hitType == 2) {
            // Royal: dist² < 101, dmg 746, ks 1.5, inair 0.25, no fire
            dmg = danger.orespawn.items.tools.Royal.ROYAL_DAMAGE;
            rangeSq = 101.0;
            ks = 1.5;
            inair = 0.25;
            fire = false;
        } else if (this.hitType == 3) {
            // Hammy: dist² < 64, dmg 82, ks 1.25, inair 0.65, explosion 1.5 at the hit
            dmg = danger.orespawn.items.tools.Hammy.HAMMY_DAMAGE;
            rangeSq = 64.0;
            ks = 1.25;
            inair = 0.65;
            fire = false;
            explode = true;
        } else {
            // Bertha / Slice default
            dmg = OrespawnToolMaterial.BerthaTools.damage;
            rangeSq = 81.0;
            ks = 2.25;
            inair = 0.35;
            fire = true;
        }

        // 1.7.10 measures range from the hit projectile to its owner
        if (this.distanceToSqr(owner) < rangeSq) {
            e.hurt(this.damageSources().playerAttack((Player) owner), dmg);
            if (fire) {
                e.igniteForSeconds(10);
            }
            if (!e.isAlive()) {
                inair *= 2.0;
            }
            double dx = e.getX() - owner.getX();
            double dz = e.getZ() - owner.getZ();
            float ang = (float) Math.atan2(dz, dx);
            e.push(Math.cos(ang) * ks, inair, Math.sin(ang) * ks);
            if (explode) {
                this.explode(1.5F);
            }
        }
        this.discard();
    }

    /** 1.7.10 {@code newExplosion(null, x, y, z, power, true, mobGriefing)} at the projectile. */
    private void explode(float power) {
        this.level().explode(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                power,
                true,
                Level.ExplosionInteraction.MOB);
    }

    @Override
    public void tick() {
        super.tick();
        // Short life — gold was a swing arc, not a long bolt
        if (this.tickCount > 12) {
            this.discard();
        }
    }

    /** Launch with gold 2× velocity after aim (EntityThrowable speed 1.5 × 2). */
    public void shootFromPlayer(Player player) {
        double xzoff = 2.0;
        double yoff = 1.55;
        float yaw = player.getYHeadRot();
        double x = player.getX() - xzoff * Math.sin(Math.toRadians(yaw));
        double y = player.getY() + yoff;
        double z = player.getZ() + xzoff * Math.cos(Math.toRadians(yaw));
        this.setPos(x, y, z);
        Vec3 look = player.getLookAngle().scale(3.0);
        this.setDeltaMovement(look);
        this.hasImpulse = true;
    }
}
