package danger.orespawn.items.tools;

import danger.orespawn.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * 1.7.10 {@code MyChainsaw} — gold registers as
 * {@code new UltimateSword(BaseItemID+447, toolCHAINSAW).setUnlocalizedName("chainsawsmall")}
 * with dedicated {@code ModelChainsaw}/{@code RenderChainsaw} (NOT Bertha).
 * <p>
 * Gold combat / tool behavior (UltimateSword special-cases MyChainsaw):
 * <ul>
 *   <li><b>No</b> auto-enchants</li>
 *   <li>Swing: plays {@code orespawn:chainsawshort}, sets swingtimer=50</li>
 *   <li>While swingtimer&gt;0 (client): flame/smoke/spark particles in front of player</li>
 *   <li>Left-click: AOE living entities in 5-block expand, damage = chainsaw_stats.damage (56)</li>
 *   <li>Wood/leaves harvest + multi-break ±5/±5.10/±5 when mineBlock</li>
 *   <li>toolCHAINSAW: harvest 3, uses 1500, eff 10, damage 56, ench 75</li>
 *   <li>UltimateSword constructor also {@code setMaxDamage(3000)} — live durability 3000</li>
 * </ul>
 * Inventory icon: {@code chainsawsmall}; in-hand texture: {@code Chainsawtexture.png}.
 */
public class Chainsaw extends SwordItem {
    public static final int CHAINSAW_MATERIAL_USES = 1500;
    public static final int CHAINSAW_USES = 3000;
    public static final float CHAINSAW_DAMAGE = 56.0F;
    public static final float CHAINSAW_EFFICIENCY = 10.0F;
    public static final int CHAINSAW_ENCHANTABILITY = 75;

