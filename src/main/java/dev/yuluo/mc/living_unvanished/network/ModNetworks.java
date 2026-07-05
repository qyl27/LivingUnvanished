package dev.yuluo.mc.living_unvanished.network;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class ModNetworks {
    public static final String PROTOCOL_VERSION = "1";

    private ModNetworks() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(PROTOCOL_VERSION)
            .playToClient(OpenLeftoverPagePayload.TYPE, OpenLeftoverPagePayload.STREAM_CODEC);
    }
}
