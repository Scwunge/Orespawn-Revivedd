package me.scwunge.mods.portalgun.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.DataComponents;

public final class ModDataComponents {
   public static final DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "orespawn");
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CHANNEL = COMPONENTS.registerComponentType(
      "channel", b -> b.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PORTAL_WIDTH = COMPONENTS.registerComponentType(
      "portal_width", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PORTAL_HEIGHT = COMPONENTS.registerComponentType(
      "portal_height", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GRAB_STRENGTH = COMPONENTS.registerComponentType(
      "grab_strength", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LAST_ORANGE = COMPONENTS.registerComponentType(
      "last_orange", b -> b.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> OWNER_UUID = COMPONENTS.registerComponentType(
      "owner_uuid", b -> b.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
   );
   public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> OWNER_NAME = COMPONENTS.registerComponentType(
      "owner_name", b -> b.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
   );

   private ModDataComponents() {
   }
}
