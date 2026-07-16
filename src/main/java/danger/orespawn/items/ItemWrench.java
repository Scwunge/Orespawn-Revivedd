package danger.orespawn.items;

import danger.orespawn.entity.AntRobot;
import danger.orespawn.entity.SpiderRobot;
import danger.orespawn.util.Reference;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemWrench} — packs SpiderRobot / AntRobot into kit items.
 * <p>
 * Gold: creative tab tools, durability 100. {@code onLeftClickEntity}:
 * <ul>
 *   <li>SpiderRobot with no rider → discard + drop SpiderRobotKit (damage = missing HP)</li>
 *   <li>AntRobot with no rider: unowned only if HP ≤ 50%; then setOwned + pack AntRobotKit</li>
 * </ul>
 * Damages wrench by 2 per successful pack.
 */
public class ItemWrench extends Item {
    /** Gold {@code setMaxDamage(100)}. */
    public static final int WRENCH_USES = 100;

    private static final ResourceLocation SPIDER_KIT_ID =
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spider_robot_kit");
    private static final ResourceLocation ANT_KIT_ID =
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ant_robot_kit");

    public ItemWrench(Properties properties) {
        super(properties);
    }

    /**
     * NeoForge hook matching gold {@code onLeftClickEntity}.
     * Return true to cancel the default attack.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();

        if (entity instanceof SpiderRobot spider && entity.getPassengers().isEmpty()) {
            float missing = spider.getMaxHealth() - spider.getHealth();
            packFx(level, entity);
            if (!level.isClientSide) {
                spider.discard();
                dropKit(level, spider, SPIDER_KIT_ID, (int) missing);
            }
            hurtWrench(stack, player);
            return true;
        }

        if (entity instanceof AntRobot ant && entity.getPassengers().isEmpty()) {
            if (ant.getOwned() == 0) {
                // gold: only pack wild ants at ≤ half health, then mark owned
                if (ant.getHealth() / ant.getMaxHealth() > 0.5F) {
                    return false;
                }
                ant.setOwned();
            }
            float missing = ant.getMaxHealth() - ant.getHealth();
            packFx(level, entity);
            if (!level.isClientSide) {
                ant.discard();
                dropKit(level, ant, ANT_KIT_ID, (int) missing);
            }
            hurtWrench(stack, player);
            return true;
        }

        return false;
    }

    private static void hurtWrench(ItemStack stack, Player player) {
        InteractionHand hand =
                player.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        stack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(hand));
    }

    private void dropKit(Level level, LivingEntity e, ResourceLocation kitId, int damage) {
        Item kitItem = BuiltInRegistries.ITEM.getOptional(kitId).orElse(Items.AIR);
        if (kitItem == Items.AIR) {
            return; // entity type not registered yet
        }
        ItemStack is = new ItemStack(kitItem, 1);
        if (is.isDamageableItem()) {
            int max = Math.max(is.getMaxDamage() - 1, 0);
            is.setDamageValue(Math.min(Math.max(damage, 0), max));
        }
        level.addFreshEntity(new ItemEntity(level, e.getX(), e.getY() + 1.0, e.getZ(), is));
    }

    private void packFx(Level level, Entity entity) {
        for (int i = 0; i < 8; i++) {
            float f1 = level.random.nextFloat() * 3.0F - level.random.nextFloat() * 3.0F;
            float f2 = 0.25F + level.random.nextFloat() * 2.0F;
            float f3 = level.random.nextFloat() * 3.0F - level.random.nextFloat() * 3.0F;
            double px = entity.getX() + f1;
            double py = entity.getY() + f2;
            double pz = entity.getZ() + f3;
            if (level instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.SMOKE, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(ParticleTypes.EXPLOSION, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                server.sendParticles(DustParticleOptions.REDSTONE, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                0.5F,
                1.5F);
    }
}
