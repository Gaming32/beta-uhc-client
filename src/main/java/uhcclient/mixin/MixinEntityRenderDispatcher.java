package uhcclient.mixin;

import static org.lwjgl.opengl.GL11.GL_GEQUAL;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDepthFunc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import uhcclient.UHCClientMod;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Redirect(
        method = "method_1920(Lnet/minecraft/entity/Entity;DDDFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;DDDFF)V"
        )
    )
    private void render(EntityRenderer renderer, Entity entity, double x, double y, double z, float yaw, float delta) {
        if (entity instanceof Player) {
            Integer glowingEffectO = UHCClientMod.glowingEffects.get(((Player)entity).name);
            if (glowingEffectO != null) {
                int glowingEffect = glowingEffectO.intValue();
                glDepthFunc(GL_GEQUAL);
                glColor4f(
                    (glowingEffect >> 16 & 0xff) / 255f,
                    (glowingEffect >> 8 & 0xff) / 255f,
                    (glowingEffect & 0xff) / 255f,
                    1f
                );
                renderer.render(entity, x, y, z, yaw, delta);
                glDepthFunc(GL_LEQUAL);
            }
        }

        renderer.render(entity, x, y, z, yaw, delta);
    }
}
