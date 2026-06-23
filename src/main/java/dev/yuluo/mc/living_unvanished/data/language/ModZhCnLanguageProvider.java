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
        add(ModConstants.Translations.IDENTIFICATION_MANUAL, "鉴定手册");
        add(ModConstants.Translations.IDENTIFY_BUTTON, "鉴定");

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
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_1, "毛里求斯蓝鸠残页一");
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_2, "毛里求斯蓝鸠残页二");
        addItem(ModItems.MAURITIUS_BLUE_PIGEON_LEFTOVER_PAGE_3, "毛里求斯蓝鸠残页三");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_1, "袋狼残页一");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_2, "袋狼残页二");
        addItem(ModItems.THYLACINE_LEFTOVER_PAGE_3, "袋狼残页三");
        addItem(ModItems.MEMOIR, "回忆录");
        addItem(ModItems.BLUE_PIGEON_SPAWN_EGG, "蓝鸠刷怪蛋");

        add("leftover_page.living_unvanished.mauritius_blue_pigeon.1", "我们从一处热带岛屿的灾厄村民村落中发现一种从未发现过的鸟类尸体：\n大小颇似鸽子，有着银白色的头与红色的尾羽，更为瞩目的是漂亮的靛蓝色羽毛，随行的生物学者依此将它暂时命名为毛里求斯蓝鸠。");
        add("leftover_page.living_unvanished.mauritius_blue_pigeon.2", "今天带着几位绅士到岛上打了好几只蓝鸽子，我和他们一起分而食之，羽毛则由他们全部带走。\n奇怪的是，他们中的一位甚至连鸟肚子里的种子都要掏出来带走，说是什么古代植物的种子，真搞不懂这些老爷……");
        add("leftover_page.living_unvanished.mauritius_blue_pigeon.3", "越来越多的船闯到我们的岛上，那些人用冒火的铁杆把森林里的蓝鸟打下来，成堆地往大船上运。\n如今即便岛上最隐秘的森林中也再不能找到它们，我也快要忘记它们的叫声了……");
        add("leftover_page.living_unvanished.thylacine.1", "这片新土地上有一种虽然和野狗十分相似，但似乎完全不同的物种——它长着类似老虎的花纹和无毛的尾巴，以及类似袋鼠的育儿袋，鉴于此前没有相关文献，我把他们命名为“袋狼”。\n这些袋狼时常被成群的野狗追赶，抢夺食物，甚至被捕食，真是可怜。");
        add("leftover_page.living_unvanished.thylacine.2", "那个戴眼镜的小子竟然跟我说我那些长着虎纹的狼根本不会吃羊？真是胡扯！世上怎么会有不吃羊的狼。\n我得把这十里八乡的猎人和牧场主号召起来，一定要把这些胆敢靠近我们牧场的怪物杀光。");
        add("leftover_page.living_unvanished.thylacine.3", "公告\n由于饲养员玩忽职守，本动物园最重要的展品，珍稀动物袋狼，因长时间暴晒而死。\n相关责任人员已被解雇，为表歉意，我们决定：\n\n§b自今日起一周内本园的门票票价减半！§r");

        addEntityType(ModEntityTypes.BLUE_PIGEON, "毛里求斯蓝鸠");
        addEntityType(ModEntityTypes.THYLACINE, "袋狼");

        add("death.attack.living_unvanished.suffocate_in_mud", "%1$s 陷入泥浆窒息了");
        add("death.attack.living_unvanished.suffocate_in_mud.player", "%1$s 在试图逃离 %2$s 时陷入泥浆窒息了");
    }
}
