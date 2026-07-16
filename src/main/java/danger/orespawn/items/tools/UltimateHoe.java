package danger.orespawn.items.tools;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Gold UltimateHoe — Efficiency 2 when unenchanted + 3×3 till (center + 8 neighbors,
 * farmland moisture 7 on dirt/grass). Plus 1.21 QoL: Fortune, Unbreaking, Mending.
 * Gold did not damage the item on till.
 */
public class UltimateHoe extends HoeItem {
    public UltimateHoe(Properties properties) {
        super(OrespawnToolMaterial.UltimateTools.tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ToolEnchantHelper.applyAllIfUnenchanted(
                stack,
                level,
                // gold
                new ToolEnchantHelper.EnchantSpec(Enchantments.EFFICIENCY, 2),
                // 1.21 QoL
                new ToolEnchantHelper.EnchantSpec(Enchantments.FORTUNE, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.UNBREAKING, 3),
                new ToolEnchantHelper.EnchantSpec(Enchantments.MENDING, 1));
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    /**
     * Gold {@code onItemUse} (func_180614_a): right-click dirt/grass with air above →
     * farmland moisture 7 at center, then the 8 neighbors (same-Y dirt/grass with air above;
     * stacked dirt/grass one up if air above that; or air neighbor with dirt/grass below).
     * No {@code damageItem} / {@code hurtAndBreak} in gold — none here.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        Direction facing = context.getClickedFace();

        if (player != null && !player.mayUseItemAt(pos.relative(facing), facing, stack)) {
            return InteractionResult.FAIL;
        }

        Block original = level.getBlockState(pos).getBlock();
        // Gold: facing != DOWN && isAirBlock(above); only DIRT or GRASS (GRASS_BLOCK)
        if (facing != Direction.DOWN && level.isEmptyBlock(pos.above())) {
            if (original != Blocks.DIRT && original != Blocks.GRASS_BLOCK) {
                return InteractionResult.PASS;
            }

            BlockState farmland = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE);

            if (!level.isClientSide) {
                level.setBlock(pos, farmland, 11);

                List<BlockPos> blockPositions = new ArrayList<>();
                blockPositions.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
                blockPositions.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1));
                blockPositions.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() - 1));
                blockPositions.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
                blockPositions.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 1));
                blockPositions.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1));
                blockPositions.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
                blockPositions.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));

                for (BlockPos blockPosition : blockPositions) {
                    Block neighbor = level.getBlockState(blockPosition).getBlock();
                    if (neighbor == Blocks.GRASS_BLOCK || neighbor == Blocks.DIRT) {
                        BlockPos abovePos = new BlockPos(blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ());
                        Block blockAbove = level.getBlockState(abovePos).getBlock();
                        if (blockAbove == Blocks.AIR) {
                            level.setBlock(blockPosition, farmland, 11);
                        } else if (blockAbove == Blocks.GRASS_BLOCK || blockAbove == Blocks.DIRT) {
                            BlockPos abovePos2 = new BlockPos(abovePos.getX(), abovePos.getY() + 1, abovePos.getZ());
                            Block blockAbove2 = level.getBlockState(abovePos2).getBlock();
                            if (blockAbove2 == Blocks.AIR) {
                                level.setBlock(abovePos, farmland, 11);
                            }
                        }
                    } else if (neighbor == Blocks.AIR) {
                        BlockPos underPos = new BlockPos(blockPosition.getX(), blockPosition.getY() - 1, blockPosition.getZ());
                        Block blockUnder = level.getBlockState(underPos).getBlock();
                        if (blockUnder == Blocks.DIRT || blockUnder == Blocks.GRASS_BLOCK) {
                            level.setBlock(underPos, farmland, 11);
                        }
                    }
                }
            }

            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
