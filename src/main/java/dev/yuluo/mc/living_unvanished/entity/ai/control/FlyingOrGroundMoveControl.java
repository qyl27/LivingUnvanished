package dev.yuluo.mc.living_unvanished.entity.ai.control;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;

import java.util.function.Supplier;

public class FlyingOrGroundMoveControl extends FlyingMoveControl {
    private final SyncableMoveControl groundMoveControl;
    private final Supplier<Boolean> shouldGround;

    public FlyingOrGroundMoveControl(Mob mob, int maxTurn, boolean hoversInPlace, Supplier<Boolean> shouldGround) {
        super(mob, maxTurn, hoversInPlace);
        this.groundMoveControl = new SyncableMoveControl(mob);
        this.shouldGround = shouldGround;
    }

    private boolean useGroundMoveControl() {
        return this.shouldGround.get();
    }

    @Override
    public boolean hasWanted() {
        return this.useGroundMoveControl() ? this.groundMoveControl.hasWanted() : super.hasWanted();
    }

    @Override
    public double getSpeedModifier() {
        return this.useGroundMoveControl() ? this.groundMoveControl.getSpeedModifier() : super.getSpeedModifier();
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speed) {
        super.setWantedPosition(x, y, z, speed);
        this.operation = MoveControl.Operation.MOVE_TO;
        this.groundMoveControl.setWantedPosition(x, y, z, speed);
    }

    @Override
    public void strafe(float forward, float strafe) {
        super.strafe(forward, strafe);
        this.groundMoveControl.strafe(forward, strafe);
    }

    @Override
    public void tick() {
        if (this.useGroundMoveControl()) {
            this.groundMoveControl.tick();
            this.groundMoveControl.syncTo(this);
            return;
        }

        super.tick();
        this.groundMoveControl.syncFrom(this);
    }

    @Override
    public double getWantedX() {
        return this.useGroundMoveControl() ? this.groundMoveControl.getWantedX() : super.getWantedX();
    }

    @Override
    public double getWantedY() {
        return this.useGroundMoveControl() ? this.groundMoveControl.getWantedY() : super.getWantedY();
    }

    @Override
    public double getWantedZ() {
        return this.useGroundMoveControl() ? this.groundMoveControl.getWantedZ() : super.getWantedZ();
    }

    private static class SyncableMoveControl extends MoveControl {
        private SyncableMoveControl(Mob mob) {
            super(mob);
        }

        private void syncFrom(FlyingOrGroundMoveControl moveControl) {
            this.operation = moveControl.operation;
            this.speedModifier = moveControl.speedModifier;
            this.strafeForwards = moveControl.strafeForwards;
            this.strafeRight = moveControl.strafeRight;
            this.wantedX = moveControl.wantedX;
            this.wantedY = moveControl.wantedY;
            this.wantedZ = moveControl.wantedZ;
        }

        private void syncTo(FlyingOrGroundMoveControl moveControl) {
            moveControl.operation = this.operation;
            moveControl.speedModifier = this.speedModifier;
            moveControl.strafeForwards = this.strafeForwards;
            moveControl.strafeRight = this.strafeRight;
            moveControl.wantedX = this.wantedX;
            moveControl.wantedY = this.wantedY;
            moveControl.wantedZ = this.wantedZ;
        }
    }
}
