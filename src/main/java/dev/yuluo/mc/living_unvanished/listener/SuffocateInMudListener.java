package dev.yuluo.mc.living_unvanished.listener;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.attachment.SuffocateInMud;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public class SuffocateInMudListener {
    @SubscribeEvent
    public static void onEntityTickPost(EntityTickEvent.Post event) {
        var entity = event.getEntity();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        if (living.level().isClientSide()) {
            return;
        }

        SuffocateInMud.tick(living);
    }
}
