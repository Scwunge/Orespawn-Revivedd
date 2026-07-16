package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code RedCow} (extends EntityCow). Size cow default 0.9×1.4.
 * Extra drops: apples (gold field_151034_e) + vanilla cow loot. Never despawns.
 */
public class RedCow extends Cow {
    public RedCow(EntityType<? extends RedCow> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Cow.createAttributes();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold: nextInt(3) + nextInt(1 + looting) apples, then super cow drops
        int looting = 0;
        if (damageSource.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            looting = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess()
                            .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                            .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING),
                    player.getMainHandItem());
        }
        int n = this.random.nextInt(3) + this.random.nextInt(1 + looting);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.APPLE));
        }
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }

    @Nullable
    @Override
    public RedCow getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.REDCOW.get().create(level);
    }

    @Override
    protected void customServerAiStep() {
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
