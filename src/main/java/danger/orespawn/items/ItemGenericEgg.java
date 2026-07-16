package danger.orespawn.items;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Gold {@code ItemGenericEgg} — right-click block to spawn linked entity above it.
 */
public class ItemGenericEgg extends Item {
    private final Supplier<? extends EntityType<?>> entityType;

    public ItemGenericEgg(Properties properties, Supplier<? extends EntityType<?>> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos spawnPos = pos.relative(face);
        EntityType<?> type = this.entityType.get();
        Entity entity = type.create(level);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        entity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof Mob mob && level instanceof ServerLevel server) {
            mob.finalizeSpawn(server, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.SPAWN_EGG, null);
            mob.playAmbientSound();
            mob.setPersistenceRequired();
        }
        level.addFreshEntity(entity);
        level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, spawnPos);
        if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
