package dev.yuluo.mc.living_unvanished.network;

import dev.yuluo.mc.living_unvanished.util.IdHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record OpenLeftoverPagePayload(String contentKey) implements CustomPacketPayload {
    public static final Type<OpenLeftoverPagePayload> TYPE = new Type<>(IdHelper.modLoc("open_leftover_page"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenLeftoverPagePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        OpenLeftoverPagePayload::contentKey,
        OpenLeftoverPagePayload::new
    );

    @Override
    public Type<OpenLeftoverPagePayload> type() {
        return TYPE;
    }
}
