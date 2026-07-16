package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Gold {@code InkSack} (EntityThrowable). Damage 1 (4 vs Creeper), 50% chance Blindness
 * (100 + 50×rand(8) ticks). Immune: WaterDragon, AttackSquid. Splash + smoke on impact.
 * Spin pitch += 30°/tick. Sprite index 65.
 * <p>
 * Registry: {@code ink_sack}.
 */
public class InkSack extends ThrowableProjectile {
    private float myRotation = 0.0F;
    private int myIndex = 65;

    public InkSack(EntityType<? extends InkSack> type, Level level) {
        super(type, level);
    }

    public InkSack(Level level, LivingEntity thrower) {
        super(ModEntities.INK_SACK.get(), thrower, level);
    }

    public InkSack(Level level, double x, double y, double z) {
        super(ModEntities.INK_SACK.get(), x, y, z, level);
    }

    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    public int getInkSackIndex() {
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
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) result).getEntity();
            if (!applyEntityHit(hit)) {
                // gold: early return without discard for immune targets
                return;
            }
        }

        for (int i = 0; i < 4; i++) {
            this.level().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getY() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getZ() + this.random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
        }

        this.playSound(
                SoundEvents.GENERIC_SPLASH,
                0.5F,
                1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F);

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    /**
     * @return false if gold returned early (no damage / no kill)
     */
    private boolean applyEntityHit(Entity hit) {
        float dmg = 1.0F;
        if (hit instanceof Creeper) {
            dmg = 4.0F;
        }

        if (hit instanceof WaterDragon) {
            return false;
        }
        if (hit instanceof AttackSquid) {
            return false;
        }

        hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);

        if (hit instanceof LivingEntity living && this.random.nextInt(2) == 0) {
            int duration = 100 + 50 * this.random.nextInt(8);
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 0));
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.myRotation += 30.0F;
        while (this.myRotation > 360.0F) {
            this.myRotation -= 360.0F;
        }
        this.setXRot(this.myRotation);
        this.xRotO = this.myRotation;
    }
}
