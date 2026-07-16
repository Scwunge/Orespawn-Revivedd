package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;

/**
 * Gold {@code OreRuby} ({@code oreruby}) — hardness 10 / resistance 4.
 * Drops 1 ruby (bonus quantity 1–2); XP {@code 5 + rand(5) + rand(5)} on break.
 */
public class BlockOreRuby extends Block {
    public static final MapCodec<BlockOreRuby> CODEC = simpleCodec(BlockOreRuby::new);

    public BlockOreRuby(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold: hardness 10.0F, resistance 4.0F. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(10.0f, 4.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        RandomSource random = params.getLevel().getRandom();
        // Gold quantityDropped=1, quantityDroppedWithBonus=1+rand(2)
        int count = 1 + random.nextInt(2);
        return List.of(new ItemStack(ModItems.RUBY.get(), count));
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropXp) {
        super.spawnAfterBreak(state, level, pos, tool, dropXp);
        if (dropXp) {
            // Gold: 5 + nextInt(5) + nextInt(5)
            int xp = 5 + level.random.nextInt(5) + level.random.nextInt(5);
            popExperience(level, pos, xp);
        }
    }
}
