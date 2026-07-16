package danger.orespawn.init;

import danger.orespawn.blocks.entity.CrystalFurnaceBlockEntity;
import danger.orespawn.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block-entity registry. Wired from {@link danger.orespawn.OreSpawnMain} mod bus.
 */
public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    /**
     * Gold {@code TileEntityCrystalFurnace} — single block + {@code LIT} (gold had off/on pair).
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystalFurnaceBlockEntity>> CRYSTAL_FURNACE =
            BLOCK_ENTITIES.register(
                    "crystal_furnace",
                    () -> BlockEntityType.Builder.of(
                                    CrystalFurnaceBlockEntity::new, ModBlocks.CRYSTAL_FURNACE.get())
                            .build(null));

    private ModBlockEntities() {}
}
