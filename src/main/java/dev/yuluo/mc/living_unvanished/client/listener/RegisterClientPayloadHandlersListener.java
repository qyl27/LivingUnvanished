package dev.yuluo.mc.living_unvanished.client.listener;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.client.util.LeftoverPageHelper;
import dev.yuluo.mc.living_unvanished.network.OpenLeftoverPagePayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = LivingUnvanished.MODID, value = Dist.CLIENT)
public final class RegisterClientPayloadHandlersListener {
    @SubscribeEvent
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(OpenLeftoverPagePayload.TYPE,
            (payload, context) -> context.enqueueWork(() -> LeftoverPageHelper.open(payload.contentKey()))
        );
    }
}
