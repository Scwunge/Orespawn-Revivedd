package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.init.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code ItemMinersDream} — instant horizontal tunnel / mine carve.
 * <p>
 * Gold: stack 16, creative tab tools; onItemUse carves a 5 high × 11 wide × 64 long
 * corridor of soft blocks toward the face the player is looking along, caps the roof,
 * and places ExtremeTorch / CrystalTorch every 5 blocks.
 */
public class ItemMinersDream extends Item {
    public static final int HEIGHT = 5;
    public static final int WIDTH = 5; // ±WIDTH each side → 11 wide
    public static final int LENGTH = 64;
    public static final int TORCH_SPACING = 5;

    public ItemMinersDream(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockPos clicked = context.getClickedPos();
        int cposx = clicked.getX();
        int cposz = clicked.getZ();

        int dirx = 0;
        int dirz = 0;
        if (cposx < 0) {
            dirx = -1;
        }
        if (cposz < 0) {
            dirz = -1;
        }

        // gold: player block pos with 0.99*dir on negative coords
        int pposx = (int) (player.getX() + 0.99 * dirx);
        int pposy = (int) player.getY();
        int pposz = (int) (player.getZ() + 0.99 * dirz);

        // must be axis-aligned with clicked block (not diagonal face)
        if (cposx - pposx != 0 && cposz - pposz != 0) {
            return InteractionResult.FAIL;
        }

        int x = cposx;
        int y = pposy;
        int z = cposz;

        int deltax = 0;
        int deltaz = 0;
        if (x - pposx < 0) {
            deltax = -1;
        }
        if (x - pposx > 0) {
            deltax = 1;
        }
        if (z - pposz < 0) {
            deltaz = -1;
        }
        if (z - pposz > 0) {
            deltaz = 1;
        }

        if (deltax == 0 && deltaz == 0) {
            return InteractionResult.FAIL;
        }
        if (deltax != 0 && deltaz != 0) {
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
            return InteractionResult.SUCCESS;
        }

        boolean crystalDim = level.dimension() == ModDimensions.CRYSTAL;
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState roofFill = crystalDim
                ? ModBlocks.CRYSTAL_STONE.get().defaultBlockState()
                : Blocks.COBBLESTONE.defaultBlockState();
        BlockState torch = ModBlocks.EXTREME_TORCH.get().defaultBlockState();

        for (int i = 0; i < HEIGHT; i++) {
            for (int k = 0; k < LENGTH; k++) {
                int solidCount = 0;

                for (int j = -WIDTH; j <= WIDTH; j++) {
                    int bx = x + k * deltax + j * deltaz;
                    int by = y + i;
                    int bz = z + k * deltaz + j * deltax;
                    BlockPos pos = new BlockPos(bx, by, bz);
                    BlockState state = level.getBlockState(pos);
                    Block bid = state.getBlock();

                    if (isCarvable(bid)) {
                        level.setBlock(pos, air, 2);
                    }

                    // roof pass on top row of tunnel
                    if (i == HEIGHT - 1) {
                        BlockPos above = pos.above();
                        BlockState aboveState = level.getBlockState(above);
                        Block aboveBid = aboveState.getBlock();
                        if (!aboveState.isAir()) {
                            solidCount++;
                        }
                        if (aboveState.isAir()
                                || aboveBid == Blocks.GRAVEL
                                || aboveBid == Blocks.SAND
                                || aboveBid == Blocks.WATER
                                || aboveBid == Blocks.LAVA
                                || aboveState.getFluidState().isSource()
                                || !aboveState.getFluidState().isEmpty()) {
                            level.setBlock(above, roofFill, 2);
                        }
                    }
                }

                // gold: if roof row had zero solids, clear the filled roof (open-air skip)
                if (i == HEIGHT - 1 && solidCount == 0) {
                    for (int j = -WIDTH; j <= WIDTH; j++) {
                        int bx = x + k * deltax + j * deltaz;
                        int bz = z + k * deltaz + j * deltax;
                        level.setBlock(new BlockPos(bx, y + i + 1, bz), air, 2);
                    }
                }
            }
        }

        // torches every TORCH_SPACING along floor center
        for (int k = 0; k < LENGTH; k += TORCH_SPACING) {
            int bx = x + k * deltax;
            int bz = z + k * deltaz;
            BlockPos floor = new BlockPos(bx, y - 1, bz);
            BlockPos torchPos = new BlockPos(bx, y, bz);
            Block floorBid = level.getBlockState(floor).getBlock();

            if ((floorBid == Blocks.STONE
                            || floorBid == Blocks.DIRT
                            || floorBid == Blocks.GRAVEL
                            || floorBid == Blocks.NETHERRACK
                            || floorBid == Blocks.END_STONE
                            || floorBid == Blocks.BEDROCK
                            || floorBid == Blocks.COBBLESTONE
                            || floorBid == Blocks.DEEPSLATE
                            || floorBid == Blocks.GRASS_BLOCK)
                    && level.getBlockState(torchPos).isAir()) {
                level.setBlock(torchPos, torch, 2);
            }

            // gold CrystalStone floor → CrystalTorch; stand-in ExtremeTorch (no crystal_torch yet)
            if (floorBid == ModBlocks.CRYSTAL_STONE.get() && level.getBlockState(torchPos).isAir()) {
                level.setBlock(torchPos, torch, 2);
            }
        }

        ItemStack stack = context.getItemInHand();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    /** Gold soft/carvable set: stone, dirt, gravel, water/lava, netherrack, end stone, CrystalStone. */
    private static boolean isCarvable(Block bid) {
        return bid == Blocks.STONE
                || bid == Blocks.DIRT
                || bid == Blocks.GRASS_BLOCK
                || bid == Blocks.GRAVEL
                || bid == Blocks.SAND
                || bid == Blocks.WATER
                || bid == Blocks.LAVA
                || bid == Blocks.NETHERRACK
                || bid == Blocks.END_STONE
                || bid == Blocks.COBBLESTONE
                || bid == Blocks.DEEPSLATE
                || bid == Blocks.ANDESITE
                || bid == Blocks.DIORITE
                || bid == Blocks.GRANITE
                || bid == Blocks.TUFF
                || bid == ModBlocks.CRYSTAL_STONE.get();
    }
}
