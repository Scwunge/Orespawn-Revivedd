package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Gold {@code EntityThrownRock} (EntityThrowable). Rock types 1–12 with gold damage,
 * knockback, status effects, glass smash, and water bounce. Lifetime 1000 ticks.
 * <p>
 * Registry: {@code thrown_rock}.
 */
public class ThrownRock extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> DATA_ROCK_TYPE =
            SynchedEntityData.defineId(ThrownRock.class, EntityDataSerializers.INT);

    private int rockType = 0;
    private int myAge = 0;
    private float myRotation = 0.0F;

    public ThrownRock(EntityType<? extends ThrownRock> type, Level level) {
        super(type, level);
    }

    public ThrownRock(Level level, LivingEntity thrower, int rockType) {
        super(ModEntities.THROWN_ROCK.get(), thrower, level);
        this.setRockType(rockType);
    }

    public ThrownRock(Level level, double x, double y, double z) {
        super(ModEntities.THROWN_ROCK.get(), x, y, z, level);
    }

    /** Gold throwable launch (~1.5) after ItemRock use. */
    public void launchFrom(LivingEntity thrower) {
        this.shootFromRotation(thrower, thrower.getXRot(), thrower.getYRot(), 0.0F, 1.5F, 1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ROCK_TYPE, 0);
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

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    @Override
    protected void onHit(HitResult result) {
        if (this.isRemoved()) {
            return;
        }
        if (this.level().isClientSide) {
            return;
        }
        if (result.getType() == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
        } else if (result.getType() == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity e = result.getEntity();
        Entity owner = this.getOwner();
        if (owner == null || e == owner) {
            return;
        }
        int type = this.rockType != 0 ? this.rockType : this.getRockType();
        if (type == 0) {
            return;
        }

        float dmg = damageForType(type);
        double ks = knockbackForType(type);
        double inair = inAirForType(type);
        if (!e.isAlive()) {
            inair *= 2.0;
        }

        if (owner instanceof Player player) {
            e.hurt(this.damageSources().playerAttack(player), dmg);
        } else {
            e.hurt(this.damageSources().thrown(this, owner), dmg);
        }

        float ang = (float) Math.atan2(e.getZ() - owner.getZ(), e.getX() - owner.getX());
        e.push(Math.cos(ang) * ks, inair, Math.sin(ang) * ks);

        applyExtras(type, e);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        int type = this.rockType != 0 ? this.rockType : this.getRockType();
        if (type == 0) {
            return;
        }

        BlockPos hit = result.getBlockPos();
        int played = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos p = hit.offset(i, j, k);
                    BlockState state = this.level().getBlockState(p);
                    if (state.is(Blocks.GLASS) || state.is(Blocks.GLASS_PANE)
                            || state.is(Blocks.TINTED_GLASS)) {
                        this.level().removeBlock(p, false);
                        if (played == 0) {
                            this.level().playSound(
                                    null,
                                    hit,
                                    SoundEvents.GLASS_BREAK,
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    1.0F);
                            played++;
                        }
                    }
                }
            }
        }

        dropRockItem(type);
    }

    private static float damageForType(int type) {
        return switch (type) {
            case 1 -> 2.0F;
            case 2, 3, 4 -> 5.0F;
            case 5 -> 10.0F;
            case 6 -> 20.0F;
            case 7, 8 -> 40.0F;
            case 9, 10, 11 -> 150.0F;
            case 12 -> 250.0F;
            default -> 0.0F;
        };
    }

    private static double knockbackForType(int type) {
        return switch (type) {
            case 1, 5 -> 0.1;
            case 8 -> 0.5;
            default -> 0.2;
        };
    }

    private static double inAirForType(int type) {
        return type == 8 ? 0.055 : 0.025;
    }

    private void applyExtras(int type, Entity e) {
        switch (type) {
            case 3 -> e.igniteForSeconds(20);
            case 4 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
                }
            }
            case 5 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                }
            }
            case 6 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            }
            case 8 -> explodeAt(e, 2.1F);
            case 9 -> {
                e.igniteForSeconds(50);
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            }
            case 10 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            }
            case 11 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            }
            case 12 -> {
                if (e instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
                explodeAt(e, 5.1F);
            }
            default -> {
            }
        }
    }

    private void explodeAt(Entity e, float power) {
        boolean grief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (this.getOwner() != null) {
            grief = EventHooks.canEntityGrief(this.level(), this.getOwner());
        }
        this.level().explode(
                null,
                e.getX(),
                e.getY() + 0.25,
                e.getZ(),
                power,
                true,
                grief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
    }

    /** Gold drop by rock type — resolves item registry names if present. */
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
            this.spawnAtLocation(new ItemStack(item), 0.1F);
        }
    }

    @Override
    public void tick() {
        BlockPos before = this.blockPosition();
        super.tick();

        this.myRotation += 30.0F;
        this.myRotation %= 360.0F;
        this.setXRot(this.myRotation);
        this.xRotO = this.myRotation;

        this.myAge++;
        if (this.myAge > 1000) {
            this.discard();
            return;
        }

        if (this.level().isClientSide) {
            this.rockType = this.getRockType();
        } else {
            this.setRockType(this.rockType);
        }

        // gold water bounce when falling through water with lateral speed
        BlockState at = this.level().getBlockState(before);
        if (at.getFluidState().isSource() || at.is(Blocks.WATER) || !at.getFluidState().isEmpty()) {
            Vec3 m = this.getDeltaMovement();
            if (m.y < -0.15 && m.y > -0.55 && (m.x * m.x + m.z * m.z) > 0.5) {
                this.setDeltaMovement(m.x * 0.75, -(m.y * 0.75), m.z * 0.75);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("RockType", this.rockType);
        tag.putInt("MyAge", this.myAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rockType = tag.getInt("RockType");
        this.myAge = tag.getInt("MyAge");
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_ROCK_TYPE, this.rockType);
        }
    }
}
