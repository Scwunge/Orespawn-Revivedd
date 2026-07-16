package danger.orespawn.client;

import danger.orespawn.client.render.BattleAxeItemRenderer;
import danger.orespawn.client.render.BerthaItemRenderer;
import danger.orespawn.client.render.ChainsawItemRenderer;
import danger.orespawn.client.render.HammyItemRenderer;
import danger.orespawn.client.render.QueenBattleAxeItemRenderer;
import danger.orespawn.client.render.RoyalItemRenderer;
import danger.orespawn.client.render.SliceItemRenderer;
import danger.orespawn.client.render.SquidZookaItemRenderer;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * Client item extensions — 1.7.10 IItemRenderer equivalents for specialty weapons.
 * <p>
 * Bertha / Slice / Royal: huge 3D mesh in third person; flat {@code *small} handheld
 * icons in first person + GUI (3D FP poses deferred as too buggy).
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ModClientItemExtensions {
    public static final ModelResourceLocation BERTHA_ICON = icon("bertha_icon");
    public static final ModelResourceLocation SLICE_ICON = icon("slice_icon");
    public static final ModelResourceLocation ROYAL_ICON = icon("royal_icon");
    public static final ModelResourceLocation HAMMY_ICON = icon("hammy_icon");
    public static final ModelResourceLocation BATTLE_AXE_ICON = icon("battle_axe_icon");
    public static final ModelResourceLocation CHAINSAW_ICON = icon("chainsaw_icon");
    public static final ModelResourceLocation QUEEN_BATTLE_AXE_ICON = icon("queen_battle_axe_icon");
    public static final ModelResourceLocation SQUID_ZOOKA_ICON = icon("squid_zooka_icon");

    private ModClientItemExtensions() {}

    private static ModelResourceLocation icon(String path) {
        return ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + path));
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(BERTHA_ICON);
        event.register(SLICE_ICON);
        event.register(ROYAL_ICON);
        event.register(HAMMY_ICON);
        event.register(BATTLE_AXE_ICON);
        event.register(CHAINSAW_ICON);
        event.register(QUEEN_BATTLE_AXE_ICON);
        event.register(SQUID_ZOOKA_ICON);
    }

    @SubscribeEvent
    public static void register(RegisterClientExtensionsEvent event) {
        registerBewlr(event, ModItems.BERTHA.get(), BerthaItemRenderer::new);
        registerBewlr(event, ModItems.SLICE.get(), SliceItemRenderer::new);
        registerBewlr(event, ModItems.ROYAL.get(), RoyalItemRenderer::new);
        registerBewlr(event, ModItems.HAMMY.get(), HammyItemRenderer::new);
        registerBewlr(event, ModItems.BATTLE_AXE.get(), BattleAxeItemRenderer::new);
        registerBewlr(event, ModItems.CHAINSAW.get(), ChainsawItemRenderer::new);
        registerBewlr(event, ModItems.QUEEN_BATTLE_AXE.get(), QueenBattleAxeItemRenderer::new);
        registerBewlr(event, ModItems.SQUID_ZOOKA.get(), SquidZookaItemRenderer::new);
    }

    private static void registerBewlr(
            RegisterClientExtensionsEvent event,
            Item item,
            java.util.function.Supplier<BlockEntityWithoutLevelRenderer> factory) {
        event.registerItem(
                new IClientItemExtensions() {
                    private BlockEntityWithoutLevelRenderer renderer;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        if (this.renderer == null) {
                            this.renderer = factory.get();
                            if (this.renderer instanceof ResourceManagerReloadListener reloadable) {
                                reloadable.onResourceManagerReload(
                                        Minecraft.getInstance().getResourceManager());
                            }
                        }
                        return this.renderer;
                    }

                    @Override
                    public HumanoidModel.ArmPose getArmPose(
                            LivingEntity entity, InteractionHand hand, ItemStack stack) {
                        return HumanoidModel.ArmPose.ITEM;
                    }
                },
                item);
    }
}
