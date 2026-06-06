package dev.yuluo.mc.living_unvanished.mixin.client.gui;

import dev.yuluo.mc.living_unvanished.attachment.SuffocateInMud;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Redirect(
        method = "extractAirBubbles",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAirSupply()I")
    )
    private int livingUnvanished$redirect$getAirSupply(Player player) {
        if (SuffocateInMud.shouldShowSuffocateAir(player)) {
            return SuffocateInMud.get(player);
        }
        return player.getAirSupply();
    }
}
