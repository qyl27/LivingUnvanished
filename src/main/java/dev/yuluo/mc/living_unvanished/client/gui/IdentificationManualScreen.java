package dev.yuluo.mc.living_unvanished.client.gui;

import dev.yuluo.mc.living_unvanished.util.IdHelper;
import dev.yuluo.mc.living_unvanished.util.ModConstants;
import dev.yuluo.mc.living_unvanished.gui.IdentificationManualMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class IdentificationManualScreen extends AbstractContainerScreen<IdentificationManualMenu> {
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 174;
    private static final int BUTTON_X = 43;
    private static final int BUTTON_Y = 58;
    private static final int BUTTON_WIDTH = 32;
    private static final int BUTTON_HEIGHT = 11;
    private static final int BUTTON_TEXT_X = 44;
    private static final int BUTTON_TEXT_Y = 58;
    private static final int BUTTON_TEXT_WIDTH = 30;
    private static final int BUTTON_TEXT_HEIGHT = 9;
    private static final int TITLE_TEXT_X = 40;
    private static final int TITLE_TEXT_Y = 16;
    private static final int TITLE_TEXT_WIDTH = 36;
    private static final int TITLE_TEXT_HEIGHT = 10;

    private static final Identifier BACKGROUND = IdHelper.modLoc("textures/gui/identification_manual.png");

    private static final WidgetSprites IDENTIFY_BUTTON_SPRITES = new WidgetSprites(
        IdHelper.modLoc("button"),
        IdHelper.modLoc("button_disabled"),
        IdHelper.modLoc("button_hover")
    );

    private final Component titleText = Component.translatable(ModConstants.Translations.IDENTIFICATION_MANUAL)
        .withColor(0xFF404040)
        .withoutShadow();
    private final Component buttonText = Component.translatable(ModConstants.Translations.IDENTIFY_BUTTON)
        .withColor(0xFFFFFFFF)
        .withoutShadow();

    @Nullable
    private ImageButton identifyButton;

    public IdentificationManualScreen(IdentificationManualMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        identifyButton = new ImageButton(
            getLeftPos() + BUTTON_X,
            getTopPos() + BUTTON_Y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            IDENTIFY_BUTTON_SPRITES,
            button -> {
                if (this.getMinecraft().gameMode != null) {
                    this.getMinecraft().gameMode.handleInventoryButtonClick(this.menu.containerId, IdentificationManualMenu.IDENTIFY_BUTTON_ID);
                }
            },
            Component.translatable(ModConstants.Translations.IDENTIFY_BUTTON)
        );
        this.identifyButton.active = this.menu.canIdentify();
        this.addRenderableWidget(this.identifyButton);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.identifyButton != null) {
            this.identifyButton.active = this.menu.canIdentify();
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            BACKGROUND,
            this.leftPos,
            this.topPos,
            0,
            0,
            this.imageWidth,
            this.imageHeight,
            IMAGE_WIDTH,
            IMAGE_HEIGHT
        );
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        graphics.textRenderer().acceptScrollingWithDefaultCenter(titleText, TITLE_TEXT_X, TITLE_TEXT_X + TITLE_TEXT_WIDTH, TITLE_TEXT_Y, TITLE_TEXT_Y + TITLE_TEXT_HEIGHT);
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFF404040, false);
        graphics.textRenderer().acceptScrollingWithDefaultCenter(buttonText, BUTTON_TEXT_X, BUTTON_TEXT_X + BUTTON_TEXT_WIDTH, BUTTON_TEXT_Y, BUTTON_TEXT_Y + BUTTON_TEXT_HEIGHT);
    }
}
