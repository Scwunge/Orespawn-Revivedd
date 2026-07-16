package danger.orespawn.items;

import danger.orespawn.entity.EntityCage;
import danger.orespawn.init.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code CritterCage} — empty (entityType null) captures on impact;
 * filled throws and releases {@code entityType} on impact (see {@link EntityCage}),
 * or places/releases when used on a block (gold onItemUse path).
 * <p>
 * Entity type is resolved lazily via {@link Supplier} so registry order never bakes a null type
 * (which would make natural spawns uncapturable).
 */
public class CritterCage extends Item {
    private static final List<CritterCage> CRITTER_CAGES = new ArrayList<>();

    /** Null supplier / empty cage = capture projectile. */
    @Nullable
    private final Supplier<? extends EntityType<?>> entityType;
    private final float chance;

    public CritterCage(Properties properties, @Nullable Supplier<? extends EntityType<?>> entityType) {
        this(properties, entityType, 1.0F);
    }

    public CritterCage(
            Properties properties, @Nullable Supplier<? extends EntityType<?>> entityType, float chance) {
        super(properties);
        this.entityType = entityType;
        this.chance = chance;
        CRITTER_CAGES.add(this);
    }

    public float getChance() {
        return this.chance;
    }

    @Nullable
    public EntityType<?> getEntityType() {
        if (this.entityType == null) {
            return null;
        }
        try {
            return this.entityType.get();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Gold {@code onItemUse} / block right-click:
     * <ul>
     *   <li>Empty cage → PASS so {@link #use} throws the projectile.</li>
     *   <li>Filled cage → release entity at block (particles, sound, empty cage drop).</li>
     * </ul>
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        EntityType<?> type = this.getEntityType();
        // Empty cage always throws
        if (type == null) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos position = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        double px = position.getX() + 0.5;
        double py = position.getY() + 1.25;
        double pz = position.getZ() + 0.5;
        for (int i = 0; i < 6; i++) {
            spawnParticle(level, ParticleTypes.LARGE_SMOKE, px, py, pz);
            spawnParticle(level, ParticleTypes.EXPLOSION_EMITTER, px, py, pz);
            spawnParticle(level, DustParticleOptions.REDSTONE, px, py, pz);
        }

        level.playSound(
                null,
                position,
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.5F);

        if (!level.isClientSide) {
            Entity summon = type.create(level);
            if (summon != null) {
                summon.moveTo(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5, 0.0F, 0.0F);
                if (summon instanceof Horse horse) {
                    horse.setVariant(Variant.byId(level.random.nextInt(7)));
                }
                if (summon instanceof LivingEntity && stack.has(DataComponents.CUSTOM_NAME)) {
                    summon.setCustomName(stack.getHoverName());
                }
                level.addFreshEntity(summon);
            }

            ItemEntity empty = new ItemEntity(
                    level,
                    position.getX(),
                    position.getY(),
                    position.getZ(),
                    new ItemStack(ModItems.EMPTY_CAGE.get()));
            level.addFreshEntity(empty);
        }

        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        EntityType<?> type = this.getEntityType();

        if (!level.isClientSide) {
            String name = null;
            if (type != null && stack.has(DataComponents.CUSTOM_NAME)) {
                name = stack.getHoverName().getString();
            }
            EntityCage ec = new EntityCage(level, player, type, name);
            ec.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(ec);
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.PLAYERS,
                1.0F,
                1.5F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    /**
     * Find the filled cage item for this living entity.
     * Matches by registry id (not instance ==) so natural and egg spawns both work.
     */
    @Nullable
    public static CritterCage getCageFromEntity(Entity e) {
        if (e == null) {
            return null;
        }
        EntityType<?> hitType = e.getType();
        ResourceLocation hitId = BuiltInRegistries.ENTITY_TYPE.getKey(hitType);

        for (CritterCage cc : CRITTER_CAGES) {
            EntityType<?> cageType = cc.getEntityType();
            if (cageType == null) {
                continue;
            }
            // Prefer identity, then registry key equality (safe across reloads)
            if (cageType == hitType) {
                return cc;
            }
            ResourceLocation cageId = BuiltInRegistries.ENTITY_TYPE.getKey(cageType);
            if (hitId != null && hitId.equals(cageId)) {
                return cc;
            }
        }
        return null;
    }

    /** True if any registered cage can capture this entity type. */
    public static boolean canCapture(Entity e) {
        return getCageFromEntity(e) != null;
    }

    private static void spawnParticle(
            Level level,
            net.minecraft.core.particles.ParticleOptions particle,
            double x,
            double y,
            double z) {
        if (level instanceof ServerLevel server) {
            server.sendParticles(particle, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        } else {
            level.addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
