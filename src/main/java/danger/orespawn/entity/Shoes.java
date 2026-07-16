package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

/**
 * Gold {@code Shoes} (EntityThrowable). Throwable shoe projectile; ShoeId 2–6
 * (redheels/blackheels/slippers/boots/gamecontroller). Damage 2 (6 for id 6),
 * +4 vs Creeper, 1 vs Girlfriend/Boyfriend, 0 vs Player; valentines deferred (10).
 * <p>
 * Registry: {@code shoes}.
 */
public class Shoes extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> DATA_SHOE_ID =
            SynchedEntityData.defineId(Shoes.class, EntityDataSerializers.INT);

    public int shoeId = 0;
    private float myRotation = 0.0F;

    public Shoes(EntityType<? extends Shoes> type, Level level) {
        super(type, level);
    }

    public Shoes(Level level) {
        super(ModEntities.SHOES.get(), level);
        int id = this.random.nextInt(4) + 2;
        this.setShoeId(id);
    }

    public Shoes(Level level, int shoeId) {
        super(ModEntities.SHOES.get(), level);
        this.setShoeId(shoeId);
    }

    public Shoes(Level level, LivingEntity thrower) {
        super(ModEntities.SHOES.get(), thrower, level);
        int id = this.random.nextInt(4) + 2;
        this.setShoeId(id);
    }

    public Shoes(Level level, LivingEntity thrower, int shoeId) {
        super(ModEntities.SHOES.get(), thrower, level);
        this.setShoeId(shoeId);
    }

    public Shoes(Level level, double x, double y, double z) {
        super(ModEntities.SHOES.get(), x, y, z, level);
        int id = this.random.nextInt(4) + 2;
        this.setShoeId(id);
    }

    /** Gold throwable launch after ItemShoes use. */
    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SHOE_ID, 2);
    }

    public int getShoeId() {
        return this.entityData.get(DATA_SHOE_ID);
    }

    public void setShoeId(int id) {
        this.shoeId = id;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_SHOE_ID, id);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) result).getEntity();
            float dmg = 2.0F;
            if (this.getShoeId() == 6) {
                dmg = 6.0F;
            }
            if (hit instanceof Creeper) {
                dmg += 4.0F;
            }
            if (hit instanceof Girlfriend) {
                dmg = 1.0F;
            }
            if (hit instanceof Boyfriend) {
                dmg = 1.0F;
            }
            if (hit instanceof Player) {
                dmg = 0.0F;
            }
            // gold OreSpawnMain.valentines_day != 0 → dmg 10 — field deferred, default 0
            if (dmg > 0.0F) {
                hit.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
            }
        }

        for (int i = 0; i < 4; i++) {
            // gold snowballpoof + reddust
            this.level()
                    .addParticle(
                            ParticleTypes.ITEM_SNOWBALL,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            0.0,
                            0.0,
                            0.0);
            this.level()
                    .addParticle(
                            new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F),
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            0.0,
                            0.0,
                            0.0);
        }

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.myRotation += 20.0F;
        while (this.myRotation > 360.0F) {
            this.myRotation -= 360.0F;
        }
        this.setXRot(this.myRotation);
        this.xRotO = this.myRotation;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShoeId", this.getShoeId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ShoeId")) {
            this.setShoeId(tag.getInt("ShoeId"));
        }
    }
}
