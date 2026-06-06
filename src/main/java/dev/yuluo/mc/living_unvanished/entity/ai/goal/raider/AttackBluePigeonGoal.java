package dev.yuluo.mc.living_unvanished.entity.ai.goal.raider;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.raid.Raider;

public class AttackBluePigeonGoal extends NearestAttackableTargetGoal<BluePigeon> {
    public AttackBluePigeonGoal(Raider raider) {
        super(raider, BluePigeon.class, false);
    }
}
