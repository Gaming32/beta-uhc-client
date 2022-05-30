package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glColor4f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Overlay;
import net.minecraft.entity.LivingEntity;
import uhcclient.UHCClientMod;

@Mixin(Overlay.class)
public class MixinOverlay {
    @Shadow
    private String jukeboxMessage = "";
    @Shadow
	private int jukeboxMessageTime = 0;
    @Shadow
	private boolean field_2551 = false;
    private boolean uhc$customJukeboxMessage = false;

    @Inject(
        method = "method_1946(FZII)V",
        at = @At("HEAD")
    )
    private void method_1946(CallbackInfo ci) {
        if (uhc$customJukeboxMessage ? jukeboxMessageTime < 21 : jukeboxMessageTime < 1) {
            if (UHCClientMod.customJukeboxMessage != null) {
                uhc$customJukeboxMessage = true;
                jukeboxMessage = UHCClientMod.customJukeboxMessage;
                jukeboxMessageTime = 21;
                field_2551 = false;
            }
        }
    }

    @Inject(
        method = "method_1952(Ljava/lang/String;)V",
        at = @At("HEAD")
    )
    private void method_1952(CallbackInfo ci) {
        uhc$customJukeboxMessage = false;
    }

    @Redirect(
        method = "method_1945(FII)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
            ordinal = 0
        )
    )
    private void renderWorldBorderVignette(float red, float green, float blue, float alpha) {
        double worldBorder = UHCClientMod.getWorldBorder();
        double distanceToBorder = UHCClientMod.getDistanceFromBorder();
        double shrinkEffect = Math.min(
            UHCClientMod.getWorldBorderInterpSpeed() * 250_000.0,
            Math.abs(UHCClientMod.getWorldBorderInterpDest() - worldBorder)
        );
        double distanceEffect = Math.max(5, shrinkEffect);
        double effect = distanceToBorder < distanceEffect ? 1.0 - ((double)distanceToBorder / distanceEffect) : 0.0;
        if (effect > 0.0) {
            effect = Math.min(effect, 1.0);
            glColor4d(0.0, effect, effect, 1.0);
        } else {
            glColor4f(red, green, blue, alpha);
        }
    }
}
