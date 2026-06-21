package dev.yuluo.mc.living_unvanished.data;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.data.language.ModZhCnLanguageProvider;
import dev.yuluo.mc.living_unvanished.data.language.ModEnUsLanguageProvider;
import dev.yuluo.mc.living_unvanished.registry.ModDatapackRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class ModData {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModLootTableProvider::new);
        event.createProvider((output, registries) -> new DatapackBuiltinEntriesProvider(output, registries, ModDatapackRegistries.BUILDER, Set.of(LivingUnvanished.MODID)));
        event.createProvider(ModEnUsLanguageProvider::new);
        event.createProvider(ModZhCnLanguageProvider::new);
    }
}
