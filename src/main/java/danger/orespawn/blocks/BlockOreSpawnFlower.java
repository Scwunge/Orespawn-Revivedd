package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Gold {@code MyBlockFlower} — decorative plants with no collision, cross render (cutout).
 * <p>
 * Gold variants: {@code flower_pink}, {@code flower_blue}, {@code flower_black}, {@code flower_scary}
 * plus crystal flowers ({@code crystalflower_red/green/blue/yellow}) — same class, no morph.
 * <p>
 * Day/night morph (gold {@code checkFlowerChange}):
 * <ul>
 *   <li>Night ({@code dayTime % 24000 > 12000}): pink → black, blue → scary</li>
 *   <li>Day: black → pink, scary → blue</li>
 * </ul>
 * Pass the partner block supplier at construction (pass DeferredBlock refs from ModBlocks).
 * Crystal flowers: pass {@code null} partner (no day/night morph).
 * <p>
 * Registration example:
 * <pre>{@code
 * public static final DeferredBlock<Block> FLOWER_PINK = register("flower_pink",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), () -> FLOWER_BLACK.get(), true));
 * public static final DeferredBlock<Block> FLOWER_BLACK = register("flower_black",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), () -> FLOWER_PINK.get(), false));
 * public static final DeferredBlock<Block> FLOWER_BLUE = register("flower_blue",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), () -> FLOWER_SCARY.get(), true));
 * public static final DeferredBlock<Block> FLOWER_SCARY = register("flower_scary",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), () -> FLOWER_BLUE.get(), false));
 * // Crystal flowers (gold MyBlockFlower, no morph):
 * public static final DeferredBlock<Block> CRYSTALFLOWER_RED = register("crystalflower_red",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
 * public static final DeferredBlock<Block> CRYSTALFLOWER_GREEN = register("crystalflower_green",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
 * public static final DeferredBlock<Block> CRYSTALFLOWER_BLUE = register("crystalflower_blue",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
 * public static final DeferredBlock<Block> CRYSTALFLOWER_YELLOW = register("crystalflower_yellow",
 *     () -> new BlockOreSpawnFlower(BlockOreSpawnFlower.defaultProps(), null, false));
 * }</pre>
 * Client: {@code ItemBlockRenderTypes.setRenderLayer(., RenderType.cutout())}.
 */
public class BlockOreSpawnFlower extends BushBlock {
    public static final MapCodec<BlockOreSpawnFlower> CODEC = simpleCodec(p -> new BlockOreSpawnFlower(p, null, false));

    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    /**
     * Partner to swap to when the day/night condition matches {@link #transformsAtNight}.
     * Null = no morph (e.g. crystal flowers).
     */
    @Nullable
    private final Supplier<Block> transformPartner;
    /**
     * {@code true}: become partner at night (pink/blue).
     * {@code false}: become partner during day (black/scary).
     */
    private final boolean transformsAtNight;

    public BlockOreSpawnFlower(
            BlockBehaviour.Properties properties,
            @Nullable Supplier<Block> transformPartner,
            boolean transformsAtNight) {
        super(properties);
        this.transformPartner = transformPartner;
        this.transformsAtNight = transformsAtNight;
    }

    /** Gold flower material + no AABB collision; random ticks for day/night morph. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY)
                .randomTicks();
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Gold AABB ~ 0.3×0.6×0.3 centered; slight random offset via OffsetType.XZ
        var offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    /** Gold {@code canPlaceBlockOn}: grass, dirt, farmland, CrystalGrass. */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.FARMLAND)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.MUD)
                // Crystal grass registry name from ModBlocks (string-safe if not loaded as class field)
                || state.getBlock().builtInRegistryHolder().key().location().getPath().equals("crystal_grass");
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return this.transformPartner != null;
    }

    /** Gold {@code updateTick} / {@code checkFlowerChange} day-night morph. */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.transformPartner == null) {
            return;
        }
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        long t = level.getDayTime() % 24000L;
        boolean night = t > 12000L;
        if (night == this.transformsAtNight) {
            Block partner = this.transformPartner.get();
            if (partner != null && partner != this) {
                level.setBlock(pos, partner.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
