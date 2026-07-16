package danger.orespawn.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * Gold {@code ExperienceCatcher} — on block use, scan XP orbs in a small box above the
 * click face. If an orb has value ≥ 3 (and 4/5 chance), consume the catcher (unless
 * creative), remove the orb, and drop: experience bottle + string + stick.
 * <p>
 * Gold bug/quirk: on miss it drops a replacement catcher and still consumes the stack.
 * Port preserves that behavior.
 */
public class ItemExperienceCatcher extends Item {
    public static final int MAX_STACK = 16;

    public ItemExperienceCatcher(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        player.swing(context.getHand());

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clicked = context.getClickedPos();
        // gold AABB used click face offsets par8/par10 on x/z
        double fx = context.getClickLocation().x;
        double fy = clicked.getY();
        double fz = context.getClickLocation().z;
        AABB bb = new AABB(fx - 0.5, fy, fz - 0.5, fx + 0.5, fy + 2.0, fz + 0.5);

        ServerLevel server = (ServerLevel) level;
        for (ExperienceOrb orb : server.getEntitiesOfClass(ExperienceOrb.class, bb)) {
            if (orb.getValue() >= 3 && server.random.nextInt(5) != 1) {
                double dropX = fx;
                double dropY = fy + 1.0;
                double dropZ = fz;
                orb.discard();
                server.addFreshEntity(new ItemEntity(server, dropX, dropY, dropZ, new ItemStack(Items.EXPERIENCE_BOTTLE)));
                server.addFreshEntity(new ItemEntity(server, dropX, dropY, dropZ, new ItemStack(Items.STRING)));
                server.addFreshEntity(new ItemEntity(server, dropX, dropY, dropZ, new ItemStack(Items.STICK)));
                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
                server.playSound(
                        null,
                        dropX,
                        dropY,
                        dropZ,
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS,
                        0.8F,
                        1.0F);
                return InteractionResult.SUCCESS;
            }
        }

        // gold miss path: spit a fresh catcher and still consume held stack
        double dropX = fx;
        double dropY = fy + 1.0;
        double dropZ = fz;
        server.addFreshEntity(
                new ItemEntity(server, dropX, dropY, dropZ, new ItemStack(this)));
        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
