package danger.orespawn.entity;

import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Whale} (EntityAnimal). Size 1.5×2.5, speed 0.35, health 100, attack 0, XP 40.
 * Water-seek when dry; heal in water; blowhole spray particles; breed food crystal apple (golden apple).
 * Tempted by fish (cod). canBreatheUnderwater=true — air forced each tick.
 * Registry size set in {@code ModEntities} .
 */
public class Whale extends Animal {
    private final float moveSpeed = 0.35F;
    private int spray = 0;
    private int sprayTimer = 0;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Whale(EntityType<? extends Whale> type, Level level) {
        super(type, level);
        this.xpReward = 40; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 100.0); // gold field_70174_ab = 100
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate, Tempt(fish), Panic 1.5, WatchClosest Player 12, MyEntityAIWander, LookIdle
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2, Ingredient.of(Items.COD), false));
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
        // gold canBreatheUnderwater=true
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();

        // gold spray timer / blowhole particles
        if (this.spray == 0) {
            if (this.sprayTimer > 0) {
                this.sprayTimer--;
            }
            if (this.sprayTimer == 0) {
                this.sprayTimer = 250 + this.random.nextInt(250);
                this.spray = 25 + this.random.nextInt(25);
            }
        }

        if (this.level().isClientSide && this.spray > 0) {
            for (int i = 0; i < 20; i++) {
                double d = this.random.nextDouble() * 0.75;
                d *= d;
                double dir = this.random.nextDouble() * 2.0 * Math.PI;
                dir -= Math.PI;
                double dx = Math.cos(dir) * d / 2.0;
                double dz = Math.sin(dir) * d / 2.0;
                dir += Math.PI / 2;
                if (i < 10) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.BUBBLE,
                                    this.getX() + dx,
                                    this.getY() + 1.0 + d,
                                    this.getZ() + dz,
                                    Math.cos(dir) * this.random.nextFloat() / 4.0,
                                    this.random.nextFloat() * 2.0F,
                                    Math.sin(dir) * this.random.nextFloat() / 4.0);
                } else {
                    this.level()
                            .addParticle(
                                    ParticleTypes.SPLASH,
                                    this.getX() + dx,
                                    this.getY() + 1.0 + d,
                                    this.getZ() + dz,
                                    Math.cos(dir) * this.random.nextFloat() / 4.0,
                                    this.random.nextFloat() * 2.0F,
                                    Math.sin(dir) * this.random.nextFloat() / 4.0);
                }
            }
            this.spray--;
        }

        // gold: 1/200 heal 1
        if (this.random.nextInt(200) == 1) {
            this.heal(1.0F);
        }
    }

    public int mygetMaxHealth() {
        return 100;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: "splash"
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold: orespawn:little_splat
        return SoundsHandler.LITTLE_SPLAT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold: orespawn:big_splat
        return SoundsHandler.BIG_SPLAT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.9F;
    }

    @Override
    public float getVoicePitch() {
        return 0.5F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 20 + nextInt(25) fish
        int var3 = this.random.nextInt(25) + 20;
        for (int i = 0; i < var3; i++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
    }

    private boolean isWater(BlockState state) {
        return state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} — find nearest water on expanding shell. */
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

            // gold: when not in water, seek every 1/20; else slowly take 4 dmg
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
                        // gold heal(-4.0F)
                        this.hurt(this.damageSources().generic(), 4.0F);
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
        List<Whale> list =
                this.level().getEntitiesOfClass(Whale.class, this.getBoundingBox().inflate(32.0, 8.0, 32.0));
        return list.size();
    }

    /** Gold: y≥50, daytime, 1/50 roll, buddies ≤ 0 (alone). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.random.nextInt(50) != 1) {
            return false;
        }
        return this.findBuddies() <= 0;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
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
        // gold spawnBabyAnimal → new Whale; no ModEntities ref in exclusive scope
        return (AgeableMob) this.getType().create(level);
    }
}
