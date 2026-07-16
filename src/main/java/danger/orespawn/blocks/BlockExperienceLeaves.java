package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;

/**
 * Gold {@code BlockExperienceLeaves} ({@code leaves_experience}).
 * No block drops. At night: rare exp bottle item above, exp bottle entity below;
 * client fireworksSpark particles.
 */
public class BlockExperienceLeaves extends LeavesBlock {
    public static final MapCodec<BlockExperienceLeaves> CODEC = simpleCodec(BlockExperienceLeaves::new);

    public BlockExperienceLeaves(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold hardness 0.2, leaves sound, light opacity 1. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isSuffocating((s, g, p) -> false)
                .isViewBlocking((s, g, p) -> false)
                .isRedstoneConductor((s, g, p) -> false)
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    /** Gold always ticked for night XP spawns. */
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // Gold quantityDropped = 0
        return Collections.emptyList();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.isClientSide) {
            return;
        }
        int range = 2;
        if (!level.hasChunksAt(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
            return;
        }
        long t = level.getDayTime() % 24000L;
        // Gold: only night window 14000–22000 for drops
        if (t < 14000L || t > 22000L) {
            return;
        }
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= 0; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) > 3) {
                        continue;
                    }
                    BlockPos support = pos.offset(dx, dy, dz);
                    BlockState supportState = level.getBlockState(support);
                    if (isLeafSupport(supportState, level, support)) {
                        if (random.nextInt(65) == 1 && level.getBlockState(pos.above()).isAir()) {
                            Block.popResource(level, pos.above(2), new ItemStack(Items.EXPERIENCE_BOTTLE));
                        }
                        if (random.nextInt(75) == 1 && level.getBlockState(pos.below()).isAir()) {
                            ThrownExperienceBottle bottle = new ThrownExperienceBottle(
                                    level, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5);
                            bottle.shoot(
                                    (random.nextFloat() - random.nextFloat()) / 2.0F,
                                    -0.1F,
                                    (random.nextFloat() - random.nextFloat()) / 2.0F,
                                    0.4F,
                                    5.0F);
                            level.addFreshEntity(bottle);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        long t = level.getDayTime() % 24000L;
        if (t < 13000L || t > 23000L) {
            return;
        }
        int rate = 0;
        if (t < 14000L) {
            rate = (14000 - (int) t) / 2;
        }
        if (t > 22000L) {
            rate = (int) (t - 22000L) / 2;
        }
        if (random.nextInt(200 + rate) == 1 && level.getBlockState(pos.above()).isAir()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(
                        ParticleTypes.FIREWORK,
                        pos.getX() + 0.5,
                        pos.getY() + 1.25,
                        pos.getZ() + 0.5,
                        random.nextGaussian(),
                        Math.abs(random.nextGaussian()),
                        random.nextGaussian());
            }
        }
        if (random.nextInt(40 + rate) == 1 && level.getBlockState(pos.below()).isAir()) {
            for (int i = 0; i < 4; i++) {
                level.addParticle(
                        ParticleTypes.FIREWORK,
                        pos.getX() + 0.5,
                        pos.getY() - 1.25,
                        pos.getZ() + 0.5,
                        random.nextFloat() - random.nextFloat(),
                        -Math.abs(random.nextFloat()),
                        random.nextFloat() - random.nextFloat());
            }
        }
    }

    private static boolean isLeafSupport(BlockState state, BlockGetter level, BlockPos pos) {
        Block b = state.getBlock();
        return state.is(BlockTags.LOGS)
                || b instanceof BlockCrystalTreeLog
                || b instanceof BlockSkyTreeLog
                || b instanceof BlockDuplicatorLog;
    }
}
