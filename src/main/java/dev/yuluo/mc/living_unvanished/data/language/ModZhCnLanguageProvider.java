package dev.yuluo.mc.living_unvanished.data.language;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModEntityTypes;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import dev.yuluo.mc.living_unvanished.util.ModConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ModZhCnLanguageProvider extends LanguageProvider {
    public ModZhCnLanguageProvider(PackOutput output) {
        super(output, LivingUnvanished.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(ModConstants.Translations.CREATIVE_TAB_KEY, "方生未逝");

        addBlock(ModBlocks.SUSPICIOUS_MUD, "可疑的泥巴");
        addBlock(ModBlocks.SUSPICIOUS_RED_SAND, "可疑的红沙");
        addBlock(ModBlocks.SUSPICIOUS_ROTTEN_WOOD, "可疑的朽木");
        addBlock(ModBlocks.MUD_SLURRY, "泥浆");
        addBlock(ModBlocks.BLUE_PIGEON_EGG, "蓝鸠蛋");
        addBlock(ModBlocks.RESTORATION_TABLE, "复原台");

        addItem(ModItems.MUD_SLURRY_BUCKET, "泥浆桶");
        addItem(ModItems.STRANGE_SKULL, "奇异的头骨");
        addItem(ModItems.AVIAN_SKULL, "鸟类头骨");
        addItem(ModItems.BEAST_SKULL, "兽类头骨");
        addItem(ModItems.STRANGE_FEATHER, "奇异的羽毛");
        addItem(ModItems.BLUE_CONTOUR_FEATHER, "蓝色的正羽");
        addItem(ModItems.ORANGE_CONTOUR_FEATHER, "橙色的正羽");
        addItem(ModItems.STRANGE_BONES, "奇异的骨骼");
        addItem(ModItems.KEEL, "龙骨突");
        addItem(ModItems.RIBS, "肋骨");
        addItem(ModItems.FEMUR, "大腿骨");
        addItem(ModItems.STRANGE_LEATHER, "奇异的兽皮");
        addItem(ModItems.STRIPED_LEATHER, "条纹兽皮");
        addItem(ModItems.IDENTIFICATION_MANUAL, "鉴定手册");
        addItem(ModItems.LEFTOVER_PAGE, "残页");
        addItem(ModItems.MEMOIR, "回忆录");
        addItem(ModItems.BLUE_PIGEON_SPAWN_EGG, "蓝鸠刷怪蛋");

        addEntityType(ModEntityTypes.BLUE_PIGEON, "毛里求斯蓝鸠");
        addEntityType(ModEntityTypes.THYLACINE, "袋狼");

        add("death.attack.living_unvanished.suffocate_in_mud", "%1$s 陷入泥浆窒息了");
        add("death.attack.living_unvanished.suffocate_in_mud.player", "%1$s 在试图逃离 %2$s 时陷入泥浆窒息了");
    }
}
