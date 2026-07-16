package danger.orespawn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code OreGenericEgg} — egg-ore blocks. On break, 50% chance to drop
 * {@code 5 + rand(3) + rand(3)} XP (gold {@code dropAmount}).
 */
public class BlockEggOre extends Block {
    public BlockEggOre(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropXp) {
        super.spawnAfterBreak(state, level, pos, tool, dropXp);
        if (dropXp && level.random.nextBoolean()) {
            int xp = 5 + level.random.nextInt(3) + level.random.nextInt(3);
            popExperience(level, pos, xp);
        }
    }
}
