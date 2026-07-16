package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Coin} (EntityAnimal). Size 1.5×1.5, speed 0, health 1, attack 0, XP 10.
 * Stationary decorative loot entity; day spawn y≥50, density-limited.
 * <p>
 * Registry: {@code coin}.
 */
public class Coin extends Animal {
    public Coin(EntityType<? extends Coin> type, Level level) {
        super(type, level);
        this.xpReward = 10; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    public int mygetMaxHealth() {
        return 1;
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAILookIdle only
        this.goalSelector.addGoal(0, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: !noDespawnRequired
        return !this.isPersistenceRequired();
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    /** Gold {@code interact} always false. */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    /**
     * Gold {@code getCanSpawnHere}: daytime, y≥50, no other Coin in 20×8×20.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        List<Coin> nearby = level.getEntitiesOfClass(
                Coin.class, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
        for (Coin c : nearby) {
            if (c != this) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold dropFewItems: nextInt(10) → diamond / uranium / titanium / gold / emerald tools / coin egg
        int i = this.random.nextInt(10);
        ItemStack drop = switch (i) {
            case 0 -> new ItemStack(Items.DIAMOND);
            case 1 -> new ItemStack(ModItems.URANIUM_NUGGET.get());
            case 2 -> new ItemStack(ModItems.TITANIUM_NUGGET.get());
            case 3 -> new ItemStack(Items.GOLD_INGOT);
            case 4 -> new ItemStack(ModItems.EMERALD_AXE.get());
            case 5 -> new ItemStack(ModItems.EMERALD_SHOVEL.get());
            case 6 -> new ItemStack(ModItems.EMERALD_PICKAXE.get());
            case 7 -> new ItemStack(ModItems.EMERALD_HOE.get());
            case 8 -> {
                // gold CoinEgg — prefer registered item; emerald fallback until egg
                var egg = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("orespawn", "coin_egg"));
                if (egg != null && egg != Items.AIR) {
                    yield new ItemStack(egg);
                }
                egg = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("orespawn", "eggcoin"));
                yield (egg != null && egg != Items.AIR) ? new ItemStack(egg) : new ItemStack(Items.EMERALD);
            }
            default -> new ItemStack(ModItems.EMERALD_SWORD.get()); // gold default MyEmeraldSword
        };
        this.spawnAtLocation(drop);
    }
}
