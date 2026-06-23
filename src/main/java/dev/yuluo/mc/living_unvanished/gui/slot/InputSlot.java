package dev.yuluo.mc.living_unvanished.gui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class InputSlot extends Slot {
    private final Predicate<ItemStack> mayPlace;

    public InputSlot(Container container, int slot, int x, int y, Predicate<ItemStack> mayPlace) {
        super(container, slot, x, y);
        this.mayPlace = mayPlace;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return mayPlace.test(itemStack);
    }
}
