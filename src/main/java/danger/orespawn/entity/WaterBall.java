package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Gold {@code WaterBall} (EntityThrowable). Damage 2 (5 vs Creeper), extinguishes fire,
 * splash FX; immune to WaterDragon / AttackSquid / non-zero Dragon type / mounted players.
 * <p>
 * Registry: {@code water_ball}.
 */
public class WaterBall extends ThrowableProjectile {
    private float myRotation = 0.0F;
    private int myIndex = 49;

    public WaterBall(EntityType<? extends WaterBall> type, Level level) {
        super(type, level);
    }

    public WaterBall(Level level, LivingEntity thrower) {
        super(ModEntities.WATER_BALL.get(), thrower, level);
    }

    public WaterBall(Level level, double x, double y, double z) {
        super(ModEntities.WATER_BALL.get(), x, y, z, level);
    }

    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    public int getWaterBallIndex() {
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
                // gold: return without kill for immune targets
                return;
            }
        }

        for (int i = 0; i < 8; i++) {
            this.level().addParticle(
                    ParticleTypes.BUBBLE,
                    this.getX() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getY() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getZ() + this.random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
            this.level().addParticle(
                    ParticleTypes.SPLASH,
                    this.getX() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getY() + this.random.nextFloat() - this.random.nextFloat(),
                    this.getZ() + this.random.nextFloat() - this.random.nextFloat(),
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
        float dmg = 2.0F;
        if (hit instanceof Creeper) {
            dmg = 5.0F;
        }

        if (hit instanceof WaterDragon) {
            return false;
        }
        if (hit instanceof AttackSquid) {
            return false;
        }
        if (hit instanceof Dragon dragon) {
            if (dragon.getDragonType() != 0) {
                return false;
            }
        }
        if (hit instanceof Player player) {
            if (player.getVehicle() != null) {
                return false;
            }
        }

        hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);

        if (this.random.nextInt(10) == 1) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", "waterball"));
            if (item != null && item != Items.AIR) {
                hit.spawnAtLocation(new ItemStack(item));
            }
        }

        hit.clearFire();
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
        this.level().addParticle(ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}
