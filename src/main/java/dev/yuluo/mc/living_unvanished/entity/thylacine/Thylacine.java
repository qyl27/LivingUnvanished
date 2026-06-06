package dev.yuluo.mc.living_unvanished.entity.thylacine;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import dev.yuluo.mc.living_unvanished.client.animation.entity.thylacine.ThylacineAnimationController;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;

public class Thylacine extends TamableAnimal implements GeoEntity {
    private static final float FOOD_HEAL_AMOUNT = 4.0F;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public Thylacine(EntityType<? extends Thylacine> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.ARMOR, 0.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3)
            .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    // region Interacting

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);

        if (!this.isTame() && isTamingItem(itemInHand)) {
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            }

            this.usePlayerItem(player, hand, itemInHand);
            if (this.getRandom().nextInt(3) == 0) {
                this.tame(player);
                this.setOrderedToSit(true);
                this.setInSittingPose(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            return InteractionResult.SUCCESS_SERVER;
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (this.canEquipBodyArmor(itemInHand)) {
                return this.equipBodyArmor(player, itemInHand);
            }

            if (itemInHand.is(Items.SHEARS) && this.hasBodyArmor()) {
                return this.removeBodyArmor(player, hand, itemInHand);
            }

            if (this.isFood(itemInHand) && this.getHealth() < this.getMaxHealth()) {
                if (this.level().isClientSide()) {
                    return InteractionResult.CONSUME;
                }

                this.usePlayerItem(player, hand, itemInHand);
                this.heal(FOOD_HEAL_AMOUNT);
                this.playEatingSound();
                this.gameEvent(GameEvent.EAT);
                return InteractionResult.SUCCESS_SERVER;
            }

            if (!this.isFood(itemInHand)) {
                if (this.level() instanceof ServerLevel) {
                    boolean sit = !this.isOrderedToSit();
                    this.setOrderedToSit(sit);
                    this.setInSittingPose(sit);
                    this.getNavigation().stop();
                    this.setTarget(null);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    public static boolean isTamingItem(ItemStack itemStack) {
        return itemStack.is(Items.BONE);
    }

    public static boolean isTemptingItem(ItemStack itemStack) {
        return isTamingItem(itemStack) || itemStack.is(ItemTags.WOLF_FOOD);
    }

    private boolean canEquipBodyArmor(ItemStack itemStack) {
        return itemStack.is(Items.WOLF_ARMOR)
            && this.getItemBySlot(EquipmentSlot.BODY).isEmpty()
            && this.isEquippableInSlot(itemStack, EquipmentSlot.BODY);
    }

    private InteractionResult equipBodyArmor(Player player, ItemStack itemInHand) {
        if (this.level().isClientSide()) {
            return InteractionResult.CONSUME;
        }

        this.setItemSlot(EquipmentSlot.BODY, itemInHand.copyWithCount(1));
        itemInHand.consume(1, player);
        return InteractionResult.SUCCESS_SERVER;
    }

    private boolean hasBodyArmor() {
        return !this.getItemBySlot(EquipmentSlot.BODY).isEmpty();
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.BODY || super.canUseSlot(slot);
    }

    private InteractionResult removeBodyArmor(Player player, InteractionHand hand, ItemStack itemInHand) {
        if (this.level().isClientSide()) {
            return InteractionResult.CONSUME;
        }

        ItemStack bodyArmor = this.getItemBySlot(EquipmentSlot.BODY).copy();
        this.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
        if (!player.getInventory().add(bodyArmor)) {
            player.drop(bodyArmor, false);
        }

        itemInHand.hurtAndBreak(1, player, hand);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.WOLF_FOOD);
    }

    // endregion


    // region Living

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && this.getTarget() != null) {
            this.setAggressive(true);
        }

        return hurt;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hurt = super.doHurtTarget(level, target);
        if (hurt) {
            this.triggerAnim("main", this.onGround() ? "attack" : "jump_attack");
        }

        return hurt;
    }

    // endregion


    // region Ageable

    @Override
    public boolean canMate(Animal partner) {
        return partner instanceof Thylacine thylacine
            && thylacine != this
            && thylacine.isTame()
            && this.isTame()
            && super.canMate(partner);
    }

    @Override
    public @Nullable Thylacine getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntityTypes.THYLACINE.get().create(level, EntitySpawnReason.BREEDING);
    }

    // endregion


    // region Animation

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        ThylacineAnimationController.registerControllers(this, controllers);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    // endregion


    // region AI

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public boolean shouldPlayRunAnimation() {
        return this.isAggressive() || this.getTarget() != null;
    }

    // endregion
}
