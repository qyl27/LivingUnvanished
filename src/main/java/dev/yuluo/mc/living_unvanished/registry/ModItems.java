package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.item.IdentificationManualItem;
import dev.yuluo.mc.living_unvanished.item.LeftoverPageItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(LivingUnvanished.MODID);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static final DeferredItem<BlockItem> BLUE_PIGEON_EGG = REGISTRY.registerSimpleBlockItem(ModBlocks.BLUE_PIGEON_EGG);

    public static final DeferredItem<BlockItem> SUSPICIOUS_MUD = REGISTRY.registerSimpleBlockItem(ModBlocks.SUSPICIOUS_MUD);
    public static final DeferredItem<BlockItem> SUSPICIOUS_RED_SAND = REGISTRY.registerSimpleBlockItem(ModBlocks.SUSPICIOUS_RED_SAND);
    public static final DeferredItem<BlockItem> SUSPICIOUS_ROTTEN_WOOD = REGISTRY.registerSimpleBlockItem(ModBlocks.SUSPICIOUS_ROTTEN_WOOD);

    public static final DeferredItem<SolidBucketItem> MUD_SLURRY_BUCKET =
        REGISTRY.registerItem(
            "mud_slurry_bucket",
            properties -> new SolidBucketItem(ModBlocks.MUD_SLURRY.get(), SoundEvents.BUCKET_EMPTY, properties),
            properties -> properties.stacksTo(1).useItemDescriptionPrefix()
        );

    public static final DeferredItem<BlockItem> RESTORATION_TABLE = REGISTRY.registerSimpleBlockItem(ModBlocks.RESTORATION_TABLE);


    public static final DeferredItem<Item> STRANGE_SKULL = REGISTRY.registerSimpleItem("strange_skull");
    public static final DeferredItem<Item> AVIAN_SKULL = REGISTRY.registerSimpleItem("avian_skull");
    public static final DeferredItem<Item> BEAST_SKULL = REGISTRY.registerSimpleItem("beast_skull");

    public static final DeferredItem<Item> STRANGE_FEATHER = REGISTRY.registerSimpleItem("strange_feather");
    public static final DeferredItem<Item> BLUE_CONTOUR_FEATHER = REGISTRY.registerSimpleItem("blue_contour_feather");
    public static final DeferredItem<Item> ORANGE_CONTOUR_FEATHER = REGISTRY.registerSimpleItem("orange_contour_feather");

    public static final DeferredItem<Item> STRANGE_BONES = REGISTRY.registerSimpleItem("strange_bones");
    public static final DeferredItem<Item> KEEL = REGISTRY.registerSimpleItem("keel");
    public static final DeferredItem<Item> RIBS = REGISTRY.registerSimpleItem("ribs");
    public static final DeferredItem<Item> FEMUR = REGISTRY.registerSimpleItem("femur");

    public static final DeferredItem<Item> STRANGE_LEATHER = REGISTRY.registerSimpleItem("strange_leather");
    public static final DeferredItem<Item> STRIPED_LEATHER = REGISTRY.registerSimpleItem("striped_leather");

    public static final DeferredItem<IdentificationManualItem> IDENTIFICATION_MANUAL =
        REGISTRY.registerItem("identification_manual", IdentificationManualItem::new);

    public static final DeferredItem<LeftoverPageItem> MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_1 = registerLeftoverPage("mauritius_blue_pigeon", 1);
    public static final DeferredItem<LeftoverPageItem> MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_2 = registerLeftoverPage("mauritius_blue_pigeon", 2);
    public static final DeferredItem<LeftoverPageItem> MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_3 = registerLeftoverPage("mauritius_blue_pigeon", 3);
    public static final DeferredItem<LeftoverPageItem> THYLACINE_LEFTOVER_PAGE_1 = registerLeftoverPage("thylacine", 1);
    public static final DeferredItem<LeftoverPageItem> THYLACINE_LEFTOVER_PAGE_2 = registerLeftoverPage("thylacine", 2);
    public static final DeferredItem<LeftoverPageItem> THYLACINE_LEFTOVER_PAGE_3 = registerLeftoverPage("thylacine", 3);

    public static final DeferredItem<Item> MEMOIR = REGISTRY.registerSimpleItem("memoir");

    public static final DeferredItem<SpawnEggItem> BLUE_PIGEON_SPAWN_EGG =
        REGISTRY.registerItem(
            "mauritius_blue_pigeon_spawn_egg",
            SpawnEggItem::new,
            properties -> properties.spawnEgg(ModEntityTypes.BLUE_PIGEON.get())
        );

    private static DeferredItem<LeftoverPageItem> registerLeftoverPage(String species, int page) {
        var id = species + "_leftover_page_" + page;
        return REGISTRY.registerItem(id,
            prop -> new LeftoverPageItem(prop, species, + page)
        );
    }
}
