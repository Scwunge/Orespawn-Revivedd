package danger.orespawn.client;

import danger.orespawn.init.ModBlocks;
import danger.orespawn.util.Reference;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Client render layers for plants / torches / crystal.
 * <p>
 * Crop and cross models use transparent PNGs. Without {@link RenderType#cutout()},
 * the atlas treats empty pixels as solid white — the classic “white box around plants”.
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ModClientSetup {
    private ModClientSetup() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // ——— Crops / plants (minecraft:block/crop) ———
            cutout(ModBlocks.CORN_PLANT);
            cutout(ModBlocks.BUTTERFLY_PLANT);
            cutout(ModBlocks.MOSQUITO_PLANT);
            cutout(ModBlocks.FIREFLY_PLANT);
            cutout(ModBlocks.MOTH_PLANT);
            cutout(ModBlocks.STRAWBERRY_PLANT);
            cutout(ModBlocks.RADISH_PLANT);
            cutout(ModBlocks.RICE_PLANT);
            cutout(ModBlocks.QUINOA_PLANT);
            cutout(ModBlocks.TOMATO_PLANT);
            cutout(ModBlocks.LETTUCE_PLANT);

            // ——— Cross / reed-like placeables ———
            cutout(ModBlocks.ISLAND_BLOCK);
            cutout(ModBlocks.KING_SPAWNER);
            cutout(ModBlocks.QUEEN_SPAWNER);
            cutout(ModBlocks.DUNGEON_SPAWNER);
            cutout(ModBlocks.FLOWER_PINK);
            cutout(ModBlocks.FLOWER_BLUE);
            cutout(ModBlocks.FLOWER_BLACK);
            cutout(ModBlocks.FLOWER_SCARY);
            cutout(ModBlocks.CRYSTALFLOWER_RED);
            cutout(ModBlocks.CRYSTALFLOWER_GREEN);
            cutout(ModBlocks.CRYSTALFLOWER_BLUE);
            cutout(ModBlocks.CRYSTALFLOWER_YELLOW);
            cutout(ModBlocks.CRYSTAL_SAPLING);
            cutout(ModBlocks.CRYSTAL_SAPLING_2);
            cutout(ModBlocks.CRYSTAL_SAPLING_3);

            // ——— Torches ———
            cutout(ModBlocks.EXTREME_TORCH);
            cutout(ModBlocks.CRYSTAL_TORCH);
            cutout(ModBlocks.CREEPER_REPELLENT);
            cutout(ModBlocks.KRAKEN_REPELLENT);

            // ——— Leaves (cutoutMipped matches vanilla leaves) ———
            cutoutMipped(ModBlocks.CRYSTAL_LEAVES);
            cutoutMipped(ModBlocks.CRYSTAL_TREE_LEAVES);
            cutoutMipped(ModBlocks.CRYSTAL_TREE_LEAVES_2);
            cutoutMipped(ModBlocks.CRYSTAL_TREE_LEAVES_3);
            cutoutMipped(ModBlocks.APPLE_LEAVES);
            cutoutMipped(ModBlocks.SCARY_LEAVES);
            cutoutMipped(ModBlocks.CHERRY_LEAVES);
            cutoutMipped(ModBlocks.PEACH_LEAVES);
            cutoutMipped(ModBlocks.EXPERIENCE_LEAVES);

            // Crystal grass: gold PNGs are mostly alpha flecks. Solid layer + flattened
            // textures looked like solid plastic color; translucent + gold textures = correct
            // crystal fleck ground (gold isOpaqueCube only in crystal dim).
            translucent(ModBlocks.CRYSTAL_GRASS);

            // ——— Crystal glass-like stone/wood (gold non-opaque / translucent pass) ———
            translucent(ModBlocks.CRYSTAL_STONE);
            translucent(ModBlocks.CRYSTAL_LOG);
            translucent(ModBlocks.CRYSTAL_PLANKS);
            translucent(ModBlocks.CRYSTAL_WORKBENCH);
            translucent(ModBlocks.CRYSTAL_FURNACE);
            translucent(ModBlocks.CRYSTAL_ORE);
            translucent(ModBlocks.CRYSTAL_COAL);
            translucent(ModBlocks.CRYSTAL_PINK_BLOCK);
            translucent(ModBlocks.TIGERSEYE);
            translucent(ModBlocks.TIGERSEYE_BLOCK);
            translucent(ModBlocks.CRYSTAL_TREE_LOG);
            translucent(ModBlocks.PORTAL_BLOCK);
        });
    }

    private static void cutout(DeferredBlock<? extends Block> block) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
    }

    private static void cutoutMipped(DeferredBlock<? extends Block> block) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutoutMipped());
    }

    private static void translucent(DeferredBlock<? extends Block> block) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
    }
}
