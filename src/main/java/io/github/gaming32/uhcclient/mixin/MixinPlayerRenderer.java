package io.github.gaming32.uhcclient.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.entity.PlayerRenderer;
import io.github.gaming32.uhcclient.UHCClientMod;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer {
    public MixinPlayerRenderer(EntityModel arg, float f) {
        super(arg, f);
    }

    @Unique
    private final ThreadLocal<Boolean> disableNametag = new ThreadLocal<>();

    @ModifyVariable(
        method = "method_340(Lnet/minecraft/entity/player/Player;DDD)V",
        at = @At("STORE"),
        ordinal = 0
    )
    private String renderDisplayName(String name) {
        return UHCClientMod.displayNames.getOrDefault(name, name);
    }

    @Inject(at = @At("HEAD"), method=  "method_340(Lnet/minecraft/entity/player/Player;DDD)V", cancellable = true)
    private void onRenderName(Player player, double x, double y, double z, CallbackInfo ci) {
        if (disableNametag.get()) {
            ci.cancel();
        }
    }


    @Redirect(
        method = "method_341",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;method_822(Lnet/minecraft/entity/LivingEntity;DDDFF)V"
        )
    )
    private void render(LivingEntityRenderer instance, LivingEntity d, double d1, double d2, double f, float f1, float v) {
            Integer glowingEffectO = UHCClientMod.glowingEffects.get(((Player) d).name);
            disableNametag.set(false);
            if (glowingEffectO != null) {
                // Push the GL attribute bits so that we don't wreck any settings
                glPushAttrib(GL_ALL_ATTRIB_BITS);
                // Enable polygon offsets, and offset filled polygons forward by 2.5
                glEnable(GL_POLYGON_OFFSET_FILL);
                glDisable(GL_DEPTH_TEST);
                glPolygonOffset(-5f, -5f);
                // Set the render mode to be line rendering with a thick line width
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                glLineWidth(6.0f);
                // Set the colour to be white
                glColor3f(1.0f, 0.0f, 0.0f);
                glDisable(GL_TEXTURE_2D);
                // Render the object
                disableNametag.set(true);
                super.method_822(d, d1, d2, f, f1, v);
                disableNametag.set(false);
                // Set the polygon mode to be filled triangles
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                glEnable(GL_LIGHTING);
                glEnable(GL_TEXTURE_2D);
                glEnable(GL_DEPTH_TEST);
                // Set the colour to the background
                glColor3f(1.0f, 1.0f, 1.0f);
                // Render the object
                super.method_822(d, d1, d2, f, f1, v);
                // Pop the state changes off the attribute stack
                // to set things back how they were
                glPopAttrib();

                //        renderer.render(entity, x, y, z, yaw, delta);
            } else {
                super.method_822(d, d1, d2, f, f1, v);
            }
    }
}
