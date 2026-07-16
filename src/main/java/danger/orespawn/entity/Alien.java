package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModBlocks;
import danger.orespawn.util.ai.GoldStyleCombat;
import danger.orespawn.util.ai.WanderALotGoal;
import danger.orespawn.util.handlers.SoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code Alien} (EntityMob) 1:1 combat/AI for NeoForge 1.21.1.
 * Size 1.1×3.25, speed 0.65, health 100, attack 12, armor 8, XP 100.
 * No MeleeAttackGoal — proximity combat in {@link #customServerAiStep()}.
 */
public class Alien extends Monster {
    private static final EntityDataAccessor<Byte> ATTACKING =
            SynchedEntityData.defineId(Alien.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.65F;
    private int hurtTimer = 0;
    /** Gold client animation state for {@code ModelAlien}. */
    private RenderInfo renderdata = new RenderInfo();

    /** Gold torch-hunt scan state ({@code closest}/{@code tx,ty,tz}). */
    private int closest = 99999;
    private int tx = 0;
    private int ty = 0;
    private int tz = 0;

    public Alien(EntityType<? extends Alien> type, Level level) {
        super(type, level);
        this.xpReward = 100;
    }

    public RenderInfo getRenderInfo() {
        return this.renderdata;
    }

    public void setRenderInfo(RenderInfo r) {
        this.renderdata = r;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.65)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.ARMOR, 8.0) // func_70658_aO
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // gold: EntityAIMoveThroughVillage — skipped (no village path helper)
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 10, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        // gold: no MeleeAttackGoal — combat is customServerAiStep proximity
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, (byte) 0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    /** Gold {@code jump()} / {@code func_70664_aZ}: extra +0.25 Y. */
    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.25, 0.0));
    }

    /**
     * Gold {@code onLivingUpdate}: client dripping-lava particles from "mouth".
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            float f = 1.7F + Math.abs(this.random.nextFloat() * 0.75F);
            if (this.random.nextInt(20) == 1) {
                double yaw = Math.toRadians(this.yBodyRot);
                this.level().addParticle(
                        ParticleTypes.DRIPPING_LAVA,
                        this.getX() - f * Math.sin(yaw),
                        this.getY() + 1.6,
                        this.getZ() + f * Math.cos(yaw),
                        0.0,
                        0.0,
                        0.0);
            }
        }
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
    }

    public int mygetMaxHealth() {
        return 100;
    }

    public int getAlienHealth() {
        return (int) this.getHealth();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? SoundsHandler.ENTITY_ALIEN_LIVING.get() : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsHandler.ENTITY_ALIEN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        // alien/death.ogg + sounds.json entity.alien.death (gold left default death)
        return SoundsHandler.ENTITY_ALIEN_DEATH.get();
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
        // gold: 5+rand(6) spider_eye, 5+rand(6) flint, map, clock, compass
        int n = 5 + this.random.nextInt(6);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.SPIDER_EYE));
        }
        n = 5 + this.random.nextInt(6);
        for (int i = 0; i < n; i++) {
            this.spawnAtLocation(new ItemStack(Items.FLINT));
        }
        this.spawnAtLocation(new ItemStack(Items.MAP));
        this.spawnAtLocation(new ItemStack(Items.CLOCK));
        this.spawnAtLocation(new ItemStack(Items.COMPASS));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // Gold attackEntityAsMob always applied damage; 1.21 super.doHurtTarget can no-op without MeleeAttackGoal ticks.
        if (!GoldStyleCombat.dealAttributeDamage(this, target)) {
            return false;
        }
        if (target instanceof LivingEntity living) {
            // gold difficulty poison duration (nested EASY/NORMAL/HARD in gold; intent here)
            int duration = 6 * 5;
            switch (this.level().getDifficulty()) {
                case EASY -> duration = 8 * 5;
                case NORMAL -> duration = 10 * 5;
                case HARD -> duration = 12 * 5;
                default -> {
                }
            }
            if (this.random.nextInt(5) == 1) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 0));
            }
            double ks = 1.1;
            double inair = 0.1;
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
        boolean ret = false;
        if (this.hurtTimer <= 0) {
            ret = super.hurt(source, amount);
        }
        Entity e = source.getEntity();
        // gold: EntityLiving attacker → set attack target + path at 1.2
        if (e instanceof LivingEntity living) {
            this.setTarget(living);
            this.getNavigation().moveTo(living, 1.2);
            ret = true;
        }
        return ret;
    }

    /**
     * Gold {@code scan_it}: shell-scan for torches / extreme_torch.
     * Matches gold loop structure (Z faces only check vanilla torch, not extreme).
     */
    private boolean scanIt(int x, int y, int z, int dx, int dy, int dz) {
        int found = 0;

        for (int i = -dy; i <= dy; i++) {
            for (int j = -dz; j <= dz; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + dx, y + i, z + j));
                if (isTorchLike(bid, true)) {
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
                if (isTorchLike(bid, true)) {
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
                if (isTorchLike(bid, true)) {
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
                if (isTorchLike(bid, true)) {
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

        // gold Z-face loops only match Blocks.TORCH (not EXTREME_TORCH)
        for (int xi = -dx; xi <= dx; xi++) {
            for (int j = -dy; j <= dy; j++) {
                BlockState bid = this.level().getBlockState(new BlockPos(x + xi, y + j, z + dz));
                if (isTorchLike(bid, false)) {
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
                if (isTorchLike(bid, false)) {
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

    /** {@code includeExtreme} mirrors gold: X/Y faces include extreme_torch; Z faces do not. */
    private static boolean isTorchLike(BlockState state, boolean includeExtreme) {
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) {
            return true;
        }
        // 1.21 also has soul torches; gold only had Blocks.TORCH — keep vanilla torch only for 1:1
        return includeExtreme && state.is(ModBlocks.EXTREME_TORCH.get());
    }

    /**
     * Gold {@code updateAITasks} / {@code func_70619_bc}:
     * 1/8 combat tick, else 1/30 torch hunt+grief, heal 1/40 when damaged.
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

        // gold: nextInt(8) == 0 → proximity combat (no MeleeAttackGoal)
        if (this.random.nextInt(8) == 0) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                // gold: getDistanceSq < 16.0 (reach ~4)
                if (this.distanceToSqr(e) < 16.0) {
                    this.setAttacking(1);
                    if (this.random.nextInt(4) == 0 || this.random.nextInt(5) == 1) {
                        this.doHurtTarget(e);
                    }
                }
                this.getNavigation().moveTo(e, 1.2);
            } else {
                this.setAttacking(0);
            }
        } else if (this.random.nextInt(30) == 0 && OreSpawnMain.PlayNicely == 0) {
            // gold: torch hunt when not on combat check tick && PlayNicely == 0
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 2; i < 15 && !this.scanIt((int) this.getX(), (int) this.getY(), (int) this.getZ(), i, i, i); i++) {
                if (i >= 10) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                // gold: closest < 27 && mobGriefing → destroy torch
                if (this.closest < 27 && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level().setBlock(new BlockPos(this.tx, this.ty, this.tz), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        // gold heal 1 every ~40 ticks when damaged
        if (this.random.nextInt(40) == 1 && this.getHealth() < this.mygetMaxHealth()) {
            this.heal(1.0F);
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        // gold: players only for new scan targets
        if (target instanceof Player player) {
            return !player.isSpectator() && !player.getAbilities().instabuild;
        }
        return false;
    }

    /**
     * Gold {@code findSomethingToAttack}: keep current attack target if alive
     * (including retaliatory non-players from hurt), else scan players in 12×4×12.
     */
    @Nullable
    private LivingEntity findSomethingToAttack() {
        // gold: PlayNicely != 0 → null
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        LivingEntity current = this.getTarget();
        if (current != null && current.isAlive()) {
            return current;
        }
        this.setTarget(null);
        return GoldStyleCombat.findTarget(this, 12.0, 4.0, this::isSuitableTarget);
    }

    public int getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(int value) {
        this.entityData.set(ATTACKING, (byte) value);
    }

    /**
     * Gold {@code getCanSpawnHere}: valid light (via super), y ≤ 50,
     * 3×3 air column at y+1.y+3 (offsets j/k in −1.1).
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (!super.checkSpawnRules(level, spawnType)) {
            return false;
        }
        if (this.getY() > 50.0) {
            return false;
        }
        int baseX = (int) this.getX();
        int baseY = (int) this.getY();
        int baseZ = (int) this.getZ();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int k = -1; k < 2; k++) {
            for (int j = -1; j < 2; j++) {
                for (int i = 1; i < 4; i++) {
                    cursor.set(baseX + j, baseY + i, baseZ + k);
                    if (!level.getBlockState(cursor).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
