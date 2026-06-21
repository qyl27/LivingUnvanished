package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.entity.thylacine.Thylacine;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class ModEntityTypes {
    public static final DeferredRegister.Entities REGISTRY = DeferredRegister.createEntities(LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BLUE_PIGEON.get(), BluePigeon.createAttributes().build());
        event.put(THYLACINE.get(), Thylacine.createAttributes().build());
    }

    public static final DeferredHolder<EntityType<?>, EntityType<BluePigeon>> BLUE_PIGEON =
        REGISTRY.registerEntityType(
            "mauritius_blue_pigeon",
            BluePigeon::new,
            MobCategory.CREATURE,
            builder -> builder
                .sized(0.6F, 0.6F)
                .eyeHeight(0.35F)
                .clientTrackingRange(8)
                .updateInterval(3)
                .noLootTable()
        );

    public static final DeferredHolder<EntityType<?>, EntityType<Thylacine>> THYLACINE =
        REGISTRY.registerEntityType(
            "thylacine",
            Thylacine::new,
            MobCategory.CREATURE,
            builder -> builder
                .sized(0.9F, 0.85F)
                .eyeHeight(0.65F)
                .clientTrackingRange(8)
                .updateInterval(3)
                .noLootTable()
        );
}
