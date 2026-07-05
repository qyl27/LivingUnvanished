package dev.yuluo.mc.living_unvanished;

import dev.yuluo.mc.living_unvanished.registry.ModBlockEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModAttachmentTypes;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModCreativeModeTabs;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import dev.yuluo.mc.living_unvanished.registry.ModMenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(LivingUnvanished.MODID)
public class LivingUnvanished {
    public static final String MODID = "living_unvanished";

    public LivingUnvanished(IEventBus bus) {
        ModBlocks.register(bus);
        ModBlockEntityTypes.register(bus);
        ModItems.register(bus);
        ModMenuTypes.register(bus);
        ModCreativeModeTabs.register(bus);
        ModEntityTypes.register(bus);
        ModAttachmentTypes.register(bus);
    }
}
