package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import java.util.List;
import java.util.Map;

import dev.yuluo.mc.living_unvanished.registry.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public class BluePigeonBegForFoodBehavior extends Behavior<BluePigeon> {
    private static final double START_DISTANCE_SQR = 16.0;
    private static final double STOP_DISTANCE_SQR = 25.0;
    private static final float SPEED_MODIFIER = 1.0F;
    private static final int CLOSE_ENOUGH_DISTANCE = 2;

    private @Nullable LivingEntity target;

    public BluePigeonBegForFoodBehavior() {
        super(
            Map.of(
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.REGISTERED,
                MemoryModuleType.NEAREST_PLAYERS, MemoryStatus.REGISTERED,
                MemoryModuleType.HURT_BY, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
            )
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BluePigeon pigeon) {
        if (!canBegForFood(pigeon)) {
            return false;
        }

        this.target = this.findTarget(pigeon, START_DISTANCE_SQR);
        return this.target != null;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BluePigeon pigeon, long timestamp) {
        if (!canBegForFood(pigeon)) {
            return false;
        }

        this.target = this.findTarget(pigeon, STOP_DISTANCE_SQR);
        return this.target != null;
    }

    @Override
    protected boolean timedOut(long timestamp) {
        return false;
    }

    @Override
    protected void start(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setBeggingForFood(true);
        this.updateTarget(pigeon);
    }

    @Override
    protected void tick(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setBeggingForFood(true);
        this.updateTarget(pigeon);
    }

    @Override
    protected void stop(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setBeggingForFood(false);
        pigeon.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pigeon.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        this.target = null;
    }

    private void updateTarget(BluePigeon pigeon) {
        if (this.target != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(pigeon, this.target, SPEED_MODIFIER, CLOSE_ENOUGH_DISTANCE);
        }
    }

    private static boolean canBegForFood(BluePigeon pigeon) {
        return pigeon.isBaby()
            && !pigeon.isOrderedToSit()
            && !pigeon.isLeashed()
            && !pigeon.isPassenger()
            && !pigeon.isWaterEscapeFlying();
    }

    private @Nullable LivingEntity findTarget(BluePigeon pigeon, double maxDistanceSqr) {
        LivingEntity player = this.findNearestBeggablePlayer(pigeon, maxDistanceSqr);
        return player != null ? player : this.findNearestAdultBluePigeon(pigeon, maxDistanceSqr);
    }

    private @Nullable LivingEntity findNearestBeggablePlayer(BluePigeon pigeon, double maxDistanceSqr) {
        LivingEntity target = null;
        double nearestDistanceSqr = maxDistanceSqr;

        for (Player candidate : pigeon.getBrain().getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of())) {
            if (!this.isPlayer(candidate)) {
                continue;
            }

            if (!candidate.getMainHandItem().is(ModTags.Items.BLUE_PIGEON_FOOD)
                && !candidate.getOffhandItem().is(ModTags.Items.BLUE_PIGEON_FOOD)) {
                continue;
            }

            double distanceSqr = candidate.distanceToSqr(pigeon);
            if (distanceSqr <= nearestDistanceSqr) {
                target = candidate;
                nearestDistanceSqr = distanceSqr;
            }
        }

        return target;
    }

    private @Nullable LivingEntity findNearestAdultBluePigeon(BluePigeon pigeon, double maxDistanceSqr) {
        LivingEntity target = null;
        double nearestDistanceSqr = maxDistanceSqr;

        for (LivingEntity candidate : pigeon.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(List.of())) {
            if (!this.isAdultBluePigeon(pigeon, candidate)) {
                continue;
            }

            double distanceSqr = candidate.distanceToSqr(pigeon);
            if (distanceSqr <= nearestDistanceSqr) {
                target = candidate;
                nearestDistanceSqr = distanceSqr;
            }
        }

        return target;
    }

    private boolean isAdultBluePigeon(BluePigeon pigeon, LivingEntity target) {
        return target instanceof BluePigeon bluePigeon
            && bluePigeon != pigeon
            && bluePigeon.isAlive()
            && !bluePigeon.isBaby();
    }

    private boolean isPlayer(LivingEntity target) {
        return target instanceof Player player && player.isAlive() && !player.isSpectator();
    }
}
