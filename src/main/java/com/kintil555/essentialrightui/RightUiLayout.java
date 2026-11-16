package com.kintil555.essentialrightui;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

/** Shared logic for the Essential mixins. Elementa only; no Essential classes referenced. */
public final class RightUiLayout {
    /**
     * Set while Essential builds the real (non-dummy) title-screen layer, so that components
     * created in that window know which screen they belong to.
     */
    private static final ThreadLocal<Boolean> BUILDING_TITLE_LAYER = new ThreadLocal<>();

    private RightUiLayout() {}

    public static boolean appliesTo(Screen screen) {
        return screen instanceof TitleScreen && EssentialBridge.isMenuLayoutEnabled();
    }

    /** Evaluated lazily (draw time): is the title screen the active screen right now? */
    public static boolean appliesToCurrentScreen() {
        Minecraft mc = Minecraft.getInstance();
        return mc != null && mc.gui != null && appliesTo(mc.gui.screen());
    }

    public static void beginBuild(Screen screen) {
        BUILDING_TITLE_LAYER.set(appliesTo(screen));
    }

    public static void endBuild() {
        BUILDING_TITLE_LAYER.remove();
    }

    /** True when the component being constructed right now belongs to the title screen. */
    public static boolean buildingForTitleScreen() {
        return Boolean.TRUE.equals(BUILDING_TITLE_LAYER.get());
    }

    public static Window findWindow(UIComponent component) {
        UIComponent current = component;
        while (!(current instanceof Window) && current.getHasParent()) {
            current = current.getParent();
        }
        return current instanceof Window window ? window : null;
    }

    /**
     * Left edge of Essential's right-hand button column (the RightSideBarNew component), found
     * through the Elementa tree so it also holds in Essential's compact layout. Falls back to
     * the full-width estimate before the tree is populated.
     */
    public static float rightColumnLeft(Window window) {
        for (UIComponent container : window.getChildren()) {
            for (UIComponent child : container.getChildren()) {
                if (child.getClass().getName().endsWith(".RightSideBarNew")) {
                    return child.getLeft();
                }
            }
        }
        return window.getRight() - RightUiConfig.RIGHT_PADDING - RightUiConfig.ESSENTIAL_RIGHT_MENU_WIDTH;
    }

    /** Absolute X for Essential's player/wardrobe column: directly left of the right column. */
    public static float playerColumnX(UIComponent playerColumn) {
        Window window = findWindow(playerColumn);
        if (window == null) {
            return playerColumn.getLeft();
        }
        return rightColumnLeft(window) - RightUiConfig.PLAYER_GAP - playerColumn.getWidth();
    }
}
