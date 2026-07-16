package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import danger.orespawn.blocks.entity.CrystalFurnaceBlockEntity;
import danger.orespawn.init.ModBlockEntities;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Gold {@code CrystalFurnace} (BlockContainer) — registry {@code crystalfurnace} / on-variant {@code crystalfurnace}.
 * <p>
 * Gold used two blocks (off/on). Port uses one block + {@link #LIT} like vanilla furnace.
 * Stats: hardness {@code 2.0F}, resistance {@code 10.0F}; active light gold {@code 0.6F} → ~9/15.
 * Non-opaque like gold.
 * <p>
 * Gold GUI id {@code 0} + {@code TileEntityCrystalFurnace} / {@code ContainerCrystalFurnace}.
 * Port: {@link AbstractFurnaceBlock} + {@link CrystalFurnaceBlockEntity} + vanilla {@code FurnaceMenu}
 * (acceptable fidelity per port plan — gold custom GUI textures not reimplemented).
 * <p>
 * Registration:
 * <pre>{@code
 * // ModBlocks
 * public static final DeferredBlock<Block> CRYSTAL_FURNACE = register(
 *     "crystal_furnace",
 *     () -> new BlockCrystalFurnace(BlockCrystalFurnace.defaultProps()));
 *
 * // OreSpawnMain constructor
 * ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
 * }</pre>
 */
public class BlockCrystalFurnace extends AbstractFurnaceBlock {
    public static final MapCodec<BlockCrystalFurnace> CODEC = simpleCodec(BlockCrystalFurnace::new);

    public BlockCrystalFurnace(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * Gold off: {@code 2.0F / 10.0F}, no creative tab on lit variant.
     * Lit light: gold {@code setLightLevel(0.6F)} ≈ 9/15.
     */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(2.0F, 10.0F)
                .sound(SoundType.GLASS)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> state.getValue(LIT) ? 9 : 0)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    /** Gold openGui(instance, 0, .) when TE present. */
    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CrystalFurnaceBlockEntity) {
            player.openMenu((MenuProvider) be);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalFurnaceBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, ModBlockEntities.CRYSTAL_FURNACE.get());
    }

    /**
     * Gold {@code randomDisplayTick} when active — smoke + flame from facing side.
     * Furnace crackle matches vanilla AbstractFurnace feel (not in gold).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(
                    x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        Direction facing = state.getValue(FACING);
        Direction.Axis axis = facing.getAxis();
        double jitter = random.nextDouble() * 0.6 - 0.3;
        double dx = axis == Direction.Axis.X ? facing.getStepX() * 0.52 : jitter;
        double dy = random.nextDouble() * 6.0 / 16.0;
        double dz = axis == Direction.Axis.Z ? facing.getStepZ() * 0.52 : jitter;
        level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
    }
}
