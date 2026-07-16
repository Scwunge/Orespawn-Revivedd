package danger.orespawn.entity;

import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code StinkBug} (EntityAnimal). Size 0.55×0.55, speed 0.15, health 5, attack 0, XP 2.
 * Fatal hit / death: nausea “fart cloud” (8×5×8) + random fart1.9 sound.
 * Gold has no non-fatal attack fart (passive; attack damage 0).
 */
public class StinkBug extends Animal {
    private final float moveSpeed = 0.15F;

    public StinkBug(EntityType<? extends StinkBug> type, Level level) {
        super(type, level);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 4.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        // gold EntityAIMoveIndoors skipped: 1.21 MoveIndoorsGoal is villager-POI only
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying() && this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
    }

    public int mygetMaxHealth() {
        return 5;
    }

    /**
     * Gold fatal-hit fart cloud: potion ID 9 (nausea/CONFUSION) 300 ticks on living in expand(8,5,8).
     * Gold only fires when health ≤ 0 / dead after the hit (not on non-fatal “attack”).
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isDeadOrDying()) {
            return false;
        }
        boolean ret = super.hurt(source, amount);
        if (this.getHealth() <= 0.0F || this.isDeadOrDying()) {
            this.releaseFartCloud();
        }
        return ret;
    }

    /** Nausea AoE + greenish cloud particles (visual for gold potion “stink” radius). */
    private void releaseFartCloud() {
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0, 5.0, 8.0));
        for (LivingEntity living : list) {
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
        }
        // Visual cloud only (gold is potion AoE with no particles); smoke stands in for stink plume
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    this.getX(),
                    this.getY() + 0.25,
                    this.getZ(),
                    16,
                    0.6,
                    0.35,
                    0.6,
                    0.01);
            server.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 0.2,
                    this.getZ(),
                    20,
                    0.5,
                    0.25,
                    0.5,
                    0.02);
        }
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
        //  gold: no death-sound event; fart plays only via die()
        return null;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        // gold: random fart1.9 on death (onDeath / func_70645_a)
        SoundEvent fart = switch (this.random.nextInt(9) + 1) {
            case 1 -> SoundsHandler.ENTITY_STINKBUG_FART1.get();
            case 2 -> SoundsHandler.ENTITY_STINKBUG_FART2.get();
            case 3 -> SoundsHandler.ENTITY_STINKBUG_FART3.get();
            case 4 -> SoundsHandler.ENTITY_STINKBUG_FART4.get();
            case 5 -> SoundsHandler.ENTITY_STINKBUG_FART5.get();
            case 6 -> SoundsHandler.ENTITY_STINKBUG_FART6.get();
            case 7 -> SoundsHandler.ENTITY_STINKBUG_FART7.get();
            case 8 -> SoundsHandler.ENTITY_STINKBUG_FART8.get();
            default -> SoundsHandler.ENTITY_STINKBUG_FART9.get();
        };
        this.playSound(fart, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return !(this.getY() < 50.0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: babies false; else !isNoDespawnRequired
        if (this.isBaby()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat: wheat seeds (field_151115_aP)
        return stack.is(Items.WHEAT_SEEDS);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return ModEntitiesHolder.createStinkBug(level);
    }

    /** Avoid circular import in constructor; use factory from ModEntities. */
    private static final class ModEntitiesHolder {
        static StinkBug createStinkBug(ServerLevel level) {
            return danger.orespawn.init.ModEntities.STINKBUG.get().create(level);
        }
    }
}
