package danger.orespawn.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code EnchantedCow} extends {@code RedCow}. Size cow default 0.9×1.4.
 * Extra drops: apple ×(nextInt(4)+nextInt(1+looting)), 2 golden apples, 1 enchanted golden apple
 * (gold ItemStack meta 1 on golden_apple), then RedCow super. Never despawns (via RedCow).
 * Render: gold_cow texture + enchant glint pass (RenderEnchantedCow.shouldRenderPass).
 */
public class EnchantedCow extends RedCow {
    public EnchantedCow(EntityType<? extends EnchantedCow> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return RedCow.createAttributes();
    }

    /** Gold {@code dropEnchantedGoldenApple}: EntityItem at y+1 with golden_apple damage=1. */
    private void dropEnchantedGoldenApple() {
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX(),
                this.getY() + 1.0,
                this.getZ(),
                new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
        this.level().addFreshEntity(item);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // gold: nextInt(4) + nextInt(1 + looting) apples, 2 golden apples, enchanted apple, super
        int looting = 0;
        if (damageSource.getEntity() instanceof Player player) {
            looting = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.LOOTING),
                    player.getMainHandItem());
        }
        int n = this.random.nextInt(4) + this.random.nextInt(1 + looting);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.APPLE));
        }
        this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE, 2));
        this.dropEnchantedGoldenApple();
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }

    @Nullable
    @Override
    public EnchantedCow getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        // gold spawnBabyAnimal → new EnchantedCow; no shared ModEntities in exclusive scope
        Entity created = this.getType().create(level);
        return created instanceof EnchantedCow cow ? cow : null;
    }
}
