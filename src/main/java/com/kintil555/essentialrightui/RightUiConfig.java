package com.kintil555.essentialrightui;

/** Layout constants (GUI pixels). Edit here to tune the title screen. */
public final class RightUiConfig {
    private RightUiConfig() {}

    /** Left edge of the vanilla title-screen buttons. */
    public static final int LEFT_MARGIN = 24;

    /**
     * Gap between the screen's right edge and Essential's right column.
     * Must stay above 5 (Essential's own rightMenuMinPadding): at exactly 5 Essential's
     * "does it fit?" check flips to compact mode.
     */
    public static final int RIGHT_PADDING = 13;

    /** Gap between Essential's player/wardrobe column and its right sidebar. */
    public static final int PLAYER_GAP = 10;

    /** Vanilla title-screen main button width; the stock layout puts its left edge at width / 2 - 100. */
    public static final int VANILLA_BUTTON_HALF_WIDTH = 100;

    /** Essential's fullRightMenuPixelWidth (PauseMenuDisplay), used only as a fallback. */
    public static final int ESSENTIAL_RIGHT_MENU_WIDTH = 104;
}
