package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.block.PortalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public final class ModBlocks {
   public static final Blocks BLOCKS = DeferredRegister.createBlocks("orespawn");
   public static final Items BLOCK_ITEMS = DeferredRegister.createItems("orespawn");
   public static final DeferredBlock<PortalBlock> PORTAL = BLOCKS.register(
      "portal",
      () -> new PortalBlock(
            Properties.of()
               .mapColor(MapColor.COLOR_LIGHT_BLUE)
               .noOcclusion()
               .noCollission()
               .strength(-1.0F, 3600000.0F)
               .sound(SoundType.GLASS)
               .lightLevel(s -> 12)
               .pushReaction(PushReaction.BLOCK)
               .isValidSpawn((s, g, p, t) -> false)
               .isRedstoneConductor((s, g, p) -> false)
               .isSuffocating((s, g, p) -> false)
               .isViewBlocking((s, g, p) -> false)
         )
   );
   public static final DeferredItem<BlockItem> PORTAL_ITEM = BLOCK_ITEMS.register(
      "portal", () -> new BlockItem((Block)PORTAL.get(), new net.minecraft.world.item.Item.Properties())
   );

   private ModBlocks() {
   }
}
