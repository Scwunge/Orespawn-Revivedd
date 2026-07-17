package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.block.entity.PortalMasterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "orespawn");
   public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PortalMasterBlockEntity>> PORTAL_MASTER = BLOCK_ENTITY_TYPES.register(
      "portal_master", () -> Builder.of(PortalMasterBlockEntity::new, new Block[]{(Block)ModBlocks.PORTAL.get()}).build(null)
   );

   private ModBlockEntities() {
   }
}
