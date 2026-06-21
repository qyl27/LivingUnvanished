package dev.yuluo.mc.living_unvanished.data.language;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import dev.yuluo.mc.living_unvanished.util.ModConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ModEnUsLanguageProvider extends LanguageProvider {
    public ModEnUsLanguageProvider(PackOutput output) {
        super(output, LivingUnvanished.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModConstants.Translations.CREATIVE_TAB_KEY, "Living Unvanished");

        addBlock(ModBlocks.SUSPICIOUS_MUD, "Suspicious Mud");
        addBlock(ModBlocks.SUSPICIOUS_RED_SAND, "Suspicious Red Sand");
        addBlock(ModBlocks.SUSPICIOUS_ROTTEN_WOOD, "Suspicious Rotten Wood");
        addBlock(ModBlocks.MUD_SLURRY, "Mud Slurry");
        addBlock(ModBlocks.BLUE_PIGEON_EGG, "Mauritius Blue Pigeon Egg");
        addBlock(ModBlocks.RESTORATION_TABLE, "Restoration Table");

        addItem(ModItems.MUD_SLURRY_BUCKET, "Mud Slurry Bucket");
        addItem(ModItems.STRANGE_SKULL, "Strange Skull");
        addItem(ModItems.AVIAN_SKULL, "Avian Skull");
        addItem(ModItems.BEAST_SKULL, "Beast Skull");
        addItem(ModItems.STRANGE_FEATHER, "Strange Feather");
        addItem(ModItems.BLUE_CONTOUR_FEATHER, "Blue Contour Feather");
        addItem(ModItems.ORANGE_CONTOUR_FEATHER, "Orange Contour Feather");
        addItem(ModItems.STRANGE_BONES, "Strange Bones");
        addItem(ModItems.KEEL, "Keel");
        addItem(ModItems.RIBS, "Ribs");
        addItem(ModItems.FEMUR, "Femur");
        addItem(ModItems.STRANGE_LEATHER, "Strange Leather");
        addItem(ModItems.STRIPED_LEATHER, "Striped Leather");
        addItem(ModItems.IDENTIFICATION_MANUAL, "Identification Manual");
        addItem(ModItems.LEFTOVER_PAGE, "Leftover Page");
        addItem(ModItems.MEMOIR, "Memoir");
        addItem(ModItems.BLUE_PIGEON_SPAWN_EGG, "Mauritius Blue Pigeon Spawn Egg");

        addEntityType(ModEntityTypes.BLUE_PIGEON, "Mauritius Blue Pigeon");
        addEntityType(ModEntityTypes.THYLACINE, "Thylacine");

        add("death.attack.living_unvanished.suffocate_in_mud", "%1$s suffocated in mud slurry");
        add("death.attack.living_unvanished.suffocate_in_mud.player", "%1$s suffocated in mud slurry while trying to escape %2$s");
    }
}
