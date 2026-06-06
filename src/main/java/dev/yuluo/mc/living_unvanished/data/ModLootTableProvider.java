package dev.yuluo.mc.living_unvanished.data;

import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public final class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
            output,
            Set.of(),
            List.of(new SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
            registries
        );
    }

    private static final class ModBlockLootSubProvider extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.<Block>of()
            .map(ItemLike::asItem)
            .collect(Collectors.toSet());

        private ModBlockLootSubProvider(HolderLookup.Provider registries) {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            this.add(ModBlocks.MUD_SLURRY.get(), noDrop());
            this.dropWhenSilkTouch(ModBlocks.BLUE_PIGEON_EGG.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(ModBlocks.BLUE_PIGEON_EGG.get(), ModBlocks.MUD_SLURRY.get());
        }
    }
}
