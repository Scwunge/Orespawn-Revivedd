package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModDimensions;
import danger.orespawn.util.AntDimensionPortal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code RedAnt} extends Ant. Size 0.2×0.2, speed 0.2, health 2, attack 1, XP 1.
 * Always aggressive toward players (gold {@code NearestAttackableTarget}).
 * Gold tick: rare proximity nip (1/15 for 1.0) on closest player within 1.5.
 * Nest break ({@code BlockRedAntTroll}) enrages spawned/nearby ants at the breaker immediately.
 * Empty-hand interact toggles any dim ↔ {@link ModDimensions#EXTREME} (1.7.10 Dimension-Extreme).
 * ConquerantFix Mining remains via {@code /orespawn mining}.
 */
public class RedAnt extends Ant {
    int attackDelay = 20;

    public RedAnt(EntityType<? extends RedAnt> type, Level level) {
        super(type, level);
        this.moveSpeed = 0.2;
        this.xpReward = 1;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Ant.createAttributes()
                .add(Attributes.MAX_HEALTH, 2.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 24.0); // nest alert / chase range
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4F));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 10, 1.0));
        // gold: NearestAttackableTarget only when PlayNicely == 0
        if (OreSpawnMain.PlayNicely == 0) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    public int mygetMaxHealth() {
        return 2;
    }

    /** Call when nest is broken — focus the breaker immediately (no wait for target goal). */
    public void enrageAt(LivingEntity attacker) {
        if (attacker == null || !attacker.isAlive() || this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (attacker instanceof Player player && (player.isSpectator() || player.getAbilities().instabuild)) {
            return;
        }
        this.setLastHurtByMob(attacker);
        this.setTarget(attacker);
    }

    /** Alert all red ants in range to attack {@code attacker} (nest swarm / nearby colony). */
    public static void alertColony(Level level, BlockPos center, LivingEntity attacker, double range) {
        if (level.isClientSide || attacker == null) {
            return;
        }
        List<RedAnt> list = level.getEntitiesOfClass(RedAnt.class, new AABB(center).inflate(range));
        for (RedAnt ant : list) {
            ant.enrageAt(attacker);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.random.nextInt(15) != 0) {
            return false;
        }
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        return target.hurt(this.damageSources().mobAttack(this), 1.0F);
    }

    /**
     * 1.7.10 {@code EntityRedAnt}: empty hand toggles ↔ Extreme (not CF Mining).
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!itemstack.isEmpty() && itemstack.getCount() <= 0) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            itemstack = ItemStack.EMPTY;
        }
        if (itemstack.isEmpty() && player instanceof ServerPlayer) {
            AntDimensionPortal.tryToggle(
                    player, itemstack, ModDimensions.EXTREME, "Warped to the Extreme Dimension.");
        }
        // Avoid Ant Utopia path: return SUCCESS/PASS without super teleport
        if (player == null) {
            return InteractionResult.FAIL;
        }
        if (!(player instanceof ServerPlayer)) {
            return InteractionResult.PASS;
        }
        return itemstack.isEmpty() ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !this.isDeadOrDying()) {
            if (this.attackDelay > 0) {
                this.attackDelay--;
            }
            if (this.attackDelay <= 0) {
                this.attackDelay = 20;
                // gold: proximity nip only when PlayNicely == 0
                if (this.level().getDifficulty() != Difficulty.PEACEFUL && OreSpawnMain.PlayNicely == 0) {
                    LivingEntity e = this.level().getNearestPlayer(this, 1.5);
                    if (e != null) {
                        this.doHurtTarget(e);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }
}
