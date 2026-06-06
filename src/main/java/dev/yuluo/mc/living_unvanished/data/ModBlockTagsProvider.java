package dev.yuluo.mc.living_unvanished.data;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

public final class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, LivingUnvanished.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Blocks.BLUE_PIGEON_SEED_DIGGABLES)
            .add(Blocks.MUD);
        tag(ModTags.Blocks.BLUE_PIGEON_SUSPICIOUS_DIGGABLES)
            .add(
                Blocks.SUSPICIOUS_SAND,
                Blocks.SUSPICIOUS_GRAVEL,
                ModBlocks.SUSPICIOUS_MUD.get(),
                ModBlocks.SUSPICIOUS_RED_SAND.get(),
                ModBlocks.SUSPICIOUS_ROTTEN_WOOD.get()
            );
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(ModBlocks.SUSPICIOUS_MUD.get(), ModBlocks.SUSPICIOUS_RED_SAND.get(), ModBlocks.MUD_SLURRY.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(ModBlocks.SUSPICIOUS_ROTTEN_WOOD.get());
    }
}
