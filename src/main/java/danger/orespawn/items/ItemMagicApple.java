package danger.orespawn.items;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.items.tools.ToolEnchantHelper;
import danger.orespawn.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.core.Direction;

/**
 * Gold {@code ItemMagicApple} — stack 1, Silk Touch glint; on grass/dirt/farmland
 * grows a large square hollow tree (gold MakeBigSquareTree simplified, finite radius).
 * <p>
 * Full gold tree generator is ~1k lines with circular/round variants and boss tops.
 * Port: bounded hollow log tower + apple-leaf canopy + optional loft chests.
 * Texture: {@code textures/item/magicapple.png}. Registry: {@code magic_apple}.
 */
public class ItemMagicApple extends Item {
    public static final int MAX_STACK = 1;
    /** Gold default tree_radius = 6. */
    public static final int TREE_RADIUS = 6;

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chests/magic_apple_tree"));

    public ItemMagicApple(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        ToolEnchantHelper.applyIfUnenchanted(stack, level, Enchantments.SILK_TOUCH, 2);
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level level,
            net.minecraft.world.entity.Entity entity,
            int slotId,
            boolean isSelected) {
        ToolEnchantHelper.applyIfUnenchanted(stack, level, Enchantments.SILK_TOUCH, 2);
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clicked = context.getClickedPos();
        BlockState base = level.getBlockState(clicked);

        if (!base.is(Blocks.GRASS_BLOCK) && !base.is(Blocks.DIRT) && !base.is(Blocks.FARMLAND) && !base.is(Blocks.COARSE_DIRT)) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) {
            level.playSound(
                    player,
                    clicked,
                    SoundEvents.GENERIC_EXPLODE.value(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.2F);
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }

        // Gold places gold block under tree origin
        server.setBlock(clicked, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        growBigSquareTree(server, server.getRandom(), clicked.getX(), clicked.getY() + 1, clicked.getZ());

        ItemStack stack = context.getItemInHand();
        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    /**
     * Bounded hollow square tower (radius {@link #TREE_RADIUS}), oak log walls,
     * apple-leaf / oak-leaf canopy bands, 1–3 loft chests.
     */
    public static void growBigSquareTree(ServerLevel level, RandomSource random, int x, int y, int z) {
        BlockState leaf = ModBlocks.APPLE_LEAVES.get().defaultBlockState();
        BlockState oakLeaf = Blocks.OAK_LEAVES.defaultBlockState().setValue(
                net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true);
        growMaterialSquareTree(
                level,
                random,
                x,
                y,
                z,
                Blocks.OAK_LOG.defaultBlockState(),
                leaf,
                oakLeaf);
    }

    /**
     * Same layout as {@link #growBigSquareTree} with custom trunk / foliage / crown
     * (used for Tree of Goodness: gold+emerald+diamond or lapis+ruby+amethyst).
     */
    public static void growMaterialSquareTree(
            ServerLevel level,
            RandomSource random,
            int x,
            int y,
            int z,
            BlockState trunk,
            BlockState foliageA,
            BlockState foliageB) {
        int r = TREE_RADIUS;
        int height = r * 4 + random.nextInt(r * 2);
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        if (y < minY + 2 || y + height + 4 >= maxY) {
            height = Math.max(8, maxY - y - 5);
        }

        BlockState step = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        // Goodness trees use diamond/valuable stairs when crown is diamond
        if (foliageB.is(Blocks.DIAMOND_BLOCK) || foliageB.is(ModBlocks.AMETHYST_BLOCK.get())) {
            step = Blocks.DIAMOND_BLOCK.defaultBlockState();
        }

        for (int j = 0; j < height; j++) {
            int yy = y + j;
            for (int i = -r; i <= r; i++) {
                placeIfAir(level, x + i, yy, z - r, trunk);
                placeIfAir(level, x + i, yy, z + r, trunk);
                placeIfAir(level, x - r, yy, z + i, trunk);
                placeIfAir(level, x + r, yy, z + i, trunk);
            }
            int spiral = ((j * 3) % (r * 2 + 1)) - r;
            placeIfAir(level, x + spiral, yy, z - r - 1, step);
            placeIfAir(level, x - spiral, yy, z + r + 1, step);
            placeIfAir(level, x - r - 1, yy, z + spiral, step);
            placeIfAir(level, x + r + 1, yy, z - spiral, step);

            if (j > r && j % 3 == 0) {
                int br = 2 + random.nextInt(3);
                for (int d = 1; d <= br; d++) {
                    BlockState foliage = random.nextBoolean() ? foliageA : foliageB;
                    placeIfAir(level, x + r + d, yy, z, foliage);
                    placeIfAir(level, x - r - d, yy, z, foliage);
                    placeIfAir(level, x, yy, z + r + d, foliage);
                    placeIfAir(level, x, yy, z - r - d, foliage);
                }
            }

            if (j > 0 && j % 6 == 0 && j < height - 2) {
                for (int i = -r + 1; i <= r - 1; i++) {
                    for (int k = -r + 1; k <= r - 1; k++) {
                        placeIfAir(level, x + i, yy, z + k, trunk);
                    }
                }
                if (random.nextBoolean()) {
                    BlockPos chestPos = new BlockPos(x, yy + 1, z);
                    if (level.getBlockState(chestPos).isAir()) {
                        level.setBlock(
                                chestPos,
                                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH),
                                3);
                        RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, LOOT_TABLE);
                    }
                }
            }
        }

        int top = y + height;
        for (int i = -r - 2; i <= r + 2; i++) {
            for (int k = -r - 2; k <= r + 2; k++) {
                if (i * i + k * k <= (r + 2) * (r + 2)) {
                    BlockState foliage = random.nextInt(3) == 0 ? foliageA : foliageB;
                    placeIfAir(level, x + i, top, z + k, foliage);
                    if (random.nextInt(4) == 0) {
                        placeIfAir(level, x + i, top + 1, z + k, foliage);
                    }
                }
            }
        }
        placeIfAir(level, x, top + 2, z, foliageB);
        placeIfAir(level, x, top + 3, z, foliageB);
    }

    private static void placeIfAir(ServerLevel level, int x, int y, int z, BlockState state) {
        BlockPos pos = new BlockPos(x, y, z);
        if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
            return;
        }
        BlockState cur = level.getBlockState(pos);
        if (cur.isAir()
                || cur.canBeReplaced()
                || cur.is(Blocks.SHORT_GRASS)
                || cur.is(Blocks.TALL_GRASS)
                || cur.is(Blocks.SNOW)
                || cur.is(Blocks.VINE)) {
            level.setBlock(pos, state, 2);
        }
    }
}
