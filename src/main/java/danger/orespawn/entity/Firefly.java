package danger.orespawn.entity;

import danger.orespawn.init.ModBlocks;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Firefly} (EntityAmbientCreature). Size 0.4×0.8, speed 0.1, health 1, attack 0.
 * Soft area light via invisible {@link Blocks#LIGHT} so surroundings glow slightly.
 */
public class Firefly extends AmbientCreature {
    /** Soft neighborhood light (0–15). Slightly brighter while the gold blinker is on. */
    private static final int GLOW_DIM = 5;
    private static final int GLOW_BRIGHT = 9;

    int myBlink;
    int blinker;
    @Nullable
    private BlockPos currentFlightTarget;
    /** Last server light-block position we placed (cleaned on move/remove). */
    @Nullable
    private BlockPos lightBlockPos;
    private int lastLightLevel = -1;

    public Firefly(EntityType<? extends Firefly> type, Level level) {
        super(type, level);
        this.myBlink = 20 + this.random.nextInt(20);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public float getBlink() {
        return this.blinker < this.myBlink / 2 ? 240.0F : 0.0F;
    }

    public int mygetMaxHealth() {
        return 1;
    }

    @Override
    protected float getSoundVolume() {
        return 0.0F;
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
    public void tick() {
        super.tick();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(m.x, m.y * 0.600000023841, m.z);
        this.blinker++;
        if (this.blinker > this.myBlink) {
            this.blinker = 0;
        }
        if (!this.level().isClientSide && !this.isPersistenceRequired()) {
            long t = this.level().getDayTime() % 24000L;
            if (t <= 11000L && this.random.nextInt(500) == 1) {
                this.discard();
                return;
            }
        }
        // Real world light (server) — pulses a little with the gold blinker
        if (!this.level().isClientSide && this.isAlive()) {
            this.updateAreaLight();
        }
    }

    /**
     * Places an invisible {@link Blocks#LIGHT} at the firefly so nearby blocks get lit.
     * Dim while off-blink, brighter while blinking. Cleans up previous cell when we move.
     */
    private void updateAreaLight() {
        if (!(this.level() instanceof ServerLevel server)) {
            return;
        }
        BlockPos want = this.blockPosition();
        int level = this.getBlink() > 0.0F ? GLOW_BRIGHT : GLOW_DIM;

        if (this.lightBlockPos != null
                && (!this.lightBlockPos.equals(want) || this.lastLightLevel != level)) {
            this.clearAreaLight(server, this.lightBlockPos);
            this.lightBlockPos = null;
            this.lastLightLevel = -1;
        }

        if (!this.canHostLight(server, want)) {
            return;
        }

        BlockState desired = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, level);
        BlockState cur = server.getBlockState(want);
        if (!cur.is(Blocks.LIGHT) || cur.getValue(LightBlock.LEVEL) != level) {
            // flag 2|16: notify clients + skip neighbor shape; light engine still updates
            server.setBlock(want, desired, 2 | 16);
        }
        this.lightBlockPos = want.immutable();
        this.lastLightLevel = level;
    }

    private boolean canHostLight(ServerLevel server, BlockPos pos) {
        BlockState s = server.getBlockState(pos);
        return s.isAir() || s.is(Blocks.LIGHT);
    }

    private void clearAreaLight(ServerLevel server, @Nullable BlockPos pos) {
        if (pos == null) {
            return;
        }
        if (!server.getBlockState(pos).is(Blocks.LIGHT)) {
            return;
        }
        // Don't snuff another firefly's lamp in the same cell
        AABB box = new AABB(pos).inflate(0.25);
        for (Firefly other : server.getEntitiesOfClass(Firefly.class, box)) {
            if (other != this && other.isAlive() && pos.equals(other.blockPosition())) {
                return;
            }
        }
        server.removeBlock(pos, false);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel server) {
            this.clearAreaLight(server, this.lightBlockPos);
            this.lightBlockPos = null;
        }
        super.remove(reason);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.currentFlightTarget == null) {
            this.currentFlightTarget = this.blockPosition();
        }
        if (this.random.nextInt(40) == 0
                || this.currentFlightTarget.distToCenterSqr(this.getX(), this.getY(), this.getZ()) < 2.0) {
            int keepTrying = 25;
            BlockState bid = Blocks.STONE.defaultBlockState();
            while (!bid.isAir() && keepTrying != 0) {
                keepTrying--;
                this.currentFlightTarget = BlockPos.containing(
                        this.getX() + this.random.nextInt(4) - this.random.nextInt(4),
                        this.getY() + this.random.nextInt(4) - 2,
                        this.getZ() + this.random.nextInt(4) - this.random.nextInt(4));
                bid = this.level().getBlockState(this.currentFlightTarget);
            }
        }
        double var1 = this.currentFlightTarget.getX() + 0.5 - this.getX();
        double var3 = this.currentFlightTarget.getY() + 0.1 - this.getY();
        double var5 = this.currentFlightTarget.getZ() + 0.5 - this.getZ();
        Vec3 m = this.getDeltaMovement();
        this.setDeltaMovement(
                m.x + (Math.signum(var1) * 0.2 - m.x) * 0.1,
                m.y + (Math.signum(var3) * 0.7F - m.y) * 0.1,
                m.z + (Math.signum(var5) * 0.2 - m.z) * 0.1);
        float var7 = (float) (Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * 180.0 / Math.PI) - 90.0F;
        float var8 = Mth.wrapDegrees(var7 - this.getYRot());
        this.setZza(0.2F);
        this.setYRot(this.getYRot() + var8 / 4.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: extreme torch as drop item
        this.spawnAtLocation(new ItemStack(ModBlocks.EXTREME_TORCH.get()));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: despawn only during day if not persistent
        if (!this.level().isDay()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    /** Gold: no push-out of blocks while flying. */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    /**
     * Gold {@code getCanSpawnHere}: air, night, y≥50, ≤10 buddies in 20×8×20.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!level.getBlockState(this.blockPosition()).isAir()) {
            return false;
        }
        if (level instanceof Level lvl && lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        return this.findBuddies() <= 10;
    }

    private int findBuddies() {
        List<Firefly> list =
                this.level().getEntitiesOfClass(Firefly.class, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
        return list.size();
    }
}
