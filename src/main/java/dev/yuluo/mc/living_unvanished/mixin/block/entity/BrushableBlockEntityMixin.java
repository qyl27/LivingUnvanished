package dev.yuluo.mc.living_unvanished.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yuluo.mc.living_unvanished.block.ConditionalBrushableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin extends BlockEntity {
    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Redirect(
        method = "brushingCompleted",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState livingUnvanished$redirect$defaultBlockState(Block turnsInto, @Local ServerLevel level, @Local LivingEntity user, @Local ItemStack brush) {
        if (turnsInto instanceof ConditionalBrushableBlock conditionalBrushableBlock) {
            return conditionalBrushableBlock.getTurnedIntoState(getBlockState(), level, getBlockPos(), user, brush);
        }
        return turnsInto.defaultBlockState();
    }
}
