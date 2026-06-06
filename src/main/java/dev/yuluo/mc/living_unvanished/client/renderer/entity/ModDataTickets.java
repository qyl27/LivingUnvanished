package dev.yuluo.mc.living_unvanished.client.renderer.entity;

import com.geckolib.constant.dataticket.DataTicket;
import net.minecraft.world.item.ItemStack;

public class ModDataTickets {
    public static final DataTicket<ItemStack> BODY_ARMOR_ITEM = DataTicket.create("body_armor_item", ItemStack.class);
    public static final DataTicket<ItemStack> HELD_ITEM = DataTicket.create("held_item", ItemStack.class);
    public static final DataTicket<Boolean> IS_BABY = DataTicket.create("is_baby", Boolean.class);
}
