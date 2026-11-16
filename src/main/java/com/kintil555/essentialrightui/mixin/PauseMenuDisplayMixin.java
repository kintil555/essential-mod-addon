package com.kintil555.essentialrightui.mixin;

import com.kintil555.essentialrightui.RightUiConfig;
import com.kintil555.essentialrightui.RightUiLayout;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.WidthConstraint;
import gg.essential.elementa.constraints.XConstraint;
import gg.essential.elementa.dsl.BasicConstraintsKt;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Essential's menu layout host (shared by title screen and pause menu). */
@Mixin(targets = "gg.essential.handlers.PauseMenuDisplay", remap = false)
public abstract class PauseMenuDisplayMixin {

    /**
     * Stock: the right column sits just right of the centred vanilla buttons. Here: pinned to
     * the screen's right edge. The +1 over Essential's 5px min padding keeps its
     * "x + width + padding >= window right" compact-mode test false.
     */
    @Inject(method = "getRightSideMenuX", at = @At("HEAD"), cancellable = true, remap = false)
    private void essentialrightui$pinRight(Window window, UIContainer topButton, WidthConstraint width,
                                           CallbackInfoReturnable<XConstraint> cir) {
        if (!RightUiLayout.appliesToCurrentScreen()) {
            return;
        }
        cir.setReturnValue(BasicConstraintsKt.basicXConstraint(
                component -> window.getRight() - width.getWidth(component) - RightUiConfig.RIGHT_PADDING));
    }

    /** Marks the real title-screen layer build so LeftSideBar knows where it is being created. */
    @Inject(method = "initContent(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"), remap = false)
    private void essentialrightui$beginBuild(Screen screen, CallbackInfo ci) {
        RightUiLayout.beginBuild(screen);
    }

    @Inject(method = "initContent(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("RETURN"), remap = false)
    private void essentialrightui$endBuild(Screen screen, CallbackInfo ci) {
        RightUiLayout.endBuild();
    }
}
