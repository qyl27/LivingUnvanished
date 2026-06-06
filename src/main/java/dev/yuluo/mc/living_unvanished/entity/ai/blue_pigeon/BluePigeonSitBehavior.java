package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BluePigeonSitBehavior extends Behavior<BluePigeon> {
    public BluePigeonSitBehavior() {
        super(
            Map.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
            )
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BluePigeon pigeon) {
        return pigeon.isOrderedToSit();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BluePigeon pigeon, long timestamp) {
        return pigeon.isOrderedToSit();
    }

    @Override
    protected void start(ServerLevel level, BluePigeon pigeon, long timestamp) {
        stopMoving(pigeon);
    }

    @Override
    protected void tick(ServerLevel level, BluePigeon pigeon, long timestamp) {
        stopMoving(pigeon);
    }

    private static void stopMoving(BluePigeon pigeon) {
        pigeon.getNavigation().stop();
        pigeon.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pigeon.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        pigeon.setDeltaMovement(pigeon.getDeltaMovement().multiply(0.0, 1.0, 0.0));
    }
}
