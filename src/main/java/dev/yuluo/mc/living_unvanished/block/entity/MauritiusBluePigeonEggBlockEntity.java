package dev.yuluo.mc.living_unvanished.block.entity;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.registry.ModBlockEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MauritiusBluePigeonEggBlockEntity extends BlockEntity {
    private static final int INCUBATION_TICKS = 12000;
    private static final int INCUBATION_LIGHT = 10;
    private static final String INCUBATION_TICKS_TAG = "IncubationTicks";

    private int incubationTicks;

    public MauritiusBluePigeonEggBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.MAURITIUS_BLUE_PIGEON_EGG.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MauritiusBluePigeonEggBlockEntity egg) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (level.getMaxLocalRawBrightness(pos) <= INCUBATION_LIGHT) {
            return;
        }

        egg.incubationTicks++;
        egg.setChanged();

        if (egg.incubationTicks >= INCUBATION_TICKS) {
            egg.hatch(serverLevel, pos, state);
        }
    }

    private void hatch(ServerLevel level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        level.removeBlock(pos, false);
        this.spawnBabyBluePigeon(level, pos);
    }

    private void spawnBabyBluePigeon(ServerLevel level, BlockPos pos) {
        BluePigeon pigeon = ModEntityTypes.BLUE_PIGEON.get().create(level, EntitySpawnReason.BREEDING);
        if (pigeon == null) {
            return;
        }

        pigeon.setBaby(true);
        pigeon.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(level.getRandom().nextFloat() * 360.0F), 0.0F);
        level.addFreshEntity(pigeon);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(INCUBATION_TICKS_TAG, this.incubationTicks);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.incubationTicks = input.getIntOr(INCUBATION_TICKS_TAG, 0);
    }
}
