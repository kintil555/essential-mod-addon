package com.kintil555.essentialrightui;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads Essential's own "is the Essential menu layout on" switch
 * ({@code PauseMenuDisplay.Companion.isEnabled()}) by reflection, so this mod needs no
 * compile-time dependency on Essential's classes.
 */
public final class EssentialBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger("essentialrightui");

    private static Object companion;
    private static Method isEnabled;
    private static boolean resolved;
    private static boolean warned;

    private EssentialBridge() {}

    /** True when Essential draws its custom menu layout (config not OFF, mod enabled). */
    public static boolean isMenuLayoutEnabled() {
        if (!resolved) {
            resolve();
        }
        if (isEnabled == null) {
            return false;
        }
        try {
            return (boolean) isEnabled.invoke(companion);
        } catch (ReflectiveOperationException | RuntimeException e) {
            warnOnce("Essential isEnabled() call failed", e);
            return false;
        }
    }

    private static synchronized void resolve() {
        if (resolved) {
            return;
        }
        try {
            Class<?> display = Class.forName("gg.essential.handlers.PauseMenuDisplay");
            Object comp = display.getField("Companion").get(null);
            Method method = comp.getClass().getMethod("isEnabled");
            companion = comp;
            isEnabled = method;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            warnOnce("Could not resolve Essential PauseMenuDisplay.Companion.isEnabled; right-aligned layout disabled", e);
        }
        resolved = true;
    }

    private static void warnOnce(String message, Throwable t) {
        if (!warned) {
            warned = true;
            LOGGER.warn(message, t);
        }
    }
}
