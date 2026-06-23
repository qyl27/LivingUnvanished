package dev.yuluo.mc.living_unvanished.client.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class LeftoverPageHelper {
    private static final int TEXT_WIDTH = BookViewScreen.TEXT_WIDTH;
    private static final int TEXT_HEIGHT = BookViewScreen.TEXT_HEIGHT;

    private LeftoverPageHelper() {
    }

    public static void open(String contentTranslationKey) {
        Minecraft.getInstance().setScreen(
            new BookViewScreen(new BookViewScreen.BookAccess(createPages(getFont(), contentTranslationKey)))
        );
    }

    private static Font getFont() {
        return Minecraft.getInstance().font;
    }

    private static List<Component> createPages(Font font, String key) {
        List<String> lines = splitLines(font, Component.translatable(key).getString());
        int linesPerPage = Math.max(1, TEXT_HEIGHT / font.lineHeight);

        int pagesCount = Math.ceilDiv(lines.size(), linesPerPage);
        List<Component> pages = new ArrayList<>();
        for (int page = 0; page < pagesCount; page += 1) {
            var firstLine = page * linesPerPage;
            var lastLine = Math.min(firstLine + linesPerPage, lines.size());
            pages.add(Component.literal(String.join(
                "\n",
                lines.subList(firstLine, lastLine)
            )));
        }

        if (pages.isEmpty()) {
            pages.add(Component.empty());
        }

        return pages;
    }

    private static List<String> splitLines(Font font, String text) {
        StringSplitter splitter = font.getSplitter();
        List<String> lines = new ArrayList<>();
        splitter.splitLines(
            text,
            TEXT_WIDTH,
            Style.EMPTY,
            false,
            (style, start, end) -> lines.add(text.substring(start, end))
        );
        return lines;
    }
}
