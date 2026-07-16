package danger.orespawn.entity;

import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Cassowary} (EntityAnimal). Size 0.5×1.2, speed 0.25, health 10, attack 8, XP 5.
 * Passive avoider; drops feathers. Registry size set in {@code ModEntities.CASSOWARY}.
 */
public class Cassowary extends Animal {
    private final float moveSpeed = 0.25F;

    public Cassowary(EntityType<? extends Cassowary> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAISwimming @0 and @1 (duplicate) → single FloatGoal
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIAvoidEntity(EntityMob, 8, 1.0, 1.4)
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.4F));
        // gold: EntityAIAvoidEntity(EntityPlayer, 8, 1.0, 1.4)
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 12.0F));
        // gold MyEntityAIWander — WaterAvoidingRandomStrollGoal is closest vanilla
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        // gold: re-assert moveSpeed every tick
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: SoundsHandler.ENTITY_DUCK_HURT
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold onDeath plays ENTITY_DUCK_HURT (no separate death event)
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold dropFewItems: nextInt(3)+2 feathers (2–4); getDropItem = feather
        int count = this.random.nextInt(3) + 2;
        for (int i = 0; i < count; i++) {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    @Override
    protected void customServerAiStep() {
        // gold: clear revenge target 1/200 ticks
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
    }

    /** Gold {@code getCanSpawnHere}: world.isDaytime(). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl) {
            return lvl.isDay();
        }
        return true;
    }

    /**
     * Gold {@code canDespawn}: babies never; else !isNoDespawnRequired.
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isBaby()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    /** Gold {@code isWheat}: apple. (isBreedingItem gold always false; offspring null.) */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        //  gold createChild returns null
        return null;
    }
}
