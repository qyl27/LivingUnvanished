package dev.yuluo.mc.living_unvanished.entity.ai.blue_pigeon;

import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.registry.ModTags;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class BluePigeonDigBehavior extends Behavior<BluePigeon> {
    private static final int SEARCH_RADIUS = 4;
    private static final int DIG_TICKS = 140;
    private static final int PECK_INTERVAL_TICKS = 200;
    private static final int SUSPICIOUS_BRUSHES_TO_FINISH = 10;
    private static final int SUSPICIOUS_BRUSH_INTERVAL_TICKS = 10;

    private @Nullable Target target;
    private int digTicks;
    private int peckTicks;

    public BluePigeonDigBehavior() {
        super(
            Map.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
            ),
            40,
            400
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BluePigeon pigeon) {
        if (!pigeon.canHoldItem() || pigeon.hasHeldItem() || pigeon.isOrderedToSit() || pigeon.isWaterEscapeFlying()) {
            return false;
        }

        this.target = this.findTarget(level, pigeon).orElse(null);
        return this.target != null;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BluePigeon pigeon, long timestamp) {
        return this.target != null
            && pigeon.canHoldItem()
            && !pigeon.hasHeldItem()
            && !pigeon.isOrderedToSit()
            && this.isTargetStillValid(level, this.target);
    }

    @Override
    protected void start(ServerLevel level, BluePigeon pigeon, long timestamp) {
        this.digTicks = 0;
        this.peckTicks = 0;
        this.moveToTarget(pigeon);
    }

    @Override
    protected void tick(ServerLevel level, BluePigeon pigeon, long timestamp) {
        if (this.target == null) {
            return;
        }

        if (!this.isCloseEnough(pigeon, this.target.approachPos())) {
            pigeon.setDigging(false);
            this.moveToTarget(pigeon);
            return;
        }

        pigeon.getNavigation().stop();
        if (this.target.markerOnly()) {
            this.tickMarker(level, pigeon);
        } else {
            this.tickDig(level, pigeon, timestamp);
        }
    }

    @Override
    protected void stop(ServerLevel level, BluePigeon pigeon, long timestamp) {
        pigeon.setDigging(false);
        this.target = null;
        this.digTicks = 0;
        this.peckTicks = 0;
    }

    private void tickMarker(ServerLevel level, BluePigeon pigeon) {
        this.peckTicks++;
        if (this.peckTicks >= PECK_INTERVAL_TICKS) {
            this.peckTicks = 0;
            pigeon.swing(InteractionHand.MAIN_HAND);
            BlockPos peckedPos = this.target.approachPos().below();
            level.levelEvent(2001, peckedPos, Block.getId(level.getBlockState(peckedPos)));
        }
    }

    private void tickDig(ServerLevel level, BluePigeon pigeon, long timestamp) {
        this.digTicks++;
        pigeon.setDigging(true);
        pigeon.swing(InteractionHand.MAIN_HAND);

        if (this.digTicks >= DIG_TICKS) {
            if (this.target.kind() == TargetKind.SEED) {
                pigeon.setHeldItem(new ItemStack(Items.PITCHER_POD));
                level.levelEvent(2001, this.target.pos(), Block.getId(level.getBlockState(this.target.pos())));
            } else {
                this.finishSuspiciousDig(level, pigeon);
            }

            this.target = null;
        }
    }

    private void finishSuspiciousDig(ServerLevel level, BluePigeon pigeon) {
        if (!(level.getBlockEntity(this.target.pos()) instanceof BrushableBlockEntity brushable)) {
            return;
        }

        Set<UUID> ignoredItems = this.getNearbyDroppedItemIds(level, this.target.pos());
        long gameTime = level.getGameTime();
        boolean finished = false;
        for (int brush = 0; brush < SUSPICIOUS_BRUSHES_TO_FINISH && !finished; brush++) {
            finished = brushable.brush(
                gameTime + (long) brush * SUSPICIOUS_BRUSH_INTERVAL_TICKS,
                level,
                pigeon,
                this.target.face(),
                Items.BRUSH.getDefaultInstance()
            );
        }

        if (finished) {
            this.takeNearestDroppedItem(level, pigeon, this.target.pos(), ignoredItems);
        }
    }

    private Set<UUID> getNearbyDroppedItemIds(ServerLevel level, BlockPos pos) {
        Set<UUID> ids = new HashSet<>();
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, this.itemSearchBox(pos), item -> !item.isRemoved())) {
            ids.add(item.getUUID());
        }

        return ids;
    }

    private void takeNearestDroppedItem(ServerLevel level, BluePigeon pigeon, BlockPos pos, Set<UUID> ignoredItems) {
        level.getEntitiesOfClass(
                ItemEntity.class,
                this.itemSearchBox(pos),
                item -> !item.isRemoved() && !item.getItem().isEmpty() && !ignoredItems.contains(item.getUUID())
            )
            .stream()
            .min(Comparator.comparingDouble(item -> item.distanceToSqr(pigeon)))
            .ifPresent(item -> {
                pigeon.setHeldItem(item.getItem().copy());
                item.discard();
            });
    }

    private AABB itemSearchBox(BlockPos pos) {
        return new AABB(pos).inflate(2.0);
    }

    private void moveToTarget(BluePigeon pigeon) {
        if (this.target != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(pigeon, this.target.approachPos(), 1.0F, 1);
        }
    }

    private boolean isCloseEnough(BluePigeon pigeon, BlockPos pos) {
        return pigeon.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 2.25;
    }

    private Optional<Target> findTarget(ServerLevel level, BluePigeon pigeon) {
        BlockPos center = pigeon.blockPosition();

        Optional<Target> suspicious = BlockPos.betweenClosedStream(center.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS), center.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))
            .map(BlockPos::immutable)
            .filter(pos -> level.getBlockState(pos).is(ModTags.Blocks.BLUE_PIGEON_SUSPICIOUS_DIGGABLES))
            .map(pos -> this.createSuspiciousTarget(level, pos))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .min(Comparator.comparingDouble(target -> target.pos().distSqr(center)));
        if (suspicious.isPresent()) {
            return suspicious;
        }

        return BlockPos.betweenClosedStream(center.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS), center.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))
            .map(BlockPos::immutable)
            .filter(pos -> level.getBlockState(pos).is(ModTags.Blocks.BLUE_PIGEON_SEED_DIGGABLES))
            .map(pos -> this.createSeedTarget(level, pos))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .min(Comparator.comparingDouble(target -> target.pos().distSqr(center)));
    }

    private Optional<Target> createSuspiciousTarget(ServerLevel level, BlockPos pos) {
        Optional<Direction> exposed = this.findExposedFace(level, pos);
        if (exposed.isPresent()) {
            Direction face = exposed.get();
            return Optional.of(new Target(pos, pos.relative(face), face, TargetKind.SUSPICIOUS, false));
        }

        return this.findMarkerApproachPos(level, pos)
            .map(approachPos -> new Target(pos, approachPos, Direction.UP, TargetKind.SUSPICIOUS, true));
    }

    private Optional<Target> createSeedTarget(ServerLevel level, BlockPos pos) {
        return this.findExposedFace(level, pos)
            .map(face -> new Target(pos, pos.relative(face), face, TargetKind.SEED, false));
    }

    private Optional<Direction> findExposedFace(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos adjacent = pos.relative(direction);
            if (this.isOpenAir(level, adjacent)) {
                return Optional.of(direction);
            }
        }

        return Optional.empty();
    }

    private Optional<BlockPos> findMarkerApproachPos(ServerLevel level, BlockPos pos) {
        for (int yOffset = 1; yOffset <= SEARCH_RADIUS + 1; yOffset++) {
            BlockPos approachPos = pos.above(yOffset);
            if (this.isStandingSpot(level, approachPos)) {
                return Optional.of(approachPos);
            }
        }

        return Optional.empty();
    }

    private boolean isStandingSpot(ServerLevel level, BlockPos pos) {
        if (!this.isOpenAir(level, pos)) {
            return false;
        }

        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return level.getFluidState(belowPos).isEmpty()
            && !below.getCollisionShape(level, belowPos).isEmpty();
    }

    private boolean isOpenAir(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir() && level.getFluidState(pos).isEmpty();
    }

    private boolean isTargetStillValid(ServerLevel level, Target target) {
        BlockState state = level.getBlockState(target.pos());
        return switch (target.kind()) {
            case SUSPICIOUS -> state.is(ModTags.Blocks.BLUE_PIGEON_SUSPICIOUS_DIGGABLES);
            case SEED -> state.is(ModTags.Blocks.BLUE_PIGEON_SEED_DIGGABLES);
        };
    }

    private enum TargetKind {
        SUSPICIOUS,
        SEED
    }

    private record Target(BlockPos pos, BlockPos approachPos, Direction face, TargetKind kind, boolean markerOnly) {
    }
}
