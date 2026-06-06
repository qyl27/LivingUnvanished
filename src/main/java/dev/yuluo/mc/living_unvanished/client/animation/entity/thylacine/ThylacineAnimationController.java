package dev.yuluo.mc.living_unvanished.client.animation.entity.thylacine;

import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.state.AnimationTest;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.entity.thylacine.Thylacine;
import lombok.AllArgsConstructor;

public final class ThylacineAnimationController {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("move");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation SIT_DOWN = RawAnimation.begin().then("sit_down", LoopType.PLAY_ONCE);
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation SIT_UP = RawAnimation.begin().then("sit_up", LoopType.PLAY_ONCE);
    private static final RawAnimation WARNING = RawAnimation.begin().thenLoop("warning");
    private static final RawAnimation ATTACK = RawAnimation.begin().then("attack", LoopType.PLAY_ONCE);
    private static final RawAnimation JUMP_ATTACK = RawAnimation.begin().then("jump_attack", LoopType.PLAY_ONCE);
    private static final RawAnimation TICKLE = RawAnimation.begin().then("tickle", LoopType.PLAY_ONCE);

    private ThylacineAnimationController() {
    }

    public static void registerControllers(Thylacine thylacine, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
            new AnimationController<>("main", 5, new Handler(thylacine))
                .triggerableAnim("attack", ATTACK)
                .triggerableAnim("jump_attack", JUMP_ATTACK)
                .triggerableAnim("tickle", TICKLE)
        );
    }

    private static class Handler implements AnimationController.AnimationStateHandler<Thylacine> {
        private final Thylacine thylacine;

        private boolean wasSitting;

        public Handler(Thylacine thylacine) {
            this.thylacine = thylacine;
            this.wasSitting = thylacine.isInSittingPose();
        }

        @Override
        public PlayState handle(AnimationTest<Thylacine> animatable) {
            if (thylacine.isInSittingPose()) {
                if (!wasSitting) {
                    wasSitting = true;
                    animatable.setAnimation(SIT_DOWN);
                } else if (animatable.isCurrentAnimation(SIT_DOWN) && !animatable.controller().hasAnimationFinished()) {
                    return PlayState.CONTINUE;
                } else {
                    animatable.setAnimation(SIT);
                }
            } else if (wasSitting) {
                wasSitting = false;
                animatable.setAnimation(SIT_UP);
            } else if (animatable.isCurrentAnimation(SIT_UP) && !animatable.controller().hasAnimationFinished()) {
                return PlayState.CONTINUE;
            } else if (thylacine.isAggressive() && !animatable.isMoving()) {
                animatable.setAnimation(WARNING);
            } else if (animatable.isMoving()) {
                animatable.setAnimation(thylacine.shouldPlayRunAnimation() ? RUN : WALK);
            } else {
                animatable.setAnimation(IDLE);
            }
            return PlayState.CONTINUE;
        }
    }
}
