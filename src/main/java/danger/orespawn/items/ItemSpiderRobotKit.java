package danger.orespawn.items;

import danger.orespawn.entity.AntRobot;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemSpiderRobotKit} — places SpiderRobot or AntRobot from a kit item.
 * <p>
 * Gold registers two items with the same class:
 * <ul>
 *   <li>{@code SpiderRobotKit} (BaseItemID+471) — max damage = SpiderRobot health (1500)</li>
 *   <li>{@code AntRobotKit} (BaseItemID+473) — max damage = AntRobot health (300); setOwned on place</li>
 * </ul>
 * Kit damage = missing health transferred from pack/unpack via {@link ItemWrench}.
 */
public class ItemSpiderRobotKit extends Item {
    public enum KitKind {
        SPIDER,
        ANT
    }

    private final KitKind kind;

    public ItemSpiderRobotKit(Properties properties, KitKind kind) {
        super(properties);
        this.kind = kind;
    }

    public KitKind getKind() {
        return this.kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockPos spawnPos = pos.above();
        double px = spawnPos.getX() + 0.5;
        double py = pos.getY() + 1.01;
        double pz = spawnPos.getZ() + 0.5;

        EntityType<?> type =
                this.kind == KitKind.ANT
                        ? ModEntities.ANT_ROBOT.get()
                        : ModEntities.SPIDER_ROBOT.get();

        Entity ent = type.create(level);
        if (ent != null) {
            ent.moveTo(px, py, pz, level.random.nextFloat() * 360.0F, 0.0F);

            if (ent instanceof LivingEntity living) {
                // gold: setHealth(maxDamage - stackDamage)
                float max = living.getMaxHealth();
                float remaining = max;
                if (stack.isDamageableItem()) {
                    remaining = Math.max(1.0F, max - stack.getDamageValue());
                }
                living.setHealth(Math.min(remaining, max));

                if (stack.has(DataComponents.CUSTOM_NAME)) {
                    living.setCustomName(stack.getHoverName());
                }
            }

            if (ent instanceof Mob mob && level instanceof ServerLevel server) {
                mob.finalizeSpawn(
                        server, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.SPAWN_EGG, null);
            }

            level.addFreshEntity(ent);

            if (ent instanceof AntRobot ant) {
                ant.setOwned();
            }

            if (player != null) {
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.GENERIC_EXPLODE.value(),
                        SoundSource.PLAYERS,
                        1.0F,
                        level.random.nextFloat() * 0.2F + 0.9F);
            }
        }

        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
