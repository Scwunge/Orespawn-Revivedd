package danger.orespawn.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code WormDoom} (EntityLiving / Mob). Health 3000, speed 0.1.
 * Client tracking range 325 (registry). Long trail body; undulates through terrain.
 * Gold {@code func_184227_b(12.0)} treated as render-distance weight (not size).
 */
public class WormDoom extends Mob {
    public double[] lposx = new double[100];
    public double[] lposy = new double[100];
    public double[] lposz = new double[100];
    public double[] rotpitch = new double[100];
    public double[] rotyaw = new double[100];
    public long lasttime = 0L;
    public int backoff = 0;
    public int inarow = 0;
    public float cycle;
    public float target_direction = 0.0F;
    public float local_rotationYaw = 0.0F;
    public float local_rotation_yaw_motion = 0.0F;
    public float updown = 0.0F;
    public float local_motionX;
    public float local_motionY;
    public float local_motionZ;
    public float local_posX;
    public float local_posY;
    public float local_posZ;

    public WormDoom(EntityType<? extends WormDoom> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.setYRot(1.0F);
        this.local_rotationYaw = 1.0F;
        this.noPhysics = true;
        double mx = Math.sin(Math.toRadians(this.local_rotationYaw)) * 0.75F;
        double mz = Math.cos(Math.toRadians(this.local_rotationYaw)) * 0.75F;
        mx /= 2.0;
        mz /= 2.0;
        for (int i = 0; i < 100; i++) {
            this.lposx[i] = this.getX() - mx * i * 1.5;
            this.lposy[i] = this.getY() + Math.sin(Math.toRadians(i * 10 + 180)) * 4.0F;
            this.lposz[i] = this.getZ() - mz * i * 1.5;
            this.rotpitch[i] = 0.0;
            this.rotyaw[i] = this.local_rotationYaw;
        }
        this.cycle = level.random.nextFloat() * 360.0F;
        this.target_direction = this.local_rotationYaw;
        this.local_motionX = (float) mx;
        this.local_motionY = 0.0F;
        this.local_motionZ = (float) mz;
        this.local_posX = (float) this.getX();
        this.local_posY = (float) this.getY();
        this.local_posZ = (float) this.getZ();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3000.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true; // gold always true
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 99; i > 0; i--) {
                this.lposx[i] = this.lposx[i - 1];
                this.lposy[i] = this.lposy[i - 1];
                this.lposz[i] = this.lposz[i - 1];
                this.rotpitch[i] = this.rotpitch[i - 1];
                this.rotyaw[i] = this.rotyaw[i - 1];
            }
            this.lposx[0] = this.getX();
            this.lposy[0] = this.getY();
            this.lposz[0] = this.getZ();
            this.rotpitch[0] = this.getXRot();
            this.rotyaw[0] = this.getYRot();
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            float myspeed = 0.3F;
            if (this.isBaby()) {
                myspeed = 0.2F;
            }
            this.local_motionX = (float) Math.sin(Math.toRadians(this.local_rotationYaw)) * myspeed;
            this.local_motionZ = (float) Math.cos(Math.toRadians(this.local_rotationYaw)) * myspeed;
            int ht = 6;
            float frq = 10.0F;
            if (this.isBaby()) {
                ht = 3;
                frq = 20.0F;
            }
            int k;
            for (k = ht; k >= -ht; k--) {
                BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + k, this.getZ()));
                if (!bid.isAir()) {
                    break;
                }
            }
            this.updown = (float) Math.sin(Math.toRadians(this.cycle * frq)) * ht;
            this.updown -= k;
            this.updown = -this.updown;
            this.local_motionY *= 0.98F;
            this.local_motionY = this.local_motionY + 0.008F * this.updown;
            this.cycle = (this.cycle + 0.5F) % 360.0F;
            float dx = (float) Math.sqrt(this.local_motionX * this.local_motionX + this.local_motionZ * this.local_motionZ);
            float dz = (float) Math.atan2(-this.local_motionY, dx);
            this.setXRot((float) Math.toDegrees(dz));
            float cdir = (float) Math.toRadians(this.local_rotationYaw);
            if (this.random.nextInt(100) == 1) {
                this.target_direction = 360.0F * this.random.nextFloat();
            }
            float tdir = (float) Math.toRadians(this.target_direction);
            float ddiff = tdir - cdir;
            while (ddiff > Math.PI) {
                ddiff = (float) (ddiff - (Math.PI * 2));
            }
            while (ddiff < -Math.PI) {
                ddiff = (float) (ddiff + (Math.PI * 2));
            }
            this.local_rotation_yaw_motion *= 0.95F;
            this.local_rotation_yaw_motion = (float) (this.local_rotation_yaw_motion + ddiff * 180.0F / Math.PI / 20.0);
            this.local_rotationYaw = this.local_rotationYaw + this.local_rotation_yaw_motion;
            this.setYRot(-this.local_rotationYaw);
            // gold: motionX = local_motionX, motionY = local_motionY, motionZ = -local_motionZ
            this.setDeltaMovement(this.local_motionX, this.local_motionY, -this.local_motionZ);
            // gold had System.out.println(motionY) debug — omitted
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold caterkiller living files not in this jar — alosaurus living stand-in
        return this.random.nextInt(4) == 0
                ? danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_LIVING.get()
                : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_ALOSAURUS_DEATH.get();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
