package dev.yuluo.mc.living_unvanished.client.listener;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.client.gui.IdentificationManualScreen;
import dev.yuluo.mc.living_unvanished.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = LivingUnvanished.MODID, value = Dist.CLIENT)
public final class RegisterMenuScreenListener {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.IDENTIFICATION_MANUAL.get(), IdentificationManualScreen::new);
    }
}
