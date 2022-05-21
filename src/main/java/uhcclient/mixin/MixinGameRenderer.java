package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
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
        double worldBorder = UHCClientMod.worldBorder;
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE); // We want both sides to render
        glEnable(GL_BLEND);
        glTranslated(-player.x, -player.y, -player.z);
        glColor4f(0.1254902f, 0.1254902f, 1.0f, 0.5f);
        glBegin(GL_QUADS);
        {
            glVertex3d(worldBorder, 0,   -worldBorder);
            glVertex3d(worldBorder, 128, -worldBorder);
            glVertex3d(worldBorder, 128,  worldBorder);
            glVertex3d(worldBorder, 0,    worldBorder);
        }
        {
            glVertex3d(-worldBorder, 0,   worldBorder);
            glVertex3d(-worldBorder, 128, worldBorder);
            glVertex3d(worldBorder,  128, worldBorder);
            glVertex3d(worldBorder,  0,   worldBorder);
        }
        {
            glVertex3d(-worldBorder, 0,   -worldBorder);
            glVertex3d(-worldBorder, 128, -worldBorder);
            glVertex3d(-worldBorder, 128,  worldBorder);
            glVertex3d(-worldBorder, 0,    worldBorder);
        }
        {
            glVertex3d(-worldBorder, 0,   -worldBorder);
            glVertex3d(-worldBorder, 128, -worldBorder);
            glVertex3d(worldBorder,  128, -worldBorder);
            glVertex3d(worldBorder,  0,   -worldBorder);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }
}
