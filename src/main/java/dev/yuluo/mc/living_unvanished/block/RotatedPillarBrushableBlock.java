package dev.yuluo.mc.living_unvanished.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RotatedPillarBrushableBlock extends ConditionalBrushableBlock {
    public static final MapCodec<BrushableBlock> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            BrushableBlock.CODEC.forGetter(Function.identity())
        ).apply(instance, RotatedPillarBrushableBlock::new)
    );

    private static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    private RotatedPillarBrushableBlock(BrushableBlock brushable) {
        this(brushable.getTurnsInto(), brushable.getBrushSound(),  brushable.getBrushCompletedSound(), brushable.properties());
    }

    public RotatedPillarBrushableBlock(Block turnsInto, SoundEvent brushSound, SoundEvent brushCompletedSound, BlockBehaviour.Properties properties) {
        super(turnsInto, brushSound, brushCompletedSound, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public MapCodec<BrushableBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.DUSTED, AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    @Override
    public boolean isBrushable(BlockState state, ServerLevel level, BlockPos pos, Direction direction, LivingEntity user, ItemStack brush) {
        return direction.getAxis() != state.getValue(AXIS);
    }

    @Override
    public BlockState getTurnedIntoState(BlockState state, ServerLevel level, BlockPos pos, LivingEntity user, ItemStack brush) {
        var result = super.getTurnedIntoState(state, level, pos, user, brush);
        if (result.hasProperty(AXIS)) {
            result = result.setValue(AXIS, state.getValue(AXIS));
        }
        return result;
    }
}
