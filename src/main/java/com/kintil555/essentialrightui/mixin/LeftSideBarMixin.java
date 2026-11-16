package com.kintil555.essentialrightui.mixin;

import com.kintil555.essentialrightui.RightUiLayout;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.dsl.BasicConstraintsKt;
import kotlin.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Essential's player + wardrobe column. Stock X: centred in the gap LEFT of the vanilla
 * buttons. Here: directly left of the right-hand column, so everything from Essential sits on
 * the right.
 */
@Mixin(targets = "gg.essential.gui.menu.LeftSideBar", remap = false)
public abstract class LeftSideBarMixin {

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void essentialrightui$moveRight(CallbackInfo ci) {
        if (!RightUiLayout.buildingForTitleScreen()) {
            return;
        }
        UIComponent self = (UIComponent) (Object) this;
        self.getConstraints().setX(BasicConstraintsKt.basicXConstraint(RightUiLayout::playerColumnX));

        // The parent is a 50%-wide container at x = 0. Our column now lies outside it, and
        // Elementa only hit-tests children inside their parent. Resize the parent once it is
        // attached so it spans exactly [column left, window right].
        boolean[] parentFixed = {false};
        self.addUpdateFunc((deltaTime, frames) -> {
            if (!parentFixed[0] && self.getHasParent()) {
                parentFixed[0] = true;
                UIComponent parent = self.getParent();
                parent.getConstraints().setX(
                        BasicConstraintsKt.basicXConstraint(c -> RightUiLayout.playerColumnX(self)));
                parent.getConstraints().setWidth(BasicConstraintsKt.basicWidthConstraint(c -> {
                    Window window = RightUiLayout.findWindow(self);
                    return window == null ? 0f : window.getRight() - RightUiLayout.playerColumnX(self);
                }));
            }
            return Unit.INSTANCE;
        });
    }
}
