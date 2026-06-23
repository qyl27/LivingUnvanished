package dev.yuluo.mc.living_unvanished.data;

import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
            output,
            Set.of(),
            List.of(
                new SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ModIdentifyLootSubProvider::new, LootContextParamSets.EMPTY)
            ),
            registries
        );
    }

    private static final class ModIdentifyLootSubProvider implements LootTableSubProvider {
        private static final ResourceKey<LootTable> STRANGE_SKULL =
            ResourceKey.create(Registries.LOOT_TABLE, IdHelper.modLoc("identify/living_unvanished_strange_skull"));
        private static final ResourceKey<LootTable> STRANGE_FEATHER =
            ResourceKey.create(Registries.LOOT_TABLE, IdHelper.modLoc("identify/living_unvanished_strange_feather"));
        private static final ResourceKey<LootTable> STRANGE_BONES =
            ResourceKey.create(Registries.LOOT_TABLE, IdHelper.modLoc("identify/living_unvanished_strange_bones"));
        private static final ResourceKey<LootTable> STRANGE_LEATHER =
            ResourceKey.create(Registries.LOOT_TABLE, IdHelper.modLoc("identify/living_unvanished_strange_leather"));

        private ModIdentifyLootSubProvider(HolderLookup.Provider registries) {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            output.accept(
                STRANGE_SKULL,
                identifyTable(
                    LootItem.lootTableItem(Items.BONE_BLOCK).setWeight(2),
                    LootItem.lootTableItem(ModItems.AVIAN_SKULL.get()).setWeight(1),
                    LootItem.lootTableItem(ModItems.BEAST_SKULL.get()).setWeight(1)
                )
            );
            output.accept(
                STRANGE_FEATHER,
                identifyTable(
                    LootItem.lootTableItem(Items.FEATHER).setWeight(2),
                    LootItem.lootTableItem(ModItems.BLUE_CONTOUR_FEATHER.get()).setWeight(1),
                    LootItem.lootTableItem(ModItems.ORANGE_CONTOUR_FEATHER.get()).setWeight(1)
                )
            );
            output.accept(
                STRANGE_BONES,
                identifyTable(
                    LootItem.lootTableItem(Items.BONE).setWeight(10),
                    LootItem.lootTableItem(ModItems.KEEL.get()).setWeight(4),
                    LootItem.lootTableItem(ModItems.FEMUR.get()).setWeight(3),
                    LootItem.lootTableItem(ModItems.RIBS.get()).setWeight(3)
                )
            );
            output.accept(
                STRANGE_LEATHER,
                identifyTable(
                    LootItem.lootTableItem(Items.LEATHER).setWeight(2),
                    LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(1),
                    LootItem.lootTableItem(ModItems.STRIPED_LEATHER.get()).setWeight(1)
                )
            );
        }

        @SafeVarargs
        private static LootTable.Builder identifyTable(LootPoolEntryContainer.Builder<?>... entries) {
            LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F));
            for (LootPoolEntryContainer.Builder<?> entry : entries) {
                pool.add(entry);
            }
            return LootTable.lootTable().withPool(pool);
        }
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
