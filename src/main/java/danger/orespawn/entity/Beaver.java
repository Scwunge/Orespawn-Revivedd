package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Beaver} (EntityAnimal). Size 0.6×0.8, speed 0.2, health 15, attack 1, XP 5.
 * Eats wood (primary log), fells connected wood with log item drops when mobGriefing; drops porkchop.
 */
public class Beaver extends Animal {
    private float moveSpeed = 0.2F;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Beaver(EntityType<? extends Beaver> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 8.0F, 1.0, 1.5));
        this.goalSelector.addGoal(4, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0, 1.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    /**
     * Gold {@code isWood}: logs + wooden doors/stairs/slabs/fences/trapdoors.
     * 1.21 uses block tags (covers all wood types; gold listed per-block).
     */
    private boolean isWood(BlockState state) {
        return state.is(BlockTags.LOGS)
                || state.is(BlockTags.PLANKS)
                || state.is(BlockTags.WOODEN_DOORS)
                || state.is(BlockTags.WOODEN_STAIRS)
                || state.is(BlockTags.WOODEN_SLABS)
                || state.is(BlockTags.WOODEN_FENCES)
                || state.is(BlockTags.WOODEN_TRAPDOORS);
    }

    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWood(bid)) {
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
                if (this.isWood(bid)) {
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
                if (this.isWood(bid)) {
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
                if (this.isWood(bid)) {
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
                if (this.isWood(bid)) {
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
                if (this.isWood(bid)) {
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
     * Gold {@code dropItemRand}: scatter item entity around beaver (x±4, y+4.7, z±4).
     * Used when felling connected wood so log items appear near the beaver.
     */
    private ItemStack dropItemRand(Item index, int count) {
        ItemStack is = new ItemStack(index, count);
        if (is.isEmpty()) {
            return is;
        }
        ItemEntity var3 = new ItemEntity(
                this.level(),
                this.getX() + this.random.nextInt(4) - this.random.nextInt(4),
                this.getY() + 4.0 + this.random.nextInt(4),
                this.getZ() + this.random.nextInt(4) - this.random.nextInt(4),
                is);
        this.level().addFreshEntity(var3);
        return is;
    }

    /**
     * Gold {@code breakRecursor}: BFS-style wood fell (depth ≤200). Removes wood and
     * {@link #dropItemRand drops} the block item (logs/planks/etc.).
     */
    public void breakRecursor(Level world, int x, int y, int z, int xf, int yf, int zf, int recursion) {
        int var7x = 1;
        if (recursion > 200) {
            return;
        }
        for (int var9 = -var7x; var9 <= var7x; var9++) {
            for (int var10 = -var7x; var10 <= var7x; var10++) {
                for (int var11 = -var7x; var11 <= var7x; var11++) {
                    if ((var9 != 0 || var10 != 0 || var11 != 0)
                            && (x + var9 != xf || y + var10 != yf || z + var11 != zf)
                            && (recursion <= 0
                                    || x + var9 < xf - var7x
                                    || x + var9 > xf + var7x
                                    || y + var10 < yf - var7x
                                    || y + var10 > yf + var7x
                                    || z + var11 < zf - var7x
                                    || z + var11 > zf + var7x)) {
                        BlockPos pos = new BlockPos(x + var9, y + var10, z + var11);
                        BlockState var12 = world.getBlockState(pos);
                        if (this.isWood(var12)) {
                            Block block = var12.getBlock();
                            world.removeBlock(pos, false);
                            Item item = block.asItem();
                            if (item != Items.AIR) {
                                this.dropItemRand(item, 1);
                            }
                            this.breakRecursor(world, x + var9, y + var10, z + var11, x, y, z, recursion + 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isDeadOrDying()) {
            if (this.random.nextInt(200) == 1) {
                this.setLastHurtByMob(null);
            }
            // gold: (1/30 when hurt OR 1/350) && PlayNicely==0
            if (((this.random.nextInt(30) == 0 && this.getHealth() < this.mygetMaxHealth())
                    || this.random.nextInt(350) == 1)
                    && OreSpawnMain.PlayNicely == 0) {
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
                            BlockPos pos = new BlockPos(this.tx, this.ty, this.tz);
                            // gold: primary wood is eaten (no item drop); neighbors fell + drop
                            this.level().removeBlock(pos, false);
                            this.breakRecursor(this.level(), this.tx, this.ty, this.tz, this.tx, this.ty, this.tz, 0);
                        }
                        this.heal(1.0F);
                        // gold CHAINSAW not in this jar — wood break stand-in
                        this.playSound(SoundEvents.WOOD_BREAK, 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
                    }
                }
            }
            if (this.random.nextInt(200) == 1) {
                Beaver buddy = this.findBuddy();
                if (buddy != null) {
                    this.getNavigation().moveTo(buddy.getX(), buddy.getY(), buddy.getZ(), 0.5);
                }
            }
        }
        super.customServerAiStep();
    }

    /** Gold {@code findBuddy}: nearest Beaver in 16×6×16 (GenericTargetSorter). */
    @Nullable
    private Beaver findBuddy() {
        List<Beaver> list =
                this.level().getEntitiesOfClass(Beaver.class, this.getBoundingBox().inflate(16.0, 6.0, 16.0));
        return list.stream()
                .filter(b -> b != this)
                .min(Comparator.comparingDouble(b -> b.distanceToSqr(this)))
                .orElse(null);
    }

    public int mygetMaxHealth() {
        return 15;
    }

    // gold canBreatheUnderwater=true — LivingEntity method is final in 1.21; cannot override

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold ENTITY_SCORPION_HIT — no ogg; CRYO_HURT stand-in (see sounds audit)
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return danger.orespawn.util.handlers.SoundsHandler.ENTITY_CRYO_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    /** Gold {@code getSoundPitch}: babies +1.5 base, adults +1.0. */
    @Override
    public float getVoicePitch() {
        if (this.isBaby()) {
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.5F;
        }
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold getDropItem: porkchop (field_151147_al)
        this.spawnAtLocation(new ItemStack(Items.PORKCHOP));
    }

    /**
     * Gold {@code getCanSpawnHere}: 50 ≤ y ≤ 100 and ground is dirt/grass/tallgrass/leaves.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (this.getY() < 50.0 || this.getY() > 100.0) {
            return false;
        }
        BlockPos below = BlockPos.containing(this.getX(), this.getY() - 1.0, this.getZ());
        BlockState ground = level.getBlockState(below);
        return ground.is(Blocks.DIRT)
                || ground.is(Blocks.GRASS_BLOCK)
                || ground.is(Blocks.SHORT_GRASS)
                || ground.is(Blocks.TALL_GRASS)
                || ground.is(BlockTags.LEAVES)
                || ground.is(BlockTags.DIRT);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn = false
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // gold isWheat: apple
        return stack.is(Items.APPLE);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return danger.orespawn.init.ModEntities.BEAVER.get().create(level);
    }
}
