package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.item.PortalGunData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
   public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "orespawn");
   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register(
      "portal_gun", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.orespawn.portal_gun")).icon(() -> {
            ItemStack gun = new ItemStack((ItemLike)ModItems.PORTAL_GUN.get());
            PortalGunData.makeGlobal(gun, "Chell");
            return gun;
         }).displayItems((params, out) -> {
            out.accept((ItemLike)ModItems.ENDER_PEARL_DUST.get());
            out.accept((ItemLike)ModItems.ENDER_STAR.get());

            for (String channel : new String[]{"Chell", "Atlas", "P-body"}) {
               ItemStack gun = new ItemStack((ItemLike)ModItems.PORTAL_GUN.get());
               PortalGunData.makeGlobal(gun, channel);
               out.accept(gun);
            }

            for (String channel : new String[]{"Creative Inventory", "Creative Inventory Type #2", "Creative Inventory Type #3"}) {
               ItemStack gun = new ItemStack((ItemLike)ModItems.PORTAL_GUN.get());
               PortalGunData.ensureDefaults(gun);
               PortalGunData.setChannel(gun, channel);
               PortalGunData.setGrabStrength(gun, 4);
               out.accept(gun);
            }
         }).build()
   );

   private ModCreativeTabs() {
   }
}

