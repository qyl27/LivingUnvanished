package dev.yuluo.mc.living_unvanished.gui;

import dev.yuluo.mc.living_unvanished.gui.slot.InputSlot;
import dev.yuluo.mc.living_unvanished.gui.slot.OutputSlot;
import dev.yuluo.mc.living_unvanished.registry.ModMenuTypes;
import dev.yuluo.mc.living_unvanished.registry.ModTags;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import dev.yuluo.mc.living_unvanished.util.ItemStackHelper;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class IdentificationManualMenu extends AbstractContainerMenu {
    public static final int IDENTIFY_BUTTON_ID = 0;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_START = 1;
    public static final int OUTPUT_SLOT_COUNT = 6;
    public static final int OUTPUT_SLOT_COLUMN = 2;
    public static final int OUTPUT_SLOT_ROW = 3;
    public static final int OUTPUT_SLOT_END = OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT;
    public static final int INTERNAL_SLOT_COUNT = OUTPUT_SLOT_END;

    public static final int PLAYER_SLOT_START = INTERNAL_SLOT_COUNT;
    public static final int PLAYER_INVENTORY_SLOT_END = PLAYER_SLOT_START + 27;
    public static final int PLAYER_HOTBAR_SLOT_END = PLAYER_INVENTORY_SLOT_END + 9;

    public static final int INPUT_SLOT_X = 52;
    public static final int INPUT_SLOT_Y = 34;
    public static final int OUTPUT_BASE_X = 102;
    public static final int OUTPUT_BASE_Y = 16;
    public static final int PLAYER_INVENTORY_X = 8;
    public static final int PLAYER_INVENTORY_Y = 92;
    public static final int PLAYER_HOTBAR_X = 8;
    public static final int PLAYER_HOTBAR_Y = 150;

    private static final int SLOT_SPACING = 18;

    private final SimpleContainer container = new SimpleContainer(INTERNAL_SLOT_COUNT);
    private final DataSlot hasLootTable = DataSlot.standalone();
    private final Player owner;

    public IdentificationManualMenu(int containerId, Inventory inventory) {
        super(ModMenuTypes.IDENTIFICATION_MANUAL.get(), containerId);
        this.owner = inventory.player;

        this.addSlot(new InputSlot(this.container, INPUT_SLOT, INPUT_SLOT_X, INPUT_SLOT_Y, item -> item.is(ModTags.Items.IDENTIFIABLE)));
        for (var row = 0; row < OUTPUT_SLOT_ROW; row++) {
            for (var column = 0; column < OUTPUT_SLOT_COLUMN; column++) {
                var slotId = OUTPUT_SLOT_START + row * OUTPUT_SLOT_COLUMN + column;
                this.addSlot(new OutputSlot(this.container, slotId, OUTPUT_BASE_X + column * SLOT_SPACING, OUTPUT_BASE_Y + row * SLOT_SPACING));
            }
        }

        for (var row = 0; row < 3; row++) {
            for (var column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, PLAYER_INVENTORY_X + column * SLOT_SPACING, PLAYER_INVENTORY_Y + row * SLOT_SPACING));
            }
        }

        for (var column = 0; column < 9; column++) {
            this.addSlot(new Slot(inventory, column, PLAYER_HOTBAR_X + column * SLOT_SPACING, PLAYER_HOTBAR_Y));
        }

        this.addDataSlot(this.hasLootTable);
        this.updateHasLootTable();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != IDENTIFY_BUTTON_ID || !(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        var input = this.container.getItem(INPUT_SLOT);
        var lootTable = this.findLootTable(serverPlayer, input);
        if (input.isEmpty() || !input.is(ModTags.Items.IDENTIFIABLE) || this.outputsFull() || lootTable.isEmpty()) {
            return false;
        }

        input.shrink(1);
        this.container.setChanged();

        LootParams params = new LootParams.Builder(serverPlayer.level())
            .withLuck(serverPlayer.getLuck())
            .create(LootContextParamSets.EMPTY);
        for (var result : lootTable.get().getRandomItems(params)) {
            this.addOutputOrReturn(serverPlayer, result);
        }

        this.updateHasLootTable();
        this.broadcastChanges();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index >= OUTPUT_SLOT_START && index < OUTPUT_SLOT_END) {
            if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (index == INPUT_SLOT) {
            if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_HOTBAR_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= PLAYER_SLOT_START && index < PLAYER_HOTBAR_SLOT_END) {
            if (stack.is(ModTags.Items.IDENTIFIABLE) && this.moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                // Moved into the input slot.
            } else if (index < PLAYER_INVENTORY_SLOT_END) {
                if (!this.moveItemStackTo(stack, PLAYER_INVENTORY_SLOT_END, PLAYER_HOTBAR_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_INVENTORY_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        this.updateHasLootTable();
        return original;
    }

    @Override
    public void broadcastChanges() {
        this.updateHasLootTable();
        super.broadcastChanges();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player instanceof ServerPlayer) {
            this.clearContainer(player, this.container);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean canIdentify() {
        var input = this.container.getItem(INPUT_SLOT);
        return !input.isEmpty() && input.is(ModTags.Items.IDENTIFIABLE) && !this.outputsFull() && this.hasLootTable();
    }

    private boolean hasLootTable() {
        return this.hasLootTable.get() != 0;
    }

    // region Server side

    private void updateHasLootTable() {
        if (this.owner instanceof ServerPlayer serverPlayer) {
            this.hasLootTable.set(this.findLootTable(serverPlayer, this.container.getItem(INPUT_SLOT)).isPresent() ? 1 : 0);
        }
    }

    private Optional<LootTable> findLootTable(ServerPlayer player, ItemStack input) {
        if (input.isEmpty() || !input.is(ModTags.Items.IDENTIFIABLE)) {
            return Optional.empty();
        }

        var server = player.level().getServer();

        ResourceKey<LootTable> key = createIdentifyLootTableKey(input);
        return server.reloadableRegistries()
            .lookup()
            .lookup(Registries.LOOT_TABLE)
            .flatMap(lookup -> lookup.get(key))
            .map(Holder.Reference::value);
    }

    private void addOutputOrReturn(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        var remainder = this.insertIntoOutputs(stack);
        if (!remainder.isEmpty()) {
            returnOrDrop(player, remainder);
        }
    }

    private ItemStack insertIntoOutputs(ItemStack stack) {
        var remainder = stack.copy();
        for (var slot = OUTPUT_SLOT_START; slot < OUTPUT_SLOT_END && !remainder.isEmpty(); slot++) {
            var existing = this.container.getItem(slot);
            var beforeCount = remainder.getCount();
            remainder = ItemStackHelper.mergeInto(existing, remainder, this.getSlot(slot).getMaxStackSize(remainder));
            if (remainder.getCount() != beforeCount) {
                this.container.setChanged();
            }
        }

        for (var slot = OUTPUT_SLOT_START; slot < OUTPUT_SLOT_END && !remainder.isEmpty(); slot++) {
            if (!this.container.getItem(slot).isEmpty()) {
                continue;
            }

            var maxStackSize = Math.min(remainder.getMaxStackSize(), this.getSlot(slot).getMaxStackSize(remainder));
            this.container.setItem(slot, remainder.split(Math.min(remainder.getCount(), maxStackSize)));
            this.container.setChanged();
        }
        return remainder;
    }

    private int firstEmptyOutputSlot() {
        for (var slot = OUTPUT_SLOT_START; slot < OUTPUT_SLOT_END; slot++) {
            if (this.container.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    private boolean outputsFull() {
        return this.firstEmptyOutputSlot() < 0;
    }

    private static void returnOrDrop(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        if (!player.getInventory().add(stack) && !stack.isEmpty()) {
            player.drop(stack, false);
        }
    }

    // endregion

    private static ResourceKey<LootTable> createIdentifyLootTableKey(ItemStack stack) {
        var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return ResourceKey.create(
            Registries.LOOT_TABLE,
            IdHelper.modLoc("identify/" + IdHelper.toUnderlinedString(itemId))
        );
    }
}
