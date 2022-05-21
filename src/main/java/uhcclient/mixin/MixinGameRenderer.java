package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.Player;
import uhcclient.UHCClientMod;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(
        method = "method_1841(FL)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/GameRenderer;renderWeather(F)V",
            shift = At.Shift.AFTER
        )
    )
    private void renderWorldBorder(CallbackInfo ci) {
        Player player = MinecraftAccessor.getInstance().player;
        int renderDistance = 256 >> MinecraftAccessor.getInstance().options.viewDistance; // Screwy old view distance
        double distanceToBorder = Math.max(
            Math.max(
                UHCClientMod.worldBorder - player.x,
                player.x + UHCClientMod.worldBorder
            ),
            Math.max(
                UHCClientMod.worldBorder - player.z,
                player.z + UHCClientMod.worldBorder
            )
        );
        if (distanceToBorder > renderDistance) {
            return;
        }
        // GameRenderer renderer = (GameRenderer)(Object)this;
        Tessellator tessellator = Tessellator.INSTANCE;
        glPushMatrix();
        glEnable(GL_BLEND);
        glPopMatrix();
    }
}
