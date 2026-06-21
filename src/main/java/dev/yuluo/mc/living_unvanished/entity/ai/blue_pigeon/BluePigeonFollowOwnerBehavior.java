package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BluePigeonFollowOwnerBehavior extends Behavior<BluePigeon> {
    private static final double START_DISTANCE_SQR = 16.0;
    private static final int TELEPORT_DISTANCE_SQR = 144;

    public BluePigeonFollowOwnerBehavior() {
        super(
            Map.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
            ),
            20
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BluePigeon pigeon) {
        LivingEntity owner = pigeon.getOwner();
        return owner != null
            && pigeon.isTame()
            && !pigeon.isOrderedToSit()
            && !pigeon.isLeashed()
            && !pigeon.isPassenger()
            && !owner.isSpectator()
            && pigeon.distanceToSqr(owner) > START_DISTANCE_SQR;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BluePigeon pigeon, long timestamp) {
        return this.checkExtraStartConditions(level, pigeon);
    }

    @Override
    protected void tick(ServerLevel level, BluePigeon pigeon, long timestamp) {
        LivingEntity owner = pigeon.getOwner();
        if (owner == null) {
            return;
        }

        if (pigeon.distanceToSqr(owner) >= TELEPORT_DISTANCE_SQR) {
            pigeon.tryToTeleportToOwner();
        } else {
            BehaviorUtils.setWalkAndLookTargetMemories(pigeon, owner, 1.25F, 2);
        }
    }
}
