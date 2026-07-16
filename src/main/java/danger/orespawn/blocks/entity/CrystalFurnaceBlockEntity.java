package danger.orespawn.blocks.entity;

import danger.orespawn.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code TileEntityCrystalFurnace} — 3-slot smelting TE (input / fuel / result).
 * <p>
 * Port strategy (gold fidelity that compiles): extend {@link AbstractFurnaceBlockEntity}
 * with {@link RecipeType#SMELTING} and open vanilla {@link FurnaceMenu}.
 * Gold custom GUI ({@code CrystalFurnaceGUI} / {@code ContainerCrystalFurnace}) not ported;
 * slot layout and smelting behavior match vanilla furnace (= gold TE logic).
 * <p>
 * Requires {@link ModBlockEntities#CRYSTAL_FURNACE} registered on the mod bus
 * ({@code OreSpawnMain} must call {@code ModBlockEntities.BLOCK_ENTITIES.register(modEventBus)}).
 */
public class CrystalFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.orespawn.crystal_furnace");

    public CrystalFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL_FURNACE.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        // Gold: ContainerCrystalFurnace — same 3 slots + player inv as FurnaceMenu
        return new FurnaceMenu(containerId, inventory, this, this.dataAccess);
    }
}
