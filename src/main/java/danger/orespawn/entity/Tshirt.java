package danger.orespawn.entity;

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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Tshirt} (EntityAnimal). Size 4.0×4.0, speed 0, health 1, attack 0, XP 40.
 * Stationary decorative entity; gold drop is gold ingot; day spawn y≥50, density-limited.
 * <p>
 * Registry: {@code tshirt}.
 */
public class Tshirt extends Animal {
    public Tshirt(EntityType<? extends Tshirt> type, Level level) {
        super(type, level);
        this.xpReward = 40; // gold field_70728_aV
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
        // gold: no AI goals
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
     * Gold {@code getCanSpawnHere}: daytime, y≥50, no other Tshirt in 20×8×20.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        List<Tshirt> nearby = level.getEntitiesOfClass(
                Tshirt.class, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
        for (Tshirt t : nearby) {
            if (t != this) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: gold_ingot (Items.field_151166_bC)
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT));
    }
}
