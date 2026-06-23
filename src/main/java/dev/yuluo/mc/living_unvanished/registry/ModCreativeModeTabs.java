package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.util.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    @SubscribeEvent
    public static void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModItems.BLUE_PIGEON_EGG);
            event.accept(ModItems.SUSPICIOUS_MUD);
            event.accept(ModItems.SUSPICIOUS_RED_SAND);
            event.accept(ModItems.SUSPICIOUS_ROTTEN_WOOD);
        } else if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.RESTORATION_TABLE);
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.MUD_SLURRY_BUCKET);
            event.accept(ModItems.IDENTIFICATION_MANUAL);
            event.accept(ModItems.MEMOIR);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BLUE_PIGEON_EGG);
            event.accept(ModItems.STRANGE_SKULL);
            event.accept(ModItems.AVIAN_SKULL);
            event.accept(ModItems.BEAST_SKULL);
            event.accept(ModItems.STRANGE_FEATHER);
            event.accept(ModItems.BLUE_CONTOUR_FEATHER);
            event.accept(ModItems.ORANGE_CONTOUR_FEATHER);
            event.accept(ModItems.STRANGE_BONES);
            event.accept(ModItems.KEEL);
            event.accept(ModItems.RIBS);
            event.accept(ModItems.FEMUR);
            event.accept(ModItems.STRANGE_LEATHER);
            event.accept(ModItems.STRIPED_LEATHER);
            event.accept(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_1);
            event.accept(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_2);
            event.accept(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_3);
            event.accept(ModItems.THYLACINE_LEFTOVER_PAGE_1);
            event.accept(ModItems.THYLACINE_LEFTOVER_PAGE_2);
            event.accept(ModItems.THYLACINE_LEFTOVER_PAGE_3);
        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.BLUE_PIGEON_SPAWN_EGG);
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
        REGISTRY.register(
            "living_unvanished",
            () -> CreativeModeTab.builder()
                .title(Component.translatable(ModConstants.Translations.CREATIVE_TAB_KEY))
                .icon(ModItems.BLUE_PIGEON_SPAWN_EGG::toStack)
                .displayItems(
                    (parameters, output) ->
                        ModItems.REGISTRY.getEntries().forEach(
                            item -> output.accept(item.get())
                        )
                )
                .build()
        );
}
