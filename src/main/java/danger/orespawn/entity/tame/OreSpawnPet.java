package danger.orespawn.entity.tame;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Lightweight pet helpers for gold {@code EntityTameable} ports that still extend
 * {@link Mob}/{@link net.minecraft.world.entity.monster.Monster}.
 * <p>
 * Each entity must define its own synched accessors with
 * {@code SynchedEntityData.defineId(ThatClass.class, .)} and pass them in.
 */
public final class OreSpawnPet {
    private OreSpawnPet() {}

    private static final byte FLAG_TAME = 1;
    private static final byte FLAG_SITTING = 2;

    public static boolean isTame(Mob mob, EntityDataAccessor<Byte> flags) {
        return (mob.getEntityData().get(flags) & FLAG_TAME) != 0;
    }

    public static boolean isSitting(Mob mob, EntityDataAccessor<Byte> flags) {
        return (mob.getEntityData().get(flags) & FLAG_SITTING) != 0;
    }

    public static void setTame(Mob mob, EntityDataAccessor<Byte> flags, boolean tame) {
        byte f = mob.getEntityData().get(flags);
        f = tame ? (byte) (f | FLAG_TAME) : (byte) (f & ~FLAG_TAME);
        // force=true so clients always get the tame flag update
        mob.getEntityData().set(flags, f, true);
    }

    public static void setSitting(Mob mob, EntityDataAccessor<Byte> flags, boolean sitting) {
        byte f = mob.getEntityData().get(flags);
        f = sitting ? (byte) (f | FLAG_SITTING) : (byte) (f & ~FLAG_SITTING);
        mob.getEntityData().set(flags, f, true);
        if (sitting) {
            mob.getNavigation().stop();
        }
    }

    @Nullable
    public static UUID getOwnerUUID(Mob mob, EntityDataAccessor<Optional<UUID>> ownerAcc) {
        return mob.getEntityData().get(ownerAcc).orElse(null);
    }

    public static void setOwnerUUID(Mob mob, EntityDataAccessor<Optional<UUID>> ownerAcc, @Nullable UUID uuid) {
        mob.getEntityData().set(ownerAcc, Optional.ofNullable(uuid), true);
    }

    public static void tame(
            Mob mob, EntityDataAccessor<Byte> flags, EntityDataAccessor<Optional<UUID>> ownerAcc, Player player) {
        setTame(mob, flags, true);
        setOwnerUUID(mob, ownerAcc, player.getUUID());
        setSitting(mob, flags, false);
        mob.setPersistenceRequired();
        if (!mob.level().isClientSide) {
            mob.level().broadcastEntityEvent(mob, (byte) 7);
        }
    }

    public static void addAdditionalSaveData(
            Mob mob, EntityDataAccessor<Byte> flags, EntityDataAccessor<Optional<UUID>> ownerAcc, CompoundTag tag) {
        tag.putBoolean("OreSpawnTame", isTame(mob, flags));
        tag.putBoolean("OreSpawnSitting", isSitting(mob, flags));
        UUID id = getOwnerUUID(mob, ownerAcc);
        if (id != null) {
            tag.putUUID("OreSpawnOwner", id);
        }
    }

    public static void readAdditionalSaveData(
            Mob mob, EntityDataAccessor<Byte> flags, EntityDataAccessor<Optional<UUID>> ownerAcc, CompoundTag tag) {
        setTame(mob, flags, tag.getBoolean("OreSpawnTame"));
        setSitting(mob, flags, tag.getBoolean("OreSpawnSitting"));
        if (tag.hasUUID("OreSpawnOwner")) {
            setOwnerUUID(mob, ownerAcc, tag.getUUID("OreSpawnOwner"));
        }
    }

    /**
     * Gold-style random tame. Creative/spectator-creative always succeeds (vanilla wolf QoL).
     * Survival uses {@code nextInt(chanceDenom) == 0} (denom 2 = 50% like gold Spyro).
     *
     * @return true if newly tamed this call
     */
    public static boolean tryTame(
            Mob mob,
            EntityDataAccessor<Byte> flags,
            EntityDataAccessor<Optional<UUID>> ownerAcc,
            Player player,
            int chanceDenom) {
        if (mob.level().isClientSide || isTame(mob, flags)) {
            return false;
        }
        boolean creative = player.getAbilities().instabuild;
        boolean success = creative || mob.getRandom().nextInt(Math.max(1, chanceDenom)) == 0;
        if (success) {
            tame(mob, flags, ownerAcc, player);
            return true;
        }
        mob.level().broadcastEntityEvent(mob, (byte) 6);
        return false;
    }

    /** Server-only: try tame + optional heal + chat feedback. */
    public static boolean tryTameWithFeedback(
            Mob mob,
            EntityDataAccessor<Byte> flags,
            EntityDataAccessor<Optional<UUID>> ownerAcc,
            Player player,
            int chanceDenom,
            String successMsg,
            String failMsg) {
        if (mob.level().isClientSide) {
            return false;
        }
        if (isTame(mob, flags)) {
            return false;
        }
        if (tryTame(mob, flags, ownerAcc, player, chanceDenom)) {
            if (successMsg != null && !successMsg.isEmpty()) {
                player.displayClientMessage(Component.literal(successMsg), true);
            }
            return true;
        }
        if (failMsg != null && !failMsg.isEmpty()) {
            player.displayClientMessage(Component.literal(failMsg), true);
        }
        return false;
    }

    public static boolean isOwnedBy(Mob mob, EntityDataAccessor<Optional<UUID>> ownerAcc, Entity other) {
        UUID id = getOwnerUUID(mob, ownerAcc);
        return id != null && other != null && id.equals(other.getUUID());
    }

    @Nullable
    public static LivingEntity getOwner(Mob mob, EntityDataAccessor<Optional<UUID>> ownerAcc) {
        UUID id = getOwnerUUID(mob, ownerAcc);
        if (id == null) {
            return null;
        }
        return mob.level().getPlayerByUUID(id);
    }

    /** True if either hand holds a matching item (main preferred). */
    public static ItemStack findFoodInHands(Player player, java.util.function.Predicate<ItemStack> foodTest) {
        ItemStack main = player.getMainHandItem();
        if (foodTest.test(main)) {
            return main;
        }
        ItemStack off = player.getOffhandItem();
        if (foodTest.test(off)) {
            return off;
        }
        return ItemStack.EMPTY;
    }
}
