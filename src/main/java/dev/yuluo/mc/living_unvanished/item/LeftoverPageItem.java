package dev.yuluo.mc.living_unvanished.item;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.network.OpenLeftoverPagePayload;
import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

@Getter
public class LeftoverPageItem extends Item {
    private final String contentKey;

    public LeftoverPageItem(Properties properties, String species, int page) {
        super(properties);
        this.contentKey = "leftover_page." + LivingUnvanished.MODID + "." + species + "." + page;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenLeftoverPagePayload(this.contentKey));
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResult.SUCCESS;
    }
}
