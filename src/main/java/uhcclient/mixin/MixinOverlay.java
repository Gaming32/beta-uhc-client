package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glColor4f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.Overlay;
import net.minecraft.entity.LivingEntity;
import uhcclient.UHCClientMod;

@Mixin(Overlay.class)
public class MixinOverlay {
    @Redirect(
        method = "method_1945(FII)V",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
            ordinal = 0
        )
    )
    private void renderWorldBorderVignette(float red, float green, float blue, float alpha) {
        LivingEntity player = UHCClientMod.MINECRAFT.player;
        double worldBorder = UHCClientMod.getWorldBorder();
        double distanceToBorder = Math.min(
            Math.min(
                worldBorder - player.x,
                player.x + worldBorder
            ),
            Math.min(
                worldBorder - player.z,
                player.z + worldBorder
            )
        );
        double shrinkEffect = Math.min(
            UHCClientMod.getWorldBorderInterpSpeed() * 750_000.0,
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
