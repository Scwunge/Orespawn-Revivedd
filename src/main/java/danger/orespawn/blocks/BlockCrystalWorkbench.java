package danger.orespawn.blocks;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Gold {@code CrystalWorkbench} (extends BlockWorkbench) — registry {@code crystalworkbench}.
 * <p>
 * Stats: hardness {@code 1.0F}, resistance {@code 5.0F}; non-opaque like gold.
 * Gold opened custom GUI id {@code 1} ({@code ContainerCrystalWorkbench}).
 * <p>
 * Port: opens vanilla 3×3 crafting via {@link CraftingMenu} (same as {@code CraftingTableBlock}).
 * {@code stillValid} is overridden so the menu stays open on this block (vanilla checks
 * {@code Blocks.CRAFTING_TABLE} only).
 * <p>
 * Register in {@code ModBlocks}:
 * <pre>{@code
 * public static final DeferredBlock<Block> CRYSTAL_WORKBENCH = register(
 *     "crystal_workbench",
 *     () -> new BlockCrystalWorkbench(BlockCrystalWorkbench.defaultProps()));
 * }</pre>
 */
public class BlockCrystalWorkbench extends Block {
    public static final MapCodec<BlockCrystalWorkbench> CODEC = simpleCodec(BlockCrystalWorkbench::new);

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public BlockCrystalWorkbench(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Gold {@code new CrystalWorkbench(., 1.0F, 5.0F)} + non-opaque. */
    public static BlockBehaviour.Properties defaultProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PINK)
                .strength(1.0F, 5.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isViewBlocking((s, g, p) -> false)
                .isSuffocating((s, g, p) -> false);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /**
     * Gold {@code onBlockActivated} → openGui(instance, 1, .).
     * Mirrors {@code CraftingTableBlock#useWithoutItem}.
     */
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(state.getMenuProvider(level, pos));
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        // Gold: ContainerCrystalWorkbench stillValid checked CrystalWorkbenchBlock.
        // Vanilla CraftingMenu hard-codes Blocks.CRAFTING_TABLE — subclass stillValid.
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new CraftingMenu(
                        containerId, inventory, ContainerLevelAccess.create(level, pos)) {
                    @Override
                    public boolean stillValid(Player p) {
                        return stillValid(ContainerLevelAccess.create(level, pos), p, BlockCrystalWorkbench.this);
                    }
                },
                CONTAINER_TITLE);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
