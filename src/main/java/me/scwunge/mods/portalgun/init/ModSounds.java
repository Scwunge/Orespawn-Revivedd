package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.PortalGunMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Sound events under the OreSpawn namespace ({@code orespawn:...}).
 * Paths match {@code assets/orespawn/sounds.json}.
 */
public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, PortalGunMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_OPEN_BLUE = reg("portal.portal_open_blue");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_OPEN_RED = reg("portal.portal_open_red");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_ENTER = reg("portal.portal_enter");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_EXIT = reg("portal.portal_exit");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_FIZZLE = reg("portal.portal_fizzle");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_INVALID = reg("portal.portal_invalid_surface");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_INVALID_SWT =
            reg("portal.portal_invalid_surface_swt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIZZLER_SHIMMY = reg("portal.wpn_portal_fizzler_shimmy");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_AMBIENT = reg("portal.wpn_portal_ambient_lp");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_FIRE_BLUE = reg("portal.wpn_portal_gun_fire_blue");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_FIRE_RED = reg("portal.wpn_portal_gun_fire_red");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_EQUIP = reg("portal.wpn_portalgun_activation");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRAB_START = reg("portal.object_use_lp_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRAB_LOOP = reg("portal.object_use_lp_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRAB_STOP = reg("portal.object_use_stop");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRAB_FAIL = reg("portal.object_use_failure");

    private static DeferredHolder<SoundEvent, SoundEvent> reg(String path) {
        return SOUNDS.register(
                path,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(PortalGunMod.MOD_ID, path)));
    }

    private ModSounds() {}
}
