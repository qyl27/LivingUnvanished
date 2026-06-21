package dev.yuluo.mc.living_unvanished.block;

import com.mojang.serialization.MapCodec;
import dev.yuluo.mc.living_unvanished.block.entity.MauritiusBluePigeonEggBlockEntity;
import dev.yuluo.mc.living_unvanished.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MauritiusBluePigeonEggBlock extends BaseEntityBlock {
    public static final MapCodec<MauritiusBluePigeonEggBlock> CODEC = simpleCodec(MauritiusBluePigeonEggBlock::new);
    private static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);

    public MauritiusBluePigeonEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MauritiusBluePigeonEggBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MauritiusBluePigeonEggBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.MAURITIUS_BLUE_PIGEON_EGG.get(), MauritiusBluePigeonEggBlockEntity::serverTick);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var state = this.defaultBlockState();
        if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
            return state;
        }
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var belowPos = pos.below();
        var belowState = level.getBlockState(belowPos);
        return Block.isFaceFull(belowState.getBlockSupportShape(level, belowPos), Direction.UP);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
