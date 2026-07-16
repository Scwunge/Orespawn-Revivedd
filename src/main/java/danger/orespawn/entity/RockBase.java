package danger.orespawn.entity;

import danger.orespawn.init.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
/**
 * Gold {@code RockBase} (EntityLiving). Size 0.25×0.15, fire-immune ground rock pickup entity.
 * Types 1–12 with crystal FX; health = 1 + type/4. Follow range gold 100000 (capped for port).
 * <p>
 * Registry: {@code rock_base}.
 */
public class RockBase extends Mob {
    private static final EntityDataAccessor<Integer> DATA_ROCK_TYPE =
            SynchedEntityData.defineId(RockBase.class, EntityDataSerializers.INT);

    public int rockType = 0;
    private double dx;
    private double dz;

    public RockBase(EntityType<? extends RockBase> type, Level level) {
        super(type, level);
        this.dx = 0.0;
        this.dz = 0.0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0) // updated by placeRock / type roll
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 64.0); // gold field_70174_ab = 100000
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ROCK_TYPE, 0);
    }

    @Override
    protected void registerGoals() {
        // gold: no goals
    }

    public int getRockType() {
        return this.entityData.get(DATA_ROCK_TYPE);
    }

    public void setRockType(int type) {
        this.rockType = type;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ROCK_TYPE, type);
        }
    }

    /** Gold {@code placeRock} — set type + health. */
    public void placeRock(int type) {
        this.rockType = type;
        this.setRockType(type);
        double hp = 1 + this.rockType / 4.0;
        if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(hp);
        }
        this.setHealth((float) hp);
    }

    @Override
    public boolean fireImmune() {
        return true; // gold field_70178_ae
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn false
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isPushable() {
        return true; // gold canBePushed true
    }

    @Override
    public boolean isPickable() {
        return true; // gold canBeCollidedWith true
    }

    @Override
    protected float getSoundVolume() {
        return 0.65F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
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
    public boolean hurt(DamageSource source, float amount) {
        if (source == this.damageSources().inWall() || "inWall".equals(source.getMsgId())) {
            return false;
        }
        Entity e = source.getEntity();
        if (e instanceof LivingEntity) {
            this.level()
                    .playSound(
                            null,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            SoundEvents.ITEM_PICKUP,
                            SoundSource.NEUTRAL,
                            0.75F,
                            2.25F);
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (this.dx == 0.0 && this.dz == 0.0) {
            this.dx = this.getX();
            this.dz = this.getZ();
        }

        super.tick();

        // gold: pitch/yaw locked
        this.setXRot(0.0F);
        this.setYRot(0.0F);
        this.yBodyRot = 0.0F;
        this.yHeadRot = 0.0F;

        if (this.level().isClientSide) {
            this.rockType = this.getRockType();
        }

        // gold: assign random type when still 0
        if (!this.level().isClientSide && this.rockType == 0) {
            if (this.level().dimension() != ModDimensions.CRYSTAL) {
                this.rockType = 1;
                if (this.random.nextInt(10) == 0) {
                    this.rockType = 2;
                }
                if (this.random.nextInt(20) == 0) {
                    this.rockType = 3;
                }
                if (this.random.nextInt(30) == 0) {
                    this.rockType = 4;
                }
                if (this.random.nextInt(40) == 0) {
                    this.rockType = 5;
                }
                if (this.random.nextInt(50) == 0) {
                    this.rockType = 6;
                }
                if (this.random.nextInt(100) == 0) {
                    this.rockType = 7;
                }
                if (this.random.nextInt(200) == 0) {
                    this.rockType = 8;
                }
                if (this.random.nextInt(500) == 0) {
                    this.rockType = 9;
                }
                if (this.random.nextInt(500) == 0) {
                    this.rockType = 10;
                }
                if (this.random.nextInt(500) == 0) {
                    this.rockType = 11;
                }
                if (this.random.nextInt(1000) == 0) {
                    this.rockType = 12;
                }
            } else {
                // gold DimensionID5 crystal
                this.rockType = 9;
                if (this.random.nextInt(3) == 0) {
                    this.rockType = 10;
                }
                if (this.random.nextInt(5) == 0) {
                    this.rockType = 11;
                }
                if (this.random.nextInt(10) == 0) {
                    this.rockType = 12;
                }
            }
            double hp = 1 + this.rockType / 4.0;
            if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(hp);
            }
            this.setHealth((float) hp);
        }

        if (!this.level().isClientSide) {
            this.setRockType(this.rockType);
        }

        // gold client crystal FX
        if (this.level().isClientSide && this.random.nextInt(20) == 0) {
            int rt = this.rockType;
            if (rt == 9) {
                this.level()
                        .addParticle(
                                ParticleTypes.FLAME,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F,
                                this.random.nextFloat() / 10.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F);
            } else if (rt == 10) {
                this.level()
                        .addParticle(
                                ParticleTypes.HAPPY_VILLAGER,
                                this.getX(),
                                this.getY() + 0.25,
                                this.getZ(),
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F,
                                this.random.nextFloat() / 2.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F);
            } else if (rt == 11) {
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F,
                                this.random.nextFloat() / 10.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F);
            } else if (rt == 12) {
                this.level()
                        .addParticle(
                                ParticleTypes.FIREWORK,
                                this.getX(),
                                this.getY() + 0.25,
                                this.getZ(),
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F,
                                this.random.nextFloat() / 5.0F,
                                (this.random.nextFloat() - this.random.nextFloat()) / 60.0F);
            }
        }
    }

    /** Gold {@code getCanSpawnHere}: y >= 50. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return !(this.getY() < 50.0);
    }

    /** Gold empty hurt animation ({@code performHurtAnimation}). */
    @Override
    public void animateHurt(float yaw) {
        this.hurtDuration = 0;
        this.hurtTime = 0;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        dropRockItem(this.rockType != 0 ? this.rockType : this.getRockType());
    }

    private void dropRockItem(int type) {
        String name = switch (type) {
            case 1 -> "rocksmall";
            case 2 -> "rock";
            case 3 -> "rockred";
            case 4 -> "rockgreen";
            case 5 -> "rockblue";
            case 6 -> "rockpurple";
            case 7 -> "rockspikey";
            case 8 -> "rocktnt";
            case 9 -> "rockcrystalred";
            case 10 -> "rockcrystalgreen";
            case 11 -> "rockcrystalblue";
            case 12 -> "rockcrystaltnt";
            default -> null;
        };
        if (name == null) {
            return;
        }
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", name));
        if (item != null && item != Items.AIR) {
            this.spawnAtLocation(new ItemStack(item));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        // gold NBT key "ButterflyType" (historical)
        tag.putInt("ButterflyType", this.rockType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rockType = tag.getInt("ButterflyType");
        this.setRockType(this.rockType);
    }
}
