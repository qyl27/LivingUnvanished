package dev.yuluo.mc.living_unvanished.util;

import net.minecraft.world.item.ItemStack;

public final class ItemStackHelper {
    private ItemStackHelper() {
    }

    public static boolean canMerge(ItemStack target, ItemStack source) {
        return canMerge(target, source, target.getMaxStackSize());
    }

    public static boolean canMerge(ItemStack target, ItemStack source, int maxStackSize) {
        return !target.isEmpty()
            && !source.isEmpty()
            && ItemStack.isSameItemSameComponents(target, source)
            && target.getCount() < effectiveMaxStackSize(target, maxStackSize);
    }

    public static ItemStack mergeInto(ItemStack target, ItemStack source) {
        return mergeInto(target, source, target.getMaxStackSize());
    }

    public static ItemStack mergeInto(ItemStack target, ItemStack source, boolean simulate) {
        return mergeInto(target, source, target.getMaxStackSize(), simulate);
    }

    public static ItemStack mergeInto(ItemStack target, ItemStack source, int maxStackSize) {
        return mergeInto(target, source, maxStackSize, false);
    }

    public static ItemStack mergeInto(ItemStack target, ItemStack source, int maxStackSize, boolean simulate) {
        if (!canMerge(target, source, maxStackSize)) {
            return source.copy();
        }

        var moveCount = Math.min(source.getCount(), effectiveMaxStackSize(target, maxStackSize) - target.getCount());
        if (!simulate) {
            target.grow(moveCount);
        }

        var remainingCount = source.getCount() - moveCount;
        if (remainingCount <= 0) {
            return ItemStack.EMPTY;
        }

        var remainder = source.copy();
        remainder.setCount(remainingCount);
        return remainder;
    }

    private static int effectiveMaxStackSize(ItemStack stack, int maxStackSize) {
        return Math.min(stack.getMaxStackSize(), maxStackSize);
    }
}
