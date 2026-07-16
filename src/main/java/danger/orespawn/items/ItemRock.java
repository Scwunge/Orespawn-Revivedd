package danger.orespawn.items;

import danger.orespawn.entity.RockBase;
import danger.orespawn.entity.ThrownRock;
import danger.orespawn.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Gold {@code ItemRock} — throwable rock projectile ({@link ThrownRock}) and placeable
 * living rock ({@link RockBase}) on block use.
 * <p>
 * Gold registers 12 variants with unlocalized names {@code rocksmall}…{@code rockcrystaltnt}
 * and rock types 1–12. Stack size 64. Creative tab combat (tools).
 * <p>
 * Inventory icons: {@code textures/item/rocksmall.png} etc. (lowercase gold names).
 */
public class ItemRock extends Item {
    /** Gold rock type 1–12 wired into {@link ThrownRock} / {@link RockBase#placeRock}. */
    private final int rockType;

    public ItemRock(Properties properties, int rockType) {
        super(properties);
        this.rockType = rockType;
    }

    public int getRockType() {
        return this.rockType;
    }

    /**
     * Gold {@code onItemRightClick}: consume (unless creative), bow sound, spawn
     * {@link ThrownRock} with this rock type and gold launch velocity 1.5.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            ThrownRock rock = new ThrownRock(level, player, this.rockType);
            rock.launchFrom(player);
            level.addFreshEntity(rock);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    /**
     * Gold {@code onItemUse}: place {@link RockBase} at clicked block +1.01 Y with
     * {@link RockBase#placeRock(int)} for this type; consume unless creative.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide) {
            RockBase rock = ModEntities.ROCK_BASE.get().create(level);
            if (rock != null) {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.01;
                double z = pos.getZ() + 0.5;
                rock.moveTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
                rock.placeRock(this.rockType);
                level.addFreshEntity(rock);
            }
        }

        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
