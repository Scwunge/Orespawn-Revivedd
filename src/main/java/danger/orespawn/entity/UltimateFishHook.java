package danger.orespawn.entity;

import danger.orespawn.init.ModEntities;
import danger.orespawn.items.tools.UltimateFishingRod;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Gold {@code UltimateFishHook} (EntityFishHook). Full gold fishing:
 * <ul>
 *   <li>Faster bite wait 50–300 (vanilla 100–600); Lure subtracts level×100 ticks</li>
 *   <li>Bobbing in <b>water or lava</b></li>
 *   <li>Lava loot: sunspoturchin / lavaeel / sunfish / sparkfish / firefish</li>
 *   <li>Water: junk/treasure weights + 50% vanilla fish / 50% orespawn fish</li>
 *   <li>Requires holding {@link UltimateFishingRod}</li>
 * </ul>
 * Registry: {@code ultimate_fish_hook}.
 */
public class UltimateFishHook extends FishingHook {
    private final RandomSource syncRandom = RandomSource.create();
    private boolean biting;
    private int outOfWaterTime;
    private int life;
    private int nibble;
    private int timeUntilLured;
    private int timeUntilHooked;
    private float fishAngle;
    private boolean openWater = true;
    @Nullable
    private Entity hookedIn;
    private FishHookState currentState = FishHookState.FLYING;
    private final int luck;
    private final int lureSpeed;
    private int ticksInAir;

    public UltimateFishHook(EntityType<? extends UltimateFishHook> type, Level level) {
        super(castType(type), level);
        this.luck = 0;
        this.lureSpeed = 0;
        this.noCulling = true;
    }

    @SuppressWarnings("unchecked")
    private static EntityType<? extends FishingHook> castType(EntityType<? extends UltimateFishHook> type) {
        return (EntityType<? extends FishingHook>) (EntityType<?>) type;
    }

