package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
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
        method = "method_1841(FJ)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/GameRenderer;renderWeather(F)V",
            shift = At.Shift.AFTER
        )
    )
    private void renderWorldBorder(CallbackInfo ci) {
        Player player = UHCClientMod.MINECRAFT.player;
        double worldBorder = UHCClientMod.getWorldBorder();
        glPushMatrix();
        glDisable(GL_CULL_FACE); // We want both sides to render
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glTranslated(-player.x, -player.y, -player.z);
        glBindTexture(GL_TEXTURE_2D, UHCClientMod.MINECRAFT.textureManager.getTextureId("/forcefield.png"));
        int stage = UHCClientMod.worldBorderInterpDir();
        if (stage == 1) {
            glColor3f(0.2509804f, 1.0f, 0.5019608f);
            // glColor4f(0.2509804f, 1.0f, 0.5019608f, 0.5f);
        } else if (stage == -1) {
            glColor3f(1.0f, 0.1882353f, 0.1882353f);
            // glColor4f(1.0f, 0.1882353f, 0.1882353f, 0.5f);
        } else {
            glColor3f(0.1254902f, 0.1254902f, 1.0f);
            // glColor4f(0.1254902f, 0.1254902f, 1.0f, 0.5f);
        }
        double yDiff = (System.currentTimeMillis() % 1000) / 1000.0;
        glBegin(GL_QUADS);
        {
            glTexCoord2d(0, 128 + yDiff);
            glVertex3d(worldBorder, 0,   -worldBorder);
            glTexCoord2d(0, 0 + yDiff);
            glVertex3d(worldBorder, 128, -worldBorder);
            glTexCoord2d(2 * worldBorder, 0 + yDiff);
            glVertex3d(worldBorder, 128,  worldBorder);
            glTexCoord2d(2 * worldBorder, 128 + yDiff);
            glVertex3d(worldBorder, 0,    worldBorder);
        }
        {
            glTexCoord2d(0, 128 + yDiff);
            glVertex3d(-worldBorder, 0,   worldBorder);
            glTexCoord2d(0, 0 + yDiff);
            glVertex3d(-worldBorder, 128, worldBorder);
            glTexCoord2d(2 * worldBorder, 0 + yDiff);
            glVertex3d(worldBorder,  128, worldBorder);
            glTexCoord2d(2 * worldBorder, 128 + yDiff);
            glVertex3d(worldBorder,  0,   worldBorder);
        }
        {
            glTexCoord2d(0, 128 + yDiff);
            glVertex3d(-worldBorder, 0,   -worldBorder);
            glTexCoord2d(0, 0 + yDiff);
            glVertex3d(-worldBorder, 128, -worldBorder);
            glTexCoord2d(2 * worldBorder, 0 + yDiff);
            glVertex3d(-worldBorder, 128,  worldBorder);
            glTexCoord2d(2 * worldBorder, 128 + yDiff);
            glVertex3d(-worldBorder, 0,    worldBorder);
        }
        {
            glTexCoord2d(0, 128 + yDiff);
            glVertex3d(-worldBorder, 0,   -worldBorder);
            glTexCoord2d(0, 0 + yDiff);
            glVertex3d(-worldBorder, 128, -worldBorder);
            glTexCoord2d(2 * worldBorder, 0 + yDiff);
            glVertex3d(worldBorder,  128, -worldBorder);
            glTexCoord2d(2 * worldBorder, 128 + yDiff);
            glVertex3d(worldBorder,  0,   -worldBorder);
        }
        glEnd();
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }
}
