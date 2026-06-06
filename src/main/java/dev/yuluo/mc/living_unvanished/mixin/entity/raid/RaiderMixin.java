package dev.yuluo.mc.living_unvanished.mixin.entity.raid;

import dev.yuluo.mc.living_unvanished.entity.ai.goal.raider.AttackBluePigeonGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raider.class)
public abstract class RaiderMixin<T extends Raider> extends PatrollingMonster {
    protected RaiderMixin(EntityType<? extends PatrollingMonster> type, Level level) {
        super(type, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(EntityType<T> type, Level level, CallbackInfo ci) {
        targetSelector.addGoal(5, new AttackBluePigeonGoal((Raider) (Object) this));
    }
}
