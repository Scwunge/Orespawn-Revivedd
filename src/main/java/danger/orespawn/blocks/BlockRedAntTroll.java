package danger.orespawn.blocks;

import danger.orespawn.entity.RedAnt;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code BlockRedAntTroll} (registry {@code red_ant_troll_block}).
 * Nest trap: break spawns 20 red ants that immediately target the breaker;
 * nearby red ants also join via {@link RedAnt#alertColony}.
 */
public class BlockRedAntTroll extends Block {
    /** Set in {@link #playerWillDestroy} so {@link #onRemove} knows who broke the nest. */
    private static final ThreadLocal<Player> BREAKER = new ThreadLocal<>();

    public BlockRedAntTroll(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BREAKER.set(player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * Gold {@code breakBlock} / {@code func_180663_b}: spawn 20 RedAnt with random yaw
     * and play ambient sound (gold {@code playLivingSound}). Port also enrages at breaker.
     */
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
        for (int i = 0; i < 20; i++) {
            Entity ant = ModEntities.RED_ANT.get().create(level);
            if (ant == null) {
                continue;
            }
            // gold spawn coords: block pos (no +0.5 offset)
            ant.moveTo(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    level.random.nextFloat() * 360.0F,
                    0.0F);
            level.addFreshEntity(ant);
            if (ant instanceof Mob mob) {
                mob.playAmbientSound();
            }
            if (ant instanceof RedAnt redAnt && breaker != null) {
                redAnt.enrageAt(breaker);
            }
        }
        if (breaker != null) {
            RedAnt.alertColony(level, pos, breaker, 16.0);
        }
    }
}
