package dev.yuluo.mc.living_unvanished.item;

import dev.yuluo.mc.living_unvanished.util.ModConstants;
import dev.yuluo.mc.living_unvanished.gui.IdentificationManualMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class IdentificationManualItem extends Item {
    public IdentificationManualItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(
                new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) -> new IdentificationManualMenu(containerId, inventory),
                    Component.translatable(ModConstants.Translations.IDENTIFICATION_MANUAL)
                )
            );
        }

        return InteractionResult.SUCCESS;
    }
}
