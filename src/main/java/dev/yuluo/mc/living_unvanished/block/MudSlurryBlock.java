package dev.yuluo.mc.living_unvanished.block;

import com.mojang.serialization.MapCodec;
import dev.yuluo.mc.living_unvanished.attachment.SuffocateInMud;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModDamageTypes;
import dev.yuluo.mc.living_unvanished.registry.ModItems;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class MudSlurryBlock extends Block implements BucketPickup {
    public static final MapCodec<MudSlurryBlock> CODEC = simpleCodec(MudSlurryBlock::new);

    private static final Vec3 STUCK_SPEED_MULTIPLIER = new Vec3(0.25, 0.05, 0.25);

    private static final float FALL_CAUSE_SMALL_SOUND_MIN_DISTANCE = 4;
    private static final float FALL_CAUSE_BIG_SOUND_MIN_DISTANCE = 7;

    private static final float FEATHER_FALLING_MIN_DISTANCE = 2.5F;

    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9F, 1.0);

    @Override
    public MapCodec<MudSlurryBlock> codec() {
        return CODEC;
    }

    public MudSlurryBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        // Todo: fog instead of showing all face.
//        return neighborState.is(this) || super.skipRendering(state, neighborState, direction);
        return false;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        entity.makeStuckInBlock(state, STUCK_SPEED_MULTIPLIER);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (fallDistance >= FALL_CAUSE_SMALL_SOUND_MIN_DISTANCE && entity instanceof LivingEntity livingEntity) {
            LivingEntity.Fallsounds fallsounds = livingEntity.getFallSounds();
            SoundEvent sound = fallDistance < FALL_CAUSE_BIG_SOUND_MIN_DISTANCE ? fallsounds.small() : fallsounds.big();
            entity.playSound(sound, 1, 1);
        }
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        VoxelShape collisionShape = this.getCollisionShape(state, level, pos, CollisionContext.of(entity));
        return collisionShape.isEmpty() ? Shapes.block() : collisionShape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!context.isPlacement() && context instanceof EntityCollisionContext entityCollisionContext) {
            var entity = entityCollisionContext.getEntity();
            if (entity != null && entity.fallDistance > FEATHER_FALLING_MIN_DISTANCE) {
                return FALLING_COLLISION_SHAPE;
            }
        }

        return Shapes.empty();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        if (!level.isClientSide()) {
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        }

        return new ItemStack(ModItems.MUD_SLURRY_BUCKET.get());
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    public static boolean isEntityInside(LivingEntity entity) {
        return entity.level()
            .getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ()))
            .is(ModBlocks.MUD_SLURRY.get());
    }
}
