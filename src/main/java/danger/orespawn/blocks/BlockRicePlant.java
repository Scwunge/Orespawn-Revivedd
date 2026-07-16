package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code BlockRice} — {@code BlockCrops}; seedfood {@code ItemRadish}-style on
 * {@code CrystalGrass} (name {@code rice}, food 5 / 0.65). Simplified: age 0–3 on
 * farmland / dirt / grass / crystal grass.
 */
public class BlockRicePlant extends BushBlock {
    public static final MapCodec<BlockRicePlant> CODEC = simpleCodec(BlockRicePlant::new);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    public static final int MAX_AGE = 3;

    private static final VoxelShape[] SHAPES = new VoxelShape[] {
            Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
    };

    public BlockRicePlant(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public MapCodec<BlockRicePlant> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[Math.min(state.getValue(AGE), MAX_AGE)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD)
                || state.is(ModBlocks.CRYSTAL_GRASS.get());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return this.mayPlaceOn(below, level, pos.below());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        int age = state.getValue(AGE);
        if (age < MAX_AGE && level.getRawBrightness(pos, 0) >= 9 && random.nextInt(3) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_ALL);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        RandomSource random = params.getLevel().getRandom();
        if (state.getValue(AGE) >= MAX_AGE) {
            // Gold quantityDropped: 2 + nextInt(4)
            drops.add(new ItemStack(this.getCropItem(), 2 + random.nextInt(4)));
        } else {
            drops.add(new ItemStack(this.getBaseSeedId()));
        }
        return drops;
    }

    protected ItemLike getBaseSeedId() {
        // seedfood ItemNameBlockItem ModItems.RICE
        return ModItems.RICE.get();
    }

    protected ItemLike getCropItem() {
        return ModItems.RICE.get();
    }

    public boolean isMature(BlockState state) {
        return state.getValue(AGE) >= MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }
}
