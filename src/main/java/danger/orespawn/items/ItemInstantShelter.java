package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code InstantShelter} — registers as {@code instantshelter}, decorations tab.
 * <p>
 * Gold: stack 16; onItemUse builds a 7×7×5 (width/length/height ±3 / +3 / 0.4) oak
 * plank cabin centered on the player with cobble floor, glass top walls, door hole
 * toward the clicked face, furnace + crafting table + starter chest inside.
 * Explode SFX; consume unless creative.
 * <p>
 * Inventory icon: {@code textures/item/instantshelter.png}. Register in {@code ModItems}.
 */
public class ItemInstantShelter extends Item {
    /** Gold max stack. */
    public static final int MAX_STACK = 16;
    /** Gold half-width / half-length (i,j from -n.n). */
    public static final int WIDTH = 3;
    public static final int LENGTH = 3;
    /** Gold interior wall height (k 1.height; roof at height+1). */
    public static final int HEIGHT = 3;

    public ItemInstantShelter(Properties properties) {
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

        int pposx = (int) (player.getX() + 0.99 * dirx);
        int pposy = (int) player.getY();
        int pposz = (int) (player.getZ() + 0.99 * dirz);

        if (cposx - pposx != 0 && cposz - pposz != 0) {
            return InteractionResult.FAIL;
        }

        int deltax = 0;
        int deltaz = 0;
        // gold furnace/chest meta: 2=N, 3=S, 4=W, 5=E
        Direction stuffFacing = Direction.NORTH;

        if (cposx - pposx < 0) {
            deltax = -1;
            stuffFacing = Direction.SOUTH; // gold stuffdir 3
        }
        if (cposx - pposx > 0) {
            deltax = 1;
            stuffFacing = Direction.NORTH; // gold stuffdir 2
        }
        if (cposz - pposz < 0) {
            deltaz = -1;
            stuffFacing = Direction.EAST; // gold stuffdir 5
        }
        if (cposz - pposz > 0) {
            deltaz = 1;
            stuffFacing = Direction.WEST; // gold stuffdir 4
        }

        if (deltax == 0 && deltaz == 0) {
            return InteractionResult.FAIL;
        }
        if (deltax != 0 && deltaz != 0) {
            return InteractionResult.FAIL;
        }

        // gold: origin snaps to player feet; y = pposy - 1
        int x = pposx;
        int y = pposy - 1;
        int z = pposz;

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

        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState glass = Blocks.GLASS.defaultBlockState();

        for (int i = -WIDTH; i <= WIDTH; i++) {
            for (int j = -LENGTH; j <= LENGTH; j++) {
                for (int k = 0; k <= HEIGHT + 1; k++) {
                    BlockPos pos = new BlockPos(x + i, y + k, z + j);
                    if (k == HEIGHT + 1) {
                        // roof
                        level.setBlock(pos, planks, 2);
                    } else if (k == 0) {
                        // floor
                        level.setBlock(pos, cobble, 2);
                    } else if (i != WIDTH && j != LENGTH && i != -WIDTH && j != -LENGTH) {
                        // hollow interior
                        level.setBlock(pos, air, 2);
                    } else if (k == HEIGHT) {
                        // top wall ring = glass
                        level.setBlock(pos, glass, 2);
                    } else if ((k == 1 || k == 2) && i == deltax * WIDTH && j == deltaz * LENGTH) {
                        // door hole toward clicked direction
                        level.setBlock(pos, air, 2);
                    } else {
                        // walls
                        level.setBlock(pos, planks, 2);
                    }
                }
            }
        }

        // furniture: gold var32 offsets along look, j = length-1 across, k = 1
        int k = 1;
        int j = LENGTH - 1;

        // furnace at var32=2
        int fx = x + 2 * deltax + j * deltaz;
        int fz = z + 2 * deltaz + j * deltax;
        BlockPos furnacePos = new BlockPos(fx, y + k, fz);
        level.setBlock(
                furnacePos,
                Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, stuffFacing),
                3);

        // crafting table at var32=1
        int cx = x + 1 * deltax + j * deltaz;
        int cz = z + 1 * deltaz + j * deltax;
        level.setBlock(new BlockPos(cx, y + k, cz), Blocks.CRAFTING_TABLE.defaultBlockState(), 2);

        // chest at var32=0 + starter loot
        int chx = x + 0 * deltax + j * deltaz;
        int chz = z + 0 * deltaz + j * deltax;
        BlockPos chestPos = new BlockPos(chx, y + k, chz);
        level.setBlock(
                chestPos,
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, stuffFacing),
                3);

        if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest) {
            // gold InstantShelter starter kit (MCP 1.7.10 item fields)
            chest.setItem(0, new ItemStack(Items.RED_BED));
            chest.setItem(1, new ItemStack(Items.CAKE));
            chest.setItem(2, new ItemStack(Items.COOKED_PORKCHOP, 8));
            chest.setItem(3, new ItemStack(Blocks.TORCH, 32));
            chest.setItem(4, new ItemStack(Items.COAL, 16));
            chest.setItem(5, new ItemStack(Items.IRON_INGOT));
            chest.setItem(6, new ItemStack(Items.IRON_INGOT));
            chest.setItem(7, new ItemStack(Items.FLINT_AND_STEEL));
            chest.setItem(8, new ItemStack(Items.IRON_SHOVEL));
            chest.setItem(9, new ItemStack(Items.IRON_SWORD));
            chest.setItem(10, new ItemStack(Items.IRON_PICKAXE));
            chest.setItem(11, new ItemStack(Items.BUCKET));
            // gold MyOreSaltBlock ×4 — salt_ore is the ported salt block
            chest.setItem(12, new ItemStack(ModBlocks.SALT_ORE.get(), 4));
            chest.setItem(13, new ItemStack(Blocks.CHEST));
        }

        ItemStack stack = context.getItemInHand();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
