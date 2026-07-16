package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Peacock} (EntityAnimal). Size 0.65×1.2, speed 0.38, health 15, attack attr 4
 * (hit deals 6.0), XP 8, follow range 100. Avoider + termite hunter; blink anim for crest/tail.
 * Registry size set in {@code ModEntities.PEACOCK} .
 */
public class Peacock extends Animal {
    private final float moveSpeed = 0.38F;
    private int myBlink;
    private int blinkcount;
    private int blinker;

    public Peacock(EntityType<? extends Peacock> type, Level level) {
        super(type, level);
        this.xpReward = 8;
        this.myBlink = 20 + this.random.nextInt(50);
        this.blinkcount = 0;
        this.blinker = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    @Override
    protected void registerGoals() {
        // gold: EntityAISwimming @0
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMate @1
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        // gold: EntityAIAvoidEntity(EntityMob, 8, 1.0, 1.4)
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.4F));
        // gold: EntityAIAvoidEntity(EntityPlayer, 12, 1.2, 1.6)
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 12.0F, 1.2, 1.6F));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        // gold MyEntityAIWander
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        // gold: EntityAINearestAttackableTarget(Termite) when PlayNicely==0
        if (OreSpawnMain.PlayNicely == 0) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Termite.class, true));
        }
    }

    /** Gold {@code getBlink} — crest/tail display state (0 closed, 1 open). */
    public int getBlink() {
        return this.blinker;
    }

    @Override
    public void tick() {
        // gold: re-assert moveSpeed every tick
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        // gold blink cycle (runs both sides like gold livingUpdate)
        this.blinkcount++;
        if (this.blinkcount > this.myBlink) {
            this.blinkcount = 0;
            if (this.blinker > 0) {
                this.blinker = 0;
                this.myBlink = 50 + this.level().random.nextInt(300);
            } else {
                this.blinker = 1;
                this.myBlink = 25 + this.level().random.nextInt(100);
            }
        }
    }

    public int mygetMaxHealth() {
        return 15;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: 1/8 chance "orespawn:peacocklive"; ogg present; SoundsHandler deferred
        if (this.random.nextInt(8) != 1) {
            return null;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //  gold: orespawn:peacockhit — SoundsHandler deferred
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        //  gold: orespawn:peacockdead — SoundsHandler deferred
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: MyRawPeacock ×1 (+1 extra 1/3); MyPeacockFeather 1/2
        // MyRawPeacock / MyPeacockFeather items not registered — stand-in chicken + feather
        this.spawnAtLocation(new ItemStack(Items.CHICKEN));
        if (this.random.nextInt(3) == 1) {
            this.spawnAtLocation(new ItemStack(Items.CHICKEN));
        }
        if (this.random.nextInt(2) == 1) {
            this.spawnAtLocation(new ItemStack(Items.FEATHER));
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // gold: fixed 6.0 damage (attr is 4.0)
        return target.hurt(this.damageSources().mobAttack(this), 6.0F);
    }

    @Override
    protected void customServerAiStep() {
        // gold: clear revenge 1/200
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        super.customServerAiStep();
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        // gold: 1/10 seek termite
        if (this.random.nextInt(10) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                // gold: getDistanceSq < 4.0 → attack; else path at 1.2
                if (this.distanceToSqr(e) < 4.0) {
                    this.doHurtTarget(e);
                } else {
                    this.getNavigation().moveTo(e, 1.2);
                }
            }
        }
        // gold: LayAnEgg(PeacockEgg, 1.3) 1/5000 — PeacockEgg item deferred
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        return target instanceof Termite;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        // gold: expand(10, 2, 10) + GenericTargetSorter
        List<LivingEntity> list = this.level().getEntitiesOfClass(
                LivingEntity.class, this.getBoundingBox().inflate(10.0, 2.0, 10.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: clear air volume j,k ∈ [-1,0], i ∈ [1,2] above feet;
     * daytime (dayTime%24000 ≤ 12000); y in [50, 100]; buddies ≤ 2.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        BlockPos base = this.blockPosition();
        // gold: for k=-1.0, j=-1.0, i=1.2 — block must be air
        for (int k = -1; k < 1; k++) {
            for (int j = -1; j < 1; j++) {
                for (int i = 1; i < 3; i++) {
                    if (!level.getBlockState(base.offset(j, i, k)).isAir()) {
                        return false;
                    }
                }
            }
        }
        if (level instanceof Level lvl) {
            long t = lvl.getDayTime() % 24000L;
            if (t > 12000L) {
                return false;
            }
        }
        if (this.getY() < 50.0 || this.getY() > 100.0) {
            return false;
        }
        return this.findBuddies() <= 2;
    }

    private int findBuddies() {
        List<Peacock> list =
                this.level().getEntitiesOfClass(Peacock.class, this.getBoundingBox().inflate(16.0, 10.0, 16.0));
        return list.size();
    }

    /**
     * Gold {@code canDespawn}: babies setNoDespawnRequired + false; else !isNoDespawnRequired.
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        return !this.isPersistenceRequired();
    }

    /**
     * Gold {@code isBreedingItem}: CrystalApple (not registered).
     * Stand-in: apple (gold {@code isWheat} was also apple).
     */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        // gold spawnBabyAnimal → new Peacock; no ModEntities ref in exclusive scope
        return (AgeableMob) this.getType().create(level);
    }
}
