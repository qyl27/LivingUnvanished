package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.gui.IdentificationManualMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> REGISTRY =
        DeferredRegister.create(BuiltInRegistries.MENU, LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static final DeferredHolder<MenuType<?>, MenuType<IdentificationManualMenu>> IDENTIFICATION_MANUAL =
        REGISTRY.register(
            "identification_manual",
            () -> IMenuTypeExtension.create((containerId, inventory, data) -> new IdentificationManualMenu(containerId, inventory))
        );
}
