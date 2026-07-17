package me.scwunge.mods.portalgun.init;

import me.scwunge.mods.portalgun.entity.PortalCameraEntity;
import me.scwunge.mods.portalgun.entity.PortalProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, "orespawn");
   public static final DeferredHolder<EntityType<?>, EntityType<PortalProjectile>> PORTAL_PROJECTILE = ENTITY_TYPES.register(
      "portal_projectile",
      () -> Builder.<PortalProjectile>of(PortalProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(10)
            .updateInterval(1)
            .build("portal_projectile")
   );
   public static final DeferredHolder<EntityType<?>, EntityType<PortalCameraEntity>> PORTAL_CAMERA = ENTITY_TYPES.register(
      "portal_camera",
      () -> Builder.<PortalCameraEntity>of(PortalCameraEntity::new, MobCategory.MISC)
            .sized(0.1F, 0.1F)
            .eyeHeight(0.0F)
            .clientTrackingRange(0)
            .updateInterval(Integer.MAX_VALUE)
            .noSave()
            .noSummon()
            .fireImmune()
            .build("portal_camera")
   );

   private ModEntities() {
   }
}
