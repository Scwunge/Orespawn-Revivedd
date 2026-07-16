package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code WormMedium} (EntityMob). Size 0.5×2.0, speed 0.1, health 30, attack 10, armor 8, XP 0.
 * Prefers near Small Worms; otherwise hunts players and strips boots.
 */
public class WormMedium extends Monster {
    public int upcount = 0;
    public int downcount = 0;

    public WormMedium(EntityType<? extends WormMedium> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ARMOR, 8.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public int mygetMaxHealth() {
        return 30;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.5F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold onDeath plays BIG_SPLAT once; use getDeathSound (no die() double-play)
        return danger.orespawn.util.handlers.SoundsHandler.BIG_SPLAT.get();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            return;
        }
        WormSmall worms = findNearest(WormSmall.class, 8.0, 8.0, 8.0);
        Player target = null;
        if (worms == null) {
            target = findNearest(Player.class, 8.0, 8.0, 8.0);
        }
        // gold: (worms != null || target == null) && PlayNicely == 0 → idle burrow
        if ((worms != null || target == null) && OreSpawnMain.PlayNicely == 0) {
            this.upcount = this.random.nextInt(50);
            this.downcount = 0;
            BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + 3.0, this.getZ()));
            if (isTallGrass(bid)) {
                bid = Blocks.AIR.defaultBlockState();
            }
            if (!bid.isAir()) {
                if (!isSoftEarth(bid)) {
                    this.discard();
                    return;
                }
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.1, 0.0));
                this.setPos(this.getX(), this.getY() + 0.05F, this.getZ());
            }
        } else if (this.upcount > 0) {
            this.upcount--;
            if (this.upcount == 0) {
                this.downcount = 100 + this.random.nextInt(150);
            }
            if (target != null) {
                this.pointAtEntity(target);
            }
            BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + 0.25, this.getZ()));
            if (isTallGrass(bid)) {
                bid = Blocks.AIR.defaultBlockState();
            }
            if (!bid.isAir()) {
                if (!isSoftEarth(bid)) {
                    this.discard();
                    return;
                }
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                this.setPos(this.getX(), this.getY() + 0.1F, this.getZ());
            }
        } else {
            if (this.downcount > 0) {
                this.downcount--;
            } else {
                this.upcount = 25 + this.random.nextInt(75);
            }
            BlockState bid = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() + 3.0, this.getZ()));
            if (isTallGrass(bid)) {
                bid = Blocks.AIR.defaultBlockState();
            }
            if (!bid.isAir()) {
                if (!isSoftEarth(bid)) {
                    this.discard();
                    return;
                }
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.1, 0.0));
                this.setPos(this.getX(), this.getY() + 0.05F, this.getZ());
            }
        }
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(0.0, m.y - 0.01, 0.0);
        this.zza = 0.0F;
    }

    @Override
    public void tick() {
        // gold: if (isNoDespawnRequired / func_104002_bU) noClip = false
        if (this.isPersistenceRequired()) {
            this.noPhysics = false;
        }
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.65, m.z);
    }

    /** Gold {@code canTriggerWalking} false → ignore pressure plates / crops. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public void pointAtEntity(LivingEntity e) {
        double d1 = e.getX() - this.getX();
        double d2 = e.getZ() - this.getZ();
        float d = (float) Math.atan2(d2, d1);
        float f2 = (float) (d * 180.0 / Math.PI) - 90.0F;
        this.setYRot(f2);
        this.yBodyRot = f2;
        this.yHeadRot = f2;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: combat only when PlayNicely == 0
        if (OreSpawnMain.PlayNicely != 0) {
            return;
        }
        WormSmall worms = findNearest(WormSmall.class, 8.0, 8.0, 8.0);
        if (worms == null) {
            Player target = findNearest(Player.class, 2.25, 8.0, 2.25);
            if (target != null && target.getAbilities().instabuild) {
                target = null;
            }
            if (target != null) {
                this.pointAtEntity(target);
                if (this.upcount > 0 && this.random.nextInt(15) == 1 && !target.getAbilities().instabuild) {
                    this.doHurtTarget(target);
                    if (this.random.nextInt(6) == 1) {
                        stripEquipment(target, EquipmentSlot.FEET, 15);
                    }
                }
            }
        }
    }

    private void stripEquipment(Player target, EquipmentSlot slot, int wearDivisor) {
        ItemStack boots = target.getItemBySlot(slot);
        if (boots.isEmpty()) {
            return;
        }
        target.setItemSlot(slot, ItemStack.EMPTY);
        int bid = boots.getMaxDamage() - boots.getDamageValue();
        if (bid > wearDivisor) {
            bid /= wearDivisor;
        } else {
            bid = 1;
        }
        boots.setDamageValue(Math.min(boots.getMaxDamage() - 1, boots.getDamageValue() + bid));
        ItemEntity drop = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(5) - this.random.nextInt(5),
                this.getY() + 3.0,
                this.getZ() + this.random.nextInt(5) - this.random.nextInt(5),
                boots);
        this.level().addFreshEntity(drop);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    // canBreatheUnderwater() is final on LivingEntity in 1.21 — gold returned true; cannot override

    @Override
    public boolean checkSpawnRules(LevelAccessor level, net.minecraft.world.entity.MobSpawnType spawnType) {
        // gold: !world.isDaytime()
        if (level instanceof Level lvl) {
            return !lvl.isDay();
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("inWall".equals(source.getMsgId()) || source == this.damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 2× rotten flesh, 2× leather
        for (int i = 0; i < 2; i++) {
            dropItemRand(new ItemStack(Items.ROTTEN_FLESH));
        }
        for (int i = 0; i < 2; i++) {
            dropItemRand(new ItemStack(Items.LEATHER));
        }
        dropItemRand(new ItemStack(ModItems.WORM_TOOTH.get()));
    }

    private void dropItemRand(ItemStack stack) {
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(3) - this.random.nextInt(3),
                this.getY() + 2.5 + this.random.nextInt(3),
                this.getZ() + this.random.nextInt(3) - this.random.nextInt(3),
                stack);
        this.level().addFreshEntity(var3);
    }

    @Nullable
    private <T extends Entity> T findNearest(Class<T> cls, double dx, double dy, double dz) {
        AABB box = this.getBoundingBox().inflate(dx, dy, dz);
        List<T> list = this.level().getEntitiesOfClass(cls, box);
        T nearest = null;
        double best = Double.MAX_VALUE;
        for (T e : list) {
            if (e == this) {
                continue;
            }
            double d = this.distanceToSqr(e);
            if (d < best) {
                best = d;
                nearest = e;
            }
        }
        return nearest;
    }

    private static boolean isTallGrass(BlockState state) {
        return state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN);
    }

    private static boolean isSoftEarth(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.ROOTED_DIRT) || state.is(Blocks.PODZOL) || state.is(Blocks.MYCELIUM)
                || state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE);
    }
}
