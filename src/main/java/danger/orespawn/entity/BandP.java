package danger.orespawn.entity;

import danger.orespawn.OreSpawnMain;
import danger.orespawn.init.ModItems;
import danger.orespawn.util.ai.WanderALotGoal;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Gold {@code BandP} / Criminal (EntityMob). Size 0.75×1.75, speed 0.32,
 * stats defaults {@code get_mobstats("BandP", 100, 1, 18)}, XP 1000, follow 2.
 * Steals player inventory; drops emerald + optional uranium/titanium nuggets.
 * Registry size set in {@code ModEntities.BAND_P} / Criminal.
 */
public class BandP extends Monster {
    private static final EntityDataAccessor<Byte> DATA_WHAT =
            SynchedEntityData.defineId(BandP.class, EntityDataSerializers.BYTE);

    private final float moveSpeed = 0.32F;
    private int whatset = 0;
    private int whatami = 0;
    /** Gold MymainInventory length 100 — stolen loot. */
    public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(100, ItemStack.EMPTY);
    int gotStuff = 0;

    public BandP(EntityType<? extends BandP> type, Level level) {
        super(type, level);
        this.xpReward = 1000;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0) // BandP_stats.health
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.ATTACK_DAMAGE, 1.0) // BandP_stats.attack
                .add(Attributes.ARMOR, 18.0) // BandP_stats.defense
                .add(Attributes.FOLLOW_RANGE, 2.0); // gold field_70174_ab = 2 (very short)
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_WHAT, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        // gold: MoveThroughVillage, WanderALot(16,0.5), WatchClosest, LookIdle, OpenDoor, MoveIndoors
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderALotGoal(this, 16, 0.5));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        // gold: noDespawnRequired → false; else got_stuff == 0
        if (this.isPersistenceRequired()) {
            return false;
        }
        return this.gotStuff == 0;
    }

    @Override
    public void tick() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.moveSpeed);
        }
        super.tick();
        if (!this.level().isClientSide && this.whatset == 0) {
            this.whatset = 1;
            this.whatami = this.level().random.nextInt(2);
            this.setWhat(this.whatami);
        }
    }

    public int mygetMaxHealth() {
        return 100;
    }

    public int getWhat() {
        return this.entityData.get(DATA_WHAT);
    }

    public void setWhat(int value) {
        this.entityData.set(DATA_WHAT, (byte) value);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        // gold: 10 + nextInt(5) emeralds
        int var4 = 10 + this.random.nextInt(5);
        for (int i = 0; i < var4; i++) {
            this.spawnAtLocation(new ItemStack(Items.EMERALD));
        }
        // gold: what==0 → 2+nextInt(3) of each uranium + titanium nugget
        if (this.getWhat() == 0) {
            int n = 2 + this.random.nextInt(3);
            for (int i = 0; i < n; i++) {
                this.spawnAtLocation(new ItemStack(ModItems.URANIUM_NUGGET.get()));
                this.spawnAtLocation(new ItemStack(ModItems.TITANIUM_NUGGET.get()));
            }
        }
        // gold: dump stolen inventory
        for (ItemStack stack : this.mainInventory) {
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack.copy());
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDeadOrDying()) {
            return;
        }
        // gold: 1/12 seek attack target
        if (this.random.nextInt(12) == 1) {
            LivingEntity e = this.findSomethingToAttack();
            if (e != null) {
                this.getLookControl().setLookAt(e, 10.0F, 10.0F);
                if (this.distanceToSqr(e) < 9.0) {
                    this.doHurtTarget(e);
                    if (e instanceof Player p) {
                        this.tryStealFromPlayer(p);
                    }
                } else {
                    this.getNavigation().moveTo(e, 1.25);
                }
            }
        }
    }

    /** Gold: steal last non-empty armor, else last non-empty inventory slot. */
    private void tryStealFromPlayer(Player p) {
        int k = -1;
        for (int i = 0; i < this.mainInventory.size(); i++) {
            if (this.mainInventory.get(i).isEmpty()) {
                k = i;
                break;
            }
        }
        if (k < 0) {
            return;
        }
        Inventory inv = p.getInventory();
        // gold armor array from end
        for (int a = inv.armor.size() - 1; a >= 0; a--) {
            ItemStack armor = inv.armor.get(a);
            if (!armor.isEmpty()) {
                this.mainInventory.set(k, armor.copy());
                inv.armor.set(a, ItemStack.EMPTY);
                this.gotStuff++;
                return;
            }
        }
        // gold main inventory from end
        for (int s = inv.items.size() - 1; s >= 0; s--) {
            ItemStack slot = inv.items.get(s);
            if (!slot.isEmpty()) {
                this.mainInventory.set(k, slot.copy());
                inv.items.set(s, ItemStack.EMPTY);
                this.gotStuff++;
                return;
            }
        }
    }

    private boolean isSuitableTarget(LivingEntity target) {
        if (target == null || target == this || !target.isAlive()) {
            return false;
        }
        if (!this.hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Player p) {
            return !p.getAbilities().instabuild;
        }
        if (target instanceof Villager) {
            return true;
        }
        // gold: Girlfriend / Boyfriend — entities may be deferred
        return false;
    }

    @Nullable
    private LivingEntity findSomethingToAttack() {
        if (OreSpawnMain.PlayNicely != 0) {
            return null;
        }
        List<LivingEntity> list = this.level()
                .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(20.0, 6.0, 20.0));
        list.sort(Comparator.comparingDouble(this::distanceToSqr));
        for (LivingEntity living : list) {
            if (this.isSuitableTarget(living)) {
                return living;
            }
        }
        return null;
    }

    /**
     * Gold {@code getCanSpawnHere}: Criminal spawner nearby OR (day + y≥100 + no BandP + villager near).
     * Spawner name check deferred — day/y/buddy/villager path only.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        if (level instanceof Level lvl && !lvl.isDay()) {
            return false;
        }
        if (this.getY() < 100.0) {
            return false;
        }
        List<BandP> peers =
                this.level().getEntitiesOfClass(BandP.class, this.getBoundingBox().inflate(32.0, 12.0, 32.0));
        for (BandP other : peers) {
            if (other != this) {
                return false;
            }
        }
        List<Villager> villagers =
                this.level().getEntitiesOfClass(Villager.class, this.getBoundingBox().inflate(36.0, 12.0, 36.0));
        return !villagers.isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GotStuff", this.gotStuff);
        tag.putInt("WhatAmi", this.getWhat());
        if (this.gotStuff != 0) {
            tag.put("Inventory", this.writeInventoryList());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.gotStuff = tag.getInt("GotStuff");
        if (tag.contains("WhatAmi")) {
            this.setWhat(tag.getInt("WhatAmi"));
            this.whatset = 1;
        }
        if (this.gotStuff != 0 && tag.contains("Inventory", Tag.TAG_LIST)) {
            this.readInventoryList(tag.getList("Inventory", Tag.TAG_COMPOUND));
        }
    }

    private ListTag writeInventoryList() {
        ListTag list = new ListTag();
        for (int i = 0; i < this.mainInventory.size(); i++) {
            ItemStack stack = this.mainInventory.get(i);
            if (!stack.isEmpty()) {
                CompoundTag slotTag = new CompoundTag();
                slotTag.putByte("Slot", (byte) i);
                list.add(stack.save(this.registryAccess(), slotTag));
            }
        }
        return list;
    }

    private void readInventoryList(ListTag list) {
        for (int i = 0; i < this.mainInventory.size(); i++) {
            this.mainInventory.set(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < list.size(); i++) {
            CompoundTag slotTag = list.getCompound(i);
            int j = slotTag.getByte("Slot") & 255;
            if (j >= 0 && j < this.mainInventory.size()) {
                ItemStack.parse(this.registryAccess(), slotTag).ifPresent(stack -> this.mainInventory.set(j, stack));
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return super.doHurtTarget(target);
    }
}
