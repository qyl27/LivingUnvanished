package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import java.util.List;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public class BluePigeonAi {
    private static final List<SensorType<? extends Sensor<? super BluePigeon>>> SENSOR_TYPES = List.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.NEAREST_PLAYERS,
        SensorType.HURT_BY
    );

    private static final Brain.Provider<BluePigeon> BRAIN_PROVIDER = Brain.provider(
        SENSOR_TYPES,
        pigeon -> List.of(initCoreActivity(), initIdleActivity())
    );

    public static Brain<BluePigeon> makeBrain(BluePigeon pigeon, Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(pigeon, packedBrain);
    }

    private static ActivityData<BluePigeon> initCoreActivity() {
        return ActivityData.<BluePigeon>create(
            Activity.CORE,
            0,
            ImmutableList.of(
                new BluePigeonSitBehavior(),
                new BluePigeonAvoidWaterBehavior(),
                new AnimalPanic<BluePigeon>(1.6F, -2),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
            )
        );
    }

    private static ActivityData<BluePigeon> initIdleActivity() {
        return ActivityData.<BluePigeon>create(
            Activity.IDLE,
            0,
            ImmutableList.of(
                new BluePigeonFollowOwnerBehavior(),
                new BluePigeonBegForFoodBehavior(),
                new AnimalMakeLove(ModEntityTypes.BLUE_PIGEON.get(), 1.0F, 2),
                new BluePigeonDigBehavior(),
                new RunOne<>(
                    ImmutableList.of(
                        Pair.of(RandomStroll.fly(1.0F), 3),
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                    )
                )
            )
        );
    }

    public static void updateActivity(BluePigeon pigeon) {
        pigeon.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}
