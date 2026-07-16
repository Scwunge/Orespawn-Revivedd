package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.WanderALotGoal;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Baryonyx} (EntityAnimal). Size 1.5×2.8, speed 0.25, health 40, attack 8, XP 5.
 * Passive grazer — scans for grass, moves to it, replaces with dirt (mobGriefing), heals 1.
 * Registry size set in {@code ModEntities.BARYONYX}.
 */
public class Baryonyx extends Animal {
    private final float moveSpeed = 0.25F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Baryonyx(EntityType<? extends Baryonyx> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMate
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        // gold: EntityAIAvoidEntity(EntityMob, 8, 1.0, 1.4)
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.4));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0F));
        // gold: MyEntityAIWander — WanderALotGoal best-effort
        this.goalSelector.addGoal(6, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** Gold was EntityAnimal, not EntityMob — never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
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

    @Override
    public void tick() {
        // gold: re-assert moveSpeed every tick
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /**
     * Gold {@code scan_it} — shell search for grass blocks around (x,y,z) at offsets dx/dy/dz.
     * Gold block: {@code Blocks.field_150349_c} → {@link Blocks#GRASS_BLOCK}.
     */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                if (this.level().getBlockState(new BlockPos(x + dx, y + i, z + j)).is(Blocks.GRASS_BLOCK)) {
                    int d = dx * dx + j * j + i * i;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + dx;
                        this.ty = y + i;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (this.level().getBlockState(new BlockPos(x - dx, y + i, z + j)).is(Blocks.GRASS_BLOCK)) {
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
                if (this.level().getBlockState(new BlockPos(x + xi, y + dy, z + j)).is(Blocks.GRASS_BLOCK)) {
                    int d = dy * dy + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + dy;
                        this.tz = z + j;
                        found++;
                    }
                }
                if (this.level().getBlockState(new BlockPos(x + xi, y - dy, z + j)).is(Blocks.GRASS_BLOCK)) {
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
                if (this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz)).is(Blocks.GRASS_BLOCK)) {
                    int d = dz * dz + j * j + xi * xi;
                    if (d < this.closest) {
                        this.closest = d;
                        this.tx = x + xi;
                        this.ty = y + j;
                        this.tz = z + dz;
                        found++;
                    }
                }
                if (this.level().getBlockState(new BlockPos(x + xi, y + j, z - dz)).is(Blocks.GRASS_BLOCK)) {
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

    /**
     * Gold {@code updateAITick} — clear revenge 1/200; graze grass 1/60 when PlayNicely==0.
     */
    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }

            // gold: nextInt(60)==0 && PlayNicely==0
            if (this.random.nextInt(60) == 0 && OreSpawnMain.PlayNicely == 0) {
                this.closest = 99999;
                this.tx = this.ty = this.tz = 0;

                for (int i = 1; i < 11; i++) {
                    int j = i;
                    if (j > 2) {
                        j = 2;
                    }
                    if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                        break;
                    }
                    if (i >= 6) {
                        i++;
                    }
                }

                if (this.closest < 99999) {
                    this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                    if (this.closest < 12) {
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                            // gold: set grass → dirt
                            this.level().setBlock(
                                    new BlockPos(this.tx, this.ty, this.tz),
                                    Blocks.DIRT.defaultBlockState(),
                                    3);
                        }
                        this.heal(1.0F);
                        // gold: SoundEvents.field_187739_dZ (entity.generic.eat)
                        this.playSound(
                                SoundEvents.GENERIC_EAT,
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.9F);
                    }
                }
            }
        }
        super.customServerAiStep();
    }

    /** Gold {@code mygetMaxHealth()}. */
    public int mygetMaxHealth() {
        return 40;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_DUCK_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 2 + nextInt(5) beef
        int count = 2 + this.random.nextInt(5);
        for (int i = 0; i < count; i++) {
            this.spawnAtLocation(new ItemStack(Items.BEEF));
        }
    }

    /**
     * Gold {@code getCanSpawnHere}: y &gt;= 50, daytime, findBuddies() &lt;= 8.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        return this.findBuddies() <= 8;
    }

    /** Gold {@code findBuddies}: nearby Baryonyx in 20×10×20 inflate. */
    private int findBuddies() {
        List<Baryonyx> list =
                this.level().getEntitiesOfClass(Baryonyx.class, this.getBoundingBox().inflate(20.0, 10.0, 20.0));
        return list.size();
    }

    /** Gold {@code isBreedingItem}: golden apple. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.GOLDEN_APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return danger.orespawn.init.ModEntities.BARYONYX.get().create(level);
    }

    /** Gold {@code canMateWith}: same species and both in love. */
    @Override
    public boolean canMate(Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (!(otherAnimal instanceof Baryonyx)) {
            return false;
        }
        return this.isInLove() && otherAnimal.isInLove();
    }
}
