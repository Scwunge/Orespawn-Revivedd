package danger.orespawn.entity;

import danger.orespawn.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code IslandToo} (EntityAnimal) — dark floating island. Size 0.5×0.5.
 * Builds/moves a square grass/ore island (cardinal drift); no AI goals.
 * Gold ClientProxy: ModelIsland(1.0F), shadow 0.25F, scale 1.0F.
 */
public class IslandToo extends Animal {
    /**
     * Gold {@code OreSpawnMain.IslandSpeedFactor} config default 2.
     * Local until config on OreSpawnMain.
     */
    public static int IslandSpeedFactor = 2;
    /**
     * Gold {@code OreSpawnMain.IslandSizeFactor} config default 2.
     * Local until config on OreSpawnMain.
     */
    public static int IslandSizeFactor = 2;

    /** Gold flower stand-ins until MyFlower* blocks exist. */
    private static final Block FLOWER_PINK = Blocks.PINK_TULIP;
    private static final Block FLOWER_BLUE = Blocks.BLUE_ORCHID;
    private static final Block FLOWER_BLACK = Blocks.WITHER_ROSE;
    private static final Block FLOWER_SCARY = Blocks.ALLIUM;
    /** Gold MySkyTreeLog stand-in. */
    private static final Block SKY_TREE_LOG = Blocks.OAK_LOG;

    private int dir = 0;
    private float speed = 0.1F;
    private int width = 5;
    private int depth = 3;
    private int length = 10;
    private int timer = 42;
    private int justSpawned = 1;
    private int ticker = 0;
    private int once = 1;
    private double myX;
    private double myY;
    private double myZ;
    private int dirchange = 0;
    private int blocktype = 0;

    public IslandToo(EntityType<? extends IslandToo> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.ticker = level.getRandom().nextInt(50);
        this.dirchange = level.getRandom().nextInt(5000);
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
        // gold: empty updateAITasks / updateAITick
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.level().isClientSide) {
            if (this.once != 0) {
                this.myX = this.getX();
                this.myY = this.getY();
                this.myZ = this.getZ();
                this.once = 0;
            }

            if (this.justSpawned != 0) {
                this.dir = this.random.nextInt(4);
                if (this.random.nextInt(40) != 1) {
                    this.width = 1 + this.random.nextInt(5 * IslandSizeFactor);
                    this.length = this.width;
                    this.depth = 1 + this.random.nextInt(4);
                    this.speed = this.random.nextFloat() / 40.0F * IslandSpeedFactor;
                    if (this.length * this.width * this.depth <= 64) {
                        this.speed *= 2.0F;
                    }
                    if (this.length * this.width * this.depth <= 32) {
                        this.speed *= 2.0F;
                    }
                } else {
                    this.width = 5 + this.random.nextInt(8 * IslandSizeFactor);
                    this.length = this.width;
                    this.depth = 3 + this.random.nextInt(6);
                    this.speed = this.random.nextFloat() / 150.0F * IslandSpeedFactor;
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
                this.dir = this.random.nextInt(4);
            }

            this.justSpawned = 0;
        }
    }

