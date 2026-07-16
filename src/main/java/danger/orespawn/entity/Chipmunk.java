package danger.orespawn.entity;

import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Chipmunk} (EntityCannonFodder / tameable). Simple passive port:
 * size 0.35×0.35, speed 0.38, health 5, attack 1, XP 5. Panic/wander/avoid;
 * drops wheat. Full pet/hat system deferred.
 */
public class Chipmunk extends Animal {
    private final float moveSpeed = 0.38F;

    public Chipmunk(EntityType<? extends Chipmunk> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAISwimming, Mate, FollowOwner (skip), Avoid Mob, Tempt apple,
        // Panic, Avoid Player, WatchClosest, WanderALot, LookIdle, MoveIndoors (skip POI)
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.6F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.APPLE), false));
        this.goalSelector.addGoal(5, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 5.0F));
        this.goalSelector.addGoal(9, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
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
        if (!this.isDeadOrDying()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }
            if (this.random.nextInt(250) == 0) {
                this.heal(1.0F);
            }
            // gold: 1/600 dig dirt/farmland under feet when mobGriefing
            if (this.random.nextInt(600) == 1
                    && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockPos below = BlockPos.containing(this.getX(), this.getY() - 1.0, this.getZ());
                BlockState state = this.level().getBlockState(below);
                if (state.is(Blocks.DIRT) || state.is(Blocks.FARMLAND)) {
                    this.level().setBlock(below, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
        super.customServerAiStep();
    }

    /** Gold fall: damage only after −3 blocks, capped at 2. */
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        if (onGround && this.fallDistance > 0.0F) {
            float i = Mth.ceil(this.fallDistance - 3.0F);
            if (i > 0.0F) {
                if (i > 3.0F) {
                    this.playSound(SoundEvents.GENERIC_BIG_FALL, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);
                }
                if (i > 2.0F) {
                    i = 2.0F;
                }
                this.hurt(this.damageSources().fall(), i);
            }
            this.resetFallDistance();
        } else {
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    public int mygetMaxHealth() {
        return 5;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //  gold: always null (even when not sitting)
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:scorpion_hit
        return SoundsHandler.ENTITY_SCORPION_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:cryo_death
        return SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby()
                ? (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.5F
                : (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: wheat (untamed path); tamed flower drops skipped in simple port
        this.spawnAtLocation(new ItemStack(Items.WHEAT));
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50 and buddies ≤ 2. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        return this.findBuddies() <= 2;
    }

    private int findBuddies() {
        List<Chipmunk> list =
                this.level().getEntitiesOfClass(Chipmunk.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0));
        return list.size();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: babies false; no-despawn required false; tamed false → simple: babies persist
        if (this.isBaby()) {
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat / tempt: apple (field_151034_e)
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return danger.orespawn.init.ModEntities.CHIPMUNK.get().create(level);
    }
}
