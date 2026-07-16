package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
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
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code EasterBunny} (EntityAnimal). Size 0.5×0.75, speed 0.45, health 10, attack 8, XP 5.
 * Passive avoider; lays random OreSpawn eggs 1/600. Registry size set in {@code ModEntities}.
 */
public class EasterBunny extends Animal {
    private final float moveSpeed = 0.45F;

    public EasterBunny(EntityType<? extends EasterBunny> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.45)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate, Avoid Mob, Avoid Player, Panic, Watch Living, WanderALot(16), LookIdle
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
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
        // gold: orespawn:duck_hurt
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:duck_hurt
        return SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: leather × (2 + 0.2) = 2–4; getDropItem = leather
        int count = this.random.nextInt(3) + 2;
        for (int i = 0; i < count; i++) {
            this.spawnAtLocation(new ItemStack(Items.LEATHER));
        }
    }

    @Override
    protected void customServerAiStep() {
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
        // gold: LayAnEgg(1 + nextInt(3)) every 1/600
        if (this.random.nextInt(600) == 1) {
            this.layAnEgg(1 + this.random.nextInt(3));
        }
    }

    /**
     * Gold LayAnEgg: nextInt(115) switch over nearly all OreSpawn eggs.
     * Port maps registered eggs only; unmapped rolls drop nothing (gold default null).
     */
    @Nullable
    private ItemStack layAnEgg(int count) {
        int i = this.random.nextInt(115);
        Item index = switch (i) {
            case 6 -> ModItems.REDCOW_EGG.get();
            case 9 -> ModItems.MOTHRA_EGG.get();
            case 10 -> ModItems.ALOSAURUS_EGG.get();
            case 11 -> ModItems.CRYOLOPHOSAURUS_EGG.get();
            case 12 -> ModItems.CAMARASAURUS_EGG.get();
            case 13 -> ModItems.VELOCITYRAPTOR_EGG.get();
            case 16 -> ModItems.DRAGONFLY_EGG.get();
            case 19 -> ModItems.CAVEFISHER_EGG.get();
            case 20 -> ModItems.SPYRO_EGG.get();
            case 21 -> ModItems.BARYONYX_EGG.get();
            case 22 -> ModItems.GAMMAMETROID_EGG.get();
            case 24 -> ModItems.KYUUBI_EGG.get();
            case 25 -> ModItems.ALIEN_EGG.get();
            case 35 -> ModItems.STINKBUG_EGG.get();
            case 38 -> ModItems.CHIPMUNK_EGG.get();
            case 41 -> null; // CliffRacer egg deferred
            case 46 -> ModItems.SMALLWORM_EGG.get();
            case 47 -> ModItems.MEDIUMWORM_EGG.get();
            case 48 -> ModItems.LARGEWORM_EGG.get();
            case 49 -> ModItems.CASSOWARY_EGG.get();
            case 56 -> ModItems.BEAVER_EGG.get();
            case 60 -> ModItems.FAIRY_EGG.get();
            case 62 -> ModItems.RAT_EGG.get();
            case 74 -> ModItems.ANT_EGG.get();
            case 75 -> ModItems.RED_ANT_EGG.get();
            case 76 -> ModItems.RAINBOW_ANT_EGG.get();
            case 77 -> ModItems.UNSTABLE_ANT_EGG.get();
            case 78 -> ModItems.TERMITE_EGG.get();
            case 79 -> ModItems.BUTTERFLY_EGG.get();
            case 80 -> ModItems.MOTH_EGG.get();
            case 81 -> ModItems.MOSQUITO_EGG.get();
            case 82 -> ModItems.FIREFLY_EGG.get();
            case 83 -> ModItems.TREX_EGG.get();
            case 85 -> ModItems.MANTIS_EGG.get();
            case 103 -> ModItems.BRUTALFLY_EGG.get();
            case 104 -> ModItems.NASTYSAURUS_EGG.get();
            case 105 -> ModItems.POINTYSAURUS_EGG.get();
            case 106 -> ModItems.CRICKET_EGG.get();
            default -> null;
        };
        if (index == null) {
            return null;
        }
        ItemStack is = new ItemStack(index, count);
        ItemEntity drop = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(2) - this.random.nextInt(2),
                this.getY() + 1.0,
                this.getZ() + this.random.nextInt(2) - this.random.nextInt(2),
                is);
        this.level().addFreshEntity(drop);
        return is;
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50, daytime, no other bunny within 32×8×32. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        List<EasterBunny> nearby = this.level()
                .getEntitiesOfClass(EasterBunny.class, this.getBoundingBox().inflate(32.0, 8.0, 32.0));
        for (EasterBunny other : nearby) {
            if (other != this) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: babies setNoDespawnRequired + false; else !isNoDespawnRequired
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat: apple; isBreedingItem: CrystalApple → apple stand-in
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return (AgeableMob) this.getType().create(level);
    }
}