    /** Gold {@code onLivingUpdate}: client-only super. */
    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            super.aiStep();
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
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
        tag.putInt("Iwidth", this.width);
        tag.putInt("Idepth", this.depth);
        tag.putInt("Ilength", this.length);
        tag.putFloat("Ispeed", this.speed);
        tag.putInt("Idir", this.dir);
        tag.putInt("Iblocktype", this.blocktype);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.justSpawned = tag.getInt("JustSpawned");
        this.width = tag.getInt("Iwidth");
        this.depth = tag.getInt("Idepth");
        this.length = tag.getInt("Ilength");
        this.speed = tag.getFloat("Ispeed");
        this.dir = tag.getInt("Idir");
        this.blocktype = tag.getInt("Iblocktype");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    /**
     * Gold {@code attackEntityFrom}: snap to block center, apply super damage, return false.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        int ix = (int) this.getX();
        int iz = (int) this.getZ();
        double nx;
        if (ix < 0) {
            nx = ix - 0.5;
        } else {
            nx = ix + 0.5;
        }
        double nz;
        if (iz < 0) {
            nz = iz - 0.5;
        } else {
            nz = iz + 0.5;
        }
        this.setPos(nx, this.getY(), nz);
        super.hurt(source, amount);
        return false;
    }

    private void createIsland() {
        int xoff = 0;
        int zoff = 0;
        if (this.getX() < 0.0) {
            xoff = 1;
        }
        if (this.getZ() < 0.0) {
            zoff = 1;
        }

        for (int k = 0; k <= this.depth; k++) {
            int il = this.length / (this.depth - k + 1);
            if (il < 1) {
                il = 1;
            }

            for (int i = -il; i <= il; i++) {
                for (int j = -il; j <= il; j++) {
                    int ix = (int) this.getX() + j - xoff;
                    int iz = (int) this.getZ() + i - zoff;
                    if (k == this.depth) {
                        BlockPos pos = new BlockPos(ix, (int) this.getY() + k, iz);
                        BlockState bid = this.level().getBlockState(pos);
                        if (bid.isAir()) {
                            if (this.random.nextInt(5000) == 1) {
                                this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                            } else {
                                this.fastSetBlock(ix, (int) this.getY() + k, iz, Blocks.GRASS_BLOCK);
                                BlockPos above = pos.above();
                                if (this.random.nextInt(30) == 1) {
                                    if (this.level().getBlockState(above).isAir()) {
                                        if (this.random.nextInt(2) == 1) {
                                            this.level().setBlock(above, FLOWER_PINK.defaultBlockState(), 3);
                                        } else {
                                            this.level().setBlock(above, FLOWER_BLUE.defaultBlockState(), 3);
                                        }
                                    }
                                } else if (this.random.nextInt(100) == 1
                                        && this.level().getBlockState(above).isAir()) {
                                    this.placeSmallTree(ix, (int) this.getY() + k + 1, iz);
                                }
                            }
                        } else if (bid.is(Blocks.BEDROCK)) {
                            this.discard();
                            return;
                        }
                    } else {
                        this.mySetBlock(ix, (int) this.getY() + k, iz);
                    }
                }
            }
        }

        this.level()
                .setBlock(
                        new BlockPos((int) this.getX() - xoff, (int) this.getY(), (int) this.getZ() - zoff),
                        Blocks.AIR.defaultBlockState(),
                        3);
    }

    private void mySetBlock(int ix, int iy, int iz) {
        Block bid = Blocks.STONE;
        if (this.blocktype == 0) {
            this.blocktype = 1 + this.random.nextInt(8);
        }

        if (this.blocktype == 1 && this.random.nextInt(5) == 1) {
            bid = Blocks.COAL_ORE;
        }
        if (this.blocktype == 2 && this.random.nextInt(10) == 1) {
            bid = Blocks.IRON_ORE;
        }
        if (this.blocktype == 3 && this.random.nextInt(20) == 1) {
            bid = Blocks.DIAMOND_ORE;
        }
        if (this.blocktype == 4 && this.random.nextInt(30) == 1) {
            bid = ModBlocks.TITANIUM_ORE.get();
        }
        if (this.blocktype == 5 && this.random.nextInt(30) == 1) {
            bid = ModBlocks.URANIUM_ORE.get();
        }
        if (this.blocktype == 6 && this.random.nextInt(30) == 1) {
            // gold MyOreRubyBlock — emerald ore stand-in
            bid = Blocks.EMERALD_ORE;
        }
        if (this.blocktype == 7 && this.random.nextInt(30) == 1) {
            bid = ModBlocks.AMETHYST_ORE.get();
        }
        if (this.blocktype == 8 && this.random.nextInt(20) == 1) {
            bid = Blocks.GOLD_ORE;
        }

        if (bid == Blocks.STONE) {
            if (this.random.nextInt(3000) == 1) {
                // gold MyEnderPearlBlock
                bid = ModBlocks.ENDER_PEARL_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 2) {
                // gold MyEyeOfEnderBlock
                bid = ModBlocks.EYE_OF_ENDER_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 3) {
                bid = ModBlocks.AMETHYST_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 4) {
                // gold MyBlockRubyBlock
                bid = ModBlocks.RUBY_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 5) {
                bid = ModBlocks.URANIUM_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 6) {
                bid = ModBlocks.TITANIUM_BLOCK.get();
            }
            if (this.random.nextInt(3000) == 7) {
                bid = Blocks.GOLD_BLOCK;
            }
            if (this.random.nextInt(3000) == 8) {
                bid = Blocks.DIAMOND_BLOCK;
            }
        }

        this.fastSetBlock(ix, iy, iz, bid);
    }

    private void updateIsland() {
        int xoff = 0;
        int zoff = 0;
        if (this.dir == 0) {
            this.myZ = this.myZ - this.speed;
        } else if (this.dir == 1) {
            this.myZ = this.myZ + this.speed;
        } else if (this.dir == 2) {
            this.myX = this.myX + this.speed;
        } else {
            this.myX = this.myX - this.speed;
        }

        int mx = (int) this.myX;
        int mz = (int) this.myZ;
        int px = (int) this.getX();
        int pz = (int) this.getZ();
        if (mx != px || mz != pz) {
            int jStart;
            int jEnd;
            int kStart;
            int kEnd;
            if (this.dir == 0) {
                jStart = 1;
                jEnd = 1;
                kStart = -1;
                kEnd = 1;
            } else if (this.dir == 1) {
                jStart = -1;
                jEnd = -1;
                kStart = -1;
                kEnd = 1;
            } else if (this.dir == 2) {
                jStart = -1;
                jEnd = 1;
                kStart = -1;
                kEnd = -1;
            } else {
                jStart = -1;
                jEnd = 1;
                kStart = 1;
                kEnd = 1;
            }

            if (this.getX() < 0.0) {
                xoff = 1;
            }
            if (this.getZ() < 0.0) {
                zoff = 1;
            }

            for (int i = 0; i <= this.depth; i++) {
                int il = this.length / (this.depth - i + 1);
                if (il < 1) {
                    il = 1;
                }

                for (int j = jStart * il; j <= jEnd * il; j++) {
                    for (int k = kStart * il; k <= kEnd * il; k++) {
                        int ix = (int) this.getX() + k - xoff;
                        int iz = (int) this.getZ() + j - zoff;
                        if (i == this.depth) {
                            BlockPos above = new BlockPos(ix, (int) this.getY() + i + 1, iz);
                            BlockState bid = this.level().getBlockState(above);
                            if (bid.is(FLOWER_PINK)
                                    || bid.is(FLOWER_BLUE)
                                    || bid.is(FLOWER_BLACK)
                                    || bid.is(FLOWER_SCARY)) {
                                this.fastSetBlock(ix, (int) this.getY() + i + 1, iz, Blocks.AIR);
                            }

                            // gold: if block above is water (still/flowing), clear this layer cell
                            if (bid.is(Blocks.WATER)) {
                                this.level()
                                        .setBlock(
                                                new BlockPos(ix, (int) this.getY() + i, iz),
                                                Blocks.AIR.defaultBlockState(),
                                                3);
                            }

                            if (bid.is(SKY_TREE_LOG)) {
                                this.level().setBlock(above, Blocks.AIR.defaultBlockState(), 3);
                                BlockState bid2 = this.level().getBlockState(above.above());
                                if (bid2.is(SKY_TREE_LOG)) {
                                    this.level().setBlock(above.above(), Blocks.AIR.defaultBlockState(), 3);
                                    BlockState bid3 = this.level().getBlockState(above.above(2));
                                    if (bid3.is(SKY_TREE_LOG)) {
                                        this.level().setBlock(above.above(2), Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
                            }

                            BlockPos at = new BlockPos(ix, (int) this.getY() + i, iz);
                            bid = this.level().getBlockState(at);
                            if (bid.is(Blocks.WATER)) {
                                this.level().setBlock(at, Blocks.AIR.defaultBlockState(), 3);
                            }
                        }

                        this.fastSetBlock(ix, (int) this.getY() + i, iz, Blocks.AIR);
                    }
                }
            }

            this.mySetBlock((int) this.getX() - xoff, (int) this.getY(), (int) this.getZ() - zoff);

            double newX = mx;
            if (this.myX < 0.0) {
                newX -= 0.5;
            } else {
                newX += 0.5;
            }
            double newZ = mz;
            if (this.myZ < 0.0) {
                newZ -= 0.5;
            } else {
                newZ += 0.5;
            }
            this.setPos(newX, this.getY(), newZ);

            if (this.dir == 0) {
                jStart = -1;
                jEnd = -1;
                kStart = -1;
                kEnd = 1;
            } else if (this.dir == 1) {
                jStart = 1;
                jEnd = 1;
                kStart = -1;
                kEnd = 1;
            } else if (this.dir == 2) {
                jStart = -1;
                jEnd = 1;
                kStart = 1;
                kEnd = 1;
            } else {
                jStart = -1;
                jEnd = 1;
                kStart = -1;
                kEnd = -1;
            }

            int var38 = 0;
            int var37 = 0;
            if (this.getX() < 0.0) {
                var37 = 1;
            }
            if (this.getZ() < 0.0) {
                var38 = 1;
            }

            this.level()
                    .setBlock(
                            new BlockPos((int) this.getX() - var37, (int) this.getY(), (int) this.getZ() - var38),
                            Blocks.AIR.defaultBlockState(),
                            3);

            for (int layer = 0; layer <= this.depth; layer++) {
                int il = this.length / (this.depth - layer + 1);
                if (il < 1) {
                    il = 1;
                }

                for (int j = jStart * il; j <= jEnd * il; j++) {
                    for (int k = kStart * il; k <= kEnd * il; k++) {
                        int ix = (int) this.getX() + k - var37;
                        int iz = (int) this.getZ() + j - var38;
                        if (layer == this.depth) {
                            BlockPos pos = new BlockPos(ix, (int) this.getY() + layer, iz);
                            BlockState bid = this.level().getBlockState(pos);
                            if (bid.isAir()) {
                                if (this.random.nextInt(5000) == 1) {
                                    this.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                                } else {
                                    this.fastSetBlock(ix, (int) this.getY() + layer, iz, Blocks.GRASS_BLOCK);
                                    BlockPos above = pos.above();
                                    if (this.random.nextInt(30) == 1) {
                                        if (this.level().getBlockState(above).isAir()) {
                                            if (this.random.nextInt(2) == 1) {
                                                this.level()
                                                        .setBlock(above, FLOWER_PINK.defaultBlockState(), 3);
                                            } else {
                                                this.level()
                                                        .setBlock(above, FLOWER_BLUE.defaultBlockState(), 3);
                                            }
                                        }
                                    } else if (this.random.nextInt(100) == 1
                                            && this.level().getBlockState(above).isAir()) {
                                        this.placeSmallTree(ix, (int) this.getY() + layer + 1, iz);
                                    }
                                }
                            } else if (bid.is(Blocks.BEDROCK)) {
                                this.discard();
                                return;
                            }
                        } else {
                            BlockPos pos = new BlockPos(ix, (int) this.getY() + layer, iz);
                            BlockState bid = this.level().getBlockState(pos);
                            if (bid.is(Blocks.END_STONE)) {
                                if (!this.level().isClientSide) {
                                    this.level()
                                            .explode(
                                                    this,
                                                    ix,
                                                    this.getY() + layer,
                                                    iz,
                                                    5.0F,
                                                    Level.ExplosionInteraction.TNT);
                                }
                            } else {
                                this.mySetBlock(ix, (int) this.getY() + layer, iz);
                            }
                        }
                    }
                }
            }

            this.level()
                    .setBlock(
                            new BlockPos((int) this.getX() - var37, (int) this.getY(), (int) this.getZ() - var38),
                            Blocks.AIR.defaultBlockState(),
                            3);
        }
    }

    /**
     * Gold {@code OreSpawnTrees.SmallTree} stand-in — short oak stem + leaf puff.
     */
    private void placeSmallTree(int x, int y, int z) {
        for (int i = 0; i < 3; i++) {
            this.level().setBlock(new BlockPos(x, y + i, z), SKY_TREE_LOG.defaultBlockState(), 3);
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 2; dy <= 4; dy++) {
                    BlockPos p = new BlockPos(x + dx, y + dy, z + dz);
                    if (this.level().getBlockState(p).isAir()) {
                        this.level().setBlock(p, Blocks.OAK_LEAVES.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    /**
     * Gold {@code getDropItem}: Item.getItemFromBlock(MyIslandBlock) — deferred.
     */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        this.spawnAtLocation(new ItemStack(danger.orespawn.init.ModBlocks.ISLAND_BLOCK.get()));
    }

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
