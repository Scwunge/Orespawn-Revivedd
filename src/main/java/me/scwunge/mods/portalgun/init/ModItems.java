package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.item.PortalGunItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

public final class ModItems {
   public static final Items ITEMS = DeferredRegister.createItems("orespawn");
   public static final DeferredItem<Item> ENDER_PEARL_DUST = ITEMS.registerSimpleItem("ender_pearl_dust", new Properties());
   public static final DeferredItem<Item> ENDER_STAR = ITEMS.registerSimpleItem("ender_star", new Properties().stacksTo(16));
   public static final DeferredItem<PortalGunItem> PORTAL_GUN = ITEMS.register("portal_gun", () -> new PortalGunItem(new Properties().stacksTo(1)));

   private ModItems() {
   }
}
