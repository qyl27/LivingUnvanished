package dev.yuluo.mc.living_unvanished.client.listener;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.client.renderer.entity.blue_pigeon.BluePigeonRenderer;
import dev.yuluo.mc.living_unvanished.client.renderer.entity.thylacine.ThylacineRenderer;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class RegisterRendererListener {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BLUE_PIGEON.get(), BluePigeonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.THYLACINE.get(), ThylacineRenderer::new);
    }
}
