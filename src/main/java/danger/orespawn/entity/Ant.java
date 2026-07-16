package danger.orespawn.entity;

import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.AntDimensionPortal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 1.7.10 {@code EntityAnt} base (and brown ant when registered as {@code ant}).
 * Empty-hand on <b>exact</b> {@code Ant} class → Utopia; subclasses override destination.
 */
public class Ant extends Animal {
    protected double moveSpeed = 0.15;

    public Ant(EntityType<? extends Ant> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 9, 1.0));
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (!this.level().isClientSide) {
            this.updateAITick();
        }
    }

    /** Gold {@code updateAITick} — clear revenge target occasionally. */
    protected void updateAITick() {
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
    }

    /**
     * Gold {@code Ant.interact}: empty-hand returns true (consumes interact); held item returns false.
     * Gold printed {@code "ANT INTERACTED"} — debug only, not ported to release.
     * Gold zero-count stack cleanup mapped to empty {@link ItemStack}.
     * Gold required {@code EntityPlayerMP}; map to {@link ServerPlayer} (server-side success).
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player == null) {
            return InteractionResult.FAIL;
        }
        // Gold: only EntityPlayerMP continues; client / non-server → false
        if (!(player instanceof ServerPlayer)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        // Gold: if stack non-null and count <= 0, clear slot and treat as empty
        if (!stack.isEmpty() && stack.getCount() <= 0) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            stack = ItemStack.EMPTY;
        }
        // 1.7.10 brown ant only (subclasses use their own destination)
        if (stack.isEmpty() && this.getClass() == Ant.class) {
            AntDimensionPortal.tryToggle(
                    player, stack, ModDimensions.UTOPIA, "Warped to Utopia.");
        }
        // Gold: return var2 == null → true only for empty hand
        return stack.isEmpty() ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public int mygetMaxHealth() {
        return 1;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
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
    protected float getSoundVolume() {
        return 0.0F;
    }

    /** Gold empty {@code playStepSound}. */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // silent
    }

    /** Gold empty {@code dropFewItems}. */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // no drops
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        return this.findBuddies() <= 4;
    }

    private int findBuddies() {
        List<Ant> list = this.level().getEntitiesOfClass(Ant.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0));
        return list.size();
    }
}
