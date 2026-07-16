package danger.orespawn.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import danger.orespawn.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code CrystalCow} extends {@code RedCow}. Size cow default 0.9×1.4.
 * Extra drops: MyCrystalApple ×(nextInt(3)+nextInt(1+looting)), then 1 apple,
 * then RedCow super. Never despawns (via RedCow).
 */
public class CrystalCow extends RedCow {
    public CrystalCow(EntityType<? extends CrystalCow> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return RedCow.createAttributes();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold: nextInt(3) + nextInt(1 + looting) MyCrystalApple, then 1 apple, then super
        int looting = 0;
        if (damageSource.getEntity() instanceof Player player) {
            looting = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.LOOTING),
                    player.getMainHandItem());
        }
        int n = this.random.nextInt(3) + this.random.nextInt(1 + looting);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(ModItems.CRYSTAL_APPLE.get()));
        }
        this.spawnAtLocation(new ItemStack(Items.APPLE));
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }

    @Nullable
    @Override
    public CrystalCow getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        // gold spawnBabyAnimal → new CrystalCow; no shared ModEntities in exclusive scope
        Entity created = this.getType().create(level);
        return created instanceof CrystalCow cow ? cow : null;
    }
}
