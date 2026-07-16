package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Flounder} (EntityAnimal). Size 0.55×0.25, speed 0.25, health 5, attack 0, XP 5.
 * Seeks water when dry; heals in water; dies slowly without water. Breed food: crystal apple (golden apple stand-in).
 * canBreatheUnderwater=true in gold — LivingEntity method is final in 1.21; air supply forced each tick.
 */
public class Flounder extends Animal {
    private final float moveSpeed = 0.25F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Flounder(EntityType<? extends Flounder> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 15.0); // gold field_70174_ab = 15
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate, Avoid Player, Panic, WatchClosest, MyEntityAIWander, LookIdle
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.4F));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(6, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        // gold canBreatheUnderwater=true — restore air each tick
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    public int mygetMaxHealth() {
        return 5;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: "splash" (vanilla)
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: "little_splat"
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:ratdead
        return SoundsHandler.ENTITY_RAT_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 1 + nextInt(2) raw fish
        int count = 1 + this.random.nextInt(2);
        for (int i = 0; i < count; i++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} — find nearest water block shell. */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWater(bid)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x - dx, y + i, z + j));
                if (this.isWater(bid)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x - dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y - dy, z + j));
                if (this.isWater(bid)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y - dy;
                        this.tz = z + j;
                        found++;
                    }
                }
            }
        }

        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z - dz));
                if (this.isWater(bid)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z - dz;
                        found++;
                    }
                }
            }
        }

        return found != 0;
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }

            // gold: when not in water, seek water every 1/20; else slowly take 1 dmg
            if (!this.isInWater() && this.random.nextInt(20) == 0) {
                this.closest = 99999;
                this.tx = this.ty = this.tz = 0;

                for (int i = 1; i < 11; i++) {
                    int j = i;
                    if (j > 4) {
                        j = 4;
                    }
                    if (this.scanIt((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                        break;
                    }
                    if (i >= 5) {
                        i++;
                    }
                }

                if (this.closest < 99999) {
                    this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.0);
                } else {
                    if (this.random.nextInt(25) == 1) {
                        this.hurt(this.damageSources().generic(), 1.0F);
                    }
                    if (this.getHealth() <= 0.0F) {
                        this.discard();
                        return;
                    }
                }
            }

            // gold: in water 1/50 splash + heal 1
            if (this.isInWater() && this.random.nextInt(50) == 0) {
                this.playSound(SoundEvents.GENERIC_SPLASH, 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
                this.heal(1.0F);
            }
        }
        super.customServerAiStep();
    }

    private int findBuddies() {
        List<Flounder> list =
                this.level().getEntitiesOfClass(Flounder.class, this.getBoundingBox().inflate(16.0, 8.0, 16.0));
        return list.size();
    }

    /** Gold {@code getCanSpawnHere}: y ≥ 50, daytime, buddies ≤ 10, 1/20 random. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.random.nextInt(20) != 1) {
            return false;
        }
        return this.findBuddies() <= 10;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: babies persist (noDespawnRequired); else !noDespawnRequired
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isBreedingItem: MyCrystalApple
        return stack.is(ModItems.CRYSTAL_APPLE.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return danger.orespawn.init.ModEntities.FLOUNDER.get().create(level);
    }
}
