package dev.yuluo.mc.living_unvanished.entity.ai.navigation;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FlyingOrGroundPathNavigation extends FlyingPathNavigation {
    @Getter
    private final GroundPathNavigation groundPathNavigation;

    private final Supplier<Boolean> shouldGround;

    public FlyingOrGroundPathNavigation(Mob mob, Level level, Supplier<Boolean> shouldGround) {
        super(mob, level);
        this.groundPathNavigation = new GroundPathNavigation(mob, level);
        this.shouldGround = shouldGround;
    }

    private boolean useGroundNavigation() {
        return this.shouldGround.get();
    }

    // region Properties

    @Override
    public void updatePathfinderMaxVisitedNodes() {
        super.updatePathfinderMaxVisitedNodes();
        this.groundPathNavigation.updatePathfinderMaxVisitedNodes();
    }

    @Override
    public void setRequiredPathLength(float length) {
        super.setRequiredPathLength(length);
        this.groundPathNavigation.setRequiredPathLength(length);
    }

    @Override
    public void resetMaxVisitedNodesMultiplier() {
        super.resetMaxVisitedNodesMultiplier();
        this.groundPathNavigation.resetMaxVisitedNodesMultiplier();
    }

    @Override
    public void setMaxVisitedNodesMultiplier(float maxVisitedNodesMultiplier) {
        super.setMaxVisitedNodesMultiplier(maxVisitedNodesMultiplier);
        this.groundPathNavigation.setMaxVisitedNodesMultiplier(maxVisitedNodesMultiplier);
    }

    @Override
    public void setSpeedModifier(double speedModifier) {
        super.setSpeedModifier(speedModifier);
        this.groundPathNavigation.setSpeedModifier(speedModifier);
    }

    @Override
    public void setCanFloat(boolean canFloat) {
        super.setCanFloat(canFloat);
        this.groundPathNavigation.setCanFloat(canFloat);
    }

    @Override
    public void setCanOpenDoors(boolean canOpenDoors) {
        super.setCanOpenDoors(canOpenDoors);
        this.groundPathNavigation.setCanOpenDoors(canOpenDoors);
    }

    // endregion

    // region Navigations

    @Override
    public @Nullable BlockPos getTargetPos() {
        return this.useGroundNavigation() ? this.groundPathNavigation.getTargetPos() : super.getTargetPos();
    }

    @Override
    public void recomputePath() {
        if (this.useGroundNavigation()) {
            this.groundPathNavigation.recomputePath();
        } else {
            super.recomputePath();
        }
    }

    @Override
    public @Nullable Path createPath(Stream<BlockPos> positions, int reachRange) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.createPath(positions, reachRange)
            : super.createPath(positions, reachRange);
    }

    @Override
    public @Nullable Path createPath(Set<BlockPos> positions, int reachRange) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.createPath(positions, reachRange)
            : super.createPath(positions, reachRange);
    }

    @Override
    public @Nullable Path createPath(BlockPos pos, int reachRange) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.createPath(pos, reachRange)
            : super.createPath(pos, reachRange);
    }

    @Override
    public @Nullable Path createPath(BlockPos pos, int reachRange, int maxPathLength) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.createPath(pos, reachRange, maxPathLength)
            : super.createPath(pos, reachRange, maxPathLength);
    }

    @Override
    public Path createPath(Entity target, int reachRange) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.createPath(target, reachRange)
            : super.createPath(target, reachRange);
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedModifier) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.moveTo(x, y, z, speedModifier)
            : super.moveTo(x, y, z, speedModifier);
    }

    @Override
    public boolean moveTo(double x, double y, double z, int reachRange, double speedModifier) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.moveTo(x, y, z, reachRange, speedModifier)
            : super.moveTo(x, y, z, reachRange, speedModifier);
    }

    @Override
    public boolean moveTo(Entity target, double speedModifier) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.moveTo(target, speedModifier)
            : super.moveTo(target, speedModifier);
    }

    @Override
    public boolean moveTo(@Nullable Path newPath, double speedModifier) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.moveTo(newPath, speedModifier)
            : super.moveTo(newPath, speedModifier);
    }

    @Override
    public @Nullable Path getPath() {
        return this.useGroundNavigation() ? this.groundPathNavigation.getPath() : super.getPath();
    }

    @Override
    public void tick() {
        if (this.useGroundNavigation()) {
            this.groundPathNavigation.tick();
        } else {
            super.tick();
        }
    }

    @Override
    public boolean isDone() {
        return this.useGroundNavigation() ? this.groundPathNavigation.isDone() : super.isDone();
    }

    @Override
    public boolean isInProgress() {
        return this.useGroundNavigation() ? this.groundPathNavigation.isInProgress() : super.isInProgress();
    }

    @Override
    public void stop() {
        super.stop();
        this.groundPathNavigation.stop();
    }

    @Override
    public boolean canCutCorner(PathType pathType) {
        return this.useGroundNavigation() ? this.groundPathNavigation.canCutCorner(pathType) : super.canCutCorner(pathType);
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.isStableDestination(pos)
            : super.isStableDestination(pos);
    }

    @Override
    public NodeEvaluator getNodeEvaluator() {
        return this.useGroundNavigation() ? this.groundPathNavigation.getNodeEvaluator() : super.getNodeEvaluator();
    }

    @Override
    public boolean canFloat() {
        return this.useGroundNavigation() ? this.groundPathNavigation.canFloat() : super.canFloat();
    }

    @Override
    public boolean shouldRecomputePath(BlockPos pos) {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.shouldRecomputePath(pos)
            : super.shouldRecomputePath(pos);
    }

    @Override
    public float getMaxDistanceToWaypoint() {
        return this.useGroundNavigation()
            ? this.groundPathNavigation.getMaxDistanceToWaypoint()
            : super.getMaxDistanceToWaypoint();
    }

    @Override
    public boolean isStuck() {
        return this.useGroundNavigation() ? this.groundPathNavigation.isStuck() : super.isStuck();
    }

    @Override
    public boolean canNavigateGround() {
        return this.useGroundNavigation();
    }

    // endregion
}
