package dev.yuluo.mc.living_unvanished.entity.blue_pigeon;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import dev.yuluo.mc.living_unvanished.client.animation.entity.blue_pigeon.BluePigeonAnimationController;
import dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon.BluePigeonAi;
import dev.yuluo.mc.living_unvanished.entity.ai.control.FlyingOrGroundMoveControl;
import dev.yuluo.mc.living_unvanished.entity.ai.navigation.FlyingOrGroundPathNavigation;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BluePigeon extends TamableAnimal implements GeoEntity {
    public BluePigeon(EntityType<? extends BluePigeon> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingOrGroundMoveControl(this, 20, true, this::shouldUseGroundNavigation);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 16.0)
            .add(Attributes.ARMOR, 0.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.FLYING_SPEED, 0.1);
    }

    // region Stub

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return !this.isTame();
    }

    @Override
    protected boolean canFlyToOwner() {
        return true;
    }

    // endregion


    // region Living

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt) {
            this.triggerAnim("main", "hurt");
            if (!this.getHeldItem().isEmpty() && this.random.nextFloat() < 0.2F) {
                this.dropHeldItem();
            }
        }

        return hurt;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        this.dropHeldItem();
    }

    // endregion


    // region Interacting

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        if (!this.getHeldItem().isEmpty() && this.isOwnedBy(player)) {
            if (this.level() instanceof ServerLevel) {
                ItemStack mouthItem = this.getHeldItem();
                this.setHeldItem(ItemStack.EMPTY);
                if (!player.getInventory().add(mouthItem)) {
                    player.drop(mouthItem, false);
                }
            }

            return InteractionResult.SUCCESS;
        }

        if (this.isFood(itemInHand)) {
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            }

            return this.interactWithFood(player, hand, itemInHand);
        }

        if (this.isTame() && this.isOwnedBy(player) && this.getHeldItem().isEmpty()) {
            if (this.level() instanceof ServerLevel) {
                boolean sit = !this.isOrderedToSit();
                this.setOrderedToSit(sit);
                this.setInSittingPose(sit);
                this.getNavigation().stop();
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult interactWithFood(Player player, InteractionHand hand, ItemStack itemInHand) {
        if (this.getAge() > 0) {
            return InteractionResult.SUCCESS_SERVER;
        }

        boolean consumed = false;
        boolean wasTame = this.isTame();
        if (!this.isTame()) {
            if (!this.getHeldItem().isEmpty()) {
                this.dropHeldItem();
            }

            this.tame(player);
            this.level().broadcastEntityEvent(this, (byte) 7);
            consumed = true;
        }

        if (this.isBaby()) {
            if (!this.playerStartedGrowth) {
                this.setAge(-FED_OR_PARENTED_BABY_GROWTH_TICKS);
                this.playerStartedGrowth = true;
                this.growthInitialized = true;
            }

            this.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
            this.heal(FOOD_HEAL_AMOUNT);
            consumed = true;
        } else if (this.getHealth() < this.getMaxHealth()) {
            this.heal(FOOD_HEAL_AMOUNT);
            consumed = true;
        } else if (wasTame && this.canFallInLove()) {
            this.setInLove(player);
            consumed = true;
        }

        if (consumed) {
            this.usePlayerItem(player, hand, itemInHand);
            this.playEatingSound();
            this.gameEvent(net.minecraft.world.level.gameevent.GameEvent.EAT);
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected void playEatingSound() {
        this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.NEUTRAL, 0.7F, 1.0F);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.PITCHER_POD);
    }

    // endregion


    // region Item Holding

    private static final EntityDataAccessor<ItemStack> DATA_HELD_ITEM = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.ITEM_STACK);
    public static final int MOUTH_FOOD_WAIT_TICKS = 560;
    public static final int MOUTH_FOOD_EAT_TICKS = 40;
    public static final float FOOD_HEAL_AMOUNT = 8.0F;

    private int heldFoodTicks;

    private void tickHeldFood(ServerLevel level) {
        ItemStack mouthItem = this.getHeldItem();
        if (mouthItem.isEmpty() || !this.isFood(mouthItem) || this.getHealth() >= this.getMaxHealth()) {
            this.heldFoodTicks = 0;
            return;
        }

        this.heldFoodTicks++;
        if (this.heldFoodTicks >= MOUTH_FOOD_WAIT_TICKS && this.heldFoodTicks < MOUTH_FOOD_WAIT_TICKS + MOUTH_FOOD_EAT_TICKS) {
            level.sendParticles(
                new ItemParticleOption(ParticleTypes.ITEM, mouthItem.getItem()),
                this.getX(),
                this.getEyeY(),
                this.getZ(),
                2,
                0.15,
                0.1,
                0.15,
                0.02
            );
        }

        if (this.heldFoodTicks >= MOUTH_FOOD_WAIT_TICKS + MOUTH_FOOD_EAT_TICKS) {
            ItemStack remainder = mouthItem.copy();
            remainder.shrink(1);
            this.setHeldItem(remainder);
            this.heal(FOOD_HEAL_AMOUNT);
            this.playSound(SoundEvents.GENERIC_EAT.value(), 0.6F, 1.0F);
            this.gameEvent(net.minecraft.world.level.gameevent.GameEvent.EAT);
            this.heldFoodTicks = 0;
        }
    }

    public void dropHeldItem() {
        if (!hasHeldItem()) {
            return;
        }

        ItemStack heldItem = this.getHeldItem();
        this.setHeldItem(ItemStack.EMPTY);
        if (this.level() instanceof ServerLevel level) {
            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY() + 0.2, this.getZ(), heldItem);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    public ItemStack getHeldItem() {
        return this.entityData.get(DATA_HELD_ITEM);
    }

    public void setHeldItem(ItemStack item) {
        if (item.isEmpty()) {
            this.entityData.set(DATA_HELD_ITEM, ItemStack.EMPTY);
            this.heldFoodTicks = 0;
            return;
        }

        if (!canHoldItem()) {
            return;
        }

        this.entityData.set(DATA_HELD_ITEM, item.copy());
        this.heldFoodTicks = 0;
    }

    public boolean canHoldItem() {
        return !this.isBaby();
    }

    public boolean hasHeldItem() {
        return !this.getHeldItem().isEmpty();
    }

    // endregion


    // region Ageable

    public static final int DEFAULT_BABY_GROWTH_TICKS = 72000;
    public static final int FED_OR_PARENTED_BABY_GROWTH_TICKS = 24000;
    private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.4F, 0.4F);

    private boolean growthInitialized;
    private boolean playerStartedGrowth;

    private void initializeBabyGrowth(ServerLevel level) {
        if (!this.isBaby()) {
            this.growthInitialized = true;
            return;
        }

        if (this.growthInitialized) {
            return;
        }

        if (this.hasNearbyAdult(level)) {
            this.setAge(-FED_OR_PARENTED_BABY_GROWTH_TICKS);
        } else if (this.getAge() > -DEFAULT_BABY_GROWTH_TICKS) {
            this.setAge(-DEFAULT_BABY_GROWTH_TICKS);
        }

        this.growthInitialized = true;
    }

    private boolean hasNearbyAdult(ServerLevel level) {
        return !level.getEntitiesOfClass(
            BluePigeon.class,
            this.getBoundingBox().inflate(2.0),
            pigeon -> pigeon != this && pigeon.isAlive() && !pigeon.isBaby()
        ).isEmpty();
    }

    @Override
    public boolean canFallInLove() {
        return !this.isBaby() && this.getAge() == 0 && super.canFallInLove();
    }

    @Override
    public boolean canMate(Animal partner) {
        return partner instanceof BluePigeon pigeon
            && partner != this
            && !pigeon.isBaby()
            && !this.isBaby()
            && this.getAge() == 0
            && pigeon.getAge() == 0
            && this.isInLove()
            && pigeon.isInLove();
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
        ItemEntity egg = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.BLUE_PIGEON_EGG.get()));
        egg.setDefaultPickUpDelay();
        level.addFreshEntity(egg);
        this.finalizeSpawnChildFromBreeding(level, partner, null);
    }

    @Override
    public @Nullable BluePigeon getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntityTypes.BLUE_PIGEON.get().create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    protected int getBabyStartAge() {
        return -DEFAULT_BABY_GROWTH_TICKS;
    }

    @Override
    public void setBaby(boolean baby) {
        super.setBaby(baby);
        if (baby) {
            this.growthInitialized = false;
            this.setHeldItem(ItemStack.EMPTY);
        } else {
            this.growthInitialized = true;
            this.setBeggingForFood(false);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        ServerLevelAccessor level,
        net.minecraft.world.DifficultyInstance difficulty,
        EntitySpawnReason spawnReason,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
        this.growthInitialized = !this.isBaby();
        return data;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    // endregion


    // region EntityData

    private static final EntityDataAccessor<Boolean> DATA_DIGGING = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.BOOLEAN);

    public boolean isDigging() {
        return this.entityData.get(DATA_DIGGING);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DATA_DIGGING, digging);
    }

    public boolean isDiggingDown() {
        return this.isDigging() && this.getXRot() > 20.0F;
    }

    private static final EntityDataAccessor<Boolean> DATA_BEGGING_FOR_FOOD = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.BOOLEAN);

    public boolean isBeggingForFood() {
        return this.entityData.get(DATA_BEGGING_FOR_FOOD);
    }

    public void setBeggingForFood(boolean begging) {
        this.entityData.set(DATA_BEGGING_FOR_FOOD, begging);
    }

    // endregion


    // region Moving

    public static final int FATIGUE_LIMIT_TICKS = 2000;

    private static final EntityDataAccessor<Long> DATA_FATIGUE = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> DATA_FLYING = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_WATER_ESCAPE = SynchedEntityData.defineId(BluePigeon.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingOrGroundPathNavigation navigation = new FlyingOrGroundPathNavigation(this, level, this::shouldUseGroundNavigation);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setRequiredPathLength(48.0F);
        return navigation;
    }

    private boolean shouldUseGroundNavigation() {
        return !this.isFlying();
    }

    private void tickFatigue() {
        if (this.isWaterEscapeFlying()) {
            this.setFlying(true);
        }

        if (this.isFlying()) {
            this.incrementFatigue();
            if (!this.isWaterEscapeFlying() && this.getFatigue() > FATIGUE_LIMIT_TICKS) {
                this.setFlying(false);
            }
        } else {
            if (this.getFatigue() > 0L) {
                this.setFatigue(this.getFatigue() - 1L);
            }

            if (this.getFatigue() <= FATIGUE_LIMIT_TICKS) {
                this.setFlying(true);
            }
        }
    }

    private void incrementFatigue() {
        long fatigue = this.getFatigue();
        if (fatigue < Long.MAX_VALUE) {
            this.setFatigue(fatigue + 1L);
        }
    }

    public long getFatigue() {
        return this.entityData.get(DATA_FATIGUE);
    }

    public void setFatigue(long fatigue) {
        this.entityData.set(DATA_FATIGUE, Math.max(0L, fatigue));
    }

    public boolean isFlying() {
        return this.entityData.get(DATA_FLYING);
    }

    public void setFlying(boolean flying) {
        boolean changed = this.isFlying() != flying;
        this.entityData.set(DATA_FLYING, flying);
        if (changed) {
            this.navigation.stop();
        }
    }

    public boolean isWaterEscapeFlying() {
        return this.entityData.get(DATA_WATER_ESCAPE);
    }

    public void setWaterEscapeFlying(boolean waterEscapeFlying) {
        this.entityData.set(DATA_WATER_ESCAPE, waterEscapeFlying);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isFlying()) {
            this.travelFlying(travelVector, this.getSpeed());
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    // endregion


    // region Data storage

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FATIGUE, 0L);
        entityData.define(DATA_FLYING, true);
        entityData.define(DATA_WATER_ESCAPE, false);
        entityData.define(DATA_DIGGING, false);
        entityData.define(DATA_BEGGING_FOR_FOOD, false);
        entityData.define(DATA_HELD_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putLong("Fatigue", this.getFatigue());
        output.putBoolean("Flying", this.isFlying());
        output.putBoolean("WaterEscapeFlying", this.isWaterEscapeFlying());
        output.putBoolean("GrowthInitialized", this.growthInitialized);
        output.putBoolean("PlayerStartedGrowth", this.playerStartedGrowth);
        output.store("MouthItem", ItemStack.OPTIONAL_CODEC, this.getHeldItem());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFatigue(input.getLongOr("Fatigue", 0L));
        this.setFlying(input.getBooleanOr("Flying", true));
        this.setWaterEscapeFlying(input.getBooleanOr("WaterEscapeFlying", false));
        this.growthInitialized = input.getBooleanOr("GrowthInitialized", !this.isBaby());
        this.playerStartedGrowth = input.getBooleanOr("PlayerStartedGrowth", false);
        this.setHeldItem(input.read("MouthItem", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
    }

    // endregion


    // region Animation

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        BluePigeonAnimationController.registerControllers(this, controllers);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    // endregion


    // region AI

    @Override
    protected Brain<BluePigeon> makeBrain(Brain.Packed packedBrain) {
        return BluePigeonAi.makeBrain(this, packedBrain);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<BluePigeon> getBrain() {
        return (Brain<BluePigeon>) super.getBrain();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level() instanceof ServerLevel level) {
            this.initializeBabyGrowth(level);
            this.tickFatigue();
            this.tickHeldFood(level);
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        this.getBrain().tick(level, this);
        BluePigeonAi.updateActivity(this);
        super.customServerAiStep(level);
    }

    // endregion
}
