package danger.orespawn.items;

import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * Gold {@code ItemSifter} — sift water / sand / gravel / dirt / grass for random loot.
 * <p>
 * Gold: stack 1, durability 600, tools tab. onItemUse rolls a sparse switch table;
 * damages by 1 per use. Mod fish / shoes / salt / flowers use available ports or
 * vanilla stand-ins .
 */
public class ItemSifter extends Item {
    /** Gold {@code setMaxDamage(600)}. */
    public static final int SIFTER_USES = 600;

    public ItemSifter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block bid = state.getBlock();

        // gold: water above clicked block forces water table
        BlockState above = level.getBlockState(pos.above());
        if (above.getFluidState().is(Fluids.WATER)
                || above.getFluidState().is(Fluids.FLOWING_WATER)
                || above.is(Blocks.WATER)) {
            bid = Blocks.WATER;
        }
        if (state.getFluidState().is(Fluids.WATER)
                || state.getFluidState().is(Fluids.FLOWING_WATER)
                || bid == Blocks.WATER) {
            bid = Blocks.WATER;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        RandomSource rand = level.random;

        if (bid == Blocks.WATER) {
            siftWater(level, rand, x, y, z);
        } else if (bid == Blocks.SAND || bid == Blocks.RED_SAND) {
            siftSand(level, rand, x, y, z);
        } else if (bid == Blocks.GRAVEL) {
            siftGravel(level, rand, x, y, z);
        } else if (bid == Blocks.DIRT || bid == Blocks.COARSE_DIRT || bid == Blocks.ROOTED_DIRT) {
            siftDirt(level, rand, x, y, z);
        } else if (bid == Blocks.GRASS_BLOCK) {
            siftGrass(level, rand, x, y, z);
        }

        ItemStack stack = context.getItemInHand();
        if (player != null) {
            stack.hurtAndBreak(1, player, net.minecraft.world.entity.LivingEntity.getSlotForHand(context.getHand()));
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Gold water table: nextInt(160); cases 0–40 only (MCP field decode).
     * Mod fish → COD stand-in; shoes skipped (unported); gems / nuggets from ModItems.
     */
    private void siftWater(Level level, RandomSource rand, int x, int y, int z) {
        int i = rand.nextInt(160);
        switch (i) {
            case 0 -> drop(Items.COD, level, x, y, z);
            case 1, 2, 3, 4, 5, 6 -> drop(Items.COD, level, x, y, z); // green/blue/pink/rock/wood/grey fish
            case 7, 14, 35 -> drop(Items.GLASS_BOTTLE, level, x, y, z);
            case 8, 26 -> drop(Items.IRON_INGOT, level, x, y, z);
            case 9, 27 -> drop(Items.GOLD_NUGGET, level, x, y, z);
            // 10–13, 30–33 shoes — unported
            case 15, 36 -> drop(Items.BONE, level, x, y, z);
            case 16, 37 -> drop(Items.STONE, level, x, y, z);
            case 17, 39 -> drop(Items.BUCKET, level, x, y, z);
            case 18, 40 -> drop(Items.WATER_BUCKET, level, x, y, z);
            case 19 -> {
                if (rand.nextInt(3) == 1) {
                    drop(Items.EMERALD, level, x, y, z);
                } else {
                    drop(Items.GRAVEL, level, x, y, z);
                }
            }
            case 20 -> {
                if (rand.nextInt(3) == 1) {
                    drop(ModItems.RUBY.get(), level, x, y, z);
                } else {
                    drop(Items.GRAVEL, level, x, y, z);
                }
            }
            case 21 -> {
                if (rand.nextInt(3) == 1) {
                    drop(ModItems.AMETHYST.get(), level, x, y, z);
                } else {
                    drop(Items.GRAVEL, level, x, y, z);
                }
            }
            case 22 -> drop(ModItems.MOTH_SCALE.get(), level, x, y, z);
            case 23 -> drop(ModItems.URANIUM_NUGGET.get(), level, x, y, z);
            case 24 -> drop(ModItems.TITANIUM_NUGGET.get(), level, x, y, z);
            case 25 -> {
                if (rand.nextInt(2) == 1) {
                    drop(Items.DIAMOND, level, x, y, z);
                } else {
                    drop(Items.GRAVEL, level, x, y, z);
                }
            }
            case 28 -> drop(Items.REDSTONE, level, x, y, z);
            case 29 -> drop(Items.COAL, level, x, y, z);
            case 38 -> drop(Items.STONE_BUTTON, level, x, y, z);
            default -> {
            }
        }
    }

    /** Gold sand table: nextInt(60); cases 0–13. */
    private void siftSand(Level level, RandomSource rand, int x, int y, int z) {
        int i = rand.nextInt(60);
        switch (i) {
            case 0 -> drop(Items.IRON_HORSE_ARMOR, level, x, y, z);
            case 1 -> drop(Items.SHEARS, level, x, y, z);
            case 2 -> drop(Items.CARROT_ON_A_STICK, level, x, y, z);
            case 3 -> drop(Items.POISONOUS_POTATO, level, x, y, z);
            case 4 -> drop(Items.ITEM_FRAME, level, x, y, z);
            case 5 -> drop(Items.BONE, level, x, y, z);
            case 6 -> drop(Items.COMPASS, level, x, y, z);
            case 7 -> drop(Items.GLASS_BOTTLE, level, x, y, z);
            case 8 -> drop(Items.SADDLE, level, x, y, z);
            case 9 -> drop(Items.IRON_HELMET, level, x, y, z);
            case 10 -> drop(Items.IRON_CHESTPLATE, level, x, y, z);
            case 11 -> drop(Items.IRON_LEGGINGS, level, x, y, z);
            case 12 -> drop(Items.IRON_BOOTS, level, x, y, z);
            case 13 -> drop(Items.SAND, level, x, y, z);
            default -> {
            }
        }
    }

    /** Gold gravel table: nextInt(60); cases 0–11. */
    private void siftGravel(Level level, RandomSource rand, int x, int y, int z) {
        int i = rand.nextInt(60);
        switch (i) {
            case 0 -> drop(Items.FLINT, level, x, y, z);
            case 1 -> drop(Items.SUGAR, level, x, y, z); // MySalt stand-in
            case 2 -> drop(Items.FLINT_AND_STEEL, level, x, y, z);
            case 3 -> drop(Items.SPIDER_EYE, level, x, y, z);
            case 4 -> drop(Items.ITEM_FRAME, level, x, y, z);
            case 5 -> drop(Items.FEATHER, level, x, y, z);
            case 6 -> drop(Items.STRING, level, x, y, z);
            case 7 -> drop(Items.GLASS_BOTTLE, level, x, y, z);
            case 8 -> drop(Items.LEAD, level, x, y, z);
            case 9 -> drop(Items.NAME_TAG, level, x, y, z);
            case 10 -> drop(Items.SAND, level, x, y, z);
            case 11 -> drop(Items.GRAVEL, level, x, y, z);
            default -> {
            }
        }
    }

    /** Gold dirt table: nextInt(60); cases 0–13. */
    private void siftDirt(Level level, RandomSource rand, int x, int y, int z) {
        int i = rand.nextInt(60);
        switch (i) {
            case 0 -> drop(Items.STRING, level, x, y, z);
            case 1 -> drop(Items.SUGAR, level, x, y, z); // MySalt stand-in
            case 2 -> drop(Items.SHEARS, level, x, y, z);
            case 3 -> drop(Items.STICK, level, x, y, z);
            case 4 -> drop(Items.BOWL, level, x, y, z);
            case 5 -> drop(Items.FLOWER_POT, level, x, y, z);
            case 6 -> drop(Items.OAK_SIGN, level, x, y, z);
            case 7 -> drop(Items.BRICK, level, x, y, z);
            case 8 -> drop(Items.PAPER, level, x, y, z);
            case 9 -> drop(Items.BONE, level, x, y, z);
            case 10 -> drop(Items.GLASS_BOTTLE, level, x, y, z);
            case 11 -> drop(Items.SAND, level, x, y, z);
            case 12 -> drop(Items.GRAVEL, level, x, y, z);
            case 13 -> drop(Items.DIRT, level, x, y, z);
            default -> {
            }
        }
    }

    /** Gold grass table: nextInt(60); cases 0–14 (case 5 falls through to 6 in gold — wheat). */
    private void siftGrass(Level level, RandomSource rand, int x, int y, int z) {
        int i = rand.nextInt(60);
        switch (i) {
            case 0 -> drop(Items.DANDELION, level, x, y, z);
            case 1 -> drop(Items.POPPY, level, x, y, z);
            // 2–5 gold mod flowers / fallthrough → wheat
            case 2, 3, 4, 5, 6 -> drop(Items.WHEAT, level, x, y, z);
            case 7 -> drop(Items.PUMPKIN_SEEDS, level, x, y, z);
            case 8 -> drop(Items.MELON_SEEDS, level, x, y, z);
            case 9 -> drop(Items.CARROT, level, x, y, z);
            case 10 -> drop(Items.POTATO, level, x, y, z);
            case 11 -> drop(Items.DEAD_BUSH, level, x, y, z);
            case 12 -> drop(Items.GRAVEL, level, x, y, z);
            case 13 -> drop(Items.DIRT, level, x, y, z);
            case 14 -> drop(Items.GRASS_BLOCK, level, x, y, z);
            default -> {
            }
        }
    }

    private void drop(Item item, Level level, int x, int y, int z) {
        drop(new ItemStack(item, 1), level, x, y, z);
    }

    private void drop(ItemStack stack, Level level, int x, int y, int z) {
        if (stack.isEmpty() || level.isClientSide) {
            return;
        }
        RandomSource rand = level.random;
        double dx = x + rand.nextInt(2) - rand.nextInt(2) + 0.5;
        double dy = y + 1.1;
        double dz = z + rand.nextInt(2) - rand.nextInt(2) + 0.5;
        level.addFreshEntity(new ItemEntity(level, dx, dy, dz, stack));
    }
}
