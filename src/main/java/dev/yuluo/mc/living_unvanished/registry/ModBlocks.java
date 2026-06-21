package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.block.MauritiusBluePigeonEggBlock;
import dev.yuluo.mc.living_unvanished.block.MudSlurryBlock;
import dev.yuluo.mc.living_unvanished.block.RestorationTable;
import dev.yuluo.mc.living_unvanished.block.RotatedPillarBrushableBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static final DeferredBlock<BrushableBlock> SUSPICIOUS_MUD = REGISTRY.registerBlock(
        "suspicious_mud",
        prop -> new BrushableBlock(Blocks.MUD, SoundEvents.MUD_HIT, SoundEvents.MUD_HIT, prop),
        () -> suspiciousBlockProps()
            .mapColor(MapColor.TERRACOTTA_CYAN)
            .sound(SoundType.MUD)
    );
    public static final DeferredBlock<BrushableBlock> SUSPICIOUS_RED_SAND = REGISTRY.registerBlock(
        "suspicious_red_sand",
        prop -> new BrushableBlock(Blocks.RED_SAND, SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND, prop),
        () -> suspiciousBlockProps()
            .mapColor(MapColor.COLOR_ORANGE)
            .sound(SoundType.SUSPICIOUS_SAND)
    );
    public static final DeferredBlock<RotatedPillarBrushableBlock> SUSPICIOUS_ROTTEN_WOOD = REGISTRY.registerBlock(
        "suspicious_rotten_wood",
        prop -> new RotatedPillarBrushableBlock(Blocks.AIR, SoundEvents.WOOD_HIT, SoundEvents.WOOD_HIT, prop),
        () -> suspiciousBlockProps()
            .mapColor(MapColor.DIRT)
            .strength(0.2F)
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final DeferredBlock<MudSlurryBlock> MUD_SLURRY = REGISTRY.registerBlock(
        "mud_slurry",
        MudSlurryBlock::new,
        () -> BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_CYAN)
            .strength(0.5F)
            .sound(SoundType.MUD)
            .dynamicShape()
            .noOcclusion()
            .isRedstoneConductor(Blocks::never)
    );

    public static final DeferredBlock<MauritiusBluePigeonEggBlock> BLUE_PIGEON_EGG = REGISTRY.registerBlock(
        "mauritius_blue_pigeon_egg",
        MauritiusBluePigeonEggBlock::new,
        () -> BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOL)
            .forceSolidOn()
            .strength(0.5F)
            .sound(SoundType.METAL)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
    );

    public static final DeferredBlock<RestorationTable> RESTORATION_TABLE = REGISTRY.registerBlock(
        "restoration_table",
        RestorationTable::new,
        () -> BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.5F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    );

    private static BlockBehaviour.Properties suspiciousBlockProps() {
        return BlockBehaviour.Properties.of()
            .instrument(NoteBlockInstrument.SNARE)
            .strength(0.25F)
            .pushReaction(PushReaction.DESTROY);
    }
}
