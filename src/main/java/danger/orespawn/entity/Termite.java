package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModDimensions;
import danger.orespawn.init.ModEntities;
import danger.orespawn.util.AntDimensionPortal;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold {@code Termite} extends Ant. Size 0.2×0.2, speed 0.2, health 5, attack attr 2, XP 1.
 * Eats wood (mobGriefing) and can breed nearby termites.
 * <p>
 * Gold verified: <b>no</b> {@code NearestAttackableTarget} (not a hunter like RedAnt).
 * Gold combat was Panic + rare contact nip only; port uses defensive pack / nest-only chase:
 * melee only after hurt or nest break ({@link #enrageAt} / {@link #alertColony}).
 * Gold always-on proximity nips omitted so passive termites stay non-aggressive.
 */
public class Termite extends Ant {
    private int closest = 99999;
    private int tx;
    private int ty;
    private int tz;

    public Termite(EntityType<? extends Termite> type, Level level) {
        super(type, level);
        this.moveSpeed = 0.2F;
        this.xpReward = 1;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Ant.createAttributes()
                .add(Attributes.MAX_HEALTH, 5.0) // mygetMaxHealth()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 24.0); // pack alert / chase range
    }

    @Override
    protected void registerGoals() {
        // Gold: Panic + Wander; NO NearestAttackableTarget (verified not always-hostile).
        // Port: no Panic so enraged pack fights; melee only when a target is set (hurt / nest).
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new WanderALotGoal(this, 8, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public int mygetMaxHealth() {
        return 5;
    }

    /** Call when nest is broken or a nestmate is attacked — focus the offender. */
    public void enrageAt(LivingEntity attacker) {
        if (attacker == null || !attacker.isAlive() || this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (attacker instanceof Player player && (player.isSpectator() || player.getAbilities().instabuild)) {
            return;
        }
        this.setLastHurtByMob(attacker);
        this.setTarget(attacker);
    }

    /** Alert all termites in range to attack {@code attacker} (pack defence). */
    public static void alertColony(Level level, BlockPos center, LivingEntity attacker, double range) {
        if (level.isClientSide || attacker == null) {
            return;
        }
        List<Termite> list = level.getEntitiesOfClass(
                Termite.class, new net.minecraft.world.phys.AABB(center).inflate(range));
        for (Termite t : list) {
            t.enrageAt(attacker);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hit = super.hurt(source, amount);
        if (hit && !this.level().isClientSide) {
            Entity src = source.getEntity();
            if (src instanceof LivingEntity living) {
                // Ensure pack-mates join even if HurtByTarget ticks slowly
                alertColony(this.level(), this.blockPosition(), living, 16.0);
            }
        }
        return hit;
    }

    /**
     * Gold {@code attackEntityAsMob}: 1/15 chance, 1.0 damage, skip peaceful.
     * Only reached via melee while enraged (MeleeAttackGoal requires a target).
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.random.nextInt(15) != 0) {
            return false;
        }
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        // Defensive pack / nest only: no free strikes without a set target
        if (this.getTarget() == null) {
            return false;
        }
        return target.hurt(this.damageSources().mobAttack(this), 1.0F);
    }

    private boolean isWood(BlockState state) {
        return state.is(BlockTags.LOGS)
                || state.is(BlockTags.PLANKS)
                || state.is(BlockTags.WOODEN_DOORS)
                || state.is(BlockTags.WOODEN_STAIRS)
                || state.is(BlockTags.WOODEN_SLABS)
                || state.is(BlockTags.WOODEN_FENCES)
                || state.is(BlockTags.WOODEN_TRAPDOORS)
                || state.is(BlockTags.WOODEN_BUTTONS)
                || state.is(BlockTags.WOODEN_PRESSURE_PLATES)
                || state.is(Blocks.CRAFTING_TABLE)
                || state.is(Blocks.CHEST)
                || state.is(Blocks.BOOKSHELF)
                || state.is(Blocks.JUKEBOX)
                || state.is(Blocks.NOTE_BLOCK);
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

    @Override
    protected void updateAITick() {
        if (this.isDeadOrDying()) {
            return;
        }
        if (this.random.nextInt(200) == 1) {
            this.setLastHurtByMob(null);
        }
        // gold: wood eat / breed scan only when PlayNicely == 0
        if (this.random.nextInt(200) == 1 && OreSpawnMain.PlayNicely == 0) {
            this.closest = 99999;
            this.tx = this.ty = this.tz = 0;
            for (int i = 1; i < 8; i++) {
                int j = i;
                if (j > 4) {
                    j = 4;
                }
                if (this.scanIt((int) this.getX(), (int) this.getY() + 1, (int) this.getZ(), i, j, i)) {
                    break;
                }
                if (i >= 5) {
                    i++;
                }
            }
            if (this.closest < 99999) {
                this.getNavigation().moveTo(this.tx, this.ty, this.tz, 1.0);
                if (this.closest < 6) {
                    BlockPos woodPos = new BlockPos(this.tx, this.ty, this.tz);
                    if (this.random.nextInt(3) != 0) {
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                            this.level().setBlock(woodPos, Blocks.DIRT.defaultBlockState(), 3);
                        }
                        if (this.findBuddies() < 10) {
                            spawnCreature(this.level(), this.getX() + 0.1F, this.getY() + 0.1F, this.getZ() + 0.1F);
                        }
                    } else {
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                            this.level().destroyBlock(woodPos, false, this);
                        }
                        if (this.findBuddies() < 10) {
                            spawnCreature(this.level(), this.tx + 0.1F, this.ty + 0.1F, this.tz + 0.1F);
                        }
                    }
                    this.heal(1.0F);
                }
            }
        }
        super.updateAITick();
    }

    private int findBuddies() {
        List<Termite> list = this.level().getEntitiesOfClass(Termite.class, this.getBoundingBox().inflate(3.0, 3.0, 3.0));
        return list.size();
    }

    private static void spawnCreature(Level level, double x, double y, double z) {
        Termite entity = ModEntities.TERMITE.get().create(level);
        if (entity != null) {
            entity.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(entity);
        }
    }

    /**
     * 1.7.10 Termite: empty hand → Crystal; enter requires empty inventory + no armor.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player == null) {
            return InteractionResult.FAIL;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getCount() <= 0) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            stack = ItemStack.EMPTY;
        }
        if (!stack.isEmpty()) {
            return InteractionResult.PASS;
        }
        boolean inCrystal = serverPlayer.level().dimension() == ModDimensions.CRYSTAL;
        if (!inCrystal && !AntDimensionPortal.inventoryEmptyForCrystal(serverPlayer)) {
            serverPlayer.displayClientMessage(
                    Component.literal("Empty your inventory and armor to enter the Crystal Dimension!"), true);
            return InteractionResult.SUCCESS;
        }
        AntDimensionPortal.tryToggle(
                player, stack, ModDimensions.CRYSTAL, "Warped to the Crystal Dimension.");
        return InteractionResult.SUCCESS;
    }
}
