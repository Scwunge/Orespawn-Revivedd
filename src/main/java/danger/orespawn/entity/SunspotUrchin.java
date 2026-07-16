package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Gold {@code SunspotUrchin} (EntityThrowable). Damage 3 (6 vs Creeper), ignites non-fire-immune
 * targets for 5s; never damages players. Block hit places fire on adjacent air face. Always on fire;
 * smoke + redstone dust trail. Sprite index 50.
 * <p>
 * Registry: {@code sunspot_urchin}.
 */
public class SunspotUrchin extends ThrowableProjectile {
    private float myRotation = 0.0F;
    private int myIndex = 50;

    public SunspotUrchin(EntityType<? extends SunspotUrchin> type, Level level) {
        super(type, level);
    }

    public SunspotUrchin(Level level, LivingEntity thrower) {
        super(ModEntities.SUNSPOT_URCHIN.get(), thrower, level);
    }

    public SunspotUrchin(Level level, double x, double y, double z) {
        super(ModEntities.SUNSPOT_URCHIN.get(), x, y, z, level);
    }

    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    public int getUrchinIndex() {
        return this.myIndex;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.level().isClientSide) {
            return;
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            applyEntityHit(((EntityHitResult) result).getEntity());
        } else if (result.getType() == HitResult.Type.BLOCK) {
            placeFire((BlockHitResult) result);
        }

        for (int i = 0; i < 5; i++) {
            this.level().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    this.random.nextFloat(),
                    this.random.nextFloat(),
                    this.random.nextFloat());
            this.level().addParticle(
                    DustParticleOptions.REDSTONE,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    this.random.nextFloat(),
                    this.random.nextFloat(),
                    this.random.nextFloat());
        }

        this.discard();
    }

    private void applyEntityHit(Entity hit) {
        float dmg = 3.0F;
        if (hit instanceof Creeper) {
            dmg = 6.0F;
        }

        // gold: no damage to players
        if (hit instanceof Player) {
            return;
        }

        hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
        if (!hit.fireImmune()) {
            hit.igniteForSeconds(5);
        }
    }

    private void placeFire(BlockHitResult result) {
        Direction face = result.getDirection();
        BlockPos place = result.getBlockPos().relative(face);
        if (this.level().getBlockState(place).isAir()
                || this.level().getBlockState(place).canBeReplaced()) {
            this.level().setBlockAndUpdate(place, Blocks.FIRE.defaultBlockState());
        }
    }

    @Override
    public void tick() {
        super.tick();
        // gold: setFire(1) every tick
        this.igniteForSeconds(1);
        this.myRotation += 30.0F;
        while (this.myRotation > 360.0F) {
            this.myRotation -= 360.0F;
        }
        this.setXRot(this.myRotation);
        this.xRotO = this.myRotation;
        this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
    }
}
