package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.block.entity.ConditionalBrushableBlockEntity;
import dev.yuluo.mc.living_unvanished.block.entity.MauritiusBluePigeonEggBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = LivingUnvanished.MODID)
public final class ModBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    @SubscribeEvent
    public static void addValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(
            BlockEntityType.BRUSHABLE_BLOCK,
            ModBlocks.SUSPICIOUS_MUD.get(),
            ModBlocks.SUSPICIOUS_RED_SAND.get()
        );
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConditionalBrushableBlockEntity>> CONDITIONAL_BRUSHABLE_BLOCK = REGISTRY.register(
        "conditional_brushable_block",
        () -> new BlockEntityType<>(ConditionalBrushableBlockEntity::new, ModBlocks.SUSPICIOUS_ROTTEN_WOOD.get())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MauritiusBluePigeonEggBlockEntity>> MAURITIUS_BLUE_PIGEON_EGG = REGISTRY.register(
        "mauritius_blue_pigeon_egg",
        () -> new BlockEntityType<>(MauritiusBluePigeonEggBlockEntity::new, ModBlocks.BLUE_PIGEON_EGG.get())
    );
}
