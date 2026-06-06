package dev.yuluo.mc.living_unvanished.registry;

import com.mojang.serialization.Codec;
import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.Nullable;

public final class ModAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SUFFOCATE_IN_MUD =
        REGISTRY.register(
            "suffocate_in_mud",
            () -> AttachmentType.builder(() -> 300)
                .serialize(Codec.INT.fieldOf("suffocate_in_mud"))
                .sync(ByteBufCodecs.VAR_INT)
                .build()
        );
}