    public UltimateFishHook(Player player, Level level, int luck, int lureSpeed) {
        super(ModEntities.ULTIMATE_FISH_HOOK.get(), level);
        this.luck = Math.max(0, luck);
        this.lureSpeed = Math.max(0, lureSpeed);
        this.noCulling = true;
        this.setOwner(player);
        float f = player.getXRot();
        float f1 = player.getYRot();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180.0F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180.0F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180.0F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180.0F));
        double d0 = player.getX() - (double) f3 * 0.3;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double) f2 * 0.3;
        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(
                0.6 / d3 + this.random.triangle(0.5, 0.0103365),
                0.6 / d3 + this.random.triangle(0.5, 0.0103365),
                0.6 / d3 + this.random.triangle(0.5, 0.0103365));
        this.setDeltaMovement(vec3);
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
        this.setXRot((float) (Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float) Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        this.syncRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        // do not call super.tick() — full gold/custom state machine (water + lava)
        this.baseTick();

        Player player = this.getPlayerOwner();
        if (player == null) {
            this.discard();
            return;
        }
        if (!this.level().isClientSide && shouldStopUltimateFishing(player)) {
            return;
        }

        if (this.onGround()) {
            this.life++;
            if (this.life >= 1200) {
                this.discard();
                return;
            }
        } else {
            this.life = 0;
        }

        float fluidHeight = 0.0F;
        BlockPos blockpos = this.blockPosition();
        FluidState fluidstate = this.level().getFluidState(blockpos);
        // gold: water OR lava counts as fishable fluid
        boolean inFishableFluid = fluidstate.is(FluidTags.WATER) || fluidstate.is(FluidTags.LAVA);
        if (inFishableFluid) {
            fluidHeight = fluidstate.getHeight(this.level(), blockpos);
        }
        boolean flag = fluidHeight > 0.0F;

        if (this.currentState == FishHookState.FLYING) {
            this.ticksInAir++;
            if (this.hookedIn != null) {
                this.setDeltaMovement(Vec3.ZERO);
                this.currentState = FishHookState.HOOKED_IN_ENTITY;
                return;
            }
            if (flag) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
                this.currentState = FishHookState.BOBBING;
                return;
            }
            this.checkCollision();
        } else if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
            if (this.hookedIn != null) {
                if (!this.hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension()) {
                    this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
                } else {
                    this.setHookedEntity(null);
                    this.currentState = FishHookState.FLYING;
                }
            }
            return;
        } else if (this.currentState == FishHookState.BOBBING) {
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getY() + vec3.y - blockpos.getY() - fluidHeight;
            if (Math.abs(d0) < 0.01) {
                d0 += Math.signum(d0) * 0.1;
            }
            this.setDeltaMovement(vec3.x * 0.9, vec3.y - d0 * this.random.nextFloat() * 0.2, vec3.z * 0.9);
            if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
                this.openWater = true;
            } else {
                this.openWater = this.openWater && this.outOfWaterTime < 10;
            }
            if (flag) {
                this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
                if (this.biting) {
                    this.setDeltaMovement(
                            this.getDeltaMovement()
                                    .add(0.0, -0.1 * this.syncRandom.nextFloat() * this.syncRandom.nextFloat(), 0.0));
                }
                if (!this.level().isClientSide) {
                    this.catchingFish(blockpos);
                }
            } else {
                this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
            }
        }

        if (!inFishableFluid) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.updateRotation();
        if (this.currentState == FishHookState.FLYING && (this.onGround() || this.horizontalCollision)) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        this.reapplyPosition();
    }

    private boolean shouldStopUltimateFishing(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        boolean holdingRod =
                main.getItem() instanceof UltimateFishingRod || off.getItem() instanceof UltimateFishingRod;
        if (!player.isRemoved() && player.isAlive() && holdingRod && !(this.distanceToSqr(player) > 1024.0)) {
            return false;
        }
        this.discard();
        return true;
    }

    private void checkCollision() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS
                && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        Player owner = this.getPlayerOwner();
        if (target == owner && this.ticksInAir < 5) {
            return false;
        }
        return super.canHitEntity(target) || (target.isAlive() && target instanceof ItemEntity);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide) {
            this.setHookedEntity(result.getEntity());
        }
    }

    private void setHookedEntity(@Nullable Entity hookedEntity) {
        this.hookedIn = hookedEntity;
        // keep entity synced data happy if accessible via getEntityData — use public API path
        // base DATA is private; local hookedIn is authoritative for our retrieve/tick
    }

    private void catchingFish(BlockPos pos) {
        ServerLevel serverlevel = (ServerLevel) this.level();
        int i = 1;
        BlockPos above = pos.above();
        if (this.random.nextFloat() < 0.25F && this.level().isRainingAt(above)) {
            i++;
        }
        if (this.random.nextFloat() < 0.5F && !this.level().canSeeSky(above)) {
            i--;
        }

        if (this.nibble > 0) {
            this.nibble--;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.biting = false;
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= i;
            if (this.timeUntilHooked > 0) {
                this.fishAngle = (float) (this.fishAngle + this.random.triangle(0.0, 9.188));
                float f = this.fishAngle * ((float) Math.PI / 180.0F);
                float f1 = Mth.sin(f);
                float f2 = Mth.cos(f);
                double d0 = this.getX() + f1 * this.timeUntilHooked * 0.1F;
                double d1 = Mth.floor(this.getY()) + 1.0F;
                double d2 = this.getZ() + f2 * this.timeUntilHooked * 0.1F;
                BlockState blockstate = serverlevel.getBlockState(BlockPos.containing(d0, d1 - 1.0, d2));
                if (isFishableSurface(blockstate)) {
                    if (this.random.nextFloat() < 0.15F) {
                        serverlevel.sendParticles(
                                ParticleTypes.BUBBLE, d0, d1 - 0.1F, d2, 1, f1, 0.1, f2, 0.0);
                    }
                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    serverlevel.sendParticles(
                            ParticleTypes.FISHING, d0, d1, d2, 0, f4, 0.01, -f3, 1.0);
                    serverlevel.sendParticles(
                            ParticleTypes.FISHING, d0, d1, d2, 0, -f4, 0.01, f3, 1.0);
                }
            } else {
                this.playSound(
                        SoundEvents.FISHING_BOBBER_SPLASH,
                        0.25F,
                        1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                double d3 = this.getY() + 0.5;
                serverlevel.sendParticles(
                        ParticleTypes.BUBBLE,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int) (1.0F + this.getBbWidth() * 20.0F),
                        this.getBbWidth(),
                        0.0,
                        this.getBbWidth(),
                        0.2F);
                serverlevel.sendParticles(
                        ParticleTypes.FISHING,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int) (1.0F + this.getBbWidth() * 20.0F),
                        this.getBbWidth(),
                        0.0,
                        this.getBbWidth(),
                        0.2F);
                // gold fish_on_hook 10–30 (nibble window)
                this.nibble = Mth.nextInt(this.random, 10, 30);
                this.biting = true;
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= i;
            float f5 = 0.15F;
            if (this.timeUntilLured < 20) {
                f5 += (20 - this.timeUntilLured) * 0.05F;
            } else if (this.timeUntilLured < 40) {
                f5 += (40 - this.timeUntilLured) * 0.02F;
            } else if (this.timeUntilLured < 60) {
                f5 += (60 - this.timeUntilLured) * 0.01F;
            }
            if (this.random.nextFloat() < f5) {
                float f6 = Mth.nextFloat(this.random, 0.0F, 360.0F) * ((float) Math.PI / 180.0F);
                float f7 = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double d4 = this.getX() + Mth.sin(f6) * f7 * 0.1F;
                double d5 = Mth.floor(this.getY()) + 1.0F;
                double d6 = this.getZ() + Mth.cos(f6) * f7 * 0.1F;
                BlockState bs = serverlevel.getBlockState(BlockPos.containing(d4, d5 - 1.0, d6));
                if (isFishableSurface(bs)) {
                    serverlevel.sendParticles(
                            ParticleTypes.SPLASH, d4, d5, d6, 2 + this.random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
                }
            }
            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                // gold ticks_catchable 100–200
                this.timeUntilHooked = Mth.nextInt(this.random, 100, 200);
            }
        } else {
            // gold wait 50–300 (faster than vanilla 100–600)
            this.timeUntilLured = Mth.nextInt(this.random, 50, 300);
            this.timeUntilLured = this.timeUntilLured - this.lureSpeed;
        }

        if (this.nibble > 0) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x,
                    this.getDeltaMovement().y
                            - this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * 0.2,
                    this.getDeltaMovement().z);
        }
    }

    private static boolean isFishableSurface(BlockState state) {
        return state.is(Blocks.WATER)
                || state.is(Blocks.LAVA)
                || !state.getFluidState().isEmpty()
                        && (state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA));
    }

    @Override
    public int retrieve(ItemStack stack) {
        Player player = this.getPlayerOwner();
        if (this.level().isClientSide || player == null || shouldStopUltimateFishing(player)) {
            return 0;
        }

        int rodDmg = 0;
        if (this.hookedIn != null) {
            this.pullEntity(this.hookedIn);
            this.level().broadcastEntityEvent(this, (byte) 31);
            rodDmg = this.hookedIn instanceof ItemEntity ? 3 : 5;
        } else if (this.nibble > 0) {
            ItemStack catchStack = this.rollCatch(player);
            ItemEntity itementity =
                    new ItemEntity(this.level(), this.getX(), this.getY() + 1.25, this.getZ(), catchStack);
            double d0 = player.getX() - this.getX();
            double d1 = player.getY() - this.getY();
            double d2 = player.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            double d9 = 0.1;
            itementity.setDeltaMovement(d0 * d9, d1 * d9 + Math.sqrt(d3) * 0.08, d2 * d9);
            this.level().addFreshEntity(itementity);
            player.level()
                    .addFreshEntity(new ExperienceOrb(
                            player.level(),
                            player.getX(),
                            player.getY() + 0.5,
                            player.getZ() + 0.5,
                            this.random.nextInt(6) + 1));
            if (catchStack.is(ItemTags.FISHES) && player instanceof ServerPlayer) {
                player.awardStat(Stats.FISH_CAUGHT, 1);
            }
            rodDmg = 1;
        }

        if (this.onGround()) {
            rodDmg = 2;
        }

        this.discard();
        return rodDmg;
    }

    /**
     * Gold {@code func_146033_f} loot selection.
     */
    private ItemStack rollCatch(Player player) {
        float f = this.random.nextFloat();
        // gold Luck of the Sea / Lure weight nudges via luck field (LuckOfTheSea levels)
        float f1 = 0.1F - this.luck * 0.025F - 0.0F;
        float f2 = 0.05F + this.luck * 0.01F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        f2 = Mth.clamp(f2, 0.0F, 1.0F);

        if (this.isInLavaFishable()) {
            player.awardStat(Stats.FISH_CAUGHT, 1);
            return weightedPick(this.random, ORE_LAVA_FISH);
        } else if (f < f1) {
            // junk
            return weightedPick(this.random, VANILLA_JUNK);
        } else {
            f -= f1;
            if (f < f2) {
                // treasure
                return weightedPick(this.random, VANILLA_TREASURE);
            } else {
                // 50% vanilla fish / 50% orespawn fish
                player.awardStat(Stats.FISH_CAUGHT, 1);
                if (this.random.nextFloat() < 0.5F) {
                    return weightedPick(this.random, VANILLA_FISH);
                }
                return weightedPick(this.random, ORE_WATER_FISH);
            }
        }
    }

    private boolean isInLavaFishable() {
        if (this.isInLava()) {
            return true;
        }
        BlockState at = this.level().getBlockState(this.blockPosition());
        return at.is(Blocks.LAVA) || at.getFluidState().is(FluidTags.LAVA);
    }

    private static ItemStack weightedPick(RandomSource rand, List<WeightedStack> list) {
        int total = 0;
        for (WeightedStack w : list) {
            total += w.weight;
        }
        if (total <= 0) {
            return new ItemStack(Items.COD);
        }
        int roll = rand.nextInt(total);
        for (WeightedStack w : list) {
            roll -= w.weight;
            if (roll < 0) {
                return w.stack();
            }
        }
        return list.get(list.size() - 1).stack();
    }

    private static ItemStack ore(String name) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("orespawn", name));
        if (item != null && item != Items.AIR) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack oreOrFallback(String name, Item fallback) {
        ItemStack s = ore(name);
        return s.isEmpty() ? new ItemStack(fallback) : s;
    }

    private record WeightedStack(int weight, ItemStack prototype) {
        ItemStack stack() {
            return this.prototype.copy();
        }
    }

    private static final List<WeightedStack> ORE_LAVA_FISH = buildLava();
    private static final List<WeightedStack> ORE_WATER_FISH = buildWaterOre();
    private static final List<WeightedStack> VANILLA_FISH = List.of(
            new WeightedStack(60, new ItemStack(Items.COD)),
            new WeightedStack(25, new ItemStack(Items.SALMON)),
            new WeightedStack(2, new ItemStack(Items.TROPICAL_FISH)),
            new WeightedStack(13, new ItemStack(Items.PUFFERFISH)));
    private static final List<WeightedStack> VANILLA_JUNK = List.of(
            new WeightedStack(10, new ItemStack(Items.LEATHER_BOOTS)),
            new WeightedStack(10, new ItemStack(Items.LEATHER)),
            new WeightedStack(10, new ItemStack(Items.BONE)),
            new WeightedStack(10, new ItemStack(Items.POTION)),
            new WeightedStack(5, new ItemStack(Items.STRING)),
            new WeightedStack(2, new ItemStack(Items.FISHING_ROD)),
            new WeightedStack(10, new ItemStack(Items.BOWL)),
            new WeightedStack(5, new ItemStack(Items.STICK)),
            new WeightedStack(1, new ItemStack(Items.INK_SAC, 10)),
            new WeightedStack(10, new ItemStack(Items.TRIPWIRE_HOOK)),
            new WeightedStack(10, new ItemStack(Items.ROTTEN_FLESH)));
    private static final List<WeightedStack> VANILLA_TREASURE = List.of(
            new WeightedStack(1, new ItemStack(Blocks.LILY_PAD)),
            new WeightedStack(1, new ItemStack(Items.NAME_TAG)),
            new WeightedStack(1, new ItemStack(Items.SADDLE)),
            new WeightedStack(1, new ItemStack(Items.BOW)),
            new WeightedStack(1, new ItemStack(Items.FISHING_ROD)),
            new WeightedStack(1, new ItemStack(Items.BOOK)));

    private static List<WeightedStack> buildLava() {
        List<WeightedStack> list = new ArrayList<>();
        list.add(new WeightedStack(25, oreOrFallback("sunspoturchin", Items.BLAZE_POWDER)));
        list.add(new WeightedStack(10, oreOrFallback("lavaeel", Items.COOKED_COD)));
        list.add(new WeightedStack(15, oreOrFallback("sunfish", Items.COOKED_SALMON)));
        list.add(new WeightedStack(10, oreOrFallback("sparkfish", Items.COD)));
        list.add(new WeightedStack(15, oreOrFallback("firefish", Items.COOKED_COD)));
        return list;
    }

    private static List<WeightedStack> buildWaterOre() {
        List<WeightedStack> list = new ArrayList<>();
        list.add(new WeightedStack(25, oreOrFallback("bluefish", Items.COD)));
        list.add(new WeightedStack(10, oreOrFallback("pinkfish", Items.SALMON)));
        list.add(new WeightedStack(15, oreOrFallback("rockfish", Items.COD)));
        list.add(new WeightedStack(10, oreOrFallback("woodfish", Items.COD)));
        list.add(new WeightedStack(15, oreOrFallback("greyfish", Items.COD)));
        return list;
    }

    @Override
    public boolean isOpenWaterFishing() {
        return this.openWater;
    }

    @Nullable
    @Override
    public Entity getHookedIn() {
        return this.hookedIn;
    }

    private enum FishHookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }
}
