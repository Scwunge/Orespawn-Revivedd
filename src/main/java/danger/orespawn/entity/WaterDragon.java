package danger.orespawn.entity;

import danger.orespawn.init.ModItems;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.entity.tame.OreSpawnPet;
import danger.orespawn.util.ai.FollowOwnerGoal;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code WaterDragon} (EntityTameable) 1:1 for NeoForge 1.21.1.
 * Size 1.25Ã—1.9, speed 0.25 land / 0.55 water, health 150, attack 20, armor 8, XP 100.
 * Tame food: cod (1/3); release cobweb; sit toggle; name tag.
 * Water-seek when dry; melee + watercanon (WaterBall deferred â†’ SmallFireball stand-in);
 * fire immune; can breathe underwater; jaw anim via attacking 0/1/2.
 */
public class WaterDragon extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(WaterDragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> PET_FLAGS =
            SynchedEntityData.defineId(WaterDragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(WaterDragon.class, EntityDataSerializers.OPTIONAL_UUID);

    /** Gold land speed; overridden in water via aiStep. */
    private float moveSpeed = 0.25F;
    private int streamCount;
    private int hurtTimer;
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;
    private final RenderInfo renderdata = new RenderInfo();

    public WaterDragon(EntityType<? extends WaterDragon> type, Level level) {
        super(type, level);
        this.xpReward = 100; // gold field_70728_aV
    }

    public static AttributeSupplier.Builder createAttributes() {
        // gold WaterDragon_stats default: health 150, attack 20, defense 8
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.FOLLOW_RANGE, 16.0); // combat scan 14; gold field_70174_ab=3 (render track)
    }

    /** Gold was EntityTameable, not EntityMob â€” never wipe on Peaceful. */
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
        builder.define(PET_FLAGS, (byte) 0);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        // gold: Swimming, Mate(skipped), FollowOwner 2.0/10/2, Tempt fish, WanderALot(16,1),
        // WatchClosest 8, LookIdle, HurtByTarget
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(
                2,
                new FollowOwnerGoal(
                        this,
                        () -> OreSpawnPet.getOwnerUUID(this, OWNER),
                        () -> OreSpawnPet.isSitting(this, PET_FLAGS),
                        2.0,
                        10.0F,
                        2.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2, Ingredient.of(Items.COD), false));
        this.goalSelector.addGoal(4, new WanderALotGoal(this, 16, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    /** Gold field_70178_ae isImmuneToFire. */
    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold canDespawn: baby â†’ persist; noDespawnRequired â†’ false; else !tamed
        if (this.isBaby()) {
            this.setPersistenceRequired();
            return false;
        }
        if (this.isPersistenceRequired()) {
            return false;
        }
        return !OreSpawnPet.isTame(this, PET_FLAGS);
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        if (OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.getNavigation().stop();
        }
        // gold canBreatheUnderwater=true â€” LivingEntity method is final in 1.21
        this.setAirSupply(this.getMaxAirSupply());
        super.tick();
    }

    /** Gold livingUpdate: land 0.25 / water 0.55 moveSpeed. */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isInWater()) {
            this.moveSpeed = 0.55F;
        } else {
            this.moveSpeed = 0.25F;
        }
    }

    public int mygetMaxHealth() {
        return 150;
    }

    public int getWaterDragonHealth() {
        return (int) this.getHealth();
    }

    public RenderInfo getRenderInfo() {
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        this.renderdata.rf1 = r.rf1;
        this.renderdata.rf2 = r.rf2;
        this.renderdata.rf3 = r.rf3;
        this.renderdata.rf4 = r.rf4;
        this.renderdata.ri1 = r.ri1;
        this.renderdata.ri2 = r.ri2;
        this.renderdata.ri3 = r.ri3;
        this.renderdata.ri4 = r.ri4;
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    public boolean isOreSpawnTame() {
        return OreSpawnPet.isTame(this, PET_FLAGS);
    }

    public boolean isOreSpawnSitting() {
        return OreSpawnPet.isSitting(this, PET_FLAGS);
    }

    /** Gold isWheat: fish. Breed food CrystalApple â†’ golden apple stand-in. */
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(Items.COD) || stack.is(Items.GOLDEN_APPLE));
    }

    private static boolean isTameFood(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Items.COD);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isTameFood(stack)) {
            ItemStack other = OreSpawnPet.findFoodInHands(player, WaterDragon::isTameFood);
            if (!other.isEmpty()) {
                stack = other;
            }
        }

        // gold distanceSq < 25 for most interact; name tag < 16 handled below
        if (player.distanceToSqr(this) > 25.0 && !stack.is(Items.NAME_TAG)) {
            return InteractionResult.PASS;
        }

        // gold: fish tame 1/3 / heal owner
        if (isTameFood(stack) && player.distanceToSqr(this) < 25.0) {
            if (!OreSpawnPet.isTame(this, PET_FLAGS)) {
                if (!this.level().isClientSide) {
                    if (OreSpawnPet.tryTameWithFeedback(
                            this,
                            PET_FLAGS,
                            OWNER,
                            player,
                            3,
                            "WaterDragon is now yours!",
                            "WaterDragon sniffs the fishâ€¦ try again!")) {
                        this.heal(this.mygetMaxHealth() - this.getHealth());
                    }
                }
            } else if (OreSpawnPet.isOwnedBy(this, OWNER, player)) {
                if (this.level().isClientSide) {
                    this.level().broadcastEntityEvent(this, (byte) 7);
                }
                if (this.mygetMaxHealth() > this.getHealth()) {
                    this.heal(this.mygetMaxHealth() - this.getHealth());
                }
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (OreSpawnPet.isTame(this, PET_FLAGS) && OreSpawnPet.isOwnedBy(this, OWNER, player)) {
            // gold: cobweb releases pet (distance < 25)
            if (stack.is(Items.COBWEB) && player.distanceToSqr(this) < 25.0) {
                if (!this.level().isClientSide) {
                    OreSpawnPet.setTame(this, PET_FLAGS, false);
                    OreSpawnPet.setOwnerUUID(this, OWNER, null);
                    OreSpawnPet.setSitting(this, PET_FLAGS, false);
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: name tag distanceSq < 16
            if (stack.is(Items.NAME_TAG) && player.distanceToSqr(this) < 16.0) {
                if (!this.level().isClientSide) {
                    this.setCustomName(stack.getHoverName());
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            // gold: empty / other â†’ toggle sit
            if (player.distanceToSqr(this) < 25.0) {
                if (!this.level().isClientSide) {
                    OreSpawnPet.setSitting(this, PET_FLAGS, !OreSpawnPet.isSitting(this, PET_FLAGS));
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.HEART,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else if (id == 6) {
            for (int i = 0; i < 7; i++) {
                double dx = this.random.nextGaussian() * 0.02;
                double dy = this.random.nextGaussian() * 0.02;
                double dz = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(
                                ParticleTypes.SMOKE,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                dx,
                                dy,
                                dz);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("WaterDragonAttacking", (byte) this.getAttacking());
        OreSpawnPet.addAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WaterDragonAttacking")) {
            this.setAttacking(tag.getByte("WaterDragonAttacking"));
        }
        OreSpawnPet.readAdditionalSaveData(this, PET_FLAGS, OWNER, tag);
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            this.setPersistenceRequired();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // gold orespawn:waterdragon_hurt (ogg present; SoundsHandler wiring deferred)
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // gold orespawn:waterdragon_death
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: WaterDragonScale, 1 name_tag, 9+r6 cod, random gear
        this.spawnAtLocation(new ItemStack(ModItems.WATER_DRAGON_SCALE.get()));
        this.spawnAtLocation(new ItemStack(Items.NAME_TAG));
        int fish = 9 + this.random.nextInt(6);
        for (int i = 0; i < fish; i++) {
            this.spawnAtLocation(new ItemStack(Items.COD));
        }
        int roll = this.random.nextInt(20);
        switch (roll) {
            case 0 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_AXE));
            case 1 -> this.spawnAtLocation(new ItemStack(Items.IRON_INGOT));
            case 2 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_PICKAXE));
            case 3 -> this.spawnAtLocation(new ItemStack(Items.IRON_SWORD));
            case 4 -> this.spawnAtLocation(new ItemStack(Items.IRON_SHOVEL));
            case 5 -> this.spawnAtLocation(new ItemStack(Items.IRON_PICKAXE));
            case 6 -> this.spawnAtLocation(new ItemStack(Items.IRON_AXE));
            case 7 -> this.spawnAtLocation(new ItemStack(Items.BOW));
            case 8 -> this.spawnAtLocation(new ItemStack(Items.IRON_HELMET));
            case 9 -> this.spawnAtLocation(new ItemStack(Items.IRON_CHESTPLATE));
            case 10 -> this.spawnAtLocation(new ItemStack(Items.IRON_LEGGINGS));
            case 11 -> this.spawnAtLocation(new ItemStack(Items.IRON_BOOTS));
            case 12 -> this.spawnAtLocation(new ItemStack(Items.DIAMOND_SHOVEL));
            case 13 -> this.spawnAtLocation(new ItemStack(Blocks.GOLD_BLOCK));
            default -> {
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // never attack owner
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold: WaterDragon_stats.attack via attribute
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity) {
            double ks = 1.1;
            double inair = 0.14;
            float f3 = (float) Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            if (!target.isAlive() || target instanceof Player) {
                inair *= 2.0;
            }
            target.push(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if ("cactus".equals(source.getMsgId())) {
            return false;
        }
        Entity e = source.getEntity();
        if (e instanceof WaterDragon) {
            return false;
        }
        if (e instanceof AttackSquid) {
            return false;
        }
        // gold WaterBall immunity â€” projectile not ported

        boolean ret = false;
        if (this.hurtTimer <= 0) {
            ret = super.hurt(source, amount);
            this.hurtTimer = 10;
        }

        if (e instanceof LivingEntity living
                && !(e instanceof AttackSquid)
                && !(e instanceof WaterDragon)) {
            this.setTarget(living);
            this.setLastHurtByMob(living);
            this.getNavigation().moveTo(living, 1.2);
        }
        return ret;
    }

    private boolean isWaterBlock(BlockState state) {
        return state.getFluidState().is(FluidTags.WATER) || state.is(Blocks.WATER);
    }

    /** Gold {@code scan_it} for water / flowing water. */
    private boolean scanWater(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;
        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
                if (this.isWaterBlock(bid)) {
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
     * Gold {@code updateAITasks}: hurt timer; water seek when dry+not sitting; combat 1/5;
     * melee (4+w/2)^2; watercanon when out of range; heal 1/100 when in water.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }

        if (this.hurtTimer > 0) {
            this.hurtTimer--;
        }

        // gold: seek water when dry and not sitting
        if (!this.isInWater() && this.random.nextInt(25) == 0 && !OreSpawnPet.isSitting(this, PET_FLAGS)) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 12; i++) {
                int j = i;
                if (j > 10) {
                    j = 10;
                }
                if (this.scanWater((int) this.getX(), (int) this.getY() - 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty - 1, this.tz, 1.33);
            } else {
                if (this.random.nextInt(50) == 1) {
                    // gold heal(-1.0F)
                    this.hurt(this.damageSources().generic(), 1.0F);
                }
                if (this.getHealth() <= 0.0F) {
                    this.discard();
                    return;
                }
            }
        }

        if (this.random.nextInt(200) == 0) {
            this.setTarget(null);
        }

        if (this.level().getDifficulty() != Difficulty.PEACEFUL && this.random.nextInt(5) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                double reach = 4.0F + e.getBbWidth() / 2.0F;
                if (this.distanceToSqr(e) < reach * reach) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.0);
                    this.watercanon(e);
                }
            } else {
                this.setAttacking(0);
            }
        }

        if (this.random.nextInt(100) == 1
                && this.isInWater()
                && this.getHealth() < this.mygetMaxHealth()) {
            this.playSound(
                    SoundEvents.GENERIC_SPLASH,
                    1.5F,
                    this.level().getRandom().nextFloat() * 0.2F + 0.9F);
            this.heal(1.0F);
        }
    }

    /**
     * Gold {@code watercanon}: stream of WaterBall + rare SmallFireball; attacking=2 while
     * streaming. WaterBall entity not ported â€” SmallFireball used as stand-in for stream hits.
     */
    private void watercanon(LivingEntity e) {
        double yoff = 1.75;
        double xzoff = 1.5;
        if (this.streamCount > 0) {
            this.setAttacking(2);
            // gold rare fireball 1/15 + every-tick WaterBall (stand-in: SmallFireball each stream)
            Vec3 dir = new Vec3(
                    e.getX() - this.getX(),
                    e.getY() + 0.75 - (this.getY() + yoff),
                    e.getZ() - this.getZ());
            if (dir.lengthSqr() > 1.0E-6) {
                dir = dir.normalize();
            }
            double sx = this.getX() - xzoff * Math.sin(Math.toRadians(this.yBodyRot));
            double sy = this.getY() + yoff;
            double sz = this.getZ() + xzoff * Math.cos(Math.toRadians(this.yBodyRot));
            SmallFireball ball = new SmallFireball(this.level(), this, dir);
            ball.setPos(sx, sy, sz);
            this.playSound(
                    SoundEvents.BLAZE_SHOOT,
                    0.75F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(ball);
            this.streamCount--;
        } else {
            this.setAttacking(0);
        }

        if (this.streamCount <= 0 && this.random.nextInt(4) == 1) {
            this.streamCount = 8;
        }
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
        if (target instanceof WaterDragon) {
            return false;
        }
        if (OreSpawnPet.isOwnedBy(this, OWNER, target)) {
            return false;
        }
        // gold: EntityMob always valid
        if (target instanceof Monster) {
            return true;
        }
        // gold: if tamed, do not hunt players / non-mobs
        if (OreSpawnPet.isTame(this, PET_FLAGS)) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild && !p.isSpectator();
        }
        // gold MyUtils.isAttackableNonMob â€” simplified: no extra non-mob targets
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        if (this.isBaby()) {
            return null;
        }
        LivingEntity current = this.getTarget();
        if (current != null && current.isAlive()) {
            return current;
        }
        this.setTarget(null);

        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(14.0, 4.0, 14.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: yâ‰¥50, daytime, no peer within expand(16,5,16);
     * spawner "Water Dragon" bypass not fully ported (always true near spawn egg / dim).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.SPAWN_EGG) {
            return true;
        }
        if (this.getY() < 50.0) {
            return false;
        }
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        List<WaterDragon> peers = level.getEntitiesOfClass(
                WaterDragon.class, this.getBoundingBox().inflate(16.0, 5.0, 16.0));
        return peers.isEmpty();
    }
}
