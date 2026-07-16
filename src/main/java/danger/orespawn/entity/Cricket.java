package danger.orespawn.entity;

import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Cricket} (EntityAnimal). Size 0.1×0.1, speed 0.15, health 3, attack 0, XP 1.
 * Panic + wander, occasional hop, singing flag for leg animation + ambient chirp.
 */
public class Cricket extends Animal {
    private static final EntityDataAccessor<Byte> DATA_SINGING =
            SynchedEntityData.defineId(Cricket.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.15F;
    private int singing;
    private int jumpcount;

    public Cricket(EntityType<? extends Cricket> type, Level level) {
        super(type, level);
        this.xpReward = 1;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SINGING, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAIPanic(1.4), MyEntityAIWanderALot(8, 1.0)
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 8, 1.0));
    }

    public int getSinging() {
        return this.entityData.get(DATA_SINGING);
    }

    public void setSinging(int value) {
        this.entityData.set(DATA_SINGING, (byte) value);
    }

    private void jumpAround() {
        // gold: add random upward + horizontal impulse
        Vec3 v = this.getDeltaMovement();
        float f = 0.3F + Math.abs(this.random.nextFloat() * 0.25F);
        float d = (float) (this.random.nextFloat() * Math.PI * 2.0);
        this.setDeltaMovement(
                v.x + f * Math.sin(d),
                v.y + (0.55F + Math.abs(this.random.nextFloat() * 0.35F)),
                v.z + f * Math.cos(d));
        this.setPos(this.getX(), this.getY() + 0.25, this.getZ());
        this.hasImpulse = true;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (!this.level().isClientSide) {
            if (this.singing != 0) {
                this.singing--;
                if (this.singing <= 0) {
                    this.setSinging(0);
                }
            }
            if (this.jumpcount > 0) {
                this.jumpcount--;
            }
            if (this.jumpcount == 0 && this.random.nextInt(50) == 1) {
                this.jumpAround();
                this.jumpcount = 50;
            }
        }
    }

    public int mygetMaxHealth() {
        return 3;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 50% silence; else set singing=40 and play cricket
        if (!this.level().isClientSide) {
            if (this.random.nextInt(2) == 0) {
                return null;
            }
            this.singing = 40;
            this.setSinging(this.singing);
        }
        return SoundsHandler.ENTITY_CRICKET_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.7F;
    }

    /** Gold: no fall damage / no step sound. */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos) {
        // no fall particles/damage
    }

    @Override
    protected void playStepSound(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        // silent
    }

    /** Gold dropFewItems empty. */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // no drops
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 30 and buddies ≤ 5. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 30.0) {
            return false;
        }
        return this.findBuddies() <= 5;
    }

    private int findBuddies() {
        List<Cricket> list =
                this.level().getEntitiesOfClass(Cricket.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0));
        return list.size();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: !isNoDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold createChild null — not breedable
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        //  gold createChild returns null
        return null;
    }
}