    public static final Tier CHAINSAW_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            CHAINSAW_USES,
            CHAINSAW_EFFICIENCY,
            CHAINSAW_DAMAGE,
            CHAINSAW_ENCHANTABILITY,
            () -> Ingredient.of(ModItems.URANIUM_INGOT.get(), ModItems.TITANIUM_INGOT.get()));

    /** Gold sound id {@code orespawn:chainsawshort} (file sounds/chainsawshort.ogg). */
    private static final ResourceLocation CHAINSAW_SHORT =
            ResourceLocation.fromNamespaceAndPath("orespawn", "chainsawshort");

    /** Gold instance field on UltimateSword (shared Item singleton). */
    private int swingtimer = 0;

    public Chainsaw(Properties properties) {
        super(CHAINSAW_TIER, properties);
    }

    /**
     * Gold multi-break volume around the mined block: X/Z ±5, Y −5.+10
     * (11×16×11 = tree-sized chunk of wood/leaves).
     */
    public static final int AREA_XZ = 5;
    public static final int AREA_Y_DOWN = 5;
    public static final int AREA_Y_UP = 10;

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // gold: MyChainsaw never auto-enchants
        if (this.swingtimer > 0) {
            this.swingtimer--;
        }
        if (level.isClientSide && this.swingtimer > 0 && entity != null) {
            spawnSwingParticles(level, entity);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    /** Swords normally cannot break blocks in creative — chainsaw must. */
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    /** Treat as correct tool so logs/leaves actually break and drop. */
    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canCrush(state) || isLeaves(state) || super.isCorrectToolForDrops(stack, state);
    }

    /** Axe dig abilities so wood is mined as with an axe. */
    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
                || super.canPerformAction(stack, itemAbility);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entityLiving) {
        // gold: play chainsawshort when swingtimer==0, then swingtimer=50
        if (entityLiving != null && this.swingtimer == 0) {
            float pitch = entityLiving.getRandom().nextFloat() * 0.2F + 0.9F;
            Level level = entityLiving.level();
            level.playSound(
                    null,
                    entityLiving.getX(),
                    entityLiving.getY(),
                    entityLiving.getZ(),
                    SoundEvent.createVariableRangeEvent(CHAINSAW_SHORT),
                    SoundSource.PLAYERS,
                    1.0F,
                    pitch);
            this.swingtimer = 50;
        }
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        // gold UltimateSword.onLeftClickEntity
        if (entity != null && pvpBlocked()) {
            if (entity instanceof Player) {
                return true;
            }
            if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
                return true;
            }
        }
        // gold: if MyChainsaw → AOE (only when primary click not cancelled)
        if (player != null && !player.level().isClientSide) {
            findSomethingToHit(player);
        }
        return false;
    }

    /** Gold {@code OreSpawnMain.ultimate_sword_pvp} — shared knob on {@link BattleAxe}. */
    private static boolean pvpBlocked() {
        return BattleAxe.ultimate_sword_pvp == 0;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // gold getStrVsBlock for MyChainsaw — fast on wood / leaves / soft plant matter
        if (canCrush(state) || isLeaves(state)) {
            return CHAINSAW_EFFICIENCY;
        }
        return 2.0F;
    }

    @Override
    public boolean mineBlock(
            ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        // gold UltimateSword.func_150894_a multi-break for MyChainsaw (server only)
        if (!level.isClientSide && (canCrush(state) || isLeaves(state))) {
            // gold: if the clicked block was leaves-class, only area-break leaves;
            // otherwise area-break all canCrush (logs + leaves + planks + …)
            boolean breakLeavesOnly = isLeaves(state);
            for (int i = -AREA_XZ; i <= AREA_XZ; i++) {
                for (int j = -AREA_Y_DOWN; j <= AREA_Y_UP; j++) {
                    for (int k = -AREA_XZ; k <= AREA_XZ; k++) {
                        if (i == 0 && j == 0 && k == 0) {
                            continue; // primary block handled by vanilla break
                        }
                        BlockPos p = pos.offset(i, j, k);
                        BlockState s = level.getBlockState(p);
                        if (s.isAir()) {
                            continue;
                        }
                        boolean ok = breakLeavesOnly ? isLeaves(s) : canCrush(s);
                        if (ok) {
                            level.destroyBlock(p, true, entity);
                        }
                    }
                }
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    private void findSomethingToHit(Player player) {
        AABB box = player.getBoundingBox().inflate(5.0, 5.0, 5.0);
        for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (isSuitableTarget(living, player)) {
                living.hurt(player.damageSources().playerAttack(player), CHAINSAW_DAMAGE);
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target, Player player) {
        if (target == null || target == player || !target.isAlive()) {
            return false;
        }
        if (pvpBlocked()) {
            if (target instanceof Player) {
                return false;
            }
            if (target instanceof OwnableEntity ownable && ownable.getOwner() != null) {
                return false;
            }
        }
        return myCanSee(target, player);
    }

    /** Gold {@code MyCanSee} — 10-step line of sight through air only. */
    private boolean myCanSee(LivingEntity e, Player player) {
        int nblks = 10;
        float startx = (float) player.getX();
        float starty = (float) (player.getY() + 1.4F);
        float startz = (float) player.getZ();
        float dx = (float) ((e.getX() - startx) / 10.0);
        float dy = (float) ((e.getY() + e.getBbHeight() / 2.0F - starty) / 10.0);
        float dz = (float) ((e.getZ() - startz) / 10.0);

        if (Math.abs(dx) > 1.0F) {
            dy /= Math.abs(dx);
            dz /= Math.abs(dx);
            nblks = (int) (nblks * Math.abs(dx));
            dx = Math.max(-1.0F, Math.min(1.0F, dx));
        }
        if (Math.abs(dy) > 1.0F) {
            dx /= Math.abs(dy);
            dz /= Math.abs(dy);
            nblks = (int) (nblks * Math.abs(dy));
            dy = Math.max(-1.0F, Math.min(1.0F, dy));
        }
        if (Math.abs(dz) > 1.0F) {
            dy /= Math.abs(dz);
            dx /= Math.abs(dz);
            nblks = (int) (nblks * Math.abs(dz));
            dz = Math.max(-1.0F, Math.min(1.0F, dz));
        }

        for (int i = 0; i < nblks; i++) {
            startx += dx;
            starty += dy;
            startz += dz;
            BlockPos p = BlockPos.containing(startx, starty, startz);
            if (!player.level().getBlockState(p).isAir()) {
                return false;
            }
        }
        return true;
    }

    private static boolean canCrush(BlockState state) {
        // gold canCrush for MyChainsaw: web, logs, leaves, planks, saplings, tall grass, snow,
        // plus OreSpawn crystal wood/leaves — use tags where possible
        if (state.is(Blocks.COBWEB)
                || state.is(Blocks.VINE)
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.PLANKS)
                || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.REPLACEABLE)
                || state.is(Blocks.SNOW)
                || state.is(Blocks.SNOW_BLOCK)) {
            return true;
        }
        // soft match OreSpawn wood/leaves by path
        ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (key != null && "orespawn".equals(key.getNamespace())) {
            String p = key.getPath();
            return p.contains("log")
                    || p.contains("leaves")
                    || p.contains("plank")
                    || p.contains("wood")
                    || p.contains("dt")
                    || p.equals("crystal_tree_log");
        }
        return false;
    }

    private static boolean isLeaves(BlockState state) {
        if (state.is(BlockTags.LEAVES)
                || state.is(Blocks.COBWEB)
                || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.REPLACEABLE)) {
            return true;
        }
        ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return key != null && "orespawn".equals(key.getNamespace()) && key.getPath().contains("leaves");
    }

    private static void spawnSwingParticles(Level level, Entity entity) {
        float f = 1.0F;
        float yaw = entity.getYRot() + 90.0F + 45.0F;
        float dx = (float) (f * Math.cos(Math.toRadians(yaw)));
        float dz = (float) (f * Math.sin(Math.toRadians(yaw)));
        double x = entity.getX() + dx;
        double y = entity.getY();
        double z = entity.getZ() + dz;
        var rand = level.random;
        if (rand.nextInt(8) == 0) {
            level.addParticle(
                    ParticleTypes.FLAME,
                    x,
                    y,
                    z,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F,
                    rand.nextFloat() / 10.0F,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F);
        }
        if (rand.nextInt(2) == 0) {
            level.addParticle(
                    ParticleTypes.SMOKE,
                    x,
                    y,
                    z,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F,
                    rand.nextFloat() / 10.0F,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F);
        }
        if (rand.nextInt(10) == 0) {
            level.addParticle(
                    ParticleTypes.FIREWORK,
                    x,
                    y,
                    z,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F,
                    rand.nextFloat() / 5.0F,
                    (rand.nextFloat() - rand.nextFloat()) / 20.0F);
        }
    }
}
