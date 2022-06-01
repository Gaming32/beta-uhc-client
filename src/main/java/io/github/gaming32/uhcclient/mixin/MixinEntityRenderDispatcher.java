package io.github.gaming32.uhcclient.mixin;

import io.github.gaming32.uhcclient.UHCClientMod;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.lwjgl.opengl.GL11.*;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
}
