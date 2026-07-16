package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import danger.orespawn.init.ModItems;
import danger.orespawn.items.CritterCage;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Gold {@code EntityCage} — empty captures, filled releases.
 * Hardened for mining dim: large dinos, grass/block hits, reliable type match.
 */
public class EntityCage extends ThrowableProjectile {
    @Nullable
    public EntityType<?> myType;
    @Nullable
    public String customName;

    public EntityCage(EntityType<? extends EntityCage> type, Level level) {
        super(type, level);
    }

    public EntityCage(Level level, LivingEntity thrower, @Nullable EntityType<?> myType, @Nullable String customName) {
        super(ModEntities.THROWN_CRITTER_CAGE.get(), thrower, level);
        this.myType = myType;
        this.customName = customName;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    /** Hit anything living that isn't the thrower (big dinos, pets, etc.). */
    @Override
    protected boolean canHitEntity(Entity target) {
        if (target == this.getOwner()) {
            return false;
        }
        if (!target.isAlive() || target.isSpectator()) {
            return false;
        }
        // Always allow living targets — default canBeHitByProjectile can miss some cases
        return target instanceof LivingEntity || super.canHitEntity(target);
    }

    @Override
    protected void onHit(HitResult result) {
        // Don't call super first — some projectile paths skip our logic after discard
        if (this.myType == null) {
            handleEmptyCageHit(result);
        } else {
            handleFilledCageHit(result);
        }
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    /**
     * Prefer direct entity hit; else scan a wide box (mining dim grass/stone hits).
     */
    @Nullable
    private LivingEntity resolveCaptureTarget(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity e = ((EntityHitResult) result).getEntity();
            if (e instanceof LivingEntity living && living.isAlive() && living != this.getOwner()) {
                return living;
            }
        }

        // Wide search: impact point + projectile position (covers large dinos / offsets)
        Vec3 at = result.getLocation();
        AABB box = new AABB(at, at).inflate(3.0, 3.0, 3.0)
                .minmax(this.getBoundingBox().inflate(3.0));
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e.isAlive()
                        && e != this.getOwner()
                        && !(e instanceof Player)
                        && CritterCage.canCapture(e));
        return list.stream().min(Comparator.comparingDouble(e -> e.distanceToSqr(at))).orElse(null);
    }

    private void handleEmptyCageHit(HitResult result) {
        LivingEntity living = this.resolveCaptureTarget(result);
        if (living == null) {
            // Last resort: scan around thrower look direction mid-flight end
            living = this.findNearestCagable(4.0);
        }
        if (living == null) {
            return;
        }

        double hx = living.getX();
        double hy = living.getY() + living.getBbHeight() * 0.5;
        double hz = living.getZ();

        for (int i = 0; i < 4; i++) {
            spawnParticle(ParticleTypes.SMOKE, hx, hy, hz);
            spawnParticle(ParticleTypes.EXPLOSION, hx, hy, hz);
            spawnParticle(DustParticleOptions.REDSTONE, hx, hy, hz);
        }

        this.level().playSound(
                null, hx, hy, hz, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.5F);

        CritterCage cc = CritterCage.getCageFromEntity(living);
        if (cc == null || this.level().isClientSide) {
            return;
        }

        Entity owner = this.getOwner();
        boolean creative = owner instanceof Player p && p.getAbilities().instabuild;
        // Gold 0.4 chance + 0.8 gate felt broken in mining; creative always, survival 0.4 only
        // (removed the separate nextInt(10) fail gate)
        if (creative || this.random.nextFloat() < Math.max(0.4F, cc.getChance())) {
            ItemStack cageStack = new ItemStack(cc);
            ItemEntity drop = new ItemEntity(this.level(), living.getX(), living.getY() + 0.5, living.getZ(), cageStack);
            drop.setDefaultPickUpDelay();
            this.level().addFreshEntity(drop);
            living.discard();
        }
    }

    @Nullable
    private LivingEntity findNearestCagable(double radius) {
        AABB box = this.getBoundingBox().inflate(radius);
        return this.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        box,
                        e -> e.isAlive()
                                && e != this.getOwner()
                                && !(e instanceof Player)
                                && CritterCage.canCapture(e))
                .stream()
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);
    }

    private void handleFilledCageHit(HitResult result) {
        BlockPos position;
        if (result.getType() == HitResult.Type.ENTITY) {
            position = ((EntityHitResult) result).getEntity().blockPosition();
        } else if (result.getType() == HitResult.Type.BLOCK) {
            position = ((BlockHitResult) result).getBlockPos().relative(((BlockHitResult) result).getDirection());
        } else {
            position = this.blockPosition();
        }

        double px = position.getX() + 0.5F;
        double py = position.getY() + 1.25F;
        double pz = position.getZ() + 0.5F;

        for (int i = 0; i < 6; i++) {
            spawnParticle(ParticleTypes.LARGE_SMOKE, px, py, pz);
            spawnParticle(ParticleTypes.EXPLOSION_EMITTER, px, py, pz);
            spawnParticle(DustParticleOptions.REDSTONE, px, py, pz);
        }

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.5F);

        if (!this.level().isClientSide && this.myType != null) {
            Entity summon = this.myType.create(this.level());
            if (summon != null) {
                summon.moveTo(position.getX() + 0.5, position.getY(), position.getZ() + 0.5, this.getYRot(), 0.0F);
                if (summon instanceof Horse horse) {
                    horse.setVariant(Variant.byId(this.random.nextInt(7)));
                }
                if (summon instanceof LivingEntity && this.customName != null) {
                    summon.setCustomName(Component.literal(this.customName));
                }
                this.level().addFreshEntity(summon);
            }

            ItemEntity empty = new ItemEntity(
                    this.level(),
                    position.getX(),
                    position.getY(),
                    position.getZ(),
                    new ItemStack(ModItems.EMPTY_CAGE.get()));
            this.level().addFreshEntity(empty);
        }
    }

    @Override
    public void tick() {
        super.tick();
        // Extra entity scan each tick — catches large mining-dim dinos the ray can miss
        if (!this.level().isClientSide && this.myType == null && !this.isRemoved()) {
            LivingEntity near = this.findNearestCagable(1.25);
            if (near != null && this.distanceToSqr(near) < 2.25) {
                // Force a synthetic hit
                this.onHit(new EntityHitResult(near));
            }
        }
        this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.myType != null) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(this.myType);
            if (id != null) {
                tag.putString("MyType", id.toString());
            }
        }
        if (this.customName != null) {
            tag.putString("CustomNameStr", this.customName);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("MyType")) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("MyType"));
            if (id != null && BuiltInRegistries.ENTITY_TYPE.containsKey(id)) {
                this.myType = BuiltInRegistries.ENTITY_TYPE.get(id);
            }
        }
        if (tag.contains("CustomNameStr")) {
            this.customName = tag.getString("CustomNameStr");
        }
    }

    private void spawnParticle(ParticleOptions particle, double x, double y, double z) {
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(particle, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        } else {
            this.level().addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
