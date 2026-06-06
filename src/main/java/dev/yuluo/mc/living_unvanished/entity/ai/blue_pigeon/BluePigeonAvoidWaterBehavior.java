package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;

public class BluePigeonAvoidWaterBehavior extends Behavior<BluePigeon> {
    public BluePigeonAvoidWaterBehavior() {
        super(
            Map.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
            ),
            40
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BluePigeon pigeon) {
        return !pigeon.isOrderedToSit() && this.shouldAvoidWater(level, pigeon);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BluePigeon pigeon, long timestamp) {
        return !pigeon.isOrderedToSit() && this.shouldAvoidWater(level, pigeon);
    }

    @Override
    protected void start(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setWaterEscapeFlying(true);
        this.moveToLand(level, pigeon);
    }

    @Override
    protected void tick(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setWaterEscapeFlying(true);
        if (pigeon.getNavigation().isDone()) {
            this.moveToLand(level, pigeon);
        }
    }

    @Override
    protected void stop(ServerLevel level, BluePigeon pigeon, long timestamp) {
        if (!this.shouldAvoidWater(level, pigeon)) {
            pigeon.setWaterEscapeFlying(false);
        }
    }

    private void moveToLand(ServerLevel level, BluePigeon pigeon) {
        Optional<BlockPos> land = BlockPos.findClosestMatch(pigeon.blockPosition(), 12, 6, pos -> this.isLandingSpot(level, pos));
        land.ifPresent(pos -> BehaviorUtils.setWalkAndLookTargetMemories(pigeon, pos, 1.35F, 1));
    }

    private boolean shouldAvoidWater(ServerLevel level, BluePigeon pigeon) {
        return pigeon.isInWater() || this.hasWaterNearby(level, pigeon.blockPosition(), 2);
    }

    private boolean isLandingSpot(ServerLevel level, BlockPos pos) {
        if (level.getFluidState(pos).is(FluidTags.WATER) || !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
            return false;
        }

        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return !level.getFluidState(belowPos).is(FluidTags.WATER)
            && !below.getCollisionShape(level, belowPos).isEmpty()
            && !this.hasWaterNearby(level, pos, 2);
    }

    private boolean hasWaterNearby(ServerLevel level, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -1, -radius), center.offset(radius, 0, radius))) {
            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }
}
