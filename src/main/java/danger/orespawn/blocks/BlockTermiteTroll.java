package danger.orespawn.blocks;

import danger.orespawn.entity.Termite;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Gold {@code BlockTermiteTroll} — nest trap. Breaking spawns termites that enrage at the
 * breaker; any nearby colony also joins ({@link Termite#alertColony}).
 */
public class BlockTermiteTroll extends Block {
    /** Set in {@link #playerWillDestroy} so {@link #onRemove} knows who broke the nest. */
    private static final ThreadLocal<Player> BREAKER = new ThreadLocal<>();

    public BlockTermiteTroll(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BREAKER.set(player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (level.isClientSide || state.is(newState.getBlock())) {
            return;
        }
        Player breaker = BREAKER.get();
        BREAKER.remove();
        LivingEntity rageAt = breaker;
        if (rageAt == null) {
            // explosion / piston / etc. — nearest player if any
            rageAt = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12.0, false);
        }
        spawnSwarm(level, pos, rageAt);
    }

    private static void spawnSwarm(Level level, BlockPos pos, LivingEntity breaker) {
        EntityType<?> type = resolveSpawnType();
        for (int i = 0; i < 20; i++) {
            Entity entity = type.create(level);
            if (entity == null) {
                continue;
            }
            entity.moveTo(
                    pos.getX() + 0.5,
                    pos.getY(),
                    pos.getZ() + 0.5,
                    level.random.nextFloat() * 360.0F,
                    0.0F);
            level.addFreshEntity(entity);
            if (entity instanceof Mob mob) {
                mob.playAmbientSound();
            }
            if (entity instanceof Termite termite && breaker != null) {
                termite.enrageAt(breaker);
            }
        }
        if (breaker != null) {
            Termite.alertColony(level, pos, breaker, 16.0);
        }
    }

    private static EntityType<?> resolveSpawnType() {
        DeferredHolder<EntityType<?>, ? extends EntityType<?>> termite = ModEntities.TERMITE;
        if (termite.isBound()) {
            return termite.get();
        }
        return ModEntities.RED_ANT.get();
    }
}
