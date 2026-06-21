package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final class Items {
        public static final TagKey<Item> BLUE_PIGEON_FOOD = create("mauritius_blue_pigeon_food");

        private static TagKey<Item> create(String path) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LivingUnvanished.MODID, path));
        }
    }

    public static final class Blocks {
        public static final TagKey<Block> BLUE_PIGEON_SEED_DIGGABLES = create("mauritius_blue_pigeon_seed_diggables");
        public static final TagKey<Block> BLUE_PIGEON_SUSPICIOUS_DIGGABLES = create("mauritius_blue_pigeon_suspicious_diggables");

        private static TagKey<Block> create(String path) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(LivingUnvanished.MODID, path));
        }
    }
}
