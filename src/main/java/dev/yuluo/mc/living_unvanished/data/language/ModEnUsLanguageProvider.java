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
        add(ModConstants.Translations.IDENTIFICATION_MANUAL, "Identification Manual");
        add(ModConstants.Translations.IDENTIFY_BUTTON, "Identify");

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
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_1, "Mauritius Blue Pigeon Leftover Page I");
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_2, "Mauritius Blue Pigeon Leftover Page II");
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_3, "Mauritius Blue Pigeon Leftover Page III");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_1, "Thylacine Leftover Page I");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_2, "Thylacine Leftover Page II");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_3, "Thylacine Leftover Page III");
        addItem(ModItems.MEMOIR, "Memoir");
        addItem(ModItems.BLUE_PIGEON_SPAWN_EGG, "Mauritius Blue Pigeon Spawn Egg");

        add("leftover_page.living_unvanished.mauritius_blue_pigeon.1", "At an illager settlement on a tropical island, we discovered the carcass of a bird unlike any previously known:\nAbout the size of a pigeon, it had a silvery-white head and red tail feathers. Most striking of all was its beautiful indigo plumage, after which the biologist accompanying us provisionally named it the Mauritius blue pigeon.");
        add("leftover_page.living_unvanished.mauritius_blue_pigeon.2", "Today I took several gentlemen to the island, where we shot quite a few blue pigeons. We divided the birds among ourselves and ate them, while the gentlemen took all the feathers.\nStrangely, one of them even removed the seeds from the birds' stomachs to take with him, saying they came from some ancient plant. I will never understand these fine gentlemen...");
        add("leftover_page.living_unvanished.mauritius_blue_pigeon.3", "More and more ships are intruding upon our island. The men aboard use fire-spitting iron rods to shoot the blue birds out of the forests, then carry them away by the heap on their great ships.\nNow they can no longer be found even in the island's most secluded forests. I, too, am beginning to forget the sound of their calls...");
        add("leftover_page.living_unvanished.thylacine.1", "This new land is home to a species that closely resembles a wild dog, yet appears to be entirely distinct—it has tiger-like stripes, a hairless tail, and a pouch like a kangaroo's. As no literature has described it before, I have named it the ‘thylacine.’\nThese thylacines are often chased by packs of wild dogs, which steal their food and even prey upon them. Poor creatures.");
        add("leftover_page.living_unvanished.thylacine.2", "That bespectacled lad actually told me that those tiger-striped wolves of mine do not eat sheep? What nonsense! How could there be a wolf in this world that does not eat sheep?\nI must rally every hunter and rancher for miles around. We will wipe out these monsters that dare approach our pastures.");
        add("leftover_page.living_unvanished.thylacine.3", "Notice\nDue to a keeper's negligence, this zoo's most important exhibit—the rare thylacine—has died after prolonged exposure to the sun.\nThe staff responsible have been dismissed. By way of apology, we have decided that:\n\n§bAdmission will be half price for one week starting today!§r");

        addEntityType(ModEntityTypes.BLUE_PIGEON, "Mauritius Blue Pigeon");
        addEntityType(ModEntityTypes.THYLACINE, "Thylacine");

        add("death.attack.living_unvanished.suffocate_in_mud", "%1$s suffocated in mud slurry");
        add("death.attack.living_unvanished.suffocate_in_mud.player", "%1$s suffocated in mud slurry while trying to escape %2$s");
    }
}
