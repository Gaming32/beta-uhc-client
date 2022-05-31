package io.github.gaming32.uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.entity.PlayerRenderer;
import io.github.gaming32.uhcclient.UHCClientMod;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {
    @ModifyVariable(
        method = "method_340(Lnet/minecraft/entity/player/Player;DDD)V",
        at = @At("STORE"),
        ordinal = 0
    )
    private String renderDisplayName(String name) {
        return UHCClientMod.displayNames.getOrDefault(name, name);
    }
}
