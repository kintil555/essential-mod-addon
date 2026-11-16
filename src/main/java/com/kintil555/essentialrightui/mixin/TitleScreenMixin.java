package com.kintil555.essentialrightui.mixin;

import com.kintil555.essentialrightui.RightUiConfig;
import com.kintil555.essentialrightui.RightUiLayout;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Moves the vanilla title-screen buttons to the left edge. Priority 500 (Essential uses the
 * default 1000) so this TAIL handler runs BEFORE Essential's proxy handler reads the button
 * positions; Essential's containers then follow the moved buttons on their own.
 */
@Mixin(value = TitleScreen.class, priority = 500)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void essentialrightui$moveButtonsLeft(CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        if (!RightUiLayout.appliesTo(self)) {
            return;
        }
        int dx = RightUiConfig.LEFT_MARGIN - (self.width / 2 - RightUiConfig.VANILLA_BUTTON_HALF_WIDTH);
        if (dx == 0) {
            return;
        }
        for (GuiEventListener child : self.children()) {
            if (child instanceof AbstractWidget widget
                    && !child.getClass().getName().startsWith("gg.essential.")) {
                widget.setX(widget.getX() + dx);
            }
        }
    }
}
