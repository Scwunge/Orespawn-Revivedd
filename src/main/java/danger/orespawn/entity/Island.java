package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Island} (EntityAnimal) — light floating island. Size 0.5×0.5.
 * Builds/moves a circular mycelium+endstone island; may spawn Triffid; no AI goals.
 * Gold ClientProxy: ModelIsland(1.0F), shadow 0.25F, scale 1.0F.
 */
public class Island extends Animal {
    /**
     * Gold {@code OreSpawnMain.IslandSpeedFactor} config default 2 (clamped 1–5).
     * Local until config on OreSpawnMain.
     */
    public static int IslandSpeedFactor = 2;

    private float dir = 0.0F;
    private float speed = 0.1F;
    private int radius = 5;
    private int depth = 3;
    private int timer = 73;
    private int justSpawned = 1;
    private int ticker = 0;
    private int once = 1;
    private double myX;
    private double myY;
    private double myZ;
    private int dirchange;

    public Island(EntityType<? extends Island> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.ticker = level.getRandom().nextInt(50);
        this.dirchange = level.getRandom().nextInt(2500);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold: no applyEntityAttributes override — EntityLiving defaults
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        // gold: empty updateAITasks / updateAITick — no goals
    }

    @Override
    public void tick() {
        super.tick();
        // gold: motionX = motionY = motionZ = 0
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.level().isClientSide) {
            if (this.once != 0) {
                this.myX = this.getX();
                this.myY = this.getY();
                this.myZ = this.getZ();
                this.once = 0;
            }

            if (this.justSpawned != 0) {
                this.dir = this.random.nextFloat() * (float) Math.PI;
                if (this.random.nextInt(2) == 1) {
                    this.dir *= -1.0F;
                }

                if (this.random.nextInt(40) != 1) {
                    this.radius = 3 + this.random.nextInt(4);
                    this.depth = 2 + this.random.nextInt(3);
                    this.speed = this.random.nextFloat() / 50.0F * IslandSpeedFactor;
                } else {
                    this.radius = 6 + this.random.nextInt(5);
                    this.depth = 3 + this.random.nextInt(4);
                    this.speed = this.random.nextFloat() / 200.0F * IslandSpeedFactor;
                }

                this.createIsland();
                this.ticker = this.random.nextInt(50);
                this.dirchange = this.random.nextInt(10000);
            }

            this.ticker++;
            if (this.ticker >= this.timer) {
                this.updateIsland();
                this.ticker = 0;
            }

            this.dirchange--;
            if (this.dirchange <= 0) {
                this.dirchange = this.random.nextInt(5000);
                this.dir = this.random.nextFloat() * (float) Math.PI;
                if (this.random.nextInt(2) == 1) {
                    this.dir *= -1.0F;
                }
            }

            this.justSpawned = 0;
        }
    }

    /**
     * Gold {@code onLivingUpdate}: only client runs super (no server living AI).
     */
    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            super.aiStep();
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false; // gold empty fall
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    /** Gold {@code canDespawn} false. */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("JustSpawned", this.justSpawned);
        tag.putInt("Idepth", this.depth);
        tag.putInt("Iradius", this.radius);
        tag.putFloat("Ispeed", this.speed);
        tag.putFloat("Idir", this.dir);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.justSpawned = tag.getInt("JustSpawned");
        this.depth = tag.getInt("Idepth");
        this.radius = tag.getInt("Iradius");
        this.speed = tag.getFloat("Ispeed");
        this.dir = tag.getFloat("Idir");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null; // gold createChild null
    }

    private void createIsland() {
        double deltadir = 0.10471975333333333;
        double deltamag = 0.35F;
        int ixlast = 0;
        int izlast = 0;
        int xoff = 0;
        int zoff = 0;

        for (int i = 0; i < this.depth; i++) {
            izlast = 0;
            ixlast = 0;

            for (double curdir = -3.1415926; curdir < 3.1415926; curdir += deltadir) {
                double tradius = this.radius;
                tradius /= i + 1;

                for (double h = 0.75; h < tradius; h += deltamag) {
                    int ix = (int) (this.getX() + Math.cos(curdir + this.dir) * h);
                    int iz = (int) (this.getZ() + Math.sin(curdir + this.dir) * h);
                    if (ix != ixlast || iz != izlast) {
                        ixlast = ix;
                        izlast = iz;
                        if (i == 0) {
                            BlockPos pos = BlockPos.containing(ix, (int) this.getY() - i + 1, iz);
                            BlockState bid = this.level().getBlockState(pos);
                            if (bid.isAir()) {
                                if (this.random.nextInt(5000) == 1) {
                                    this.level().setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
                                } else {
                                    this.fastSetBlock(ix, (int) this.getY() - i + 1, iz, Blocks.MYCELIUM);
                                    BlockPos above = pos.above();
                                    if (this.random.nextInt(20) == 1 && this.level().getBlockState(above).isAir()) {
                                        if (this.random.nextInt(2) == 1) {
                                            this.level().setBlock(above, Blocks.RED_MUSHROOM.defaultBlockState(), 3);
                                        } else {
                                            this.level().setBlock(above, Blocks.BROWN_MUSHROOM.defaultBlockState(), 3);
                                        }
                                    }
                                }
                            } else if (bid.is(Blocks.BEDROCK)) {
                                this.discard();
                                return;
                            }
                        } else if (this.random.nextInt(10) == 1) {
                            this.fastSetBlock(ix, (int) this.getY() - i + 1, iz, Blocks.EMERALD_ORE);
                        } else {
                            this.fastSetBlock(ix, (int) this.getY() - i + 1, iz, Blocks.END_STONE);
                        }
                    }
                }
            }
        }

        if (this.getX() < 0.0) {
            xoff = -1;
        }
        if (this.getZ() < 0.0) {
            zoff = -1;
        }

        int cx = (int) this.getX() + xoff;
        int cy = (int) this.getY();
        int cz = (int) this.getZ() + zoff;
        this.level().setBlock(new BlockPos(cx, cy, cz), Blocks.AIR.defaultBlockState(), 3);
        this.fastSetBlock(cx, cy, cz, Blocks.AIR);
    }

    private void updateIsland() {
        double deltadir = 0.10471975333333333;
        double deltamag = 0.35F;
        int ixlast = 0;
        int izlast = 0;
        int xoff = 0;
        int zoff = 0;
        this.myX = this.myX + this.speed * Math.cos(this.dir);
        this.myZ = this.myZ + this.speed * Math.sin(this.dir);
        int mx = (int) this.myX;
        int mz = (int) this.myZ;
        int px = (int) this.getX();
        int pz = (int) this.getZ();
        if (mx != px || mz != pz) {
            for (int i = 0; i < this.depth; i++) {
                izlast = 0;
                ixlast = 0;

                for (double curdir = -3.3; curdir < 3.3; curdir += deltadir / 2.0) {
                    double tradius = this.radius;
                    tradius /= i + 1;
                    double h = 0.75;

                    while (h < tradius) {
                        h += deltamag;
                    }

                    h -= deltamag;
                    if (h < 0.75) {
                        h = 0.75;
                    }

                    for (; h < tradius + deltamag; h += deltamag / 2.0) {
                        int ix = (int) (this.getX() + Math.cos(curdir + this.dir) * h);
                        int iz = (int) (this.getZ() + Math.sin(curdir + this.dir) * h);
                        if (ix != ixlast || iz != izlast) {
                            ixlast = ix;
                            izlast = iz;
                            if (i == 0) {
                                BlockPos top = BlockPos.containing(ix, (int) this.getY() + 1 + 1, iz);
                                BlockState bid = this.level().getBlockState(top);
                                if (bid.is(Blocks.BROWN_MUSHROOM) || bid.is(Blocks.RED_MUSHROOM)) {
                                    this.fastSetBlock(ix, (int) this.getY() + 1 + 1, iz, Blocks.AIR);
                                }
                            }

                            this.fastSetBlock(ix, (int) this.getY() - i + 1, iz, Blocks.AIR);
                        }
                    }
                }
            }

            if (this.getX() < 0.0) {
                xoff = -1;
            }
            if (this.getZ() < 0.0) {
                zoff = -1;
            }

            this.level()
                    .setBlock(
                            new BlockPos((int) this.getX() + xoff, (int) this.getY(), (int) this.getZ() + zoff),
                            Blocks.END_STONE.defaultBlockState(),
                            3);

            double newX = (int) this.myX;
            if (this.myX < 0.0) {
                newX -= 0.5;
            } else {
                newX += 0.5;
            }
            double newZ = (int) this.myZ;
            if (this.myZ < 0.0) {
                newZ -= 0.5;
            } else {
                newZ += 0.5;
            }
            this.setPos(newX, this.getY(), newZ);

            for (int layer = 0; layer < this.depth; layer++) {
                izlast = 0;
                ixlast = 0;

                for (double curdir = -3.1415926; curdir < 3.1415926; curdir += deltadir) {
                    double tradius = this.radius;
                    tradius /= layer + 1;
                    double h = 0.75;

                    while (h < tradius) {
                        h += deltamag;
                    }

                    h -= deltamag * 3.0;
                    if (h < 0.75) {
                        h = 0.75;
                    }

                    for (; h < tradius; h += deltamag) {
                        int ix = (int) (this.getX() + Math.cos(curdir + this.dir) * h);
                        int iz = (int) (this.getZ() + Math.sin(curdir + this.dir) * h);
                        if (ix != ixlast || iz != izlast) {
                            ixlast = ix;
                            izlast = iz;
                            if (layer == 0) {
                                BlockPos pos = BlockPos.containing(ix, (int) this.getY() - layer + 1, iz);
                                BlockState bid = this.level().getBlockState(pos);
                                if (bid.isAir()) {
                                    if (this.random.nextInt(5000) == 1) {
                                        this.level().setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
                                    } else {
                                        this.fastSetBlock(ix, (int) this.getY() - layer + 1, iz, Blocks.MYCELIUM);
                                        BlockPos above = pos.above();
                                        if (this.random.nextInt(20) == 1
                                                && this.level().getBlockState(above).isAir()) {
                                            if (this.random.nextInt(2) == 1) {
                                                this.level()
                                                        .setBlock(above, Blocks.RED_MUSHROOM.defaultBlockState(), 3);
                                            } else {
                                                this.level()
                                                        .setBlock(
                                                                above, Blocks.BROWN_MUSHROOM.defaultBlockState(), 3);
                                            }
                                        }
                                    }
                                } else if (bid.is(Blocks.BEDROCK)) {
                                    this.discard();
                                    return;
                                }
                            } else {
                                BlockPos pos = BlockPos.containing(ix, (int) this.getY() - layer + 1, iz);
                                BlockState bid = this.level().getBlockState(pos);
                                if (bid.is(Blocks.STONE)) {
                                    if (!this.level().isClientSide) {
                                        this.level()
                                                .explode(
                                                        this,
                                                        ix,
                                                        this.getY() - layer + 1.0,
                                                        iz,
                                                        5.0F,
                                                        Level.ExplosionInteraction.TNT);
                                    }
                                } else if (this.random.nextInt(10) == 1) {
                                    this.fastSetBlock(ix, (int) this.getY() - layer + 1, iz, Blocks.EMERALD_ORE);
                                } else {
                                    this.fastSetBlock(ix, (int) this.getY() - layer + 1, iz, Blocks.END_STONE);
                                }
                            }
                        }
                    }
                }
            }

            int var40 = 0;
            if (this.getX() < 0.0) {
                var40 = -1;
            }
            int var41 = 0;
            if (this.getZ() < 0.0) {
                var41 = -1;
            }

            int cx = (int) this.getX() + var40;
            int cy = (int) this.getY();
            int cz = (int) this.getZ() + var41;
            this.level().setBlock(new BlockPos(cx, cy, cz), Blocks.AIR.defaultBlockState(), 3);
            this.fastSetBlock(cx, cy, cz, Blocks.AIR);
        }

        // gold: 1/(2+2000/timer) chance to spawn Triffid if none nearby
        if (this.random.nextInt(2 + 2000 / this.timer) == 1) {
            AABB bb = new AABB(
                    this.getX() - 10.0,
                    this.getY() - 5.0,
                    this.getZ() - 10.0,
                    this.getX() + 10.0,
                    this.getY() + 5.0,
                    this.getZ() + 10.0);
            List<Triffid> nearby = this.level().getEntitiesOfClass(Triffid.class, bb);
            if (nearby.isEmpty()) {
                spawnTriffid(this.level(), this.getX(), this.getY() + 2.01, this.getZ());
            }
        }
    }

    /** Gold {@code spawnCreature(world, "Triffid", .)}. */
    private static void spawnTriffid(Level level, double x, double y, double z) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        Triffid triffid = ModEntities.TRIFFID.get().create(server);
        if (triffid != null) {
            triffid.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
            triffid.finalizeSpawn(
                    server, server.getCurrentDifficultyAt(triffid.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            server.addFreshEntity(triffid);
        }
    }

    /**
     * Gold {@code getDropItem}: Item.getItemFromBlock(MyIslandBlock) — island block deferred.
     */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        this.spawnAtLocation(new ItemStack(danger.orespawn.init.ModBlocks.ISLAND_BLOCK.get()));
    }

    /** Gold {@code FastSetBlock} → setBlock flags 3. */
    public void fastSetBlock(int ix, int iy, int iz, Block id) {
        this.level().setBlock(new BlockPos(ix, iy, iz), id.defaultBlockState(), 3);
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

    @Override
    protected float getSoundVolume() {
        return 0.0F;
    }
}
