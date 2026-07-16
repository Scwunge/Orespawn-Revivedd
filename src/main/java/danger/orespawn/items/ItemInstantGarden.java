package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
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
 * Gold {@code InstantGarden} — registers as {@code instantgarden}, decorations tab.
 * <p>
 * Gold: stack 16; onItemUse along axis toward clicked face carves a 10 high × 15 wide
 * (±7) × 18 long air corridor, grass floor at y-1, then plants crop rows (farmland +
 * crops / water trenches / cactus row). OreSpawn plants not yet ported use vanilla
 * stand-ins (see row comments). Explode SFX; consume unless creative.
 * <p>
 * Inventory icon: {@code textures/item/instantgarden.png}. Register in {@code ModItems}.
 */
public class ItemInstantGarden extends Item {
    /** Gold max stack. */
    public static final int MAX_STACK = 16;
    /** Gold clear height. */
    public static final int HEIGHT = 10;
    /** Gold half-width (j from -width.width → 15 wide). */
    public static final int WIDTH = 7;
    /** Gold length along look axis. */
    public static final int LENGTH = 18;

    public ItemInstantGarden(Properties properties) {
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

        // must be axis-aligned with clicked block (not diagonal)
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

        // gold: "random.explode" 1.0F, 1.5F
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
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState farmland = Blocks.FARMLAND.defaultBlockState();
        BlockState water = Blocks.WATER.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState cactus = Blocks.CACTUS.defaultBlockState();

        // SET12: real OreSpawn crops + vanilla garden staples (gold InstantGarden rows)
        BlockState radish = ModBlocks.RADISH_PLANT.get().defaultBlockState();
        BlockState lettuce = ModBlocks.LETTUCE_PLANT.get().defaultBlockState();
        BlockState carrots = Blocks.CARROTS.defaultBlockState();
        BlockState potatoes = Blocks.POTATOES.defaultBlockState();
        BlockState wheat = Blocks.WHEAT.defaultBlockState();
        BlockState tomato = ModBlocks.TOMATO_PLANT.get().defaultBlockState();
        BlockState corn = ModBlocks.CORN_PLANT.get().defaultBlockState();
        BlockState strawberry = ModBlocks.STRAWBERRY_PLANT.get().defaultBlockState();
        BlockState pumpkinStem = Blocks.PUMPKIN_STEM.defaultBlockState();

        // pass 1: clear volume + grass underfloor
        for (int i = 0; i < HEIGHT; i++) {
            for (int k = 0; k < LENGTH; k++) {
                for (int j = -WIDTH; j <= WIDTH; j++) {
                    int bx = x + k * deltax + j * deltaz;
                    int by = y + i;
                    int bz = z + k * deltaz + j * deltax;
                    level.setBlock(new BlockPos(bx, by, bz), air, 2);
                    if (i == 0) {
                        level.setBlock(new BlockPos(bx, by - 1, bz), grass, 2);
                    }
                }
            }
        }

        // pass 2: plant rows for k = 1 . length-2; j sweeps -width.width, var28++ each j
        for (int k = 1; k < LENGTH - 1; k++) {
            int var28 = 0;
            for (int j = -WIDTH; j <= WIDTH; j++) {
                int bx = x + k * deltax + j * deltaz;
                int bz = z + k * deltaz + j * deltax;
                BlockPos floor = new BlockPos(bx, y - 1, bz);
                BlockPos crop = new BlockPos(bx, y, bz);
                BlockPos belowFloor = new BlockPos(bx, y - 2, bz);

                if (var28 == 1) {
                    // gold: farmland + MyRadishPlant
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, radish, 2);
                } else if (var28 == 2) {
                    // gold: farmland + MyLettucePlant1
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, lettuce, 2);
                } else if (var28 == 3) {
                    // gold: farmland + carrots
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, carrots, 2);
                } else if (var28 == 4) {
                    // gold: water trench + cobble under
                    level.setBlock(floor, water, 2);
                    level.setBlock(belowFloor, cobble, 2);
                } else if (var28 == 5) {
                    // gold: farmland + potatoes
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, potatoes, 2);
                } else if (var28 == 6) {
                    // gold: farmland + wheat
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, wheat, 2);
                } else if (var28 == 7) {
                    // gold: farmland + MyTomatoPlant1
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, tomato, 2);
                } else if (var28 == 8) {
                    // gold: water trench + cobble under
                    level.setBlock(floor, water, 2);
                    level.setBlock(belowFloor, cobble, 2);
                } else if (var28 == 9) {
                    // gold: farmland + MyCornPlant1
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, corn, 2);
                } else if (var28 == 10) {
                    // gold: farmland + MyStrawberryPlant
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, strawberry, 2);
                } else if (var28 == 11) {
                    // gold: cobble under + sand + cactus
                    level.setBlock(belowFloor, cobble, 2);
                    level.setBlock(floor, sand, 2);
                    level.setBlock(crop, cactus, 2);
                } else if (var28 == 12) {
                    // gold: water trench + cobble under
                    level.setBlock(floor, water, 2);
                    level.setBlock(belowFloor, cobble, 2);
                } else if (var28 == 13) {
                    // gold: farmland + pumpkin stem
                    level.setBlock(floor, farmland, 2);
                    level.setBlock(crop, pumpkinStem, 2);
                }

                var28++;
            }
        }

        ItemStack stack = context.getItemInHand();
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
