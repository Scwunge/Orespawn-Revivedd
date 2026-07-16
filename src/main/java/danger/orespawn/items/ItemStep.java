package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code StepUp} / {@code StepDown} / {@code StepAccross} — place a cobble path of
 * length 32 in the facing direction (8-way), with ExtremeTorch every 8 steps when air.
 * <ul>
 *   <li>{@link Kind#UP} — rising stairs: y+k-1 each step</li>
 *   <li>{@link Kind#DOWN} — descending stairs: y-k-1 each step</li>
 *   <li>{@link Kind#ACROSS} — flat bridge at click y+1 floor</li>
 * </ul>
 * Stack 16; consume unless creative. Explode SFX + particles on use.
 */
public class ItemStep extends Item {
    public static final int MAX_STACK = 16;
    public static final int LENGTH = 33;

    public enum Kind {
        UP,
        DOWN,
        ACROSS
    }

    private final Kind kind;

    public ItemStep(Properties properties, Kind kind) {
        super(properties);
        this.kind = kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockPos clicked = context.getClickedPos();
        int x = clicked.getX();
        int y = clicked.getY() + 1;
        int z = clicked.getZ();

        int[] delta = facingDelta(player.getYRot());
        int deltax = delta[0];
        int deltaz = delta[1];
        if (deltax == 0 && deltaz == 0) {
            return InteractionResult.FAIL;
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                1.0F,
                1.5F);

        if (level.isClientSide) {
            for (int i = 0; i < 6; i++) {
                double px = x + level.random.nextFloat() - level.random.nextFloat();
                double py = y + level.random.nextFloat() + (this.kind == Kind.UP ? 1.0 : 0.0);
                double pz = z + level.random.nextFloat() - level.random.nextFloat();
                level.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.EXPLOSION, px, py, pz, 0.0, 0.0, 0.0);
                level.addParticle(ParticleTypes.SMOKE, px, py, pz, 0.0, 0.0, 0.0);
            }
            return InteractionResult.SUCCESS;
        }

        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState torch = ModBlocks.EXTREME_TORCH.get().defaultBlockState();

        for (int k = 1; k < LENGTH; k++) {
            int bx = x + k * deltax;
            int bz = z + k * deltaz;
            int byFloor;
            int byTorch;
            switch (this.kind) {
                case UP -> {
                    byFloor = y + k - 1;
                    byTorch = y + k;
                }
                case DOWN -> {
                    byFloor = y - k - 1;
                    byTorch = y - k;
                }
                default -> {
                    byFloor = y - 1;
                    byTorch = y;
                }
            }

            BlockPos floorPos = new BlockPos(bx, byFloor, bz);
            if (!level.getBlockState(floorPos).isAir()) {
                break;
            }
            level.setBlock(floorPos, cobble, 2);

            if ((k - 1) % 8 == 0) {
                BlockPos torchPos = new BlockPos(bx, byTorch, bz);
                if (level.getBlockState(torchPos).isAir()) {
                    level.setBlock(torchPos, torch, 2);
                }
            }
        }

        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Gold: yaw + 22.5, mod 360, /45 → 8 directions.
     *
     * @return [dx, dz]
     */
    private static int[] facingDelta(float yRot) {
        float f = yRot + 22.5F;
        f %= 360.0F;
        if (f < 0.0F) {
            f += 360.0F;
        }
        f /= 45.0F;
        return switch ((int) f) {
            case 0 -> new int[] {0, 1};
            case 1 -> new int[] {-1, 1};
            case 2 -> new int[] {-1, 0};
            case 3 -> new int[] {-1, -1};
            case 4 -> new int[] {0, -1};
            case 5 -> new int[] {1, -1};
            case 6 -> new int[] {1, 0};
            case 7 -> new int[] {1, 1};
            default -> new int[] {0, 0};
        };
    }
}
