package dev.yuluo.mc.living_unvanished.block.entity;

import dev.yuluo.mc.living_unvanished.block.ConditionalBrushableBlock;
import dev.yuluo.mc.living_unvanished.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConditionalBrushableBlockEntity extends BrushableBlockEntity {
    public ConditionalBrushableBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(worldPosition, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntityTypes.CONDITIONAL_BRUSHABLE_BLOCK.get();
    }

    @Override
    public boolean brush(long gameTime, ServerLevel level, LivingEntity user, Direction direction, ItemStack brush) {
        BlockState state = this.getBlockState();
        if (state.getBlock() instanceof ConditionalBrushableBlock block
            && !block.isBrushable(state, level, this.getBlockPos(), direction, user, brush)) {
            return false;
        }

        return super.brush(gameTime, level, user, direction, brush);
    }
}
