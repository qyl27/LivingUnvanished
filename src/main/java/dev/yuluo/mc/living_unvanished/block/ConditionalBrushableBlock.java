package dev.yuluo.mc.living_unvanished.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.yuluo.mc.living_unvanished.block.entity.ConditionalBrushableBlockEntity;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ConditionalBrushableBlock extends BrushableBlock {
    public static final MapCodec<BrushableBlock> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            BrushableBlock.CODEC.forGetter(Function.identity())
        ).apply(instance, ConditionalBrushableBlock::new)
    );

    private ConditionalBrushableBlock(BrushableBlock brushable) {
        this(brushable.getTurnsInto(), brushable.getBrushSound(), brushable.getBrushCompletedSound(), brushable.properties());
    }

    public ConditionalBrushableBlock(Block turnsInto, SoundEvent brushSound, SoundEvent brushCompletedSound, BlockBehaviour.Properties properties) {
        super(turnsInto, brushSound, brushCompletedSound, properties);
    }

    @Override
    public MapCodec<BrushableBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ConditionalBrushableBlockEntity(worldPosition, blockState);
    }

    public boolean isBrushable(BlockState state, ServerLevel level, BlockPos pos, Direction direction, LivingEntity user, ItemStack brush) {
        return true;
    }

    public BlockState getTurnedIntoState(BlockState state, ServerLevel level, BlockPos pos, LivingEntity user, ItemStack brush) {
        return getTurnsInto().defaultBlockState();
    }
}
