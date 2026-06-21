package dev.yuluo.mc.living_unvanished.client.animation.entity.blue_pigeon;

import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animatable.manager.AnimatableManager;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;

public final class BluePigeonAnimationController {
    private static final RawAnimation ADULT_SIT = RawAnimation.begin().thenLoop("idel");
    private static final RawAnimation ADULT_STAND = RawAnimation.begin().thenWait(20);
    private static final RawAnimation ADULT_GROUND_MOVE = RawAnimation.begin().thenLoop("move");
    private static final RawAnimation ADULT_TAKE_OFF = RawAnimation.begin()
        .then("move", LoopType.PLAY_ONCE)
        .then("start_fly", LoopType.PLAY_ONCE)
        .thenLoop("fly");
    private static final RawAnimation ADULT_FLY = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation ADULT_LAND = RawAnimation.begin()
        .then("end_fly", LoopType.PLAY_ONCE)
        .then("move", LoopType.PLAY_ONCE);
    private static final RawAnimation ADULT_PREEN_FEATHERS = RawAnimation.begin().then("preen_feathers", LoopType.PLAY_ONCE);
    private static final RawAnimation ADULT_SING = RawAnimation.begin().then("sing", LoopType.PLAY_ONCE);
    private static final RawAnimation ADULT_DIG_SIDE = RawAnimation.begin().thenLoop("explore_side");
    private static final RawAnimation ADULT_DIG_DOWN = RawAnimation.begin().thenLoop("explore_down");
    private static final RawAnimation ADULT_HURT = RawAnimation.begin().thenPlay("get_attacked");

    private static final RawAnimation BABY_SIT = RawAnimation.begin().thenLoop("idel");
    private static final RawAnimation BABY_MOVE = RawAnimation.begin().thenLoop("move");
    private static final RawAnimation BABY_BEG_FOOD = RawAnimation.begin().thenLoop("begfood");
    private static final RawAnimation BABY_HURT = RawAnimation.begin().thenPlay("get_attacked");

    private BluePigeonAnimationController() {
    }

    public static void registerControllers(BluePigeon pigeon, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
            new AnimationController<>("main", 5, new Handler(pigeon))
                .triggerableAnim("hurt", ADULT_HURT)
                .triggerableAnim("baby_hurt", BABY_HURT)
        );
    }

    private static void setAdultGroundAnimation(BluePigeon pigeon, AnimationTest<BluePigeon> state) {
        if (state.isMoving()) {
            state.setAnimation(ADULT_GROUND_MOVE);
            return;
        }

        if (isAdultStandingAnimation(state)) {
            if (!state.controller().hasAnimationFinished()) {
                return;
            }

            // Allow the same one-shot standing clip to be selected again.
            state.controller().reset();
        }

        state.setAnimation(selectAdultStandingAnimation(pigeon));
    }

    private static boolean isAdultStandingAnimation(AnimationTest<BluePigeon> state) {
        return state.isCurrentAnimation(ADULT_STAND)
            || state.isCurrentAnimation(ADULT_PREEN_FEATHERS)
            || state.isCurrentAnimation(ADULT_SING);
    }

    private static RawAnimation selectAdultStandingAnimation(BluePigeon pigeon) {
        return switch (pigeon.getRandom().nextInt(5)) {
            case 0 -> ADULT_PREEN_FEATHERS;
            case 1 -> ADULT_SING;
            default -> ADULT_STAND;
        };
    }

    private static class Handler implements AnimationController.AnimationStateHandler<BluePigeon> {
        private final BluePigeon pigeon;

        private boolean wasFlying;

        public Handler(BluePigeon pigeon) {
            this.pigeon = pigeon;
            this.wasFlying = pigeon.isFlying();
        }

        @Override
        public PlayState handle(AnimationTest<BluePigeon> animatable) {
            if (pigeon.isBaby()) {
                wasFlying = pigeon.isFlying();
                if (pigeon.isInSittingPose()) {
                    animatable.setAnimation(BABY_SIT);
                } else if (pigeon.isBeggingForFood()) {
                    animatable.setAnimation(BABY_BEG_FOOD);
                } else if (!pigeon.isFlying() && animatable.isMoving()) {
                    animatable.setAnimation(BABY_MOVE);
                } else {
                    return PlayState.STOP;
                }
            } else {
                if (pigeon.isInSittingPose()) {
                    wasFlying = pigeon.isFlying();
                    animatable.setAnimation(ADULT_SIT);
                } else if (pigeon.isDigging()) {
                    wasFlying = pigeon.isFlying();
                    animatable.setAnimation(pigeon.isDiggingDown() ? ADULT_DIG_DOWN : ADULT_DIG_SIDE);
                } else if (pigeon.isFlying()) {
                    if (wasFlying) {
                        if (!animatable.isCurrentAnimation(ADULT_TAKE_OFF)) {
                            animatable.setAnimation(ADULT_FLY);
                        }
                    } else {
                        wasFlying = true;
                        animatable.setAnimation(ADULT_TAKE_OFF);
                    }
                } else if (wasFlying) {
                    wasFlying = false;
                    animatable.setAnimation(ADULT_LAND);
                } else if (animatable.isCurrentAnimation(ADULT_LAND) && !animatable.controller().hasAnimationFinished()) {
                    return PlayState.CONTINUE;
                } else {
                    setAdultGroundAnimation(pigeon, animatable);
                }
            }

            return PlayState.CONTINUE;
        }
    }
}
